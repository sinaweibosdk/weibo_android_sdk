package com.sina.weibo.sdk.openapi.legacy;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;
/**
 * 该类封装了微博的短链接接口，详情请参考<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E7.9F.AD.E9.93.BE">短链接接口</a>
 * @author xiaowei6@staff.sina.com.cn
 */
public class ShortUrlAPI extends WeiboAPI {
	public ShortUrlAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/short_url";

	/**
	 * 将一个或多个长链接转换成短链接
	 * 
	 * @param url_long 需要转换的长链接，最多不超过20个。
	 * @param listener
	 */
	public void shorten( String[] url_long, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		if(url_long!=null){
		    int length=url_long.length;
            for (int i=0;i<length;i++) {
                params.add("url_long", url_long[i]);
            }
		}
		
		request( SERVER_URL_PRIX + "/shorten.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 将一个或多个短链接还原成原始的长链接
	 * 
	 * @param url_short 需要还原的短链接，最多不超过20个 。
	 * @param listener
	 */
	public void expand( String[] url_short, RequestListener listener) {
	    WeiboParameters params = new WeiboParameters();
		if(url_short!=null){
		    int length=url_short.length;
		    for (int i=0;i<length;i++) {
                params.add("url_short", url_short[i]);
            }
		}
		
		request( SERVER_URL_PRIX + "/expand.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取短链接的总点击数
	 * 
	 * @param url_short 需要取得点击数的短链接，最多不超过20个。
	 * @param listener
	 */
	public void clicks( String[] url_short, RequestListener listener) {
	    WeiboParameters params = new WeiboParameters();
		if(url_short!=null){
            int length=url_short.length;
            for (int i=0;i<length;i++) {
                params.add("url_short", url_short[i]);
            }
        }
		request( SERVER_URL_PRIX + "/clicks.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取一个短链接点击的referer来源和数量
	 * 
	 * @param url_short 需要取得点击来源的短链接
	 * @param listener
	 */
	public void referers( String url_short, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("url_short", url_short);
		request( SERVER_URL_PRIX + "/referers.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取一个短链接点击的地区来源和数量
	 * 
	 * @param url_short 需要取得点击来源的短链接
	 * @param listener
	 */
	public void locations( String url_short, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("url_short", url_short);
		request( SERVER_URL_PRIX + "/locations.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取短链接在微博上的微博分享数
	 * 
	 * @param url_short 需要取得分享数的短链接，最多不超过20个。
	 * @param listener
	 */
	public void shareCounts( String[] url_short, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		if(url_short!=null){
            int length=url_short.length;
            for (int i=0;i<length;i++) {
                params.add("url_short", url_short[i]);
            }
        }
		request( SERVER_URL_PRIX + "/share/counts.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取包含指定单个短链接的最新微博内容
	 * 
	 * @param url_short 需要取得关联微博内容的短链接
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，默认为50，最多不超过200。
	 * @param page 返回结果的页码，默认为1。
	 * @param listener
	 */
	public void shareStatuses( String url_short, long since_id, long max_id, int count,
			int page, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("url_short", url_short);
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		request( SERVER_URL_PRIX + "/share/statuses.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取短链接在微博上的微博评论数
	 * 
	 * @param url_short 需要取得分享数的短链接，最多不超过20个。
	 * @param listener
	 */
	public void commentCounts( String[] url_short, RequestListener listener) {
	    WeiboParameters params = new WeiboParameters();
		if(url_short!=null){
            int length=url_short.length;
            for (int i=0;i<length;i++) {
                params.add("url_short", url_short[i]);
            }
        }
		request( SERVER_URL_PRIX + "/comment/counts.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取包含指定单个短链接的最新微博评论
	 * 
	 * @param url_short 需要取得关联微博评论内容的短链接
	 * @param since_id 若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
	 * @param count 单页返回的记录条数，默认为50，最多不超过200。
	 * @param page 返回结果的页码，默认为1。
	 * @param listener
	 */
	public void comments( String url_short, long since_id, long max_id, int count,
			int page, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("url_short", url_short);
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		request( SERVER_URL_PRIX + "/comment/comments.json", params, HTTPMETHOD_GET, listener);
	}


}
