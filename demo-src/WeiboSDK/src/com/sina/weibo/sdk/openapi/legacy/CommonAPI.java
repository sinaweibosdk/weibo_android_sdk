package com.sina.weibo.sdk.openapi.legacy;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;
/**
 * 此类封装了公共服务的接口，详情见<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E5.85.AC.E5.85.B1.E6.9C.8D.E5.8A.A1">公共服务接口</a>
 * @author xiaowei6@staff.sina.com.cn
 *
 */
public class CommonAPI extends WeiboAPI {
	public CommonAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/common";

	/**
	 * 获取城市列表
	 * 
	 * @param province
	 * @param capital
	 * @param language 返回的语言版本，zh-cn：简体中文、zh-tw：繁体中文、english：英文，默认为zh-cn。
	 * @param listener
	 */
	public void getCity( String province, CAPITAL capital,String language, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("province", province);
	      if(null!=capital){
	          params.add("capital", capital.name().toLowerCase());
	      }
		params.add("language", language);
		request( SERVER_URL_PRIX + "/get_city.json", params, HTTPMETHOD_GET, listener);
	}
		
	/**
     * 获取国家列表
     * @param capital 国家的首字母，a-z，可为空代表返回全部，默认为全部。
     * @param language 返回的语言版本，zh-cn：简体中文、zh-tw：繁体中文、english：英文，默认为zh-cn。
     */
    public void getCountry(CAPITAL capital,String language, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
      if(null!=capital){
          params.add("capital", capital.name().toLowerCase());
      }
        params.add("language", language);
        request( SERVER_URL_PRIX + "/get_country.json", params, HTTPMETHOD_GET, listener);
    }
	
	/**
	 * 获取时区配置表
	 * 
	 * @param language 返回的语言版本，zh-cn：简体中文、zh-tw：繁体中文、english：英文，默认为zh-cn。
	 * @param listener
	 */
	public void getTimezone( String language, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("language", language);
		request( SERVER_URL_PRIX + "/get_timezone.json", params, HTTPMETHOD_GET, listener);
	}
}
