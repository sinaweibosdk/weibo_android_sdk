package com.weibo.sdk.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.BaseResponse;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboHandler;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.utils.Util;

/**
 * 该Activity演示了第三方应用如何发送请求消息给微博客户端。发送的内容包括文字、图片、视频、音乐等。
 * 执行流程： 从本应用->微博->本应用
 * 
 * @author taibin@staff.sina.com.cn
 */
public class RequestMessageActivity extends Activity implements OnClickListener, IWeiboHandler.Response {
    
    /** 微博OpenAPI访问入口 */
    IWeiboAPI mWeiboAPI = null;

    // UI元素列表
    /** 分享文本 */
    private TextView    mTitle;
    
    /** 分享图片 */
    private ImageView   mImage;

    /** 分享网页 */
    private TextView    mWebpageTitle;
    private ImageView   mWebpageImage;
    private TextView    mWebpageContent;
    @SuppressWarnings("unused")
    private TextView    mWebpageUrl;
    
    /** 分享音乐 */
    private TextView    mMusicTitle;
    private ImageView   mMusicImage;
    private TextView    mMusicContent;
    @SuppressWarnings("unused")
    private TextView    mMusicUrl;

    /** 分享视频 */
    private TextView    mVideoTitle;
    private ImageView   mVideoImage;
    private TextView    mVideoContent;
    @SuppressWarnings("unused")
    private TextView    mVideoUrl;

    /** 分享声音 */
    private TextView    mVoiceTitle;
    private ImageView   mVoiceImage;
    private TextView    mVoiceContent;
    @SuppressWarnings("unused")
    private TextView    mVoiceUrl;

    /**
     * CheckBox 和 RadioButton用于控制分享的内容，
     * 用户可以同时分享文本、图片和其它媒体资源（网页、音乐、视频、声音中的一种）
     */
    private CheckBox    mTextCb;
    private CheckBox    mImageCb;
    private RadioButton mWebpageRadio;
    private RadioButton mMusicRadio;
    private RadioButton mVideoRadio;
    private RadioButton mVoiceRadio;

    /** 分享按钮 */
    private Button mSharedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reqmessage);
        initViews();

        // 创建微博对外接口实例
        mWeiboAPI = WeiboSDK.createWeiboAPI(this, ConstantS.APP_KEY);
        mWeiboAPI.responseListener(getIntent(), this);
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboAPI.responseListener(intent, this);
    }

    /**
     * 从本应用->微博->本应用
     * 接收响应数据，该方法被调用。
     * 注意：确保{@link #onCreate(Bundle)} 与 {@link #onNewIntent(Intent)}中，
     * 调用 mWeiboAPI.responseListener(intent, this)
     */
    @Override
    public void onResponse(BaseResponse baseResp) {
        switch (baseResp.errCode) {
        case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_OK:
            Toast.makeText(this, "成功！！", Toast.LENGTH_LONG).show();
            break;
        case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_CANCEL:
            Toast.makeText(this, "用户取消！！", Toast.LENGTH_LONG).show();
            break;
        case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_FAIL:
            Toast.makeText(this, baseResp.errMsg + ":失败！！", Toast.LENGTH_LONG).show();
            break;
        }
    }

    /**
     * 用户点击分享按钮，唤起微博客户端进行分享。
     */
    @Override
    public void onClick(View v) {
        if (R.id.sharedBtn == v.getId()) {
            mWeiboAPI.registerApp();
            reqMsg(mTextCb.isChecked(), 
                    mImageCb.isChecked(), 
                    mWebpageRadio.isChecked(),
                    mMusicRadio.isChecked(), 
                    mVideoRadio.isChecked(), 
                    mVoiceRadio.isChecked());
        }
    }

    private void initViews() {
        mTextCb = (CheckBox) findViewById(R.id.sharedTextCb);
        mImageCb = (CheckBox) findViewById(R.id.sharedImageCb);

        MyCheckedChangeListener listener = new MyCheckedChangeListener();
        mWebpageRadio = (RadioButton) findViewById(R.id.sharedWebpageCb);
        mWebpageRadio.setOnCheckedChangeListener(listener);
        mMusicRadio = (RadioButton) findViewById(R.id.sharedMusicCb);
        mMusicRadio.setOnCheckedChangeListener(listener);
        mVideoRadio = (RadioButton) findViewById(R.id.sharedVedioCb);
        mVideoRadio.setOnCheckedChangeListener(listener);
        mVoiceRadio = (RadioButton) findViewById(R.id.sharedVoiceCb);
        mVoiceRadio.setOnCheckedChangeListener(listener);

        mSharedBtn = (Button) findViewById(R.id.sharedBtn);
        mSharedBtn.setOnClickListener(this);

        mTitle = (TextView) findViewById(R.id.titleText);
        mImage = (ImageView) findViewById(R.id.image);

        mMusicTitle     = (TextView) findViewById(R.id.music_title);
        mMusicImage     = (ImageView) findViewById(R.id.music_image);
        mMusicContent   = (TextView) findViewById(R.id.music_desc);
        mMusicUrl       = (TextView) findViewById(R.id.music_url);

        mVideoTitle     = (TextView) findViewById(R.id.video_title);
        mVideoImage     = (ImageView) findViewById(R.id.video_image);
        mVideoContent   = (TextView) findViewById(R.id.video_desc);
        mVideoUrl       = (TextView) findViewById(R.id.video_url);

        mWebpageTitle   = (TextView) findViewById(R.id.webpage_title);
        mWebpageImage   = (ImageView) findViewById(R.id.webpage_image);
        mWebpageContent = (TextView) findViewById(R.id.webpage_desc);
        mWebpageUrl     = (TextView) findViewById(R.id.webpage_url);

        mVoiceTitle     = (TextView) findViewById(R.id.voice_title);
        mVoiceImage     = (ImageView) findViewById(R.id.voice_image);
        mVoiceContent   = (TextView) findViewById(R.id.voice_desc);
        mVoiceUrl       = (TextView) findViewById(R.id.voice_url);

    }

    private class MyCheckedChangeListener implements
            android.widget.CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mWebpageRadio.setChecked(false);
            mMusicRadio.setChecked(false);
            mVideoRadio.setChecked(false);
            mVoiceRadio.setChecked(false);

            buttonView.setChecked(isChecked);
        }
    }

    private void reqMsg(boolean hasText, boolean hasImage, boolean hasWebpage, 
            boolean hasMusic, boolean hasVedio, boolean hasVoice) {
        
        if (mWeiboAPI.isWeiboAppSupportAPI()) {
            Toast.makeText(this, "当前微博版本支持SDK分享", Toast.LENGTH_SHORT).show();
            
            int supportApi = mWeiboAPI.getWeiboAppSupportAPI();
            if (supportApi >= 10351) {
                Toast.makeText(this, "当前微博版本支持多条消息，Voice消息分享", Toast.LENGTH_SHORT).show();
                reqMultiMsg(hasText, hasImage, hasWebpage, hasMusic, hasVedio, hasVoice);
            } else {
                Toast.makeText(this, "当前微博版本只支持单条消息分享", Toast.LENGTH_SHORT).show();
                reqSingleMsg(hasText, hasImage, hasWebpage, hasMusic, hasVedio/*, hasVoice*/);
            }
        } else {
            Toast.makeText(this, "当前微博版本不支持SDK分享", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 注意：当isWeiboAppSupportAPI() >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）， 并且支持Voice消息。
     * 
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     * @param hasVoice   分享的内容是否有声音
     */
    private void reqMultiMsg(boolean hasText, boolean hasImage, boolean hasWebpage,
            boolean hasMusic, boolean hasVideo, boolean hasVoice) {
        
        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        
        if (hasImage) {
            weiboMessage.imageObject = getImageObj();
        }
        
        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj();
        }
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }
        if (hasVideo) {
            weiboMessage.mediaObject = getVideoObj();
        }
        if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }
        
        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest req = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.multiMessage = weiboMessage;
        
        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboAPI.sendRequest(this, req);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 当isWeiboAppSupportAPI() < 10351 只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     * 
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     */
    private void reqSingleMsg(boolean hasText, boolean hasImage, boolean hasWebpage,
            boolean hasMusic, boolean hasVideo/*, boolean hasVoice*/) {
        
        // 1. 初始化微博的分享消息
        // 用户可以分享文本、图片、网页、音乐、视频中的一种
        WeiboMessage weiboMessage = new WeiboMessage();
        if (hasText) {
            weiboMessage.mediaObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.mediaObject = getImageObj();
        }
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj();
        }
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }
        if (hasVideo) {
            weiboMessage.mediaObject = getVideoObj();
        }
        /*if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }*/
        
        // 2. 初始化从第三方到微博的消息请求
        SendMessageToWeiboRequest req = new SendMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = weiboMessage;
        
        // 3. 发送请求消息到微博，唤起微博分享界面
        mWeiboAPI.sendRequest(this, req);
    }

    private String getActionUrl() {
        return "http://sina.com?eet" + System.currentTimeMillis();
    }

    /**
     * 文本消息构造方法。
     * 
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = mTitle.getText().toString();
        return textObject;
    }

    /**
     * 图片消息构造方法。
     * 
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mImage.getDrawable();
        imageObject.setImageObject(bitmapDrawable.getBitmap());
        return imageObject;
    }

    /**
     * 多媒体（网页）消息构造方法。
     * 
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Util.generateId();// 创建一个唯一的ID
        mediaObject.title = mWebpageTitle.getText().toString();
        mediaObject.description = mWebpageContent.getText().toString();
        
        // 设置bitmap类型的图片到视频对象里
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mWebpageImage.getDrawable();
        mediaObject.setThumbImage(bitmapDrawable.getBitmap());
        mediaObject.actionUrl = getActionUrl();
        mediaObject.defaultText = "webpage默认文案";
        return mediaObject;
    }

    /**
     * 多媒体（视频）消息构造方法。
     * 
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObj() {
        // 创建媒体消息
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Util.generateId();// 创建一个唯一的ID
        videoObject.title = mVideoTitle.getText().toString();
        videoObject.description = mVideoContent.getText().toString();
        
        // 设置bitmap类型的图片到视频对象里
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mVideoImage.getDrawable();
        videoObject.setThumbImage(bitmapDrawable.getBitmap());
        videoObject.actionUrl = getActionUrl();
        videoObject.dataUrl = "www.weibo.com";
        videoObject.dataHdUrl = "www.weibo.com";
        videoObject.duration = 10;
        videoObject.defaultText = "vedio默认文案";
        return videoObject;
    }

    /**
     * 多媒体（音乐）消息构造方法。
     * 
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getMusicObj() {
        // 创建媒体消息
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Util.generateId();// 创建一个唯一的ID
        musicObject.title = mMusicTitle.getText().toString();
        musicObject.description = mMusicContent.getText().toString();
        
        // 设置bitmap类型的图片到视频对象里
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mMusicImage.getDrawable();
        musicObject.setThumbImage(bitmapDrawable.getBitmap());
        musicObject.actionUrl = getActionUrl();
        musicObject.dataUrl = "www.weibo.com";
        musicObject.dataHdUrl = "www.weibo.com";
        musicObject.duration = 10;
        musicObject.defaultText = "music默认文案";
        return musicObject;
    }

    /**
     * 多媒体（音频）消息构造方法。
     * 
     * @return 多媒体（音乐）消息对象。
     */
    private VoiceObject getVoiceObj() {
        // 创建媒体消息
        VoiceObject voiceObject = new VoiceObject();
        voiceObject.identify = Util.generateId();// 创建一个唯一的ID
        voiceObject.title = mVoiceTitle.getText().toString();
        voiceObject.description = mVoiceContent.getText().toString();
        
        // 设置bitmap类型的图片到视频对象里
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mVoiceImage.getDrawable();
        voiceObject.setThumbImage(bitmapDrawable.getBitmap());
        voiceObject.actionUrl = getActionUrl();
        voiceObject.dataUrl = "www.weibo.com";
        voiceObject.dataHdUrl = "www.weibo.com";
        voiceObject.duration = 10;
        voiceObject.defaultText = "voice默认文案";
        return voiceObject;
    }
}
