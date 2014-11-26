package com.sina.weibo.sdk.auth;

import android.content.Context;
import android.os.Bundle;

import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 该类用于保存授权认证所需要的信息。
 * 
 * @author SINA
 * @since 2014-05-06
 */
public class AuthInfo {
    /** 第三方应用的 Appkey */
    private String mAppKey      = "";
    /** 第三方应用的回调页 */
    private String mRedirectUrl = "";
    /** 第三方应用申请的权限 */
    private String mScope       = "";
    /** 应用程序的包名 */
    private String mPackageName = "";
    /** 应用程序的签名的哈希码 */
    private String mKeyHash     = "";
    
    public AuthInfo(Context context, String appKey, String redirectUrl, String scope) {
        mAppKey      = appKey;
        mRedirectUrl = redirectUrl;
        mScope = scope;
        mPackageName = context.getPackageName();
        mKeyHash = Utility.getSign(context, mPackageName);
    }
    
    public String getAppKey() {
        return mAppKey;
    }
    
    public String getRedirectUrl() {
        return mRedirectUrl;
    }
    
    public String getScope() {
        return mScope;
    }
    
    public String getPackageName() {
        return mPackageName;
    }
    
    public String getKeyHash() {
        return mKeyHash;
    }
    
    public Bundle getAuthBundle() {
        Bundle mBundle = new Bundle();
        mBundle.putString(WBConstants.SSO_APP_KEY,      mAppKey);
        mBundle.putString(WBConstants.SSO_REDIRECT_URL, mRedirectUrl);
        mBundle.putString(WBConstants.SSO_USER_SCOPE,   mScope);
        mBundle.putString(WBConstants.SSO_PACKAGE_NAME, mPackageName);
        mBundle.putString(WBConstants.SSO_KEY_HASH,     mKeyHash);
        return mBundle;
    }

    public static AuthInfo parseBundleData(Context context, Bundle data) {
        String appKey = data.getString(WBConstants.SSO_APP_KEY);
        String redirectUrl = data.getString(WBConstants.SSO_REDIRECT_URL);
        String scope = data.getString(WBConstants.SSO_USER_SCOPE);
        return new AuthInfo(context, appKey, redirectUrl, scope);
    }
    
}
