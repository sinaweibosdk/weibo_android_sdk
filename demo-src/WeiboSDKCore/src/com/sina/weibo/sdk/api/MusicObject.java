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

import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 音乐数据对象。
 * 
 * @author SINA
 * @since 2013-10-22
 */
public class MusicObject extends BaseMediaObject {
    /** 扩展的字段 */
    public static final String EXTRA_KEY_DEFAULTTEXT = "extra_key_defaulttext";
    
    /** 一段默认文案，用来分享后在信息流中进行显示 */
    public String defaultText;
    
    /** H5 中间页：音视频信息在移动端跳转地址，页面需满足新浪 UI 设计规范 */
    public String h5Url;
    
    /** 音乐的源地址，低质量，适用于移动端 2/3G 注意：长度不得超过 512Bytes */
    public String dataUrl;
     
    /** 高品质音乐的源地址，高质量，适用于 PC、WIFI 注意：长度不得超过 512Bytes */
    public String dataHdUrl;
    
    /** 媒体时长(单位：秒) */
    public int duration;

    public static final Parcelable.Creator<MusicObject> CREATOR = new Parcelable.Creator<MusicObject>() {
        public MusicObject createFromParcel(Parcel in) {
            return new MusicObject(in);
        }
    
        public MusicObject[] newArray(int size) {
            return new MusicObject[size];
        }
    };

    public MusicObject() {
    }

    public MusicObject(Parcel in) {
        super(in);
        h5Url     = in.readString();
        dataUrl   = in.readString();
        dataHdUrl = in.readString();
        duration  = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(h5Url);
        dest.writeString(dataUrl);
        dest.writeString(dataHdUrl);
        dest.writeInt(duration);
    }

    @Override
    public boolean checkArgs() {
        if (!super.checkArgs()) {
            return false;
        }
        if ((this.dataUrl != null) && (this.dataUrl.length() > 512)) {
            LogUtil.e("Weibo.MusicObject", "checkArgs fail, dataUrl is invalid");
            return false;
        }
        if ((this.dataHdUrl != null) && (this.dataHdUrl.length() > 512)) {
            LogUtil.e("Weibo.MusicObject", "checkArgs fail, dataHdUrl is invalid");
            return false;
        }
        if (duration <= 0) {
            LogUtil.e("Weibo.MusicObject", "checkArgs fail, duration is invalid");
            return false;
        }
        return true;
    }

    @Override
    public int getObjType() {
        return MEDIA_TYPE_MUSIC;
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
