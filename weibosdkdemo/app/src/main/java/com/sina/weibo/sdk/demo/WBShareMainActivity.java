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
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.sina.weibo.sdk.R;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.io.File;

/**
 * 该类是分享功能的入口。
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBShareMainActivity extends Activity {
    /** 微博分享按钮 */
    private Button mShareButton;
    
    /** 微博 ALL IN ONE 分享按钮 */
    private Button mShareAllInOneButton;

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_main);
        initialize();
    }

    /**
     * 初始化 UI 和微博接口实例 。
     */
    private void initialize() {
        /**
         * 初始化 UI
         */
        // 设置提示文本
        ((TextView)findViewById(R.id.register_app_to_weibo_hint)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)findViewById(R.id.weibosdk_demo_support_api_level_hint)).setMovementMethod(LinkMovementMethod.getInstance());
        // 设置注册按钮对应回调
        findViewById(R.id.register_app_to_weibo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 注册到新浪微博
//                Toast.makeText(WBShareMainActivity.this,
//                        R.string.weibosdk_demo_toast_register_app_to_weibo, Toast.LENGTH_LONG).show();
                
                mShareButton.setEnabled(true);
                mShareAllInOneButton.setEnabled(true);
                WbShareHandler shareHandler = new WbShareHandler(WBShareMainActivity.this);
                shareHandler.registerApp();
            }
        });
        
        // 设置分享按钮对应回调
        mShareButton = (Button) findViewById(R.id.share_to_weibo);
        mShareButton.setEnabled(false);
        mShareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WBShareMainActivity.this, WBShareActivity.class);
                i.putExtra(WBShareActivity.KEY_SHARE_TYPE, WBShareActivity.SHARE_CLIENT);
                File file = new File("xxxx");
                i.setData(Uri.parse(file.getPath()));
                startActivity(i);
            }
        });
        
        // 设置ALL IN ONE分享按钮对应回调
        mShareAllInOneButton = (Button) findViewById(R.id.share_to_weibo_all_in_one);
        mShareAllInOneButton.setEnabled(false);
        mShareAllInOneButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(WBShareMainActivity.this, WBShareActivity.class);
                i.putExtra(WBShareActivity.KEY_SHARE_TYPE, WBShareActivity.SHARE_ALL_IN_ONE);
                startActivity(i);
            }
        });
    }
}
