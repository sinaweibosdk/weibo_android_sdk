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

/**
 * 该类用于创建分享接口 {@link IWeiboShareAPI} 实例。
 * 
 * @author SINA
 * @since 2013-10-22
 */
public class WeiboShareSDK {

    /**
     * 创建微博接口实例。
     * 
     * @param context         应用程序上下文环境
     * @param appKey          第三方应用的 APP_KEY
     * @param isDownloadWeibo 如果当前系统没有安装微博，是否提示用户下载微博（true：下载）
     * 
     * @return 微博SDK对外接口
     */
    public static IWeiboShareAPI createWeiboAPI(Context context, String appKey, boolean isDownloadWeibo) {
        return new WeiboShareAPIImpl(context, appKey, false);
    }

    /**
     * 创建微博接口实例。如果当前系统没有安装微博，则会在注册 {@link IWeiboShareAPI#registerApp}
     * 和分享 {@link IWeiboShareAPI#sendRequest} 时，提示用户下载微博。
     * 
     * @param context 应用程序上下文环境
     * @param appKey  第三方应用的 APP_KEY
     * 
     * @return 微博接口实例
     */
    public static IWeiboShareAPI createWeiboAPI(Context context, String appKey) {
        return createWeiboAPI(context, appKey, false);
    }
}
