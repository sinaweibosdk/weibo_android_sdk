package com.sina.weibo.sdk.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.component.ShareRequestParam.UploadPicResult;
import com.sina.weibo.sdk.component.view.LoadingBar;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.sina.weibo.sdk.utils.ResourceManager;
import com.sina.weibo.sdk.utils.Utility;

public class WeiboSdkBrowser extends Activity implements BrowserRequestCallBack {
    private final static String TAG = WeiboSdkBrowser.class.getName();
    
    /** Strings **/
    private static final String CANCEL_EN    = "Close";
    private static final String CANCEL_ZH_CN = "关闭"; 
    private static final String CANCEL_ZH_TW = "关闭";
    
    private static final String EMPTY_PROMPT_BAD_NETWORK_UI_EN = "A network error occurs, please tap the button to reload";
    private static final String EMPTY_PROMPT_BAD_NETWORK_UI_ZH_CN = "网络出错啦，请点击按钮重新加载";
    private static final String EMPTY_PROMPT_BAD_NETWORK_UI_ZH_TW = "網路出錯啦，請點擊按鈕重新載入";
    
    private static final String CHANNEL_DATA_ERROR_EN ="channel_data_error";
    private static final String CHANNEL_DATA_ERROR_ZH_CN ="重新加载";
    private static final String CHANNEL_DATA_ERROR_ZH_TW ="重新載入";
    
    @SuppressWarnings("unused")
    private static final String WEIBOBROWSER_NO_TITLE_EN     = "No Title";
    @SuppressWarnings("unused")
    private static final String WEIBOBROWSER_NO_TITLE_ZH_CN = "无标题";
    @SuppressWarnings("unused")
    private static final String WEIBOBROWSER_NO_TITLE_ZH_TW = "無標題";

    private static final String LOADINFO_EN = "Loading....";
    private static final String LOADINFO_ZH_CN = "加载中....";
    private static final String LOADINFO_ZH_TW = "載入中....";

    
    /** scheme **/
    /** 关闭浏览器WebView的Scheme */
    public static final String BROWSER_CLOSE_SCHEME = "sinaweibo://browser/close";
    /** Widget请求回调结果地址（非关闭网页） **/
    public static final String BROWSER_WIDGET_SCHEME = "sinaweibo://browser/datatransfer";
    
    /** 从 Intent 传进来的指定的 title(优先显示) **/
    private String mSpecifyTitle;
    /** 从网页中读取的 title **/
    private String mHtmlTitle;
    private boolean isLoading;
    private String mUrl;
    
    private boolean isErrorPage;
    
    private TextView mLeftBtn;
    private TextView mTitleText;
    private WebView mWebView;
    private LoadingBar mLoadingBar;
    private LinearLayout mLoadErrorView;
    private Button mLoadErrorRetryBtn;
    
    /**
     * 请求类型
     */
    private BrowserRequestParamBase mRequestParam;
    /**
     * 浏览器处理逻辑封装
     */
    private WeiboWebViewClient mWeiboWebViewClient;
    
    
    public static void startAuth(Context context, 
            String url, AuthInfo authInfo, WeiboAuthListener listener) {
        AuthRequestParam reqParam = new AuthRequestParam(context);
        reqParam.setLauncher(BrowserLauncher.AUTH);
        reqParam.setUrl(url);
        reqParam.setAuthInfo(authInfo);
        reqParam.setAuthListener(listener);
        
        Intent intent = new Intent(context, WeiboSdkBrowser.class);
        intent.putExtras(reqParam.createRequestParamBundle());
        context.startActivity(intent);
    }
    
    public static void startShared(Context context, 
            String url, AuthInfo authInfo, WeiboAuthListener listener) {
        
    }
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        
        if (!initDataFromIntent(getIntent())) {
            finish();
            return;
        }
        
        setContentView();
        
        initWebView();
        
        if (isWeiboShareRequestParam(mRequestParam)) {
            startShare();
        } else {
            openUrl(mUrl);
        }
    }

    private boolean initDataFromIntent(Intent data) {
        mRequestParam = createBrowserRequestParam(data.getExtras());
        if (mRequestParam == null) {
            return false;
        }
        
        mUrl = mRequestParam.getUrl();
        if (TextUtils.isEmpty(mUrl)) {
            return false;
        }
        LogUtil.d(TAG, "LOAD URL : "+mUrl);
        
        mSpecifyTitle = mRequestParam.getSpecifyTitle();
        
        return true;
    }
    
    private void openUrl( String url ) {
        mWebView.loadUrl(url);
    }
    
    private void startShare() {
        LogUtil.d(TAG, "Enter startShare()............");
        final ShareRequestParam req = (ShareRequestParam) mRequestParam;
        if (req.hasImage()) {
            LogUtil.d(TAG, "loadUrl hasImage............");
            WeiboParameters param = new WeiboParameters(req.getAppKey());
            param = req.buildUploadPicParam(param);
            new AsyncWeiboRunner(this).requestAsync(ShareRequestParam.UPLOAD_PIC_URL, 
                    param, "POST", new RequestListener() {
                        @Override
                        public void onWeiboException( WeiboException e ) {
                            LogUtil.d(TAG, "post onWeiboException "+e.getMessage());
                            req.sendSdkErrorResponse(WeiboSdkBrowser.this, e.getMessage());
                            finish();
                        }
                        @Override
                        public void onComplete( String response ) {
                            LogUtil.d(TAG, "post onComplete : " + response);
                            UploadPicResult result = UploadPicResult.parse(response);
                            if (result != null && 
                                    result.getCode() == ShareRequestParam.RESP_UPLOAD_PIC_SUCC_CODE 
                                    && !TextUtils.isEmpty(result.getPicId())) { //succ
                                openUrl(req.buildUrl(result.getPicId()));
                            } else { //error
                                req.sendSdkErrorResponse(WeiboSdkBrowser.this, "upload pic faild");
                                finish();
                            }
                        }
                    });
        } else {
            openUrl(mUrl);
        }
    }
    
    /**
     * 初始化 WebView 设置。
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        // user-agent
        if (isWeiboShareRequestParam(mRequestParam)) { // 分享过来需要加上 user-agent
            mWebView.getSettings().setUserAgentString(Utility.generateUA(this));
        }
        // 取消浏览器记忆密码功能
        mWebView.getSettings().setSavePassword(false);
        mWebView.setWebViewClient(mWeiboWebViewClient);
        mWebView.setWebChromeClient(new WeiboChromeClient());
        mWebView.requestFocus();
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
    }
    
    private void setTopNavTitle(){
        mTitleText.setText(mSpecifyTitle);
        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                mRequestParam.execRequest(WeiboSdkBrowser.this, 
                        BrowserRequestParamBase.EXEC_REQUEST_ACTION_CANCEL);
                finish();
            }
        });
    }
    
    /**
     * 在一级页面，优先显示指定的标题；用户点击链接进入其他页面后，优先显示网页的标题
     **/
    private void updateTitleName() {
        String showTitle = "";
        
        if (!TextUtils.isEmpty(mHtmlTitle)) {
            showTitle = mHtmlTitle;
        } else if (!TextUtils.isEmpty(mSpecifyTitle)) {
            showTitle = mSpecifyTitle;
        }
        
        mTitleText.setText(showTitle);
    }
    
    private void setContentView() {
        final int TITLE_BAR_HEIGHT = 45;
        
        RelativeLayout contentLy = new RelativeLayout(this);
        contentLy.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, 
                ViewGroup.LayoutParams.FILL_PARENT));
        contentLy.setBackgroundColor(Color.WHITE);
        
        LinearLayout titleBarLy = new LinearLayout(this);
        titleBarLy.setId(1);
        titleBarLy.setOrientation(LinearLayout.VERTICAL);
        titleBarLy.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT, 
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        
        RelativeLayout titleBar = new RelativeLayout(this);
        titleBar.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, 
                ResourceManager.dp2px(this, TITLE_BAR_HEIGHT)));
        titleBar.setBackgroundDrawable(ResourceManager.getNinePatchDrawable(
                this, "weibosdk_navigationbar_background.9.png"));
        
        mLeftBtn = new TextView(this);
        mLeftBtn.setClickable(true);
        mLeftBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        mLeftBtn.setTextColor(ResourceManager.createColorStateList(0xFFFF8200, 0x66FF8200));
        mLeftBtn.setText(ResourceManager.getString(this, CANCEL_EN, CANCEL_ZH_CN, CANCEL_ZH_TW));
        RelativeLayout.LayoutParams leftBtnLp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT);
        leftBtnLp.addRule(RelativeLayout.ALIGN_LEFT);
        leftBtnLp.addRule(RelativeLayout.CENTER_VERTICAL);
        leftBtnLp.leftMargin = ResourceManager.dp2px(this, 10);
        leftBtnLp.rightMargin = ResourceManager.dp2px(this, 10);
        mLeftBtn.setLayoutParams(leftBtnLp);
        titleBar.addView(mLeftBtn);
        
        mTitleText = new TextView(this);
        mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mTitleText.setTextColor(0xFF525252);
        mTitleText.setEllipsize(TruncateAt.END);
        mTitleText.setSingleLine(true);
        mTitleText.setGravity(Gravity.CENTER);
        mTitleText.setMaxWidth(ResourceManager.dp2px(this, 160));
        RelativeLayout.LayoutParams titleTextLy = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleTextLy.addRule(RelativeLayout.CENTER_IN_PARENT);
        mTitleText.setLayoutParams(titleTextLy);
        titleBar.addView(mTitleText);
        
        TextView shadowBar = new TextView(this);
        shadowBar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, ResourceManager.dp2px(this, 2)));
        shadowBar.setBackgroundDrawable(
                ResourceManager.getNinePatchDrawable(this, "weibosdk_common_shadow_top.9.png"));
        
        mLoadingBar = new LoadingBar(this);
        mLoadingBar.setBackgroundColor(Color.TRANSPARENT);
        mLoadingBar.drawProgress(0);
        LinearLayout.LayoutParams loadingBarLy = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, ResourceManager.dp2px(this, 3));
        mLoadingBar.setLayoutParams(loadingBarLy);
        
        titleBarLy.addView(titleBar);
        titleBarLy.addView(shadowBar);
        titleBarLy.addView(mLoadingBar);
        
        mWebView = new WebView(this);
        mWebView.setBackgroundColor(Color.WHITE);
        RelativeLayout.LayoutParams webViewLp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, 
                RelativeLayout.LayoutParams.FILL_PARENT);
        webViewLp.addRule(RelativeLayout.BELOW, 1);
        mWebView.setLayoutParams(webViewLp);
        
        mLoadErrorView = new LinearLayout(this);
        mLoadErrorView.setVisibility(View.GONE);
        mLoadErrorView.setOrientation(LinearLayout.VERTICAL);
        mLoadErrorView.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams mLoadErrorViewLp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.FILL_PARENT, 
                        RelativeLayout.LayoutParams.FILL_PARENT);
        mLoadErrorViewLp.addRule(RelativeLayout.BELOW, 1);
        mLoadErrorView.setLayoutParams(mLoadErrorViewLp);
        
        ImageView loadErrorImg = new ImageView(this);
        loadErrorImg.setImageDrawable(ResourceManager.getDrawable(this, "weibosdk_empty_failed.png"));
        LinearLayout.LayoutParams loadErrorImgLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        loadErrorImgLp.leftMargin = loadErrorImgLp.topMargin 
                = loadErrorImgLp.rightMargin 
                = loadErrorImgLp.bottomMargin 
                = ResourceManager.dp2px(this, 8);
        loadErrorImg.setLayoutParams(loadErrorImgLp);
        mLoadErrorView.addView(loadErrorImg);
        
        TextView loadErrorContent = new TextView(this);
        loadErrorContent.setGravity(Gravity.CENTER_HORIZONTAL);
        loadErrorContent.setTextColor(0xFFBDBDBD);
        loadErrorContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        loadErrorContent.setText(ResourceManager.getString(this, 
                EMPTY_PROMPT_BAD_NETWORK_UI_EN, 
                EMPTY_PROMPT_BAD_NETWORK_UI_ZH_CN, 
                EMPTY_PROMPT_BAD_NETWORK_UI_ZH_TW));
        LinearLayout.LayoutParams loadErrorContentLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        loadErrorContent.setLayoutParams(loadErrorContentLp);
        mLoadErrorView.addView(loadErrorContent);
        
        mLoadErrorRetryBtn = new Button(this);
        mLoadErrorRetryBtn.setGravity(Gravity.CENTER);
        mLoadErrorRetryBtn.setTextColor(0xFF787878);
        mLoadErrorRetryBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mLoadErrorRetryBtn.setText(ResourceManager.getString(this, 
                CHANNEL_DATA_ERROR_EN, 
                CHANNEL_DATA_ERROR_ZH_CN, 
                CHANNEL_DATA_ERROR_ZH_TW));
        mLoadErrorRetryBtn.setBackgroundDrawable(ResourceManager.createStateListDrawable(this, 
                "weibosdk_common_button_alpha.9.png",
                "weibosdk_common_button_alpha_highlighted.9.png"));
        LinearLayout.LayoutParams loadErrorRetryBtnLp = new LinearLayout.LayoutParams(
                ResourceManager.dp2px(this, 142), 
                ResourceManager.dp2px(this, 46));
        loadErrorRetryBtnLp.topMargin = ResourceManager.dp2px(this, 10);
        mLoadErrorRetryBtn.setLayoutParams(loadErrorRetryBtnLp);
        mLoadErrorRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                openUrl(mUrl);
                isErrorPage = false;
            }
        });
        mLoadErrorView.addView(mLoadErrorRetryBtn);
        
        contentLy.addView(titleBarLy);
        contentLy.addView(mWebView);
        contentLy.addView(mLoadErrorView);

        setContentView(contentLy);
        
        setTopNavTitle();
    }
    
    protected void refreshAllViews() {
        if (isLoading) {
            setViewLoading();
        } else {
            setViewNormal();
        }
    }
    
    // 更新UI为正常模式
    private void setViewNormal() {
        updateTitleName();
        mLoadingBar.setVisibility(View.GONE);
    }

    // 更新UI为loading模式
    private void setViewLoading() {
        mTitleText.setText(ResourceManager.getString(this, 
                LOADINFO_EN, LOADINFO_ZH_CN, LOADINFO_ZH_TW));
        mLoadingBar.setVisibility(View.VISIBLE);
    }
    
    private void handleReceivedError( WebView view, int errorCode, String description,
            String failingUrl ) {
        if (!failingUrl.startsWith("sinaweibo")) {
            isErrorPage = true;
            promptError();
        }
    }
    
    private void promptError() {
        mLoadErrorView.setVisibility(View.VISIBLE);
        mWebView.setVisibility(View.GONE);
    }

    private void hiddenErrorPrompt() {
        mLoadErrorView.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
    }
    
    
    private class WeiboChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged( WebView view, int newProgress ) {
            mLoadingBar.drawProgress(newProgress);
            if (newProgress == 100) {
                isLoading = false;
                refreshAllViews();
            } else {
                if (!isLoading) {
                    isLoading = true;
                    refreshAllViews();
                }
            }
        }
        
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (!isWeiboCustomScheme(mUrl)) {
                mHtmlTitle = title;
                updateTitleName();
            }
        }
    }
    
    
    private boolean isWeiboCustomScheme(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if ("sinaweibo".equalsIgnoreCase(Uri.parse(url).getAuthority())) {
            return true;
        }
        return false;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        NetworkHelper.clearCookies(this);
        super.onDestroy();
    }
    
    @Override
    public boolean onKeyUp( int keyCode, KeyEvent event ) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            mRequestParam.execRequest(WeiboSdkBrowser.this, 
                    BrowserRequestParamBase.EXEC_REQUEST_ACTION_CANCEL);
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
    
    private BrowserRequestParamBase createBrowserRequestParam(Bundle data) {
        BrowserRequestParamBase result = null;
        BrowserLauncher launcher = (BrowserLauncher) data.getSerializable(
                BrowserRequestParamBase.EXTRA_KEY_LAUNCHER);
        if (launcher == BrowserLauncher.AUTH) {
            AuthRequestParam authRequestParam = new AuthRequestParam(this);
            authRequestParam.setupRequestParam(data);
            installAuthWeiboWebViewClient(authRequestParam);
            result = authRequestParam;
            return result;
        }
        else if (launcher == BrowserLauncher.SHARE) {
            ShareRequestParam shareRequestParam = new ShareRequestParam(this);
            shareRequestParam.setupRequestParam(data);
            installShareWeiboWebViewClient(shareRequestParam);
            result = shareRequestParam;
        }
        else if (launcher == BrowserLauncher.WIDGET) {
            WidgetRequestParam widgetRequestParam = new WidgetRequestParam(this);
            widgetRequestParam.setupRequestParam(data);
            installWidgetWeiboWebViewClient(widgetRequestParam);
            result = widgetRequestParam;
        }
        return result;
    }
    
    private boolean isWeiboShareRequestParam(BrowserRequestParamBase reqParam) {
        return (reqParam.getLauncher() == BrowserLauncher.SHARE);
    }
    
    private void installAuthWeiboWebViewClient(AuthRequestParam param) {
        mWeiboWebViewClient = new AuthWeiboWebViewClient(this, param);
        mWeiboWebViewClient.setBrowserRequestCallBack(this);
    }

    private void installShareWeiboWebViewClient(ShareRequestParam param) {
        ShareWeiboWebViewClient client = new ShareWeiboWebViewClient(this, param);
        client.setBrowserRequestCallBack(this);
        mWeiboWebViewClient = client;
    }
    
    private void installWidgetWeiboWebViewClient(WidgetRequestParam param) {
        WidgetWeiboWebViewClient client = new WidgetWeiboWebViewClient(this, param);
        client.setBrowserRequestCallBack(this);
        mWeiboWebViewClient = client;
    }
    
    @Override
    public void onPageStartedCallBack( WebView view, String url, Bitmap favicon ) {
        LogUtil.d(TAG, "onPageStarted URL: " + url);
        mUrl = url;
        if (!isWeiboCustomScheme(url)) {
            /**
            mHtmlTitle = ResourceManager.getString(getApplicationContext(), 
                    WEIBOBROWSER_NO_TITLE_EN, 
                    WEIBOBROWSER_NO_TITLE_ZH_CN, 
                    WEIBOBROWSER_NO_TITLE_ZH_TW);
                    **/
            mHtmlTitle = "";
        }
    }

    @Override
    public boolean shouldOverrideUrlLoadingCallBack( WebView view, String url ) {
        LogUtil.i(TAG, "shouldOverrideUrlLoading URL: " + url);
        return false;
    }

    @Override
    public void onPageFinishedCallBack( WebView view, String url ) {
        LogUtil.d(TAG, "onPageFinished URL: " + url);
        if (isErrorPage) {
            promptError();
        } else {
            isErrorPage = false;
            hiddenErrorPrompt();
        }
    }

    @Override
    public void onReceivedErrorCallBack( WebView view, int errorCode, String description,
            String failingUrl ) {
        LogUtil.d(TAG, "onReceivedError: " + "errorCode = " + errorCode + 
                ", description = " + description + 
                ", failingUrl = " + failingUrl);
        handleReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslErrorCallBack( WebView view, SslErrorHandler handler, SslError error ) {
        LogUtil.d(TAG, "onReceivedSslErrorCallBack.........");
    }
    
    public static void closeBrowser(Activity act, String authListenerKey, String widgetRequestCallbackKey) {
        WeiboCallbackManager manager = WeiboCallbackManager.getInstance(
                act.getApplicationContext());
        if (!TextUtils.isEmpty(authListenerKey)) {
            manager.removeWeiboAuthListener(authListenerKey);
            act.finish();
        }
        if (!TextUtils.isEmpty(widgetRequestCallbackKey)) {
            manager.removeWidgetRequestCallback(widgetRequestCallbackKey);
            act.finish();
        }
    }
    
}
