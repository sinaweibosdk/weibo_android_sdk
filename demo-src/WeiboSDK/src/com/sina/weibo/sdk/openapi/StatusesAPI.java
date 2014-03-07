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

package com.sina.weibo.sdk.openapi;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.SparseArray;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * 该类封装了微博接口。
 * 详情请参考<a href="http://t.cn/8F3e7SE">微博接口</a>
 * 
 * @author SINA
 * @since 2014-03-03
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
    
    /** 原创筛选类型，0：全部微博、1：原创的微博。  */
    public static final int TYPE_FILTER_ALL     = 0;
    public static final int TYPE_FILTER_ORIGAL  = 1;    

    /** API URL */
    private static final String API_BASE_URL = API_SERVER + "/statuses";

    /**
     * API 类型。
     * 命名规则：
     *      <li>读取接口：READ_API_XXX
     *      <li>写入接口：WRITE_API_XXX
     * 请注意：该类中的接口仅做为演示使用，并没有包含所有关于微博的接口，第三方开发者可以
     * 根据需要来填充该类，可参考legacy包下 {@link com.sina.weibo.sdk.openapi.legacy.StatusesAPI}
     */
    private static final int READ_API_FRIENDS_TIMELINE = 0;
    private static final int READ_API_MENTIONS         = 1;    
    private static final int WRITE_API_UPDATE          = 2;
    private static final int WRITE_API_REPOST          = 3;
    private static final int WRITE_API_UPLOAD          = 4;
    private static final int WRITE_API_UPLOAD_URL_TEXT = 5;

    private static final SparseArray<String> sAPIList = new SparseArray<String>();
    static {
        sAPIList.put(READ_API_FRIENDS_TIMELINE, API_BASE_URL + "/friends_timeline.json");
        sAPIList.put(READ_API_MENTIONS,         API_BASE_URL + "/mentions.json");
        sAPIList.put(WRITE_API_REPOST,          API_BASE_URL + "/repost.json");
        sAPIList.put(WRITE_API_UPDATE,          API_BASE_URL + "/update.json");
        sAPIList.put(WRITE_API_UPLOAD,          API_BASE_URL + "/upload.json");
        sAPIList.put(WRITE_API_UPLOAD_URL_TEXT, API_BASE_URL + "/upload_url_text.json");
    }

    /**
     * 构造函数，使用各个 API 接口提供的服务前必须先获取 Token。
     * 
     * @param accesssToken 访问令牌
     */
    public StatusesAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }
    
    /**
     * 获取当前登录用户及其所关注用户的最新微博。
     * 
     * @param since_id    若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
     * @param max_id      若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
     * @param count       单页返回的记录条数，默认为50。
     * @param page        返回结果的页码，默认为1。
     * @param base_app    是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
     * @param featureType 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
     *                    <li>{@link #FEATURE_ALL}
     *                    <li>{@link #FEATURE_ORIGINAL}
     *                    <li>{@link #FEATURE_PICTURE}
     *                    <li>{@link #FEATURE_VIDEO}
     *                    <li>{@link #FEATURE_MUSICE}
     * @param trim_user   返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
     * @param listener    异步请求回调接口
     */
    public void friendsTimeline(long since_id, long max_id, int count, int page, boolean base_app,
            int featureType, boolean trim_user, RequestListener listener) {
        WeiboParameters params = 
                buildTimeLineParamsBase(since_id, max_id, count, page, base_app, trim_user, featureType);
        requestAsync(sAPIList.get(READ_API_FRIENDS_TIMELINE), params, HTTPMETHOD_GET, listener);
    }    
    
    /**
     * 获取最新的提到登录用户的微博列表，即@我的微博。
     * 
     * @param since_id      若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
     * @param max_id        若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
     * @param count         单页返回的记录条数，默认为50。
     * @param page          返回结果的页码，默认为1。
     * @param authorType    作者筛选类型，0：全部、1：我关注的人、2：陌生人 ,默认为0。可为以下几种 :
     *                      <li>{@link #AUTHOR_FILTER_ALL}
     *                      <li>{@link #AUTHOR_FILTER_ATTENTIONS}
     *                      <li>{@link #AUTHOR_FILTER_STRANGER}
     * @param sourceType    来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论，默认为0。可为以下几种 :
     *                      <li>{@link #SRC_FILTER_ALL}
     *                      <li>{@link #SRC_FILTER_WEIBO}
     *                      <li>{@link #SRC_FILTER_WEIQUN}
     * @param filterType    原创筛选类型，0：全部微博、1：原创的微博，默认为0。 可为以下几种 :
     *                      <li>{@link #TYPE_FILTER_ALL}
     *                      <li>{@link #TYPE_FILTER_ORIGAL}
     * @param trim_user     返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false
     * @param listener      异步请求回调接口
     */
    public void mentions(long since_id, long max_id, int count, int page, int authorType, int sourceType,
            int filterType, boolean trim_user, RequestListener listener) {
        WeiboParameters params = buildMentionsParams(since_id, max_id, count, page, authorType, sourceType, filterType, trim_user);
        requestAsync(sAPIList.get(READ_API_MENTIONS), params, HTTPMETHOD_GET, listener);
    }
    
    /**
     * 发布一条新微博（连续两次发布的微博不可以重复）。
     * 
     * @param content  要发布的微博文本内容，内容不超过140个汉字。
     * @param lat      纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
     * @param lon      经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
     * @param listener 异步请求回调接口
     */
    public void update(String content, String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        requestAsync(sAPIList.get(WRITE_API_UPDATE), params, HTTPMETHOD_POST, listener);
    }
    
    /**
     * 上传图片并发布一条新微博。
     * 
     * @param content  要发布的微博文本内容，内容不超过140个汉字
     * @param bitmap   要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M
     * @param lat      纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
     * @param lon      经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
     * @param listener 异步请求回调接口
     */
    public void upload(String content, Bitmap bitmap, String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        params.put("pic", bitmap);
        requestAsync(sAPIList.get(WRITE_API_UPLOAD), params, HTTPMETHOD_POST, listener);
    }
    
    /**
     * 指定一个图片URL地址抓取后上传并同时发布一条新微博，此方法会处理URLencod。
     * 
     * @param status   要发布的微博文本内容，内容不超过140个汉字。
     * @param imageUrl 图片的URL地址，必须以http开头。
     * @param pic_id   已经上传的图片pid，多个时使用英文半角逗号符分隔，最多不超过九张。 
     *                 imageUrl 和 pic_id必选一个，两个参数都存在时，取picid参数的值为准。
     *                 <b>注：目前该参数不可用，现在还只能通过BD合作接入，不对个人申请</b>
     * @param lat      纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
     * @param lon      经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
     * @param listener 异步请求回调接口
     */
    public void uploadUrlText(String status, String imageUrl, String pic_id, String lat, String lon,
            RequestListener listener) {
        WeiboParameters params = buildUpdateParams(status, lat, lon);
        params.put("url", imageUrl);
        params.put("pic_id", pic_id);
        requestAsync(sAPIList.get(WRITE_API_UPLOAD_URL_TEXT), params, HTTPMETHOD_POST, listener);
    }
    
    /**
     * @see #friendsTimeline(long, long, int, int, boolean, int, boolean, RequestListener)
     */
    public String friendsTimelineSync(long since_id, long max_id, int count, int page, boolean base_app, int featureType,
            boolean trim_user) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page, base_app,
                trim_user, featureType);
        return requestSync(sAPIList.get(READ_API_FRIENDS_TIMELINE), params, HTTPMETHOD_GET);
    }

    /**
     * -----------------------------------------------------------------------
     * 请注意：以下方法匀均同步方法。如果开发者有自己的异步请求机制，请使用该函数。
     * -----------------------------------------------------------------------
     */
    
    /**
     * @see #mentions(long, long, int, int, int, int, int, boolean, RequestListener)
     */
    public String mentionsSync(long since_id, long max_id, int count, int page,
            int authorType, int sourceType, int filterType, boolean trim_user) {
        WeiboParameters params = buildMentionsParams(since_id, max_id, count, page, authorType, sourceType, filterType, trim_user);
        return requestSync(sAPIList.get(READ_API_MENTIONS), params, HTTPMETHOD_GET);
    }

    /**
     * @see #update(String, String, String, RequestListener)
     */
    public String updateSync(String content, String lat, String lon) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        return requestSync(sAPIList.get(WRITE_API_UPDATE), params, HTTPMETHOD_POST);
    }

    /**
     * @see #upload(String, Bitmap, String, String, RequestListener)
     */
    public String uploadSync(String content, Bitmap bitmap, String lat, String lon) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        params.put("pic", bitmap);
        return requestSync(sAPIList.get(WRITE_API_UPLOAD), params, HTTPMETHOD_POST);
    }

    /**
     * @see #uploadUrlText(String, String, String, String, String, RequestListener)
     */
    public String uploadUrlTextSync(String status, String imageUrl, String pic_id, String lat, String lon) {
        WeiboParameters params = buildUpdateParams(status, lat, lon);
        params.put("url", imageUrl);
        params.put("pic_id", pic_id);
        return requestSync(sAPIList.get(WRITE_API_UPLOAD_URL_TEXT), params, HTTPMETHOD_POST);
    }

    // 组装TimeLines的参数
    private WeiboParameters buildTimeLineParamsBase(long since_id, long max_id, int count, int page,
            boolean base_app, boolean trim_user, int featureType) {
        WeiboParameters params = new WeiboParameters();
        params.put("since_id", since_id);
        params.put("max_id", max_id);
        params.put("count", count);
        params.put("page", page);
        params.put("base_app", base_app ? 1 : 0);
        params.put("trim_user", trim_user ? 1 : 0);
        params.put("feature", featureType);
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
    
    private WeiboParameters buildMentionsParams(long since_id, long max_id, int count, int page,
            int authorType, int sourceType, int filterType, boolean trim_user) {
        WeiboParameters params = new WeiboParameters();
        params.put("since_id", since_id);
        params.put("max_id", max_id);
        params.put("count", count);
        params.put("page", page);
        params.put("filter_by_author", authorType);
        params.put("filter_by_source", sourceType);
        params.put("filter_by_type", filterType);
        params.put("trim_user", trim_user ? 1 : 0);
        
        return params;
    } 
}
