package com.sina.weibo.sdk.openapi.legacy;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;
/**
 * 该类封装了微博的注册接口，详情请参考<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E6.B3.A8.E5.86.8C">注册接口</a>
 * @author xiaowei6@staff.sina.com.cn
 *
 */
public class RegisterAPI extends WeiboAPI {
	public RegisterAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/register";

	/**
	 * 验证昵称是否可用
	 * 
	 * @param nickname 需要验证的昵称。4-20个字符，支持中英文、数字、"_"或减号。
	 * @param listener
	 */
	public void suggestions( String nickname, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("nickname", nickname);
		request( SERVER_URL_PRIX + "/verify_nickname.json", params, HTTPMETHOD_GET, listener);
	}

}
