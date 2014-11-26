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
import java.io.File;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 图像数据对象。
 * 
 * @author SINA
 * @since 2013-10-22
 */
public class ImageObject extends BaseMediaObject {

    private static final int DATA_SIZE = 2 * 1024 * 1024;

    /** 二进制图片。注意：大小不得超过 2MB */
    public byte[] imageData;
    
    /** 图片的路径。注意：长度不得超过 512Bytes */
    public String imagePath;

    public static final Parcelable.Creator<ImageObject> CREATOR = new Parcelable.Creator<ImageObject>() {
        public ImageObject createFromParcel(Parcel in) {
            return new ImageObject(in);
        }
    
        public ImageObject[] newArray(int size) {
            return new ImageObject[size];
        }
    };

    public ImageObject() {
    }

    public ImageObject(Parcel in) {
        imageData = in.createByteArray();
        imagePath = in.readString();
    }

    public final void setImageObject(Bitmap bitmap) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
            this.imageData = os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("Weibo.ImageObject", "put thumb failed");
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray(imageData);
        dest.writeString(imagePath);
    }

    public boolean checkArgs() {
        if (imageData == null && imagePath == null) {
            LogUtil.e("Weibo.ImageObject", "imageData and imagePath are null");
            return false;
        }
        if (imageData != null && imageData.length > DATA_SIZE) {
            LogUtil.e("Weibo.ImageObject", "imageData is too large");
            return false;
        }
        if (imagePath != null && imagePath.length() > 512) {
            LogUtil.e("Weibo.ImageObject", "imagePath is too length");
            return false;
        }
        if (imagePath != null) {
            File file = new File(imagePath);
            try {
                if (!file.exists() || file.length() == 0 || file.length() > 10485760) {
                    LogUtil.e("Weibo.ImageObject",
                            "checkArgs fail, image content is too large or not exists");
                    return false;
                }
            } catch (SecurityException e) {
                LogUtil.e("Weibo.ImageObject",
                        "checkArgs fail, image content is too large or not exists");
                return false;
            }
        }
        return true;
    }

    @Override
    public int getObjType() {
        return MEDIA_TYPE_IMAGE;
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
