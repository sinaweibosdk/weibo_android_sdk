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
import com.sina.weibo.sdk.openapi.CommentsAPI;

/**
 * 此类封装了公共服务的接口。
 * 详情见<a href="http://t.cn/8Fdg7EX">公共服务接口</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class CommonAPI extends AbsOpenAPI {

    /** 国家的首字母，默认为空，代表全部。 */
    public enum CAPITAL {
        a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z
    }

    /** 语言版本，zh-cn：简体中文、zh-tw：繁体中文、english：英文，默认为zh-cn。 */
    public static final String LANGUAGE_ZH_CN   = "zh-cn";
    public static final String LANGUAGE_ZH_TW   = "zh-tw";
    public static final String LANGUAGE_EN      = "english";

    public CommonAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/common";

    /**
     * 获取城市列表。
     * 
     * @param province  省份代码
     * @param capital   国家的首字母，a-z，可为空代表返回全部，默认为全部。
     *                  <li> {@link CommentsAPI#CAPITAL}
     * @param language  返回的语言版本，zh-cn：简体中文、zh-tw：繁体中文、english：英文，默认为zh-cn
     *                  <li> {@link #LANGUAGE_ZH_CN}
     *                  <li> {@link #LANGUAGE_ZH_TW}
     *                  <li> {@link #LANGUAGE_EN}
     * @param listener  异步请求回调接口
     */
    public void getCity(String province, String capital, String language, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("province", province);
        if (null != capital) {
            params.put("capital", capital);
        }
        params.put("language", language);
        requestAsync(SERVER_URL_PRIX + "/get_city.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取国家列表
     * @param capital   国家的首字母，a-z，可为空代表返回全部，默认为全部。
     * @param language  返回的语言版本，zh-cn：简体中文、zh-tw：繁体中文、english：英文，默认为zh-cn
     *                  <li> {@link #LANGUAGE_ZH_CN}
     *                  <li> {@link #LANGUAGE_ZH_TW}
     *                  <li> {@link #LANGUAGE_EN}
     *@param listener   异步请求回调接口
     */
    public void getCountry(CAPITAL capital, String language, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        if (null != capital) {
            params.put("capital", capital.name().toLowerCase());
        }
        params.put("language", language);
        requestAsync(SERVER_URL_PRIX + "/get_country.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取时区配置表。
     * 
     * @param language  返回的语言版本，zh-cn：简体中文、zh-tw：繁体中文、english：英文，默认为zh-cn
     *                  <li> {@link #LANGUAGE_ZH_CN}
     *                  <li> {@link #LANGUAGE_ZH_TW}
     *                  <li> {@link #LANGUAGE_EN}
     *@param listener   异步请求回调接口
     */
    public void getTimezone(String language, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("language", language);
        requestAsync(SERVER_URL_PRIX + "/get_timezone.json", params, HTTPMETHOD_GET, listener);
    }
}
