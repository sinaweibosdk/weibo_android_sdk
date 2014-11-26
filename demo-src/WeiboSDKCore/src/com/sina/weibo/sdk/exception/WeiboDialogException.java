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

import android.webkit.WebViewClient;

/**
 * 通过 {@link WeiboDialog} 进行 Web 授权时，如果加载 URL 发生异常，则以该异常进行抛出。
 * 
 * @author SINA
 * @since 2013-10-16
 */
public class WeiboDialogException extends WeiboException {
    private static final long serialVersionUID = 1L;
    
    /** WebView 返回的错误码 */
    private int mErrorCode;
    /** WebView 返回的请求失败的 URL */
    private String mFailingUrl;

    /**
     * 构造函数。
     * 
     * @param message    WebView 返回的错误描述
     * @param errorCode  WebView 返回的错误码
     * @param failingUrl WebView 返回的请求失败的 URL 
     */
    public WeiboDialogException(String message, int errorCode, String failingUrl) {
        super(message);
        mErrorCode = errorCode;
        mFailingUrl = failingUrl;
    }
    
    /**
     * 获取 WebView 返回的错误码。
     * @See {@link WebViewClient#onReceivedError}
     * 
     * @return WebView 返回的错误码
     */
    public int getErrorCode() {
        
        return mErrorCode;
    }
    
    /**
     * 获取请求失败的 URL。
     * 
     * @return 请求失败的 URL
     */
    public String getFailingUrl() {
        return mFailingUrl;
    }
}
