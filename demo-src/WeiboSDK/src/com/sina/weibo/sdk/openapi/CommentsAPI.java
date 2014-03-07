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

import android.util.SparseArray;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * 此类封装了评论的接口。
 * 详情请参考<a href="http://t.cn/8F3geol">评论接口</a>
 * 
 * @author SINA
 * @since 2014-03-03
 */
public class CommentsAPI extends AbsOpenAPI {

    /** 作者筛选类型，0：全部、1：我关注的人、2：陌生人 */
    public static final int AUTHOR_FILTER_ALL        = 0;
    public static final int AUTHOR_FILTER_ATTENTIONS = 1;
    public static final int AUTHOR_FILTER_STRANGER   = 2;

    /** 来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论 */
    public static final int SRC_FILTER_ALL    = 0;
    public static final int SRC_FILTER_WEIBO  = 1;
    public static final int SRC_FILTER_WEIQUN = 2;

    /**
     * API 类型。
     * 命名规则：
     *      <li>读取接口：READ_API_XXX
     *      <li>写入接口：WRITE_API_XXX
     * 请注意：该类中的接口仅做为演示使用，并没有包含所有关于微博的接口，第三方开发者可以
     * 根据需要来填充该类，可参考legacy包下 {@link com.sina.weibo.sdk.openapi.legacy.CommentsAPI}
     */
    private static final int READ_API_TO_ME           = 0;
    private static final int READ_API_BY_ME           = 1;
    private static final int READ_API_SHOW            = 2;
    private static final int READ_API_TIMELINE        = 3;
    private static final int READ_API_MENTIONS        = 4;
    private static final int READ_API_SHOW_BATCH      = 5;
    private static final int WRITE_API_CREATE         = 6;
    private static final int WRITE_API_DESTROY        = 7;
    private static final int WRITE_API_SDESTROY_BATCH = 8;
    private static final int WRITE_API_REPLY          = 9;
    
    private static final String API_BASE_URL = API_SERVER + "/comments";
    
    private static final SparseArray<String> sAPIList = new SparseArray<String>();
    static {
        sAPIList.put(READ_API_TO_ME,           API_BASE_URL + "/to_me.json");
        sAPIList.put(READ_API_BY_ME,           API_BASE_URL + "/by_me.json");
        sAPIList.put(READ_API_SHOW,            API_BASE_URL + "/show.json");
        sAPIList.put(READ_API_TIMELINE,        API_BASE_URL + "/timeline.json");
        sAPIList.put(READ_API_MENTIONS,        API_BASE_URL + "/mentions.json");
        sAPIList.put(READ_API_SHOW_BATCH,      API_BASE_URL + "/show_batch.json");
        sAPIList.put(WRITE_API_CREATE,         API_BASE_URL + "/create.json");
        sAPIList.put(WRITE_API_DESTROY,        API_BASE_URL + "/destroy.json");
        sAPIList.put(WRITE_API_SDESTROY_BATCH, API_BASE_URL + "/sdestroy_batch.json");
        sAPIList.put(WRITE_API_REPLY,          API_BASE_URL + "/reply.json");
    }
    
    /**
     * 构造函数，使用各个 API 接口提供的服务前必须先获取 Token。
     * 
     * @param accesssToken 访问令牌
     */
	public CommentsAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    /**
     * 根据微博ID返回某条微博的评论列表。
     * 
     * @param id         需要查询的微博ID。
     * @param since_id   若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
     * @param max_id     若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
     * @param count      单页返回的记录条数，默认为50
     * @param page       返回结果的页码，默认为1。
     * @param authorType 作者筛选类型，0：全部、1：我关注的人、2：陌生人 ,默认为0。可为以下几种 :
     *                   <li>{@link #AUTHOR_FILTER_ALL}
     *                   <li>{@link #AUTHOR_FILTER_ATTENTIONS}
     *                   <li>{@link #AUTHOR_FILTER_STRANGER}
     * @param listener   异步请求回调接口
     */
    public void show(long id, long since_id, long max_id, int count, int page, int authorType, RequestListener listener) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("id", id);
        params.put("filter_by_author", authorType);
        requestAsync(sAPIList.get(READ_API_SHOW), params, HTTPMETHOD_GET, listener);
    }
    
    /**
     * 获取当前登录用户所发出的评论列表。
     * 
     * @param since_id   若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
     * @param max_id     若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
     * @param count      单页返回的记录条数，默认为50。
     * @param page       返回结果的页码，默认为1。
     * @param sourceType 来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论，默认为0。
     *                   <li>{@link #SRC_FILTER_ALL}
     *                   <li>{@link #SRC_FILTER_WEIBO}
     *                   <li>{@link #SRC_FILTER_WEIQUN} 
     * @param listener   异步请求回调接口
     */
    public void byME(long since_id, long max_id, int count, int page, int sourceType, RequestListener listener) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("filter_by_source", sourceType);
        requestAsync(sAPIList.get(READ_API_BY_ME), params, HTTPMETHOD_GET, listener);
    }
    
    /**
     * 获取当前登录用户所接收到的评论列表。
     * 
     * @param since_id   若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
     * @param max_id     若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
     * @param count      单页返回的记录条数，默认为50。
     * @param page       返回结果的页码，默认为1。
     * @param authorType 作者筛选类型，0：全部、1：我关注的人、2：陌生人 ,默认为0。可为以下几种 :
     *                   <li>{@link #AUTHOR_FILTER_ALL}
     *                   <li>{@link #AUTHOR_FILTER_ATTENTIONS}
     *                   <li>{@link #AUTHOR_FILTER_STRANGER}
     * @param sourceType 来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论，默认为0。
     *                   <li>{@link #SRC_FILTER_ALL}
     *                   <li>{@link #SRC_FILTER_WEIBO}
     *                   <li>{@link #SRC_FILTER_WEIQUN}
     * @param listener   异步请求回调接口
     */
    public void toME(long since_id, long max_id, int count, int page, int authorType, int sourceType,
            RequestListener listener) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("filter_by_author", authorType);
        params.put("filter_by_source", sourceType);
        requestAsync(sAPIList.get(READ_API_TO_ME), params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取当前登录用户的最新评论包括接收到的与发出的。
     * 
     * @param since_id  若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
     * @param max_id    若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
     * @param count     单页返回的记录条数，默认为50。
     * @param page      返回结果的页码，默认为1。
     * @param trim_user 返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
     * @param listener  异步请求回调接口
     */
    public void timeline(long since_id, long max_id, int count, int page, boolean trim_user, RequestListener listener) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("trim_user", trim_user ? 1 : 0);
        requestAsync(sAPIList.get(READ_API_TIMELINE), params, HTTPMETHOD_GET, listener);
    }

    /**
     * 获取最新的提到当前登录用户的评论，即@我的评论 若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0
     * 
     * @param since_id   若指定此参数，则返回ID小于或等于max_id的评论，默认为0
     * @param max_id     若指定此参数，则返回ID小于或等于max_id的评论，默认为0
     * @param count      单页返回的记录条数，默认为50
     * @param page       返回结果的页码，默认为1
     * @param authorType 作者筛选类型，0：全部，1：我关注的人， 2：陌生人，默认为0
     *                   <li> {@link #AUTHOR_FILTER_ALL}
     *                   <li> {@link #AUTHOR_FILTER_ATTENTIONS}
     *                   <li> {@link #AUTHOR_FILTER_STRANGER}
     *@param sourceType  来源筛选类型，0：全部，1：来自微博的评论，2：来自微群的评论，默认为0
     *                   <li> {@link #SRC_FILTER_ALL}
     *                   <li> {@link #SRC_FILTER_WEIBO}
     *                   <li> {@link #SRC_FILTER_WEIQUN}
     * @param listener   异步请求回调接口
     */
    public void mentions(long since_id, long max_id, int count, int page, int authorType, int sourceType,
            RequestListener listener) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("filter_by_author", authorType);
        params.put("filter_by_source", sourceType);
        requestAsync(sAPIList.get(READ_API_MENTIONS), params, HTTPMETHOD_GET, listener);
    }
    
    /**
     * 根据评论ID批量返回评论信息。
     * 
     * @param cids      需要查询的批量评论ID数组，最大50
     * @param listener  异步请求回调接口
     */
    public void showBatch(long[] cids, RequestListener listener) {
        WeiboParameters params = buildShowOrDestoryBatchParams(cids);
        requestAsync(sAPIList.get(READ_API_SHOW_BATCH), params, HTTPMETHOD_GET, listener);
    }
    
    /**
     * 对一条微博进行评论。
     * 
     * @param comment     评论内容，内容不超过140个汉字。
     * @param id          需要评论的微博ID。
     * @param comment_ori 当评论转发微博时，是否评论给原微博
     * @param listener    异步请求回调接口
     */
    public void create(String comment, long id, boolean comment_ori, RequestListener listener) {
        WeiboParameters params = buildCreateParams(comment, id, comment_ori);
        requestAsync(sAPIList.get(WRITE_API_CREATE), params, HTTPMETHOD_POST, listener);
    }
    
    /**
     * 删除一条评论。
     * 
     * @param cid      要删除的评论ID，只能删除登录用户自己发布的评论。
     * @param listener 异步请求回调接口
     */
    public void destroy(long cid, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        params.put("cid", cid);
        requestAsync(sAPIList.get(WRITE_API_DESTROY), params, HTTPMETHOD_POST, listener);
    }
    
    /**
     * 根据评论ID批量删除评论。
     * 
     * @param ids      需要删除的评论ID数组，最多20个。
     * @param listener 异步请求回调接口
     */
    public void destroyBatch(long[] ids, RequestListener listener) {
        WeiboParameters params = buildShowOrDestoryBatchParams(ids);
        requestAsync(sAPIList.get(WRITE_API_SDESTROY_BATCH), params, HTTPMETHOD_POST, listener);
    }
    
    /**
     * 回复一条评论。
     * 
     * @param cid             需要回复的评论ID
     * @param id              需要评论的微博ID
     * @param comment         回复评论内容，内容不超过140个汉字
     * @param without_mention 回复中是否自动加入“回复@用户名”，true：是、false：否，默认为false
     * @param comment_ori     当评论转发微博时，是否评论给原微博，false：否、true：是，默认为false
     * @param listener        异步请求回调接口
     */
    public void reply(long cid, long id, String comment, boolean without_mention, boolean comment_ori,
            RequestListener listener) {
        WeiboParameters params = buildReplyParams(cid, id, comment, without_mention, comment_ori);
        requestAsync(sAPIList.get(WRITE_API_REPLY), params, HTTPMETHOD_POST, listener);
    }
    
    /**
     * -----------------------------------------------------------------------
     * 请注意：以下方法匀均同步方法。如果开发者有自己的异步请求机制，请使用该函数。
     * -----------------------------------------------------------------------
     */
    
    /**
     * @see #show(long, long, long, int, int, int, RequestListener)
     */
    public String showSync(long id, long since_id, long max_id, int count, int page, int authorType) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("id", id);
        params.put("filter_by_author", authorType);
        return requestSync(sAPIList.get(READ_API_SHOW), params, HTTPMETHOD_GET);
    }

    /**
     * @see #byME(long, long, int, int, int, RequestListener)
     */
    public String byMESync(long since_id, long max_id, int count, int page, int sourceType) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("filter_by_source", sourceType);
        return requestSync(sAPIList.get(READ_API_BY_ME), params, HTTPMETHOD_GET);
    }

    /**
     * @see #toME(long, long, int, int, int, int, RequestListener)
     */
    public String toMESync(long since_id, long max_id, int count, int page, int authorType, int sourceType) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("filter_by_author", authorType);
        params.put("filter_by_source", sourceType);
        return requestSync(sAPIList.get(READ_API_TO_ME), params, HTTPMETHOD_GET);
    }

    /**
     * @see #timeline(long, long, int, int, boolean, RequestListener)
     */
    public String timelineSync(long since_id, long max_id, int count, int page, boolean trim_user) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("trim_user", trim_user ? 1 : 0);
        return requestSync(sAPIList.get(READ_API_TIMELINE), params, HTTPMETHOD_GET);
    }

    /**
     * @see #mentions(long, long, int, int, int, int, RequestListener)
     */
    public String mentionsSync(long since_id, long max_id, int count, int page, int authorType, int sourceType) {
        WeiboParameters params = buildTimeLineParamsBase(since_id, max_id, count, page);
        params.put("filter_by_author", authorType);
        params.put("filter_by_source", sourceType);
        return requestSync(sAPIList.get(READ_API_MENTIONS), params, HTTPMETHOD_GET);
    }

    /**
     * @see #showBatch(long[], RequestListener)
     */
    public String showBatchSync(long[] cids) {
        WeiboParameters params = buildShowOrDestoryBatchParams(cids);
        return requestSync(sAPIList.get(READ_API_SHOW_BATCH), params, HTTPMETHOD_GET);
    }

    /**
     * @see #create(String, long, boolean, RequestListener)
     */
    public String createSync(String comment, long id, boolean comment_ori) {
        WeiboParameters params = buildCreateParams(comment, id, comment_ori);
        return requestSync(sAPIList.get(WRITE_API_CREATE), params, HTTPMETHOD_POST);
    }

    /**
     * @see #destroyBatch(long[], RequestListener)
     */
    public String destroySync(long cid) {
        WeiboParameters params = new WeiboParameters();
        params.put("cid", cid);
        return requestSync(sAPIList.get(WRITE_API_DESTROY), params, HTTPMETHOD_POST);
    }

    /**
     * @see #destroyBatchSync(long[])
     */
    public String destroyBatchSync(long[] ids) {
        WeiboParameters params = buildShowOrDestoryBatchParams(ids);
        return requestSync(sAPIList.get(WRITE_API_SDESTROY_BATCH), params, HTTPMETHOD_POST);
    }

    /**
     * @see #reply(long, long, String, boolean, boolean, RequestListener)
     */
    public String replySync(long cid, long id, String comment, boolean without_mention, boolean comment_ori) {
        WeiboParameters params = buildReplyParams(cid, id, comment, without_mention, comment_ori);
        return requestSync(sAPIList.get(WRITE_API_REPLY), params, HTTPMETHOD_POST);
    }

    /** 
     * 组装TimeLines的参数
     */
    private WeiboParameters buildTimeLineParamsBase(long since_id, long max_id, int count, int page) {
        WeiboParameters params = new WeiboParameters();
        params.put("since_id", since_id);
        params.put("max_id", max_id);
        params.put("count", count);
        params.put("page", page);
        return params;
    }

    private WeiboParameters buildShowOrDestoryBatchParams(long[] cids) {
        WeiboParameters params = new WeiboParameters();
        StringBuilder strb = new StringBuilder();
        for (long cid : cids) {
            strb.append(cid).append(",");
        }
        strb.deleteCharAt(strb.length() - 1);
        params.put("cids", strb.toString());
        return params;
    }

    private WeiboParameters buildCreateParams(String comment, long id, boolean comment_ori) {
        WeiboParameters params = new WeiboParameters();
        params.put("comment", comment);
        params.put("id", id);
        params.put("comment_ori", comment_ori ? 1: 0);
        return params;
    }

    private WeiboParameters buildReplyParams(long cid, long id, String comment, boolean without_mention,
            boolean comment_ori) {
        WeiboParameters params = new WeiboParameters();
        params.put("cid", cid);
        params.put("id", id);
        params.put("comment", comment);
        params.put("without_mention", without_mention ? 1: 0);
        params.put("comment_ori",     comment_ori ? 1: 0);
        return params;
    }
}
