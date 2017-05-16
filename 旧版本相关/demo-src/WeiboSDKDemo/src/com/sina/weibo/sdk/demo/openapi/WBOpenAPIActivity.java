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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.demo.AccessTokenKeeper;
import com.sina.weibo.sdk.demo.Constants;
import com.sina.weibo.sdk.demo.R;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.openapi.RefreshTokenApi;

/**
 * 该类是所有 OpenAPI Demo 的入口 Activity。
 * 
 * @author SINA
 * @since 2013-11-05
 */
public class WBOpenAPIActivity extends Activity implements OnItemClickListener {
    /** OpenAPI DEMO 的包名（请区分应用程序包名）*/
    private static final String DEST_ACTIVITY_PACKAGE_NAME = "com.sina.weibo.sdk.demo.openapi";
    /** 该 MAP 用于存放 OpenAPI 名称以及对应的 DEMO Activity 名 */
    private static final LinkedHashMap<String, String> sAPIList = 
            new LinkedHashMap<String, String>();
    
    /**
     * 初始化用于存放 OpenAPI 名称以及对应的 DEMO Activity 名的 MAP。
     */
    static {
        sAPIList.put("授权 - RefreshToken",  "refreshtoken");
        sAPIList.put("用户 - UserAPI",    "WBUserAPIActivity");
        sAPIList.put("微博 - StatusAPI",  "WBStatusAPIActivity");
        sAPIList.put("评论 - CommentAPI", "WBCommentAPIActivity");
        sAPIList.put("邀请 - InviteAPI",  "WBInviteAPIActivity");
        sAPIList.put("注销 - LogoutAPI",  "WBLogoutAPIActivity");
    }
    
    /** UI 元素：ListView */
    private ListView mApiListView;
    
    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_api);
        
        mApiListView = (ListView)findViewById(R.id.api_list);
        mApiListView.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, getAPINameList()));
        mApiListView.setOnItemClickListener(this);
    }
    
    /**
     * @see {@link AdapterView.OnItemClickListener#onItemClick}
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (view instanceof TextView) {
            String className = sAPIList.get(((TextView)view).getText().toString());
            if ("refreshtoken".equals(className)) {
                refreshTokenRequest();
            } else {
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), DEST_ACTIVITY_PACKAGE_NAME + "." + className);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void refreshTokenRequest() {
        Oauth2AccessToken   token  =  AccessTokenKeeper.readAccessToken(WBOpenAPIActivity.this);
        RefreshTokenApi.create(getApplicationContext()).refreshToken(
                Constants.APP_KEY, token.getRefreshToken(), new RequestListener() {
                   
            @Override
            public void onWeiboException( WeiboException arg0 ) {
                Toast.makeText(WBOpenAPIActivity.this, "RefreshToken Result : " + arg0.getMessage(), Toast.LENGTH_LONG).show();
               
            }
            
            @Override
            public void onComplete( String arg0 ) {
                Toast.makeText(WBOpenAPIActivity.this, "RefreshToken Result : " + arg0, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    
    /**
     * 获取 OpenAPI 名称列表。
     * 
     * @return OpenAPI 名称列表
     */
    private ArrayList<String> getAPINameList() {
        ArrayList<String> nameList = new ArrayList<String>();
        Set<String> nameSet = sAPIList.keySet();
        for (String name : nameSet) {
            nameList.add(name);
        }
        
        return nameList;
    }
}
