package com.weibo.sdk.android.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.sdk.api.message.InviteApi;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibo.sdk.android.util.AccessTokenKeeper;
import com.weibo.sdk.view.LoginButton;
import com.weibo.sdk.view.LogoutButton;

public class DemoLoginButton extends Activity {
    private final int ERROR_RESPONSE = 0;
    private final int SUCC_RESPONSE = 1;
    private final int OBTAIN_TOKEN = 2;
    private final int REVOKE_AUTH = 3;
    
    /** UI元素列表 */
    private LoginButton mLoginBt;
    private Button mInviteButton;
    private EditText mEditText;
    
    /** 微博OpenAPI访问入口 */
    IWeiboAPI mWeiboAPI = null;
    
    /** 登陆认证对应的listener */
    private AuthListener mLoginListener = new AuthListener();
    /** 登出操作对应的listener */
    private LogOutRequestListener mLogoutRequestListener = new LogOutRequestListener();
    /** 好友邀请对应的listener */
    private InviteRequestListener mInviteRequestListener = new InviteRequestListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_login_button);
        mEditText = (EditText) findViewById(R.id.uid);

        // 登陆按钮
        mLoginBt = (LoginButton) findViewById(R.id.login_bt);
        mLoginBt.setCurrentActivity(this);
        mLoginBt.setAuthListener(mLoginListener);

        // 登出按钮
        LogoutButton logoutBt = (LogoutButton) findViewById(R.id.logout_bt);
        logoutBt.setRequestListener(mLogoutRequestListener);

        // 邀请按钮
        mInviteButton = (Button) findViewById(R.id.invite);
        mInviteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(InviteApi.KEY_TEXT, "songxiaoxue 邀请接口");
                bundle.putString(InviteApi.KEY_URL, "http://weibo.com/u/1846671692?wvr=5&");
                String uid = mEditText.getText().toString();
                if (!TextUtils.isEmpty(uid)) {
                    Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(DemoLoginButton.this);
                    if (!TextUtils.isEmpty(accessToken.getToken())) {
                        new InviteApi(accessToken).sendInvite(getApplicationContext(), bundle, uid, mInviteRequestListener);
                    } else {
                        Toast.makeText(getApplicationContext(), "AccessToken不存在！", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请输入被邀请人的Uid!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SsoHandler mSsoHandler = mLoginBt.getSsoHandler();
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // Log.d(TAG,"msg.what:"+msg.what);
            switch (msg.what) {
            case ERROR_RESPONSE:
                if (!TextUtils.isEmpty((String) msg.obj)) {
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_LONG).show();
                }
                break;
                
            case SUCC_RESPONSE:
                Toast.makeText(getApplicationContext(), getString(R.string.invite_succ), Toast.LENGTH_LONG).show();
                break;

            case OBTAIN_TOKEN:
                TextView tv = (TextView) findViewById(R.id.result);
                Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(getApplicationContext());
                if (token != null && !TextUtils.isEmpty(token.getToken())) {
                    tv.setText(token.getToken());
                }
                break;
                
            case REVOKE_AUTH:
                TextView tv1 = (TextView) findViewById(R.id.result);
                tv1.setText("已登出！");
                break;
            }
        }
    };

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Message msg = Message.obtain();
            msg.what = OBTAIN_TOKEN;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            if (e != null) {
                Toast.makeText(DemoLoginButton.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(WeiboDialogError e) {
        }

        @Override
        public void onCancel() {
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onIOException(IOException e) {
        }

        @Override
        public void onError(WeiboException e) {
        }

        @Override
        public void onComplete4binary(ByteArrayOutputStream responseOS) {
        }

        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equals(value)) {
                        AccessTokenKeeper.clear(DemoLoginButton.this);
                        Message msg = Message.obtain();
                        msg.what = REVOKE_AUTH;

                        mHandler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 好友邀请按钮的监听器，接收好友邀请处理结果。（API请求结果的监听器）
     */
    private class InviteRequestListener implements RequestListener {

        @Override
        public void onComplete(String response) {
            // Log.d(TAG,"Invite Response : response=="+response);
            try {
                Message msg = Message.obtain();
                if (TextUtils.isEmpty(response) || response.contains("error_code")) {

                    msg.what = ERROR_RESPONSE;
                    JSONObject obj = new JSONObject(response);
                    msg.obj = obj.getString("error");
                } else {

                    msg.what = SUCC_RESPONSE;
                }

                mHandler.sendMessage(msg);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onComplete4binary(ByteArrayOutputStream responseOS) {
        }

        @Override
        public void onIOException(IOException e) {
        }

        @Override
        public void onError(WeiboException e) {
            Message msg = Message.obtain();
            msg.what = ERROR_RESPONSE;
            msg.obj = e.getMessage();

            mHandler.sendMessage(msg);
        }
    }
}
