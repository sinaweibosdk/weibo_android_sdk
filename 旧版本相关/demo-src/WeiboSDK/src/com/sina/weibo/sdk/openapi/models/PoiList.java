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

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 地理列表结构体。
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class PoiList {
    public ArrayList<Poi> pois;
    public String totalNumber;

    public static PoiList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        PoiList poiList = new PoiList();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            poiList.totalNumber = jsonObject.optString("total_number");
            JSONArray jsonArray = jsonObject.optJSONArray("geos");
            if (jsonArray != null && jsonArray.length() > 0) {
                int length = jsonArray.length();
                poiList.pois = new ArrayList<Poi>(length);
                for (int ix = 0; ix < length; ix++) {
                    poiList.pois.add(Poi.parse(jsonArray.optJSONObject(ix)));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return poiList;
    }
}
