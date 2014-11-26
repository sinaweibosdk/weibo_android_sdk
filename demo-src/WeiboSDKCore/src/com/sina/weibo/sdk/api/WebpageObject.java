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

package com.sina.weibo.sdk.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 网页数据对象。
 * 
 * @author SINA
 * @since 2013-10-22
 */
public class WebpageObject extends BaseMediaObject {
    /** 扩展的字段 */
    public static final String EXTRA_KEY_DEFAULTTEXT = "extra_key_defaulttext";
    
    /** 一段默认文案，用来分享后在信息流中进行显示 */
    public String defaultText;

    public static final Parcelable.Creator<WebpageObject> CREATOR = new Parcelable.Creator<WebpageObject>() {
        public WebpageObject createFromParcel(Parcel in) {
            return new WebpageObject(in);
        }

        public WebpageObject[] newArray(int size) {
            return new WebpageObject[size];
        }
    };

    public WebpageObject() {
    }

    public WebpageObject(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public boolean checkArgs() {
        if (!super.checkArgs()) {
            return false;
        }
        return true;
    }

    @Override
    public int getObjType() {
        return MEDIA_TYPE_WEBPAGE;
    }

    @Override
    protected BaseMediaObject toExtraMediaObject(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject json = new JSONObject(str);
                this.defaultText = json.optString(EXTRA_KEY_DEFAULTTEXT);
            } catch (JSONException e) {
            }
        }
        return this;
    }

    @Override
    protected String toExtraMediaString() {
        try {
            JSONObject json = new JSONObject();
            if (!TextUtils.isEmpty(defaultText)) {
                json.put(EXTRA_KEY_DEFAULTTEXT, defaultText);
            }
            return json.toString();
        } catch (JSONException e) {
        }
        return "";
    }
}
