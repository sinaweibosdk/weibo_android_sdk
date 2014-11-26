package com.sina.weibo.sdk.component.view;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.component.WeiboSdkBrowser;
import com.sina.weibo.sdk.component.WidgetRequestParam;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.ResourceManager;
import com.sina.weibo.sdk.utils.Utility;

public class AttentionComponentView extends FrameLayout {
    private static final String TAG = AttentionComponentView.class.getName();
    
    private static final String ATTENTION_H5 = "http://widget.weibo.com/relationship/followsdk.php";
    private static final String FRIENDSHIPS_SHOW_URL = "https://api.weibo.com/2/friendships/show.json";
    
    private static final String ALREADY_ATTEND_EN    = "Following";
    private static final String ALREADY_ATTEND_ZH_CN = "已关注"; 
    private static final String ALREADY_ATTEND_ZH_TW = "已關注";
    
    private static final String ATTEND_EN    = "Follow";
    private static final String ATTEND_ZH_CN = "关注"; 
    private static final String ATTEND_ZH_TW = "關注";
    
    private RequestParam mAttentionParam;
    private volatile boolean mIsLoadingState = false;
    
    private FrameLayout flButton;
    private TextView mButton;
    private ProgressBar pbLoading;
    
    public AttentionComponentView(Context context) {
        super(context);
        init(context);
    }

    public AttentionComponentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AttentionComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    private void init(Context context) {
        Drawable relationShipButtonBg = ResourceManager.createStateListDrawable(context, 
                "common_button_white.9.png", 
                "common_button_white_highlighted.9.png");
        
        flButton = new FrameLayout(context);
        flButton.setBackgroundDrawable(relationShipButtonBg);
        int paddingTop = ResourceManager.dp2px(getContext(), 6);
        int paddingRight = ResourceManager.dp2px(getContext(), 2);
        int paddingBottom = ResourceManager.dp2px(getContext(), 6);
        flButton.setPadding(0, paddingTop, paddingRight, paddingBottom);
        flButton.setLayoutParams(new FrameLayout.LayoutParams(
                ResourceManager.dp2px(getContext(), 66), 
                FrameLayout.LayoutParams.WRAP_CONTENT));
        addView(flButton);
        
        mButton = new TextView(getContext());
        mButton.setIncludeFontPadding(false);
        mButton.setSingleLine(true);
        mButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        FrameLayout.LayoutParams buttonLp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, 
                FrameLayout.LayoutParams.WRAP_CONTENT);
        buttonLp.gravity = Gravity.CENTER;
        mButton.setLayoutParams(buttonLp);
        flButton.addView(mButton);
        
        pbLoading = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);
        pbLoading.setVisibility(GONE);
        FrameLayout.LayoutParams pbLoadingLp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, 
                FrameLayout.LayoutParams.WRAP_CONTENT);
        pbLoadingLp.gravity = Gravity.CENTER;
        pbLoading.setLayoutParams(pbLoadingLp);
        flButton.addView(pbLoading);
        
        flButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                execAttented();
            }
        });
        
        showFollowButton(false);
    }
    
    public void setAttentionParam(RequestParam param) {
        this.mAttentionParam = param;
        if (param.hasAuthoriz()) {
            loadAttentionState(param);
        }
    }
    
    private void startLoading() {
        flButton.setEnabled(false);
        mButton.setVisibility(GONE);
        pbLoading.setVisibility(VISIBLE);
    }
    
    private void stopLoading() {
        flButton.setEnabled(true);
        mButton.setVisibility(VISIBLE);
        pbLoading.setVisibility(GONE);
    }
    
    private void showFollowButton(boolean attention) {
        stopLoading();
        if (attention) {
            mButton.setText(ResourceManager.getString(getContext(), 
                    ALREADY_ATTEND_EN, ALREADY_ATTEND_ZH_CN, ALREADY_ATTEND_ZH_TW));
            mButton.setTextColor(0xFF333333);
            Drawable leftDrawable = ResourceManager.getDrawable(getContext(), 
                    "timeline_relationship_icon_attention.png");
            mButton.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
            flButton.setEnabled(false);
        } else {
            mButton.setText(ResourceManager.getString(getContext(), 
                    ATTEND_EN, ATTEND_ZH_CN, ATTEND_ZH_TW));
            mButton.setTextColor(0xFFFF8200);
            Drawable leftDrawable = ResourceManager.getDrawable(getContext(), 
                    "timeline_relationship_icon_addattention.png");
            mButton.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, null, null, null);
            flButton.setEnabled(true);
        }
    }
    
    private void loadAttentionState(RequestParam req) {
        if (mIsLoadingState) {
            return;
        }
        mIsLoadingState = true;
        startLoading();
        
        WeiboParameters params = new WeiboParameters(req.mAppKey);
        params.put("access_token", req.mAccessToken);
        params.put("target_id", req.mAttentionUid);
        params.put("target_screen_name", req.mAttentionScreenName);
        requestAsync(getContext(), FRIENDSHIPS_SHOW_URL, params, "GET", new RequestListener() {
            @Override
            public void onWeiboException( WeiboException e ) {
                LogUtil.d(TAG, "error : " + e.getMessage());
                mIsLoadingState = false;
            }
            @Override
            public void onComplete( String response ) {
                LogUtil.d(TAG, "json : " + response);
                try {
                    JSONObject root = new JSONObject(response);
                    final JSONObject target = root.optJSONObject("target");
                    getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (target != null) {
                                showFollowButton(target.optBoolean("followed_by", false));
                            }
                            mIsLoadingState = false;
                        }
                    });
                } catch (JSONException e) {}
            }
        });
    }
    
    private void execAttented() {
        WidgetRequestParam req = new WidgetRequestParam(getContext());
        req.setUrl(ATTENTION_H5);
        req.setSpecifyTitle(ResourceManager.getString(getContext(), 
                ATTEND_EN, ATTEND_ZH_CN, ATTEND_ZH_TW));
        req.setAppKey(mAttentionParam.mAppKey);
        req.setAttentionFuid(mAttentionParam.mAttentionUid);
        req.setAuthListener(mAttentionParam.mAuthlistener);
        req.setToken(mAttentionParam.mAccessToken);
        req.setWidgetRequestCallback(new WidgetRequestParam.WidgetRequestCallback() {
            @Override
            public void onWebViewResult( String url ) {
                Bundle b = Utility.parseUri(url);
                String result = b.getString("result");
                if (!TextUtils.isEmpty(result)) {
                    try {
                        long attented = Integer.parseInt(result);
                        if (attented == 1) {
                            showFollowButton(true);
                        } else if (attented == 0) {
                            showFollowButton(false);
                        }
                    } catch(NumberFormatException e) {
                    }
                }
            }
        });
        Bundle data = req.createRequestParamBundle();
        Intent intent = new Intent(getContext(), WeiboSdkBrowser.class);
        intent.putExtras(data);
        getContext().startActivity(intent);
    }
    
    private void requestAsync(Context context, 
            String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        new AsyncWeiboRunner(context.getApplicationContext())
            .requestAsync(url, params, httpMethod, listener);
    }
    
    public static class RequestParam {
        private String mAppKey;
        private String mAccessToken;
        private String mAttentionUid;
        private String mAttentionScreenName;
        private WeiboAuthListener mAuthlistener;
        
        private RequestParam() {}
        
        /**
         * 创建请求参数（如果用户已经授权，并且有token）
         * @param appKey
         * @param token
         * @param attentionUid 需要 关注/取消关注 的用户UID
         * @param attentionScreenName 需要 关注/取消关注 的用户昵称(attentionUid 和 attentionScreenName 两者选其一就行)
         * @param listener 如果想获取授权信息，需要传出授权回调Listener
         * @return
         */
        public static RequestParam createRequestParam(String appKey, 
                String token, String attentionUid, String attentionScreenName,
                WeiboAuthListener listener) {
            RequestParam param = new RequestParam();
            param.mAppKey = appKey;
            param.mAccessToken = token;
            param.mAttentionUid = attentionUid;
            param.mAttentionScreenName = attentionScreenName;
            param.mAuthlistener = listener;
            return param;
        }
        
        /**
         * 创建请求参数（如果用户没有授权）
         * @param appKey
         * @param attentionUid 需要 关注/取消关注 的用户UID
         * @param attentionScreenName 需要 关注/取消关注 的用户昵称(attentionUid 和 attentionScreenName 两者选其一就行)
         * @param listener 如果想获取授权信息，需要传出授权回调Listener
         * @return
         */
        public static RequestParam createRequestParam(String appKey, String attentionUid, String attentionScreenName, 
                WeiboAuthListener listener) {
            RequestParam param = new RequestParam();
            param.mAppKey = appKey;
            param.mAttentionUid = attentionUid;
            param.mAttentionScreenName = attentionScreenName;
            param.mAuthlistener = listener;
            return param;
        }
        
        /**
         * 判断用户是否传入授权信息
         * 如果没有传入，则不会后台去获取按钮的状态
         * @return
         */
        private boolean hasAuthoriz() {
            if (!TextUtils.isEmpty(mAccessToken)) {
                return true;
            }
            return false;
        }
        
    }
    
}
