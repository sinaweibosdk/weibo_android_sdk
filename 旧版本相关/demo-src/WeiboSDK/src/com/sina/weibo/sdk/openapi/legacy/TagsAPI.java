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
 
package com.sina.weibo.sdk.openapi.legacy;

import android.content.Context;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * 该类封装了标签接口。
 * 详情请参考<a href="http://t.cn/8F1nHUA">标签接口</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class TagsAPI extends AbsOpenAPI {
    public TagsAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/tags";

    /**
     * 返回指定用户的标签列表。
     * 
     * @param uid       要获取的标签列表所属的用户ID
     * @param count     单页返回的记录条数，默认为20
     * @param page      返回结果的页码，默认为1
     * @param listener  异步请求回调接口
     */
    public void tags(long uid, int count, int page, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("uid", uid);
        params.put("count", count);
        params.put("page", page);
        requestAsync(SERVER_URL_PRIX + ".json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 批量获取用户的标签列表。
     * 
     * @param uids      要获取标签的用户ID。最大20
     * @param listener  异步请求回调接口
     */
    public void tagsBatch(String[] uids, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        StringBuilder strb = new StringBuilder();
        for (String uid : uids) {
            strb.append(uid).append(",");
        }
        strb.deleteCharAt(strb.length() - 1);
        params.put("uids", strb.toString());
        requestAsync(SERVER_URL_PRIX + "/tags_batch.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取系统推荐的标签列表。
     * 
     * @param count     返回记录数，默认10，最大10
     * @param listener  异步请求回调接口
     */
    public void suggestions(int count, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("count", count);
        requestAsync(SERVER_URL_PRIX + "/suggestions.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 为当前登录用户添加新的用户标签(无论调用该接口次数多少，每个用户最多可以创建10个标签)。
     * 
     * @param tags      要创建的一组标签，每个标签的长度不可超过7个汉字，14个半角字符
     * @param listener  异步请求回调接口
     */
    public void create(String[] tags, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        StringBuilder strb = new StringBuilder();
        for (String tag : tags) {
            strb.append(tag).append(",");
        }
        strb.deleteCharAt(strb.length() - 1);
        params.put("tags", strb.toString());
        requestAsync(SERVER_URL_PRIX + "/create.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 删除一个用户标签。
     * 
     * @param tag_id    要删除的标签ID
     * @param listener  异步请求回调接口
     */
    public void destroy(long tag_id, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("tag_id", tag_id);
        requestAsync(SERVER_URL_PRIX + "/destroy.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 批量删除一组标签。
     * 
     * @param ids       要删除的一组标签ID，一次最多提交10个ID
     * @param listener  异步请求回调接口
     */
    public void destroyBatch(String[] ids, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        StringBuilder strb = new StringBuilder();
        for (String id : ids) {
            strb.append(id).append(",");
        }
        strb.deleteCharAt(strb.length() - 1);
        params.put("ids", strb.toString());
        requestAsync(SERVER_URL_PRIX + "/destroy_batch.json", params, HTTPMETHOD_POST, listener);
    }
}
