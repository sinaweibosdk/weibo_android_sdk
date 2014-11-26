/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sina.weibo.sdk.api.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.os.Bundle;
import android.text.TextUtils;
import com.sina.weibo.sdk.ApiUtils;
import com.sina.weibo.sdk.WeiboAppManager;
import com.sina.weibo.sdk.WeiboAppManager.WeiboInfo;
import com.sina.weibo.sdk.api.CmdObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Request;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.component.BrowserRequestParamBase;
import com.sina.weibo.sdk.component.ShareRequestParam;
import com.sina.weibo.sdk.component.WeiboCallbackManager;
import com.sina.weibo.sdk.component.WeiboSdkBrowser;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.exception.WeiboShareException;
import com.sina.weibo.sdk.utils.AidTask;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.MD5;
import com.sina.weibo.sdk.utils.Reflection;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 该类实现了 {@link IWeiboShareAPI} 所有接口。
 * 
 * @author SINA
 * @since 2013-10-22
 */
@SuppressWarnings( "unused" )
/*public*/ class WeiboShareAPIImpl implements IWeiboShareAPI {

    private static final String TAG = WeiboShareAPIImpl.class.getName();

    /** 当前应用程序的上下文 */
    private Context mContext;

    /** 第三方应用的 APP_KEY */
    private String mAppKey;
    
    /** 微博客户端信息实例 */
    private WeiboInfo mWeiboInfo = null;
    
    /** 如果当前系统没有安装微博，是否需要下载微博，默认为 true */
    private boolean mNeedDownloadWeibo = true;
    
    /** 下载微博时的 Listener */
    private IWeiboDownloadListener mDownloadListener;
    
    /** 如果当前系统没有安装微博，弹出的下载微博客户端对话框 */
    private Dialog mDownloadConfirmDialog = null;

    /**
     * 构造函数。
     * @param context           当前应用程序的上下文
     * @param appKey            第三方应用的 APP_KEY
     * @param needDownloadWeibo 如果当前系统没有安装微博，是否需要下载微博
     */
    public WeiboShareAPIImpl(Context context, String appKey, boolean needDownloadWeibo) {
        mContext = context;
        mAppKey = appKey;
        mNeedDownloadWeibo = needDownloadWeibo;
        
        // 获取微博客户端信息
        mWeiboInfo = WeiboAppManager.getInstance(context).getWeiboInfo();
        if (mWeiboInfo != null) {
            LogUtil.d(TAG, mWeiboInfo.toString());
        } else {
            LogUtil.d(TAG, "WeiboInfo is null");
        }
        AidTask.getInstance(context).aidTaskInit(appKey);
    }

    /**
     * 获取当前微博客户端程序支持的 SDK 的最高版本号。
     * 如果微博版本号 < {@link ApiUtils#BUILD_INT_VER_2_2}，不支持 {@link VoiceObject}
     * 如果微博版本号 < {@link ApiUtils#BUILD_INT_VER_2_3}，不支持 {@link CmdObject}
     * 
     * @return 版本号
     */
    @Override
    public int getWeiboAppSupportAPI() {
        return (null == mWeiboInfo || !mWeiboInfo.isLegal()) ? -1 : mWeiboInfo.getSupportApi();
    }

    /**
     * 检查用户是否安装了微博客户端程序
     * 
     * @return 如果安装了，返回 true；否则，返回 false。
     */
    @Override
    public boolean isWeiboAppInstalled() {        
        return (mWeiboInfo != null && mWeiboInfo.isLegal()) ? true : false;
    }

    /**
     * 检查微博是否支持此 SDK。
     * 
     * @return 如果支持，返回 true；否则，返回 false。
     */
    @Override
    public boolean isWeiboAppSupportAPI() {
        return getWeiboAppSupportAPI() >= ApiUtils.BUILD_INT;
    }

    /* (non-Javadoc)
     * @see com.sina.weibo.sdk.api.share.IWeiboShareAPI#isSupportWeiboPay()
     */
    @Override
    public boolean isSupportWeiboPay() {
        return (getWeiboAppSupportAPI() >= ApiUtils.BUILD_INT_VER_2_5);
    }

    /**
     * 注册第三方应用 到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
     * 默认情况下，如果未安装微博客户端，在注册时，会弹出对话框询问用户是否下载微博客户端。
     * 
     * @return 成功返回 true，会显示在微博第三方列表中，否则不显示
     */
    @Override
    public boolean registerApp() {
        /**
        try {
            if (!checkEnvironment(mNeedDownloadWeibo)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        **/
        sendBroadcast(mContext, WBConstants.ACTION_WEIBO_REGISTER, mAppKey, null, null);
        return true;
    }

    /**
     * 处理微博客户端分享后的响应数据。
     * <p>当从当前应用唤起微博发博器并进行分享后，返回到当前应用时，需要在 
     * {@link Activity#onNewIntent} 和 {@link Activity#onCreate} 函数中，主动调用该
     * 函数处理分享后的响应数据。在 {@link Activity#onCreate} 函数中调用该函数是为了防
     * 止该 Activity 处于后台时，可能会由于内存不足被杀掉了而导致的重新初始化。</p>
     * <p><em>执行流程：本应用->微博->本应用</em></p>
     * 
     * @param intent  数据内容
     * @param handler 处理应答对应的回调函数
     * 
     * @return 成功返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
     *         失败返回 false，不调用上述回调
     */
    @Override
    public boolean handleWeiboResponse(Intent intent, Response handler) {
        String appPackage = intent.getStringExtra(WBConstants.Base.APP_PKG);
        String transaction = intent.getStringExtra(WBConstants.TRAN);

        if (TextUtils.isEmpty(appPackage)) {
            LogUtil.e(TAG, "handleWeiboResponse faild appPackage is null");
            return false;
        }
        if (!(handler instanceof Activity)) {
            LogUtil.e(TAG, "handleWeiboResponse faild handler is not Activity");
            return false;
        }
        
        Activity act = (Activity) handler;
        String callPkg = act.getCallingPackage();
        LogUtil.d(TAG, "handleWeiboResponse getCallingPackage : " + callPkg);

        if (TextUtils.isEmpty(transaction)) {
            LogUtil.e(TAG, "handleWeiboResponse faild intent _weibo_transaction is null");
            return false;
        }
        /*
        if (!appPackage.equals(callPkg)) {
            LogUtil.e(TAG, "responseListener() faild appPackage not equal callPkg");
            return false;
        }
        */
        if (!ApiUtils.validateWeiboSign(mContext, appPackage) && !appPackage.equals(act.getPackageName())) {
            LogUtil.e(TAG, "handleWeiboResponse faild appPackage validateSign faild");
            return false;
        }

        SendMessageToWeiboResponse data = new SendMessageToWeiboResponse(intent.getExtras());
        handler.onResponse((BaseResponse) data);
        return true;
    }

    /**
     * 处理微博客户端发送过来的请求。
     * <p>当微博客户端唤起当前应用并进行分享时，需要在 {@link Activity#onCreate} 和 
     * {@link Activity#onNewIntent} 函数中，主动调用该函数来处理客户端发送过来的请求。</p>
     * <p><em>执行流程：微博->本应用->微博</em></p>
     * 
     * @param intent  数据内容
     * @param handler 处理请求后对应的回调函数
     * 
     * @return 成功返回 true，失败返回 false，并调用 {@link IWeiboHandler.Request#onRequest}
     */
    @Override
    public boolean handleWeiboRequest(Intent intent, Request handler) {
        if (null == intent || null == handler) {
            return false;
        }
        
        String appPackage = intent.getStringExtra(WBConstants.Base.APP_PKG);
        String transaction = intent.getStringExtra(WBConstants.TRAN);
        
        if (TextUtils.isEmpty(appPackage)) {
            LogUtil.e(TAG, "handleWeiboRequest faild appPackage validateSign faild");
            handler.onRequest(null);
            return false;
        } 
        if (TextUtils.isEmpty(transaction)) {
            LogUtil.e(TAG, "handleWeiboRequest faild intent _weibo_transaction is null");
            handler.onRequest(null);
            return false;
        }
        if (!ApiUtils.validateWeiboSign(mContext, appPackage)) {
            LogUtil.e(TAG, "handleWeiboRequest faild appPackage validateSign faild");
            handler.onRequest(null);
            return false;
        }
        
        ProvideMessageForWeiboRequest data = new ProvideMessageForWeiboRequest(intent.getExtras());
        handler.onRequest(data);
        return true;
    }

    /**
     * 启动微博客户端程序。
     * 
     * @return 启动成功，返回 true；否则，返回 false
     */
    @Override
    public boolean launchWeibo(Activity act) {
        if (!isWeiboAppInstalled()) {
            LogUtil.e(TAG, "launchWeibo faild WeiboInfo is null");
            return false;
        }
        
        try {
            act.startActivity(
                    act.getPackageManager().getLaunchIntentForPackage(mWeiboInfo.getPackageName()));
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            return false;
        }
        
        return true;
    }

    /**
     * 发送微博分享请求给微博客户端程序。
     * 默认情况下，如果未安装微博客户端，在分享时，会弹出对话框询问用户是否下载微博客户端。
     * 
     * @param request 请求的内容。
     * 
     * @return 发送成功，返回 true；否则，返回 false
     */
    @Override
    public boolean sendRequest(Activity act, BaseRequest request) {
        if (null == request) {
            LogUtil.e(TAG, "sendRequest faild request is null");
            return false;
        }
        
        try {
            if (!checkEnvironment(mNeedDownloadWeibo)) {
                return false;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
            return false;
        }

        if (!request.check(mContext, mWeiboInfo, new VersionCheckHandler())) {
            LogUtil.e(TAG, "sendRequest faild request check faild");
            return false;
        }
        
        Bundle data = new Bundle();
        request.toBundle(data);
        return launchWeiboActivity(act, WBConstants.ACTIVITY_WEIBO, mWeiboInfo.getPackageName(), mAppKey, data);
    }
    
    @Override
    public boolean sendRequest(final Activity act, final BaseRequest request, 
            final AuthInfo authInfo, final String token, final WeiboAuthListener authListener) {
        if (null == request) {
            LogUtil.e(TAG, "sendRequest faild request is null !");
            return false;
        }
        
        if (isWeiboAppInstalled() && isWeiboAppSupportAPI()) {
            // 跳到微博客户端进行分享
            int supportApi = getWeiboAppSupportAPI();
            if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) { // 支持多条消息同时分享
                return sendRequest(act, request);
            } else { // 仅支持单条消息分享
                if (request instanceof SendMultiMessageToWeiboRequest) {
                    // 把多条消息拆成单条消息
                    SendMultiMessageToWeiboRequest multiMessageReq = (SendMultiMessageToWeiboRequest) request;
                    SendMessageToWeiboRequest singleMessageReq = new SendMessageToWeiboRequest();
                    singleMessageReq.packageName = multiMessageReq.packageName;
                    singleMessageReq.transaction = multiMessageReq.transaction;
                    singleMessageReq.message = adapterMultiMessage2SingleMessage(multiMessageReq.multiMessage);
                    return sendRequest(act, singleMessageReq);
                }
                else {
                    return sendRequest(act, request);
                }
            }
        }
        else {
            return startShareWeiboActivity(act, token, request, authListener);
        }
    }
    
    private WeiboMessage adapterMultiMessage2SingleMessage(WeiboMultiMessage multiMessage) {
        if (multiMessage == null) {
            return new WeiboMessage();
        }
        Bundle data = new Bundle();
        multiMessage.toBundle(data);
        WeiboMessage message = new WeiboMessage(data);
        return message;
    }
    
    private boolean startShareWeiboActivity(Activity act, String token, BaseRequest request, 
            final WeiboAuthListener authListener) {
        try {
            Bundle data = new Bundle();
            String appPackage = act.getPackageName();
            
            ShareRequestParam param = new ShareRequestParam(act);
            param.setToken(token);
            param.setAppKey(mAppKey);
            param.setAppPackage(appPackage);
            param.setBaseRequest(request);
            param.setSpecifyTitle("微博分享");
            param.setAuthListener(authListener);
            
            Intent intent = new Intent(act, WeiboSdkBrowser.class);
            intent.putExtras(param.createRequestParamBundle());
            act.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
        }
        return false;
    }
    
    /**
     * 收到微博客户端程序的请求后，发送对应的应答内容给微博客户端程序。
     * 
     * @param response 应答数据
     * 
     * @return 响应成功，返回 true；否则，返回 false
     */
    @Override
    public boolean sendResponse(BaseResponse response) {
        if (null == response) {
            LogUtil.e(TAG, "sendResponse failed response null");
            return false;
        }
        if (!response.check(mContext, new VersionCheckHandler())) {
            LogUtil.e(TAG, "sendResponse check fail");
            return false;
        }
        
        Bundle data = new Bundle();
        response.toBundle(data);
        sendBroadcast(mContext, WBConstants.ACTION_WEIBO_RESPONSE, mAppKey, response.reqPackageName, data);
        return true;
    }
    
    /**
     * 注册微博客户端下载的监听器。
     * 当用户取消下载时，该监听器的 {@link IWeiboDownloadListener#onCancel} 函数被调用。
     * 
     * @param listener 监听微博客户端下载的 Listener
     */
    private void registerWeiboDownloadListener(IWeiboDownloadListener listener) {
        mDownloadListener = listener;
    }

    /**
     * 检查微博客户端环境是否正常。
     * 
     * @param bShowDownloadDialog 如果未安装微博，是否弹出对话框询问用户下载微博客户端
     * @return 如果微博客户端已安装并且合法，返回 true；否则，返回 false
     * @throws WeiboShareException 微博客户端不合法时，抛出异常
     */
    private boolean checkEnvironment(boolean bShowDownloadDialog) throws WeiboShareException {
        if (!isWeiboAppInstalled()) {
            if (bShowDownloadDialog) {
                if (null == mDownloadConfirmDialog) {
                    mDownloadConfirmDialog = WeiboDownloader.createDownloadConfirmDialog(mContext, mDownloadListener);
                    mDownloadConfirmDialog.show();
                } else {
                    if (!mDownloadConfirmDialog.isShowing()) {
                        mDownloadConfirmDialog.show();
                    }
                }
                return false;
            } else {
                throw new WeiboShareException("Weibo is not installed!");
            }
        }
        
        if (!isWeiboAppSupportAPI()) {
            throw new WeiboShareException("Weibo do not support share api!");
        }

        if (!ApiUtils.validateWeiboSign(mContext, mWeiboInfo.getPackageName())) {
            throw new WeiboShareException("Weibo signature is incorrect!");
        }
        
        return true;
    }
    
    /* (non-Javadoc)
     * @see com.sina.weibo.sdk.api.share.IWeiboShareAPI#launchWeiboPay(java.lang.String)
     */
    @Override
    public boolean launchWeiboPay(Activity act, String payArgs) {
        if (!isWeiboAppInstalled()) {
            return false;
        }
        //if (isSupportWeiboPay()) {
            Bundle bundle = new Bundle();
            bundle.putString("rawdata", payArgs);
            bundle.putInt(WBConstants.COMMAND_TYPE_KEY, WBConstants.COMMAND_PAY);
            bundle.putString(WBConstants.TRAN, String.valueOf(System.currentTimeMillis()));
            return launchWeiboActivity(act, WBConstants.ACTIVITY_WEIBO_PAY, mWeiboInfo.getPackageName(), mAppKey, bundle);
        //}
    }

    private boolean launchWeiboActivity(Activity activity, String action, String pkgName, String appkey, Bundle data) {
        if (null == activity 
                || TextUtils.isEmpty(action) 
                || TextUtils.isEmpty(pkgName) 
                || TextUtils.isEmpty(appkey)) {
            LogUtil.e(TAG, "launchWeiboActivity fail, invalid arguments");
            return false;
        }
        
        Intent intent = new Intent();
        intent.setPackage(pkgName);
        intent.setAction(action);
        String appPackage = activity.getPackageName();

        intent.putExtra(WBConstants.Base.SDK_VER, WBConstants.WEIBO_SDK_VERSION_CODE);
        intent.putExtra(WBConstants.Base.APP_PKG, appPackage);
        intent.putExtra(WBConstants.Base.APP_KEY, appkey);
        intent.putExtra(WBConstants.SDK.FLAG, WBConstants.WEIBO_FLAG_SDK);
        intent.putExtra(WBConstants.SIGN, MD5.hexdigest(Utility.getSign(activity, appPackage)));

        if (data != null) {
            intent.putExtras(data);
        }
        
        try {
            LogUtil.d(TAG, "launchWeiboActivity intent=" + intent + ", extra=" + intent.getExtras());
            activity.startActivityForResult(intent, WBConstants.SDK_ACTIVITY_FOR_RESULT_CODE);
        } catch (ActivityNotFoundException e) {
            LogUtil.e(TAG, e.getMessage());
            return false;
        }
        
        return true;
    }
    
    private void sendBroadcast(Context context, String action, String key, String packageName, Bundle data) {
        Intent intent = new Intent(action);       
        String appPackage = context.getPackageName();
        intent.putExtra(WBConstants.Base.SDK_VER, WBConstants.WEIBO_SDK_VERSION_CODE);
        intent.putExtra(WBConstants.Base.APP_PKG, appPackage);
        intent.putExtra(WBConstants.Base.APP_KEY, key);
        intent.putExtra(WBConstants.SDK.FLAG, WBConstants.WEIBO_FLAG_SDK);
        intent.putExtra(WBConstants.SIGN, MD5.hexdigest(Utility.getSign(context, appPackage)));
        
        if (!TextUtils.isEmpty(packageName)) {
            intent.setPackage(packageName);
        }
        
        if (data != null) {
            intent.putExtras(data);
        }
        
        LogUtil.d(TAG, "intent=" + intent + ", extra=" + intent.getExtras());
        context.sendBroadcast(intent, WBConstants.ACTION_WEIBO_SDK_PERMISSION);
    }
    
}
