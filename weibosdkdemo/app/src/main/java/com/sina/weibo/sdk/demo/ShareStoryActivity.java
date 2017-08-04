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
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.io.File;

/**
 * Created by xuesong6 on 2017/7/19.
 */

public class ShareStoryActivity extends Activity implements WbShareCallback {
    private RadioButton picRadio;
    private RadioButton videoRadio;
    private Button shareBtn;
    private WbShareHandler shareHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        picRadio = (RadioButton)findViewById(R.id.radio_story_pic);
        videoRadio = (RadioButton)findViewById(R.id.radio_story_video);
        shareBtn = (Button)findViewById(R.id.story_share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoryMessage storyMessage = new StoryMessage();

                if(picRadio.isChecked()){
                    File picFile = new File(getExternalFilesDir(null)+"/aaa.png");

                    storyMessage.setImageUri(Uri.fromFile(picFile));

                }else{
                    File videoFile = new File(getExternalFilesDir(null)+"/eeee.mp4");
                    storyMessage.setVideoUri(Uri.fromFile(videoFile));
                }
                shareHandler.shareToStory(storyMessage);
            }
        });
        shareHandler = new WbShareHandler(this);
        shareHandler.registerApp();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent,this);
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
}
