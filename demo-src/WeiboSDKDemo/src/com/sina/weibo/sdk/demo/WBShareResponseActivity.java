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
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseRequest;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.ProvideMessageForWeiboResponse;
import com.sina.weibo.sdk.api.share.ProvideMultiMessageForWeiboResponse;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 该类演示了第三方应用如何通过微博客户端分享文字、图片、视频、音乐等。
 * 执行流程： 从微博->本应用->微博
 * <p><b>该类与 {@link WBShareActivity} 类的界面及流程大致一样，请注意区分。</b></p>
 * 
 * @author SINA
 * @since 2013-11-01
 */
public class WBShareResponseActivity extends Activity implements OnClickListener, IWeiboHandler.Request {
    @SuppressWarnings("unused")
    private static final String TAG = "WBShareResponseActivity";

    /** 界面标题 */
    private TextView        mTitleView;    
    /** 分享图片 */
    private ImageView       mImageView;
    /** 用于控制是否分享文本的 CheckBox */
    private CheckBox        mTextCheckbox;
    /** 用于控制是否分享图片的 CheckBox */
    private CheckBox        mImageCheckbox;
    /** 分享网页控件 */
    private WBShareItemView mShareWebPageView;
    /** 分享音乐控件 */
    private WBShareItemView mShareMusicView;
    /** 分享视频控件 */
    private WBShareItemView mShareVideoView;
    /** 分享声音控件 */
    private WBShareItemView mShareVoiceView;
    /** 分享按钮 */
    private Button          mSharedBtn;
    
    /** 微博微博分享接口实例 */
    private IWeiboShareAPI  mShareWeiboAPI    = null;
    /** 从微博客户端唤起第三方应用时，客户端发送过来的请求数据对象 */
    private BaseRequest     mBaseRequest = null;

    /**
     * @see {@link Activity#onCreate}
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        initViews();

        // 创建微博分享接口实例
        mShareWeiboAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);
        
        // 处理微博客户端发送过来的请求
		mShareWeiboAPI.handleWeiboRequest(getIntent(), this);
	}

    /**
     * @see {@link Activity#onNewIntent}
     */
	@Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	setIntent(intent);
    	
    	// 处理微博客户端发送过来的请求
    	mShareWeiboAPI.handleWeiboRequest(intent, this);
    }

    /**
     * 接收微客户端博请求的数据。
     * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
     * 
     * @param baseRequest 微博请求数据对象
     * @see {@link IWeiboShareAPI#handleWeiboRequest}
     */
    @Override
    public void onRequest(BaseRequest baseRequest) {
        // 保存从微博客户端唤起第三方应用时，客户端发送过来的请求数据对象
        mBaseRequest = baseRequest;
        
        int resId = (mBaseRequest != null) ? 
                R.string.weibosdk_demo_toast_share_response_args_success : 
                R.string.weibosdk_demo_toast_share_response_args_failed;
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    /**
     * 用户点击分享按钮，将数据发送给微博客户端。
     */
    @Override
    public void onClick(View v) {
    	if (null == mBaseRequest) {
    		Toast.makeText(this, R.string.weibosdk_demo_toast_share_response_args_failed, 
    		        Toast.LENGTH_LONG).show();

    		// 结束掉当前 Activity
    		finish();
    		return;
    	}
    	
    	if (R.id.share_to_btn == v.getId()) {
    	    // 发送响应消息给微博客户端
            responseMessage(mTextCheckbox.isChecked(), 
                            mImageCheckbox.isChecked(), 
                            mShareWebPageView.isChecked(),
                            mShareMusicView.isChecked(), 
                            mShareVideoView.isChecked(), 
                            mShareVoiceView.isChecked());
            
            // 结束掉当前 Activity
            finish();
        }
    }

    /**
     * 初始化界面。
     */
    private void initViews() {
        mTitleView = (TextView) findViewById(R.id.share_title);
        mTitleView.setText(R.string.weibosdk_demo_share_from_weibo_title);
        mImageView = (ImageView) findViewById(R.id.share_imageview);
        mTextCheckbox = (CheckBox) findViewById(R.id.share_text_checkbox);
        mImageCheckbox = (CheckBox) findViewById(R.id.shared_image_checkbox);

        mSharedBtn = (Button) findViewById(R.id.share_to_btn);
        mSharedBtn.setOnClickListener(this);
        
        mShareWebPageView = (WBShareItemView)findViewById(R.id.share_webpage_view);
        mShareMusicView = (WBShareItemView)findViewById(R.id.share_music_view);
        mShareVideoView = (WBShareItemView)findViewById(R.id.share_video_view);
        mShareVoiceView = (WBShareItemView)findViewById(R.id.share_voice_view);
        mShareWebPageView.setOnCheckedChangeListener(mCheckedChangeListener);
        mShareMusicView.setOnCheckedChangeListener(mCheckedChangeListener);
        mShareVideoView.setOnCheckedChangeListener(mCheckedChangeListener);
        mShareVoiceView.setOnCheckedChangeListener(mCheckedChangeListener);
        
        mShareWebPageView.initWithRes(
                R.string.weibosdk_demo_share_webpage_title, 
                R.drawable.ic_sina_logo, 
                R.string.weibosdk_demo_share_webpage_title, 
                R.string.weibosdk_demo_share_webpage_desc, 
                R.string.weibosdk_demo_test_webpage_url);
        
        mShareMusicView.initWithRes(
                R.string.weibosdk_demo_share_music_title, 
                R.drawable.ic_share_music_thumb, 
                R.string.weibosdk_demo_share_music_title, 
                R.string.weibosdk_demo_share_music_desc, 
                R.string.weibosdk_demo_test_music_url);
        
        mShareVideoView.initWithRes(
                R.string.weibosdk_demo_share_video_title, 
                R.drawable.ic_share_video_thumb, 
                R.string.weibosdk_demo_share_video_title, 
                R.string.weibosdk_demo_share_video_desc, 
                R.string.weibosdk_demo_test_video_url);
        
        mShareVoiceView.initWithRes(
                R.string.weibosdk_demo_share_voice_title, 
                R.drawable.ic_share_voice_thumb, 
                R.string.weibosdk_demo_share_voice_title, 
                R.string.weibosdk_demo_share_voice_desc, 
                R.string.weibosdk_demo_test_voice_url);
    }

    /**
     * 监听 RadioButton 的点击事件。
     */
    private WBShareItemView.OnCheckedChangeListener mCheckedChangeListener = new WBShareItemView.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(WBShareItemView view, boolean isChecked) {
            mShareWebPageView.setIsChecked(false);
            mShareMusicView.setIsChecked(false);
            mShareVideoView.setIsChecked(false);
            mShareVoiceView.setIsChecked(false);
            
            view.setIsChecked(isChecked);
        }
    };
	
    /**
     * 第三方应用响应微博客户端的请求，提供需要分享的数据。
     * @see {@link #responseMultiMessage} 或者 {@link #responseSingleMessage}
     */
	private void responseMessage(boolean hasText, boolean hasImage, 
            boolean hasWebpage, boolean hasMusic, boolean hasVideo, boolean hasVoice) {
        
        if (mShareWeiboAPI.isWeiboAppSupportAPI()) {
            int supportApi = mShareWeiboAPI.getWeiboAppSupportAPI();
            if (supportApi >= 10351 /*ApiUtils.BUILD_INT_VER_2_2*/) {
                responseMultiMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo, hasVoice);
            } else {
                responseSingleMessage(hasText, hasImage, hasWebpage, hasMusic, hasVideo/*, hasVoice*/);
            }
        } else {
            Toast.makeText(this, R.string.weibosdk_demo_not_support_api_hint, Toast.LENGTH_SHORT).show();
        }
    }
	
    /**
     * 第三方应用响应微博客户端的请求，提供需要分享的数据。
     * 注意：当 {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     * 
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     * @param hasVoice   分享的内容是否有声音
     */
    private void responseMultiMessage(boolean hasText, boolean hasImage, boolean hasWebpage, 
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
        
		// 2. 初始化从微博到第三方的消息请求
        ProvideMultiMessageForWeiboResponse response = new ProvideMultiMessageForWeiboResponse();
        response.transaction = mBaseRequest.transaction;
        response.reqPackageName = mBaseRequest.packageName;
        response.multiMessage = weiboMessage;
       
	    // 3. 发送响应消息到微博
        mShareWeiboAPI.sendResponse(response);
    }
    
    /**
     * 第三方应用响应微博客户端的请求，提供需要分享的数据。
     * 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()} < 10351 时，只支持分享单条消息，即
     * 文本、图片、网页、音乐、视频中的一种，不支持Voice消息。
     * 
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasMusic   分享的内容是否有音乐
     * @param hasVideo   分享的内容是否有视频
     */
    private void responseSingleMessage(boolean hasText, boolean hasImage, boolean hasWebpage, 
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
        
		// 2. 初始化从微博到第三方的消息请求
        ProvideMessageForWeiboResponse response = new ProvideMessageForWeiboResponse();
        response.transaction = mBaseRequest.transaction;
        response.reqPackageName = mBaseRequest.packageName;
        response.message = weiboMessage;
        
		// 3. 发送响应消息到微博
        mShareWeiboAPI.sendResponse(response);
    }
	
    /**
     * 获取分享的文本模板。
     * 
     * @return 分享的文本模板
     */
    private String getSharedText() {
        int formatId = R.string.weibosdk_demo_share_text_template;
        String format = getString(formatId);
        String text = format;
        String demoUrl = getString(R.string.weibosdk_demo_app_url);
        if (mTextCheckbox.isChecked() || mImageCheckbox.isChecked()) {
            format = getString(R.string.weibosdk_demo_share_text_template);
        }
        if (mShareWebPageView.isChecked()) {
            format = getString(R.string.weibosdk_demo_share_webpage_template);
            text = String.format(format, getString(R.string.weibosdk_demo_share_webpage_demo), demoUrl);
        }
        if (mShareMusicView.isChecked()) {
            format = getString(R.string.weibosdk_demo_share_music_template);
            text = String.format(format, getString(R.string.weibosdk_demo_share_music_demo), demoUrl);
        }
        if (mShareVideoView.isChecked()) {
            format = getString(R.string.weibosdk_demo_share_video_template);
            text = String.format(format, getString(R.string.weibosdk_demo_share_video_demo), demoUrl);
        }
        if (mShareVoiceView.isChecked()) {
            format = getString(R.string.weibosdk_demo_share_voice_template);
            text = String.format(format, getString(R.string.weibosdk_demo_share_voice_demo), demoUrl);
        }
        
        return text;
    }

    /**
     * 创建文本消息对象。
     * 
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = getSharedText();
        return textObject;
    }

    /**
     * 创建图片消息对象。
     * 
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageView.getDrawable();
        imageObject.setImageObject(bitmapDrawable.getBitmap());
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
        mediaObject.title = mShareWebPageView.getTitle();
        mediaObject.description = mShareWebPageView.getShareDesc();
        
        // 设置 Bitmap 类型的图片到视频对象里
        mediaObject.setThumbImage(mShareWebPageView.getThumbBitmap());
        mediaObject.actionUrl = mShareWebPageView.getShareUrl();
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }

    /**
     * 创建多媒体（音乐）消息对象。
     * 
     * @return 多媒体（音乐）消息对象。
     */
    private MusicObject getMusicObj() {
        // 创建媒体消息
        MusicObject musicObject = new MusicObject();
        musicObject.identify = Utility.generateGUID();
        musicObject.title = mShareMusicView.getTitle();
        musicObject.description = mShareMusicView.getShareDesc();
        
        // 设置 Bitmap 类型的图片到视频对象里
        musicObject.setThumbImage(mShareMusicView.getThumbBitmap());
        musicObject.actionUrl = mShareMusicView.getShareUrl();
        musicObject.dataUrl = "www.weibo.com";
        musicObject.dataHdUrl = "www.weibo.com";
        musicObject.duration = 10;
        musicObject.defaultText = "Music 默认文案";
        return musicObject;
    }

    /**
     * 创建多媒体（视频）消息对象。
     * 
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObj() {
        // 创建媒体消息
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = mShareVideoView.getTitle();
        videoObject.description = mShareVideoView.getShareDesc();
        
        // 设置 Bitmap 类型的图片到视频对象里
        videoObject.setThumbImage(mShareVideoView.getThumbBitmap());
        videoObject.actionUrl = mShareVideoView.getShareUrl();
        videoObject.dataUrl = "www.weibo.com";
        videoObject.dataHdUrl = "www.weibo.com";
        videoObject.duration = 10;
        videoObject.defaultText = "Vedio 默认文案";
        return videoObject;
    }

    /**
     * 创建多媒体（音频）消息对象。
     * 
     * @return 多媒体（音乐）消息对象。
     */
    private VoiceObject getVoiceObj() {
        // 创建媒体消息
        VoiceObject voiceObject = new VoiceObject();
        voiceObject.identify = Utility.generateGUID();
        voiceObject.title = mShareVoiceView.getTitle();
        voiceObject.description = mShareVoiceView.getShareDesc();
        
        // 设置 Bitmap 类型的图片到视频对象里
        voiceObject.setThumbImage(mShareVoiceView.getThumbBitmap());
        voiceObject.actionUrl = mShareVoiceView.getShareUrl();
        voiceObject.dataUrl = "www.weibo.com";
        voiceObject.dataHdUrl = "www.weibo.com";
        voiceObject.duration = 10;
        voiceObject.defaultText = "Voice 默认文案";
        return voiceObject;
    }
}
