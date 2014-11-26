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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 消息中包含的多媒体数据对象基类。
 * 
 * @author SINA
 * @since 2013-10-22
 */
public abstract class BaseMediaObject implements Parcelable {

    /**
     * 定义媒体数据类型，分为：文本、图片、音乐、视频、网页、语音以及命令类型。
     */
    public static final int MEDIA_TYPE_TEXT    = 1;
    public static final int MEDIA_TYPE_IMAGE   = 2;
    public static final int MEDIA_TYPE_MUSIC   = 3;
    public static final int MEDIA_TYPE_VIDEO   = 4;
    public static final int MEDIA_TYPE_WEBPAGE = 5;
    public static final int MEDIA_TYPE_VOICE   = 6;
    public static final int MEDIA_TYPE_CMD     = 7;

    /** 点击跳转 URL。注意：长度不得超过 512Bytes */
    public String actionUrl;
    
    /** 呼起第三方特定页面 */
    public String schema;
    
    /** 唯一标识一个媒体分享，不能重复。注意：长度不得超过 512Bytes */
    public String identify;
    
    /** 标题。注意：长度不得超过 512Bytes */
    public String title;
    
    /** 描述。注意：长度不得超过 1kb */
    public String description;
    
    /** 缩略图。注意：大小不得超过 32kb */
    public byte[] thumbData;

    /**
     * 构造函数。
     */
    public BaseMediaObject() {
    }

    /**
     * 构造函数。
     * 
     * @param in {@link Parcel} 对象
     */
    public BaseMediaObject(Parcel in) {
        actionUrl   = in.readString();
        schema      = in.readString();
        identify    = in.readString();
        title       = in.readString();
        description = in.readString();
        thumbData   = in.createByteArray();
    }

    /**
     * 设置缩略图。
     * <b>注意：最终压缩过的缩略图大小不得超过 32kb。</b>
     * 
     * @param bitmap 位图对象
     */
    public final void setThumbImage(Bitmap bitmap) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
            this.thumbData = os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("Weibo.BaseMediaObject", "put thumb failed");
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @see Parcelable#describeContents
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @see Parcelable#writeToParcel
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(actionUrl);
        dest.writeString(schema);
        dest.writeString(identify);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeByteArray(thumbData);
    }

    /**
     * 获取对象类型
     */
    public abstract int getObjType();
    
    /**
     * 检查数据是否正确。
     * @see {@link #actionUrl}
     * @see {@link #schema}
     * @see {@link #identify}
     * @see {@link #title}
     * @see {@link #description}
     * @see {@link #thumbData}
     * 
     * @return 数据合法，返回 true；否则，返回 false
     */
    protected boolean checkArgs() {
        if (this.actionUrl == null || this.actionUrl.length() > 512) {
            LogUtil.e("Weibo.BaseMediaObject", "checkArgs fail, actionUrl is invalid");
            return false;
        }
        if ((this.identify == null) || (this.identify.length() > 512)) {
            LogUtil.e("Weibo.BaseMediaObject", "checkArgs fail, identify is invalid");
            return false;
        }
        if ((this.thumbData == null) || (this.thumbData.length > 32768)) {
            LogUtil.e("Weibo.BaseMediaObject", "checkArgs fail, thumbData is invalid,size is "
                    + ((this.thumbData != null) ? this.thumbData.length : -1) + "! more then 32768.");
            return false;
        }
        if ((this.title == null) || (this.title.length() > 512)) {
            LogUtil.e("Weibo.BaseMediaObject", "checkArgs fail, title is invalid");
            return false;
        }
        if ((this.description == null) || (this.description.length() > 1024)) {
            LogUtil.e("Weibo.BaseMediaObject", "checkArgs fail, description is invalid");
            return false;
        }
        return true;
    }

    protected abstract BaseMediaObject toExtraMediaObject(String str);
    protected abstract String toExtraMediaString();
}
