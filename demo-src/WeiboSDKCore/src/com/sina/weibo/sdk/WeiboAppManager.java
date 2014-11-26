package com.sina.weibo.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.sina.weibo.sdk.utils.LogUtil;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

public class WeiboAppManager {

    private static final String TAG = WeiboAppManager.class.getName();
    
    /** Provider，用来给SDK提供微博数据 **/
    private static final Uri WEIBO_NAME_URI = Uri.parse("content://com.sina.weibo.sdkProvider/query/package");
    /** 用来标示微博，方便SDK根据此标识进行查找 **/
    private static final String WEIBO_IDENTITY_ACTION = "com.sina.weibo.action.sdkidentity";
    /** 微博assets目录下的文件，用来标识微博的版本信息 **/
    private static final String SDK_INT_FILE_NAME = "weibo_for_sdk.json";
    
    
    private static WeiboAppManager sInstance;
    private Context mContext;
    /**
     * 微博客户端基本信息类。
     */
    public static class WeiboInfo {
        private String mPackageName;
        private int mSupportApi;
        
        private void setPackageName(String packageName) {
            this.mPackageName = packageName;
        }
        public String getPackageName() {
            return this.mPackageName;
        }
        private void setSupportApi(int supportApi) {
            this.mSupportApi = supportApi;
        }
        public int getSupportApi() {
            return this.mSupportApi;
        }
        
        public boolean isLegal() {
            if (!TextUtils.isEmpty(mPackageName) && mSupportApi > 0) {
                return true;
            } else {
                return false;
            }
        }
        
        @Override
        public String toString() {
            return "WeiboInfo: PackageName = " + mPackageName + ", supportApi = " + mSupportApi;
        }
    }
    
    private WeiboAppManager(Context context) {
        this.mContext = context.getApplicationContext();
    }
    
    public static synchronized WeiboAppManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WeiboAppManager(context);
        }
        return sInstance;
    }
    
    /**
     * 查询微博客户端信息。
     * 
     * @param mContext 应用程序上下文环境
     * @return 博客户端信息实例
     */
    public synchronized WeiboInfo getWeiboInfo() {
        return queryWeiboInfoInternal(mContext);
    }

    private WeiboInfo queryWeiboInfoInternal(Context context) {
        WeiboInfo winfo1 = queryWeiboInfoByProvider(context);
        WeiboInfo winfo2 = queryWeiboInfoByAsset(context);
        
        boolean hasWinfo1 = (winfo1 != null);
        boolean hasWinfo2 = (winfo2 != null);
        if (hasWinfo1 && hasWinfo2) {
            if (winfo1.getSupportApi() >= winfo2.getSupportApi()) {
                return winfo1;
            } else {
                return winfo2;
            }
        } else {
            if (hasWinfo1) {
                return winfo1;
            }
            if (hasWinfo2) {
                return winfo2;
            }
        }
        
        return null;
    }
    
    /**
     * 通过 ContentProvider 查询微博信息。
     * 
     * @param context 应用程序上下文环境
     * @return 博客户端信息实例
     */
    private WeiboInfo queryWeiboInfoByProvider(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = cr.query(WEIBO_NAME_URI, null, null, null, null);
            if (null == cursor) {
                return null;
            }
            
            int supportApiIndex = cursor.getColumnIndex("support_api");
            int packageIndex = cursor.getColumnIndex("package");
            if (cursor.moveToFirst()) {
                int supportApiInt = -1;
                String supportApi = cursor.getString(supportApiIndex);
                try {
                    supportApiInt = Integer.parseInt(supportApi);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
    
                String packageName = cursor.getString(packageIndex);
                if (!TextUtils.isEmpty(packageName) && ApiUtils.validateWeiboSign(context, packageName)) {
                    WeiboInfo winfo = new WeiboInfo();
                    winfo.setPackageName(packageName);
                    winfo.setSupportApi(supportApiInt);
                    return winfo;
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return null;
    }
    
    
    /**
     * 通过 assets/weibo_for_sdk.json 文件获取微博信息。
     * 
     * @param context 应用程序上下文环境
     * @return 博客户端信息实例
     */
    private WeiboInfo queryWeiboInfoByAsset(Context context) {
        Intent intent = new Intent(WEIBO_IDENTITY_ACTION);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> list = context.getPackageManager().queryIntentServices(intent, 0);
        if (null == list || list.isEmpty()) {
            return null;
        }
        
        WeiboInfo weiboInfo = null;
        for (ResolveInfo ri : list) {
            if (null == ri.serviceInfo
                    || null == ri.serviceInfo.applicationInfo
                    || TextUtils.isEmpty(ri.serviceInfo.applicationInfo.packageName)) {
                continue;
            }
            String packageName = ri.serviceInfo.applicationInfo.packageName;
            WeiboInfo tmpWeiboInfo = parseWeiboInfoByAsset(packageName);
            if (tmpWeiboInfo != null) {
                if (weiboInfo == null) {
                    weiboInfo = tmpWeiboInfo;
                } else {
                    if (weiboInfo.getSupportApi() < tmpWeiboInfo.getSupportApi()) {
                        weiboInfo = tmpWeiboInfo;
                    }
                }
            }
        }
        return weiboInfo;
    }
    
    
    /**
     * 解析微博 assets/weibo_for_sdk.json 文件，获取微博信息
     * 
     * @param mContext     应用程序上下文环境
     * @param packageName 指定应用包名
     * 
     * @return 博客户端信息实例
     */
    public WeiboInfo parseWeiboInfoByAsset(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        InputStream is = null;
        try {
            Context weiboContext = 
                    mContext.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY);

            final int bufferSize = 4096;
            byte[] buf = new byte[bufferSize];
            int readNum;
            is = weiboContext.getAssets().open(SDK_INT_FILE_NAME);
            StringBuilder sbContent = new StringBuilder();

            while ((readNum = is.read(buf, 0, bufferSize)) != -1) {
                sbContent.append(new String(buf, 0, readNum));
            }
            if (TextUtils.isEmpty(sbContent.toString()) || !ApiUtils.validateWeiboSign(mContext, packageName)) {
                return null;
            }
            JSONObject json = new JSONObject(sbContent.toString());
            int supportApi = json.optInt("support_api", -1);

            WeiboInfo winfo = new WeiboInfo();
            winfo.setPackageName(packageName);
            winfo.setSupportApi(supportApi);
            return winfo;

        } catch (NameNotFoundException e) {
            LogUtil.e(TAG, e.getMessage());
        } catch (IOException e) {
            LogUtil.e(TAG, e.getMessage());
        } catch (JSONException e) {
            LogUtil.e(TAG, e.getMessage());
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LogUtil.e(TAG, e.getMessage());
                }
            }
        }
        return null;
    }
}
