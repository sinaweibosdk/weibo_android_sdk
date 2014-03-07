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

import org.json.JSONObject;

/**
 * 该类用于解析JSONObject类型数据。
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class Coordinate {
    public Double Longtitude;
    public Double Latitude;

    public static Coordinate parse(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        Coordinate coordinate = new Coordinate();
        coordinate.Longtitude = jsonObject.optDouble("longitude");
        coordinate.Latitude   = jsonObject.optDouble("latitude");

        return coordinate;
    }
}