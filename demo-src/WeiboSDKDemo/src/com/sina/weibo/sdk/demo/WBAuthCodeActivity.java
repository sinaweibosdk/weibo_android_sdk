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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.UIUtils;

/**
 * 该类主要演示如何使用 Code 手动进行授权。
 * 
 * <b>请注意：</b>基于以下几点原因，您可能不想使用前面的两种授权方式：
 * <li>出于安全性的考虑，您不信任目前使用应用的包名和签名来获取 Token 的方式</li>
 * <li>您不想安装微博客户端</li></br>
 * 您可以自行通过 Code 来获取 Token。通过这种方式需要使用到 APP_SECRET，请务必妥善保管好
 * 自己的 APP_SECRET，<b>泄露有风险</b>。
 * 更多细节请查看：</b><a href="http://open.weibo.com/wiki/%E6%8E%88%E6%9D%83%E6%9C%BA%E5%88%B6%E8%AF%B4%E6%98%8E#Web.E5.BA.94.E7.94.A8.E7.9A.84.E9.AA.8C.E8.AF.81.E6.8E.88.E6.9D.83">验证授权步骤</a>
 * 
 * @author SINA
 * @since 2013-10-18
 */
public class WBAuthCodeActivity extends Activity {

    private static final String TAG = "WBAuthCodeActivity";

    /**
     * WeiboSDKDemo 程序的 APP_SECRET。
     * 请注意：请务必妥善保管好自己的 APP_SECRET，不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
    private static final String WEIBO_DEMO_APP_SECRET = "4e47e691a516afad0fc490e05ff70ee5";
    
    /** 通过 code 获取 Token 的 URL */
    private static final String OAUTH2_ACCESS_TOKEN_URL = "https://open.weibo.cn/oauth2/access_token";
    
    /** 获取 Token 成功或失败的消息 */
    private static final int MSG_FETCH_TOKEN_SUCCESS = 1;
    private static final int MSG_FETCH_TOKEN_FAILED  = 2;
    
    /** UI 元素列表 */
    private TextView mNote;
    private TextView mCodeText;
    private TextView mTokenText;
    private Button mCodeButton;
    private Button mAuthCodeButton;
    
    /** 微博 Web 授权接口类，提供登陆等功能  */
    private WeiboAuth mWeiboAuth;
    /** 获取到的 Code */
    private String mCode;
    /** 获取到的 Token */
    private Oauth2AccessToken mAccessToken;

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_code);
        
        // 初始化控件
        mNote = (TextView) findViewById(R.id.note);
        mNote.setMovementMethod(LinkMovementMethod.getInstance());
        mCodeText = (TextView) findViewById(R.id.code_text);
        mTokenText = (TextView) findViewById(R.id.token_text);
        mCodeButton = (Button) findViewById(R.id.code);
        mAuthCodeButton = (Button) findViewById(R.id.auth_code);
        mAuthCodeButton.setEnabled(false);

        // 初始化微博对象
        mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);

        // 第一步：获取 Code
        mCodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeiboAuth.authorize(new AuthListener(), WeiboAuth.OBTAIN_AUTH_CODE);
            }
        });
        
        // 第二步：通过 Code 获取 Token
        mAuthCodeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchTokenAsync(mCode, WEIBO_DEMO_APP_SECRET);
            }
        });
    }

    /**
     * 微博认证授权回调类。
     */
    class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            if (null == values) {
                Toast.makeText(WBAuthCodeActivity.this, 
                        R.string.weibosdk_demo_toast_obtain_code_failed, Toast.LENGTH_SHORT).show();
                return;
            }
            
            String code = values.getString("code");
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(WBAuthCodeActivity.this, 
                        R.string.weibosdk_demo_toast_obtain_code_failed, Toast.LENGTH_SHORT).show();
                return;
            }
            
            mCode = code;
            mCodeText.setText(code);
            mAuthCodeButton.setEnabled(true);
            mTokenText.setText("");
            Toast.makeText(WBAuthCodeActivity.this, 
                    R.string.weibosdk_demo_toast_obtain_code_success, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(WBAuthCodeActivity.this, 
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            UIUtils.showToast(WBAuthCodeActivity.this, 
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG);
        }
    }

    /**
     * 该 Handler 配合 {@link RequestListener} 对应的回调来更新 UI。
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_FETCH_TOKEN_SUCCESS:
                // 显示 Token
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(mAccessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                mTokenText.setText(String.format(format, mAccessToken.getToken(), date));
                mAuthCodeButton.setEnabled(false);
                
                Toast.makeText(WBAuthCodeActivity.this, 
                        R.string.weibosdk_demo_toast_obtain_token_success, Toast.LENGTH_SHORT).show();
                break;
                
            case MSG_FETCH_TOKEN_FAILED:
                Toast.makeText(WBAuthCodeActivity.this, 
                        R.string.weibosdk_demo_toast_obtain_token_failed, Toast.LENGTH_SHORT).show();
                break;
                
            default:
                break;
            }
        };
    };
    
    /**
     * 异步获取 Token。
     * 
     * @param authCode  授权 Code，该 Code 是一次性的，只能被获取一次 Token
     * @param appSecret 应用程序的 APP_SECRET，请务必妥善保管好自己的 APP_SECRET，
     *                  不要直接暴露在程序中，此处仅作为一个DEMO来演示。
     */
    public void fetchTokenAsync(String authCode, String appSecret) {
        /*
        LinkedHashMap<String, String> requestParams = new LinkedHashMap<String, String>();
        requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_ID,     Constants.APP_KEY);
        requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_SECRET, appSecretConstantS.APP_SECRET);
        requestParams.put(WBConstants.AUTH_PARAMS_GRANT_TYPE,    "authorization_code");
        requestParams.put(WBConstants.AUTH_PARAMS_CODE,          authCode);
        requestParams.put(WBConstants.AUTH_PARAMS_REDIRECT_URL,  Constants.REDIRECT_URL);
        */
        WeiboParameters requestParams = new WeiboParameters();
        requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_ID,     Constants.APP_KEY);
        requestParams.add(WBConstants.AUTH_PARAMS_CLIENT_SECRET, appSecret);
        requestParams.add(WBConstants.AUTH_PARAMS_GRANT_TYPE,    "authorization_code");
        requestParams.add(WBConstants.AUTH_PARAMS_CODE,          authCode);
        requestParams.add(WBConstants.AUTH_PARAMS_REDIRECT_URL,  Constants.REDIRECT_URL);
    
        /**
         * 请注意：
         * {@link RequestListener} 对应的回调是运行在后台线程中的，
         * 因此，需要使用 Handler 来配合更新 UI。
         */
        AsyncWeiboRunner.request(OAUTH2_ACCESS_TOKEN_URL, requestParams, "POST", new RequestListener() {
            @Override
            public void onComplete(String response) {
                LogUtil.d(TAG, "Response: " + response);
                
                // 获取 Token 成功
                Oauth2AccessToken token = Oauth2AccessToken.parseAccessToken(response);
                if (token != null && token.isSessionValid()) {
                    LogUtil.d(TAG, "Success! " + token.toString());
                    
                    mAccessToken = token;
                    mHandler.obtainMessage(MSG_FETCH_TOKEN_SUCCESS).sendToTarget();
                } else {
                    LogUtil.d(TAG, "Failed to receive access token");
                }
            }
    
            @Override
            public void onComplete4binary(ByteArrayOutputStream responseOS) {
                LogUtil.e(TAG, "onComplete4binary...");
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }
    
            @Override
            public void onIOException(IOException e) {
                LogUtil.e(TAG, "onIOException： " + e.getMessage());
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }
    
            @Override
            public void onError(WeiboException e) {
                LogUtil.e(TAG, "WeiboException： " + e.getMessage());
                mHandler.obtainMessage(MSG_FETCH_TOKEN_FAILED).sendToTarget();
            }
        });
    }
}

