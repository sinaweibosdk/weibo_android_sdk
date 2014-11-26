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

package com.sina.weibo.sdk.auth.sso;

import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.sina.sso.RemoteSSO;
import com.sina.weibo.sdk.WeiboAppManager;
import com.sina.weibo.sdk.WeiboAppManager.WeiboInfo;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboDialogException;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.utils.AidTask;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.SecurityHelper;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 该类用于处理 SSO 认证功能，无需输入用户名和密码即可通过微博账号访问经过授权的第三方应用。 
 * 使用 SSO 登录前，需要检查手机上是否已经安装新浪微博客户端，目前仅 3.0.0 及以上微博客户端版
 * 本支持 SSO，如果未安装，将自动转为浏览器页面 Oauth2.0 进行认证。
 * 
 * @author SINA
 * @since 2013-10-08
 */
public class SsoHandler {
    private static final String TAG = "Weibo_SSO_login";
    
    /** 官方微博客户端包名 */
    //private static final String DEFAULT_SINA_WEIBO_PACKAGE_NAME       = "com.sina.weibo";
    /** 官方微博客户端 SSO 服务名 */
    private static final String DEFAULT_WEIBO_REMOTE_SSO_SERVICE_NAME = "com.sina.weibo.remotessoservice";
    
    /** 启动 SSOActivity 时传入的请求码 */
    private static final int REQUEST_CODE_SSO_AUTH = 32973;

    /** 微博 Web 授权类，提供登陆等功能  */
    private WebAuthHandler mWebAuthHandler;
    /** 微博认证授权回调 */
    private WeiboAuthListener mAuthListener;
    /** 启动 SSO 登陆的原始 Activity */
    private Activity mAuthActivity;
    /** 启动 SSOActivity 时传入的请求码，当前仅可使用 {@link #REQUEST_CODE_SSO_AUTH} */
    private int mSSOAuthRequestCode;    // XXX: TBR??
    /** 微博应用信息 **/
    private WeiboInfo mWeiboInfo;
    /** 三方应用信息 **/
    private AuthInfo mAuthInfo;
    
    private static enum AuthType {
        ALL, SsoOnly, WebOnly
    }
    
    public static final String AUTH_FAILED_MSG = "auth failed!!!!!";
    public static final String AUTH_FAILED_NOT_INSTALL_MSG = "not install weibo client!!!!!";
    
    /** 远程 Service 连接对象 */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mWebAuthHandler.anthorize(mAuthListener);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            RemoteSSO remoteSSOservice = RemoteSSO.Stub.asInterface(service);
            try {
                String ssoPackageName = remoteSSOservice.getPackageName();
                String ssoActivityName = remoteSSOservice.getActivityName();
                
                // 解除 Service 绑定
                mAuthActivity.getApplicationContext().unbindService(mConnection);
                
                if (!startSingleSignOn(ssoPackageName, ssoActivityName)) {
                    mWebAuthHandler.anthorize(mAuthListener);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 构造函数。
     * 
     * @param activity  发起认证的 Activity
     * @param weiboAuth {@link WeiboAuth} 实例
     */
    public SsoHandler(Activity activity, AuthInfo weiboAuthInfo) {
        mAuthActivity = activity;
        mAuthInfo = weiboAuthInfo;
        mWebAuthHandler = new WebAuthHandler(activity, weiboAuthInfo);
        mWeiboInfo = WeiboAppManager.getInstance(activity).getWeiboInfo();
        AidTask.getInstance(mAuthActivity).aidTaskInit(weiboAuthInfo.getAppKey());
    }

    /**
     * SSO 认证授权入口函数。
     * 
     * @param activity 发起认证的 Activity
     * @param listener 用于接收认证信息的回调
     */
    public void authorize(WeiboAuthListener listener) {
        authorize(REQUEST_CODE_SSO_AUTH, listener, AuthType.ALL);
    }

    /**
     * SSO 认证授权入口函数
     * 注：仅仅通过微博客户端进行认证，在客户端没有安装的情况下
     *     不会弹出网页进行授权
     * 
     * @param activity 发起认证的 Activity
     * @param listener 用于接收认证信息的回调
     */
    public void authorizeClientSso(WeiboAuthListener listener) {
        authorize(REQUEST_CODE_SSO_AUTH, listener, AuthType.SsoOnly);
    }
    
    /**
     * SSO 认证授权入口函数
     * 注：仅仅通过WEB认证
     * 
     * @param activity 发起认证的 Activity
     * @param listener 用于接收认证信息的回调
     */
    public void authorizeWeb(WeiboAuthListener listener) {
        authorize(REQUEST_CODE_SSO_AUTH, listener, AuthType.WebOnly);
    }
    
    /**
     * SSO 认证授权入口函数。
     * 
     * @param requestCode 启动 SSOActivity 时传入的请求码，当前仅可使用 {@link #REQUEST_CODE_SSO_AUTH}
     * @param listener    用于接收认证信息的回调
     * @param authType  {@link AuthType} 授权类型
     */
    private void authorize(int requestCode, WeiboAuthListener listener, AuthType authType) {
        mSSOAuthRequestCode = requestCode;
        mAuthListener = listener;

        boolean onlyClientSso = false;
        if (authType == AuthType.SsoOnly) {
            onlyClientSso = true;
        }
        if (authType == AuthType.WebOnly) {
            if (listener != null) {
                mWebAuthHandler.anthorize(listener);
            }
            return;
        }
        
        // Prefer single sign-on, where available.
        boolean bindSucced = bindRemoteSSOService(mAuthActivity.getApplicationContext());
        
        // Otherwise fall back to traditional dialog authorize.
        if (!bindSucced) {
            if (onlyClientSso) {
                if (mAuthListener != null) {
                    mAuthListener.onWeiboException(
                            new WeiboException(AUTH_FAILED_NOT_INSTALL_MSG));
                }
            } else {
                mWebAuthHandler.anthorize(mAuthListener);
            }
        }
    }

    /**
     * 使用 SSO 授权时，需要手动调用该函数。
     * <p>
     * 重要：使用 SSO 授权的Activity必须重写 {@link Activity#onActivityResult(int, int, Intent)}，
     *       并在内部调用该函数，否则无法授权成功。</p>
     * <p>Sample Code：</p>
     * <pre class="prettyprint">
     * protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     *     super.onActivityResult(requestCode, resultCode, data);
     *     
     *     // 在此处调用
     *     mSsoHandler.onActivityResult(requestCode, resultCode, data);
     * }
     * </pre>
     * @param requestCode 请查看 {@link Activity#onActivityResult(int, int, Intent)}
     * @param resultCode  请查看 {@link Activity#onActivityResult(int, int, Intent)}
     * @param data        请查看 {@link Activity#onActivityResult(int, int, Intent)}
     */
    // TODO：需要简化
    public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
        LogUtil.d(TAG, "requestCode: " + requestCode + ", resultCode: " + resultCode + ", data: " + data);
        if (requestCode == mSSOAuthRequestCode) {
            
            // Successfully redirected.
            if (resultCode == Activity.RESULT_OK) {
                if (!SecurityHelper.checkResponseAppLegal(mAuthActivity, mWeiboInfo, data)) {
                    return;
                }
    
                // Check OAuth 2.0/2.10 error code.
                String error = data.getStringExtra("error");
                if (null == error) {
                    error = data.getStringExtra("error_type");
                }
    
                // error occurred.
                if (error != null) {
                    if (error.equals("access_denied") || error.equals("OAuthAccessDeniedException")) {
                        LogUtil.d(TAG, "Login canceled by user.");
                        
                        mAuthListener.onCancel();
                    } else {
                        String description = data.getStringExtra("error_description");
                        if (description != null) {
                            error = error + ":" + description;
                        }
                        
                        LogUtil.d(TAG, "Login failed: " + error);
                        mAuthListener.onWeiboException(
                                new WeiboDialogException(error, resultCode, description));
                    }
                } else { // No errors.
                    
                    Bundle bundle = data.getExtras();
                    Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(bundle);
                    
                    if (accessToken != null && accessToken.isSessionValid()) {
                        LogUtil.d(TAG, "Login Success! " + accessToken.toString());
                        mAuthListener.onComplete(bundle);
                    } else {
                        LogUtil.d(TAG, "Failed to receive access token by SSO");
                        // startDialogAuth(mAuthActivity, mAuthPermissions);
                        mWebAuthHandler.anthorize(mAuthListener);
                    }
                }
    
                // An error occurred before we could be redirected.
            } else if (resultCode == Activity.RESULT_CANCELED) {
    
                // An Android error occured.
                if (data != null) {
                    LogUtil.d(TAG, "Login failed: " + data.getStringExtra("error"));
                    mAuthListener.onWeiboException(
                            new WeiboDialogException(
                                    data.getStringExtra("error"), 
                                    data.getIntExtra("error_code", -1), 
                                    data.getStringExtra("failing_url")));
                } else { 
                    // User pressed the 'back' button.
                    LogUtil.d(TAG, "Login canceled by user.");
                    mAuthListener.onCancel();
                }
            }
        }
    }
    
    /**
     * 检测用于 SSO 授权的远程服务否存在。
     * 
     * @param context     当前应用程序上下文环境
     * @param packageName 指定需要绑定的远程 SSO 服务的包名
     * 
     * @return 如果远程服务存在，返回对应的组件名；否则，返回 null。
     */
    public static ComponentName isServiceExisted(Context context, String packageName) {
        ActivityManager activityManager = 
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = 
                activityManager.getRunningServices(Integer.MAX_VALUE);

        for (RunningServiceInfo runningServiceInfo : serviceList) {
            ComponentName serviceName = runningServiceInfo.service;
            
            if (serviceName.getPackageName().equals(packageName)) {
                if (serviceName.getClassName().equals(packageName + ".business.RemoteSSOService")) {
                    return serviceName;
                }
            }
        }

        return null;
    }

    /**
     * 绑定远程 SSO 服务。
     * 
     * @param context     当前应用程序上下文环境
     * @param packageName 指定需要绑定的远程 SSO 服务的包名
     * 
     * @return 绑定成功，返回 true；否则，返回 false。
     */
    private boolean bindRemoteSSOService(Context context) {
        if (!isWeiboAppInstalled()) {
            return false;
        }
        
        String pkgName = mWeiboInfo.getPackageName();
        Intent intent = new Intent(DEFAULT_WEIBO_REMOTE_SSO_SERVICE_NAME);
        intent.setPackage(pkgName);
        
        if (!context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)) {
            intent = new Intent(DEFAULT_WEIBO_REMOTE_SSO_SERVICE_NAME);
            return context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
        
        return true;
    }

    /**
     *  启动 SSO 登陆的 Activity。
     *  
     * @param ssoPackageName  SSO 登陆的包名
     * @param ssoActivityName SSO 登陆对应的 Activity
     * 
     * @return 启动成功，返回 true；否则，返回 false。
     */
    private boolean startSingleSignOn(String ssoPackageName, String ssoActivityName) {
        
        boolean bSucceed = true;
        Intent intent = new Intent();
        // 设置要启动的 Package 名和 Activity 名
        intent.setClassName(ssoPackageName, ssoActivityName);
        
        // 设置微博授权所需参数
        intent.putExtras(mWebAuthHandler.getAuthInfo().getAuthBundle());
        
        // 设置安全验证所需参数
        intent.putExtra(WBConstants.COMMAND_TYPE_KEY, WBConstants.COMMAND_SSO);
        intent.putExtra(WBConstants.TRAN, String.valueOf(System.currentTimeMillis()));

        // 增加aid参数
        intent.putExtra("aid", Utility.getAid(mAuthActivity, mAuthInfo.getAppKey()));
        
        // 判读是否存在有合法的微博客户端
        if (!SecurityHelper.validateAppSignatureForIntent(mAuthActivity, intent)) {
            return false;
        }

        // 设置AID参数
        String aid = Utility.getAid(mAuthActivity, mAuthInfo.getAppKey());
        if (!TextUtils.isEmpty(aid)) {
            intent.putExtra(WBConstants.AID, aid);
        }
        
        try {
            // 启动 SSO 授权 Activity
            mAuthActivity.startActivityForResult(intent, mSSOAuthRequestCode);
        } catch (ActivityNotFoundException e) {
            bSucceed = false;
        }

        // 解除 Service 绑定
        //mAuthActivity.getApplicationContext().unbindService(mConnection);
        return bSucceed;
    }
    
    /**
     * 判断微博是否安装
     * @return
     */
    public boolean isWeiboAppInstalled() {
        return (mWeiboInfo != null && mWeiboInfo.isLegal()) ? true : false;
    }
}
