package com.sina.weibo.sdk.component;

import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.utils.Utility;
import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

class ShareWeiboWebViewClient extends WeiboWebViewClient {
    /**
     * 网页Respons参数
     */
    private static final String RESP_SUCC_CODE = "0";
    private static final String RESP_PARAM_CODE = "code";
    private static final String RESP_PARAM_MSG = "msg";
    
    private Activity mAct;
    private ShareRequestParam mShareRequestParam;
    private WeiboAuthListener mListener;
    
    public ShareWeiboWebViewClient(Activity activity,  ShareRequestParam requestParam) {
        this.mAct = activity;
        this.mShareRequestParam = requestParam;
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
        
        if (url.startsWith(WeiboSdkBrowser.BROWSER_CLOSE_SCHEME)) {
            Bundle bundle = Utility.parseUri(url);
            if (!bundle.isEmpty()) {
                if (mListener != null) {
                    mListener.onComplete(bundle);
                }
            }
            String errCode = bundle.getString(RESP_PARAM_CODE);
            String errMsg = bundle.getString(RESP_PARAM_MSG);
            if (TextUtils.isEmpty(errCode)) {
                mShareRequestParam.sendSdkCancleResponse(mAct);
            } else {
                if (!RESP_SUCC_CODE.equals(errCode)) {
                    mShareRequestParam.sendSdkErrorResponse(mAct, errMsg);
                } else {
                    mShareRequestParam.sendSdkOkResponse(mAct);
                }
            }
            WeiboSdkBrowser.closeBrowser(mAct, 
                    mShareRequestParam.getAuthListenerKey(), null);
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
        
        mShareRequestParam.sendSdkErrorResponse(mAct, description);
        
        WeiboSdkBrowser.closeBrowser(mAct, 
                mShareRequestParam.getAuthListenerKey(), null);
    }

    @Override
    public void onReceivedSslError( WebView view, SslErrorHandler handler, SslError error ) {
        if (mCallBack != null) {
            mCallBack.onReceivedSslErrorCallBack(view, handler, error);
        }
        
        handler.cancel();
        mShareRequestParam.sendSdkErrorResponse(mAct, "ReceivedSslError");
        
        WeiboSdkBrowser.closeBrowser(mAct, 
                mShareRequestParam.getAuthListenerKey(), null);
    }
    
}