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
 * 微博授权认证常量定义类。
 * <p><b>注意：当前该类未被使用，为预留类。</b></p>
 * 
 * @author SINA
 * @since 2013-09-27
 */
public class WBAuthErrorCode {
    /** 重定向地址不匹配 */
    public static final int redirect_uri_mismatch       = 21322;
    /** 请求不合法 */
    public static final int invalid_request             = 21323;
    /** client_id 或 client_secret 参数无效 */
    public static final int invalid_client              = 21324;
    /** 提供的 Access Grant 是无效的、过期的或已撤销的 */
    public static final int invalid_grant               = 21325;
    /** 客户端没有权限 */
    public static final int unauthorized_client         = 21326;
    /** token 过期 */
    public static final int expired_token               = 21327;
    /** 不支持的 GrantType */
    public static final int unsupported_grant_type      = 21328;
    /** 不支持的 ResponseType */
    public static final int unsupported_response_type   = 21329;
    /** 用户或授权服务器拒绝授予数据访问权限 */
    public static final int access_denied               = 21330;
    /** 服务暂时无法访问 */
    public static final int temporarily_unavailable     = 21331;
    /** 应用权限不足 */
    public static final int appkey_permission_denied    = 21337;
}
