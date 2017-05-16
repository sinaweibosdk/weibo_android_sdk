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
 * 地理位置纠正信息.
 * 
 * @author SINA
 * @since 2013-12-4
 */
public class OffsetGeo {

    public ArrayList<Coordinate> Geos;

    public static OffsetGeo parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        OffsetGeo offsetGeo = new OffsetGeo();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            JSONArray jsonArray = jsonObject.optJSONArray("geos");
            if (jsonArray != null && jsonArray.length() > 0) {
                int length = jsonArray.length();
                offsetGeo.Geos = new ArrayList<Coordinate>(length);
                for (int ix = 0; ix < length; ix++) {
                    offsetGeo.Geos.add(Coordinate.parse(jsonArray.optJSONObject(ix)));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return offsetGeo;
    }
}

