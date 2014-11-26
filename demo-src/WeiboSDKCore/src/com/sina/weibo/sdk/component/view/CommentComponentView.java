package com.sina.weibo.sdk.component.view;

import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.component.WeiboSdkBrowser;
import com.sina.weibo.sdk.component.WidgetRequestParam;
import com.sina.weibo.sdk.utils.ResourceManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentComponentView extends FrameLayout {
    
    private static final String COMMENT_H5 = "http://widget.weibo.com/distribution/socail_comments_sdk.php";
    
    private static final String ALREADY_COMMENT_EN    = "Comment";
    private static final String ALREADY_COMMENT_ZH_CN = "微博热评"; 
    private static final String ALREADY_COMMENT_ZH_TW = "微博熱評";
    
    private RequestParam mCommentParam;
    
    private LinearLayout mContentLy;
    
    public CommentComponentView(Context context) {
        super(context);
        init(context);
    }

    public CommentComponentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CommentComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    private void init(Context context) {
        mContentLy = new LinearLayout(context);
        mContentLy.setOrientation(LinearLayout.HORIZONTAL);
        mContentLy.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT));
        
        ImageView logoIv = new ImageView(context);
        logoIv.setImageDrawable(ResourceManager.getDrawable(
                context, "sdk_weibo_logo.png"));
        LinearLayout.LayoutParams logoIvLp = new LinearLayout.LayoutParams(
                ResourceManager.dp2px(getContext(), 20), 
                ResourceManager.dp2px(getContext(), 20));
        logoIvLp.gravity = Gravity.CENTER_VERTICAL;
        logoIv.setLayoutParams(logoIvLp);
        
        TextView commentTv = new TextView(context);
        commentTv.setText(ResourceManager.getString(context, 
                ALREADY_COMMENT_EN, 
                ALREADY_COMMENT_ZH_CN, 
                ALREADY_COMMENT_ZH_TW));
        commentTv.setTextColor(0xFFFF8200);
        commentTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        commentTv.setIncludeFontPadding(false);
        LinearLayout.LayoutParams commentTvLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                LinearLayout.LayoutParams.WRAP_CONTENT);
        commentTvLp.gravity = Gravity.CENTER_VERTICAL;
        commentTvLp.leftMargin = ResourceManager.dp2px(getContext(), 4);
        commentTv.setLayoutParams(commentTvLp);
        
        mContentLy.addView(logoIv);
        mContentLy.addView(commentTv);
        addView(mContentLy);
        
        commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                execAttented();
            }
        });
        
    }
    
    public void setCommentParam(RequestParam param) {
        this.mCommentParam = param;
    }
    
    private void execAttented() {
        WidgetRequestParam req = new WidgetRequestParam(getContext());
        req.setUrl(COMMENT_H5);
        req.setSpecifyTitle(ResourceManager.getString(getContext(), 
                ALREADY_COMMENT_EN, 
                ALREADY_COMMENT_ZH_CN, 
                ALREADY_COMMENT_ZH_TW));
        req.setAppKey(mCommentParam.mAppKey);
        req.setCommentTopic(mCommentParam.mTopic);
        req.setCommentContent(mCommentParam.mContent);
        req.setCommentCategory(mCommentParam.mCategory.getValue());
        req.setAuthListener(mCommentParam.mAuthlistener);
        req.setToken(mCommentParam.mAccessToken);
        Bundle data = req.createRequestParamBundle();
        Intent intent = new Intent(getContext(), WeiboSdkBrowser.class);
        intent.putExtras(data);
        getContext().startActivity(intent);
    }
    
    public static class RequestParam {
        private String mAppKey;
        private String mAccessToken;
        private String mTopic;
        private String mContent;
        private Category mCategory;
        private WeiboAuthListener mAuthlistener;
        
        private RequestParam() {}
        
        /**
         * 创建请求参数（如果用户已经授权，并且有token）
         * @param appKey
         * @param token
         * @param commentTopic 评论的话题
         * @param commentContent 评论的内容
         * @param category 评论的内容的分类
         * @param listener 如果想获取授权信息，需要传出授权回调Listener
         * @return
         */
        public static RequestParam createRequestParam(String appKey, 
                String token, String commentTopic, String commentContent, Category category, WeiboAuthListener listener) {
            RequestParam param = new RequestParam();
            param.mAppKey = appKey;
            param.mAccessToken = token;
            param.mTopic = commentTopic;
            param.mContent = commentContent;
            param.mCategory = category;
            param.mAuthlistener = listener;
            return param;
        }
        
        /**
         * 创建请求参数（如果用户没有授权）
         * @param appKey
         * @param commentTopic 评论的话题
         * @param commentContent 评论的内容
         * @param category 评论的内容的分类
         * @param listener 如果想获取授权信息，需要传出授权回调Listener
         * @return
         */
        public static RequestParam createRequestParam(String appKey, String commentTopic, String commentContent, 
                Category category, WeiboAuthListener listener) {
            RequestParam param = new RequestParam();
            param.mAppKey = appKey;
            param.mTopic = commentTopic;
            param.mContent = commentContent;
            param.mCategory = category;
            param.mAuthlistener = listener;
            return param;
        }
        
    }
    
    public static enum Category {
        MOVIE("1001"), TRAVEL("1002");
        
        private String mVal;
        
        private Category(String value) {
            this.mVal = value;
        }
        
        public String getValue() {
            return mVal;
        }
        
    }
    
}
