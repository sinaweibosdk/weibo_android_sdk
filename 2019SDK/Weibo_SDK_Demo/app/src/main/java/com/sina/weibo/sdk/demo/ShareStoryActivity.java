package com.sina.weibo.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.sina.weibo.sdk.R;
import com.sina.weibo.sdk.api.StoryMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;

import java.io.File;

public class ShareStoryActivity extends Activity implements View.OnClickListener, WbShareCallback {

    private RadioButton mShareImage;

    private Button mCommitBtn;

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
        setContentView(R.layout.activity_share_story);
        mShareImage = findViewById(R.id.share_image);
        mCommitBtn = findViewById(R.id.commit);
        mCommitBtn.setOnClickListener(this);
        AuthInfo authInfo = new AuthInfo(this, APP_KY, REDIRECT_URL, SCOPE);
        mWBAPI = WBAPIFactory.createWBAPI(this);
        mWBAPI.registerApp(this, authInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWBAPI.doResultIntent(data, this);
    }

    @Override
    public void onClick(View v) {
        StoryMessage message = new StoryMessage();
        if (mShareImage.isChecked()) {
            File picFile = new File(getExternalFilesDir(null) + "/aaa.png");
            message.setImageUri(Uri.fromFile(picFile));
        } else {
            File videoFile = new File(getExternalFilesDir(null) + "/eeee.mp4");
            message.setVideoUri(Uri.fromFile(videoFile));
        }
        if (v == mCommitBtn) {
            mWBAPI.shareStory(message);
        }
    }

    @Override
    public void onComplete() {
        Toast.makeText(ShareStoryActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(UiError error) {
        Toast.makeText(ShareStoryActivity.this, "分享失败:" + error.errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(ShareStoryActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
    }
}
