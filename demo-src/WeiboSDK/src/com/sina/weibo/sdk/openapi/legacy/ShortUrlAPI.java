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

import android.content.Context;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * 该类封装了微博的短链接接口。
 * 详情请参考<a href="http://t.cn/8F3g1mY">短链接接口</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class ShortUrlAPI extends AbsOpenAPI {
    public ShortUrlAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/short_url";

    /**
     * 将一个或多个长链接转换成短链接
     * 
     * @param url_long  需要转换的长链接，最多不超过20个
     * @param listener  异步请求回调接口
     */
    public void shorten(String[] url_long, RequestListener listener) {
        WeiboParameters params = buildURLRequest(url_long, "url_long");
        requestAsync(SERVER_URL_PRIX + "/shorten.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 将一个或多个短链接还原成原始的长链接。
     * 
     * @param url_short 需要还原的短链接，最多不超过20个 
     * @param listener  异步请求回调接口
     */
    public void expand(String[] url_short, RequestListener listener) {
        WeiboParameters params = buildURLRequest(url_short, "url_short");
        requestAsync(SERVER_URL_PRIX + "/expand.json", params, HTTPMETHOD_GET, listener);
    }

    public String expandSync(String[] url_short) {
        WeiboParameters params = buildURLRequest(url_short, "url_short");
        return requestSync(SERVER_URL_PRIX + "/expand.json", params, HTTPMETHOD_GET);
    }

    /**
     * 获取短链接的总点击数。
     * 
     * @param url_short     需要取得点击数的短链接，最多不超过20个
     * @param listener      异步请求回调接口
     */
    public void clicks(String[] url_short, RequestListener listener) {
        WeiboParameters params = buildURLRequest(url_short, "url_short");
        requestAsync(SERVER_URL_PRIX + "/clicks.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取一个短链接点击的referer来源和数量。
     * 
     * @param url_short     需要取得点击来源的短链接
     * @param listener      异步请求回调接口
     */
    public void referers(String url_short, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("url_short", url_short);
        requestAsync(SERVER_URL_PRIX + "/referers.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取一个短链接点击的地区来源和数量。
     * 
     * @param url_short     需要取得点击来源的短链接
     * @param listener      异步请求回调接口
     */
    public void locations(String url_short, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("url_short", url_short);
        requestAsync(SERVER_URL_PRIX + "/locations.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取短链接在微博上的微博分享数。
     * 
     * @param url_short     需要取得分享数的短链接，最多不超过20个
     * @param listener      异步请求回调接口
     */
    public void shareCounts(String[] url_short, RequestListener listener) {
        WeiboParameters params = buildURLRequest(url_short, "url_short");
        requestAsync(SERVER_URL_PRIX + "/share/counts.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取包含指定单个短链接的最新微博内容。
     * 
     * @param url_short 需要取得关联微博内容的短链接
     * @param since_id  若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id    若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count     单页返回的记录条数，默认为50，最多不超过200
     * @param page      返回结果的页码，默认为1
     * @param listener  异步请求回调接口
     */
    public void shareStatuses(String url_short, long since_id, long max_id, int count, int page,
            RequestListener listener) {
        WeiboParameters params = buildRequestParams(url_short, since_id, max_id, count, page);
        requestAsync(SERVER_URL_PRIX + "/share/statuses.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取短链接在微博上的微博评论数
     * 
     * @param url_short 需要取得分享数的短链接，最多不超过20个
     * @param listener  异步请求回调接口
     */
    public void commentCounts(String[] url_short, RequestListener listener) {
        WeiboParameters params = buildURLRequest(url_short, "url_short");
        requestAsync(SERVER_URL_PRIX + "/comment/counts.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取包含指定单个短链接的最新微博评论。
     * 
     * @param url_short 需要取得关联微博评论内容的短链接
     * @param since_id  若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0
     * @param max_id    若指定此参数，则返回ID小于或等于max_id的评论，默认为0
     * @param count     单页返回的记录条数，默认为50，最多不超过200
     * @param page      返回结果的页码，默认为1
     * @param listener  异步请求回调接口
     */
    public void comments(String url_short, long since_id, long max_id, int count, int page, RequestListener listener) {
        WeiboParameters params = buildRequestParams(url_short, since_id, max_id, count, page);
        requestAsync(SERVER_URL_PRIX + "/comment/comments.json", params, HTTPMETHOD_GET, listener);
    }

    // 组装URL参数
    private WeiboParameters buildRequestParams(String url_short, long since_id, long max_id, int count, int page) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("url_short", url_short);
        params.put("since_id", since_id);
        params.put("max_id", max_id);
        params.put("count", count);
        params.put("page", page);
        return params;
    }

    // 组装URL参数
    private WeiboParameters buildURLRequest(String[] url, String type) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        if (url != null) {
            int length = url.length;
            for (int i = 0; i < length; i++) {
                params.put(type, url[i]);
            }
        }
        return params;
    }

}
