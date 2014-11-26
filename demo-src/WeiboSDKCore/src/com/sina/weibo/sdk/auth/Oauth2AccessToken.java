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

package com.sina.weibo.sdk.auth;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;

/**
 * 该类封装了 "access_token"，"expires_in"，"refresh_token"，"uid"，并提供了他们的管理功能。
 * 
 * @author SINA
 * @since 2013-10-08
 */
public class Oauth2AccessToken {
    
    /** Bundle 或者 从服务器返回的 JSON 字符串中包含的数据的 Key，用于解析 */
    private static final String KEY_UID           = "uid";
    private static final String KEY_ACCESS_TOKEN  = "access_token";
    private static final String KEY_EXPIRES_IN    = "expires_in";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    
    /** 当前授权用户的 UID */
    private String mUid             = "";
    /** 访问令牌 */
    private String mAccessToken     = "";
    /** 用于刷新 Access Token 的 Refresh Token */
    private String mRefreshToken    = "";
    /** 访问令牌过期时间点 */
    private long mExpiresTime       = 0;    // XXX: 使用 Date ？

    /**
     * Oauth2AccessToken 的构造函数。
     */
    public Oauth2AccessToken() {
    }

    /**
     * 根据服务器返回的 JSON 字符串生成 Oauth2AccessToken 的构造函数，
     * 此方法会将字符串中里的 "access_token"，"expires_in"，"refresh_token" 解析出来。
     * 
     * @param responseText 服务器返回的 JSON 字符串
     */
    @Deprecated
    public Oauth2AccessToken(String responseText) {
        if (responseText != null) {
            if (responseText.indexOf("{") >= 0) {
                try {
                    JSONObject json = new JSONObject(responseText);
                    setUid(json.optString(KEY_UID));
                    setToken(json.optString(KEY_ACCESS_TOKEN));
                    setExpiresIn(json.optString(KEY_EXPIRES_IN));
                    setRefreshToken(json.optString(KEY_REFRESH_TOKEN));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Oauth2AccessToken 的构造函数，根据 accessToken 和 expires in 生成 Oauth2AccessToken 实例。
     * 
     * @param accessToken 访问令牌
     * @param expiresIn   访问令牌的有效期，表示距离超过认证时间还有多少秒
     */
    public Oauth2AccessToken(String accessToken, String expiresIn) {
        mAccessToken = accessToken;
        mExpiresTime = System.currentTimeMillis();
        if (expiresIn != null) {
            mExpiresTime += Long.parseLong(expiresIn) * 1000;
        }
    }

    /**
     * 解析从服务器获取的 JSON 字符串，构造 Oauth2AccessToken 实例。
     * 
     * @param responseJsonText 服务器返回的JSON字符串
     * 
     * @return 解析成功，返回 Oauth2AccessToken 实例；解析失败，返回null。
     */
    public static Oauth2AccessToken parseAccessToken(String responseJsonText) {
        if (!TextUtils.isEmpty(responseJsonText)) {
            if (responseJsonText.indexOf("{") >= 0) {
                try {
                    JSONObject json = new JSONObject(responseJsonText);
                    Oauth2AccessToken token = new Oauth2AccessToken();
                    token.setUid(json.optString(KEY_UID));
                    token.setToken(json.optString(KEY_ACCESS_TOKEN));
                    token.setExpiresIn(json.optString(KEY_EXPIRES_IN));
                    token.setRefreshToken(json.optString(KEY_REFRESH_TOKEN));
                    
                    return token;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return null;
    }
    
    /**
     * 从 Bundle 构造 Oauth2AccessToken 实例。
     * 
     * @param bundle Bundle 实例
     * 
     * @return 解析成功，返回 Oauth2AccessToken 实例；解析失败，返回null。
     */
    public static Oauth2AccessToken parseAccessToken(Bundle bundle) {
        if (null != bundle) {
            Oauth2AccessToken accessToken = new Oauth2AccessToken();
            accessToken.setUid(getString(bundle, KEY_UID, ""));
            accessToken.setToken(getString(bundle, KEY_ACCESS_TOKEN, ""));
            accessToken.setExpiresIn(getString(bundle, KEY_EXPIRES_IN, ""));
            accessToken.setRefreshToken(getString(bundle, KEY_REFRESH_TOKEN, ""));
            
            return accessToken;
        }
        
        return null;
    }

    /**
     * AccessToken是否有效。
     * 
     * @return 如果 accessToken 为空或者 expiresTime 过期，返回 false，否则返回 true
     */
    public boolean isSessionValid() {
        return (!TextUtils.isEmpty(mAccessToken));
        /**
        return (!TextUtils.isEmpty(mAccessToken) 
                && (mExpiresTime != 0)
                && (System.currentTimeMillis() < mExpiresTime));
                **/
    }
    
    /**
     * 生成一个 {@link #Bundle} 对象，里面包含了"access_token"，"expires_in"，"refresh_token"。
     * @return
     */
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_UID, mUid);
        bundle.putString(KEY_ACCESS_TOKEN, mAccessToken);
        bundle.putString(KEY_REFRESH_TOKEN, mRefreshToken);
        bundle.putString(KEY_EXPIRES_IN, Long.toString(mExpiresTime));    // TODO: expires_in 在Bundle中应该使用 LONG？？？
        return bundle;
    }
    
    /**
     * 重写 {@link Object#toString()}。
     */
    @Override
    public String toString() {
        return KEY_UID + ": " + mUid + ", " +
               KEY_ACCESS_TOKEN + ": " + mAccessToken + ", " + 
               KEY_REFRESH_TOKEN + ": " + mRefreshToken + ", " + 
               KEY_EXPIRES_IN + ": " + Long.toString(mExpiresTime);
    }
    
    /**
     * 获取 UID。
     * 
     * @return UID
     */
    public String getUid() {
        return mUid;
    }
    
    /**
     * 设置 UID。
     * 
     * @param uid UID
     */
    public void setUid(String uid) {
        mUid = uid;
    }
    
    /**
     * 获取访问令牌。
     */
    public String getToken() {
        return mAccessToken;
    }

    /**
     * 设置accessToken。
     * 
     * @param mToken
     */
    public void setToken(String mToken) {
        this.mAccessToken = mToken;
    }

    /**
     * 获取刷新访问令牌。
     */
    public String getRefreshToken() {
        return mRefreshToken;
    }

    /**
     * 设置刷新访问令牌。
     * 
     * @param refreshToken 刷新访问令牌
     */
    public void setRefreshToken(String refreshToken) {
        mRefreshToken = refreshToken;
    }

    /**
     * 获取访问令牌过期的时间点。
     * 单位: 毫秒，表示从格林威治时间1970年01月01日00时00分00秒起至现在的总毫秒数。
     */
    public long getExpiresTime() {
        return mExpiresTime;
    }
    
    /**
     * 设置访问令牌过期的时间点。
     * 
     * @param mExpiresTime 访问令牌过期的时间点，
     *                     单位：毫秒，表示从格林威治时间1970年01月01日00时00分00秒起至现在的总毫秒数
     */
    public void setExpiresTime(long mExpiresTime) {
        this.mExpiresTime = mExpiresTime;
    }

    /**
     * 设置过期时间长度值，仅当从服务器获取到数据时使用此方法。
     * 
     * @param expiresIn 访问令牌的有效期，表示距离超过认证时间还有多少秒
     */
    public void setExpiresIn(String expiresIn) {
        if (!TextUtils.isEmpty(expiresIn) && !expiresIn.equals("0")) {
            setExpiresTime(System.currentTimeMillis() + Long.parseLong(expiresIn) * 1000);
        }
    }
    
    /**
     * 获取 {@link Bundle} 中，指定 key 对应的 value 值。
     * <p>
     * 当 API Level >= 12时，才能使用 Bundle 中  getString (String key, String defaultValue)，
     * 因此，我们封装该方法，确保在 key 对应的 value 不存在的情况下，我们能获取到默认值，提高容错性。
     * </p>
     * 
     * @param bundle       {@link Bundle} 对象
     * @param key          {@link Bundle} 对象中的某个 key
     * @param defaultValue 该 key 如果在 {@link Bundle} 对象中找不到，则返回该默认值
     * 
     * @return 返回指定 key 对应的 value 值；如果找不到，则返回默认值
     */
    private static String getString(Bundle bundle, String key, String defaultValue) {
        if (bundle != null) {
            String value = bundle.getString(key);
            return (value != null) ? value : defaultValue;
        }
        
        return defaultValue;
    }
}