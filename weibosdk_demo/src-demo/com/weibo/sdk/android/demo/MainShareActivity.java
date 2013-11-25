package com.weibo.sdk.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboDownloadListener;

public class MainShareActivity extends Activity implements OnClickListener {

    // 新浪微博分享的开放接口
    private IWeiboAPI mWeiboAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ininWeiboSDK();
        // Log.setDebug(true);
        ((Button) findViewById(R.id.regBtn)).setOnClickListener(this);
        ((Button) findViewById(R.id.sendImageMsg)).setOnClickListener(this);
        ((Button) findViewById(R.id.newWeibo)).setOnClickListener(this);
    }

    private void ininWeiboSDK() {
        // 初始化SDK
        mWeiboAPI = WeiboSDK.createWeiboAPI(this, ConstantS.APP_KEY);
        mWeiboAPI.registerWeiboDownloadListener(new IWeiboDownloadListener() {
            @Override
            public void onCancel() {
                Toast.makeText(MainShareActivity.this, "取消下载", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void regWeibo() {
        // 注册到新浪微博
        mWeiboAPI.registerApp();
        // Toast.makeText(this, "已注册到微博", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.regBtn:
            // weiboAPI.registerApp();
            regWeibo();
            break;
            
        case R.id.newWeibo:
            Toast.makeText(this, 
                    mWeiboAPI.isWeiboAppSupportAPI() ? "此版本支持SDK!!" : "此版本不支持SDK!!",
                    Toast.LENGTH_LONG).show();
            break;
            
        default:
            Intent intent = new Intent(this, RequestMessageActivity.class);
            intent.putExtra("id", v.getId());
            startActivity(intent);
            break;
        }
    }
}
