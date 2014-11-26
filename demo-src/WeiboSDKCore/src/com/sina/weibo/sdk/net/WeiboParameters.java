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

package com.sina.weibo.sdk.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Set;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 在发起网络请求时，用来存放请求参数的容器类。
 * 
 * @author SINA
 * @since 2013-10-12
 */
public class WeiboParameters {
    /** 默认按UTF-8格式进行编码  */
    private static final String DEFAULT_CHARSET = "UTF-8";
    private LinkedHashMap<String, Object> mParams = new LinkedHashMap<String, Object>();
    private String mAppKey;
    
    public WeiboParameters(String appKey) {
        this.mAppKey = appKey;
    }
    
    public String getAppKey() {
        return mAppKey;
    }
    
    public LinkedHashMap<String, Object> getParams() {
        return mParams;
    }

    public void setParams(LinkedHashMap<String, Object> params) {
        this.mParams = params;
    }

    @Deprecated
    public void add(String key, String val) {
        mParams.put(key, val);
    }

    @Deprecated
    public void add(String key, int value) {
        mParams.put(key, String.valueOf(value));
    }

    @Deprecated
    public void add(String key, long value) {
        mParams.put(key, String.valueOf(value));
    }

    @Deprecated
    public void add(String key, Object val) {
        mParams.put(key, val.toString());
    }
    
    public void put(String key, String val) {
        mParams.put(key, val);
    }

    public void put(String key, int value) {
        mParams.put(key, String.valueOf(value));
    }

    public void put(String key, long value) {
        mParams.put(key, String.valueOf(value));
    }
    
    public void put(String key, Bitmap bitmap) {
        mParams.put(key, bitmap);
    }

    public void put(String key, Object val) {
        mParams.put(key, val.toString());
    }

    public Object get(String key) {
        return mParams.get(key);
    }

    public void remove(String key) {
        if (mParams.containsKey(key)) {
            mParams.remove(key);
            mParams.remove(mParams.get(key));
        }
    }

    public Set<String> keySet() {
        return mParams.keySet();
    }
    
    public boolean containsKey(String key) {
        return mParams.containsKey(key);
    }
    
    public boolean containsValue(String value) {
        return mParams.containsValue(value);
    }    

    public int size() {
        return mParams.size();
    }
    
    public String encodeUrl() {
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(String key : mParams.keySet()){
            if (first) {
                first = false;
            } else {
                sb.append("&");
            }
            
            Object value = mParams.get(key);
            if (value instanceof String) {
                String param = (String) value;
                if (!TextUtils.isEmpty(param)) {
                    try {
                        sb.append(URLEncoder.encode(key, DEFAULT_CHARSET) + "="
                                + URLEncoder.encode(param, DEFAULT_CHARSET));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                LogUtil.i("encodeUrl", sb.toString());
            }
        }
        
        return sb.toString();
    }
    
    public boolean hasBinaryData() {
        Set<String> keys = mParams.keySet();
        for (String key : keys) {
            Object value = mParams.get(key);
            if (value instanceof ByteArrayOutputStream 
                    || value instanceof Bitmap) {
                return true;
            }
        }
        
        return false;
    }
}
