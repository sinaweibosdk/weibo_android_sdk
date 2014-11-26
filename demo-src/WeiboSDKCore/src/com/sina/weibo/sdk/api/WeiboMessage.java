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
 * 注意：消息结构中存放的媒体数据只能是文本、图片、音乐、视频、网页、语音以及命令类型中的一种。
 * 
 * @author SINA
 * @since 2013-10-22
 */
public final class WeiboMessage {
    /** 消息的多媒体内容 */
    public BaseMediaObject mediaObject;

    public WeiboMessage() {
    }

    public WeiboMessage(Bundle data) {
        toBundle(data);
    }

    public Bundle toBundle(Bundle data) {
        if (mediaObject != null) {
            data.putParcelable(WBConstants.Msg.MEDIA, mediaObject);
            data.putString(WBConstants.Msg.MEDIA_EXTRA, mediaObject.toExtraMediaString());
        }
        return data;
    }

    public WeiboMessage toObject(Bundle data) {
        mediaObject = data.getParcelable(WBConstants.Msg.MEDIA);
        if (mediaObject != null) {
            mediaObject.toExtraMediaObject(data.getString(WBConstants.Msg.MEDIA_EXTRA));
        }
        return this;
    }

    public boolean checkArgs() {
        if (mediaObject == null) {
            LogUtil.e("Weibo.WeiboMessage", "checkArgs fail, mediaObject is null");
            return false;
        }
        if (mediaObject != null && !mediaObject.checkArgs()) {
            LogUtil.e("Weibo.WeiboMessage", "checkArgs fail, mediaObject is invalid");
            return false;
        }
        return true;
    }
}
