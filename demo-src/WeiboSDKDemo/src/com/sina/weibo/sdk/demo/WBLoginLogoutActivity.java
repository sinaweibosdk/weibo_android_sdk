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

package com.sina.weibo.sdk.demo;

import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.sina.weibo.sdk.widget.LoginButton;
import com.sina.weibo.sdk.widget.LoginoutButton;

/**
 * 该类主要演示了如何使用登录/注销控件。
 * 目前，我们提供了两类登录控件：
 * <li>单独的登陆控件，只提供登录功能，它有三种样式
 * <li>登录/注销组合控件，提供内置的登录和注销功能，它有两种样式
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBLoginLogoutActivity extends Activity {
    
    /** UI元素列表 */
    private TextView mTokenView;
    private LoginButton mLoginBtnDefault;
    private LoginButton mLoginBtnStyle2;
    private LoginButton mLoginBtnStyle3;
    private LoginoutButton mLoginoutBtnDefault;
    private LoginoutButton mLoginoutBtnSilver;
    
    /** 登陆认证对应的listener */
    private AuthListener mLoginListener = new AuthListener();
    /** 登出操作对应的listener */
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();

    /**
     * 该按钮用于记录当前点击的是哪一个 Button，用于在 {@link #onActivityResult}
     * 函数中进行区分。通常情况下，我们的应用中只需要一个合适的 {@link LoginButton} 
     * 或者 {@link LoginoutButton} 即可。
     */
    private Button mCurrentClickedButton;
    
    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_logout);
        mTokenView = (TextView) findViewById(R.id.result);

        // 创建授权认证信息
        AuthInfo authInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);

        /**
         * 登陆按钮
         */
        // 登陆按钮（默认样式）
        mLoginBtnDefault = (LoginButton) findViewById(R.id.login_button_default);
        mLoginBtnDefault.setWeiboAuthInfo(authInfo, mLoginListener);
        //mLoginBtnStyle2.setStyle(LoginButton.LOGIN_INCON_STYLE_1);

        // 登陆按钮（样式二）
        mLoginBtnStyle2 = (LoginButton) findViewById(R.id.login_button_style1);
        mLoginBtnStyle2.setWeiboAuthInfo(authInfo, mLoginListener);
        mLoginBtnStyle2.setStyle(LoginButton.LOGIN_INCON_STYLE_2);
        
        // 登陆按钮（样式三）:
        // 请注意：该样式没有按下的效果
        mLoginBtnStyle3 = (LoginButton) findViewById(R.id.login_button_style2);
        mLoginBtnStyle3.setWeiboAuthInfo(authInfo, mLoginListener);
        mLoginBtnStyle3.setStyle(LoginButton.LOGIN_INCON_STYLE_3);
        
        /**
         * 登录/注销按钮
         */
        // 登录/注销按钮（默认样式：蓝色）
        mLoginoutBtnDefault = (LoginoutButton) findViewById(R.id.login_out_button_default);
        mLoginoutBtnDefault.setWeiboAuthInfo(authInfo, mLoginListener);
        mLoginoutBtnDefault.setLogoutListener(mLogoutListener);
        
        // 登陆按钮（样式二：银灰色）
        mLoginoutBtnSilver = (LoginoutButton) findViewById(R.id.login_out_button_silver);
        mLoginoutBtnSilver.setWeiboAuthInfo(authInfo, mLoginListener);
        mLoginoutBtnSilver.setLogoutListener(mLogoutListener);
        // 由于 LoginLogouButton 并不保存 Token 信息，因此，如果您想在初次
        // 进入该界面时就想让该按钮显示"注销"，请放开以下代码
        //Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(this);
        //mLoginoutBtnSilver.setLogoutInfo(token, mLogoutListener);
        
        /**
         * 注销按钮：该按钮未做任何封装，直接调用对应 API 接口
         */
        final Button logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogoutAPI(AccessTokenKeeper.readAccessToken(WBLoginLogoutActivity.this)).logout(mLogoutListener);
            }
        });
        
        /**
         * 请注意：为每个 Button 设置一个额外的 Listener 只是为了记录当前点击的
         * 是哪一个 Button，用于在 {@link #onActivityResult} 函数中进行区分。
         * 通常情况下，我们的应用不需要调用该函数。
         */
        mLoginBtnDefault.setExternalOnClickListener(mButtonClickListener);
        mLoginBtnStyle2.setExternalOnClickListener(mButtonClickListener);
        mLoginBtnStyle3.setExternalOnClickListener(mButtonClickListener);
        mLoginoutBtnDefault.setExternalOnClickListener(mButtonClickListener);
        mLoginoutBtnSilver.setExternalOnClickListener(mButtonClickListener);
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     * 
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (mCurrentClickedButton != null) {
            if (mCurrentClickedButton instanceof LoginButton) {
                ((LoginButton)mCurrentClickedButton).onActivityResult(requestCode, resultCode, data);
            } else if (mCurrentClickedButton instanceof LoginoutButton) {
                ((LoginoutButton)mCurrentClickedButton).onActivityResult(requestCode, resultCode, data);
            }
        }
        
        /*
        if (mLoginBtnDefault != null) {
			mLoginBtnDefault.onActivityResult(requestCode, resultCode, data);
		}
        */
        
        /*
        if (mLoginBtnStyle2 != null) {
            mLoginBtnStyle2.onActivityResult(requestCode, resultCode, data);
        }
        */
        
        /*
        if (mLoginBtnStyle3 != null) {
            mLoginBtnStyle3.onActivityResult(requestCode, resultCode, data);
        }
        */
        
        /*
        if (mLoginoutBtnDefault != null) {
        	mLoginoutBtnDefault.onActivityResult(requestCode, resultCode, data);
		}
		*/
        
        /*
        if (mLoginoutBtnSilver != null) {
            mLoginoutBtnSilver.onActivityResult(requestCode, resultCode, data);
        }
        */
    }
    
    /**
     * 请注意：为每个 Button 设置一个额外的 Listener 只是为了记录当前点击的
     * 是哪一个 Button，用于在 {@link #onActivityResult} 函数中进行区分。
     * 通常情况下，我们的应用不需要定义该 Listener。
     */
    private OnClickListener mButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                mCurrentClickedButton = (Button)v;
            }
        }
    };

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                mTokenView.setText(String.format(format, accessToken.getToken(), date));

                AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(WBLoginLogoutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(WBLoginLogoutActivity.this, 
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(WBLoginLogoutActivity.this);
                        mTokenView.setText(R.string.weibosdk_demo_logout_success);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }     

		@Override
		public void onWeiboException(WeiboException e) {
			mTokenView.setText(R.string.weibosdk_demo_logout_failed);
		}
    }
}
