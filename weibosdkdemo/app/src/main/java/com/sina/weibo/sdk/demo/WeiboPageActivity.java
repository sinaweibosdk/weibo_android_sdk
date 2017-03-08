package com.sina.weibo.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.web.WeiboPageUtils;
import com.sina.weibo.sdk.web.WeiboSdkWebActivity;

/**
 * Created by xuesong6 on 2017/2/28.
 */

public class WeiboPageActivity extends Activity{

    private String uid = "2052202067";
    private String mblogid = "4080071993851792";
    private String articleId = "2309404068917940255425";
    private boolean useWeb = true;
    private AuthInfo authInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_page);
        authInfo = new AuthInfo(this,Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);
        //打开指定的微博个人主页
        findViewById(R.id.userinfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeiboPageUtils.getInstance(WeiboPageActivity.this,authInfo).startUserMainPage(uid,useWeb);
            }
        });

        //打开指定的微博详情
        findViewById(R.id.detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeiboPageUtils.getInstance(WeiboPageActivity.this,authInfo).startWeiboDetailPage(mblogid,uid,useWeb);
            }
        });

        //打开指定的微博头条文章
        findViewById(R.id.article).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeiboPageUtils.getInstance(WeiboPageActivity.this,authInfo).startWeiboTopPage(articleId,useWeb);
            }
        });

        //分享到微博（只能分享文字内容）
        findViewById(R.id.sendweibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeiboPageUtils.getInstance(WeiboPageActivity.this,authInfo).shareToWeibo("哈哈哈哈哈",useWeb);
            }
        });

        //评论指定微博
        findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeiboPageUtils.getInstance(WeiboPageActivity.this,authInfo).commentWeibo("mblogid",useWeb);
            }
        });

        //连接到微博搜索内容流
        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeiboPageUtils.getInstance(WeiboPageActivity.this,authInfo).openWeiboSearchPage("大屁老师",useWeb);
            }
        });

        //连接到我的微博消息流
        findViewById(R.id.gotohome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeiboPageUtils.getInstance(WeiboPageActivity.this,authInfo).gotoMyHomePage(useWeb);
            }
        });

        //连接到我的微博个人主页
        findViewById(R.id.myprofile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeiboPageUtils.getInstance(WeiboPageActivity.this,authInfo).gotoMyProfile(useWeb);
            }
        });
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WeiboPageActivity.this,WeiboSdkWebActivity.class);
                startActivity(intent);
            }
        });
        CheckBox checkBox = (CheckBox)findViewById(R.id.web_switch);
        checkBox.setChecked(useWeb);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                useWeb = b;
            }
        });

    }
}
