package com.sina.weibo.sdk.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sina.weibo.sdk.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mInit;

    private Button mSso;

    private Button mClientSso;

    private Button mWebSso;

    private Button mClientShare;

    private Button mStoryShare;

    //在微博开发平台为应用申请的App Key
    private static final String APP_KY = "2045436852";
    //在微博开放平台设置的授权回调页
    private static final String REDIRECT_URL = "http://www.sina.com";
    //在微博开放平台为应用申请的高级权限
    private static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";


    private IWBAPI mWBAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInit = findViewById(R.id.init_sdk);
        mSso = findViewById(R.id.sso);
        mClientSso = findViewById(R.id.sso_client);
        mWebSso = findViewById(R.id.sso_web);
        mInit.setOnClickListener(this);
        mSso.setOnClickListener(this);
        mClientSso.setOnClickListener(this);
        mWebSso.setOnClickListener(this);
        mClientShare = findViewById(R.id.share_client);
        mClientShare.setOnClickListener(this);
        mStoryShare = findViewById(R.id.share_story);
        mStoryShare.setOnClickListener(this);
        copy();
    }

    //init sdk
    private void initSdk() {
        AuthInfo authInfo = new AuthInfo(this, APP_KY, REDIRECT_URL, SCOPE);
        mWBAPI = WBAPIFactory.createWBAPI(this);
        mWBAPI.registerApp(this, authInfo);
    }

    private void startAuth() {
        //auth
        mWBAPI.authorize(new WbAuthListener() {
            @Override
            public void onComplete(Oauth2AccessToken token) {
                Toast.makeText(MainActivity.this, "微博授权成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(UiError error) {
                Toast.makeText(MainActivity.this, "微博授权出错", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "微博授权取消", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWBAPI.authorizeCallback(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v == mInit) {
            initSdk();
        }
        if (v == mSso) {
            startAuth();
        }
        if (v == mClientSso) {
            startClientAuth();
        }
        if (v == mWebSso) {
            startWebAuth();
        }
        if (v == mClientShare) {
            Intent intent = new Intent(MainActivity.this, ShareActivity.class);
            startActivity(intent);
        }
        if (v == mStoryShare) {
            Intent intent = new Intent(MainActivity.this, ShareStoryActivity.class);
            startActivity(intent);
        }
    }

    private void startClientAuth() {
        mWBAPI.authorizeClient(new WbAuthListener() {
            @Override
            public void onComplete(Oauth2AccessToken token) {
                Toast.makeText(MainActivity.this, "微博授权成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(UiError error) {
                Toast.makeText(MainActivity.this, "微博授权出错", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "微博授权取消", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startWebAuth() {
        mWBAPI.authorizeWeb(new WbAuthListener() {
            @Override
            public void onComplete(Oauth2AccessToken token) {
                Toast.makeText(MainActivity.this, "微博授权成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(UiError error) {
                Toast.makeText(MainActivity.this, "微博授权出错:" + error.errorDetail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "微博授权取消", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void copy() {
        copyFile("eeee.mp4");
        copyFile("aaa.png");
        copyFile("bbb.jpg");
        copyFile("ccc.JPG");
        copyFile("eee.jpg");
        copyFile("ddd.jpg");
        copyFile("fff.jpg");
        copyFile("ggg.JPG");
        copyFile("hhhh.jpg");
        copyFile("kkk.JPG");
    }

    private void copyFile(final String fileName) {
        final File file = new File(getExternalFilesDir(null).getPath() + "/" + fileName);
        if (!file.exists()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = getAssets().open(fileName);
                        OutputStream outputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[1444];
                        int readSize;
                        while ((readSize = inputStream.read(buffer)) != 0) {
                            outputStream.write(buffer, 0, readSize);
                        }
                        inputStream.close();
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }
}
