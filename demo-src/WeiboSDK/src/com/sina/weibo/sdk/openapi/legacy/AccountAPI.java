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
import android.text.TextUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * 此类封装了账号的接口，详情见<a href="http://t.cn/8F1Egjs">账号接口</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class AccountAPI extends AbsOpenAPI {

    /** 学校类型，1：大学、2：高中、3：中专技校、4：初中、5：小学，默认为1。 */
    public static final int SCHOOL_TYPE_COLLEGE     = 1;
    public static final int SCHOOL_TYPE_SENIOR      = 2;
    public static final int SCHOOL_TYPE_TECHNICAL   = 3;
    public static final int SCHOOL_TYPE_JUNIOR      = 4;
    public static final int SCHOOL_TYPE_PRIMARY     = 5;

    /** 学校首字母，默认为A。 */
    public enum CAPITAL {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/account";

    public AccountAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    /**
     * 获取当前登录用户的隐私设置。
     * 
     * @param listener 异步请求回调接口
     */
    public void getPrivacy(RequestListener listener) {
        requestAsync(SERVER_URL_PRIX + "/get_privacy.json", new WeiboParameters(mAppKey), HTTPMETHOD_GET, listener);
    }

    /**
     * 获取所有的学校列表。
     * NOTE：参数keyword与capital二者必选其一，且只能选其一 按首字母capital查询时，必须提供province参数
     * 
     * @param province   省份范围，省份ID
     * @param city       城市范围，城市ID
     * @param area       区域范围，区ID
     * @param schoolType 学校类型，可为以下几种： 1：大学，2：高中，3：中专技校，4：初中，5：小学
     *                   <li> {@link #SCHOOL_TYPE_COLLEGE}
     *                   <li> {@link #SCHOOL_TYPE_SENIOR}
     *                   <li> {@link #SCHOOL_TYPE_TECHNICAL}
     *                   <li> {@link #SCHOOL_TYPE_JUNIOR}
     *                   <li> {@link #SCHOOL_TYPE_PRIMARY}
     * @param capital    学校首字母，默认为A
     * @param keyword    学校名称关键字
     * @param count      返回的记录条数，默认为10
     * @param listener   异步请求回调接口
     */
    public void schoolList(int province, int city, int area, int schoolType, CAPITAL capital, String keyword,
            int count, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("province", province);
        params.put("city", city);
        params.put("area", area);
        params.put("type", schoolType);
        if (!TextUtils.isEmpty(capital.name())) {
            params.put("capital", capital.name());
        } else if (!TextUtils.isEmpty(keyword)) {
            params.put("keyword", keyword);
        }
        params.put("count", count);
        requestAsync(SERVER_URL_PRIX + "/profile/school_list.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取当前登录用户的API访问频率限制情况。
     * 
     * @param listener 异步请求回调接口
     */
    public void rateLimitStatus(RequestListener listener) {
        requestAsync(SERVER_URL_PRIX + "/rate_limit_status.json", new WeiboParameters(mAppKey), HTTPMETHOD_GET, listener);
    }

    /**
     * OAuth授权之后，获取授权用户的UID。
     * 
     * @param listener 异步请求回调接口
     */
    public void getUid(RequestListener listener) {
        requestAsync(SERVER_URL_PRIX + "/get_uid.json", new WeiboParameters(mAppKey), HTTPMETHOD_GET, listener);
    }

    /**
     * 退出登录。
     * 
     * @param listener 异步请求回调接口
     */
    public void endSession(RequestListener listener) {
        requestAsync(SERVER_URL_PRIX + "/end_session.json", new WeiboParameters(mAppKey), HTTPMETHOD_POST, listener);
    }
}
