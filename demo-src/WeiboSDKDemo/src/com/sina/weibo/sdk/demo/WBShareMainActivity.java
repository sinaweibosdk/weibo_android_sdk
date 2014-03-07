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
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.IWeiboDownloadListener;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

/**
 * 该类是分享功能的入口。
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBShareMainActivity extends Activity {

    /** 微博分享的接口实例 */
    private IWeiboShareAPI mWeiboShareAPI;
    
    /** 微博分享按钮 */
    private Button mShareButton;

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
        
        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);
        
        // 获取微博客户端相关信息，如是否安装、支持 SDK 的版本
        boolean isInstalledWeibo = mWeiboShareAPI.isWeiboAppInstalled();
        int supportApiLevel = mWeiboShareAPI.getWeiboAppSupportAPI(); 
        
        // 如果未安装微博客户端，设置下载微博对应的回调
        if (!isInstalledWeibo) {
            mWeiboShareAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
                @Override
                public void onCancel() {
                    Toast.makeText(WBShareMainActivity.this, 
                            R.string.weibosdk_demo_cancel_download_weibo, 
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        
        /**
         * 初始化 UI
         */
        // 设置提示文本
        ((TextView)findViewById(R.id.register_app_to_weibo_hint)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)findViewById(R.id.weibosdk_demo_support_api_level_hint)).setMovementMethod(LinkMovementMethod.getInstance());
        
        // 设置微博客户端相关信息
        String installInfo = getString(isInstalledWeibo ? R.string.weibosdk_demo_has_installed_weibo : R.string.weibosdk_demo_has_installed_weibo);
        ((TextView)findViewById(R.id.weibosdk_demo_is_installed_weibo)).setText(installInfo);
        ((TextView)findViewById(R.id.weibosdk_demo_support_api_level)).setText("\t" + supportApiLevel);
        
        // 设置注册按钮对应回调
        ((Button) findViewById(R.id.register_app_to_weibo)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 注册到新浪微博
                mWeiboShareAPI.registerApp();
                Toast.makeText(WBShareMainActivity.this, 
                        R.string.weibosdk_demo_toast_register_app_to_weibo, Toast.LENGTH_LONG).show();
                
                mShareButton.setEnabled(true);
            }
        });
        
        // 设置分享按钮对应回调
        mShareButton = (Button) findViewById(R.id.share_to_weibo);
        mShareButton.setEnabled(false);
        mShareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBShareMainActivity.this, WBShareActivity.class));
            }
        });
    }
}
