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
import android.text.TextUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 微博 OpenAPI 的基类，每个接口类都继承了此抽象类。
 * 
 * @author SINA
 * @since 2013-11-05
 */
public abstract class AbsOpenAPI {
    private static final String TAG = AbsOpenAPI.class.getName();
    
    /** 访问微博服务接口的地址 */
    protected static final String API_SERVER       = "https://api.weibo.com/2";
    /** POST 请求方式 */
    protected static final String HTTPMETHOD_POST  = "POST";
    /** GET 请求方式 */
    protected static final String HTTPMETHOD_GET   = "GET";
    /** HTTP 参数 */
    protected static final String KEY_ACCESS_TOKEN = "access_token";
    
    /** 当前的 Token */
    protected Oauth2AccessToken mAccessToken;
    protected Context mContext;
    protected String mAppKey;
    
    /**
     * 构造函数，使用各个 API 接口提供的服务前必须先获取 Token。
     * 
     * @param accesssToken 访问令牌
     */
    public AbsOpenAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        mContext = context;
        mAppKey = appKey;
        mAccessToken = accessToken;
    }

    /**
     * HTTP 异步请求。
     * 
     * @param url        请求的地址
     * @param params     请求的参数
     * @param httpMethod 请求方法
     * @param listener   请求后的回调接口
     */
    protected void requestAsync(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        if (null == mAccessToken
                || TextUtils.isEmpty(url)
                || null == params
                || TextUtils.isEmpty(httpMethod)
                || null == listener) {
            LogUtil.e(TAG, "Argument error!");
            return;
        }
        
        params.put(KEY_ACCESS_TOKEN, mAccessToken.getToken());
        new AsyncWeiboRunner(mContext).requestAsync(url, params, httpMethod, listener);
    }
    
    /**
     * HTTP 同步请求。
     * 
     * @param url        请求的地址
     * @param params     请求的参数
     * @param httpMethod 请求方法
     * 
     * @return 同步请求后，服务器返回的字符串。
     */
    protected String requestSync(String url, WeiboParameters params, String httpMethod) {
        if (null == mAccessToken
                || TextUtils.isEmpty(url)
                || null == params
                || TextUtils.isEmpty(httpMethod)) {
            LogUtil.e(TAG, "Argument error!");
            return "";
        }
        
        params.put(KEY_ACCESS_TOKEN, mAccessToken.getToken());
        return new AsyncWeiboRunner(mContext).request(url, params, httpMethod);
    }
}
