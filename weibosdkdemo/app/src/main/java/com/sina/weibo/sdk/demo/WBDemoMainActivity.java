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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.sina.weibo.sdk.R;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.network.IRequestParam;
import com.sina.weibo.sdk.network.IRequestService;
import com.sina.weibo.sdk.network.impl.RequestParam;
import com.sina.weibo.sdk.network.impl.RequestService;
import com.sina.weibo.sdk.network.target.SimpleTarget;
import com.sina.weibo.sdk.utils.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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

        LogUtil.enableLog();


        // 微博授权功能
        this.findViewById(R.id.feature_oauth).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LogUtil.e("aidinfo", "请求数据");

                WbSdk.install(WBDemoMainActivity.this, new AuthInfo(WBDemoMainActivity.this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));


                SsoHandler mSsoHandler = new SsoHandler(WBDemoMainActivity.this);


                /*mSsoHandler.fetchGuestUserInfoAsync("hwshortvideo", "KTazRAtKBnSd05lJm6fxL4lEbfLTayyw", "1478195010", new IGuestUserInfoListener() {
                    @Override
                    public void onGuestUserInfoRetrieved(GuestUserInfo userInfo) {
                        LogUtil.e("aidinfo", "用户信息" + userInfo.getGsid());
                    }

                    @Override
                    public void onGuestUserInfoRetrievedFailed(WeiboException e) {
                        LogUtil.e("aidinfo", "获取势必爱" + e.toString());
                    }
                });*/

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

                        //WbSdk.install(WBDemoMainActivity.this,new AuthInfo(WBDemoMainActivity.this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));
//                        startActivity(new Intent(WBDemoMainActivity.this, WBStatisticActivity.class));
//                        //统计事件
//                        Map<String, String> extend = new HashMap<String, String>();
//                        extend.put("object", "button");
//                        WBAgent.onEvent(WBDemoMainActivity.this, "upload_log", extend);
                        WbSdk.install(WBDemoMainActivity.this, new AuthInfo(WBDemoMainActivity.this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE));

                        IRequestService requestService = RequestService.getInstance();
                        RequestParam.Builder builder = new RequestParam.Builder(WBDemoMainActivity.this);
                        builder.setShortUrl("http://api.weibo.cn/2/client/addlog_batch");
                        builder.defaultHostEnable(false);
                        //builder.setNeedIntercept(false);
                        builder.setRequestType(IRequestParam.RequestType.POST);
                        builder.addBodyParam("s", "653b1431".getBytes());
                        builder.addBodyParam("gsid", "_2A253vMWRDeRxGedI41AQ8S3PzzuIHXVS6F5ZrDV6PUJbkdAKLVnHkWpNVqQJXUEgtRFNu9dlYz85qrZuPE3r_ML8".getBytes());
                        builder.addBodyParam("from", "1084195010".getBytes());
                        builder.addBodyParam("i", "c2c6522".getBytes());
                        builder.addBodyParam("c", "android".getBytes());
                        builder.addBodyParam("addlogs", "[{\"act\":\"guardunionlog\",\"logs\":[{\"act\":\"guardunionlog\",\"client_id\":\"\",\"extended\":\"p_state:0\",\"effective\":false,\"version_name\":\"8.4.1\",\"from\":\"1084195010\",\"name\":\"weibo_sync_guard\",\"time\":\"2018_04_10_19:46:57\",\"device_id\":\"ca0a438b12043052d967eeaafceddbea96d70f9c\",\"services\":\"com.sina.push.service.SinaPushService\"},{\"act\":\"guardunionlog\",\"client_id\":\"\",\"extended\":\"\",\"effective\":true,\"version_name\":\"8.4.1\",\"from\":\"1084195010\",\"name\":\"weibo\",\"time\":\"2018_04_10_19:46:59\",\"device_id\":\"ca0a438b12043052d967eeaafceddbea96d70f9c\",\"services\":\"com.sina.weibo.releasemanager|com.igexin.sdk.PushService|result:1\"}]},{\"act\":\"actlog\",\"logs\":[{\"act\":\"actlog\",\"act_code\":\"929\",\"ext\":\"timestamp:1523360817323|bannerid:\",\"from\":\"1084195010\",\"actionlog\":\"\"},{\"act\":\"actlog\",\"act_code\":\"1339\",\"uicode\":\"10000011\",\"ext\":\"flag:3|uid:1682113347|did:ca0a438b12043052d967eeaafceddbea96d70f9c|loginTime:1523360813.834|useTime:5.429\",\"fid\":\"1005051682113347_-_new\",\"from\":\"1084195010\"},{\"act\":\"actlog\",\"act_code\":\"1339\",\"uicode\":\"10000011\",\"ext\":\"flag:0|uid:1682113347|ext_uid:5301652531|did:ca0a438b12043052d967eeaafceddbea96d70f9c|toForegroundTime:1523360813.834|toBackgroundTime:1523360819.263|useTime:5.429\",\"fid\":\"1005051682113347_-_new\",\"from\":\"1084195010\"},{\"act\":\"actlog\",\"act_code\":\"1464\",\"ext\":\"page_time:10000011#100505##=4.997&1|toForegroundTime:1523360813.834|toBackgroundTime:1523360819.263\",\"from\":\"1084195010\"}]},{\"act\":\"ad_track\",\"logs\":[{\"act\":\"ad_track\",\"act_code\":\"ad_net_api\",\"from\":\"1084195010\",\"time\":\"1523360817735\",\"type\":\"actionad\",\"is_ok\":\"1\"}]}]".getBytes());
                        builder.addBodyParam("aid", "01Ar_N4vmZWIrsUjCReaLtwt-YCR-n1ayjqI8OrI_qwL7qA-g.".getBytes());

                        requestService.asyncRequest(builder.build(), new SimpleTarget() {
                            @Override
                            public void onSuccess(String response) {

                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });

                        //testThread();
                    }
                });

        this.findViewById(R.id.goto_weibo_page).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WBDemoMainActivity.this, WBAuthActivity.class));
            }
        });
        findViewById(R.id.feature_story).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WBDemoMainActivity.this, ShareStoryActivity.class);
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

    private void testThread() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        MyThread thread = new MyThread(list, 0);
        MyThread thread1 = new MyThread(list, 1);
        thread.start();
        thread1.start();


    }

    private class MyThread extends Thread {
        ArrayList<Integer> list;
        private int type;

        public MyThread(ArrayList list, int type) {
            this.list = list;
            this.type = type;
        }

        @Override
        public void run() {
            super.run();
            if (type == 0) {
                readList(list);
            } else {
                readList1(list);
            }
        }
    }

    private void readList1(ArrayList<Integer> list) {
        Log.v("zxs", "readList1 start");
        synchronized (list) {
            for (int i = 0; i < 10; i++) {
                list.add(10 + 1);
                Log.v("zxs", "添加数据 " + i);
            }
        }
        Log.v("zxs", "readList1 end");
    }

    private void readList(ArrayList<Integer> list) {
        Log.v("zxs", "readList2 start");
//        synchronized (list){
//
//        }
        for (Integer i : list) {
            Log.v("zxs", "读取数据 " + i);
        }
        Log.v("zxs", "readList2 end");
    }

    private void copyFile(final String fileName) {
        final File file = new File(getExternalFilesDir(null).getPath() + "/" + fileName);
        if (!file.exists()) {
            //复制文件
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = getAssets().open(fileName);
                        OutputStream outputStream = new FileOutputStream(file);
                        byte[] buffer = new byte[1444];
                        int readSize = 0;
                        while ((readSize = inputStream.read(buffer)) != 0) {
                            outputStream.write(buffer, 0, readSize);
                        }
                        inputStream.close();
                        outputStream.close();
                    } catch (Exception e) {
                    }

                }
            });
            thread.start();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
