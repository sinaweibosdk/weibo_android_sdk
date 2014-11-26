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
 * 通过 {@link WeiboDialog} 进行 Web 授权时，如果服务器返回的数据不正确，则以该异常进行抛出。
 * 
 * @author SINA
 * @since 2013-10-16
 */
public class WeiboAuthException extends WeiboException {
    private static final long serialVersionUID = 1L;

    /**
     * 默认的 Exception 对应的错误编号、错误类型、错误描述。
     */
    public static final String DEFAULT_AUTH_ERROR_CODE = "-1";
    public static final String DEFAULT_AUTH_ERROR_DESC = "Unknown Error Description";
    public static final String DEFAULT_AUTH_ERROR_TYPE = "Unknown Error Type";
    
    /**
     * 微博 OAuth2.0 授权认证时，从服务器获取的错误码结构如下图：
     * 
     * ----------------------------------------------------------------------------
     * | 错误类型(error_type)  | 错误编号(error_code) | 错误描述(error_description) |
     * ----------------------------------------------------------------------------
     * | redirect_uri_mismatch | 21322               | 重定向地址不匹配                         |
     * ----------------------------------------------------------------------------
     * 
     * 其它错误定义请详见：
     * http://t.cn/8FkSkwp
     */
    
    /** 服务器返回的错误类型，如上图描述的错误类型(error_type) */
    private final String mErrorType;
    /** 服务器返回的错误编号，如上图描述的错误编号(error_code) */
    private final String mErrorCode;

    /**
     * 构造函数。
     * 
     * @param errorCode        服务器返回的错误编号
     * @param errorType        服务器返回的错误类型
     * @param errorDescription 服务器返回的错误的描述信息
     */
    public WeiboAuthException(String errorCode, String errorType,  String errorDescription) {
        super(errorDescription);
        mErrorType = errorType;
        mErrorCode = errorCode;
    }
    
    /**
     * 服务器返回的错误类型。
     * 
     * @return 服务器返回的错误类型
     */
    public String getErrorType() {
        return mErrorType;
    }
    
    /**
     * 服务器返回的错误编号。
     * 
     * @return 服务器返回的错误编号
     */
    public String getErrorCode() {
        return mErrorCode;
    }
}
