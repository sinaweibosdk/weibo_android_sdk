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
 * 好友分组列表。
 * 
 * @author SINA
 * @since 2013-11-27
 */
public class GroupList {
    /** 分组列表 **/
    public ArrayList<Group> groupList;
    /** 分组数目 **/
    public int total_number;

    public static GroupList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        GroupList groupList = new GroupList();
        try {
            JSONObject jsonObject  = new JSONObject(jsonString);
            groupList.total_number = jsonObject.optInt("total_number");

            JSONArray jsonArray = jsonObject.optJSONArray("lists");
            if (jsonArray != null && jsonArray.length() > 0) {
                int length = jsonArray.length();
                groupList.groupList = new ArrayList<Group>(length);
                for (int ix = 0; ix < length; ix++) {
                    groupList.groupList.add(Group.parse(jsonArray.optJSONObject(ix)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return groupList;
    }
}
