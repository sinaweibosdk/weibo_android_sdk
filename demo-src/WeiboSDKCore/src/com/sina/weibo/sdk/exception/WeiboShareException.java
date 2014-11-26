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
 * 进行分享时，如果发生异常，则以该异常进行抛出。
 * 
 * @author SINA
 * @since 2013-10-24
 */
public class WeiboShareException extends WeiboException {
    private static final long serialVersionUID = 1L;

    /**
     * 构造一个包含当前 StackTrace 的异常。
     */
    public WeiboShareException() {
        super();
    }
    
    /**
     * 构造一个包含当前 StackTrace 和异常描述信息的异常。
     * 
     * @param message 异常描述信息
     */
    public WeiboShareException(String message) {
        super(message);
    }

    /**
     * 构造一个包含当前 StackTrace、 异常描述信息、及异常产生的原因的异常。
     * 
     * @param detailMessage 异常描述信息
     * @param throwable     异常产生的原因
     */
    public WeiboShareException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * 构造一个包含当前 StackTrace 和异常产生的原因的异常。
     * 
     * @param throwable 异常产生的原因
     */
    public WeiboShareException(Throwable throwable) {
        super(throwable);
    }
}
