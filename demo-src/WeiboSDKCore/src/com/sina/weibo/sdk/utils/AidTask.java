package com.sina.weibo.sdk.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.locks.ReentrantLock;
import javax.crypto.Cipher;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.exception.WeiboHttpException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * 用于处理广告相关业务

 * @author SINA
 * @since 2014-06-16
 */
public class AidTask {
    private static final String TAG = "AidTask";
    
    private static final String AID_FILE_NAME = "weibo_sdk_aid";
    
    private static final int VERSION = 1; //如果本地数据存储的结构发生变换需要+1
    
    public static final int WHAT_LOAD_AID_SUC = 1001;
    public static final int WHAT_LOAD_AID_API_ERR = 1002;
    public static final int WHAT_LOAD_AID_IO_ERR = 1003;
    
    private static AidTask sInstance;
    private Context mContext;
    private String mAppKey;
    
    private volatile ReentrantLock mTaskLock = new ReentrantLock(true);
    
    public static final class AidInfo {
        private String mAid;
        private String mSubCookie;
        
        public String getAid() {
            return this.mAid;
        }
        
        public String getSubCookie() {
            return this.mSubCookie;
        }
        
        public static AidInfo parseJson(String response) throws WeiboException {
            AidInfo instance = new AidInfo();
            try {
                JSONObject resObj = new JSONObject(response);
                if (resObj.has("error") 
                        || resObj.has("error_code")) { // has error
                    LogUtil.d(TAG, "loadAidFromNet has error !!!");
                    throw new WeiboException("loadAidFromNet has error !!!");
                } else {
                    instance.mAid = resObj.optString("aid", "");
                    instance.mSubCookie = resObj.optString("sub", "");
                }
            } catch (JSONException e) {
                LogUtil.d(TAG, "loadAidFromNet JSONException Msg : " + e.getMessage());
                throw new WeiboException("loadAidFromNet has error !!!");
            }
            return instance;
        }
    }
    
    
    private AidTask(Context context) {
        this.mContext = context.getApplicationContext();
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<VERSION; i++) {
                    File f = getAidInfoFile(i);
                    try {
                        f.delete();
                    } catch (Exception e) {}
                }
            }
        }).start();
    }
    
    
    public static synchronized AidTask getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AidTask(context);
        }
        return sInstance;
    }
    
    /**
     * 设置AppKey
     * @param appKey
     */
    public void setAppkey(String appKey) {
        mAppKey = appKey;
    }
    
    /**
     * 初始化AID
     */
    public void aidTaskInit() {
        aidTaskInit(mAppKey);
    }
    
    /**
     * 初始化AID
     * @param appKey
     */
    public void aidTaskInit(String appKey) {
        if (TextUtils.isEmpty(appKey)) {
            return;
        }
        
        mAppKey = appKey;
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!mTaskLock.tryLock()) {
                    return;
                }
                String cacheAid = loadAidFromCache();
                if (!TextUtils.isEmpty(cacheAid)) {
                    mTaskLock.unlock();
                    return;
                }
                
                final int COUNT = 3;
                int retry = 0;
                while(retry < COUNT) {
                    try {
                        String response = loadAidFromNet();
                        AidInfo.parseJson(response);
                        cacheAidInfo(response);
                        break;
                    } catch (WeiboException e) {
                        LogUtil.e(TAG, "AidTaskInit WeiboException Msg : " + e.getMessage());
                    }
                    retry++;
                }
                mTaskLock.unlock();
            }
        }).start();
        
    }
    
    public ReentrantLock getTaskLock() {
        return this.mTaskLock;
    }
    
    /**
     * 同步获取AID
     * @return
     */
    public AidInfo getAidSync() throws WeiboException {
        if (TextUtils.isEmpty(mAppKey)) {
            return null;
        }
        String response = loadAidFromNet();
        AidInfo aidInfo = AidInfo.parseJson(response);
        cacheAidInfo(response);
        return aidInfo;
    }
    
    /**
     * 异步获取AID
     * @param h
     */
    public void getAidAsync(final Handler h) {
        if (TextUtils.isEmpty(mAppKey)) {
            return;
        }
        final Message msg = Message.obtain();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = loadAidFromNet();
                    AidInfo aidInfo = AidInfo.parseJson(response);
                    cacheAidInfo(response);
                    
                    msg.what = WHAT_LOAD_AID_SUC;
                    msg.obj = aidInfo;
                    if (h != null) {
                        h.sendMessage(msg);
                    }
                    return;
                } catch (WeiboException e) {
                    if (e.getCause() instanceof IOException 
                            || e instanceof WeiboHttpException) {
                        msg.what = WHAT_LOAD_AID_IO_ERR;
                        if (h != null) {
                            h.sendMessage(msg);
                        }
                        return;
                    }
                    else {
                        msg.what = WHAT_LOAD_AID_API_ERR;
                        if (h != null) {
                            h.sendMessage(msg);
                        }
                        return;
                    }
                }
            }
        }).start();
    }
    
    /**
     * 从本地缓存加载AID
     * @return
     */
    public synchronized String loadAidFromCache() {
        AidInfo info = loadAidInfoFromCache();
        if (info != null) {
            return info.getAid();
        }
        return "";
    }
    
    /**
     * 从本地缓存加载Sub Cookie
     * @return
     */
    public synchronized String loadSubCookieFromCache() {
        AidInfo info = loadAidInfoFromCache();
        if (info != null) {
            return info.getSubCookie();
        }
        return "";
    }
    
    /**
     * 从本地缓存加载AidInfo
     * @return
     */
    private synchronized AidInfo loadAidInfoFromCache() {
        FileInputStream fis = null;
        try {
            File aidFile = getAidInfoFile(VERSION);
            fis = new FileInputStream(aidFile);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            return AidInfo.parseJson(new String(buffer));
        } catch (Exception e) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {}
            }
        }
        return null;
    }
    
    private File getAidInfoFile(int version) {
        File dir = mContext.getFilesDir();
        File aidFile = new File(dir, AID_FILE_NAME+version);
        return aidFile;
    }
    
    /**
     * 从网络加载AID
     * @return
     * @throws WeiboException
     */
    private String loadAidFromNet() throws WeiboException {
        final String url = "http://api.weibo.com/oauth2/getaid.json";
        final String pkgName = mContext.getPackageName();
        final String keyHash = Utility.getSign(mContext, pkgName);
        final String mfp = getMfp();
        
        final WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("appkey", mAppKey);
        params.put("mfp", mfp);
        params.put("packagename", pkgName);
        params.put("key_hash", keyHash);
            
        try {
            String response = new AsyncWeiboRunner(mContext).request(url, params, "GET");
            LogUtil.d(TAG, "loadAidFromNet response : " + response);
            return response;
        } catch (WeiboException e) {
            LogUtil.d(TAG, "loadAidFromNet WeiboException Msg : " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * 本地保存aid信息
     * @param aid
     */
    private synchronized void cacheAidInfo(String json) {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        
        FileOutputStream fos = null;
        try {
            File aidFile = getAidInfoFile(VERSION);
            fos = new FileOutputStream(aidFile);
            fos.write(json.getBytes());
        } catch (Exception e) {
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {}
            }
        }
    }
    
    /**
     * 获得设备指纹
     * @return
     */
    private String getMfp() {
        /**
         * RSA
         */
        final String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHHM0Fi2Z6+QYKXqFUX2Cy6AaW" +
                "q3cPi+GSn9oeAwQbPZR75JB7Netm0HtBVVbtPhzT7UO2p1JhFUKWqrqoYuAjkgMV" +
                "PmA0sFrQohns5EE44Y86XQopD4ZO+dE5KjUZFE6vrPO3rWW3np2BqlgKpjnYZri6" +
                "TJApmIpGcQg9/G/3zQIDAQAB";
        String mfpJson = genMfpString();
        String mfpJsonUtf8 = "";
        try {
            mfpJsonUtf8 = new String(mfpJson.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        LogUtil.d(TAG, "genMfpString() utf-8 string : " + mfpJsonUtf8);
        try {
            String rsaMfp = encryptRsa(mfpJsonUtf8, publicKey);
            LogUtil.d(TAG, "encryptRsa() string : " + rsaMfp);
            return rsaMfp;
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return "";
    }
    
    private String genMfpString() {
        JSONObject mfpObj = new JSONObject();
        try {
            final String os = getOS();
            if (!TextUtils.isEmpty(os)) {
                mfpObj.put("1", os);
            }
            final String imei = getImei();
            if (!TextUtils.isEmpty(imei)) {
                mfpObj.put("2", imei);
            }
            final String meid = getMeid();
            if (!TextUtils.isEmpty(meid)) {
                mfpObj.put("3", meid);
            }
            final String imsi = getImsi();
            if (!TextUtils.isEmpty(imsi)) {
                mfpObj.put("4", imsi);
            }
            final String mac = getMac();
            if (!TextUtils.isEmpty(mac)) {
                mfpObj.put("5", mac);
            }
            final String iccid = getIccid();
            if (!TextUtils.isEmpty(iccid)) {
                mfpObj.put("6", iccid);
            }
            final String serial = getSerialNo();
            if (!TextUtils.isEmpty(serial)) {
                mfpObj.put("7", serial);
            }
            final String androidId = getAndroidId();
            if (!TextUtils.isEmpty(androidId)) {
                mfpObj.put("10", androidId);
            }
            final String cpu = getCpu();
            if (!TextUtils.isEmpty(cpu)) {
            mfpObj.put("13", cpu);
            }
            final String model = getModel();
            if (!TextUtils.isEmpty(model)) {
                mfpObj.put("14", model);
            }
            final String sdcard = getSdSize();
            if (!TextUtils.isEmpty(sdcard)) {
                mfpObj.put("15", sdcard);
            }
            final String resolution = getResolution();
            if (!TextUtils.isEmpty(resolution)) {
                mfpObj.put("16", resolution);
            }
            final String ssid = getSsid();
            if (!TextUtils.isEmpty(ssid)) {
                mfpObj.put("17", ssid);
            }
            final String deviceName = getDeviceName();
            if (!TextUtils.isEmpty(deviceName)) {
                mfpObj.put("18", deviceName);
            }
            final String connectType = getConnectType();
            if (!TextUtils.isEmpty(connectType)) {
                mfpObj.put("19", connectType);
            }
            
            return mfpObj.toString();
        } catch (JSONException e) {}
        
        return "";
    }
    
    private String encryptRsa(String src, String publicKeyStr) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // "RSA/ECB/PKCS1Padding"
        PublicKey publicKey = getPublicKey(publicKeyStr);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        
        ByteArrayOutputStream bos = null;
        byte[] plainText = src.getBytes("UTF-8");
        try {
            bos = new ByteArrayOutputStream();
            int len = 0;
            int offset = 0;
            while ((len = splite(plainText, offset, 117)) != -1) {
                byte[] enBytes = cipher.doFinal(plainText, offset, len);
                bos.write(enBytes);
                LogUtil.d(TAG, "encryptRsa offset = " + offset + "     len = " + len 
                        + "     enBytes len = " + enBytes.length);
                offset += len;
            }
            bos.flush();
            byte[] enBytes = bos.toByteArray();
            LogUtil.d(TAG, "encryptRsa total enBytes len = " + enBytes.length);
            
            byte[] base64byte = Base64.encodebyte(enBytes);
            LogUtil.d(TAG, "encryptRsa total base64byte len = " + base64byte.length);
            
            final String VERSION = "01";
            String base64string = new String(base64byte, "UTF-8");
            base64string = VERSION + base64string;
            LogUtil.d(TAG, "encryptRsa total base64string : " + base64string);
            
            return base64string;
            //return URLEncoder.encode(base64string, "UTF-8");
            
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                }
            }
        }
    }
    
    private int splite(byte[] src, int offset, int limit) {
        if (offset >= src.length) {
            return -1;
        }
        int delta = src.length - offset;
        return Math.min(delta, limit);
    }
    
    private PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
    
    private String getOS() {
        try {
            return ("Android " + Build.VERSION.RELEASE);
        } catch (Exception e) {}
        return "";
    }
    
    private String getImei() {
        try {
            TelephonyManager telePhonyMgr = (TelephonyManager) mContext.getSystemService(
                    Context.TELEPHONY_SERVICE);
            return telePhonyMgr.getDeviceId();
        } catch (Exception e) {}
        return "";
    }
    
    private String getMeid() {
        try {
            TelephonyManager telePhonyMgr = (TelephonyManager) mContext.getSystemService(
                    Context.TELEPHONY_SERVICE);
            return telePhonyMgr.getDeviceId();
        } catch (Exception e) {}
        return "";
    }
    
    private String getImsi() {
        try {
            TelephonyManager telePhonyMgr = (TelephonyManager) mContext.getSystemService(
                    Context.TELEPHONY_SERVICE);
            return telePhonyMgr.getSubscriberId();
        } catch (Exception e) {}
        return "";
    }
    
    private String getMac() {
        try {
            WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            if (null == wifi) {
                return "";
            }
            WifiInfo info = wifi.getConnectionInfo();  
            return (info != null) ? info.getMacAddress() : "";
        } catch (Exception e) {}
        return "";
    }
    
    private String getIccid() {
        try {
            TelephonyManager telePhonyMgr = (TelephonyManager) mContext.getSystemService(
                    Context.TELEPHONY_SERVICE);
            return telePhonyMgr.getSimSerialNumber();
        } catch (Exception e) {}
        return "";
    }
    
    private String getSerialNo() {
        String serialnum = "";
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);
            serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
        } catch (Exception ignored) {
        }
        return serialnum;
    }
    
    private String getAndroidId() {
        try {
            String androidId = Settings.Secure.getString(
                    mContext.getContentResolver(),Settings.Secure.ANDROID_ID);
            return androidId;
        } catch (Exception e) {}
        return "";
    }
    
    private String getCpu() {
        try {
            return Build.CPU_ABI;
        } catch (Exception e) {}
        return "";
    }
    
    private String getModel() {
        try {
            return Build.MODEL;
        } catch (Exception e) {}
        return "";
    }
    
    private String getSdSize() {
        try {
            File path = Environment.getExternalStorageDirectory(); 
            StatFs stat = new StatFs(path.getPath()); 
            long blockSize = stat.getBlockSize(); 
            long availableBlocks = stat.getBlockCount();
            return Long.toString(availableBlocks * blockSize);
        } catch (Exception e) {}
        return "";
    }
    
    private String getResolution() {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) mContext.getSystemService(
                    Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
            return (String.valueOf(dm.widthPixels) + 
                    "*" +
                    String.valueOf(dm.heightPixels));
        } catch (Exception e) {}
        return "";
    }
    
    private String getSsid() {
        try {
            WifiManager wifiManager = (WifiManager) mContext.getSystemService(
                    Context.WIFI_SERVICE);  
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                return wifiInfo.getSSID();
            }
        } catch (Exception e) {}
        return "";
    }
    
    private String getDeviceName() {
        try {
            return Build.BRAND;
        } catch (Exception e) {}
        return "";
    }
    
    private String getConnectType() {
        String network = "none";
        try {
            ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            
            // 请注意：
            // 为了完美支持所有SDK，需要将所有的 TelephonyManager.NETWORK_TYPE_xxx 换成数字，
            // 在低版本上，如 Android 1.6上，就没有定义 TelephonyManager.NETWORK_TYPE_HSPAP
            if(null != info){
                if(info.getType() == ConnectivityManager.TYPE_MOBILE){
                    switch (info.getSubtype()) {                        
                    // 联通移动2G
                    case /*TelephonyManager.NETWORK_TYPE_GPRS*/ 1:
                    case /*TelephonyManager.NETWORK_TYPE_EDGE*/ 2:
                    // 电信2G
                    case /*TelephonyManager.NETWORK_TYPE_CDMA*/ 4:
                    // Other 2G
                    case /*TelephonyManager.NETWORK_TYPE_1xRTT*/ 7:
                    case /*TelephonyManager.NETWORK_TYPE_IDEN*/ 11:
                        network = "2G";
                        break;
                        
                    // 联通3G
                    case /*TelephonyManager.NETWORK_TYPE_UMTS*/ 3:
                    case /*TelephonyManager.NETWORK_TYPE_HSDPA*/ 8:
                    // 电信3G
                    case /*TelephonyManager.NETWORK_TYPE_EVDO_0*/ 5:
                    case /*TelephonyManager.NETWORK_TYPE_EVDO_A*/ 6:
                    // Other 3G
                    case /*TelephonyManager.NETWORK_TYPE_HSUPA*/ 9:
                    case /*TelephonyManager.NETWORK_TYPE_HSPA*/ 10:
                    case /*TelephonyManager.NETWORK_TYPE_EVDO_B*/ 12:
                    case /*TelephonyManager.NETWORK_TYPE_EHRPD*/ 14:
                    case /*TelephonyManager.NETWORK_TYPE_HSPAP*/ 15:
                        network = "3G";
                        break;                  
                    
                    case /*TelephonyManager.NETWORK_TYPE_LTE*/ 13:
                        network = "4G";
                        break;
                        
                    default:
                        network = "none";
                        break;
                    }
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    network = "wifi";
                }
            }
        } catch (Exception e) {}
        
        return network;
    }
    
}
