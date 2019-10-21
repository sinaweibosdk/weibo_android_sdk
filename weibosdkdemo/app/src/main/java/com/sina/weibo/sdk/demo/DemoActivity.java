package com.sina.weibo.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sina.weibo.sdk.R;
import com.sina.weibo.sdk.network.IRequestParam;
import com.sina.weibo.sdk.network.IRequestService;
import com.sina.weibo.sdk.network.impl.RequestParam;
import com.sina.weibo.sdk.network.impl.RequestService;
import com.sina.weibo.sdk.network.target.SimpleTarget;

import java.io.File;

/**
 * Created by xuesong6 on 2018/3/29.
 */

public class DemoActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Button button = (Button) findViewById(R.id.test_request);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IRequestService requestService = RequestService.getInstance();
                RequestParam.Builder builder = new RequestParam.Builder(DemoActivity.this);
                builder.setShortUrl("http://39.107.104.47:8081/SmartService/test");
                builder.setRequestType(IRequestParam.RequestType.POST);
                builder.addPostParam("name","zhuxuesong");
                builder.addPostParam("version",1);
                //new File(getExternalFilesDir(null)+"/aaa.png")
                File file = new File(getExternalFilesDir(null)+"/aaa.png");
                builder.addBodyParam("testpic",file,"image/png");
                requestService.asyncRequest(builder.build(), new SimpleTarget() {
                    @Override
                    public void onSuccess(String response) {
                        Log.v("zxs","请求结果"+response);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
            }
        });
    }
}
