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

package com.sina.weibo.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.UUID;

import com.sina.weibo.sdk.constant.WBConstants;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * 该类提供了一些用于编码、解码等类型的工具函数。
 * 
 * @author SINA
 * @since 2013-10-16
 */
public class Utility {

    /** 默认按UTF-8格式进行编码 */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 根据URL，解析里边的参数值。
     * 
     * @param url URL地址
     */
    public static Bundle parseUrl(String url) {
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }
    
    public static Bundle parseUri(String uri) {
        try {
            URI u = new URI(uri);
            Bundle b = decodeUrl(u.getQuery());
            return b;
        } catch (Exception e) {
            return new Bundle();
        }
    }

    /**
     * 解板URL地址。
     * 
     * @param s URL地址变成字符串形式
     */
    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                try {
                    params.putString(URLDecoder.decode(v[0], DEFAULT_CHARSET),
                            URLDecoder.decode(v[1], DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return params;
    }

    /**
     * 检测是否是中文环境
     * 
     * @param Context 上下文

     * @return 是否是中文情况，是返回True，否则返回False
     */
    public static boolean isChineseLocale(Context context) {
        try {
            Locale locale = context.getResources().getConfiguration().locale;
            if (Locale.CHINA.equals(locale) 
                    || Locale.CHINESE.equals(locale)
                    || Locale.SIMPLIFIED_CHINESE.equals(locale) 
                    || Locale.TAIWAN.equals(locale)) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

	/**
     * 生成随机的UUid作为资源的唯一标识。
     *            
     * @return String 返回UUID
     */
	public static String generateGUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
	
    /**
     * 利用MD5算法，根据包名获取签名。
     * 
     * @param context  上下文
     * @param pkgName  应用的包名
     *            
     * @return String  应用的签名
     */
    public static String getSign(Context context, String pkgName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName,
                    PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            return null;
        }
        for (int j = 0; j < packageInfo.signatures.length; j++) {
            byte[] str = packageInfo.signatures[j].toByteArray();
            if (str != null) {
                return MD5.hexdigest(str);
            }
        }
        return null;
    }
    
    public static String safeString(String orignal) {
        return TextUtils.isEmpty(orignal) ? "" : orignal;
    }
    
    public static String getAid(Context context, String appKey) {
        AidTask task = AidTask.getInstance(context);
        String  cacheAid = task.loadAidFromCache();
        if (!TextUtils.isEmpty(cacheAid)) {
            return cacheAid;
        } else {
            task.aidTaskInit(appKey);
            return "";
        }
    }
    
    public static String generateUA(Context ctx) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(Build.MANUFACTURER).append("-").append(Build.MODEL);
        buffer.append("_");
        buffer.append(android.os.Build.VERSION.RELEASE);
        buffer.append("_");
        buffer.append("weibosdk");
        buffer.append("_");
        buffer.append(WBConstants.WEIBO_SDK_VERSION_CODE);
        buffer.append("_android");
        return buffer.toString();
    }
}
