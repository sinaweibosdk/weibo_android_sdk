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
 * 该类封装了微博的位置服务接口。
 * 详情请参考<a href="http://t.cn/8F1s1DJ">微博位置服务</a>
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class PlaceAPI extends AbsOpenAPI {

    /** 获取到周边的排序方式， 0：按时间排序 , 1：按与中心点距离排序。 */
    public static final int SORT_BY_TIME     = 0;
    public static final int SORT_BY_DISTENCE = 1;

    /** 获取位置的排序方式， 0：按权重，1：按距离，3：按签到人数。 */
    public static final int NEARBY_POIS_SORT_BY_WEIGHT          = 0;
    public static final int NEARBY_POIS_SORT_BY_DISTENCE        = 1;
    public static final int NEARBY_POIS_SORT_BY_CHECKIN_NUMBER  = 2;

    /** 地点的排序方式，0：按时间、1：按热门，默认为0，目前只支持按时间。 */
    public static final int POIS_SORT_BY_TIME   = 0;
    public static final int POIS_SORT_BY_HOT    = 1;

    /** 用户关系过滤，0：全部、1：只返回陌生人、2：只返回关注人，默认为0。 */
    public static final int RELATIONSHIP_FILTER_ALL         = 0;
    public static final int RELATIONSHIP_FILTER_STRANGER    = 1;
    public static final int RELATIONSHIP_FILTER_FOLLOW      = 2;

    /** 性别过滤，0：全部、1：男、2：女，默认为0。 */
    public static final int GENDER_ALL   = 0;
    public static final int GENDER_MAN   = 1;
    public static final int GENDER_WOMAM = 2;

    /** 用户级别过滤，0：全部、1：普通用户、2：VIP用户、7：达人，默认为0。 */
    public static final int USER_LEVEL_ALL    = 0;
    public static final int USER_LEVEL_NORMAL = 1;
    public static final int USER_LEVEL_VIP    = 2;
    public static final int USER_LEVEL_STAR   = 7;

    /** 周边用户排序方式，0：按时间、1：按距离、2：按社会化关系，默认为2。 */
    public static final int NEARBY_USER_SORT_BY_TIME        = 0;
    public static final int NEARBY_USER_SORT_BY_DISTANCE    = 1;
    public static final int NEARBY_USER_SORT_BY_SOCIAL_SHIP = 2;

    private static final String SERVER_URL_PRIX = API_SERVER + "/place";

    public PlaceAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    /**
     * 获取最新20条公共的位置动态。
     * 
     * @param count     每次返回的动态数，最大为50，默认为20
     * @param base_app  是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0
     */
    public void pulicTimeline(long count, boolean base_app, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("count", count);
        params.put("base_app", base_app ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/public_timelin.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取当前登录用户与其好友的位置动态。
     * 
     * @param since_id  若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id    若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count     单页返回的记录条数，最大为50，默认为20
     * @param page      返回结果的页码，默认为1
     * @param only_attentions true：仅返回关注的，false：返回好友的，默认为true
     * @param listener  异步请求回调接口
     */
    public void friendsTimeline(long since_id, long max_id, int count, int page, boolean only_attentions,
            RequestListener listener) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("type", only_attentions ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/friends_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取某个用户的位置动态。
     * 
     * @param uid       需要查询的用户ID
     * @param since_id  若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id    若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count     单页返回的记录条数，最大为50，默认为20
     * @param page      返回结果的页码，默认为1
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void userTimeline(long uid, long since_id, long max_id, int count, int page, boolean base_app,
            RequestListener listener) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("uid", uid);
        params.put("base_app", base_app ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/user_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取某个位置地点的动态。
     * 
     * @param poiid     需要查询的POI点ID
     * @param since_id  若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id    若指定此参数，则返回ID小于或等于max_id的微博，默认为0
     * @param count     单页返回的记录条数，最大为50，默认为20
     * @param page      返回结果的页码，默认为1
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void poiTimeline(String poiid, long since_id, long max_id, int count, int page, boolean base_app,
            RequestListener listener) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("poiid", poiid);
        params.put("base_app", base_app ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/poi_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取某个位置周边的动态。
     * 
     * @param lat       纬度。有效范围：-90.0到+90.0，+表示北纬
     * @param lon       经度。有效范围：-180.0到+180.0，+表示东经
     * @param range     搜索范围，单位米，默认2000米，最大11132米
     * @param starttime 开始时间，Unix时间戳
     * @param endtime   结束时间，Unix时间戳
     * @param sortType  排序方式。0：按时间排序， 1：按与中心点距离进行排序
     *                  <li>{@link #SORT_BY_TIME}
     *                  <li>{@link #SORT_BY_DISTENCE}
     * @param count     单页返回的记录条数，最大为50，默认为20
     * @param page      返回结果的页码，默认为1
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param offset    传入的经纬度是否是纠偏过，false：没纠偏、true：纠偏过，默认为false
     * @param listener  异步请求回调接口
     */
    public void nearbyTimeline(String lat, String lon, int range, long starttime, long endtime, int sortType,
            int count, int page, boolean base_app, boolean offset, RequestListener listener) {
        WeiboParameters params = buildNearbyParams(lat, lon, range, count, page, sortType, offset);
        params.put("starttime", starttime);
        params.put("endtime", endtime);
        params.put("base_app", base_app ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/nearby_timeline.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 根据ID获取动态的详情。
     * 
     * @param id        需要获取的动态ID
     * @param listener  异步请求回调接口
     */
    public void statusesShow(long id, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("id", id);
        requestAsync(SERVER_URL_PRIX + "/statuses/show.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取LBS位置服务内的用户信息。
     * 
     * @param uid       需要查询的用户ID
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void usersShow(long uid, boolean base_app, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("uid", uid);
        params.put("base_app", base_app ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/users/show.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户签到过的地点列表。
     * 
     * @param uid       需要查询的用户ID
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void usersCheckins(long uid, int count, int page, boolean base_app, RequestListener listener) {
        WeiboParameters params = buildUserParams(uid, count, page, base_app);
        requestAsync(SERVER_URL_PRIX + "/users/checkins.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户的照片列表。
     * 
     * @param uid       需要查询的用户ID
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void usersPhotos(long uid, int count, int page, boolean base_app, RequestListener listener) {
        WeiboParameters params = buildUserParams(uid, count, page, base_app);
        requestAsync(SERVER_URL_PRIX + "/users/photos.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户的点评列表。
     * 
     * @param uid       需要查询的用户ID
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void usersTips(long uid, int count, int page, boolean base_app, RequestListener listener) {
        WeiboParameters params = buildUserParams(uid, count, page, base_app);
        requestAsync(SERVER_URL_PRIX + "/users/tips.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取用户的todo列表。
     * 
     * @param uid       需要查询的用户ID
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void usersTodo(long uid, int count, int page, boolean base_app, RequestListener listener) {
        WeiboParameters params = buildUserParams(uid, count, page, base_app);
        requestAsync(SERVER_URL_PRIX + "/users/todos.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取地点详情。
     * 
     * @param poiid     需要查询的POI地点ID
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void poisShow(String poiid, boolean base_app, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("poiid", poiid);
        params.put("base_app", base_app ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/pois/show.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取在某个地点签到的人的列表。
     * 
     * @param poiid     需要查询的POI地点ID
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void poisUsers(String poiid, int count, int page, boolean base_app, RequestListener listener) {
        WeiboParameters params = buildPoisParams(poiid, count, page, base_app);
        requestAsync(SERVER_URL_PRIX + "/pois/users.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取地点照片列表。
     * 
     * @param poiid     需要查询的POI地点ID
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1
     * @param sortType  排序方式，0：按时间、1：按热门，默认为0，目前只支持按时间。
     *                  <li>{@link #POIS_SORT_BY_TIME}
     *                  <li>{@link #POIS_SORT_BY_HOT}
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void poisPhotos(String poiid, int count, int page, int sortType, boolean base_app, RequestListener listener) {
        WeiboParameters params = buildPoisParams(poiid, count, page, base_app);
        params.put("sort", sortType);
        requestAsync(SERVER_URL_PRIX + "/pois/photos.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取地点点评列表。
     * 
     * @param poiid     需要查询的POI地点ID
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1
     * @param sortType  排序方式，0：按时间、1：按热门，默认为0，目前只支持按时间。
     *                  <li>{@link #POIS_SORT_BY_TIME}
     *                  <li>{@link #POIS_SORT_BY_HOT}
     * @param base_app  是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false
     * @param listener  异步请求回调接口
     */
    public void poisTips(String poiid, int count, int page, int sortType, boolean base_app, RequestListener listener) {
        WeiboParameters params = buildPoisParams(poiid, count, page, base_app);
        params.put("sort", sortType);
        requestAsync(SERVER_URL_PRIX + "/pois/tips.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 按省市查询地点。
     * 
     * @param keyword   查询的关键词
     * @param city      城市代码，默认为全国搜索
     * @param category  查询的分类代码，取值范围见：分类代码对应表
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1。
     * @param listener  异步请求回调接口
     */
    public void poisSearch(String keyword, String city, String category, int count, int page, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("keyword", keyword);
        params.put("city", city);
        params.put("category", category);
        params.put("count", count);
        params.put("page", page);
        requestAsync(SERVER_URL_PRIX + "/pois/search.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取地点分类。
     * 
     * @param pid       父分类ID，默认为0
     * @param returnALL 是否返回全部分类，false：只返回本级下的分类，true：返回全部分类，默认为false
     * @param listener  异步请求回调接口
     */
    public void poisCategory(int pid, boolean returnALL, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("pid", pid);
        params.put("flag", returnALL ? 1 : 0);
        requestAsync(SERVER_URL_PRIX + "/pois/category.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取附近地点。
     * 
     * @param lat       纬度，有效范围：-90.0到+90.0，+表示北纬
     * @param lon       经度，有效范围：-180.0到+180.0，+表示东经
     * @param range     查询范围半径，默认为2000，最大为10000，单位米
     * @param q         查询的关键词
     * @param category  查询的分类代码，取值范围见：分类代码对应表
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1
     * @param sortType  排序方式，0：按权重，1：按距离，2：按签到人数。默认为0
     *                  <li>{@link #NEARBY_POIS_SORT_BY_WEIGHT}
     *                  <li>{@link #NEARBY_POIS_SORT_BY_DISTENCE}
     *                  <li>{@link #NEARBY_POIS_SORT_BY_CHECKIN_NUMBER}
     * @param offset    传入的经纬度是否是纠偏过，false：没纠偏、true：纠偏过，默认为false
     * @param listener  异步请求回调接口
     */
    public void nearbyPois(String lat, String lon, int range, String q, String category, int count, int page,
            int sortType, boolean offset, RequestListener listener) {
        WeiboParameters params = buildNearbyParams(lat, lon, range, count, page, sortType, offset);
        params.put("q", q);
        params.put("category", category);
        requestAsync(SERVER_URL_PRIX + "/nearby/pois.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取附近发位置微博的人。
     * 
     * @param lat       纬度，有效范围：-90.0到+90.0，+表示北纬
     * @param lon       经度，有效范围：-180.0到+180.0，+表示东经
     * @param range     查询范围半径，默认为2000，最大为11132，单位米
     * @param starttime 开始时间，Unix时间戳
     * @param endtime   结束时间，Unix时间戳
     * @param sortType  排序方式，0：按时间、1：按距离，默认为0
     *                  <li>{@link #SORT_BY_TIME}
     *                  <li>{@link #SORT_BY_DISTENCE}
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1
     * @param offset    传入的经纬度是否是纠偏过，false：没纠偏、true：纠偏过，默认为false
     * @param listener  异步请求回调接口
     */
    public void nearbyUsers(String lat, String lon, int range, long starttime, long endtime, int sortType, int count,
            int page, boolean offset, RequestListener listener) {
        WeiboParameters params = buildNearbyParams(lat, lon, range, count, page, sortType, offset);
        params.put("starttime", starttime);
        params.put("endtime", endtime);
        requestAsync(SERVER_URL_PRIX + "/nearby/users.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取附近照片。
     * 
     * @param lat       纬度，有效范围：-90.0到+90.0，+表示北纬
     * @param lon       经度，有效范围：-180.0到+180.0，+表示东经
     * @param range     查询范围半径，默认为2000，最大为11132，单位米
     * @param starttime 开始时间，Unix时间戳
     * @param endtime   结束时间，Unix时间戳
     * @param sortType  排序方式，0：按时间、1：按距离，默认为0
     *                  <li>{@link #SORT_BY_TIME}
     *                  <li>{@link #SORT_BY_DISTENCE}
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1
     * @param offset    传入的经纬度是否是纠偏过，false：没纠偏、true：纠偏过，默认为false
     * @param listener  异步请求回调接口
     */
    public void nearbyPhotos(String lat, String lon, int range, long starttime, long endtime, int sortType, int count,
            int page, boolean offset, RequestListener listener) {
        WeiboParameters params = buildNearbyParams(lat, lon, range, count, page, sortType, offset);
        params.put("starttime", starttime);
        params.put("endtime", endtime);
        requestAsync(SERVER_URL_PRIX + "/nearby/photos.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取附近的人
     * 
     * @param lat       纬度，有效范围：-90.0到+90.0，+表示北纬
     * @param lon       经度，有效范围：-180.0到+180.0，+表示东经
     * @param count     单页返回的记录条数，默认为20，最大为50
     * @param page      返回结果的页码，默认为1
     * @param range     查询范围半径，默认为2000，最大为11132
     * @param sortType  排序方式，0：按时间、1：按距离、2：按社会化关系，默认为2。
     *                  <li>{@link #NEARBY_USER_SORT_BY_TIME}
     *                  <li>{@link #NEARBY_POIS_SORT_BY_DISTENCE}
     *                  <li>{@link #NEARBY_USER_SORT_BY_SOCIAL_SHIP}
     * @param filterType（暂未启用）用户关系过滤，0：全部、1：只返回陌生人、2：只返回关注人，默认为0
     *                  <li>{@link #RELATIONSHIP_FILTER_ALL}
     *                  <li>{@link #RELATIONSHIP_FILTER_STRANGER}
     *                  <li>{@link #RELATIONSHIP_FILTER_FOLLOW}
     * @param genderType性别过滤，0：全部、1：男、2：女，默认为0
     *                  <li>{@link #GENDER_ALL}
     *                  <li>{@link #GENDER_MAN}
     *                  <li>{@link #GENDER_WOMAM}
     * @param levelType 用户级别过滤，0：全部、1：普通用户、2：VIP用户、7：达人，默认为0
     *                  <li>{@link #USER_LEVEL_ALL}
     *                  <li>{@link #USER_LEVEL_NORMAL}
     *                  <li>{@link #USER_LEVEL_VIP}
     *                  <li>{@link #USER_LEVEL_STAR}
     * @param start_birth 与参数endbirth一起定义过滤年龄段，数值为年龄大小，默认为空
     * @param end_birth 与参数startbirth一起定义过滤年龄段，数值为年龄大小，默认为空
     * @param offset    传入的经纬度是否是纠偏过，0：没纠偏、1：纠偏过，默认为0
     * @param listener  异步请求回调接口
     */
    public void nearbyUserList(String lat, String lon, int count, int page, int range, int sortType, int filterType,
            int genderType, int levelType, int start_birth, int end_birth, boolean offset, RequestListener listener) {
        WeiboParameters params = buildNearbyParams(lat, lon, range, count, page, sortType, offset);
        params.put("filter", filterType);
        params.put("gender", genderType);
        params.put("level", levelType);
        params.put("startbirth", start_birth);
        params.put("endbirth", end_birth);
        requestAsync(SERVER_URL_PRIX + "/nearby_users/list.json", params, HTTPMETHOD_GET, listener);
    }

    /**
     * 添加地点
     * 
     * @param title     POI点的名称，不超过30个字符，必须进行URLencode
     * @param address   POI点的地址，不超过60个字符，必须进行URLencode
     * @param category  POI的类型分类代码，取值范围见：分类代码对应表，默认为500
     * @param lat       纬度，有效范围：-90.0到+90.0，+表示北纬
     * @param lon       经度，有效范围：-180.0到+180.0，+表示东经
     * @param city      城市代码
     * @param province  省份代码
     * @param country   国家代码
     * @param phone     POI点的电话，不超过14个字符
     * @param postCode  POI点的邮编
     * @param extra     其他，必须进行URLencode
     * @param listener  异步请求回调接口
     */
    public void poisCreate(String title, String address, String category, String lat, String lon, String city,
            String province, String country, String phone, String postCode, String extra, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("title", title);
        params.put("address", address);
        params.put("category", category);
        params.put("lat", lat);
        params.put("long", lon);
        params.put("city", city);
        params.put("province", province);
        params.put("country", country);
        params.put("phone", phone);
        params.put("postcode", postCode);
        params.put("extra", extra);
        requestAsync(SERVER_URL_PRIX + "/pois/create.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 签到同时可以上传一张图片。
     * 
     * @param poiid     需要签到的POI地点ID
     * @param status    签到时发布的动态内容，内容不超过140个汉字
     * @param pic       需要上传的图片路径，仅支持JPEG、GIF、PNG格式，图片大小小于5M。例如：/sdcard/pic.jgp； 注意：pic不能为网络图片
     * @param isPublic  是否同步到微博，默认为不同步
     * @param listener  异步请求回调接口
     */
    public void poisAddCheckin(String poiid, String status, String pic, boolean isPublic, RequestListener listener) {
        WeiboParameters params = buildPoiis(poiid, status, isPublic);
        params.put("pic", pic);
        requestAsync(SERVER_URL_PRIX + "/pois/add_checkin.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 添加照片。
     * 
     * @param poiid     需要添加照片的POI地点ID
     * @param status    签到时发布的动态内容，内容不超过140个汉字
     * @param pic       需要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M。例如：/sdcard/pic.jgp； 注意： pic不能为网络图片
     * @param isPublic  是否同步到微博，默认为不同步
     * @param listener  异步请求回调接口
     */
    public void poisAddPhoto(String poiid, String status, String pic, boolean isPublic, RequestListener listener) {
        WeiboParameters params = buildPoiis(poiid, status, isPublic);
        params.put("pic", pic);
        requestAsync(SERVER_URL_PRIX + "/pois/add_photo.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 添加点评。
     * 
     * @param poiid     需要点评的POI地点ID
     * @param status    点评时发布的动态内容，内容不超过140个汉字
     * @param isPublic  是否同步到微博，默认为不同步
     * @param listener  异步请求回调接口
     */
    public void poisAddTip(String poiid, String status, boolean isPublic, RequestListener listener) {
        WeiboParameters params = buildPoiis(poiid, status, isPublic);
        requestAsync(SERVER_URL_PRIX + "/pois/add_tip.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 添加todo。
     * 
     * @param poiid     需要添加todo的POI地点ID
     * @param status    添加todo时发布的动态内容，必须做URLencode，内容不超过140个汉字
     * @param isPublic  是否同步到微博，1：是、0：否，默认为0
     * @param listener  异步请求回调接口
     */
    public void poisAddTodo(String poiid, String status, boolean isPublic, RequestListener listener) {
        WeiboParameters params = buildPoiis(poiid, status, isPublic);
        requestAsync(SERVER_URL_PRIX + "/pois/add_todo.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 用户添加自己的位置。
     * 
     * @param lat       纬度，有效范围：-90.0到+90.0，+表示北纬
     * @param lon       经度，有效范围：-180.0到+180.0，+表示东经
     * @param listener  异步请求回调接口
     */
    public void nearbyUsersCreate(String lat, String lon, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("lat", lat);
        params.put("long", lon);
        requestAsync(SERVER_URL_PRIX + "/nearby_users/create.json", params, HTTPMETHOD_POST, listener);
    }

    /**
     * 用户删除自己的位置。
     * 
     * @param listener  异步请求回调接口
     */
    public void nearbyUsersDestroy(RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        requestAsync(SERVER_URL_PRIX + "/nearby_users/destory.json", params, HTTPMETHOD_POST, listener);
    }

    // 组装TimeLines的参数
    private WeiboParameters buildTimeLineParamsBase(long since_id, long max_id, int count, int page) {
        WeiboParameters params = new WeiboParameters();
        params.put("since_id", since_id);
        params.put("max_id", max_id);
        params.put("count", count);
        params.put("page", page);
        return params;
    }

    // 组装UserParams的参数
    private WeiboParameters buildUserParams(long uid, int count, int page, boolean base_app) {
        WeiboParameters params = new WeiboParameters();
        params.put("uid", uid);
        params.put("count", count);
        params.put("page", page);
        params.put("base_app", base_app ? 1 : 0);
        return params;
    }

    // 组装UserParams的参数
    private WeiboParameters buildNearbyParams(String lat, String lon, int range, int count, int page,
            int sortType, boolean offset) {
        WeiboParameters params = new WeiboParameters();
        params.put("lat", lat);
        params.put("long", lon);
        params.put("range", range);
        params.put("count", count);
        params.put("page", page);
        params.put("sort", sortType);
        params.put("offset", offset ? 1 : 0);
        return params;
    }

    private WeiboParameters buildPoiis(String poiid, String status, boolean isPublic) {
        WeiboParameters params = new WeiboParameters();
        params.put("poiid", poiid);
        params.put("status", status);
        params.put("public", isPublic ? 1 : 0);
        return params;
    }

    private WeiboParameters buildPoisParams(String poiid, int count, int page, boolean base_app) {
        WeiboParameters params = new WeiboParameters();
        params.put("poiid", poiid);
        params.put("base_app", base_app ? 1 : 0);
        params.put("count", count);
        params.put("page", page);
        return params;
    }
}
