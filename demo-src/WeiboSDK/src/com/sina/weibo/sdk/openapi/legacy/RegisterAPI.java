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

package com.sina.weibo.sdk.openapi.legacy;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * 该类封装了微博的注册接口。
 * 详情请参考<a href="http://t.cn/8F1nSzB">注册接口</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class RegisterAPI extends AbsOpenAPI {
    public RegisterAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/register";

    /**
     * 验证昵称是否可用。
     * 
     * @param nickname  需要验证的昵称。4-20个字符，支持中英文、数字、"_"或减号
     * @param listener  异步请求回调接口
     */
    public void suggestions(String nickname, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("nickname", nickname);
        requestAsync(SERVER_URL_PRIX + "/verify_nickname.json", params, HTTPMETHOD_GET, listener);
    }

}
