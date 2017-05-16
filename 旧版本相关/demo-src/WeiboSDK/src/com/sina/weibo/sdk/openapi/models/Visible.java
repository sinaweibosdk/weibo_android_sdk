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

package com.sina.weibo.sdk.openapi.models;

import org.json.JSONObject;

/**
 * 微博可见性结构体。
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class Visible {
    
    public static final int VISIBLE_NORMAL  = 0;
    public static final int VISIBLE_PRIVACY = 1;
    public static final int VISIBLE_GROUPED = 2;
    public static final int VISIBLE_FRIEND  = 3;
    
    /** type 取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博 */
    public int type;
    /** 分组的组号 */
    public int list_id;
    
    public static Visible parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        
        Visible visible = new Visible();
        visible.type    = jsonObject.optInt("type", 0);
        visible.list_id = jsonObject.optInt("list_id", 0);
        
        return visible;
    }
}