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
import android.text.TextUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * 此类封装了分组的接口。
 * 详情见<a href="http://t.cn/8F3geol">评论接口</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class GroupAPI extends AbsOpenAPI {

    /** 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐 */
    public static final int FEATURE_ALL       = 0;
    public static final int FEATURE_ORIGINAL  = 1;
    public static final int FEATURE_PICTURE   = 2;
    public static final int FEATURE_VIDEO     = 3;
    public static final int FEATURE_MUSICE    = 4;

    public GroupAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/friendships/groups";

    /**
     * 获取当前登陆用户好友分组列表。
     * 
     * @param listener  异步请求回调接口
     */
    public void groups(RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        requestAsync(SERVER_URL_PRIX + ".json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取当前登录用户某一好友分组的微博列表。
     * 
     * @param list_id       需要查询的好友分组ID，建议使用返回值里的idstr，当查询的为私有分组时，则当前登录用户必须为其所有者
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param base_app      是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param featureType   过滤类型ID，0：全部，1：原创， 2：图片，3：视频，4：音乐
     *                      {@link #FEATURE_ALL}
     *                      {@link #FEATURE_ORIGINAL}
     *                      {@link #FEATURE_PICTURE}
     *                      {@link #FEATURE_VIDEO}
     *                      {@link #FEATURE_MUSICE}
     * @param listener      异步请求回调接口
     */
    public void timeline(long list_id, long since_id, long max_id, int count, int page, boolean base_app,
            int featureType, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("list_id", list_id);
        params.put("since_id", since_id);
        params.put("max_id", max_id);
        params.put("count", count);
        params.put("page", page);
        params.put("base_app", base_app ? 1 : 0);
        params.put("feature", featureType);
        requestAsync(SERVER_URL_PRIX + "/timeline.json", params, HTTPMETHOD_GET, listener);
    }

    // TODO 获取当前登陆用户某一好友分组的微博ID列表
    public void timelineIds() {
    }

    /**
     * 获取某一好友分组下的成员列表。
     * 
     * @param list_id   获取某一好友分组下的成员列表
     * @param count     单页返回的记录条数，默认为50，最大不超过200
     * @param cursor    分页返回结果的游标，下一页用返回值里的next_cursor，上一页用previous_cursor，默认为0
     * @param listener  异步请求回调接口
     */
    public void members(long list_id, int count, int cursor, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("list_id", list_id);
        params.put("count", count);
        params.put("cursor", cursor);
        requestAsync(SERVER_URL_PRIX + "/members.json", params, HTTPMETHOD_GET, listener);
    }

    // TODO 获取某一好友分组下的成员列表的ID
    public void membersIds() {
    }

    // TODO 批量取好友分组成员的分组说明
    public void memberDescriptionPatch() {
    }

    /**
     * 判断某个用户是否是当前登录用户指定好友分组内的成员。
     * 
     * @param uid       需要判断的用户的UID
     * @param list_id   指定的当前用户的好友分组ID，建议使用返回值里的idstr
     * @param listener  异步请求回调接口
     */
    public void isMember(long uid, long list_id, RequestListener listener) {
        WeiboParameters params = buildeMembersParams(list_id, uid);
        requestAsync(SERVER_URL_PRIX + "/is_member.json", params, HTTPMETHOD_GET, listener);
    }
    // TODO 批量获取某些用户在指定用户的好友分组中的收录信息
    public void listed() {
    };

    /**
     * 获取当前登陆用户某个分组的详细信息。
     * 
     * @param list_id   需要查询的好友分组ID，建议使用返回值里的idstr
     * @param listener  异步请求回调接口
     */
    public void showGroup(long list_id, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("list_id", list_id);
        requestAsync(SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 批量获取好友分组的详细信息。
     * 
     * @param list_ids  需要查询的好友分组ID，建议使用返回值里的idstr，多个之间用逗号分隔，每次不超过20个
     * @param uids      参数list_ids所指的好友分组的所有者UID，多个之间用逗号分隔，每次不超过20个，需与list_ids一一对应
     * @param listener  异步请求回调接口
     */
    public void showGroupBatch(String list_ids, long uids, RequestListener listener) {
        WeiboParameters params = buildeMembersParams(Long.valueOf(list_ids), uids);
        requestAsync(SERVER_URL_PRIX + "/show_batch.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 创建好友分组。
     * 
     * @param name          要创建的好友分组的名称，不超过10个汉字，20个半角字符
     * @param description   要创建的好友分组的描述，不超过70个汉字，140个半角字符
     * @param tags          要创建的好友分组的标签，多个之间用逗号分隔，最多不超过10个，每个不超过7个汉字，14个半角字符
     * @param listener      异步请求回调接口
     */
    public void create(String name, String description, String tags, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("name", name);
        if (TextUtils.isEmpty(description) == false) {
            params.put("description", description);
        }
        if (TextUtils.isEmpty(tags) == false) {
            params.put("tags", tags);
        }
        requestAsync(SERVER_URL_PRIX + "/create.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 更新好友分组。
     * 
     * @param list_id       需要更新的好友分组ID，建议使用返回值里的idstr，只能更新当前登录用户自己创建的分组
     * @param name          要创建的好友分组的名称，不超过10个汉字，20个半角字符
     * @param description   要创建的好友分组的描述，不超过70个汉字，140个半角字符
     * @param tags          要创建的好友分组的标签，多个之间用逗号分隔，最多不超过10个，每个不超过7个汉字，14个半角字符
     * @param listener      异步请求回调接口
     */
    public void update(long list_id, String name, String description, String tags, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("list_id", list_id);
        if (TextUtils.isEmpty(name) == false) {
            params.put("name", name);
        }
        if (TextUtils.isEmpty(description) == false) {
            params.put("description", description);
        }
        if (TextUtils.isEmpty(tags) == false) {
            params.put("tags", tags);
        }
        requestAsync(SERVER_URL_PRIX + "/update.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 删除好友分组。
     * 
     * @param list_id   要删除的好友分组ID，建议使用返回值里的idstr
     * @param listener  异步请求回调接口
     */
    public void deleteGroup(long list_id, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("list_id", list_id);
        requestAsync(SERVER_URL_PRIX + "/destroy.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 添加关注用户到好友分组。
     * 
     * @param list_id   好友分组ID，建议使用返回值里的idstr
     * @param uid       好友分组ID，建议使用返回值里的idstr
     * @param listener  异步请求回调接口
     */
    public void addMember(long list_id, long uid, RequestListener listener) {
        WeiboParameters params = buildeMembersParams(list_id, uid);
        requestAsync(SERVER_URL_PRIX + "/members/add.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 批量添加用户到好友分组。
     * 
     * @param list_id       好友分组ID，建议使用返回值里的idstr
     * @param uids          需要添加的用户的UID，多个之间用逗号分隔，最多不超过30个
     * @param group_descriptions 添加成员的分组说明，每个说明最多8个汉字，16个半角字符，多个需先URLencode，然后再用半角逗号分隔，最多不超过30个，且需与uids参数一一对应
     * @param listener      异步请求回调接口
     */
    public void addMemberBatch(long list_id, String uids, String group_descriptions, RequestListener listener) {
        WeiboParameters params = buildeMembersParams(list_id, Long.valueOf(uids));
        params.put("group_descriptions", group_descriptions);
        requestAsync(SERVER_URL_PRIX + "/members/add_batch.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 更新好友分组中成员的分组说明。
     * 
     * @param list_id       好友分组ID，建议使用返回值里的idstr
     * @param uid           需要更新分组成员说明的用户的UID
     * @param group_description 需要更新的分组成员说明，每个说明最多8个汉字，16个半角字符，需要URLencode
     * @param listener      异步请求回调接口
     */
    public void updateMembers(long list_id, long uid, String group_description, RequestListener listener) {
        WeiboParameters params = buildeMembersParams(list_id, uid);
        if (TextUtils.isDigitsOnly(group_description) == false) {
            params.put("group_description", group_description);
        }
        requestAsync(SERVER_URL_PRIX + "/members/update.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 删除好友分组内的关注用户。
     * 
     * @param list_id   好友分组ID，建议使用返回值里的idstr
     * @param uid       需要删除的用户的UID
     * @param listener  异步请求回调接口
     */
    public void deleteMembers(long list_id, long uid, RequestListener listener) {
        WeiboParameters params = buildeMembersParams(list_id, uid);
        requestAsync(SERVER_URL_PRIX + "/members/destroy.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 调整当前登录用户的好友分组顺序。
     * 
     * @param list_ids  调整好顺序后的分组ID列表，以逗号分隔，例：57,38，表示57排第一、38排第二，以此类推
     * @param count     好友分组数量，必须与用户所有的分组数一致、与分组ID的list_id个数一致
     * @param listener  异步请求回调接口
     */
    public void order(String list_ids, int count, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("list_id", list_ids);
        params.put("count", count);
        requestAsync(SERVER_URL_PRIX + "/order.json", params, HTTPMETHOD_POST, listener);
    }
    
    private WeiboParameters buildeMembersParams(long list_id, long uid){
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("list_id", list_id);
        params.put("uid", uid);
        return params;
    }
}
