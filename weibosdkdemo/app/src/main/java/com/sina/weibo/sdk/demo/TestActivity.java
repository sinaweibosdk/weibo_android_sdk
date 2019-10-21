package com.sina.weibo.sdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.sina.weibo.sdk.R;

/**
 * Created by xuesong6 on 2018/4/26.
 */

public class TestActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.testlayout);
    }
}
