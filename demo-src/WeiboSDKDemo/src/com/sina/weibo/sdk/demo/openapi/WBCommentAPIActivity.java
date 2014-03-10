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

package com.sina.weibo.sdk.demo.openapi;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.demo.AccessTokenKeeper;
import com.sina.weibo.sdk.demo.R;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 该类主要演示了如何使用微博 OpenAPI 来获取以下内容：
 * <li>获取某条微博的评论列表
 * <li>...
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class WBCommentAPIActivity extends Activity implements OnItemClickListener {
    private static final String TAG = "WBCommentAPIActivity";

    /** UI 元素：ListView */
    private ListView mFuncListView;
    /** 功能列表 */
    private String[] mFuncList;
    /** 当前 Token 信息 */
    private Oauth2AccessToken mAccessToken;
    /** 微博评论接口 */
    private CommentsAPI mCommentsAPI;
    
    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_api_base_layout);
        
        // 获取功能列表
        mFuncList = getResources().getStringArray(R.array.comment_func_list);
        // 初始化功能列表 ListView
        mFuncListView = (ListView)findViewById(R.id.api_func_list);
        mFuncListView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, mFuncList));
        mFuncListView.setOnItemClickListener(this);
        
        // 获取当前已保存过的 Token
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 获取微博评论信息接口
        mCommentsAPI = new CommentsAPI(mAccessToken);
    }
    
    /**
     * @see {@link AdapterView.OnItemClickListener#onItemClick}
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view instanceof TextView) {
            if (mAccessToken != null && mAccessToken.isSessionValid()) {
                switch (position) {
                case 0:
                	mCommentsAPI.toME(0L, 0L, 10, 1, CommentsAPI.AUTHOR_FILTER_ALL, CommentsAPI.SRC_FILTER_ALL, mListener);
                    break;

                case 1:
                    mCommentsAPI.byME(0L, 0L, 10, 1, CommentsAPI.SRC_FILTER_ALL, mListener);
                    break;
                    
                case 2:
                    mCommentsAPI.timeline(0L, 0L, 10, 1, true, mListener);
                    break;
                    
                default:
                    break;
                }
            } else {
                Toast.makeText(WBCommentAPIActivity.this, 
                        R.string.weibosdk_demo_access_token_is_empty, 
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            LogUtil.i(TAG, response);
            if (!TextUtils.isEmpty(response)) {
                CommentList comments = CommentList.parse(response);
                if(comments != null && comments.total_number > 0){
                    Toast.makeText(WBCommentAPIActivity.this,
                            "获取评论成功, 条数: " + comments.commentList.size(), 
                            Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            LogUtil.e(TAG, e.getMessage());
        }
    }; 
}
