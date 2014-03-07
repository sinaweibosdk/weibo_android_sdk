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

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.demo.AccessTokenKeeper;
import com.sina.weibo.sdk.demo.R;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.InviteAPI;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 该类主要演示了如何使用 {@link InviteAPI} 来邀请好友。
 * 
 * @author SINA
 * @since 2013-11-05
 */
public class WBInviteAPIActivity extends Activity implements OnClickListener {
    private final static String TAG = WBInviteAPIActivity.class.getName();
    
    /** 用于输入被邀请人的 Uid */
    private EditText mEditText;
    /** 好友邀请按钮 */
    private Button mInviteButton;
    /** 好友邀请对应的回调 */
    private InviteRequestListener mInviteRequestListener = new InviteRequestListener();

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_api_invite);

        mEditText = (EditText) findViewById(R.id.friend_uid);
        mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mInviteButton.setEnabled(!(0 == count));
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // 邀请按钮
        mInviteButton = (Button) findViewById(R.id.invite_button);
        mInviteButton.setEnabled(false);
        mInviteButton.setOnClickListener(this);
    }

    /**
     * 点击邀请按钮时，该函数被调用 。
     */
    @Override
    public void onClick(View v) {
        
        // 创建请求的 JSON 数据对象
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(InviteAPI.KEY_TEXT,        "这个游戏太好玩了，加入一起玩吧");
            jsonObject.put(InviteAPI.KEY_URL,         "http://app.sina.com.cn/appdetail.php?appID=770915");
            jsonObject.put(InviteAPI.KEY_INVITE_LOGO, "http://hubimage.com2us.com/hubweb/contents/123_499.jpg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        // 获取被邀请人的 Uid
        String uid = mEditText.getText().toString();
        //String uid = "2785593564";
        
        // 获取 Token
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(WBInviteAPIActivity.this);
        if (accessToken != null && accessToken.isSessionValid()) {
            // 调用 OpenAPI 接口发送邀请
            new InviteAPI(accessToken).sendInvite(uid, jsonObject, mInviteRequestListener);
        } else {
            Toast.makeText(WBInviteAPIActivity.this, R.string.weibosdk_demo_access_token_is_empty, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 好友邀请按钮的监听器，接收好友邀请处理结果。（API请求结果的监听器）
     */
    private class InviteRequestListener implements RequestListener {

        @Override
        public void onComplete(String response) {
            LogUtil.d(TAG, "Invite Response: " + response);

            if (TextUtils.isEmpty(response) || response.contains("error_code")) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String errorMsg = obj.getString("error");
                    String errorCode = obj.getString("error_code");
                    String message = "error_code: " + errorCode + "error_message: " + errorMsg;
                    LogUtil.e(TAG, "Invite Failed: " + message);
                    showToast(false, message);
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showToast(true, null);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            showToast(false, e.getMessage());
        }
    }

    /**
     * 弹出 Toast。
     * 
     * @param bSuccess 操作是否成功
     * @param message  额外的信息
     */
    private void showToast(boolean bSuccess, String message) {
        int resId = bSuccess ? 
                    R.string.weibosdk_demo_openapi_invite_success :
                    R.string.weibosdk_demo_openapi_invite_failed;
        String finalMsg = getString(resId);
        if (!TextUtils.isEmpty(message)) {
            finalMsg = finalMsg + "：" + message;
        }
                    
        Toast.makeText(this, finalMsg, Toast.LENGTH_LONG).show();
    }
}
