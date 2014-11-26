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

package com.sina.weibo.sdk.constant;

/**
 * 微博 API 常量定义类。
 * 
 * @author SINA
 * @since 2013-09-27
 */
public class WBConstants {

    /** 微博客户端 TEST Key */
//    public static final String WEIBO_SIGN = "c756f5460ac7745bd562c5ea19457889";
    /** 微博客户端 RELEASE Key */
    public static final String WEIBO_SIGN = "18da2bf10352443a00a5e046d9fca6bd";
    
    
    /** 以下常量用于 Oauth2.0 授权认证（通过 Web 授权认证） */ 
    public static final String AUTH_PARAMS_CLIENT_ID     = "client_id";
    public static final String AUTH_PARAMS_RESPONSE_TYPE = "response_type";
    public static final String AUTH_PARAMS_REDIRECT_URL  = "redirect_uri";
    public static final String AUTH_PARAMS_DISPLAY       = "display";
    public static final String AUTH_PARAMS_SCOPE         = "scope";
    public static final String AUTH_PARAMS_PACKAGE_NAME  = "packagename";
    public static final String AUTH_PARAMS_KEY_HASH      = "key_hash";
    public static final String AUTH_PARAMS_AID      = "aid";
    public static final String AUTH_PARAMS_VERSION      = "version";
    
    public static final String AUTH_PARAMS_CLIENT_SECRET = "client_secret";
    public static final String AUTH_PARAMS_GRANT_TYPE    = "grant_type";
    public static final String AUTH_PARAMS_CODE          = "code";
    public static final String AUTH_ACCESS_TOKEN         = "access_token";
    
    /** 以下常量用于 SSO 授权认证（通过微博客户端认证） */ 
    public static final String SSO_APP_KEY       = "appKey";
    public static final String SSO_REDIRECT_URL  = "redirectUri";
    public static final String SSO_USER_SCOPE    = "scope";
    public static final String SSO_PACKAGE_NAME  = "packagename";
    public static final String SSO_KEY_HASH      = "key_hash";

    /** SDK Version Code **/
    public static final String WEIBO_SDK_VERSION_CODE       = "0030105000";

    public static final String ACTION_WEIBO_REGISTER       = "com.sina.weibo.sdk.Intent.ACTION_WEIBO_REGISTER";
    public static final String ACTION_WEIBO_RESPONSE       = "com.sina.weibo.sdk.Intent.ACTION_WEIBO_RESPONSE";
    public static final String ACTION_WEIBO_SDK_PERMISSION = "com.sina.weibo.permission.WEIBO_SDK_PERMISSION";

    /** 微博接收第三方启动的 Activity action */
    public static final String ACTIVITY_WEIBO           = "com.sina.weibo.sdk.action.ACTION_WEIBO_ACTIVITY";
    /** 第三方接收微博请求 Activity action */
    public static final String ACTIVITY_REQ_SDK         = "com.sina.weibo.sdk.action.ACTION_SDK_REQ_ACTIVITY";
    /** 第三方接收微博响应 Activity action */
    public static final String ACTIVITY_RESP_SDK        = "com.sina.weibo.sdk.action.ACTION_SDK_RESP_ACTIVITY";

    /** 唤起微博支付的 Activity action */
    public static final String ACTIVITY_WEIBO_PAY       = "com.sina.weibo.sdk.action.ACTION_WEIBO_PAY_ACTIVITY";
    public static final String ACTIVITY_WEIBO_PAY_REQ   = "com.sina.weibo.sdk.action.ACTION_SDK_REQ_PAY_ACTIVITY";
    
    public static final int SDK_ACTIVITY_FOR_RESULT_CODE = 765;

    /** 微博下载链接 */
    public static final String WEIBO_DOWNLOAD_URL = "http://app.sina.cn/appdetail.php?appID=84560";

    /** 微博分享 API 响应错误代码 */
    public static interface ErrorCode {
        public static final int ERR_OK     = 0;
        public static final int ERR_CANCEL = 1;
        public static final int ERR_FAIL   = 2;
    }

    /** 公共定义 */
    public static final int COMMAND_TO_WEIBO    = 1;
    public static final int COMMAND_FROM_WEIBO  = 2;
    public static final int COMMAND_SSO         = 3;
    public static final int COMMAND_PAY         = 4;

    public static final String COMMAND_TYPE_KEY = "_weibo_command_type";
    public static final String TRAN             = "_weibo_transaction";
    public static final String SIGN             = "_weibo_sign";
    public static final String AID             = "aid";

    /** 响应数据定义 */
    public static interface Response {
        static final String ERRCODE = "_weibo_resp_errcode";
        static final String ERRMSG  = "_weibo_resp_errstr";
    }

    /**
     * 基础信息定义
     */
    public static interface Base {
        static final String SDK_VER = "_weibo_sdkVersion";  // int
        static final String APP_PKG = "_weibo_appPackage";
        static final String APP_KEY = "_weibo_appKey";
    }

    /** 传输数据信息定义 */
    public static interface Msg {
        static final String TEXT        = "_weibo_message_text";
        static final String IMAGE       = "_weibo_message_image";
        static final String MEDIA       = "_weibo_message_media";

        static final String TEXT_EXTRA  = "_weibo_message_text_extra";
        static final String IMAGE_EXTRA = "_weibo_message_image_extra";
        static final String MEDIA_EXTRA = "_weibo_message_media_extra";

        static final String IDENTIFY    = "_weibo_message_identify";
    }

    public static interface Media {
        static final String SDK_VER     = "_weibo_object_sdkVer";   // int
        static final String TITLE       = "_weibo_object_title";
        static final String DESC        = "_weibo_object_description";
        static final String THUMB_DATA  = "_weibo_object_thumbdata";
        static final String URL         = "_weibo_object_url";
    }

    /** 标识Intent是否来自SDK */
    public static interface SDK {
        static final String FLAG = "_weibo_flag";
    }

    public static final int WEIBO_FLAG_SDK = 538116905;
}
