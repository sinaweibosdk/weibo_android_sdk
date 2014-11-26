///*
// * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.sina.weibo.sdk.auth;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.sina.weibo.sdk.auth.sso.SsoHandler;
//import com.sina.weibo.sdk.constant.WBConstants;
//import com.sina.weibo.sdk.net.WeiboParameters;
//import com.sina.weibo.sdk.utils.LogUtil;
//import com.sina.weibo.sdk.utils.NetworkHelper;
//import com.sina.weibo.sdk.utils.ResourceManager;
//import com.sina.weibo.sdk.utils.UIUtils;
//import com.sina.weibo.sdk.utils.Utility;
//
///**
// * 该类是进行 OAuth2.0 Web 授权认证的类，如果想使用 SSO 授权，请查看 {@link SsoHandler} 类。
// * 
// * @author SINA
// * @since 2013-10-08
// */
//public class WeiboAuth {
//    public final static String TAG = "Weibo_web_login";
//    
//    /** 获取 code 或 token 的 Base URL */
//    private static final String OAUTH2_BASE_URL = "https://open.weibo.cn/oauth2/authorize?";
//    /** 获取 code */
//    public static final int OBTAIN_AUTH_CODE  = 0;
//    /** 获取 token */
//    public static final int OBTAIN_AUTH_TOKEN = 1;
//    
//    /** 应用程序上下文环境 */
//    private Context mContext;
//    /** 微博授权认证所需信息 */
//    private AuthInfo mAuthInfo;
//
//    /**
//     * 该类用于保存授权认证所需要的信息。
//     */
//    public static class AuthInfo {
//        /** 第三方应用的 Appkey */
//        private String mAppKey      = "";
//        /** 第三方应用的回调页 */
//        private String mRedirectUrl = "";
//        /** 第三方应用申请的权限 */
//        private String mScope       = "";
//        /** 应用程序的包名 */
//        private String mPackageName = "";
//        /** 应用程序的签名的哈希码 */
//        private String mKeyHash     = "";
//        /** 保存第三方应用的 Appkey、回调页、 申请的权限、包名、MD5码等信息 */
//        private Bundle mBundle      = null;
//        
//        public AuthInfo(Context context, String appKey, String redirectUrl, String scope) {
//            mAppKey      = appKey;
//            mRedirectUrl = redirectUrl;
//            mScope       = scope;
//            
//            mPackageName = context.getPackageName();
//            mKeyHash     = Utility.getSign(context, mPackageName);
//            
//            initAuthBundle();
//        }
//        
//        public String getAppKey() {
//            return mAppKey;
//        }
//        
//        public String getRedirectUrl() {
//            return mRedirectUrl;
//        }
//        
//        public String getScope() {
//            return mScope;
//        }
//        
//        public String getPackageName() {
//            return mPackageName;
//        }
//        
//        public String getKeyHash() {
//            return mKeyHash;
//        }
//        
//        public Bundle getAuthBundle() {
//            return mBundle;
//        }
//
//        private void initAuthBundle() {
//            mBundle = new Bundle();
//            mBundle.putString(WBConstants.SSO_APP_KEY,      mAppKey);
//            mBundle.putString(WBConstants.SSO_REDIRECT_URL, mRedirectUrl);
//            mBundle.putString(WBConstants.SSO_USER_SCOPE,   mScope);
//            mBundle.putString(WBConstants.SSO_PACKAGE_NAME, mPackageName);
//            mBundle.putString(WBConstants.SSO_KEY_HASH,     mKeyHash);
//        }
//    }
//
//    /**
//     * 构造函数，创建微博实例。
//     * 
//     * @param context     应用程序上下文环境
//     * @param appKey      第三方应用的 APP_KEY
//     * @param redirectUrl 第三方应用的回调页
//     * @param scope       第三方应用申请的权限 
//     * 
//     * @return WeiboAuth 实例
//     */
//    public WeiboAuth(Context context, String appKey, String redirectUrl, String scope) {
//        mContext = context;
//        mAuthInfo = new AuthInfo(context, appKey, redirectUrl, scope);
//    }
//    
//    /**
//     * 构造函数，创建微博实例。
//     * 
//     * @param context  应用程序上下文环境
//     * @param authInfo 授权认证所需要的信息实例
//     */
//    public WeiboAuth(Context context, AuthInfo authInfo) {
//        mContext = context;
//        mAuthInfo = authInfo;
//    }
//
//    /**
//     * 获取授权认证所需要的信息。
//     * 
//     * @return 返回授权认证所信息实例
//     */
//    public AuthInfo getAuthInfo() {
//        return mAuthInfo;
//    }
//
//    /**
//     * 设置授权认证所需要的信息。
//     * 
//     * @param authInfo 授权认证所需要的信息
//     */
//    public void setAuthInfo(AuthInfo authInfo) {
//        mAuthInfo = authInfo;
//    }
//
//    /**
//     * 微博授权认证函数，用于获取 Token。
//     * 
//     * @param listener 微博授权认证的回调接口
//     */
//    public void anthorize(WeiboAuthListener listener) {
//        authorize(listener, OBTAIN_AUTH_TOKEN);
//    }
//    
//    /**
//     * 微博授权认证函数，用于获取 Token 或者 Code。
//     * <b>对于只想获取 Code 的情况，第三方需要自己通过 Code 来换取 Token，请参考 DEMO 实现。</b>
//     * 
//     * @param listener 微博授权认证的回调接口
//     * @param type     微博授权时，指定或者 code 还是 token，可以是以下常量中的一种：
//     *                 {@link #OBTAIN_AUTH_CODE}，{@link #OBTAIN_AUTH_TOKEN}。
//     */
//    public void authorize(WeiboAuthListener listener, int type) {
//        startDialog(listener, type);
//    }
//
//    /**
//     * 启动微博认证对话框。
//     * 
//     * @param listener 微博授权认证的回调接口
//     * @param type     微博授权时，指定或者 code 还是 token，可以是以下常量中的一种：
//     *                 {@link #OBTAIN_AUTH_CODE}，{@link #OBTAIN_AUTH_TOKEN}。
//     */
//    private void startDialog(WeiboAuthListener listener, int type) {
//        if (null == listener) {
//            return;
//        }
//        
//        WeiboParameters requestParams = new WeiboParameters();
//        requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_ID,     mAuthInfo.mAppKey);
//        requestParams.put(WBConstants.AUTH_PARAMS_REDIRECT_URL,  mAuthInfo.mRedirectUrl);
//        requestParams.put(WBConstants.AUTH_PARAMS_SCOPE,         mAuthInfo.mScope);
//        requestParams.put(WBConstants.AUTH_PARAMS_RESPONSE_TYPE, "code");
//        requestParams.put(WBConstants.AUTH_PARAMS_DISPLAY,       "mobile");
//        
//        // 对于想直接获取 Token 的情况，需要以下参数；只想获取 Code 的时，不需要以下参数
//        if (OBTAIN_AUTH_TOKEN == type) {
//            requestParams.put(WBConstants.AUTH_PARAMS_PACKAGE_NAME, mAuthInfo.mPackageName);
//            requestParams.put(WBConstants.AUTH_PARAMS_KEY_HASH,     mAuthInfo.mKeyHash);
//        }
//
//        String url = OAUTH2_BASE_URL + requestParams.encodeUrl();
//        if (!NetworkHelper.hasInternetPermission(mContext)) {
//            UIUtils.showAlert(mContext, "Error", "Application requires permission to access the Internet");
//        } else {
//            if (NetworkHelper.isNetworkAvailable(mContext)) {
//                new WeiboDialog(mContext, url, listener, this).show();
//            } else {
//            	String networkNotAvailable = ResourceManager.getString(mContext, ResourceManager.string_network_not_available);
//            	LogUtil.i(TAG, "String: " + networkNotAvailable);
//                UIUtils.showToast(mContext, networkNotAvailable, Toast.LENGTH_SHORT);
//            }
//        }
//    }
//}
