package com.sina.weibo.sdk.openapi.legacy;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;
/**
 * 该类封装了标签接口，详情请参考<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E6.A0.87.E7.AD.BE">标签接口</a>
 * @author xiaowei6@staff.sina.com.cn
 */
public class TagsAPI extends WeiboAPI {
	public TagsAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/tags";

	/**
	 * 返回指定用户的标签列表
	 * @param uid 要获取的标签列表所属的用户ID。
	 * @param count 单页返回的记录条数，默认为20。
	 * @param page 返回结果的页码，默认为1。
	 * @param listener
	 */
	public void tags( long uid, int count, int page, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", uid);
		params.add("count", count);
		params.add("page", page);
		request( SERVER_URL_PRIX + ".json", params, HTTPMETHOD_GET, listener);
	}
	
	/**
	 * 批量获取用户的标签列表
	 * @param uids 要获取标签的用户ID。最大20
	 * @param listener
	 */
	public void tagsBatch( String[] uids, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		StringBuilder strb = new StringBuilder();
		for (String uid : uids) {
			strb.append(uid).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.add("uids", strb.toString());
		request( SERVER_URL_PRIX + "/tags_batch.json", params, HTTPMETHOD_GET, listener);
	}
	
	/**
	 * 获取系统推荐的标签列表
	 * @param count 返回记录数，默认10，最大10。
	 * @param listener
	 */
	public void suggestions( int count, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("count", count);
		request( SERVER_URL_PRIX + "/suggestions.json", params, HTTPMETHOD_GET, listener);
	}
	
	/**
	 * 为当前登录用户添加新的用户标签(无论调用该接口次数多少，每个用户最多可以创建10个标签)
	 * @param tags 要创建的一组标签，每个标签的长度不可超过7个汉字，14个半角字符。
	 * @param listener
	 */
	public void create( String[] tags, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		StringBuilder strb = new StringBuilder();
		for (String tag : tags) {
			strb.append(tag).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.add("tags", strb.toString());
		request( SERVER_URL_PRIX + "/create.json", params, HTTPMETHOD_POST, listener);
	}
	
	/**
	 * 删除一个用户标签
	 * @param tag_id 要删除的标签ID。
	 * @param listener
	 */
	public void destroy( long tag_id, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("tag_id", tag_id);
		request( SERVER_URL_PRIX + "/destroy.json", params, HTTPMETHOD_POST, listener);
	}
	
	/**
	 * 批量删除一组标签
	 * @param ids 要删除的一组标签ID，一次最多提交10个ID。
	 * @param listener
	 */
	public void destroyBatch( String[] ids, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		StringBuilder strb = new StringBuilder();
		for (String id : ids) {
			strb.append(id).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.add("ids", strb.toString());
		request( SERVER_URL_PRIX + "/destroy_batch.json", params, HTTPMETHOD_POST, listener);
	}
}
