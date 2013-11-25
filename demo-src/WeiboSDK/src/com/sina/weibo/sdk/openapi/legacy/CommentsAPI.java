package com.sina.weibo.sdk.openapi.legacy;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;
/**
 * 此类封装了评论的接口，详情见<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E8.AF.84.E8.AE.BA">评论接口</a>
 * @author xiaowei6@staff.sina.com.cn
 *
 */
public class CommentsAPI extends WeiboAPI {
	public CommentsAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/comments";

	/**
	 * 根据微博ID返回某条微博的评论列表
	 * @param id 需要查询的微博ID。
	 * @param since_id 若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
	 * @param count 单页返回的记录条数，默认为50
	 * @param page 返回结果的页码，默认为1。
	 * @param filter_by_author 作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
	 * @param listener
	 */
	public void show(long id, long since_id, long max_id, int count, int page,
			AUTHOR_FILTER filter_by_author,RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("id", id);
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		params.add("filter_by_author", filter_by_author.ordinal());
		request( SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET,listener);
	}

	/**
	 * 获取当前登录用户所发出的评论列表
	 * @param since_id 若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param filter_by_source 来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论，默认为0。
	 * @param listener
	 */
	public void byME(long since_id, long max_id, int count, int page,
			SRC_FILTER filter_by_source,RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		params.add("filter_by_source", filter_by_source.ordinal());
		request(SERVER_URL_PRIX + "/by_me.json", params, HTTPMETHOD_GET,listener);
	}

	/**
	 * 获取当前登录用户所接收到的评论列表
	 * 
	 * @param since_id 若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param filter_by_author 作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
	 * @param filter_by_source 来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论，默认为0。
	 * @param listener
	 */
	public void toME(long since_id, long max_id, int count, int page,
			AUTHOR_FILTER filter_by_author, SRC_FILTER filter_by_source,RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		params.add("filter_by_author", filter_by_author.ordinal());
		params.add("filter_by_source", filter_by_source.ordinal());
		request(SERVER_URL_PRIX + "/to_me.json", params, HTTPMETHOD_GET,listener);
	}

	/**
	 * 获取当前登录用户的最新评论包括接收到的与发出的
	 * 
	 * @param since_id 若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param trim_user 返回值中user字段开关，false：返回完整user字段、true：user字段仅返回user_id，默认为false。
	 * @param listener
	 */
	public void timeline(long since_id, long max_id, int count, int page,
			boolean trim_user,RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (trim_user) {
			params.add("trim_user", 1);
		} else {
			params.add("trim_user", 0);
		}
		request(SERVER_URL_PRIX + "/timeline.json", params, HTTPMETHOD_GET,listener);
	}

	/**
	 * 获取最新的提到当前登录用户的评论，即@我的评论
	 *  若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
	 * @param since_id 若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
	 * @param count 单页返回的记录条数，默认为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param filter_by_author 作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
	 * @param filter_by_source 来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论，默认为0。
	 * @param listener
	 */
	public void mentions(long since_id, long max_id, int count, int page,
			AUTHOR_FILTER filter_by_author, SRC_FILTER filter_by_source,RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		params.add("filter_by_author", filter_by_author.ordinal());
		params.add("filter_by_source", filter_by_source.ordinal());
		request(SERVER_URL_PRIX + "/mentions.json", params, HTTPMETHOD_GET,listener);
	}

	/**
	 * 根据评论ID批量返回评论信息
	 * 
	 * @param cids 需要查询的批量评论ID数组，最大50。
	 * @param listener
	 */
	public void showBatch( long[] cids, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		StringBuilder strb = new StringBuilder();
		for (long cid : cids) {
			strb.append(String.valueOf(cid)).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.add("cids", strb.toString());
		request(SERVER_URL_PRIX + "/show_batch.json", params, HTTPMETHOD_GET,listener);
	}

	/**
	 * 对一条微博进行评论
	 * 
	 * @param comment 评论内容，内容不超过140个汉字。
	 * @param id 需要评论的微博ID。
	 * @param comment_ori 当评论转发微博时，是否评论给原微博
	 * @param listener
	 */
	public void create(String comment, long id, boolean comment_ori, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("comment", comment);
		params.add("id", id);
		if (comment_ori) {
			params.add("comment_ori", 0);
		} else {
			params.add("comment_ori", 1);
		}
		request(SERVER_URL_PRIX + "/create.json", params, HTTPMETHOD_POST, listener);
	}

	/**
	 * 删除一条评论
	 * 
	 * @param cid 要删除的评论ID，只能删除登录用户自己发布的评论。
	 * @param listener
	 */
	public void destroy(long cid, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("cid", cid);
		request( SERVER_URL_PRIX + "/destroy.json", params, HTTPMETHOD_POST, listener);
	}

	/**
	 * 根据评论ID批量删除评论
	 * 
	 * @param ids 需要删除的评论ID数组，最多20个。
	 * @param listener
	 */
	public void destroyBatch( long[] ids, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		StringBuilder strb = new StringBuilder();
		for (long cid : ids) {
			strb.append(String.valueOf(cid)).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.add("ids", strb.toString());
		request(SERVER_URL_PRIX + "/sdestroy_batch.json", params, HTTPMETHOD_POST, listener);
	}

	/**
	 * 回复一条评论
	 * 
	 * @param cid 需要回复的评论ID。
	 * @param id 需要评论的微博ID。
	 * @param comment 回复评论内容，内容不超过140个汉字。
	 * @param without_mention 回复中是否自动加入“回复@用户名”，true：是、false：否，默认为false。
	 * @param comment_ori 当评论转发微博时，是否评论给原微博，false：否、true：是，默认为false。
	 * @param listener
	 */
	public void reply(long cid, long id, String comment, boolean without_mention,
			boolean comment_ori, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("cid", cid);
		params.add("id", id);
		params.add("comment", comment);
		if (without_mention) {
			params.add("without_mention", 1);
		} else {
			params.add("without_mention", 0);
		}
		if (comment_ori) {
			params.add("comment_ori", 1);
		} else {
			params.add("comment_ori", 0);
		}
		request(SERVER_URL_PRIX + "/reply.json", params, HTTPMETHOD_POST, listener);
	}

}
