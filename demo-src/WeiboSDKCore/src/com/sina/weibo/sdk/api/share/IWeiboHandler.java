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

/**
 * 用于接收请求、响应的接口。
 * 
 * @author SINA
 * @since 2013-10-29
 */
public interface IWeiboHandler {

    public interface Response {
        /**
         * 处理微博客户端分享后的响应数据。
         * 当从当前应用唤起微博发博器并进行分享后，返回到当前应用时，该方法被调用。
         * 
         * @param baseResponse 微博响应数据对象
         * @see {@link IWeiboShareAPI#handleWeiboResponse}
         */
        public void onResponse(BaseResponse baseResponse);
    }
    
    public interface Request {
        /**
         * 接收微客户端博请求的数据。
         * 当微博客户端唤起当前应用并进行分享时，该方法被调用。
         * 
         * @param baseRequest 微博请求数据对象
         * @see {@link IWeiboShareAPI#handleWeiboRequest}
         */
        public void onRequest(BaseRequest baseRequest);
    }
}
