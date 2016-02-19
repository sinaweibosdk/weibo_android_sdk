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

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.sina.weibo.sdk.demo.openapi.WBOpenAPIActivity;
import com.sina.weibo.sdk.statistic.WBAgent;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 该类是整个 DEMO 程序的入口。
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBDemoMainActivity extends Activity {

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtil.sIsLogEnable = true;
        
        // 微博授权功能
        this.findViewById(R.id.feature_oauth).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBDemoMainActivity.this, WBAuthActivity.class));
            }
        });

        // 分享到微博功能
        this.findViewById(R.id.feature_share).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBDemoMainActivity.this, WBShareMainActivity.class));
            }
        });
        
        // 社会化组件
        this.findViewById(R.id.social_component).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBDemoMainActivity.this, WBSocialActivity.class));
            }
        });
        
        // 登录注销按钮功能
        this.findViewById(R.id.feature_login_logout).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBDemoMainActivity.this, WBLoginLogoutActivity.class));
            }
        });
        
        // 开放接口（Open API）功能
        this.findViewById(R.id.feature_open_api).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBDemoMainActivity.this, WBOpenAPIActivity.class));
            }
        });
        // 游戏入口
        this.findViewById(R.id.feature_game).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBDemoMainActivity.this, WBGameActivity.class));
            }
        });
        
        
        // 支付入口
        this.findViewById(R.id.feature_pay).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBDemoMainActivity.this, WBPayActivity.class));
            }
        });
        
        // 分享到私信入口
        this.findViewById(R.id.shear_message).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WBDemoMainActivity.this, WBShareToMessageFriendActivity.class));
            }
        });
        
        
        // 日志上传（Open API）功能
        this.findViewById(R.id.feature_upload_log).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(WBDemoMainActivity.this, WBStatisticActivity.class));
                        //统计事件
                        Map<String, String> extend = new HashMap<String, String>();
                        extend.put("object", "button");
                        WBAgent.onEvent(WBDemoMainActivity.this, "upload_log", extend);
                    }
                });
        
        
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        //统计应用启动时间
      WBAgent.onPageStart("WBDemoMainActivity");
      WBAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //统计页面退出
      WBAgent.onPageEnd("WBDemoMainActivity");
      WBAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //退出应用时关闭统计进程
        WBAgent.onKillProcess();
    }
    
    
    //初始化日志统计相关的数据 
    public void initLog() {
        WBAgent.setAppKey(Constants.APP_KEY);
        WBAgent.setChannel("weibo"); //这个是统计这个app 是从哪一个平台down下来的  百度手机助手
        
        WBAgent.openActivityDurationTrack(false);
        try {
            //设置发送时间间隔 需大于90s小于8小时
            WBAgent.setUploadInterval(91000);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }  
}
