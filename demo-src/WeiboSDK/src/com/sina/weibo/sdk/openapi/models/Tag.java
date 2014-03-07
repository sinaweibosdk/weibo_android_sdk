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
 * 我喜欢的微博标签（Tag）结构体。
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class Tag {
    
    /** type 取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博 */
    public int id;
    /** 分组的组号 */
    public String tag;
    
    public static Tag parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        
        Tag tag = new Tag();
        tag.id  = jsonObject.optInt("id", 0);
        tag.tag = jsonObject.optString("tag", "");
        
        return tag;
    }
}