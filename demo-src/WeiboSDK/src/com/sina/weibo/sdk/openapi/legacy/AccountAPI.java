package com.sina.weibo.sdk.openapi.legacy;

import android.text.TextUtils;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;
/**
 * 此类封装了账号的接口，详情见<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E8.B4.A6.E5.8F.B7">账号接口</a>
 * @author xiaowei6@staff.sina.com.cn
 *
 */
public class AccountAPI extends WeiboAPI {
    private static final String SERVER_URL_PRIX = API_SERVER + "/account";
	public AccountAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    

	/**
	 * 获取当前登录用户的隐私设置
	 * @param listener
	 */
	public void getPrivacy(RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		request( SERVER_URL_PRIX + "/get_privacy.json", params, HTTPMETHOD_GET,listener);
	}

	/**
	 * 获取所有的学校列表(参数keyword与capital二者必选其一，且只能选其一
	 * 按首字母capital查询时，必须提供province参数)
	 * @param province 省份范围，省份ID。
	 * @param city 城市范围，城市ID
	 * @param area 区域范围，区ID。
	 * @param type 学校类型，1：大学、2：高中、3：中专技校、4：初中、5：小学，默认为1。
	 * @param capital 学校首字母，默认为A。
	 * @param keyword 学校名称关键字
	 * @param count 返回的记录条数，默认为10。
	 * @param listener
	 */
	public void schoolList( int province, int city, int area, SCHOOL_TYPE type,
			CAPITAL capital, String keyword, int count,RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("province", province);
		params.add("city", city);
		params.add("area", area);
		params.add("type", type.ordinal()+1);
		if(!TextUtils.isEmpty(capital.name())){
			params.add("capital", capital.name());
		}else if(!TextUtils.isEmpty(keyword)){
			params.add("keyword", keyword);
		}
		params.add("count", count);
		request( SERVER_URL_PRIX + "/profile/school_list.json", params, HTTPMETHOD_GET,listener);
	}
	
	/**
     * 获取当前登录用户的API访问频率限制情况
     * @param listener
     */
    public void rateLimitStatus(RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        request(SERVER_URL_PRIX + "/rate_limit_status.json", params, HTTPMETHOD_GET,listener);
    }
   
    
    /**
     * OAuth授权之后，获取授权用户的UID
     * @param listener
     */
    public void getUid(RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
        request( SERVER_URL_PRIX + "/get_uid.json", params, HTTPMETHOD_GET,listener);
    }
	/**
	 * 退出登录
	 * @param listener
	 */
	public void endSession(RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		request( SERVER_URL_PRIX + "/end_session.json", params, HTTPMETHOD_POST,listener);
	}
}
