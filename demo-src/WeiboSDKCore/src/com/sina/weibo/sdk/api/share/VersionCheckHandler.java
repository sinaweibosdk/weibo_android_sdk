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

package com.sina.weibo.sdk.api.share;

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.ApiUtils;
import com.sina.weibo.sdk.WeiboAppManager;
import com.sina.weibo.sdk.WeiboAppManager.WeiboInfo;
import com.sina.weibo.sdk.api.CmdObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 该类用于版本检查。
 * 
 * @author SINA
 * @since 2013-10-29
 */
public class VersionCheckHandler implements IVersionCheckHandler {

    private static final String TAG = VersionCheckHandler.class.getName();

    @Override
    public boolean checkRequest( Context context, WeiboInfo weiboInfo, WeiboMessage message ) {
        if (weiboInfo == null 
                || !weiboInfo.isLegal()) {
            return false;
        }
        LogUtil.d(TAG, "WeiboMessage WeiboInfo package : " + weiboInfo.getPackageName());
        LogUtil.d(TAG, "WeiboMessage WeiboInfo supportApi : " + weiboInfo.getSupportApi());

        if (weiboInfo.getSupportApi() < ApiUtils.BUILD_INT_VER_2_2) {
            // 微博<BUILD_INT_VER_2_2，不支持VoiceObject
            if (message.mediaObject != null && (message.mediaObject instanceof VoiceObject)) {
                message.mediaObject = null;
            }
        }
        
        if (weiboInfo.getSupportApi() < ApiUtils.BUILD_INT_VER_2_3) {
            // 微博<BUILD_INT_VER_2_3，不支持CmdObject
            if (message.mediaObject != null && (message.mediaObject instanceof CmdObject)) {
                message.mediaObject = null;
            }
        }
        
        return true;
    }

    @Override
    public boolean checkRequest( Context context, WeiboInfo weiboInfo, WeiboMultiMessage message ) {
        if (weiboInfo == null 
                || !weiboInfo.isLegal()) {
            return false;
        }
        LogUtil.d(TAG, "WeiboMultiMessage WeiboInfo package : " + weiboInfo.getPackageName());
        LogUtil.d(TAG, "WeiboMultiMessage WeiboInfo supportApi : " + weiboInfo.getSupportApi());

        if (weiboInfo.getSupportApi() < ApiUtils.BUILD_INT_VER_2_2) {
            // 微博<BUILD_INT_VER_2_2，不支持多条消息分享
            return false;
        }
        
        if (weiboInfo.getSupportApi() < ApiUtils.BUILD_INT_VER_2_3) {
            // 微博<BUILD_INT_VER_2_3，不支持CmdObject
            if (message.mediaObject != null && (message.mediaObject instanceof CmdObject)) {
                message.mediaObject = null;
            }
        }
        
        return true;
    }

    @Override
    public boolean checkResponse( Context context, String weiboPackage, WeiboMessage message ) {
        if (TextUtils.isEmpty(weiboPackage)) {
            return false;
        }
        WeiboInfo weiboInfo = WeiboAppManager.getInstance(context)
                .parseWeiboInfoByAsset(weiboPackage);
        return checkRequest(context, weiboInfo, message);
    }

    @Override
    public boolean checkResponse( Context context, String weiboPackage, WeiboMultiMessage message ) {
        if (TextUtils.isEmpty(weiboPackage)) {
            return false;
        }
        WeiboInfo weiboInfo = WeiboAppManager.getInstance(context)
                .parseWeiboInfoByAsset(weiboPackage);
        return checkRequest(context, weiboInfo, message);
    }
}
