package com.sina.weibo.sdk.openapi.legacy;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;
/**
 * 该类封装了话题接口，详情请参考<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E8.AF.9D.E9.A2.98">话题接口</a>
 * @author xiaowei6@staff.sina.com.cn
 */
public class TrendsAPI extends WeiboAPI {
	public TrendsAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/trends";

	/**
	 * 获取某人的话题列表
	 * @param uid 需要获取话题的用户的UID。
	 * @param count 单页返回的记录条数，默认为10。
	 * @param page 返回结果的页码，默认为1。
	 * @param listener
	 */
	public void trends( long uid, int count, int page, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", uid);
		params.add("count", count);
		params.add("page", page);
		request( SERVER_URL_PRIX + ".json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 判断当前用户是否关注某话题
	 * @param trend_name 话题关键字
	 * @param listener
	 */
	public void isFollow( String trend_name, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("trend_name", trend_name);
		request( SERVER_URL_PRIX + "/is_follow.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 返回最近一小时内的热门话题
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void hourly( boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/hourly.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 返回最近一天内的热门话题
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void daily( boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/daily.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 返回最近一周内的热门话题
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void weekly( boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		if (base_app) {
			params.add("base_app", 0);
		} else {
			params.add("base_app", 1);
		}
		request( SERVER_URL_PRIX + "/weekly.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 关注某话题
	 * @param trend_name 要关注的话题关键词。
	 * @param listener
	 */
	public void follow( String trend_name, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("trend_name", trend_name);
		request( SERVER_URL_PRIX + "/follow.json", params, HTTPMETHOD_POST, listener);
	}

	/**
	 * 取消对某话题的关注
	 * @param trend_id 要取消关注的话题ID。
	 * @param listener
	 */
	public void destroy( long trend_id, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("trend_id", trend_id);
		request( SERVER_URL_PRIX + "/destroy.json", params, HTTPMETHOD_POST, listener);
	}
}
