package com.sina.weibo.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.sina.weibo.sdk.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ShareActivity extends Activity implements View.OnClickListener, WbShareCallback {

    private CheckBox mShareText;

    private CheckBox mShareImage;

    private CheckBox mShareUrl;

    private CheckBox mShareMultiImage;

    private CheckBox mShareVideo;

    private RadioButton mShareClientOnly;

    private RadioButton mShareClientH5;

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
        mShareUrl = findViewById(R.id.share_url_cb);
        mShareMultiImage = findViewById(R.id.share_multi_image_cb);
        mShareVideo = findViewById(R.id.share_video_cb);
        mShareClientOnly = findViewById(R.id.share_client_only);
        mShareClientH5 = findViewById(R.id.share_client_h5);
        mCommitBtn = findViewById(R.id.commit);
        mCommitBtn.setOnClickListener(this);
        AuthInfo authInfo = new AuthInfo(this, APP_KY, REDIRECT_URL, SCOPE);
        mWBAPI = WBAPIFactory.createWBAPI(this);
        mWBAPI.registerApp(this, authInfo);
        mWBAPI.setLoggerEnable(true);
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
            textObject.text = text;
            message.textObject = textObject;
        }

        // 分享图片
        if (mShareImage.isChecked()) {
            ImageObject imageObject = new ImageObject();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.share_image);
            imageObject.setImageData(bitmap);
            message.imageObject = imageObject;
        }

        // 分享网页
        if (mShareUrl.isChecked()) {
            WebpageObject webObject = new WebpageObject();
            webObject.identify = UUID.randomUUID().toString();
            webObject.title = "标题";
            webObject.description = "描述";
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
            ByteArrayOutputStream os = null;
            try {
                os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
                webObject.thumbData = os.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            webObject.actionUrl = "https://weibo.com";
            webObject.defaultText = "分享网页";
            message.mediaObject = webObject;
        }

        if (mShareMultiImage.isChecked()) {
            // 分享多图
            MultiImageObject multiImageObject = new MultiImageObject();
            ArrayList<Uri> list = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authority = this.getPackageName() + ".fileprovider";
                list.add(FileProvider.getUriForFile(this, authority, new File(getExternalFilesDir(null) + "/aaa.png")));
                list.add(FileProvider.getUriForFile(this, authority, new File(getExternalFilesDir(null) + "/ccc.JPG")));
                list.add(FileProvider.getUriForFile(this, authority, new File(getExternalFilesDir(null) + "/ddd.jpg")));
                list.add(FileProvider.getUriForFile(this, authority, new File(getExternalFilesDir(null) + "/fff.jpg")));
                list.add(FileProvider.getUriForFile(this, authority, new File(getExternalFilesDir(null) + "/ggg.JPG")));
                list.add(FileProvider.getUriForFile(this, authority, new File(getExternalFilesDir(null) + "/eee.jpg")));
                list.add(FileProvider.getUriForFile(this, authority, new File(getExternalFilesDir(null) + "/hhhh.jpg")));
                list.add(FileProvider.getUriForFile(this, authority, new File(getExternalFilesDir(null) + "/kkk.JPG")));
            } else {
                list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/aaa.png")));
                list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/ccc.JPG")));
                list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/ddd.jpg")));
                list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/fff.jpg")));
                list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/ggg.JPG")));
                list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/eee.jpg")));
                list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/hhhh.jpg")));
                list.add(Uri.fromFile(new File(getExternalFilesDir(null) + "/kkk.JPG")));
            }
            multiImageObject.imageList = list;
            message.multiImageObject = multiImageObject;
        }

        if (mShareVideo.isChecked()) {
            // 分享视频
            VideoSourceObject videoObject = new VideoSourceObject();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String filePath = getExternalFilesDir(null) + "/eeee.mp4";
                File videoFile = new File(filePath);
                if (!videoFile.getParentFile().exists()) {
                    videoFile.getParentFile().mkdir();
                }
                videoObject.videoPath = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", videoFile);
            } else {
                videoObject.videoPath = Uri.fromFile(new File(getExternalFilesDir(null) + "/eeee.mp4"));
            }

            message.videoSourceObject = videoObject;
        }

        mWBAPI.shareMessage(message, mShareClientOnly.isChecked());
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
