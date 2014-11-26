package com.sina.weibo.sdk.component;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboAuthException;
import com.sina.weibo.sdk.utils.Utility;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

class AuthWeiboWebViewClient extends WeiboWebViewClient {

    private Activity mAct;
    private AuthRequestParam mAuthRequestParam;
    private WeiboAuthListener mListener;
    
    private boolean isCallBacked = false;
    
    public AuthWeiboWebViewClient(Activity activity, AuthRequestParam requestParam) {
        this.mAct = activity;
        this.mAuthRequestParam = requestParam;
        this.mListener = mAuthRequestParam.getAuthListener();
    }
    
    @Override
    public void onPageStarted( WebView view, String url, Bitmap favicon ) {
        if (mCallBack != null) {
            mCallBack.onPageStartedCallBack(view, url, favicon);
        }
        
        AuthInfo authInfo = mAuthRequestParam.getAuthInfo();
        if (url.startsWith(authInfo.getRedirectUrl())) {
            if (!isCallBacked) {
                isCallBacked = true;
                handleRedirectUrl(url);
                view.stopLoading();
                
                WeiboSdkBrowser.closeBrowser(mAct, 
                        mAuthRequestParam.getAuthListenerKey(), null);
                return;
            }
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public boolean shouldOverrideUrlLoading( WebView view, String url ) {
        if (mCallBack != null) {
            mCallBack.shouldOverrideUrlLoadingCallBack(view, url);
        }
        
        // 针对 WebView 里的短信注册流程，需要在此单独处理 SMS 协议
        if (url.startsWith("sms:")) {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.putExtra("address", url.replace("sms:", ""));
            sendIntent.setType("vnd.android-dir/mms-sms");
            mAct.startActivity(sendIntent);
            return true;
        }
        else if (url.startsWith(WeiboSdkBrowser.BROWSER_CLOSE_SCHEME)) {
            if (mListener != null) {
                mListener.onCancel();
            }
            WeiboSdkBrowser.closeBrowser(mAct, 
                    mAuthRequestParam.getAuthListenerKey(), null);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageFinished( WebView view, String url ) {
        if (mCallBack != null) {
            mCallBack.onPageFinishedCallBack(view, url);
        }
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError( WebView view, int errorCode, String description, String failingUrl ) {
        if (mCallBack != null) {
            mCallBack.onReceivedErrorCallBack(view, errorCode, description, failingUrl);
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError( WebView view, SslErrorHandler handler, SslError error ) {
        handler.cancel();
        if (mCallBack != null) {
            mCallBack.onReceivedSslErrorCallBack(view, handler, error);
        }
        super.onReceivedSslError(view, handler, error);
    }
    
    
    /**
     * 当获取到回调页后，该函数被调用，用于处理 Server 返回的数据（包含 token 或者 error 信息）。
     * 
     * @param url 处理回调页（包含 token 或者 error 信息）
     */
    private void handleRedirectUrl(String url) {
        // 解析 URL 并将数据放到 Bundle 中
        Bundle values = Utility.parseUrl(url);

        String errorType = values.getString("error");
        String errorCode = values.getString("error_code");
        String errorDescription = values.getString("error_description");

        if (null == errorType && null == errorCode) {
            if (mListener != null) {
                mListener.onComplete(values);
            }
        } else {
            if (mListener != null) {
                mListener.onWeiboException(
                        new WeiboAuthException(errorCode, errorType, errorDescription));
            }
        }
    }
    
}
