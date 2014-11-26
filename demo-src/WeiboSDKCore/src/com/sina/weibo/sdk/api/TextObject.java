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

import android.os.Parcel;
import android.os.Parcelable;

import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 文本数据对象。
 * 
 * @author SINA
 * @since 2013-10-22
 */
public class TextObject extends BaseMediaObject {

    /** 文本不得超过 1KB */
    public String text;

    public TextObject() {
    }

    public TextObject(Parcel in) {
        text = in.readString();
    }

    public static final Parcelable.Creator<TextObject> CREATOR = new Parcelable.Creator<TextObject>() {
        public TextObject createFromParcel(Parcel in) {
            return new TextObject(in);
        }

        public TextObject[] newArray(int size) {
            return new TextObject[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }

    public boolean checkArgs() {
        if (text == null || text.length() == 0 || text.length() > 1024) {
            LogUtil.e("Weibo.TextObject", "checkArgs fail, text is invalid");
            return false;
        }
        return true;
    }

    @Override
    public int getObjType() {
        return MEDIA_TYPE_TEXT;
    }

    @Override
    protected BaseMediaObject toExtraMediaObject(String str) {
        return this;
    }

    @Override
    protected String toExtraMediaString() {
        return "";
    }
}
