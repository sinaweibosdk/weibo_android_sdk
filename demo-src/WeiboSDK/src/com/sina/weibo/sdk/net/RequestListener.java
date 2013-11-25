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

package com.sina.weibo.sdk.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 发起访问请求时所需的回调接口。
 * TODO：（To be design...）
 * 
 * @author SINA
 * @since 2013-11-05
 */
public interface RequestListener {
    
    /**
     * 当获取服务器返回的字符串后，该函数被调用。
     * 
     * @param response 服务器返回的字符串
     */
    public void onComplete(String response);

    /**
     * 当获取服务器返回的文件流后，该函数被调用。
     * 
     * @param responseOS 服务器返回的文件流
     */
    public void onComplete4binary(ByteArrayOutputStream responseOS);

    /**
     * 当访问服务器时，发生 I/O 异常时，该函数被调用。
     * 
     * @param e I/O 异常对象
     */
    public void onIOException(IOException e);

    /**
     * 当访问服务器时，其它异常时，该函数被调用。
     * 
     * @param e 微博自定义异常对象
     */
    public void onError(WeiboException e);
}
