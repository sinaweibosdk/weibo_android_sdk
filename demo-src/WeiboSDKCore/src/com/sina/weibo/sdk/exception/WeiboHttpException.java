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

package com.sina.weibo.sdk.exception;

/**
 * 通过 OpenAPI 进行 HTTP 请求时，如果发生异常，则以该异常进行抛出。
 * 
 * @author SINA
 * @since 2013-10-16
 * TODO: （To be design...）
 */
public class WeiboHttpException extends WeiboException {
    private static final long serialVersionUID = 1L;
    
    /** HTTP请求出错时，服务器返回的错误状态码 */
    private final int mStatusCode;

    /**
     * 构造函数。
     * 
     * @param message    HTTP请求出错时，服务器返回的字符串
     * @param statusCode HTTP请求出错时，服务器返回的错误状态码
     */
    public WeiboHttpException(String message, int statusCode) {
        super(message);
        mStatusCode = statusCode;
    }
    
    /**
     * HTTP请求出错时，服务器返回的错误状态码。
     * 
     * @return 服务器返回的错误状态码
     */
    public int getStatusCode() {
        return mStatusCode;
    }
}
