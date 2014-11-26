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

import com.sina.weibo.sdk.WeiboAppManager.WeiboInfo;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * 消息请求抽象类。
 * 
 * @author SINA
 * @since 2013-10-29
 */
public abstract class BaseRequest extends Base {

    /** 对应该请求相应的包名 */
    public String packageName;

    /**
     * 将请求数据序列化到指定的 Bundle 中。
     * 
     * @param bundle 要存放请求数据的 Bundle 对象
     */
    @Override
    public void toBundle(Bundle bundle) {
        bundle.putInt(WBConstants.COMMAND_TYPE_KEY, getType());
        bundle.putString(WBConstants.TRAN, this.transaction);
    }

    /**
     * 从指定的 Bundle 中反序列化请求数据。
     * 
     * @param bundle 存放请求数据的 Bundle 对象
     */
    @Override
    public void fromBundle(Bundle bundle) {
        this.transaction = bundle.getString(WBConstants.TRAN);
        this.packageName = bundle.getString(WBConstants.Base.APP_PKG);
    }
    
    /**
     * 检查请求数据是否合法。
     * 
     * @return 合法，返回 true；否则返回 false
     */
    abstract boolean check(Context context, WeiboInfo weiboInfo, VersionCheckHandler handler);
}
