package com.weibo.sdk.android.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class RegisActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_more);
        this.findViewById(R.id.oauth).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisActivity.this, MainActivity.class));
            }
        });

        this.findViewById(R.id.shareTo).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisActivity.this, MainShareActivity.class));
            }
        });
        
        this.findViewById(R.id.login).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisActivity.this, DemoLoginButton.class));
            }
        });
    }
}
