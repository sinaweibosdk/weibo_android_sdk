package com.sina.weibo.sdk.openapi.legacy;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;
/**
 * 该类封装了微博的搜索接口，详情请参考<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E6.90.9C.E7.B4.A2">搜索接口</a>
 * @author xiaowei6@staff.sina.com.cn
 */
public class SearchAPI extends WeiboAPI {
	public SearchAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/search";

	/**
	 * 搜索用户时的联想搜索建议
	 * 
	 * @param q 搜索的关键字，必须做URLencoding。
	 * @param count 返回的记录条数，默认为10。
	 * @param listener
	 */
	public void users( String q, int count, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("q", q);
		params.add("count", count);
		request( SERVER_URL_PRIX + "/suggestions/users.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 搜索微博时的联想搜索建议
	 * 
	 * @param q 搜索的关键字，必须做URLencoding。
	 * @param count 返回的记录条数，默认为10。
	 * @param listener
	 */
	public void statuses( String q, int count, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("q", q);
		params.add("count", count);
		request( SERVER_URL_PRIX + "/suggestions/statuses.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 搜索学校时的联想搜索建议
	 * 
	 * @param q 搜索的关键字，必须做URLencoding。
	 * @param count 返回的记录条数，默认为10。
	 * @param type 学校类型，0：全部、1：大学、2：高中、3：中专技校、4：初中、5：小学，默认为0。
	 * @param listener
	 */
	public void schools( String q, int count, SCHOOL_TYPE type, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("q", q);
		params.add("count", count);
		params.add("type", type.ordinal());
		request( SERVER_URL_PRIX + "/suggestions/schools.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 搜索公司时的联想搜索建议
	 * 
	 * @param q 搜索的关键字，必须做URLencoding。
	 * @param count 返回的记录条数，默认为10。
	 * @param listener
	 */
	public void companies( String q, int count, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("q", q);
		params.add("count", count);
		request( SERVER_URL_PRIX + "/suggestions/companies.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 搜索应用时的联想搜索建议
	 * 
	 * @param q 搜索的关键字，必须做URLencoding。
	 * @param count 返回的记录条数，默认为10。
	 * @param listener
	 */
	public void apps( String q, int count, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("q", q);
		params.add("count", count);
		request( SERVER_URL_PRIX + "/suggestions/apps.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 *  “@”用户时的联想建议
	 * 
	 * @param q 搜索的关键字，必须做URLencoding。
	 * @param count 返回的记录条数，默认为10，粉丝最多1000，关注最多2000。
	 * @param type 联想类型，0：关注、1：粉丝。
	 * @param range 联想范围，0：只联想关注人、1：只联想关注人的备注、2：全部，默认为2。
	 * @param listener
	 */
	public void atUsers( String q, int count, FRIEND_TYPE type, RANGE range,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("q", q);
		params.add("count", count);
		params.add("type", type.ordinal());
		params.add("range", range.ordinal());
		request( SERVER_URL_PRIX + "/suggestions/at_users.json", params, HTTPMETHOD_GET,
				listener);
	}
}
