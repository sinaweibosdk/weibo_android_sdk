/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sina.weibo.sdk.widget;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.sina.weibo.sdk.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 该类提供了一个简单的登录/注销控件。
 * 该控件提供内置的登录（SSO 登陆授权）和注销功能，它有两种样式（蓝色和银色）。
 * 注意：使用者可以自行修改 /res/values/styles.xml 文件中的样式。
 * 
 * @author SINA
 * @since 2013-11-04
 */
public class LoginoutButton extends Button implements OnClickListener {
    private static final String TAG = "LoginButton";
    
    /** 微博授权时，启动 SSO 界面的 Activity */
	private Context mContext;
    /** 授权认证所需要的信息 */
    private AuthInfo mAuthInfo;
    /** SSO 授权认证实例 */
    private SsoHandler mSsoHandler;
    /** 微博授权认证回调 */
    private WeiboAuthListener mAuthListener;
    /** Access Token 实例  */
    private Oauth2AccessToken mAccessToken;
    /** 注销回调 */
    private RequestListener mLogoutListener;
    /** 点击 Button 时，额外的 Listener */
    private OnClickListener mExternalOnClickListener;

    /**
     * 创建一个登录/注销按钮。
     * 
     * @see View#View(Context)
     */
	public LoginoutButton(Context context) {
		this(context, null);
	}
	
    /**
     * 从 XML 配置文件中创建一个登录/注销按钮。
     * 
     * @see View#View(Context, AttributeSet)
     */	
	public LoginoutButton(Context context, AttributeSet attrs) {
		this(context, attrs, /*R.style.com_sina_weibo_sdk_loginview_default_style*/0);
	}

    /**
     * 从 XML 配置文件以及样式中创建一个登录/注销按钮。
     * 
     * @see View#View(Context, AttributeSet, int)
     */	
	public LoginoutButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context, attrs);
	}
    
    /**
     * 设置微博授权所需信息以及回调函数。
     * 
     * @param authInfo     用于保存授权认证所需要的信息
     * @param authListener 微博授权认证回调接口
     */
    public void setWeiboAuthInfo(AuthInfo authInfo, WeiboAuthListener authListener) {
		mAuthInfo = authInfo;
		mAuthListener = authListener;
	}
    
    /**
     * 设置微博授权所需信息。
     * 
     * @param appKey       第三方应用的 APP_KEY
     * @param redirectUrl  第三方应用的回调页
     * @param scope        第三方应用申请的权限
     * @param authListener 微博授权认证回调接口
     */
    public void setWeiboAuthInfo(String appKey, String redirectUrl, String scope, WeiboAuthListener authListener) {
		mAuthInfo = new AuthInfo(mContext, appKey, redirectUrl, scope);
		mAuthListener = authListener;
	}
    
    /**
     * 设置注销时，需要设置的 Token 信息以及注销后的回调接口。
     * 
     * @param accessToken    AccessToken 信息
     * @param logoutListener 注销回调
     */
    public void setLogoutInfo(Oauth2AccessToken accessToken, RequestListener logoutListener) {
		mAccessToken = accessToken;
		mLogoutListener = logoutListener;

		if (mAccessToken != null && mAccessToken.isSessionValid()) {
            setText(R.string.com_sina_weibo_sdk_logout);
        }
	}
    
    /**
     * 设置注销回调。
     * 
     * @param logoutListener 注销回调
     */
    public void setLogoutListener(RequestListener logoutListener) {
    	mLogoutListener = logoutListener;
	}
    
    /**
     * 设置一个额外的 Button 点击时的 Listener。
     * 当触发 Button 点击事件时，会先调用该 Listener，给使用者一个可访问的机会，
     * 然后再调用内部默认的处理。
     * <p><b>注意：一般情况下，使用者不需要调用该方法，除非有其它必要性。<b></p>
     * 
     * @param listener Button 点击时的 Listener
     */    
    public void setExternalOnClickListener(OnClickListener l) {
        mExternalOnClickListener = l;
    }

	/**
	 * 使用该控件进行授权登陆时，需要手动调用该函数。
	 * <p>
	 * 重要：使用该控件的 Activity 必须重写 {@link Activity#onActivityResult(int, int, Intent)}，
	 *       并在内部调用该函数，否则无法授权成功。</p>
	 * <p>Sample Code：</p>
	 * <pre class="prettyprint">
	 * protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	 *     super.onActivityResult(requestCode, resultCode, data);
	 *     
	 *     // 在此处调用
	 *     mLoginoutButton.onActivityResult(requestCode, resultCode, data);
	 * }
	 * </pre>
	 * @param requestCode 请查看 {@link Activity#onActivityResult(int, int, Intent)}
	 * @param resultCode  请查看 {@link Activity#onActivityResult(int, int, Intent)}
	 * @param data        请查看 {@link Activity#onActivityResult(int, int, Intent)}
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (mSsoHandler != null) {
	        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }
	}

	/**
     * 按钮被点击时，调用该函数。
     */
	@Override
	public void onClick(View v) {
	    // Give a chance to external listener
	    if (mExternalOnClickListener != null) {
            mExternalOnClickListener.onClick(v);
        }
	    
		if (mAccessToken != null && mAccessToken.isSessionValid()) {
			logout();
		} else {
			login();
		}
	}

	/**
	 * 初始化函数。
	 * 
	 * @param context 上下文环境，一般为放置该 Button 的 Activity 
	 * @param attrs   XML 属性集合对象
	 */
	private void initialize(Context context, AttributeSet attrs) {
		mContext = context;
		this.setOnClickListener(this);
		
		// 如果布局文件中未设置 style，加载默认的 style
		loadDefaultStyle(attrs);
	}

	/**
	 * 加载默认的样式（蓝色）。
	 * 
	 * @param attrs XML 属性集合对象
	 */
	private void loadDefaultStyle(AttributeSet attrs) {
		if (attrs != null && 0 == attrs.getStyleAttribute()) {
			Resources res = getResources();
			this.setBackgroundResource(R.drawable.com_sina_weibo_sdk_button_blue);
			this.setPadding(res.getDimensionPixelSize(R.dimen.com_sina_weibo_sdk_loginview_padding_left),
					res.getDimensionPixelSize(R.dimen.com_sina_weibo_sdk_loginview_padding_top),
					res.getDimensionPixelSize(R.dimen.com_sina_weibo_sdk_loginview_padding_right),
					res.getDimensionPixelSize(R.dimen.com_sina_weibo_sdk_loginview_padding_bottom));
			this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_com_sina_weibo_sdk_logo, 0, 0, 0);
			this.setCompoundDrawablePadding(
					res.getDimensionPixelSize(R.dimen.com_sina_weibo_sdk_loginview_compound_drawable_padding));
	        this.setTextColor(res.getColor(R.color.com_sina_weibo_sdk_loginview_text_color));
	        this.setTextSize(TypedValue.COMPLEX_UNIT_PX,
	        		res.getDimension(R.dimen.com_sina_weibo_sdk_loginview_text_size));
	        this.setTypeface(Typeface.DEFAULT_BOLD);
	        this.setGravity(Gravity.CENTER);
	        this.setText(R.string.com_sina_weibo_sdk_login_with_weibo_account);
		}
	}
	
	/**
	 * 进行 SSO 登陆。如果未安装微博客户端，或 SSO 验证失败，则会跳转到正常的 Web 授权。
	 */
	private void login() {
		LogUtil.i(TAG, "Click to login");
		
		if (null == mSsoHandler && mAuthInfo != null) {
			mSsoHandler = new SsoHandler((Activity)mContext, mAuthInfo);
		}
		
		if (mSsoHandler != null) {
			mSsoHandler.authorize(new WeiboAuthListener() {
				
				@Override
				public void onComplete(Bundle values) {
					// 从 Bundle 中解析 Token
					mAccessToken = Oauth2AccessToken.parseAccessToken(values);
					if (mAccessToken.isSessionValid()) {
						setText(R.string.com_sina_weibo_sdk_logout);
					}
					
					if (mAuthListener != null) {
						mAuthListener.onComplete(values);
					}
				}
				
				@Override
				public void onCancel() {
					if (mAuthListener != null) {
						mAuthListener.onCancel();
					}
				}
				
				@Override
				public void onWeiboException(WeiboException e) {
					if (mAuthListener != null) {
						mAuthListener.onWeiboException(e);
					}
				}
			});
		} else {
			LogUtil.e(TAG, "Please setWeiboAuthInfo(...) for first");
		}
	}

	/**
	 * 调用 {@link LogoutAPI#logout(RequestListener)} 来注销。
	 */
	private void logout() {
		if (mAccessToken != null && mAccessToken.isSessionValid()) {
			LogUtil.i(TAG, "Click to logout");
			
			new LogoutAPI(mContext, mAuthInfo.getAppKey(), mAccessToken).logout(new RequestListener() {
	            @Override
	            public void onComplete(String response) {
		            if (!TextUtils.isEmpty(response)) {
		                try {
		                    JSONObject obj = new JSONObject(response);
		                    if(obj.isNull("error")){
			                    String value = obj.getString("result");
	
			                    // 注销成功
			                    if ("true".equalsIgnoreCase(value)) {
			                    	// XXX: 考虑是否需要将 AccessTokenKeeper 放到 SDK 中？？
			                        //AccessTokenKeeper.clear(getContext());
			                    	// 清空当前 Token
			                        mAccessToken = null;
			                        
			                        setText(R.string.com_sina_weibo_sdk_login_with_weibo_account);
			                    }
		                    } else {
		                    	String error_code = obj.getString("error_code");
		                    	if(error_code.equals("21317")){
		                    		 mAccessToken = null;
				                     setText(R.string.com_sina_weibo_sdk_login_with_weibo_account);
		                    	}
		                    }
		                } catch (JSONException e) {
		                    e.printStackTrace();
		                }
		            }
		            
		            if (mLogoutListener != null) {
		            	mLogoutListener.onComplete(response);
					}
		        }

				@Override
				public void onWeiboException(WeiboException e) {
					LogUtil.e(TAG, "WeiboException： " + e.getMessage());
	                // 注销失败
	                setText(R.string.com_sina_weibo_sdk_logout);
	                if (mLogoutListener != null) {
	                	mLogoutListener.onWeiboException(e);
	                }
				}
	        });
		}
	}
}
