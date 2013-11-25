package com.sina.weibo.sdk.openapi.legacy;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;
/**
 * 该类封装了用户接口，详情请参考<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E7.94.A8.E6.88.B7">用户接口</a>
 * @author xiaowei6@staff.sina.com.cn
 */
public class UsersAPI extends WeiboAPI {
	public UsersAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/users";

	/**
	 * 根据用户ID获取用户信息
	 * @param uid 需要查询的用户ID。
	 * @param listener
	 */
	public void show( long uid, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", uid);
		request( SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 根据用户ID获取用户信息
	 * @param screen_name 需要查询的用户昵称。
	 * @param listener
	 */
	public void show( String screen_name, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("screen_name", screen_name);
		request( SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 通过个性化域名获取用户资料以及用户最新的一条微博
	 * @param domain 需要查询的个性化域名。
	 * @param listener
	 */
	public void domainShow( String domain, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("domain", domain);
		request( SERVER_URL_PRIX + "/domain_show.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 批量获取用户的粉丝数、关注数、微博数
	 * @param uids 需要获取数据的用户UID，多个之间用逗号分隔，最多不超过100个。
	 * @param listener
	 */
	public void counts( long[] uids, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		StringBuilder strb = new StringBuilder();
		for (long cid : uids) {
			strb.append(String.valueOf(cid)).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.add("uids", strb.toString());
		request( SERVER_URL_PRIX + "/counts.json", params, HTTPMETHOD_GET, listener);
	}

}
