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

import android.os.Bundle;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * 消息传输的基类。
 * 
 * @author SINA
 * @since 2013-10-29
 */
public abstract class Base {
    
    /** 对应该请求的事务 ID，通常由 Request 发起，回复 Response 时应填入对应事务 ID */
    public String transaction;

    /**
     * 将数据序列化到指定的 Bundle 中。
     * 
     * @param bundle 要存放数据的 Bundle 对象
     */
    public abstract void toBundle(Bundle bundle);

    /**
     * 从指定的 Bundle 中反序列化数据。
     * 
     * @param bundle 存放数据的 Bundle 对象
     */
    public abstract void fromBundle(Bundle bundle);
    
    /**
     * 返回当前请求或响应的类型 ID，可以以下类型中的一种：
     * {@link WBConstants#COMMAND_TO_WEIBO}，
     * {@link WBConstants#COMMAND_FROM_WEIBO}，
     * {@link WBConstants#COMMAND_SSO}
     * 
     * @return 当前请请求或响应的类型 ID
     */
    public abstract int getType();

}
