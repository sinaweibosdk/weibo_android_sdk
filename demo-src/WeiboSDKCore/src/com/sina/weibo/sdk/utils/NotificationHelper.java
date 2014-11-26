package com.sina.weibo.sdk.utils;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

public class NotificationHelper {

    private static final int NOTIFICATION_ID = 0x0001; 
    
    private static final String WEIBO = "Weibo";
    private static final String WEIBO_ZH_CN = "微博";
    private static final String WEIBO_ZH_TW = "微博";
    /**
     * 根据传入的文案及下载的apk路径在通知栏中弹出Notification
     * @param ctx 应用上下文环境
     * @param notifyContent notification内容
     * @param apkFilePath apk所在的文件路径
     */
    public static void showNotification(Context ctx, String notifyContent, String apkFilePath){
        if(TextUtils.isEmpty(notifyContent)){
            return;
        }
        NotificationManager mNotifyManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notificaton = buildNotification(ctx, notifyContent, apkFilePath);
        mNotifyManager.notify(NOTIFICATION_ID, notificaton);
    }
    
    /**
     * 构造notification对象
     * @param ctx 应用上下文环境
     * @param notifyContent notification内容
     * @param apkFilePath apk所在的文件路径
     * @return
     */
    private static Notification buildNotification(Context ctx, String notifyContent, String apkFilePath){
        Notification notification = null;

        String tickerText = "";
        String contentTitle = "";

        tickerText = notifyContent;
        contentTitle = ResourceManager.getString(ctx, WEIBO, WEIBO_ZH_CN, WEIBO_ZH_TW);

        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(ctx);
        mNotificationBuilder.setAutoCancel(true);
        mNotificationBuilder.setWhen(System.currentTimeMillis());
        Drawable drawable = ResourceManager.getDrawable(ctx, "ic_com_sina_weibo_sdk_weibo_logo.png");
        mNotificationBuilder.setLargeIcon(((BitmapDrawable)drawable).getBitmap());
        mNotificationBuilder.setSmallIcon(getNotificationIcon(ctx));
        mNotificationBuilder.setContentTitle(contentTitle);
        mNotificationBuilder.setTicker(tickerText);
        mNotificationBuilder.setContentText(tickerText);

        PendingIntent pendingIntent = buildInstallApkIntent(ctx, apkFilePath);
        mNotificationBuilder.setContentIntent(pendingIntent);
        
        notification = mNotificationBuilder.build();

        return notification;
    }
    
    /**
     * 构造点击notification时的的PendingIntent，当安装文件路径不为空时，跳转到对应的系统安装页面，否则构造一个空的PendingIntent，防止在2.x系统crash
     * @param ctx 应用上下文环境
     * @param filePath apk所在的文件路径
     * @return 
     */
    private static PendingIntent buildInstallApkIntent(Context ctx, String filePath){
        PendingIntent pendingIntent = null;
        if(!TextUtils.isEmpty(filePath)){
            Intent intentInstall = new Intent("android.intent.action.VIEW");
            Uri localUri = Uri.fromFile(new File(filePath));
            intentInstall.setDataAndType(localUri,
                    "application/vnd.android.package-archive");
            pendingIntent = PendingIntent.getActivity(ctx, 0, intentInstall, Notification.FLAG_AUTO_CANCEL);
        }else{
            Intent intent = new Intent();
            pendingIntent = PendingIntent.getActivity(ctx, 0, intent, Notification.FLAG_AUTO_CANCEL);
        }
        return pendingIntent;
    }
    
    /**
     * 获取notification的icon
     * 当第三方应用内置有"com_sina_weibo_sdk_weibo_logo"图标时，采用此图标，否则采用系统图标
     * @param ctx
     * @return icon对应的资源id
     */
    private static int getNotificationIcon(Context ctx){
        int resId = getResourceId(ctx, "com_sina_weibo_sdk_weibo_logo", "drawable");
        return resId > 0 ? resId
                : android.R.drawable.ic_dialog_info;
    }
    
    /**
     * 通过包名、资源类型以及资源名称或者去资源id
     * @param ctx
     * @param name 资源名称
     * @param type 资源类型
     * @return 资源id
     */
    private static int getResourceId(Context ctx, String name, String type){
        Resources themeResources = null;
        final String packageName = ctx.getApplicationContext().getPackageName();
        PackageManager pm = ctx.getPackageManager();
        try {
          themeResources = pm.getResourcesForApplication(packageName);
          return themeResources.getIdentifier(name, type, packageName);
        } catch (PackageManager.NameNotFoundException e) {
          e.printStackTrace();
        }
        return 0;
    }
}
