package com.sina.weibo.sdk.component;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

interface BrowserRequestCallBack {
    public void onPageStartedCallBack( WebView view, String url, Bitmap favicon );
    public boolean shouldOverrideUrlLoadingCallBack( WebView view, String url );
    public void onPageFinishedCallBack( WebView view, String url );
    public void onReceivedErrorCallBack( WebView view, int errorCode, String description, String failingUrl );
    public void onReceivedSslErrorCallBack( WebView view, SslErrorHandler handler, SslError error );
}

abstract class WeiboWebViewClient extends WebViewClient {
    
    protected BrowserRequestCallBack mCallBack;
    
    public void setBrowserRequestCallBack(BrowserRequestCallBack callback) {
        this.mCallBack = callback;
    }
    
}