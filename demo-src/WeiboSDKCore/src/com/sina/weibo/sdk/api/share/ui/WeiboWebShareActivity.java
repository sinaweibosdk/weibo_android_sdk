//package com.sina.weibo.sdk.api.share.ui;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.net.http.SslError;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.webkit.CookieManager;
//import android.webkit.SslErrorHandler;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.sina.weibo.sdk.api.ImageObject;
//import com.sina.weibo.sdk.api.MusicObject;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.VideoObject;
//import com.sina.weibo.sdk.api.VoiceObject;
//import com.sina.weibo.sdk.api.WebpageObject;
//import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.auth.WeiboAuthListener;
//import com.sina.weibo.sdk.component.WeiboCallbackManager;
//import com.sina.weibo.sdk.constant.WBConstants;
//import com.sina.weibo.sdk.exception.WeiboException;
//import com.sina.weibo.sdk.net.AsyncWeiboRunner;
//import com.sina.weibo.sdk.net.RequestListener;
//import com.sina.weibo.sdk.net.WeiboParameters;
//import com.sina.weibo.sdk.utils.Base64;
//import com.sina.weibo.sdk.utils.LogUtil;
//import com.sina.weibo.sdk.utils.ResourceManager;
//import com.sina.weibo.sdk.utils.Utility;
//
//public class WeiboWebShareActivity extends Activity {
//    private final static String TAG = "WeiboWebShareActivity";
//    
//    /** Strings **/
//    private static final String LOADING_EN    = "Loading...";
//    private static final String LOADING_ZH_CN = "加载中..."; 
//    private static final String LOADING_ZH_TW = "載入中...";
//    
//    public static final String KEY_APPKEY = "key_appkey";
//    public static final String KEY_TOKEN = "key_token";
//    public static final String KEY_HASH = "key_hash";
//    public static final String KEY_PACKAGE = "key_package";
//    public static final String KEY_SHARE_CALLBACK_ID = "key_share_callback_id";
//    
//    /**
//     * 网页请求参数
//     */
//    private static final String SHARE_URL = "http://service.weibo.com/share/mobilesdk.php";
//    private static final String UPLOAD_PIC_URL = "http://service.weibo.com/share/mobilesdk_uppic.php";
//    
//    private static final String REQ_PARAM_TITLE = "title";
//    private static final String REQ_PARAM_VERSION = "version";
//    private static final String REQ_PARAM_SOURCE = "source";
//    private static final String REQ_PARAM_AID = "aid";
//    private static final String REQ_PARAM_PACKAGENAME = "packagename";
//    private static final String REQ_PARAM_KEY_HASH = "key_hash";
//    private static final String REQ_PARAM_TOKEN = "access_token";
//    private static final String REQ_PARAM_PICINFO = "picinfo";
//    
//    private static final String REQ_UPLOAD_PIC_PARAM_IMG = "img";
//    
//    /**
//     * 网页Respons参数
//     */
//    private static final String RESP_SUCC_CODE = "0";
//    private static final String RESP_PARAM_CODE = "code";
//    private static final String RESP_PARAM_MSG = "msg";
//    
//    private static final String RESP_UPLOAD_PIC_PARAM_CODE = "code";
//    private static final String RESP_UPLOAD_PIC_PARAM_DATA = "data";
//    private static final int RESP_UPLOAD_PIC_SUCC_CODE = 1;
//    
//    /** 关闭浏览器WebView的Scheme */
//    public static final String BROWSER_CLOSE_SCHEME = "sinaweibo://browser/close";
//    
//    // UI elements
//    /** 授权认证的  WebView */
//    private WebView mWebView;
//    /** Loading 对话框 */
//    private ProgressDialog mLoadingDlg;
//    
//    private WeiboAuthListener authListener;
//    private String callBackId;
//    private String token;
//    private String appKey;
//    private String keyHash;
//    private String appPackage;
//    private String shareTitle = "";
//    private byte[] imgDataBase64;
//    
//    /**
//     * 初始化窗口熟悉等设置。
//     */
//    private void initWindow() {
//        mWebView = new WebView(this);
//        mWebView.setBackgroundColor(Color.WHITE);
//        addContentView(mWebView, new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//    }
//
//    /**
//     * 初始化 WebView 设置。
//     */
//    @SuppressLint("SetJavaScriptEnabled")
//    private void initWebView() {
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        // 取消浏览器记忆密码功能
//        mWebView.getSettings().setSavePassword(false);
//        //mWebView.getSettings().
//        mWebView.getSettings().setUserAgentString(Utility.generateUA(this));
//        mWebView.setWebViewClient(new WeiboWebViewClient());
//        mWebView.requestFocus();
//        mWebView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
//    }
//
//    /**
//     * 初始化 Loading 对话框。
//     */
//    private void initLoadingDlg() {
//        // Add loading dialog
//        mLoadingDlg = new ProgressDialog(this);
//        mLoadingDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mLoadingDlg.setMessage(ResourceManager.getString(this, 
//                LOADING_EN, LOADING_ZH_CN, LOADING_ZH_TW));
//        mLoadingDlg.show();
//    }
//    
//    /**
//     * 该类用于处理加载网页时各种回调。
//     * @see {@link WebViewClient}
//     */
//    private class WeiboWebViewClient extends WebViewClient {
//        
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            LogUtil.d(TAG, "shouldOverrideUrlLoading URL: " + url);
//            if (url.startsWith(BROWSER_CLOSE_SCHEME)) {
//                Bundle bundle = Utility.parseUri(url);
//                if (!bundle.isEmpty()) {
//                    if (authListener != null) {
//                        authListener.onComplete(bundle);
//                    }
//                }
//                String errCode = bundle.getString(RESP_PARAM_CODE);
//                String errMsg = bundle.getString(RESP_PARAM_MSG);
//                if (TextUtils.isEmpty(errCode)) {
//                    sendSdkCancleResponse(WeiboWebShareActivity.this);
//                } else {
//                    if (!RESP_SUCC_CODE.equals(errCode)) {
//                        sendSdkErrorResponse(WeiboWebShareActivity.this, errMsg);
//                    } else {
//                        sendSdkOkResponse(WeiboWebShareActivity.this);
//                    }
//                }
//                finish();
//                return true;
//            }
//            return super.shouldOverrideUrlLoading(view, url);
//        }
//
//        @Override
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            closeProgress();
//            LogUtil.d(TAG, "onReceivedError: " + "errorCode = " + errorCode + 
//                    ", description = " + description + 
//                    ", failingUrl = " + failingUrl);
//            super.onReceivedError(view, errorCode, description, failingUrl);
//            sendSdkErrorResponse(WeiboWebShareActivity.this, description);
//            finish();
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            LogUtil.d(TAG, "onPageStarted URL: " + url);
//            super.onPageStarted(view, url, favicon);
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            LogUtil.d(TAG, "onPageFinished URL: " + url);
//            closeProgress();
//            super.onPageFinished(view, url);
//        }
//
//        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            closeProgress();
//            if (error != null) {
//                LogUtil.d(TAG, "onReceivedSslError error: " + error.toString());
//            }
//            handler.cancel();
//            sendSdkErrorResponse(WeiboWebShareActivity.this, "ReceivedSslError");
//            finish();
//        }
//        
//    }
//
//    private void closeProgress() {
//        if (mLoadingDlg != null && mLoadingDlg.isShowing()) {
//            mLoadingDlg.dismiss();
//        }
//    }
//    
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        sendSdkCancleResponse(this);
//    }
//
//    @Override
//    protected void onCreate( Bundle savedInstanceState ) {
//        setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
//        super.onCreate(savedInstanceState);
//        initData();
//        initWindow();
//        initLoadingDlg();
//        initWebView();
//        startShare();
//    }
//    
//    private WeiboParameters buildUploadPicParam(WeiboParameters param) {
//        if (!hasImage()) {
//            return param;
//        }
//        String imgDataBase64Str = new String(imgDataBase64);
//        param.put(REQ_UPLOAD_PIC_PARAM_IMG, imgDataBase64Str);
//        return param;
//    }
//    
//    private void startShare() {
//        LogUtil.d(TAG, "Enter startShare()............");
//        if (hasImage()) {
//            LogUtil.d(TAG, "loadUrl hasImage............");
//            WeiboParameters param = new WeiboParameters(appKey);
//            param = buildUploadPicParam(param);
//            new AsyncWeiboRunner(this).requestAsync(UPLOAD_PIC_URL, 
//                    param, "POST", new RequestListener() {
//                        @Override
//                        public void onWeiboException( WeiboException e ) {
//                            LogUtil.d(TAG, "post onWeiboException "+e.getMessage());
//                            sendSdkErrorResponse(WeiboWebShareActivity.this, e.getMessage());
//                            finish();
//                        }
//                        @Override
//                        public void onComplete( String response ) {
//                            LogUtil.d(TAG, "post onComplete : " + response);
//                            UploadPicResult result = UploadPicResult.parse(response);
//                            if (result != null && 
//                                    result.code == RESP_UPLOAD_PIC_SUCC_CODE 
//                                    && !TextUtils.isEmpty(result.picId)) { //succ
//                                mWebView.loadUrl(buildUrl(result.picId));
//                            } else { //error
//                                sendSdkErrorResponse(WeiboWebShareActivity.this, "upload pic faild");
//                                finish();
//                            }
//                        }
//                    });
//        } else {
//            mWebView.loadUrl(buildUrl());
//        }
//    }
//    
//    private static class UploadPicResult {
//        private int code = ~RESP_UPLOAD_PIC_SUCC_CODE;
//        private String picId; 
//        
//        private UploadPicResult() {}
//        
//        public static UploadPicResult parse(String resp) {
//            if (TextUtils.isEmpty(resp)) {
//                return null;
//            }
//            UploadPicResult result = new UploadPicResult();
//            try {
//                JSONObject obj = new JSONObject(resp);
//                result.code = obj.optInt(RESP_UPLOAD_PIC_PARAM_CODE, 
//                        ~RESP_UPLOAD_PIC_SUCC_CODE);
//                result.picId = obj.optString(RESP_UPLOAD_PIC_PARAM_DATA, "");
//            } catch (JSONException e) {
//            }
//            return result;
//        }
//    }
//    
//    private String buildUrl() {
//        return buildUrl("");
//    }
//    
//    private String buildUrl(String picid) {
//        StringBuilder url = new StringBuilder(SHARE_URL);
//        
//        url.append("?").append(REQ_PARAM_TITLE).append("=").append(shareTitle);
//        
//        url.append("&").append(REQ_PARAM_VERSION).append("=").append(WBConstants.WEIBO_SDK_VERSION_NAME);
//        
//        if (!TextUtils.isEmpty(appKey)) {
//            url.append("&").append(REQ_PARAM_SOURCE).append("=").append(appKey);
//        }
//        if (!TextUtils.isEmpty(token)) {
//            url.append("&").append(REQ_PARAM_TOKEN).append("=").append(token);
//        }
//        String aid = Utility.getAid(this, appKey);
//        if (!TextUtils.isEmpty(aid)) {
//            url.append("&").append(REQ_PARAM_AID).append("=").append(aid);
//        }
//        if (!TextUtils.isEmpty(appPackage)) {
//            url.append("&").append(REQ_PARAM_PACKAGENAME).append("=").append(appPackage);
//        }
//        if (!TextUtils.isEmpty(keyHash)) {
//            url.append("&").append(REQ_PARAM_KEY_HASH).append("=").append(keyHash);
//        }
//        if (!TextUtils.isEmpty(picid)) {
//            url.append("&").append(REQ_PARAM_PICINFO).append("=").append(picid);
//        }
//        return url.toString();
//    }
//    
//    private void initData() {
//        Intent intent = getIntent();
//        appKey = intent.getStringExtra(KEY_APPKEY);
//        appPackage = intent.getStringExtra(KEY_PACKAGE);
//        keyHash = intent.getStringExtra(KEY_HASH);
//        token = intent.getStringExtra(KEY_TOKEN);
//        callBackId = intent.getStringExtra(KEY_SHARE_CALLBACK_ID);
//        if (TextUtils.isEmpty(appKey)) {
//            sendSdkErrorResponse(this, "send appkey is null!!!");
//            finish();
//            return;
//        }
//        if (TextUtils.isEmpty(appPackage)) {
//            sendSdkErrorResponse(this, "send package is null!!!");
//            finish();
//            return;
//        }
//        if (TextUtils.isEmpty(keyHash)) {
//            sendSdkErrorResponse(this, "send keyhash is null!!!");
//            finish();
//            return;
//        }
//        if (TextUtils.isEmpty(callBackId)) {
//            sendSdkErrorResponse(this, "send error internal!!!");
//            finish();
//            return;
//        }
//        
//        authListener = WeiboCallbackManager.getInstance(this).getWeiboAuthListener(callBackId);
//        WeiboCallbackManager.getInstance(this).removeWeiboAuthListener(callBackId);
//        
//        handleSharedMessage(intent.getExtras());
//    }
//    
//    private void sendSdkResponse( Activity activity, int errCode, String errMsg ) {
//        Bundle bundle = getIntent().getExtras();
//        if (bundle == null) {
//            return;
//        }
//        Intent intent = new Intent(WBConstants.ACTIVITY_REQ_SDK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        intent.setPackage(bundle.getString(WBConstants.Base.APP_PKG));
//
//        intent.putExtras(bundle);
//        intent.putExtra(WBConstants.Base.APP_PKG, activity.getPackageName());
//        intent.putExtra(WBConstants.Response.ERRCODE, errCode);
//        intent.putExtra(WBConstants.Response.ERRMSG, errMsg);
//        try {
//            activity.startActivityForResult(intent, WBConstants.SDK_ACTIVITY_FOR_RESULT_CODE);
//        } catch (ActivityNotFoundException e) {
//        }
//    }
//    
//    private void sendSdkCancleResponse( Activity activity ) {
//        sendSdkResponse(activity, WBConstants.ErrorCode.ERR_CANCEL, "send cancel!!!");
//    }
//
//    private void sendSdkOkResponse( Activity activity ) {
//        sendSdkResponse(activity, WBConstants.ErrorCode.ERR_OK, "send ok!!!");
//    }
//    
//    private void sendSdkErrorResponse( Activity activity, String msg ) {
//        sendSdkResponse(activity, WBConstants.ErrorCode.ERR_FAIL, msg);
//    }
//    
//    private boolean hasImage() {
//        if (imgDataBase64 != null && imgDataBase64.length > 0) {
//            return true;
//        }
//        return false;
//    }
//    
//    private void handleSharedMessage( Bundle bundle ) {
//        WeiboMultiMessage multiMessage = new WeiboMultiMessage();
//        multiMessage.toObject(bundle);
//        
//        StringBuilder content = new StringBuilder();
//        if (multiMessage.textObject instanceof TextObject) {
//            TextObject textObject = (TextObject) multiMessage.textObject;
//            content.append(textObject.text);
//        } 
//        if (multiMessage.imageObject instanceof ImageObject) {
//            ImageObject imageObject = (ImageObject) multiMessage.imageObject;
//            handleMblogPic(imageObject.imagePath, imageObject.imageData);
//        } 
//        if (multiMessage.mediaObject instanceof TextObject) {
//            TextObject textObject = (TextObject) multiMessage.mediaObject;
//            content.append(textObject.text);
//        } 
//        if (multiMessage.mediaObject instanceof ImageObject) {
//            ImageObject imageObject = (ImageObject) multiMessage.mediaObject;
//            handleMblogPic(imageObject.imagePath, imageObject.imageData);
//        }
//        if (multiMessage.mediaObject instanceof WebpageObject) {
//            WebpageObject webPageObject = (WebpageObject) multiMessage.mediaObject;
//            content.append(" ").append(webPageObject.actionUrl);
//        }
//        if (multiMessage.mediaObject instanceof MusicObject) {
//            MusicObject musicObject = (MusicObject) multiMessage.mediaObject;
//            content.append(" ").append(musicObject.actionUrl);
//        }
//        if (multiMessage.mediaObject instanceof VideoObject) {
//            VideoObject videoObject = (VideoObject) multiMessage.mediaObject;
//            content.append(" ").append(videoObject.actionUrl);
//        }
//        if (multiMessage.mediaObject instanceof VoiceObject) {
//            VoiceObject voiceObject = (VoiceObject) multiMessage.mediaObject;
//            content.append(" ").append(voiceObject.actionUrl);
//        }
//        
//        shareTitle = content.toString();
//    }
//
//    private void handleMblogPic(String picPath, byte[] thumbData) {
//        try {
//            if (!TextUtils.isEmpty(picPath)) {
//                File picFile = new File(picPath);
//                if (picFile.exists() && picFile.canRead() && picFile.length() > 0) {
//                    byte[] tmpPic = new byte[(int)picFile.length()];
//                    FileInputStream fis = null;
//                    try {
//                        fis = new FileInputStream(picFile);
//                        fis.read(tmpPic);
//                        imgDataBase64 = Base64.encodebyte(tmpPic);
//                        return;
//                    } catch (IOException e) {
//                        tmpPic = null;
//                    } finally {
//                        try {
//                            if (fis != null) {
//                                fis.close();
//                            }
//                        } catch (Exception e) {}
//                    }
//                    
//                }
//            }
//        } catch (SecurityException e) {}
//
//        if (thumbData != null && thumbData.length > 0) {
//            imgDataBase64 = Base64.encodebyte(thumbData);
//        }
//    }
//    
//    @Override
//    protected void onResume() {
//        super.onResume();
//        
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        removeCookies();
//    }
//    
//    private void removeCookies() {
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeAllCookie();
//    }
//    
//}
