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
 * 此类封装了关系的接口。
 * 详情见<a href=http://t.cn/8FUByoM">关系接口</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class FriendshipsAPI extends AbsOpenAPI {

    public FriendshipsAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/friendships";

    /**
     * 获取用户的关注列表。
     * 
     * @param uid           需要查询的用户UID
     * @param count         单页返回的记录条数，默认为50，最大不超过200
     * @param cursor        返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param trim_status   返回值中user字段中的status字段开关，false：返回完整status字段、true：status字段仅返回status_id，默认为true
     * @param listener      异步请求回调接口
     */
    public void friends(long uid, int count, int cursor, boolean trim_status, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey); params = buildFriendsParam(count, cursor, trim_status);
        params.put("uid", uid);
        requestAsync(SERVER_URL_PRIX + "/friends.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户的关注列表。
     * 
     * @param screen_name   需要查询的用户昵称
     * @param count         单页返回的记录条数，默认为50，最大不超过200
     * @param cursor        返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param trim_status   返回值中user字段中的status字段开关，false：返回完整status字段、true：status字段仅返回status_id，默认为true
     * @param listener      异步请求回调接口
     */
    public void friends(String screen_name, int count, int cursor, boolean trim_status, RequestListener listener) {
        WeiboParameters params = buildFriendsParam(count, cursor, trim_status);
        params.put("screen_name", screen_name);
        requestAsync(SERVER_URL_PRIX + "/friends.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取两个用户之间的共同关注人列表。
     * 
     * @param uid       需要获取共同关注关系的用户UID
     * @param suid      需要获取共同关注关系的用户UID，默认为当前登录用户
     * @param count     单页返回的记录条数，默认为50
     * @param page      返回结果的页码，默认为1
     * @param trim_status 返回值中user字段中的status字段开关，false：返回完整status字段、true：status字段仅返回status_id，默认为true
     * @param listener  异步请求回调接口
     */
    public void inCommon(long uid, long suid, int count, int page, boolean trim_status, RequestListener listener) {
        WeiboParameters params = buildFriendIDParam(uid, count);
        params.put("suid", suid);
        params.put("page", page);
        params.put("trim_status", trim_status ? 1: 0);
        requestAsync(SERVER_URL_PRIX + "/friends/in_common.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户的双向关注列表，即互粉列表。
     * 
     * @param uid       需要获取双向关注列表的用户UID
     * @param count     单页返回的记录条数，默认为50
     * @param page      返回结果的页码，默认为1
     * @param listener  异步请求回调接口
     */
    public void bilateral(long uid, int count, int page, RequestListener listener) {
        WeiboParameters params = buildFriendIDParam(uid, count);
        params.put("page", page);
        requestAsync(SERVER_URL_PRIX + "/friends/bilateral.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户双向关注的用户ID列表，即互粉UID列表。
     * 
     * @param uid       需要获取双向关注列表的用户UID
     * @param count     单页返回的记录条数，默认为50，最大不超过2000
     * @param page      返回结果的页码，默认为1
     * @param listener  异步请求回调接口
     */
    public void bilateralIds(long uid, int count, int page, RequestListener listener) {
        WeiboParameters params = buildFriendIDParam(uid, count);
        params.put("page", page);
        requestAsync(SERVER_URL_PRIX + "/friends/bilateral/ids.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户关注的用户UID列表。
     * 
     * @param uid       需要查询的用户UID
     * @param count     单页返回的记录条数，默认为500，最大不超过5000
     * @param cursor    返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param listener  异步请求回调接口
     */
    public void friendsIds(long uid, int count, int cursor, RequestListener listener) {
        WeiboParameters params = buildFriendIDParam(uid, count);
        params.put("cursor", cursor);
        requestAsync(SERVER_URL_PRIX + "/friends/ids.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户关注的用户UID列表。
     * 
     * @param screen_name   需要查询的用户昵称
     * @param count         单页返回的记录条数，默认为500，最大不超过5000
     * @param cursor        返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param listener      异步请求回调接口
     */
    public void friendsIds(String screen_name, int count, int cursor, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("screen_name", screen_name);
        params.put("count", count);
        params.put("cursor", cursor);
        requestAsync(SERVER_URL_PRIX + "/friends/ids.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户的粉丝列表(最多返回5000条数据)。
     * 
     * @param uid       需要查询的用户UID
     * @param count     单页返回的记录条数，默认为50，最大不超过200
     * @param cursor    返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param trim_status 返回值中user字段中的status字段开关，false：返回完整status字段、true：status字段仅返回status_id，默认为false
     * @param listener  异步请求回调接口
     */
    public void followers(long uid, int count, int cursor, boolean trim_status, RequestListener listener) {
        WeiboParameters params = buildFriendIDParam(uid, count);
        params.put("cursor", cursor);
        params.put("trim_status", trim_status ? 1: 0);
        requestAsync(SERVER_URL_PRIX + "/followers.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户的粉丝列表(最多返回5000条数据)。
     * 
     * @param screen_name   需要查询的用户昵称
     * @param count         单页返回的记录条数，默认为50，最大不超过200
     * @param cursor        返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param trim_status   返回值中user字段中的status字段开关，false：返回完整status字段、true：status字段仅返回status_id，默认为false
     * @param listener      异步请求回调接口
     */
    public void followers(String screen_name, int count, int cursor, boolean trim_status, RequestListener listener) {
        WeiboParameters params = buildFriendsParam(count, cursor, trim_status);
        params.put("screen_name", screen_name);
        requestAsync(SERVER_URL_PRIX + "/followers.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户粉丝的用户UID列表。
     * 
     * @param uid       需要查询的用户UID
     * @param count     单页返回的记录条数，默认为500，最大不超过5000
     * @param cursor    返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param listener  异步请求回调接口
     */
    public void followersIds(long uid, int count, int cursor, RequestListener listener) {
        WeiboParameters params = buildFriendIDParam(uid, count);
        params.put("cursor", cursor);
        requestAsync(SERVER_URL_PRIX + "/followers/ids.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户粉丝的用户UID列表。
     * 
     * @param screen_name   需要查询的用户昵称
     * @param count         单页返回的记录条数，默认为500，最大不超过5000
     * @param cursor        返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param listener      异步请求回调接口
     */
    public void followersIds(String screen_name, int count, int cursor, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("screen_name", screen_name);
        params.put("count", count);
        params.put("cursor", cursor);
        requestAsync(SERVER_URL_PRIX + "/followers/ids.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户的活跃粉丝列表。
     * 
     * @param uid       需要查询的用户UID
     * @param count     返回的记录条数，默认为20，最大不超过200
     * @param listener  异步请求回调接口
     */
    public void followersActive(long uid, int count, RequestListener listener) {
        WeiboParameters params = buildFriendIDParam(uid, count);
        requestAsync(SERVER_URL_PRIX + "/followers/active.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取当前登录用户的关注人中又关注了指定用户的用户列表。
     * 
     * @param uid       指定的关注目标用户UID
     * @param count     单页返回的记录条数，默认为50
     * @param page      返回结果的页码，默认为1
     * @param listener  异步请求回调接口
     */
    public void chainFollowers(long uid, int count, int page, RequestListener listener) {
        WeiboParameters params = buildFriendIDParam(uid, count);
        params.put("page", page);
        requestAsync(SERVER_URL_PRIX + "/friends_chain/followers.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取两个用户之间的详细关注关系情况。
     * 
     * @param source_id     源用户的UID
     * @param target_id     目标用户的UID
     * @param listener      异步请求回调接口 
     */
    public void show(long source_id, long target_id, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("source_id", source_id);
        params.put("target_id", target_id);
        requestAsync(SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取两个用户之间的详细关注关系情况。
     * 
     * @param source_id             源用户的UID
     * @param target_screen_name    目标用户的微博昵称
     * @param listener              异步请求回调接口
     */
    public void show(long source_id, String target_screen_name, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("source_id", source_id);
        params.put("target_screen_name", target_screen_name);
        requestAsync(SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取两个用户之间的详细关注关系情况。
     * 
     * @param source_screen_name    源用户的微博昵称
     * @param target_id             目标用户的UID
     * @param listener              异步请求回调接口
     */
    public void show(String source_screen_name, long target_id, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("source_screen_name", source_screen_name);
        params.put("target_id", target_id);
        requestAsync(SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取两个用户之间的详细关注关系情况。
     * 
     * @param source_screen_name 源用户的微博昵称
     * @param target_screen_name 目标用户的微博昵称
     * @param listener           异步请求回调接口
     */
    public void show(String source_screen_name, String target_screen_name, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("target_screen_name", target_screen_name);
        params.put("source_screen_name", source_screen_name);
        requestAsync(SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 关注一个用户。
     * 
     * @param uid           需要关注的用户ID
     * @param screen_name   需要关注的用户昵称
     * @param listener      异步请求回调接口
     */
    public void create(long uid, String screen_name, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("uid", uid);
        params.put("screen_name", screen_name);
        requestAsync(SERVER_URL_PRIX + "/create.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 关注一个用户
     * 
     * @param screen_name   需要关注的用户昵称
     * @param listener      异步请求回调接口
     */
    @Deprecated
    public void create(String screen_name, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("screen_name", screen_name);
        requestAsync(SERVER_URL_PRIX + "/create.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 取消关注一个用户。
     * 
     * @param uid           需要取消关注的用户ID
     * @param screen_name   需要取消关注的用户昵称
     * @param listener      异步请求回调接口
     */
    public void destroy(long uid, String screen_name, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("uid", uid);
        params.put("screen_name", screen_name);
        requestAsync(SERVER_URL_PRIX + "/destroy.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 取消关注一个用户。
     * 
     * @param screen_name 需要取消关注的用户昵称
     * @param listener    异步请求回调接口
     */
    @Deprecated
    public void destroy(String screen_name, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("screen_name", screen_name);
        requestAsync(SERVER_URL_PRIX + "/destroy.json", params, HTTPMETHOD_POST, listener);
    }

    /** 组装FriendsParam的参数 */
    private WeiboParameters buildFriendsParam(int count, int cursor, boolean trim_status) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("count", count);
        params.put("cursor", cursor);
        params.put("trim_status", trim_status ? 1: 0);
        return params;
    }

    /** 组装FriendsID的参数 */
    private WeiboParameters buildFriendIDParam(long uid, int count) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("uid", uid);
        params.put("count", count);
        return params;
    }
}
