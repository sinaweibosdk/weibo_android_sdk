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
 * 该类封装了话题接口。
 * 详情请参考<a href="http://t.cn/8F1nE9v">话题接口</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class TrendsAPI extends AbsOpenAPI {
    public TrendsAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/trends";

    /**
     * 获取某人的话题列表。
     * 
     * @param uid       需要获取话题的用户的UID
     * @param count     单页返回的记录条数，默认为10
     * @param page      返回结果的页码，默认为1
     * @param listener  异步请求回调接口
     */
    public void trends(long uid, int count, int page, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("uid", uid);
        params.put("count", count);
        params.put("page", page);
        requestAsync(SERVER_URL_PRIX + ".json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 判断当前用户是否关注某话题。
     * 
     * @param trend_name    话题关键字
     * @param listener      异步请求回调接口
     */
    public void isFollow(String trend_name, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("trend_name", trend_name);
        requestAsync(SERVER_URL_PRIX + "/is_follow.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 返回最近一小时内的热门话题。
     * 
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void hourly(boolean base_app, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("base_app", base_app ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/hourly.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 返回最近一天内的热门话题。
     * 
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void daily(boolean base_app, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("base_app", base_app ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/daily.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 返回最近一周内的热门话题。
     * 
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void weekly(boolean base_app, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("base_app", base_app ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/weekly.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 关注某话题。
     * 
     * @param trend_name    要关注的话题关键词
     * @param listener      异步请求回调接口
     */
    public void follow(String trend_name, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("trend_name", trend_name);
        requestAsync(SERVER_URL_PRIX + "/follow.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 取消对某话题的关注。
     * 
     * @param trend_id  要取消关注的话题ID
     * @param listener  异步请求回调接口
     */
    public void destroy(long trend_id, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("trend_id", trend_id);
        requestAsync(SERVER_URL_PRIX + "/destroy.json", params, HTTPMETHOD_POST, listener);
    }
}
