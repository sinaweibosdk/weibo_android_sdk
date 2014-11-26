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

package com.sina.weibo.sdk.auth;

import android.os.Bundle;

import com.sina.weibo.sdk.exception.WeiboAuthException;
import com.sina.weibo.sdk.exception.WeiboDialogException;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 微博授权认证回调接口。
 * 
 * @author SINA
 * @since 2013-10-10
 */
public interface WeiboAuthListener {

    /**
     * 授权认证结束后将调用此方法。
     * 
     * @param values 保存从服务器返回的数据（键值对）。
     *               授权成功时，包含"access_token"、"expires_in"、"refresh_token"等信息；
     *               授权不成功时，包含 "code" 或 values 为 null。
     * @see http://t.cn/aHDHkl
     */
    public void onComplete(Bundle values);

    /**
     * 当认证过程中捕获到 {@link WeiboException} 时调用。
     * 如：
     * <li>Web 授权时，加载 URL 异常，此时抛出 {@link WeiboDialogException}
     * <li>Web 授权时，服务器返回的数据不正确，此时抛出 {@link WeiboAuthException}
     * 
     * @param e WeiboException 微博认证错误异常
     */
    public void onWeiboException(WeiboException e);

    /**
     * Oauth2.0 认证过程中，如果认证窗口被关闭或认证取消时调用。
     */
    public void onCancel();
}
