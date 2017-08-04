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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.R;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

import java.io.File;
import java.util.ArrayList;

/**
 * 该类演示了第三方应用如何通过微博客户端
 * 执行流程： 从本应用->微博->本应用
 * @author SINA
 * @since 2013-10-22
 */
public class WBShareActivity extends Activity implements OnClickListener, WbShareCallback {
    public static final String KEY_SHARE_TYPE = "key_share_type";
    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;
    
    /** 界面标题 */
    private TextView        mTitleView;
    /** 分享图片 */
    private ImageView       mImageView;
    /** 用于控制是否分享文本的 CheckBox */
    private CheckBox        mTextCheckbox;
    /** 用于控制是否分享图片的 CheckBox */
    private CheckBox        mImageCheckbox;

    private CheckBox multiImageCheckbox;
    private CheckBox videoCheckbox;
    /** 分享按钮 */
    private Button          mSharedBtn;

    private WbShareHandler shareHandler;
    private int mShareType = SHARE_CLIENT;

    int flag = 0;
    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initViews();
        mShareType = getIntent().getIntExtra(KEY_SHARE_TYPE, SHARE_CLIENT);
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
        shareHandler.setProgressColor(0xff33b5e5);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent,this);
    }

    /**
     * 用户点击分享按钮，唤起微博客户端进行分享。
     */
    @Override
    public void onClick(View v) {
        if (R.id.share_to_btn == v.getId()) {
            mSharedBtn.setText("share done");
            flag = 1;
            sendMessage(true,mImageCheckbox.isChecked());
        }
    }

    /**
     * 初始化界面。
     */
    private void initViews() {
        mTitleView = (TextView) findViewById(R.id.share_title);
        mTitleView.setText(R.string.weibosdk_demo_share_to_weibo_title);
        mImageView = (ImageView) findViewById(R.id.share_imageview);
        mTextCheckbox = (CheckBox) findViewById(R.id.share_text_checkbox);
        mImageCheckbox = (CheckBox) findViewById(R.id.shared_image_checkbox);
        mSharedBtn = (Button) findViewById(R.id.share_to_btn);
        mSharedBtn.setOnClickListener(this);
        multiImageCheckbox = (CheckBox)findViewById(R.id.share_multi_image);
        videoCheckbox = (CheckBox)findViewById(R.id.share_multi_video);

    }


    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMessage(boolean hasText, boolean hasImage) {
        sendMultiMessage(hasText, hasImage);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage) {


        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.imageObject = getImageObj();
        }
        if(multiImageCheckbox.isChecked()){
            weiboMessage.multiImageObject = getMultiImageObject();
        }
        if(videoCheckbox.isChecked()){
            weiboMessage.videoSourceObject = getVideoObject();
        }
        shareHandler.shareMessage(weiboMessage, false);

    }



    @Override
    public void onWbShareSuccess() {
        Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWbShareFail() {
        Toast.makeText(this,
                getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: ",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWbShareCancel() {
        Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
    }

    /**
     * 获取分享的文本模板。
     */
    private String getSharedText() {
        int formatId = R.string.weibosdk_demo_share_text_template;
        String format = getString(formatId);
        String text = format;
        if (mTextCheckbox.isChecked() || mImageCheckbox.isChecked()) {
            text = "@大屁老师，这是一个很漂亮的小狗，朕甚是喜欢-_-!! #大屁老师#http://weibo.com/p/1005052052202067/home?from=page_100505&mod=TAB&is_all=1#place";
        }
        return text;
    }

    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = getSharedText();
        textObject.title = "xxxx";
        textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title ="测试title";
        mediaObject.description = "测试描述";
        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = "http://news.sina.com.cn/c/2013-10-22/021928494669.shtml";
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }

    /***
     * 创建多图
     * @return
     */
    private MultiImageObject getMultiImageObject(){
        MultiImageObject multiImageObject = new MultiImageObject();
        //pathList设置的是本地本件的路径,并且是当前应用可以访问的路径，现在不支持网络路径（多图分享依靠微博最新版本的支持，所以当分享到低版本的微博应用时，多图分享失效
        // 可以通过WbSdk.hasSupportMultiImage 方法判断是否支持多图分享,h5分享微博暂时不支持多图）多图分享接入程序必须有文件读写权限，否则会造成分享失败
        ArrayList<Uri> pathList = new ArrayList<Uri>();
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/aaa.png")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/bbbb.jpg")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/ccc.JPG")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/ddd.jpg")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/fff.jpg")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/ggg.JPG")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/eee.jpg")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/hhhh.jpg")));
        pathList.add(Uri.fromFile(new File(getExternalFilesDir(null)+"/kkk.JPG")));
        multiImageObject.setImageList(pathList);
        return multiImageObject;
    }

    private VideoSourceObject getVideoObject(){
        //获取视频
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        videoSourceObject.videoPath = Uri.fromFile(new File(getExternalFilesDir(null)+"/eeee.mp4"));
        return videoSourceObject;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
