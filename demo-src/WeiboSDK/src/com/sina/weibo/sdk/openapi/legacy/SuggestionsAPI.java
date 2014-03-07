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

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * 该类封装了推荐接口。
 * 详情请参考<a href="http://t.cn/8F1nOVu">推荐接口</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class SuggestionsAPI extends AbsOpenAPI {

    /** 推荐分类 */
    public enum USER_CATEGORY {
        DEFAULT, ent, hk_famous, model, cooking, sports, finance, tech, singer, writer, moderator, medium, stockplayer
    }

    /** 微博精选分类 */
    public enum STATUSES_TYPE {
        ENTERTAINMENT, FUNNY, BEAUTY, VIDEO, CONSTELLATION, LOVELY, FASHION, CARS, CATE, MUSIC
    }

    public SuggestionsAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/suggestions";

    /**
     * 返回系统推荐的热门用户列表。
     * 
     * @param category  推荐分类，返回某一类别的推荐用户，默认为default，如果不在以下分类中，返回空列表。
     *                  default：人气关注     ent：影视名星        hk_famous：港台名人   model：模特
     *                  cooking：美食&健康   sports：体育名人  finance：商界名人       tech：IT互联网
     *                  singer：歌手               writer：作家         moderator：主持人       medium：媒体总编 
     *                  stockplayer：炒股高手
     * @param listener  异步请求回调接口
     */
    public void usersHot(USER_CATEGORY category, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("category", category.name());
        requestAsync(SERVER_URL_PRIX + "/users/hot.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户可能感兴趣的人。
     * 
     * @param count     单页返回的记录条数，默认为10
     * @param page      返回结果的页码，默认为1
     * @param listener  异步请求回调接口
     */
    public void mayInterested(int count, int page, RequestListener listener) {
        WeiboParameters params = builderCountPage(count, page);
        requestAsync(SERVER_URL_PRIX + "/users/may_interested.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 根据一段微博正文推荐相关微博用户。
     * 
     * @param content   微博正文内容
     * @param num       返回结果数目，默认为10
     * @param listener  异步请求回调接口
     */
    public void byStatus(String content, int num, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("content", content);
        params.put("num", num);
        requestAsync(SERVER_URL_PRIX + "/users/may_interested.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取微博精选推荐。
     * 
     * @param type      微博精选分类，1：娱乐、2：搞笑、3：美女、4：视频、5：星座、6：各种萌、7：时尚、8：名车、9：美食、10：音乐
     * @param is_pic    是否返回图片精选微博，false：全部、true：图片微博
     * @param count     单页返回的记录条数，默认为20
     * @param page      返回结果的页码，默认为1
     * @param listener  异步请求回调接口
     */
    public void statusesHot(STATUSES_TYPE type, boolean is_pic, int count, int page, RequestListener listener) {
        WeiboParameters params = builderCountPage(count, page);
        params.put("type", type.ordinal() + 1);
        params.put("is_pic", is_pic ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/statuses/hot.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 返回系统推荐的热门收藏。
     * 
     * @param count     每页返回结果数，默认20
     * @param page      返回页码，默认1
     * @param listener  异步请求回调接口
     */
    public void favoritesHot(int count, int page, RequestListener listener) {
        WeiboParameters params = builderCountPage(count, page);
        requestAsync(SERVER_URL_PRIX + "/favorites/hot.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 把某人标识为不感兴趣的人。
     * 
     * @param uid       不感兴趣的用户的UID
     * @param listener  异步请求回调接口
     */
    public void notInterested(long uid, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("uid", uid);
        requestAsync(SERVER_URL_PRIX + "/users/not_interested.json", params, HTTPMETHOD_POST, listener);
    }
    
    private WeiboParameters builderCountPage(int count, int page) {
        WeiboParameters params = new WeiboParameters();
        params.put("count", count);
        params.put("page", page);
        return params;
    }
}
