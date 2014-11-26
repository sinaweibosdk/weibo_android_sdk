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
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.util.DisplayMetrics;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.view.Window;
//import android.view.WindowManager;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//
//import com.sina.weibo.sdk.exception.WeiboAuthException;
//import com.sina.weibo.sdk.exception.WeiboDialogException;
//import com.sina.weibo.sdk.utils.LogUtil;
//import com.sina.weibo.sdk.utils.NetworkHelper;
//import com.sina.weibo.sdk.utils.ResourceManager;
//import com.sina.weibo.sdk.utils.Utility;
//
///**
// * 该类是微博授权认证对话框，用来进行 Web 授权。
// * 
// * @author SINA
// * @since 2013-10-12
// */
//public class WeiboDialog extends Dialog {
//    private final static String TAG = "WeiboDialog";
//
//    /** WebView 容器距离它的容器顶部的高度：30dp */
//    private static final int WEBVIEW_CONTAINER_MARGIN_TOP = 25;
//    /** WebView 距离它的容器顶部的高度：10dp */
//    private static final int WEBVIEW_MARGIN = 10;
//    
//    /** 应用程序上下文环境 */
//    private Context mContext;
//
//    // UI elements
//    /** 根容器，放置 WebView 及其它 */
//    private RelativeLayout mRootContainer;
//    /** WebView 对应的容器 */
//    private RelativeLayout mWebViewContainer;
//    /** Loading 对话框 */
//    private ProgressDialog mLoadingDlg;
//    /** 授权认证的  WebView */
//    private WebView mWebView;
//    /** 当前 RootView 是否已 Detached */
//    private boolean mIsDetached = false;
//
//    /** 授权认证的 URL */
//    private String mAuthUrl;
//    /** 授权认证对应的回调 */
//    private WeiboAuthListener mListener;
//    /** 授权认证实例 */
//    private AuthInfo mWeibo;
//
//    /** Dialog 默认Theme */
//    private static int theme = android.R.style.Theme_Translucent_NoTitleBar;
//
//    /**
//     * 授权对话框构造函数。
//     * @param context  应用程序上下文环境
//     * @param authUrl  授权认证的 URL
//     * @param listener 授权认证对应的回调
//     * @param weibo    授权认证实例
//     */
//    public WeiboDialog(Context context, String authUrl, WeiboAuthListener listener, AuthInfo weibo) {
//        super(context, theme);
//        mAuthUrl  = authUrl;
//        mListener = listener;
//        mContext  = context;
//        mWeibo    = weibo;
//    }
//
//    /**
//     * 按返回键时，调用该函数。
//     * @see {@link Dialog#onBackPressed}
//     */
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        
//        if (mListener != null) {
//            mListener.onCancel();
//        }
//    }
//    
//    /**
//     * 对话框消失时，调用该函数。
//     * @see {@link Dialog#dismiss}
//     */
//    @Override
//    public void dismiss() {
//        if (!mIsDetached) {
//            if (mLoadingDlg != null && mLoadingDlg.isShowing()) {
//                mLoadingDlg.dismiss();
//            }
//            
//            super.dismiss();
//        }
//    }
//    
//    @Override
//    public void onAttachedToWindow() {
//        mIsDetached = false;
//        super.onAttachedToWindow();
//    }
//    
//    /**
//     * @see {@link Window.Callback#onDetachedFromWindow()}
//     */
//    @Override
//    public void onDetachedFromWindow() {
//        if (mWebView != null) {
//            mWebViewContainer.removeView(mWebView);
//            mWebView.stopLoading();
//            mWebView.removeAllViews();
//            mWebView.destroy();
//            mWebView = null;
//        }
//        
//        mIsDetached = true;
//        super.onDetachedFromWindow();
//    }
//
//    /**
//     * Dialog 初始化时被调用。
//     * @see {@link Dialog#onCreate}
//     */
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        
//        // 初始化 Dialog 的相关属性
//        initWindow();
//        // 初始化 Loading 对话框
//        initLoadingDlg();
//        // 初始化 WebView
//        initWebView();
//        // 添加关闭按钮
//        initCloseButton();
//    }
//
//    /**
//     * 初始化窗口熟悉等设置。
//     */
//    private void initWindow() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFeatureDrawableAlpha(Window.FEATURE_OPTIONS_PANEL, 0);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        
//        mRootContainer = new RelativeLayout(getContext());
//        mRootContainer.setBackgroundColor(Color.TRANSPARENT);
//        addContentView(mRootContainer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//    }
//
//    /**
//     * 初始化 Loading 对话框。
//     */
//    private void initLoadingDlg() {
//        // Add loading dialog
//        mLoadingDlg = new ProgressDialog(/*mContext*/getContext());
//        // mSpinner.setCanceledOnTouchOutside(false);
//        mLoadingDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //mLoadingDlg.setMessage(mContext.getString(R.string.com_sina_weibo_sdk_loading));
//        mLoadingDlg.setMessage(ResourceManager.getString(mContext, ResourceManager.string_loading));
//    }
//
//    /**
//     * 初始化 WebView 设置。
//     */
//    @SuppressLint("SetJavaScriptEnabled")
//	private void initWebView() {
//        mWebViewContainer = new RelativeLayout(getContext());
//        mWebView = new WebView(getContext());
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        // 取消浏览器记忆密码功能
//        mWebView.getSettings().setSavePassword(false);
//        mWebView.setWebViewClient(new WeiboDialog.WeiboWebViewClient());
//        mWebView.requestFocus();
//        mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
//        mWebView.setVisibility(View.INVISIBLE);
//        //mWebView.setFocusableInTouchMode(false);
//
//        NetworkHelper.clearCookies(mContext, mAuthUrl);
//        mWebView.loadUrl(mAuthUrl);
//    
//        RelativeLayout.LayoutParams webViewContainerLayout = new RelativeLayout.LayoutParams(
//                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//
//        RelativeLayout.LayoutParams webviewLayout = new RelativeLayout.LayoutParams(
//                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//        
//        // 设置 WebView LayoutParam
//        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
//        float density = dm.density;
//        int margin = (int) (WEBVIEW_MARGIN * density);
//        webviewLayout.setMargins(margin, margin, margin, margin);      
//        Drawable background = ResourceManager.getNinePatchDrawable(mContext, ResourceManager.drawable_dialog_background);
//        //mWebViewContainer.setBackground(background);
//        mWebViewContainer.setBackgroundDrawable(background);
//
//        //mWebViewContainer.setBackgroundResource(R.drawable.weibosdk_dialog_bg);
//        mWebViewContainer.addView(mWebView, webviewLayout);
//        mWebViewContainer.setGravity(Gravity.CENTER);
//        
//        Drawable drawable = ResourceManager.getDrawable(mContext, ResourceManager.drawable_dialog_close_button);
//        int width = drawable.getIntrinsicWidth() / 2 + 1/* + (int)(5 * dm.density)*/;
//        
//        // 设置 WebView Container LayoutParam
//        /*
//        webViewContainerLayout.leftMargin   = ResourceManager.getDimensionPixelSize(ResourceManager.dimen_dialog_left_margin);
//        webViewContainerLayout.topMargin    = ResourceManager.getDimensionPixelSize(ResourceManager.dimen_dialog_top_margin);
//        webViewContainerLayout.rightMargin  = ResourceManager.getDimensionPixelSize(ResourceManager.dimen_dialog_right_margin);
//        webViewContainerLayout.bottomMargin = ResourceManager.getDimensionPixelSize(ResourceManager.dimen_dialog_bottom_margin);
//        */
//        webViewContainerLayout.setMargins(width, (int)(WEBVIEW_CONTAINER_MARGIN_TOP * dm.density)/*width*/, width, width);
//        mRootContainer.addView(mWebViewContainer, webViewContainerLayout);
//    }
//
//    /**
//     * 初始化关闭 按钮。
//     */
//    private void initCloseButton() {
//        ImageView closeImage = new ImageView(mContext);
//        Drawable drawable = ResourceManager.getDrawable(mContext, ResourceManager.drawable_dialog_close_button);
//        closeImage.setImageDrawable(drawable);
//        closeImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                
//                if (mListener != null) {
//                    mListener.onCancel();
//                }
//            }
//        });
//
//        // 设置关闭按钮布局，在 mWebViewContainer 的左上角，
//        // 关闭按钮的中心与 mWebViewContainer 的左上角基本保持一致
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mWebViewContainer.getLayoutParams();
//        layoutParams.leftMargin = params.leftMargin - drawable.getIntrinsicWidth() / 2 + 5;
//        layoutParams.topMargin = params.topMargin - drawable.getIntrinsicHeight() / 2 + 5;
//        mRootContainer.addView(closeImage, layoutParams);
//    }
//
//    /**
//     * 该类用于处理加载网页时各种回调。
//     * @see {@link WebViewClient}
//     */
//    private class WeiboWebViewClient extends WebViewClient {
//        private boolean isCallBacked = false;
//
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            LogUtil.i(TAG, "load URL: " + url);
//            
//            // 针对 WebView 里的短信注册流程，需要在此单独处理 SMS 协议
//            if (url.startsWith("sms:")) {
//                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//                sendIntent.putExtra("address", url.replace("sms:", ""));
//                sendIntent.setType("vnd.android-dir/mms-sms");
//                WeiboDialog.this.getContext().startActivity(sendIntent);
//                return true;
//            }
//            return super.shouldOverrideUrlLoading(view, url);
//        }
//
//        @Override
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            LogUtil.d(TAG, "onReceivedError: " + "errorCode = " + errorCode + 
//                    ", description = " + description + 
//                    ", failingUrl = " + failingUrl);
//            super.onReceivedError(view, errorCode, description, failingUrl);
//            
//            if (mListener != null) {
//                mListener.onWeiboException(new WeiboDialogException(description, errorCode, failingUrl));
//            }
//            WeiboDialog.this.dismiss();
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            LogUtil.d(TAG, "onPageStarted URL: " + url);
//            if (url.startsWith(mWeibo.getRedirectUrl())) {
//                if (!isCallBacked) {
//                    isCallBacked = true;
//                    handleRedirectUrl(url);
//                    view.stopLoading();
//                    WeiboDialog.this.dismiss();
//                    
//                    return;
//                }
//            }
//            super.onPageStarted(view, url, favicon);
//            
//            if (!mIsDetached && mLoadingDlg != null && !mLoadingDlg.isShowing()) {
//                mLoadingDlg.show();
//            }
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            LogUtil.d(TAG, "onPageFinished URL: " + url);
//            super.onPageFinished(view, url);
//            if (!mIsDetached && mLoadingDlg != null/* && mLoadingDlg.isShowing()*/) {
//                mLoadingDlg.dismiss();
//            }
//            
//            if (mWebView != null) {
//                mWebView.setVisibility(View.VISIBLE);
//            }
//        }
//
//        /*public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) 
//        { 
//            handler.proceed(); 
//        }*/
//    }
//
//    /**
//     * 当获取到回调页后，该函数被调用，用于处理 Server 返回的数据（包含 token 或者 error 信息）。
//     * 
//     * @param url 处理回调页（包含 token 或者 error 信息）
//     */
//    private void handleRedirectUrl(String url) {
//        // 解析 URL 并将数据放到 Bundle 中
//        Bundle values = Utility.parseUrl(url);
//
//        String errorType = values.getString("error");
//        String errorCode = values.getString("error_code");
//        String errorDescription = values.getString("error_description");
//
//        if (null == errorType && null == errorCode) {
//            mListener.onComplete(values);
//        } else {
//            mListener.onWeiboException(
//                    new WeiboAuthException(errorCode, errorType, errorDescription));
//        }
//    }
//}
