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
import android.os.Bundle;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.component.view.AttentionComponentView;
import com.sina.weibo.sdk.component.view.CommentComponentView;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 社会化组件（关注，评论，赞）
 * 
 * @author SINA
 * @since 2014-10-29
 */
public class WBSocialActivity extends Activity {
    
    private Oauth2AccessToken mAccessToken;
    
    private AttentionComponentView mAttentionView;
    private CommentComponentView mCommentView;
    
    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
        
        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        
        mAttentionView = (AttentionComponentView) findViewById(R.id.attentionView);
        mAttentionView.setAttentionParam(AttentionComponentView.RequestParam.createRequestParam(
                Constants.APP_KEY, mAccessToken.getToken(), "2016183205", "", new WeiboAuthListener() {
                    @Override
                    public void onWeiboException( WeiboException arg0 ) {
                    }
                    @Override
                    public void onComplete( Bundle arg0 ) {
                        Toast.makeText(WBSocialActivity.this, "auth acess_token:"+Oauth2AccessToken.parseAccessToken(arg0).getToken(), 
                                0).show();
                    }
                    @Override
                    public void onCancel() {
                    }
                }));
        
        mCommentView = (CommentComponentView) findViewById(R.id.commentView);
        mCommentView.setCommentParam(CommentComponentView.RequestParam.createRequestParam(
                Constants.APP_KEY, mAccessToken.getToken(), 
                "后会无期", "测试评论", CommentComponentView.Category.MOVIE, new WeiboAuthListener() {
                    @Override
                    public void onWeiboException( WeiboException arg0 ) {
                    }
                    @Override
                    public void onComplete( Bundle arg0 ) {
                        Toast.makeText(WBSocialActivity.this, "auth acess_token:"+Oauth2AccessToken.parseAccessToken(arg0).getToken(), 
                                0).show();
                    }
                    @Override
                    public void onCancel() {
                    }
                }));
        
    }

}