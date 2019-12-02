package com.sina.weibo.sdk.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.sina.weibo.sdk.R;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;

import java.io.File;
import java.util.ArrayList;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener, WbShareCallback {

    private CheckBox mShareText;

    private CheckBox mShareImage;

    private CheckBox mShareMultiImage;

    private CheckBox mShareVideo;

    private RadioButton mShareClient;

    private RadioButton mShareWeb;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mShareText = findViewById(R.id.share_text_cb);
        mShareImage = findViewById(R.id.share_image_cb);
        mShareMultiImage = findViewById(R.id.share_multi_image_cb);
        mShareVideo = findViewById(R.id.share_video_cb);
        mShareClient = findViewById(R.id.share_client);
        mShareWeb = findViewById(R.id.share_h5);
        mCommitBtn = findViewById(R.id.commit);
        mCommitBtn.setOnClickListener(this);
        AuthInfo authInfo = new AuthInfo(this, APP_KY, REDIRECT_URL, SCOPE);
        mWBAPI = WBAPIFactory.createWBAPI(this);
        mWBAPI.registerApp(this, authInfo);
    }

    @Override
    public void onClick(View v) {
        if (v == mCommitBtn) {
            doWeiboShare();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWBAPI.doResultIntent(data, this);
    }

    private void doWeiboShare() {
        WeiboMultiMessage message = new WeiboMultiMessage();

        TextObject textObject = new TextObject();
        String text = "我正在使用微博客户端发博器分享文字。";

        // 分享文字
        if (mShareText.isChecked()) {
            text = "这里设置您要分享的内容！";
        }
        textObject.text = text;
        message.textObject = textObject;

        // 分享图片
        if (mShareImage.isChecked()) {
            ImageObject imageObject = new ImageObject();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.share_image);
            imageObject.setImageData(bitmap);
            message.imageObject = imageObject;
        }

        if (mShareMultiImage.isChecked()) {
            // 分享多图
            MultiImageObject multiImageObject = new MultiImageObject();
            ArrayList<Uri> list = new ArrayList<>();
            list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/aaa.png")));
            list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/ccc.JPG")));
            list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/ddd.jpg")));
            list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/fff.jpg")));
            list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/ggg.JPG")));
            list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/eee.jpg")));
            list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/hhhh.jpg")));
            list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/kkk.JPG")));
            multiImageObject.imageList = list;
            message.multiImageObject = multiImageObject;
        }

        if (mShareVideo.isChecked()) {
            // 分享视频
            VideoSourceObject videoObject = new VideoSourceObject();
            videoObject.videoPath = Uri.fromFile(new File(getExternalFilesDir(null) + "/eeee.mp4"));
            message.videoSourceObject = videoObject;
        }

        mWBAPI.shareMessage(message, mShareClient.isChecked());
    }

    @Override
    public void onComplete() {
        Toast.makeText(ShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(UiError error) {
        Toast.makeText(ShareActivity.this, "分享失败:" + error.errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(ShareActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
    }
}
