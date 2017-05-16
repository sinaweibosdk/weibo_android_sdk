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

package com.sina.weibo.sdk.openapi;

import android.content.Context;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * 该类提供了授权回收接口，帮助开发者主动取消用户的授权。
 * 详情请参考<a href="http://t.cn/zYeuB0k">授权回收</a>
 * 
 * @author SINA
 * @since 2013-11-05
 */
public class LogoutAPI extends AbsOpenAPI {
    /** 注销地址（URL） */
    private static final String REVOKE_OAUTH_URL = "https://api.weibo.com/oauth2/revokeoauth2";
    
    /**
     * 构造函数。
     * 
     * @param oauth2AccessToken Token 实例
     */
    public LogoutAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    /**
     * 异步取消用户的授权。
     * 
     * @param listener 异步请求回调接口
     */
    public void logout(RequestListener listener) {
        requestAsync(REVOKE_OAUTH_URL, new WeiboParameters(mAppKey), HTTPMETHOD_POST, listener);
    }
}
