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
import android.os.Bundle;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * 消息响应抽象类。
 * 
 * @author SINA
 * @since 2013-10-29
 */
public abstract class BaseResponse extends Base {

    /** 错误码, 参考 {@link WBConstants.ErrorCode} */
    public int errCode;
    /** 错误描述 */
    public String errMsg;
    /** 对应的请求传过来的包名 */
    public String reqPackageName;

    /**
     * 将响应数据序列化到指定的 Bundle 中。
     * 
     * @param bundle 要存放响应数据的 Bundle 对象
     */
    public void toBundle(Bundle bundle) {
        bundle.putInt(WBConstants.COMMAND_TYPE_KEY, getType());
        bundle.putInt(WBConstants.Response.ERRCODE, this.errCode);
        bundle.putString(WBConstants.Response.ERRMSG, this.errMsg);
        bundle.putString(WBConstants.TRAN, this.transaction);
    }
    
    /**
     * 从指定的 Bundle 中反序列化响应数据。
     * 
     * @param bundle 存放响应数据的 Bundle 对象
     */
    public void fromBundle(Bundle bundle) {
        this.errCode = bundle.getInt(WBConstants.Response.ERRCODE);
        this.errMsg = bundle.getString(WBConstants.Response.ERRMSG);
        this.transaction = bundle.getString(WBConstants.TRAN);
        this.reqPackageName = bundle.getString(WBConstants.Base.APP_PKG);
    }
    
    /**
     * 检查响应数据是否合法。
     * 
     * @return 合法，返回 true；否则返回 false
     */
    abstract boolean check(Context context, VersionCheckHandler handler);
}