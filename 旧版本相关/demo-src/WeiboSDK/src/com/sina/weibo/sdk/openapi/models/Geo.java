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

package com.sina.weibo.sdk.openapi.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 地理信息结构体。
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class Geo {
    
    /** 经度坐标 */
    public String longitude;
    /** 维度坐标 */
    public String latitude;
    /** 所在城市的城市代码 */
    public String city;
    /** 所在省份的省份代码 */
    public String province;
    /** 所在城市的城市名称 */
    public String city_name;
    /** 所在省份的省份名称 */
    public String province_name;
    /** 所在的实际地址，可以为空 */
    public String address;
    /** 地址的汉语拼音，不是所有情况都会返回该字段 */
    public String pinyin;
    /** 更多信息，不是所有情况都会返回该字段 */
    public String more;
    
    public static Geo parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        Geo geo = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            geo = parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return geo;
    }

    public static Geo parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        
        Geo geo = new Geo();
        geo.longitude       = jsonObject.optString("longitude");
        geo.latitude        = jsonObject.optString("latitude");
        geo.city            = jsonObject.optString("city");
        geo.province        = jsonObject.optString("province");
        geo.city_name       = jsonObject.optString("city_name");
        geo.province_name   = jsonObject.optString("province_name");
        geo.address         = jsonObject.optString("address");
        geo.pinyin          = jsonObject.optString("pinyin");
        geo.more            = jsonObject.optString("more");
        
        return geo;
    }
}
