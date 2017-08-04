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
import android.view.View;
import android.view.View.OnClickListener;

import com.sina.weibo.sdk.R;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.statistic.WBAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
        WbSdk.install(this,new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
        // 微博授权功能
        this.findViewById(R.id.feature_oauth).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                initLog();
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
        
        this.findViewById(R.id.goto_weibo_page).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WBDemoMainActivity.this,WeiboPageActivity.class));
            }
        });
        findViewById(R.id.feature_story).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WBDemoMainActivity.this,ShareStoryActivity.class);
                startActivity(intent);
            }
        });
        copyFile("eeee.mp4");
        copyFile("aaa.png");
        copyFile("bbbb.jpg");
        copyFile("ccc.JPG");
        copyFile("eee.jpg");
        copyFile("ddd.jpg");
        copyFile("fff.jpg");
        copyFile("ggg.JPG");
        copyFile("hhhh.jpg");
        copyFile("kkk.JPG");

    }

    private void copyFile(final String fileName){

        final File file = new File(getExternalFilesDir(null).getPath()+"/"+fileName);
        if(!file.exists()){
            //复制文件
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        InputStream inputStream = getAssets().open(fileName);
                        OutputStream outputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[1444];
                        int readSize = 0;
                        while ((readSize = inputStream.read(buffer)) != 0){
                            outputStream.write(buffer,0,readSize);
                        }
                        inputStream.close();
                        outputStream.close();
                    }catch (Exception e){}

                }
            });
            thread.start();
        }
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

    private void testSendAction(){

    }
}
