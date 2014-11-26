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

import android.os.Bundle;

import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 该类定义了微博客户端程序和第三方应用之间传递数据的消息结构。
 * 一个消息结构由三部分组成：文字、图片和多媒体数据（网页、音乐、视频、语音中的一种）。
 * 三部分内容可以同时存在，但是至少有一项不为空。
 * 
 * @author SINA
 * @since 2013-10-22
 */
public final class WeiboMultiMessage {

    private static final String TAG = "WeiboMultiMessage";

    /** 文字内容 */
    public TextObject textObject;
    /** 图片内容 */
    public ImageObject imageObject;
    /** 多媒体内容（网页、音乐、视频、语音中的一种） */
    public BaseMediaObject mediaObject;

    public WeiboMultiMessage() {

    }

    public WeiboMultiMessage(Bundle data) {
        toBundle(data);
    }

    public Bundle toBundle(Bundle data) {
        if (textObject != null) {
            data.putParcelable(WBConstants.Msg.TEXT, textObject);
            data.putString(WBConstants.Msg.TEXT_EXTRA, textObject.toExtraMediaString());
        }
        if (imageObject != null) {
            data.putParcelable(WBConstants.Msg.IMAGE, imageObject);
            data.putString(WBConstants.Msg.IMAGE_EXTRA, imageObject.toExtraMediaString());
        }
        if (mediaObject != null) {
            data.putParcelable(WBConstants.Msg.MEDIA, mediaObject);
            data.putString(WBConstants.Msg.MEDIA_EXTRA, mediaObject.toExtraMediaString());
        }
        return data;
    }

    public WeiboMultiMessage toObject(Bundle data) {
        textObject = data.getParcelable(WBConstants.Msg.TEXT);
        if (textObject != null) {
            textObject.toExtraMediaObject(data.getString(WBConstants.Msg.TEXT_EXTRA));
        }
        imageObject = data.getParcelable(WBConstants.Msg.IMAGE);
        if (imageObject != null) {
            imageObject.toExtraMediaObject(data.getString(WBConstants.Msg.IMAGE_EXTRA));
        }
        mediaObject = data.getParcelable(WBConstants.Msg.MEDIA);
        if (mediaObject != null) {
            mediaObject.toExtraMediaObject(data.getString(WBConstants.Msg.MEDIA_EXTRA));
        }
        return this;
    }

    public boolean checkArgs() {
        if (textObject != null && !textObject.checkArgs()) {
            LogUtil.e(TAG, "checkArgs fail, textObject is invalid");
            return false;
        }
        if (imageObject != null && !imageObject.checkArgs()) {
            LogUtil.e(TAG, "checkArgs fail, imageObject is invalid");
            return false;
        }
        if (mediaObject != null && !mediaObject.checkArgs()) {
            LogUtil.e(TAG, "checkArgs fail, mediaObject is invalid");
            return false;
        }
        if (textObject == null && imageObject == null && mediaObject == null) {
            LogUtil.e(TAG, "checkArgs fail, textObject and imageObject and mediaObject is null");
            return false;
        }
        return true;
    }
}
