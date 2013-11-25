package com.sina.weibo.sdk.openapi.legacy;

import android.text.TextUtils;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;
/**
 * 该类封装了微博接口，详情请参考<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E5.BE.AE.E5.8D.9A">微博接口</a>
 * @author xiaowei6@staff.sina.com.cn
 */
public class StatusesAPI extends WeiboAPI {
	public StatusesAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }
	

    private static final String SERVER_URL_PRIX = API_SERVER + "/statuses";

	/**
	 * 返回最新的公共微博
	 * 
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void publicTimeline( int count, int page, boolean base_app,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/public_timeline.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取当前登录用户及其所关注用户的最新微博
	 * 
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param feature 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @param trim_user 返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
	 * @param listener
	 */
	public void friendsTimeline( long since_id, long max_id, int count, int page,
			boolean base_app, FEATURE feature, boolean trim_user, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		params.add("feature", feature.ordinal());
		if (trim_user) {
			params.add("trim_user", 1);
		} else {
			params.add("trim_user", 0);
		}
		request( SERVER_URL_PRIX + "/friends_timeline.json", params, HTTPMETHOD_GET, listener);
	}
	/**
	 * 获取当前登录用户及其所关注用户的最新微博的ID
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param feature 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @param listener
	 */
	public void friendsTimelineIds(long since_id, long max_id, int count, int page,boolean base_app, FEATURE feature, RequestListener listener){
	    WeiboParameters params = new WeiboParameters();
        params.add("since_id", since_id);
        params.add("max_id", max_id);
        params.add("count", count);
        params.add("page", page);
        if (base_app) {
            params.add("base_app", 1);
        } else {
            params.add("base_app", 0);
        }
        params.add("feature", feature.ordinal());
        request( SERVER_URL_PRIX + "/friends_timeline/ids.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取当前登录用户及其所关注用户的最新微博
	 * 
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param feature 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @param trim_user 返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
	 * @param listener
	 */
	public void homeTimeline( long since_id, long max_id, int count, int page,
			boolean base_app, FEATURE feature, boolean trim_user, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		params.add("feature", feature.ordinal());
		if (trim_user) {
			params.add("trim_user", 1);
		} else {
			params.add("trim_user", 0);
		}
		request( SERVER_URL_PRIX + "/home_timeline.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取某个用户最新发表的微博列表
	 * 
	 * @param uid 需要查询的用户ID。
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param feature 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @param trim_user 返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
	 * @param listener
	 */
	public void userTimeline( long uid, long since_id, long max_id, int count, int page,
			boolean base_app, FEATURE feature, boolean trim_user, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", uid);
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 01);
		} else {
			params.add("base_app", 0);
		}
		params.add("feature", feature.ordinal());
		if (trim_user) {
			params.add("trim_user", 1);
		} else {
			params.add("trim_user", 0);
		}
		request( SERVER_URL_PRIX + "/user_timeline.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取某个用户最新发表的微博列表
	 * 
	 * @param screen_name 需要查询的用户昵称。
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param feature 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @param trim_user 返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
	 * @param listener
	 */
	public void userTimeline( String screen_name, long since_id, long max_id, int count,
			int page, boolean base_app, FEATURE feature, boolean trim_user, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("screen_name", screen_name);
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		params.add("feature", feature.ordinal());
		if (trim_user) {
			params.add("trim_user", 1);
		} else {
			params.add("trim_user", 0);
		}
		request( SERVER_URL_PRIX + "/user_timeline.json", params, HTTPMETHOD_GET, listener);
	}
	
	/**
	 * 获取当前用户最新发表的微博列表
	 * 
	 * @param screen_name 需要查询的用户昵称。
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param feature 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @param trim_user 返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
	 * @param listener
	 */
	public void userTimeline( long since_id, long max_id, int count,
			int page, boolean base_app, FEATURE feature, boolean trim_user, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		params.add("feature", feature.ordinal());
		if (trim_user) {
			params.add("trim_user", 1);
		} else {
			params.add("trim_user", 0);
		}
		request( SERVER_URL_PRIX + "/user_timeline.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取用户发布的微博的ID
	 * 
	 * @param uid 需要查询的用户ID。
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param feature 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @param listener
	 */
	public void userTimelineIds( long uid, long since_id, long max_id, int count,
			int page, boolean base_app, FEATURE feature, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", uid);
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		params.add("feature", feature.ordinal());
		request( SERVER_URL_PRIX + "/user_timeline/ids.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 获取用户发布的微博的ID
	 * 
	 * @param screen_name 需要查询的用户昵称。
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param feature 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @param listener
	 */
	public void userTimelineIds( String screen_name, long since_id, long max_id,
			int count, int page, boolean base_app, FEATURE feature,	RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("screen_name", screen_name);
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		params.add("feature", feature.ordinal());
		request( SERVER_URL_PRIX + "/user_timeline/ids.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 获取指定微博的转发微博列表
	 * 
	 * @param id 需要查询的微博ID。
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param filter_by_author 作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
	 * @param listener
	 */
	public void repostTimeline( long id, long since_id, long max_id, int count, int page,
			AUTHOR_FILTER filter_by_author, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("id", id);
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		params.add("filter_by_author", filter_by_author.ordinal());
		request( SERVER_URL_PRIX + "/repost_timeline.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取一条原创微博的最新转发微博的ID
	 * 
	 * @param id 需要查询的微博ID。
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param filter_by_author 作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
	 * @param listener
	 */
	public void repostTimelineIds( long id, long since_id, long max_id, int count,
			int page, AUTHOR_FILTER filter_by_author, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("id", id);
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		params.add("filter_by_author", filter_by_author.ordinal());
		request( SERVER_URL_PRIX + "/repost_timeline/ids.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 获取当前用户最新转发的微博列表
	 * 
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param listener
	 */
	public void repostByMe( long since_id, long max_id, int count, int page,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		request( SERVER_URL_PRIX + "/repost_by_me.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取最新的提到登录用户的微博列表，即@我的微博
	 * 
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param filter_by_author 作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
	 * @param filter_by_source 来源筛选类型，0：全部、1：来自微博、2：来自微群，默认为0。
	 * @param filter_by_type 原创筛选类型，0：全部微博、1：原创的微博，默认为0。
	 * @param trim_user 返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
	 * @param listener
	 */
	public void mentions( long since_id, long max_id, int count, int page,
			AUTHOR_FILTER filter_by_author, SRC_FILTER filter_by_source,
			TYPE_FILTER filter_by_type, boolean trim_user, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		params.add("filter_by_author", filter_by_author.ordinal());
		params.add("filter_by_source", filter_by_source.ordinal());
		params.add("filter_by_type", filter_by_type.ordinal());
		if (trim_user) {
			params.add("trim_user", 1);
		} else {
			params.add("trim_user", 0);
		}
		request( SERVER_URL_PRIX + "/mentions.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取@当前用户的最新微博的ID
	 * 
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param filter_by_author 作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
	 * @param filter_by_source 来源筛选类型，0：全部、1：来自微博、2：来自微群，默认为0。
	 * @param filter_by_type 原创筛选类型，0：全部微博、1：原创的微博，默认为0。
	 * @param listener
	 */
	public void mentionsIds( long since_id, long max_id, int count, int page,
			AUTHOR_FILTER filter_by_author, SRC_FILTER filter_by_source,
			TYPE_FILTER filter_by_type, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		params.add("filter_by_author", filter_by_author.ordinal());
		params.add("filter_by_source", filter_by_source.ordinal());
		params.add("filter_by_type", filter_by_type.ordinal());
		request( SERVER_URL_PRIX + "/mentions/ids.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取双向关注用户的最新微博
	 * 
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param feature 过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @param trim_user 返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
	 * @param listener
	 */
	public void bilateralTimeline(  long since_id, long max_id,
			int count, int page, boolean base_app, FEATURE feature, boolean trim_user,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		params.add("feature", feature.ordinal());
		if (trim_user) {
			params.add("trim_user", 1);
		} else {
			params.add("trim_user", 0);
		}
		request( SERVER_URL_PRIX + "/bilateral_timeline.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 根据微博ID获取单条微博内容
	 * 
	 * @param id 需要获取的微博ID。
	 * @param listener
	 */
	public void show( long id, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("id", id);
		request( SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 通过微博（评论、私信）ID获取其MID
	 * 
	 * @param ids 需要查询的微博（评论、私信）ID，最多不超过20个。
	 * @param type 获取类型，1：微博、2：评论、3：私信，默认为1。
	 * @param listener
	 */
	public void queryMID( long[] ids, TYPE type, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		if(1==ids.length){
			params.add("id", ids[0]);
		}else{
			params.add("is_batch", 1);
			StringBuilder strb = new StringBuilder();
			for(long id: ids){
				strb.append(id).append(",");
			}
			strb.deleteCharAt(strb.length()-1);
			params.add("id", strb.toString());
		}
		
		params.add("type", type.ordinal());
		request( SERVER_URL_PRIX + "/querymid.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 通过微博（评论、私信）MID获取其ID,形如“3z4efAo4lk”的MID即为经过base62转换的MID
	 * 
	 * @param mids 需要查询的微博（评论、私信）MID，最多不超过20个。
	 * @param type 获取类型，1：微博、2：评论、3：私信，默认为1。
	 * @param inbox 仅对私信有效，当MID类型为私信时用此参数，0：发件箱、1：收件箱，默认为0 。
	 * @param isBase62 MID是否是base62编码，0：否、1：是，默认为0。
	 * @param listener
	 */
	public void queryID( String[] mids, TYPE type, boolean inbox, boolean isBase62,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		if(mids!=null){
		    if(1 == mids.length){
	            params.add("mid", mids[0]);
	        }else{
	            params.add("is_batch", 1);
	            StringBuilder strb = new StringBuilder();
	            for(String mid: mids){
	                strb.append(mid).append(",");
	            }
	            strb.deleteCharAt(strb.length()-1);
	            params.add("mid", strb.toString());
	        }
		}
		
		params.add("type", type.ordinal());
		if (inbox) {
			params.add("inbox", 0);
		} else {
			params.add("inbox", 1);
		}
		if (isBase62) {
			params.add("isBase62", 0);
		} else {
			params.add("isBase62", 1);
		}
		request( SERVER_URL_PRIX + "/queryid.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 按天返回热门微博转发榜的微博列表
	 * 
	 * @param count 返回的记录条数，最大不超过50，默认为20。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void hotRepostDaily( int count, boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("count", count);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/hot/repost_daily.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 按周返回热门微博转发榜的微博列表,
	 * 
	 * @param count 返回的记录条数，最大不超过50，默认为20。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void hotRepostWeekly( int count, boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("count", count);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/hot/repost_weekly.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 按天返回热门微博评论榜的微博列表
	 * 
	 * @param count 返回的记录条数，最大不超过50，默认为20。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void hotCommentsDaily( int count, boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("count", count);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/hot/comments_daily.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 按周返回热门微博评论榜的微博列表
	 * 
	 * @param count 返回的记录条数，最大不超过50，默认为20。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void hotCommentsWeekly( int count, boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("count", count);
		if (base_app) {
			params.add("base_app", 0);
		} else {
			params.add("base_app", 1);
		}
		request( SERVER_URL_PRIX + "/hot/comments_weekly.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 批量获取指定微博的转发数评论数
	 * 
	 * @param ids 需要获取数据的微博ID，最多不超过100个。
	 * @param listener
	 */
	public void count( String[] ids, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		StringBuilder strb = new StringBuilder();
		for (String id : ids) {
			strb.append(id).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.add("ids", strb.toString());
		request( SERVER_URL_PRIX + "/count.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 转发一条微博
	 * 
	 * @param id 要转发的微博ID。
	 * @param status 添加的转发文本，内容不超过140个汉字，不填则默认为“转发微博”。
	 * @param is_comment 是否在转发的同时发表评论，0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0
	 * @param listener
	 */
	public void repost( long id, String status, COMMENTS_TYPE is_comment,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("id", id);
		params.add("status", status);
		params.add("is_comment", is_comment.ordinal());
		request( SERVER_URL_PRIX + "/repost.json", params, HTTPMETHOD_POST, listener);
	}

	/**
	 * 根据微博ID删除指定微博
	 * 
	 * @param id 需要删除的微博ID。
	 * @param listener
	 */
	public void destroy( long id, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("id", id);
		request( SERVER_URL_PRIX + "/destroy.json", params, HTTPMETHOD_POST, listener);
	}

	/**
	 * 发布一条新微博(连续两次发布的微博不可以重复)
	 * 
	 * @param content 要发布的微博文本内容，内容不超过140个汉字。
	 * @param lat 纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
	 * @param lon 经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
	 * @param listener
	 */
	public void update( String content, String lat, String lon, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("status", content);
		if (!TextUtils.isEmpty(lon)) {
			params.add("long", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			params.add("lat", lat);
		}
		request( SERVER_URL_PRIX + "/update.json", params, HTTPMETHOD_POST, listener);
	}

	/**
	 * 上传图片并发布一条新微博，此方法会处理urlencode
	 * @param content 要发布的微博文本内容，内容不超过140个汉字
	 * @param file 要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M。
	 * @param lat 纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
	 * @param lon 经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
	 * @param listener
	 */
	public void upload( String content, String file, String lat, String lon,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("status", content);
		params.add("pic", file);
		if (!TextUtils.isEmpty(lon)) {
			params.add("long", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			params.add("lat", lat);
		}
		
		request( SERVER_URL_PRIX + "/upload.json", params, HTTPMETHOD_POST, listener);
	}
	
	/**
	 * 上传图片，此方法会处理urlencode
	 * @param content 要发布的微博文本内容，内容不超过140个汉字
	 * @param file 要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M。
	 * @param listener
	 */
	public void uploadPic( String content, String filePath,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("pic", filePath);

		request( SERVER_URL_PRIX + "/upload_pic.json", params, HTTPMETHOD_POST, listener);
	}

	
	/**
	 * 指定一个图片URL地址抓取后上传并同时发布一条新微博，此方法会处理URLencode
	 * 
	 * @param status 要发布的微博文本内容，内容不超过140个汉字。
	 * @param imageUrl 图片的URL地址，必须以http开头。
	 * @param pic_id 已经上传的图片pid，多个时使用英文半角逗号符分隔，最多不超过九张。
	 * imageUrl 和 pic_id必选一个，两个参数都存在时，取picid参数的值为准。
	 * @param lat 纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
	 * @param lon 经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
	 * @param listener
	 */
	public void uploadUrlText( String status, String imageUrl, String pic_id,String lat, String lon,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("status", status);
		params.add("url", imageUrl);
		params.add("pic_id", pic_id);
		if (!TextUtils.isEmpty(lon)) {
			params.add("long", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			params.add("lat", lat);
		}
		request( SERVER_URL_PRIX + "/upload_url_text.json", params, HTTPMETHOD_POST, listener);
	}

	
	
	/**
	 * 获取微博官方表情的详细信息
	 * 
	 * @param type 表情类别，face：普通表情、ani：魔法表情、cartoon：动漫表情，默认为face。
	 * @param language 语言类别，cnname：简体、twname：繁体，默认为cnname。
	 * @param listener
	 */
	public void emotions( EMOTION_TYPE type, LANGUAGE language, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("type", type.name());
		params.add("language", language.name());
		request( API_SERVER + "/emotions.json", params, HTTPMETHOD_GET, listener);
	}

}
