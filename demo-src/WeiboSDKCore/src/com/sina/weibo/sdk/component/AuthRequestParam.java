package com.sina.weibo.sdk.component;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;

public class AuthRequestParam extends BrowserRequestParamBase {
    public static final String EXTRA_KEY_AUTHINFO = "key_authinfo";
    public static final String EXTRA_KEY_LISTENER = "key_listener";
    
    private AuthInfo mAuthInfo;
    private WeiboAuthListener mAuthListener;
    private String mAuthListenerKey;
    
    public AuthRequestParam(Context context) {
        super(context);
        mLaucher = BrowserLauncher.AUTH;
    }

    @Override
    protected void onSetupRequestParam( Bundle data ) {
        Bundle authInfoBundle = data.getBundle(EXTRA_KEY_AUTHINFO);
        if (authInfoBundle != null) {
            mAuthInfo = AuthInfo.parseBundleData(mContext, authInfoBundle);
        }
        mAuthListenerKey = data.getString(EXTRA_KEY_LISTENER);
        if (!TextUtils.isEmpty(mAuthListenerKey)) {
            mAuthListener = WeiboCallbackManager
                    .getInstance(mContext)
                    .getWeiboAuthListener(mAuthListenerKey);
        }
    }

    public void onCreateRequestParamBundle(Bundle data) {
        if (mAuthInfo != null) {
            data.putBundle(
                    EXTRA_KEY_AUTHINFO, 
                    mAuthInfo.getAuthBundle());
        }
        if (mAuthListener != null) {
            WeiboCallbackManager manager = WeiboCallbackManager
                    .getInstance(mContext);
            mAuthListenerKey = manager.genCallbackKey();
            manager.setWeiboAuthListener(mAuthListenerKey, mAuthListener);
            data.putString(EXTRA_KEY_LISTENER, mAuthListenerKey);
        }
    }
    
    public void execRequest(Activity act, int action) {
        if (action == EXEC_REQUEST_ACTION_CANCEL) {
            if (mAuthListener != null) {
                mAuthListener.onCancel();
            }
            WeiboSdkBrowser.closeBrowser(act, mAuthListenerKey, null);
        }
    }

    public AuthInfo getAuthInfo() {
        return mAuthInfo;
    }

    public void setAuthInfo( AuthInfo mAuthInfo ) {
        this.mAuthInfo = mAuthInfo;
    }

    public WeiboAuthListener getAuthListener() {
        return mAuthListener;
    }
    
    public String getAuthListenerKey() {
        return mAuthListenerKey;
    }

    public void setAuthListener( WeiboAuthListener mAuthListener ) {
        this.mAuthListener = mAuthListener;
    }
    
}
