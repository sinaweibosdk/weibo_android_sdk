package com.sina.weibo.sdk.component;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.MD5;
import com.sina.weibo.sdk.utils.Utility;

public class WidgetRequestParam extends BrowserRequestParamBase {
    public static final String EXTRA_KEY_WIDGET_CALLBACK = "key_widget_callback";
    
    /**
     * 网页请求参数（关注组件）
     */
    public static final String REQ_PARAM_ATTENTION_FUID = "fuid";
    /**
     * 网页请求参数（评论组件）
     */
    public static final String REQ_PARAM_COMMENT_TOPIC = "q";
    public static final String REQ_PARAM_COMMENT_CONTENT = "content";
    public static final String REQ_PARAM_COMMENT_CATEGORY = "category";
    
    private WeiboAuthListener mAuthListener;
    private String mAuthListenerKey;
    private WidgetRequestCallback mWidgetRequestCallback;
    private String mWidgetRequestCallbackKey;
    private String mAppPackage;
    private String mToken;
    private String mAppKey;
    private String mHashKey;
    
    /**
     * 关注组件的参数
     */
    private String mAttentionFuid;
    
    /**
     * 评论组件的参数
     */
    private String mCommentContent;
    private String mCommentTopic;
    private String mCommentCategory;
    
    public WidgetRequestParam(Context context) {
        super(context);
        mLaucher = BrowserLauncher.WIDGET;
    }

    @Override
    protected void onSetupRequestParam( Bundle data ) {
        mAppKey = data.getString(ShareRequestParam.REQ_PARAM_SOURCE);
        mAppPackage = data.getString(ShareRequestParam.REQ_PARAM_PACKAGENAME);
        mHashKey = data.getString(ShareRequestParam.REQ_PARAM_KEY_HASH);
        mToken = data.getString(ShareRequestParam.REQ_PARAM_TOKEN);
        
        /**
         * 关注组件参数
         */
        mAttentionFuid = data.getString(REQ_PARAM_ATTENTION_FUID);
        
        /**
         * 评论组件参数
         */
        mCommentTopic = data.getString(REQ_PARAM_COMMENT_TOPIC);
        mCommentContent = data.getString(REQ_PARAM_COMMENT_CONTENT);
        mCommentCategory = data.getString(REQ_PARAM_COMMENT_CATEGORY);
        
        mAuthListenerKey = data.getString(AuthRequestParam.EXTRA_KEY_LISTENER);
        if (!TextUtils.isEmpty(mAuthListenerKey)) {
            mAuthListener = WeiboCallbackManager
                    .getInstance(mContext)
                    .getWeiboAuthListener(mAuthListenerKey);
        }
        mWidgetRequestCallbackKey = data.getString(EXTRA_KEY_WIDGET_CALLBACK);
        if (!TextUtils.isEmpty(mWidgetRequestCallbackKey)) {
            mWidgetRequestCallback = WeiboCallbackManager
                    .getInstance(mContext)
                    .getWidgetRequestCallback(mWidgetRequestCallbackKey);
        }
        
        String baseUrl = mUrl;
        mUrl = buildUrl(baseUrl);
    }
    
    public void onCreateRequestParamBundle(Bundle data) {
        mAppPackage = mContext.getPackageName();
        if (!TextUtils.isEmpty(mAppPackage)) {
            mHashKey = MD5.hexdigest(Utility.getSign(mContext, mAppPackage));
        }
        data.putString(ShareRequestParam.REQ_PARAM_TOKEN, mToken);
        data.putString(ShareRequestParam.REQ_PARAM_SOURCE, mAppKey);
        data.putString(ShareRequestParam.REQ_PARAM_PACKAGENAME, mAppPackage);
        data.putString(ShareRequestParam.REQ_PARAM_KEY_HASH, mHashKey);
        
        /**
         * 关注组件参数
         */
        data.putString(REQ_PARAM_ATTENTION_FUID, mAttentionFuid);
        
        /**
         * 评论组件参数
         */
        data.putString(REQ_PARAM_COMMENT_TOPIC, mCommentTopic);
        data.putString(REQ_PARAM_COMMENT_CONTENT, mCommentContent);
        data.putString(REQ_PARAM_COMMENT_CATEGORY, mCommentCategory);
        
        WeiboCallbackManager manager = WeiboCallbackManager.getInstance(mContext);
        if (mAuthListener != null) {
            mAuthListenerKey = manager.genCallbackKey();
            manager.setWeiboAuthListener(mAuthListenerKey, mAuthListener);
            data.putString(AuthRequestParam.EXTRA_KEY_LISTENER, mAuthListenerKey);
        }
        if (mWidgetRequestCallback != null) {
            mWidgetRequestCallbackKey = manager.genCallbackKey();
            manager.setWidgetRequestCallback(mWidgetRequestCallbackKey, mWidgetRequestCallback);
            data.putString(EXTRA_KEY_WIDGET_CALLBACK, mWidgetRequestCallbackKey);
        }
        
    }
    
    private String buildUrl(String baseUrl) {
        StringBuilder url = new StringBuilder(baseUrl);
        
        url.append("?").append(ShareRequestParam.REQ_PARAM_VERSION).append("=").append(WBConstants.WEIBO_SDK_VERSION_CODE);
        
        if (!TextUtils.isEmpty(mAppKey)) {
            url.append("&").append(ShareRequestParam.REQ_PARAM_SOURCE).append("=").append(mAppKey);
        }
        if (!TextUtils.isEmpty(mToken)) {
            url.append("&").append(ShareRequestParam.REQ_PARAM_TOKEN).append("=").append(mToken);
        }
        String aid = Utility.getAid(mContext, mAppKey);
        if (!TextUtils.isEmpty(aid)) {
            url.append("&").append(ShareRequestParam.REQ_PARAM_AID).append("=").append(aid);
        }
        if (!TextUtils.isEmpty(mAppPackage)) {
            url.append("&").append(ShareRequestParam.REQ_PARAM_PACKAGENAME).append("=").append(mAppPackage);
        }
        if (!TextUtils.isEmpty(mHashKey)) {
            url.append("&").append(ShareRequestParam.REQ_PARAM_KEY_HASH).append("=").append(mHashKey);
        }
        
        /**
         * 关注参数
         */
        if (!TextUtils.isEmpty(mAttentionFuid)) {
            url.append("&").append(REQ_PARAM_ATTENTION_FUID).append("=").append(mAttentionFuid);
        }
        /**
         * 评论参数
         */
        if (!TextUtils.isEmpty(mCommentTopic)) {
            url.append("&").append(REQ_PARAM_COMMENT_TOPIC).append("=").append(mCommentTopic);
        }
        if (!TextUtils.isEmpty(mCommentContent)) {
            url.append("&").append(REQ_PARAM_COMMENT_CONTENT).append("=").append(mCommentContent);
        }
        if (!TextUtils.isEmpty(mCommentCategory)) {
            url.append("&").append(REQ_PARAM_COMMENT_CATEGORY).append("=").append(mCommentCategory);
        }
        
        return url.toString();
    }
    
    public String getAttentionFuid() {
        return this.mAttentionFuid;
    }
    
    public void setAttentionFuid(String fuid) {
        this.mAttentionFuid = fuid;
    }
    
    public String getCommentContent() {
        return this.mCommentContent;
    }
    
    public void setCommentContent(String content) {
        this.mCommentContent = content;
    }
    
    public String getCommentTopic() {
        return this.mCommentTopic;
    }
    
    public void setCommentTopic(String topic) {
        this.mCommentTopic = topic;
    }

    public String getCommentCategory() {
        return this.mCommentCategory;
    }
    
    public void setCommentCategory(String category) {
        this.mCommentCategory = category;
    }
    
    public String getToken() {
        return mToken;
    }

    public void setToken( String mToken ) {
        this.mToken = mToken;
    }

    public String getAppKey() {
        return mAppKey;
    }

    public void setAppKey( String mAppKey ) {
        this.mAppKey = mAppKey;
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
    
    public WidgetRequestCallback getWidgetRequestCallback() {
        return this.mWidgetRequestCallback;
    }
    
    public String getWidgetRequestCallbackKey() {
        return this.mWidgetRequestCallbackKey;
    }
    
    public void setWidgetRequestCallback(WidgetRequestCallback l) {
        this.mWidgetRequestCallback = l;
    }

    @Override
    public void execRequest(Activity act, int action) {
        if (action == EXEC_REQUEST_ACTION_CANCEL) {
            WeiboSdkBrowser.closeBrowser(act, 
                    mAuthListenerKey, mWidgetRequestCallbackKey);
        }
    }
    
    public static interface WidgetRequestCallback {
        void onWebViewResult(String url);
    }
    
}
