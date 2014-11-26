package com.sina.weibo.sdk.net;

import java.io.File;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;

import com.sina.weibo.sdk.WbAppInstallActivator;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.NotificationHelper;

/**
 * 下载服务
 * @author SINA
 * @since 2014/11/12
 */
public class DownloadService extends IntentService {

    public static final String EXTRA_DOWNLOAD_URL = "download_url";
    
    public static final String EXTRA_NOTIFICATION_CONTENT = "notification_content";
    
    private static final String TAG = DownloadService.class.getCanonicalName();
    
    private static final String APK_SAVE_DIR = WbAppInstallActivator.WB_APK_FILE_DIR;
    
    public DownloadService() {
        super(TAG);
    }

    @Override
    public IBinder onBind( Intent intent ) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    
    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
        return super.onStartCommand(intent, flags, startId);
    }
    
    @Override
    public void onStart( Intent intent, int startId ) {
        super.onStart(intent, startId);
        if(intent == null){
            return;
        }
    }

    @Override
    protected void onHandleIntent( Intent intent ) {
        final Bundle bundle = intent.getExtras();
        if(bundle == null){
            stopSelf();
            return;
        }
        
        final String downLoadUrl = bundle.getString(EXTRA_DOWNLOAD_URL);
        final String notificationContent = bundle.getString(EXTRA_NOTIFICATION_CONTENT);
        LogUtil.e(TAG, "onHandleIntent downLoadUrl:"+downLoadUrl+"!!!!!");
        if(TextUtils.isEmpty(downLoadUrl)){
            LogUtil.e(TAG, "downloadurl is null");
            stopSelf();
            return;
        }
        
        String filePath = "";
        try{
            String redirectUrl = HttpManager.openRedirectUrl4LocationUri(getApplicationContext(), downLoadUrl,
                    "GET", new WeiboParameters(""));
            
            String fileName = generateSaveFileName(redirectUrl);
            if (TextUtils.isEmpty(fileName) || !fileName.endsWith(".apk")) {
                LogUtil.e(TAG, "redirectDownloadUrl is illeagle");
                stopSelf();
                return;
            }
            String saveDir = APK_SAVE_DIR;
            filePath = HttpManager.downloadFile(getApplicationContext(), redirectUrl,
                    saveDir, fileName);
        }catch(WeiboException e ){
            e.printStackTrace();
        }
        
        if(!TextUtils.isEmpty(filePath)){
            File downloadFile = new File(filePath);
            if(downloadFile.exists()){
                //下载成功
                LogUtil.e(TAG, "download successed!");
                NotificationHelper.showNotification(getApplicationContext(), notificationContent, filePath);
            }
        }else{
            LogUtil.e(TAG, "download failed!");
        }
}
    
    /**
     * 从下载链接中获取文件名称
     * @param downloadUrl 下载链接
     * @return
     */
    private static String generateSaveFileName(String downloadUrl){
        String fileName = "";
        int index = downloadUrl.lastIndexOf("/");
        if(index != -1){
            fileName = downloadUrl.substring(index+1, downloadUrl.length());
        }
        return fileName;
    }
}
