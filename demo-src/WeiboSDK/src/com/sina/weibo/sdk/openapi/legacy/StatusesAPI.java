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

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * 该类封装了微博接口。
 * 详情请参考<a href="http://t.cn/8F3e7SE">微博接口</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class StatusesAPI extends AbsOpenAPI {

    /** 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐 */
    public static final int FEATURE_ALL      = 0;
    public static final int FEATURE_ORIGINAL = 1;
    public static final int FEATURE_PICTURE  = 2;
    public static final int FEATURE_VIDEO    = 3;
    public static final int FEATURE_MUSICE   = 4;

    /** 作者筛选类型，0：全部、1：我关注的人、2：陌生人 */
    public static final int AUTHOR_FILTER_ALL        = 0;
    public static final int AUTHOR_FILTER_ATTENTIONS = 1;
    public static final int AUTHOR_FILTER_STRANGER   = 2;

    /** 来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论 */
    public static final int SRC_FILTER_ALL      = 0;
    public static final int SRC_FILTER_WEIBO    = 1;
    public static final int SRC_FILTER_WEIQUN   = 2;

    /** 原创筛选类型，0：全部微博、1：原创的微博。 */
    public static final int TYPE_FILTER_ALL     = 0;
    public static final int TYPE_FILTER_ORIGAL  = 1;

    /** 获取类型，1：微博、2：评论、3：私信，默认为1。 */
    public static final int TYPE_STATUSES   = 1;
    public static final int TYPE_COMMENTS   = 2;
    public static final int TYPE_MESSAGE    = 3;

    /** 标识是否在转发的同时发表评论，0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0 */
    public static final int COMMENTS_NONE           = 0;
    public static final int COMMENTS_CUR_STATUSES   = 1;
    public static final int COMMENTS_RIGAL_STATUSES = 2;
    public static final int COMMENTS_BOTH           = 3;

    /** 表情类别，face：普通表情、ani：魔法表情、cartoon：动漫表情，默认为face。 */
    public static final String EMOTION_TYPE_FACE    = "face";
    public static final String EMOTION_TYPE_ANI     = "ani";
    public static final String EMOTION_TYPE_CARTOON = "cartoon";

    /** 语言类别，cnname：简体、twname：繁体，默认为cnname。 */
    public static final String LANGUAGE_CNNAME = "cnname";
    public static final String LANGUAGE_TWNAME = "twname";

    public StatusesAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/statuses";

    /**
     * 返回最新的公共微博。
     * 
     * @param count     单页返回的记录条数，默认为50
     * @param page      返回结果的页码，默认为1
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void publicTimeline(int count, int page, boolean base_app, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("count", count);
        params.put("page", page);
        params.put("base_app", base_app ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/public_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取当前登录用户及其所关注用户的最新微博。
     * 
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param base_app      是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param featureType   过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。 
     *                      <li> {@link #FEATURE_ALL}
     *                      <li> {@link #FEATURE_ORIGINAL}
     *                      <li> {@link #FEATURE_PICTURE}
     *                      <li> {@link #FEATURE_VIDEO}
     *                      <li> {@link #FEATURE_MUSICE}
     * @param trim_user     返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
     * @param listener      异步请求回调接口
     */
    public void friendsTimeline(long since_id, long max_id, int count, int page, boolean base_app, int featureType,
            boolean trim_user, RequestListener listener) {
        WeiboParameters params = buildTimeLineWithAppTrim(since_id, max_id, count, page, base_app, trim_user, featureType);
        requestAsync(SERVER_URL_PRIX + "/friends_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取当前登录用户及其所关注用户的最新微博的ID。
     * 
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param base_app      是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param featureType   过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0
     *                      <li> {@link #FEATURE_ALL}
     *                      <li> {@link #FEATURE_ORIGINAL}
     *                      <li> {@link #FEATURE_PICTURE}
     *                      <li> {@link #FEATURE_VIDEO}
     *                      <li> {@link #FEATURE_MUSICE}
     * @param listener      异步请求回调接口
     */
    public void friendsTimelineIds(long since_id, long max_id, int count, int page, boolean base_app, int featureType,
            RequestListener listener) {
        WeiboParameters params = buildTimeLineWithApp(since_id, max_id, count, page, base_app, featureType);
        requestAsync(SERVER_URL_PRIX + "/friends_timeline/ids.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取当前登录用户及其所关注用户的最新微博。
     * 
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param base_app      是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param featureType   过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0
     *                      <li> {@link #FEATURE_ALL}
     *                      <li> {@link #FEATURE_ORIGINAL}
     *                      <li> {@link #FEATURE_PICTURE}
     *                      <li> {@link #FEATURE_VIDEO}
     *                      <li> {@link #FEATURE_MUSICE}
     * @param trim_user     返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
     * @param listener      异步请求回调接口
     */
    public void homeTimeline(long since_id, long max_id, int count, int page, boolean base_app, int featureType,
            boolean trim_user, RequestListener listener) {
        WeiboParameters params = buildTimeLineWithAppTrim(since_id, max_id, count, page, base_app, trim_user,
                featureType);
        requestAsync(SERVER_URL_PRIX + "/home_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取某个用户最新发表的微博列表。
     * 
     * @param uid           需要查询的用户ID
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param base_app      是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param featureType   过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0
     *                      <li> {@link #FEATURE_ALL}
     *                      <li> {@link #FEATURE_ORIGINAL}
     *                      <li> {@link #FEATURE_PICTURE}
     *                      <li> {@link #FEATURE_VIDEO}
     *                      <li> {@link #FEATURE_MUSICE}
     * @param trim_user     返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
     * @param listener      异步请求回调接口
     */
    public void userTimeline(long uid, long since_id, long max_id, int count, int page, boolean base_app,
            int featureType, boolean trim_user, RequestListener listener) {
        WeiboParameters params = buildTimeLineWithAppTrim(since_id, max_id, count, page, base_app, trim_user,
                featureType);
        params.put("uid", uid);
        requestAsync(SERVER_URL_PRIX + "/user_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取某个用户最新发表的微博列表。
     * 
     * @param screen_name   需要查询的用户昵称
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param base_app      是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param featureType   过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0
     *                      <li> {@link #FEATURE_ALL}
     *                      <li> {@link #FEATURE_ORIGINAL}
     *                      <li> {@link #FEATURE_PICTURE}
     *                      <li> {@link #FEATURE_VIDEO}
     *                      <li> {@link #FEATURE_MUSICE}
     * @param trim_user     返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
     * @param listener      异步请求回调接口
     */
    public void userTimeline(String screen_name, long since_id, long max_id, int count, int page, boolean base_app,
            int featureType, boolean trim_user, RequestListener listener) {
        WeiboParameters params = buildTimeLineWithAppTrim(since_id, max_id, count, page, base_app, trim_user,
                featureType);
        params.put("screen_name", screen_name);
        requestAsync(SERVER_URL_PRIX + "/user_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取当前用户最新发表的微博列表
     * 
     * @param screen_name   需要查询的用户昵称
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param base_app      是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param featureType   过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0
     *                      <li> {@link #FEATURE_ALL}
     *                      <li> {@link #FEATURE_ORIGINAL}
     *                      <li> {@link #FEATURE_PICTURE}
     *                      <li> {@link #FEATURE_VIDEO}
     *                      <li> {@link #FEATURE_MUSICE}
     * @param trim_user     返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
     * @param listener      异步请求回调接口
     */
    public void userTimeline(long since_id, long max_id, int count, int page, boolean base_app, int featureType,
            boolean trim_user, RequestListener listener) {
        WeiboParameters params = buildTimeLineWithAppTrim(since_id, max_id, count, page, base_app, trim_user,
                featureType);
        requestAsync(SERVER_URL_PRIX + "/user_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户发布的微博的ID
     * 
     * @param uid           需要查询的用户ID
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param base_app      是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param featureType   过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0
     *                      <li> {@link #FEATURE_ALL}
     *                      <li> {@link #FEATURE_ORIGINAL}
     *                      <li> {@link #FEATURE_PICTURE}
     *                      <li> {@link #FEATURE_VIDEO}
     *                      <li> {@link #FEATURE_MUSICE}
     * @param listener      异步请求回调接口
     */
    public void userTimelineIds(long uid, long since_id, long max_id, int count, int page, boolean base_app,
            int featureType, RequestListener listener) {
        WeiboParameters params = buildTimeLineWithApp(since_id, max_id, count, page, base_app, featureType);
        params.put("uid", uid);
        requestAsync(SERVER_URL_PRIX + "/user_timeline/ids.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户发布的微博的ID
     * 
     * @param screen_name   需要查询的用户昵称
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param base_app      是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param featureType   过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0
     *                      <li> {@link #FEATURE_ALL}
     *                      <li> {@link #FEATURE_ORIGINAL}
     *                      <li> {@link #FEATURE_PICTURE}
     *                      <li> {@link #FEATURE_VIDEO}
     *                      <li> {@link #FEATURE_MUSICE}
     * @param listener      异步请求回调接口
     */
    public void userTimelineIds(String screen_name, long since_id, long max_id, int count, int page, boolean base_app,
            int featureType, RequestListener listener) {
        WeiboParameters params = buildTimeLineWithApp(since_id, max_id, count, page, base_app, featureType);
        params.put("screen_name", screen_name);
        requestAsync(SERVER_URL_PRIX + "/user_timeline/ids.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取指定微博的转发微博列表
     * 
     * @param id            需要查询的微博ID。
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param authorType    作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。可为以下几种：
     *                      <li> {@link #AUTHOR_FILTER_ALL}
     *                      <li> {@link #AUTHOR_FILTER_ATTENTIONS}
     *                      <li> {@link #AUTHOR_FILTER_STRANGER}
     * @param listener      异步请求回调接口
     */
    public void repostTimeline(long id, long since_id, long max_id, int count, int page, int authorType,
            RequestListener listener) {
        WeiboParameters params = buildTimeLineBase(since_id, max_id, count, page);
        params.put("id", id);
        params.put("filter_by_author", authorType);
        requestAsync(SERVER_URL_PRIX + "/repost_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取一条原创微博的最新转发微博的ID。
     * 
     * @param id            需要查询的微博ID
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param authorType    作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。可为以下几种：
     *                      <li> {@link #AUTHOR_FILTER_ALL}
     *                      <li> {@link #AUTHOR_FILTER_ATTENTIONS}
     *                      <li> {@link #AUTHOR_FILTER_STRANGER}
     * @param listener      异步请求回调接口
     */
    public void repostTimelineIds(long id, long since_id, long max_id, int count, int page, int authorType,
            RequestListener listener) {
        WeiboParameters params = buildTimeLineBase(since_id, max_id, count, page);
        params.put("id", id);
        params.put("filter_by_author", authorType);
        requestAsync(SERVER_URL_PRIX + "/repost_timeline/ids.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取当前用户最新转发的微博列表。
     * 
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param listener      异步请求回调接口
     */
    public void repostByMe(long since_id, long max_id, int count, int page, RequestListener listener) {
        WeiboParameters params = buildTimeLineBase(since_id, max_id, count, page);
        requestAsync(SERVER_URL_PRIX + "/repost_by_me.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取最新的提到登录用户的微博列表，即@我的微博。
     * 
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param authorType    作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。可为以下几种：
     *                      <li> {@link #AUTHOR_FILTER_ALL}
     *                      <li> {@link #AUTHOR_FILTER_ATTENTIONS}
     *                      <li> {@link #AUTHOR_FILTER_STRANGER}
     * @param sourceType    来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论。可分为以下几种：
     *                      <li> {@link #SRC_FILTER_ALL}
     *                      <li> {@link #SRC_FILTER_WEIBO}
     *                      <li> {@link #SRC_FILTER_WEIQUN}
     * @param filterType    原创筛选类型，0：全部微博、1：原创的微博，默认为0。可分为以下几种：
     *                      <li> {@link #TYPE_FILTER_ALL}
     *                      <li> {@link #TYPE_FILTER_ORIGAL}
     * @param trim_user     返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
     * @param listener      异步请求回调接口
     */
    public void mentions(long since_id, long max_id, int count, int page, int authorType, int sourceType,
            int filterType, boolean trim_user, RequestListener listener) {
        WeiboParameters params = buildTimeLineBase(since_id, max_id, count, page);
        params.put("filter_by_author", authorType);
        params.put("filter_by_source", sourceType);
        params.put("filter_by_type", filterType);
        params.put("trim_user", trim_user ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/mentions.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取@当前用户的最新微博的ID。
     * 
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param authorType    作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。可为以下几种：
     *                      <li> {@link #AUTHOR_FILTER_ALL}
     *                      <li> {@link #AUTHOR_FILTER_ATTENTIONS}
     *                      <li> {@link #AUTHOR_FILTER_STRANGER}
     * @param sourceType    来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论。可分为以下几种：
     *                      <li> {@link #SRC_FILTER_ALL}
     *                      <li> {@link #SRC_FILTER_WEIBO}
     *                      <li> {@link #SRC_FILTER_WEIQUN}
     * @param filterType    原创筛选类型，0：全部微博、1：原创的微博，默认为0。可分为以下几种：
     *                      <li> {@link #TYPE_FILTER_ALL}
     *                      <li> {@link #TYPE_FILTER_ORIGAL}
     * @param listener      异步请求回调接口
     */
    public void mentionsIds(long since_id, long max_id, int count, int page, int authorType, int sourceType,
            int filterType, RequestListener listener) {
        WeiboParameters params = buildTimeLineBase(since_id, max_id, count, page);
        params.put("filter_by_author", authorType);
        params.put("filter_by_source", sourceType);
        params.put("filter_by_type", filterType);
        requestAsync(SERVER_URL_PRIX + "/mentions/ids.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取双向关注用户的最新微博。
     * 
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count         单页返回的记录条数，默认为50
     * @param page          返回结果的页码，默认为1
     * @param base_app      是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param featureType   过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0
     *                      <li> {@link #FEATURE_ALL}
     *                      <li> {@link #FEATURE_ORIGINAL}
     *                      <li> {@link #FEATURE_PICTURE}
     *                      <li> {@link #FEATURE_VIDEO}
     *                      <li> {@link #FEATURE_MUSICE}
     * @param trim_user     返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
     * @param listener      异步请求回调接口
     */
    public void bilateralTimeline(long since_id, long max_id, int count, int page, boolean base_app, int featureType,
            boolean trim_user, RequestListener listener) {
        WeiboParameters params = buildTimeLineWithAppTrim(since_id, max_id, count, page, base_app, trim_user,
                featureType);
        requestAsync(SERVER_URL_PRIX + "/bilateral_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 根据微博ID获取单条微博内容。
     * 
     * @param id        需要获取的微博ID
     * @param listener  异步请求回调接口
     */
    public void show(long id, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("id", id);
        requestAsync(SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 通过微博（评论、私信）ID获取其MID。
     * 
     * @param ids       需要查询的微博（评论、私信）ID，最多不超过20个。
     * @param type      获取类型，1：微博、2：评论、3：私信，默认为1。可为几下几种： 
     *                  <li> {@link #TYPE_STATUSES}
     *                  <li> {@link #TYPE_COMMENTS}
     *                  <li> {@link #TYPE_MESSAGE}
     * @param listener  异步请求回调接口
     */
    public void queryMID(long[] ids, int type, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        if (1 == ids.length) {
            params.put("id", ids[0]);
        } else {
            params.put("is_batch", 1);
            StringBuilder strb = new StringBuilder();
            for (long id : ids) {
                strb.append(id).append(",");
            }
            strb.deleteCharAt(strb.length() - 1);
            params.put("id", strb.toString());
        }
        params.put("type", type);
        requestAsync(SERVER_URL_PRIX + "/querymid.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 通过微博（评论、私信）MID获取其ID,形如“3z4efAo4lk”的MID即为经过base62转换的MID。
     * 
     * @param mids      需要查询的微博（评论、私信）MID，最多不超过20个
     * @param type      获取类型，1：微博、2：评论、3：私信，默认为1。可为几下几种： 
     *                  <li> {@link #TYPE_STATUSES}
     *                  <li> {@link #TYPE_COMMENTS}
     *                  <li> {@link #TYPE_MESSAGE}
     * @param inbox     仅对私信有效，当MID类型为私信时用此参数，0：发件箱、1：收件箱，默认为0
     * @param isBase62  MID是否是base62编码，0：否、1：是，默认为0
     * @param listener  异步请求回调接口
     */
    public void queryID(String[] mids, int type, boolean inbox, boolean isBase62, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        if (mids != null) {
            if (1 == mids.length) {
                params.put("mid", mids[0]);
            } else {
                params.put("is_batch", 1);
                StringBuilder strb = new StringBuilder();
                for (String mid : mids) {
                    strb.append(mid).append(",");
                }
                strb.deleteCharAt(strb.length() - 1);
                params.put("mid", strb.toString());
            }
        }

        params.put("type", type);
        params.put("inbox", inbox ? 1 : 0);
        params.put("isBase62", isBase62 ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/queryid.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 按天返回热门微博转发榜的微博列表。
     * 
     * @param count     返回的记录条数，最大不超过50，默认为20
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void hotRepostDaily(int count, boolean base_app, RequestListener listener) {
        WeiboParameters params = buildHotParams(count, base_app);
        requestAsync(SERVER_URL_PRIX + "/hot/repost_daily.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 按周返回热门微博转发榜的微博列表。
     * 
     * @param count     返回的记录条数，最大不超过50，默认为20
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void hotRepostWeekly(int count, boolean base_app, RequestListener listener) {
        WeiboParameters params = buildHotParams(count, base_app);
        requestAsync(SERVER_URL_PRIX + "/hot/repost_weekly.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 按天返回热门微博评论榜的微博列表。
     * 
     * @param count     返回的记录条数，最大不超过50，默认为20
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void hotCommentsDaily(int count, boolean base_app, RequestListener listener) {
        WeiboParameters params = buildHotParams(count, base_app);
        requestAsync(SERVER_URL_PRIX + "/hot/comments_daily.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 按周返回热门微博评论榜的微博列表。
     * 
     * @param count     返回的记录条数，最大不超过50，默认为20
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void hotCommentsWeekly(int count, boolean base_app, RequestListener listener) {
        WeiboParameters params = buildHotParams(count, base_app);
        requestAsync(SERVER_URL_PRIX + "/hot/comments_weekly.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 批量获取指定微博的转发数评论数。
     * 
     * @param ids       需要获取数据的微博ID，最多不超过100个
     * @param listener  异步请求回调接口
     */
    public void count(String[] ids, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        StringBuilder strb = new StringBuilder();
        for (String id : ids) {
            strb.append(id).append(",");
        }
        strb.deleteCharAt(strb.length() - 1);
        params.put("ids", strb.toString());
        requestAsync(SERVER_URL_PRIX + "/count.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 转发一条微博。
     * 
     * @param id            要转发的微博ID
     * @param status        添加的转发文本，内容不超过140个汉字，不填则默认为“转发微博”
     * @param commentType   是否在转发的同时发表评论，0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0
     *                      <li> {@link #COMMENTS_NONE}
     *                      <li> {@link #COMMENTS_CUR_STATUSES}
     *                      <li> {@link #COMMENTS_RIGAL_STATUSES}
     *                      <li> {@link #COMMENTS_BOTH}
     * @param listener      异步请求回调接口
     */
    public void repost(long id, String status, int commentType, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("id", id);
        params.put("status", status);
        params.put("is_comment", commentType);
        requestAsync(SERVER_URL_PRIX + "/repost.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 根据微博ID删除指定微博。
     * 
     * @param id        需要删除的微博ID
     * @param listener  异步请求回调接口
     */
    public void destroy(long id, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("id", id);
        requestAsync(SERVER_URL_PRIX + "/destroy.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 发布一条新微博(连续两次发布的微博不可以重复)。
     * 
     * @param content   要发布的微博文本内容，内容不超过140个汉字
     * @param lat       纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0
     * @param lon       经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0
     * @param listener  异步请求回调接口
     */
    public void update(String content, String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        requestAsync(SERVER_URL_PRIX + "/update.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 上传图片并发布一条新微博，此方法会处理urlencode。
     * 
     * @param content   要发布的微博文本内容，内容不超过140个汉字
     * @param bitmap    要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M
     * @param lat       纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0
     * @param lon       经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0
     * @param listener  异步请求回调接口
     
     */
    public void upload(String content, Bitmap bitmap, String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        params.put("pic", bitmap);
        requestAsync(SERVER_URL_PRIX + "/upload.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 指定一个图片URL地址抓取后上传并同时发布一条新微博，此方法会处理URLencode。
     * 
     * @param status    要发布的微博文本内容，内容不超过140个汉字
     * @param imageUrl  图片的URL地址，必须以http开头
     * @param pic_id    已经上传的图片pid，多个时使用英文半角逗号符分隔，最多不超过九张。 imageUrl 和 pic_id必选一个，两个参数都存在时，取picid参数的值为准
     * @param lat       纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0
     * @param lon       经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0
     * @param listener  异步请求回调接口
     */
    public void uploadUrlText(String status, String imageUrl, String pic_id, String lat, String lon,
            RequestListener listener) {
        WeiboParameters params = buildUpdateParams(status, lat, lon);
        params.put("url", imageUrl);
        params.put("pic_id", pic_id);
        requestAsync(SERVER_URL_PRIX + "/upload_url_text.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 获取微博官方表情的详细信息。
     * 
     * @param type      表情类别，表情类别，face：普通表情、ani：魔法表情、cartoon：动漫表情，默认为face。可为以下几种： 
     *                  <li> {@link #EMOTION_TYPE_FACE}
     *                  <li> {@link #EMOTION_TYPE_ANI}
     *                  <li> {@link #EMOTION_TYPE_CARTOON}
     * @param language  语言类别，cnname：、twname：，默认为cnname。 
     *                  <li> {@link #LANGUAGE_CNNAME}
     *                  <li> {@link #LANGUAGE_TWNAME}
     * @param listener  异步请求回调接口
     */
    public void emotions(String type, String language, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("type", type);
        params.put("language", language);
        requestAsync(API_SERVER + "/emotions.json", params, HTTPMETHOD_GET, listener);
    }

    // 组装TimeLines的参数
    private WeiboParameters buildTimeLineBase(long since_id, long max_id, int count, int page) {
        WeiboParameters params = new WeiboParameters();
        params.put("since_id", since_id);
        params.put("max_id", max_id);
        params.put("count", count);
        params.put("page", page);
        return params;
    }

    private WeiboParameters buildTimeLineWithApp(long since_id, long max_id, int count, int page, boolean base_app,
            int featureType) {
        WeiboParameters params = buildTimeLineBase(since_id, max_id, count, page);
        params.put("feature", featureType);
        params.put("base_app", base_app ? 1 : 0);
        return params;
    }

    private WeiboParameters buildTimeLineWithAppTrim(long since_id, long max_id, int count, int page, boolean base_app,
            boolean trim_user, int featureType) {
        WeiboParameters params = buildTimeLineWithApp(since_id, max_id, count, page, base_app, featureType);
        params.put("trim_user", trim_user ? 1 : 0);
        return params;
    }

    private WeiboParameters buildHotParams(int count, boolean base_app) {
        WeiboParameters params = new WeiboParameters();
        params.put("count", count);
        params.put("base_app", base_app ? 1 : 0);
        return params;
    }

    // 组装微博请求参数
    private WeiboParameters buildUpdateParams(String content, String lat, String lon) {
        WeiboParameters params = new WeiboParameters();
        params.put("status", content);
        if (!TextUtils.isEmpty(lon)) {
            params.put("long", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            params.put("lat", lat);
        }
        return params;
    }

}
