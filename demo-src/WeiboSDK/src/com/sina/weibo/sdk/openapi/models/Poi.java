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
 * 位置信息结构体。
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class Poi {

    /** Poi id */
    public String poiid;
    /** 名称 */
    public String title;
    /** 地址 **/
    public String address;
    /** 经度 **/
    public String lon;
    /** 纬度 **/
    public String lat;
    /** 分类 **/
    public String category;
    /** 城市 **/
    public String city;
    /** 省 **/
    public String province;
    /** 国家 **/
    public String country;
    /** 链接 **/
    public String url;
    /** 电话**/
    public String phone;
    /** 邮政编码 **/
    public String postcode;
    /** 微博ID **/
    public String weibo_id;
    /** 分类码 **/
    public String categorys;
    /** 分类名称 **/
    public String category_name;
    /** 图标 **/
    public String icon;
    /** 签到数 **/
    public String checkin_num;
    /** 签到用户数 **/
    public String checkin_user_num;
    /** tip数 **/
    public String tip_num;
    /** 照片数 **/
    public String photo_num;
    /** todo数量 **/
    public String todo_num;
    /** 距离 **/
    public String distance;

    public static Poi parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        Poi poi = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            poi = parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return poi;
    }

    public static Poi parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        
        Poi poi = new Poi();
        poi.poiid               = jsonObject.optString("poiid");
        poi.title               = jsonObject.optString("title");
        poi.address             = jsonObject.optString("address");
        poi.lon                 = jsonObject.optString("lon");
        poi.lat                 = jsonObject.optString("lat");
        poi.category            = jsonObject.optString("category");
        poi.city                = jsonObject.optString("city");
        poi.province            = jsonObject.optString("province");
        poi.country             = jsonObject.optString("country");
        poi.url                 = jsonObject.optString("url");
        poi.phone               = jsonObject.optString("phone");
        poi.postcode            = jsonObject.optString("postcode");
        poi.weibo_id            = jsonObject.optString("weibo_id");
        poi.categorys           = jsonObject.optString("categorys");
        poi.category_name       = jsonObject.optString("category_name");
        poi.icon                = jsonObject.optString("icon");
        poi.checkin_num         = jsonObject.optString("checkin_num");
        poi.checkin_user_num    = jsonObject.optString("checkin_user_num");
        poi.tip_num             = jsonObject.optString("tip_num");
        poi.photo_num           = jsonObject.optString("photo_num");
        poi.todo_num            = jsonObject.optString("todo_num");
        poi.distance            = jsonObject.optString("distance");
        
        return poi;
    }
}
