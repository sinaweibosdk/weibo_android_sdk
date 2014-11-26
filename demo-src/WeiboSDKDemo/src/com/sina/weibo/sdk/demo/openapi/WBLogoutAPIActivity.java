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

package com.sina.weibo.sdk.demo.openapi;

import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.demo.AccessTokenKeeper;
import com.sina.weibo.sdk.demo.Constants;
import com.sina.weibo.sdk.demo.R;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;

/**
 * 该类主要演示了如何使用 {@link LogoutAPI} 来注销登陆。
 * 当注销后，您的已有 Token 将不能再次被使用。
 * 
 * @author SINA
 * @since 2013-11-05
 */
public class WBLogoutAPIActivity extends Activity {
    /** 显示 Token 信息*/
    private TextView mTokenView;
    /** 注销按钮 */
    private Button mLogoutButton;
    /** 当前 Token 信息 */
    private Oauth2AccessToken mAccessToken;
    /** 注销操作回调 */
    private LogOutRequestListener mLogoutRequestListener = new LogOutRequestListener();
    
    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_api_logout);
        mTokenView = (TextView) findViewById(R.id.current_token_info);
        mLogoutButton = (Button) findViewById(R.id.logout_button);
        
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken != null && mAccessToken.isSessionValid()) {
            String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(mAccessToken.getExpiresTime()));
            String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
            mTokenView.setText(String.format(format, mAccessToken.getToken(), date));
        }
        
        // 注销按钮
        mLogoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAccessToken != null && mAccessToken.isSessionValid()) {
                    new LogoutAPI(WBLogoutAPIActivity.this, Constants.APP_KEY, mAccessToken).logout(mLogoutRequestListener);
                } else {
                    mTokenView.setText(R.string.weibosdk_demo_logout_failed_1);
                }
            }
        });
    }
    
    /**
     * 注销按钮的监听器，接收注销处理结果。（API请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");
                    
                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(WBLogoutAPIActivity.this);

                        mTokenView.setText(R.string.weibosdk_demo_logout_success);
                        mAccessToken = null;
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
