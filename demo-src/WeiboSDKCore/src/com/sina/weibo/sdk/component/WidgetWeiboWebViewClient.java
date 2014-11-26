package com.sina.weibo.sdk.component;

import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.utils.Utility;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

class WidgetWeiboWebViewClient extends WeiboWebViewClient {
    
    private Activity mAct;
    private WidgetRequestParam mWidgetRequestParam;
    private WeiboAuthListener mListener;
    private WidgetRequestParam.WidgetRequestCallback mWidgetCallback;
    
    public WidgetWeiboWebViewClient(Activity activity,  WidgetRequestParam requestParam) {
        this.mAct = activity;
        this.mWidgetRequestParam = requestParam;
        this.mWidgetCallback = requestParam.getWidgetRequestCallback();
        this.mListener = requestParam.getAuthListener();
    }
    
    @Override
    public void onPageStarted( WebView view, String url, Bitmap favicon ) {
        if (mCallBack != null) {
            mCallBack.onPageStartedCallBack(view, url, favicon);
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public boolean shouldOverrideUrlLoading( WebView view, String url ) {
        if (mCallBack != null) {
            mCallBack.shouldOverrideUrlLoadingCallBack(view, url);
        }
        
        boolean needClose = url.startsWith(WeiboSdkBrowser.BROWSER_CLOSE_SCHEME);
        if (url.startsWith(WeiboSdkBrowser.BROWSER_CLOSE_SCHEME) 
                || url.startsWith(WeiboSdkBrowser.BROWSER_WIDGET_SCHEME)) {
            Bundle bundle = Utility.parseUri(url);
            if (!bundle.isEmpty()) {
                if (mListener != null) {
                    mListener.onComplete(bundle);
                }
            }
            if (mWidgetCallback != null) {
                mWidgetCallback.onWebViewResult(url);
            }
            if (needClose) {
                WeiboSdkBrowser.closeBrowser(mAct, 
                        mWidgetRequestParam.getAuthListenerKey(), 
                        mWidgetRequestParam.getWidgetRequestCallbackKey());
            }
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
    
}