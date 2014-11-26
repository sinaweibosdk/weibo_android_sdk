package com.sina.weibo.sdk;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Pair;

import com.sina.weibo.sdk.WeiboAppManager.WeiboInfo;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.DownloadService;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.MD5;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.sina.weibo.sdk.utils.NotificationHelper;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 功能：在三方应用进行网页授权时，激活安装微博客户端
 * @author guizhong
 * @since 2014/11/12
 */
public class WbAppInstallActivator {
    
    private final static String TAG = WbAppInstallActivator.class.getName();
    
    public final static String WB_APK_FILE_DIR = Environment.getExternalStorageDirectory()+"/Android/org_share_data/";
    
    private Context mContext;
    
    private static WbAppInstallActivator mInstance;
    
    private CountDownLatch mCountDownlatch;
    
    private NotificationInfo mNotificationInfo;
    
    private String mAppkey;
    
    private boolean isFree = true;
    
    private WbAppInstallActivator(Context ctx, String appkey){
        this.mContext = ctx.getApplicationContext();
        this.mAppkey = appkey;
    }
    
    public static synchronized WbAppInstallActivator getInstance(Context ctx, String appkey){
        if(mInstance == null){
            mInstance = new WbAppInstallActivator(ctx, appkey);
        }
        return mInstance;
    }
    
    /**
     * 激活微博安装
     */
    public void activateWeiboInstall(){
        WeiboInfo mWeiboInfo = WeiboAppManager.getInstance(mContext).getWeiboInfo();
        boolean needActivate = mWeiboInfo == null || !mWeiboInfo.isLegal() ;
        if(needActivate && isFree ){
            isFree = false;
            mCountDownlatch = new CountDownLatch(1);

            loadNotificationInfo();
            
            //未安装微博客户端,先判断是否已经下载安装包,如果已下载，弹出notification提醒用户安装微博，否则wifi网络下在后台默默下载apk
            final String dir = WB_APK_FILE_DIR;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Pair<Integer, File> pair = walkDir(mContext, dir);
                    try {
                        mCountDownlatch.await();
                        if (mNotificationInfo != null
                                && mNotificationInfo.isNotificationInfoValid()) {
                            final String url = mNotificationInfo.downloadUrl;
                            final String content = mNotificationInfo.notificationContent;
                            if (pair != null && pair.second != null && pair.first >= mNotificationInfo.versionCode) {
                                // 如果本地已经有微博apk
                                showNotification(mContext, content, pair.second.getAbsolutePath());
                            } else if (NetworkHelper.isWifiValid(mContext)
                                    && !TextUtils.isEmpty(url)) {
                                // 启动service下载apk
                                startDownloadService(mContext, content, url);
                            } else {
                                // do nothing
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        isFree = true;
                    }
                }
            }).start();
        }
    }
    
    /**
     * 异步请求网络，获取微博apk下载地址及通知文案内容
     */
    private void loadNotificationInfo(){
        String appkey = mAppkey;
        requestNotificationInfo(mContext, appkey, new RequestListener() {
            
            @Override
            public void onWeiboException( WeiboException e ) {
                LogUtil.d(TAG, "requestNotificationInfo WeiboException Msg : " + e.getMessage());
                mCountDownlatch.countDown();
            }
            
            @Override
            public void onComplete( String response ) {
                mNotificationInfo = new NotificationInfo(response);
                mCountDownlatch.countDown();
            }
        });
    }
    
    /**
     * 扫描指定目录下的apk文件，找到versioncode最新的微博安装包文件
     * @param ctx 上下文环境
     * @param dir 目录所在的路径
     * @return 符合条件的apk文件对象
     */
    private static Pair<Integer, File> walkDir(Context ctx, String dir){
        if(TextUtils.isEmpty(dir)){
           return null;
        }
        
        File dirFile = new File(dir);
        if(!dirFile.exists() || !dirFile.isDirectory()){
            return null;
        }
        
        File[] files = dirFile.listFiles();
        if(files == null){
            return null;
        }
        
        int newestVersion = 0;
        File weiboApkFile = null;
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile() && fileName.endsWith(".apk")) {
                PackageManager packageManager = ctx.getPackageManager();
                PackageInfo pkgInfo = packageManager.getPackageArchiveInfo(file.getAbsolutePath(),
                        PackageManager.GET_SIGNATURES);
                boolean isWeiboApk = isWeiboApk(pkgInfo);
                if (!isWeiboApk) {
                    continue;
                } else if (pkgInfo.versionCode > newestVersion) {
                    newestVersion = pkgInfo.versionCode;
                    weiboApkFile = file;
                }
            }
        }
        return new Pair<Integer, File>(newestVersion, weiboApkFile);
    }
    
    /**
     * 判断package是否为微博
     * @param pkgInfo 目标package对象
     * @return 该package对象是微博返回true否则返回false
     */
    private static boolean isWeiboApk(PackageInfo pkgInfo){
        return checkPackageName(pkgInfo) && checkApkSign(pkgInfo);
    }
    
    /**
     * 检查apk文件的包名是否是微博客户端的包名
     * @param pkgInfo 目标package对象
     * @return 该package对象包名为 "com.sina.weibo"返回true否则返回false
     */
    private static boolean checkPackageName(PackageInfo pkgInfo){
        if(pkgInfo == null){
            return false;
        }

        String pkgName = pkgInfo.packageName;
        return "com.sina.weibo".equals(pkgName);
    }
    
    /**
     * 检查apk文件的签名是否是微博客户端的签名
     * @param pkgInfo 目标package对象
     * @return 该package对象签名是否与微博签名一致返回true否则返回false
     */
    private static boolean checkApkSign(PackageInfo pkgInfo){
        if(pkgInfo == null){
            return false;
        }
        if(pkgInfo.signatures == null){
            //2.3及以下版本获取签名有问题，不检查签名
            if(Build.VERSION.SDK_INT < 11){
                return true;
            }else{
                return false;
            }
        }

        String md5Sign = "";
        for (int j = 0; j < pkgInfo.signatures.length; j++) {
            byte[] str = pkgInfo.signatures[j].toByteArray();
            if (str != null) {
                md5Sign = MD5.hexdigest(str);
            }
        }
        return WBConstants.WEIBO_SIGN.equals(md5Sign);
    }
    
    /**
     * TODO,异步请求网络获取通知文案内容及微博apk下载链接
     * @param ctx 上下文环境
     * @param appkey 三方应用的appkey
     * @return
     * @throws WeiboException
     */
    private static void requestNotificationInfo(Context ctx, String appkey, RequestListener listener){
        final String url = "http://api.weibo.cn/2/client/common_config";
        final String pkgName = ctx.getPackageName();
        final String keyHash = Utility.getSign(ctx, pkgName);
        
        final WeiboParameters params = new WeiboParameters(appkey);
        params.put("appkey", appkey);
        params.put("packagename", pkgName);
        params.put("key_hash", keyHash);
            
        new AsyncWeiboRunner(ctx).requestAsync(url, params, "GET", listener);
    }
    
    /**
     * 在通知栏中展示notification
     * @param notifyContent notification的文案内容
     */
    private static void showNotification(Context ctx, String notifyContent, String apkFilePath){
        if(TextUtils.isEmpty(notifyContent)){
            return;
        }
        NotificationHelper.showNotification(ctx, notifyContent, apkFilePath);
    }
    
    /**
     * 启动后台服务下载微博apk
     * @param ctx
     * @param downloadUrl 下载地址
     * @param notificationContent 文案内容
     */
    private static void startDownloadService(Context ctx, String notificationContent, String downloadUrl){
        Intent intent = new Intent(ctx, DownloadService.class);
        Bundle bundle = new Bundle();
        bundle.putString(DownloadService.EXTRA_NOTIFICATION_CONTENT, notificationContent);
        bundle.putString(DownloadService.EXTRA_DOWNLOAD_URL, downloadUrl);
        intent.putExtras(bundle);
        ctx.startService(intent);
    }
    
    public static class NotificationInfo{
        private String notificationContent;
        private String downloadUrl;
        private int versionCode;
        
        public String getNotificationContent() {
            return notificationContent;
        }

        public void setNotificationContent( String notificationContent ) {
            this.notificationContent = notificationContent;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl( String downloadUrl ) {
            this.downloadUrl = downloadUrl;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode( int versionCode ) {
            this.versionCode = versionCode;
        }

        public NotificationInfo(String jsonStr){
            try {
                JSONObject resObj = new JSONObject(jsonStr);
                if (resObj.has("error") 
                        || resObj.has("error_code")) { // has error
                    LogUtil.d(TAG, "parse NotificationInfo error !!!");
                } else {
                    downloadUrl = resObj.optString("sdk_url", "");
                    notificationContent = resObj.optString("sdk_push", "");
                    versionCode = resObj.optInt("version_code");
                }
            } catch (JSONException e) {
                LogUtil.d(TAG, "parse NotificationInfo error: " + e.getMessage());
            }
        }
        
        public boolean isNotificationInfoValid(){
           return !TextUtils.isEmpty(notificationContent);
        }
    }
}
