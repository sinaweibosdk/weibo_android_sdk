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
import com.sina.weibo.sdk.api.BaseRequest;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboHandler;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.ProvideMessageForWeiboRequest;
import com.sina.weibo.sdk.api.ProvideMessageForWeiboResponse;
import com.sina.weibo.sdk.api.ProvideMultiMessageForWeiboResponse;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.log.Log;
import com.sina.weibo.sdk.utils.Util;

/**
 * 执行流程： 2. 从微博->本应用->微博
 * 
 * @author taibin@staff.sina.com.cn
 * 
 */
public class ResponseMessageActivity extends Activity implements
		OnClickListener, IWeiboHandler.Request {
	IWeiboAPI weiboAPI = null;
	
	TextView title;

	ImageView image;

	TextView musicTitle;
	ImageView musicImage;
	TextView musicContent;
	TextView musicUrl;

	TextView videoTitle;
	ImageView videoImage;
	TextView videoContent;
	TextView videoUrl;

	TextView webpageTitle;
	ImageView webpageImage;
	TextView webpageContent;
	TextView webpageUrl;

	TextView  voiceTitle;
    ImageView voiceImage;
    TextView  voiceContent;
    TextView  voiceUrl;
    
    private CheckBox mTextCb;
    private CheckBox mImageCb;
    private RadioButton mWebpageRadio;
    private RadioButton mMusicRadio;
    private RadioButton mVedioRadio;
    private RadioButton mVoiceRadio;
    
    private Button mSharedBtn;
	
	Bundle mBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_responsemsg);
		weiboAPI = WeiboSDK.createWeiboAPI(this, ConstantS.APP_KEY);	
		mBundle = getIntent().getExtras();
		initViews();
		weiboAPI.requestListener(getIntent(), this);
	}

	private void initViews() {
	    mTextCb = (CheckBox) findViewById(R.id.sharedTextCb);
        mImageCb = (CheckBox) findViewById(R.id.sharedImageCb);
        
        MyCheckedChangeListener listener = new MyCheckedChangeListener();
        mWebpageRadio = (RadioButton) findViewById(R.id.sharedWebpageCb);
        mWebpageRadio.setOnCheckedChangeListener(listener);
        mMusicRadio = (RadioButton) findViewById(R.id.sharedMusicCb);
        mMusicRadio.setOnCheckedChangeListener(listener);
        mVedioRadio = (RadioButton) findViewById(R.id.sharedVedioCb);
        mVedioRadio.setOnCheckedChangeListener(listener);
        mVoiceRadio = (RadioButton) findViewById(R.id.sharedVoiceCb);
        mVoiceRadio.setOnCheckedChangeListener(listener);
        
        mSharedBtn = (Button) findViewById(R.id.sharedBtn);
        mSharedBtn.setOnClickListener(this);
        
        title = (TextView) findViewById(R.id.titleText);
        
        image = (ImageView) findViewById(R.id.image);
        
        musicTitle = (TextView) findViewById(R.id.music_title);
        musicImage = (ImageView) findViewById(R.id.music_image);
        musicContent = (TextView) findViewById(R.id.music_desc);
        musicUrl = (TextView) findViewById(R.id.music_url);
        
        videoTitle = (TextView) findViewById(R.id.video_title);
        videoImage = (ImageView) findViewById(R.id.video_image);
        videoContent = (TextView) findViewById(R.id.video_desc);
        videoUrl = (TextView) findViewById(R.id.video_url);
        
        webpageTitle = (TextView) findViewById(R.id.webpage_title);
        webpageImage = (ImageView) findViewById(R.id.webpage_image);
        webpageContent = (TextView) findViewById(R.id.webpage_desc);
        webpageUrl = (TextView) findViewById(R.id.webpage_url);
        
        voiceTitle = (TextView) findViewById(R.id.voice_title);
        voiceImage = (ImageView) findViewById(R.id.voice_image);
        voiceContent = (TextView) findViewById(R.id.voice_desc);
        voiceUrl = (TextView) findViewById(R.id.voice_url);
        
	}

	private class MyCheckedChangeListener implements android.widget.CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {
            // TODO Auto-generated method stub
            mWebpageRadio.setChecked(false);
            mMusicRadio.setChecked(false);
            mVedioRadio.setChecked(false);
            mVoiceRadio.setChecked(false);
            
            buttonView.setChecked(isChecked);
        }
    }
	
	@Override
	public void onClick(View v) {
		if (mBundle == null) {
			Toast.makeText(this, "未收到微博发起的请求！", Toast.LENGTH_LONG).show();
			return;
		}
		if (R.id.sharedBtn == v.getId()) {
            respMsg(mTextCb.isChecked(), mImageCb.isChecked(), 
                    mWebpageRadio.isChecked(), mMusicRadio.isChecked(), 
                    mVedioRadio.isChecked(), mVoiceRadio.isChecked());
        }
		// 防止创建多个
		finish();
	}
	
	private void respMsg(boolean hasText, boolean hasImage, 
            boolean hasWebpage, boolean hasMusic, boolean hasVedio, boolean hasVoice) {
        
	    if (weiboAPI.isWeiboAppSupportAPI()) {
            Toast.makeText(this, "当前微博版本支持SDK分享", 0).show();
            int supportApi = weiboAPI.getWeiboAppSupportAPI();
            if (supportApi >= 10351) {
                Toast.makeText(this, "当前微博版本支持多条消息，Voice消息分享", 0).show();
                respMultiMsg(hasText, hasImage, hasWebpage, hasMusic, hasVedio, hasVoice);
            } else {
                Toast.makeText(this, "当前微博版本只支持单条消息分享", 0).show();
                respSingleMsg(hasText, hasImage, hasWebpage, hasMusic, hasVedio, hasVoice);
            }
        } else {
            Toast.makeText(this, "当前微博版本不支持SDK分享", 0).show();
        }
    }
	
	
	/**
     * isWeiboAppSupportAPI() >= 10351 支持同时分享多条消息，并且支持Voice消息
     * @param hasText
     * @param hasImage
     * @param hasWebpage
     * @param hasMusic
     * @param hasVedio
     * @param hasVoice
     */
    private void respMultiMsg(boolean hasText, boolean hasImage, 
            boolean hasWebpage, boolean hasMusic, boolean hasVedio, boolean hasVoice) {
        // 微博到三方
        // 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        if (hasImage) {
            weiboMessage.imageObject = getImageObj();
        }
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebpageObj();
        }
        if (hasMusic) {
            weiboMessage.mediaObject = getMusicObj();
        }
        if (hasVedio) {
            weiboMessage.mediaObject = getVideoObj();
        }
        if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }
        // 初始化从微博到三方的消息请求
        ProvideMultiMessageForWeiboResponse resp = new ProvideMultiMessageForWeiboResponse();
        ProvideMessageForWeiboRequest req = new ProvideMessageForWeiboRequest(mBundle);
        resp.transaction = req.transaction;
        resp.reqPackageName = req.packageName;
        resp.multiMessage = weiboMessage;
        // 发送响应消息到微博
        weiboAPI.sendResponse(resp);
    }
    
    /**
     * isWeiboAppSupportAPI() < 10351 只支持分享单条消息，不支持Voice消息
     * @param hasText
     * @param hasImage
     * @param hasWebpage
     * @param hasMusic
     * @param hasVedio
     */
    private void respSingleMsg(boolean hasText, boolean hasImage, 
            boolean hasWebpage, boolean hasMusic, boolean hasVedio, boolean hasVoice) {
     // 微博到三方
        // 初始化微博的分享消息
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
        if (hasVedio) {
            weiboMessage.mediaObject = getVideoObj();
        }
        if (hasVoice) {
            weiboMessage.mediaObject = getVoiceObj();
        }
        // 初始化从微博到三方的消息请求
        ProvideMessageForWeiboResponse resp = new ProvideMessageForWeiboResponse();
        ProvideMessageForWeiboRequest req = new ProvideMessageForWeiboRequest(mBundle);
        resp.transaction = req.transaction;
        resp.message = weiboMessage;
        resp.reqPackageName = req.packageName;
        // 发送响应消息到微博
        weiboAPI.sendResponse(resp);
    }
	
	
	
	
	/**
	 * 文本消息构造方法
	 * 
	 * @return
	 */
	private TextObject getTextObj() {
		TextObject textObject = new TextObject();
		textObject.text = title.getText().toString();
		return textObject;
	}

	/**
	 * 图片消息构造方法
	 * 
	 * @return 图片消息对象
	 */
	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
		imageObject.setImageObject(bitmapDrawable.getBitmap());
		return imageObject;
	}

	/**
	 * 多媒体（网页）消息构造方法
	 * @return 多媒体（网页）消息对象
	 */
	private WebpageObject getWebpageObj() {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Util.generateId();// 创建一个唯一的ID
		mediaObject.title = webpageTitle.getText().toString();
		mediaObject.description = webpageContent.getText().toString();
		// 设置bitmap类型的图片到视频对象里
		BitmapDrawable bitmapDrawable = (BitmapDrawable) webpageImage
				.getDrawable();
		mediaObject.setThumbImage(bitmapDrawable.getBitmap());
		mediaObject.actionUrl = getActionUrl();
		mediaObject.defaultText = "webpage默认文案";
		return mediaObject;
	}

	/**
	 * 多媒体（视频）消息构造方法
	 * @return 多媒体（视频）消息对象
	 */
	private VideoObject getVideoObj() {
		// 创建媒体消息
		VideoObject videoObject = new VideoObject();
		videoObject.identify = Util.generateId();// 创建一个唯一的ID
		videoObject.title = videoTitle.getText().toString();
		videoObject.description = videoContent.getText().toString();
		// 设置bitmap类型的图片到视频对象里
		BitmapDrawable bitmapDrawable = (BitmapDrawable) videoImage
				.getDrawable();
		videoObject.setThumbImage(bitmapDrawable.getBitmap());
		videoObject.actionUrl = getActionUrl();
		videoObject.dataUrl = "www.weibo.com";
		videoObject.dataHdUrl = "www.weibo.com";
		videoObject.duration = 10;
		videoObject.defaultText = "vedio默认文案";
		return videoObject;
	}

	/**
	 * 多媒体（音乐）消息构造方法
	 * 
	 * @return 多媒体（音乐）消息对象
	 */
	private MusicObject getMusicObj() {
		// 创建媒体消息
		MusicObject musicObject = new MusicObject();
		musicObject.identify = Util.generateId();// 创建一个唯一的ID
		musicObject.title = musicTitle.getText().toString();
		musicObject.description = musicContent.getText().toString();
		// 设置bitmap类型的图片到视频对象里
		BitmapDrawable bitmapDrawable = (BitmapDrawable) musicImage
				.getDrawable();
		musicObject.setThumbImage(bitmapDrawable.getBitmap());
		musicObject.actionUrl = getActionUrl();
		musicObject.dataUrl = "www.weibo.com";
		musicObject.dataHdUrl = "www.weibo.com";
		musicObject.duration = 10;
		musicObject.defaultText = "music默认文案";
		return musicObject;
	}
	
	/**
     * 多媒体（音频）消息构造方法
     * 
     * @return 多媒体（音乐）消息对象
     */
    private VoiceObject getVoiceObj() {
        // 创建媒体消息
        VoiceObject voiceObject = new VoiceObject();
        voiceObject.identify = Util.generateId();// 创建一个唯一的ID
        voiceObject.title = voiceTitle.getText().toString();
        voiceObject.description = voiceContent.getText().toString();
        // 设置bitmap类型的图片到视频对象里
        BitmapDrawable bitmapDrawable = (BitmapDrawable) voiceImage
                .getDrawable();
        voiceObject.setThumbImage(bitmapDrawable.getBitmap());
        voiceObject.actionUrl = getActionUrl();
        voiceObject.dataUrl = "www.weibo.com";
        voiceObject.dataHdUrl = "www.weibo.com";
        voiceObject.duration = 10;
        voiceObject.defaultText="voice默认文案";
        return voiceObject;
    }
	
	private String getActionUrl() {
        return "http://sina.com?eet"+System.currentTimeMillis();
    }
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		weiboAPI.requestListener(getIntent(), this);
	}
	@Override
	public void onRequest(BaseRequest baseReq) {
		// TODO
		Log.i("msg", "收到请求消息");
	}

}
