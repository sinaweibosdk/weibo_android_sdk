package com.sina.weibo.sdk.openapi.legacy;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;
/**
 * 此类封装了收藏的接口，详情见<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E6.94.B6.E8.97.8F">公共接口</a>
 * @author xiaowei6@staff.sina.com.cn
 *
 */
public class FavoritesAPI extends WeiboAPI {
	public FavoritesAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/favorites";

	/**
	 * 获取当前登录用户的收藏列表
	 * 
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param listener
	 */
	public void favorites(int count, int page, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("count", count);
		params.add("page", page);
		request( SERVER_URL_PRIX + ".json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取当前用户的收藏列表的ID
	 * 
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1
	 * @param listener
	 */
	public void ids( int count, int page, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("count", count);
		params.add("page", page);
		request( SERVER_URL_PRIX + "/ids.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 根据收藏ID获取指定的收藏信息
	 * 
	 * @param id 需要查询的收藏ID。
	 * @param listener
	 */
	public void show(long id, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("id", id);
		request( SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 根据标签获取当前登录用户该标签下的收藏列表
	 * 
	 * @param tid 需要查询的标签ID。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param listener
	 */
	public void byTags(long tid, int count, int page, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("tid", tid);
		params.add("count", count);
		params.add("page", page);
		request( SERVER_URL_PRIX + "/by_tags.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取当前登录用户的收藏标签列表
	 * 
	 * @param count 单页返回的记录条数，默认为10。
	 * @param page 返回结果的页码，默认为1。
	 * @param listener
	 */
	public void tags( int count, int page, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("count", count);
		params.add("page", page);
		request( SERVER_URL_PRIX + "/tags.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取当前用户某个标签下的收藏列表的ID
	 * 
	 * @param tid 需要查询的标签ID。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param listener
	 */
	public void byTagsIds( long tid, int count, int page, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("tid", tid);
		params.add("count", count);
		params.add("page", page);
		request( SERVER_URL_PRIX + "/by_tags/ids.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 添加一条微博到收藏里
	 * 
	 * @param id 要收藏的微博ID。
	 * @param listener
	 */
	public void create( long id, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("id", id);
		request( SERVER_URL_PRIX + "/create.json", params, HTTPMETHOD_POST, listener);
	}

	/**
	 * 取消收藏一条微博
	 * 
	 * @param id 要取消收藏的微博ID。
	 * @param listener
	 */
	public void destroy( long id, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("id", id);
		request( SERVER_URL_PRIX + "/destroy.json", params, HTTPMETHOD_POST, listener);
	}

	/**
	 * 根据收藏ID批量取消收藏
	 * 
	 * @param ids 要取消收藏的收藏ID，最多不超过10个。
	 * @param listener
	 */
	public void destroyBatch( long[] ids, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		StringBuilder strb = new StringBuilder();
		for (long id : ids) {
			strb.append(String.valueOf(id)).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.add("ids", strb.toString());
		request( SERVER_URL_PRIX + "/destroy_batch.json", params, HTTPMETHOD_POST, listener);
	}

	/**
	 * 更新一条收藏的收藏标签
	 * 
	 * @param id 需要更新的收藏ID。
	 * @param tags 需要更新的标签内容，最多不超过2条。
	 * @param listener
	 */
	public void tagsUpdate( long id, String[] tags, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("id", id);
		StringBuilder strb = new StringBuilder();
		for (String tag : tags) {
			strb.append(tag).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.add("tags", strb.toString());
		request( SERVER_URL_PRIX + "/tags/update.json", params, HTTPMETHOD_POST, listener);
	}
	
	/**
	 * 更新当前登录用户所有收藏下的指定标签
	 * 
	 * @param id 需要更新的标签ID。
	 * @param tag 需要更新的标签内容
	 * @param listener
	 */
	public void tagsUpdateBatch( long id, String tag, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("tid", id);
		params.add("tag", tag);
		request( SERVER_URL_PRIX + "/tags/update_batch.json", params, HTTPMETHOD_POST, listener);
	}
	
	/**
	 * 删除当前登录用户所有收藏下的指定标签
	 * 
	 * @param tid 需要删除的标签ID。
	 * @param listener
	 */
	public void tagsDestroyBatch( long tid, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("tid", tid);
		request( SERVER_URL_PRIX + "/tags/destroy_batch.json", params, HTTPMETHOD_POST, listener);
	}
}
