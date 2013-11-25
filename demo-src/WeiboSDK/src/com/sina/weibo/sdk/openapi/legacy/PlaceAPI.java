package com.sina.weibo.sdk.openapi.legacy;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.net.RequestListener;

/**
 * 该类封装了微博的位置服务接口，详情请参考<a href="http://open.weibo.com/wiki/%E4%BD%8D%E7%BD%AE%E6%9C%8D%E5%8A%A1">微博位置服务</a>
 * @author luopeng (luopeng@staff.sina.com.cn)
 */
public class PlaceAPI extends WeiboAPI {
	public PlaceAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/place";


	/**
	 * 获取当前登录用户与其好友的位置动态
	 * 
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，最大为50，默认为20。
	 * @param page 返回结果的页码，默认为1。
	 * @param only_attentions true：仅返回关注的，false：返回好友的，默认为true。
	 * @param listener
	 */
	public void friendsTimeline( long since_id, long max_id, int count, int page,
			boolean only_attentions, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (only_attentions) {
			params.add("type", 0);
		} else {
			params.add("type", 1);
		}
		request( SERVER_URL_PRIX + "/friends_timeline.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取某个用户的位置动态
	 * 
	 * @param uid 需要查询的用户ID。
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，最大为50，默认为20。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void userTimeline( long uid, long since_id, long max_id, int count, int page,
			boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", uid);
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/user_timeline.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取某个位置地点的动态
	 * 
	 * @param poiid 需要查询的POI点ID。
	 * @param since_id 若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
	 * @param max_id 若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
	 * @param count 单页返回的记录条数，最大为50，默认为20。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void poiTimeline( String poiid, long since_id, long max_id, int count,
			int page, boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("poiid", poiid);
		params.add("since_id", since_id);
		params.add("max_id", max_id);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 0);
		} else {
			params.add("base_app", 1);
		}
		request( SERVER_URL_PRIX + "/poi_timeline.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取某个位置周边的动态
	 * 
	 * @param lat 纬度。有效范围：-90.0到+90.0，+表示北纬。
	 * @param lon 经度。有效范围：-180.0到+180.0，+表示东经。
	 * @param range 搜索范围，单位米，默认2000米，最大11132米。
	 * @param starttime 开始时间，Unix时间戳。
	 * @param endtime 结束时间，Unix时间戳。
	 * @param sort 排序方式。按时间排序或按与中心点距离进行排序。
	 * @param count 单页返回的记录条数，最大为50，默认为20。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param offset 传入的经纬度是否是纠偏过，false：没纠偏、true：纠偏过，默认为false。
	 * @param listener
	 */
	public void nearbyTimeline( String lat, String lon, int range, long starttime,
	        long endtime, SORT3 sort, int count, int page, boolean base_app, boolean offset,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("lat", lat);
		params.add("long", lon);
		params.add("range", range);
		params.add("starttime", starttime);
		params.add("endtime", endtime);
		params.add("sort", sort.ordinal());
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		if (offset) {
			params.add("offset", 1);
		} else {
			params.add("offset", 0);
		}
		request( SERVER_URL_PRIX + "/nearby_timeline.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 根据ID获取动态的详情
	 * 
	 * @param id 需要获取的动态ID。
	 * @param listener
	 */
	public void statusesShow( long id, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("id", id);
		request( SERVER_URL_PRIX + "/statuses/show.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取LBS位置服务内的用户信息
	 * 
	 * @param uid 需要查询的用户ID。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void usersShow( long uid, boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", uid);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/users/show.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取用户签到过的地点列表
	 * 
	 * @param uid 需要查询的用户ID。
	 * @param count 单页返回的记录条数，默认为20，最大为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void usersCheckins( long uid, int count, int page, boolean base_app,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", uid);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/users/checkins.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取用户的照片列表
	 * 
	 * @param uid 需要查询的用户ID。
	 * @param count 单页返回的记录条数，默认为20，最大为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void usersPhotos( long uid, int count, int page, boolean base_app,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", uid);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/users/photos.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取用户的点评列表
	 * 
	 * @param uid 需要查询的用户ID。
	 * @param count 单页返回的记录条数，默认为20，最大为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void usersTips( long uid, int count, int page, boolean base_app,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", uid);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/users/tips.json", params, HTTPMETHOD_GET, listener);
	}


	/**
	 * 获取地点详情
	 * 
	 * @param poiid 需要查询的POI地点ID。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void poisShow( String poiid, boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("poiid", poiid);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/pois/show.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取在某个地点签到的人的列表
	 * 
	 * @param poiid 需要查询的POI地点ID。
	 * @param count 单页返回的记录条数，默认为20，最大为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void poisUsers( String poiid, int count, int page, boolean base_app,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("poiid", poiid);
		params.add("count", count);
		params.add("page", page);
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/pois/users.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取地点照片列表
	 * 
	 * @param poiid 需要查询的POI地点ID。
	 * @param count 单页返回的记录条数，默认为20，最大为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param sort 排序方式，0：按时间、1：按热门，默认为0，目前只支持按时间。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void poisPhotos( String poiid, int count, int page, SORT2 sort, boolean base_app,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("uid", poiid);
		params.add("count", count);
		params.add("page", page);
		params.add("sort", sort.ordinal());
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/pois/photos.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取地点点评列表
	 * 
	 * @param poiid 需要查询的POI地点ID。
	 * @param count 单页返回的记录条数，默认为20，最大为50
	 * @param page 返回结果的页码，默认为1。
	 * @param sort 排序方式，0：按时间、1：按热门，默认为0，目前只支持按时间。
	 * @param base_app 是否只获取当前应用的数据。false为否（所有数据），true为是（仅当前应用），默认为false。
	 * @param listener
	 */
	public void poisTips( String poiid, int count, int page, SORT2 sort,
			boolean base_app, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("poiid", poiid);
		params.add("count", count);
		params.add("page", page);
		params.add("sort", sort.ordinal());
		if (base_app) {
			params.add("base_app", 1);
		} else {
			params.add("base_app", 0);
		}
		request( SERVER_URL_PRIX + "/pois/tips.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 按省市查询地点
	 * 
	 * @param keyword 查询的关键词
	 * @param city 城市代码，默认为全国搜索。
	 * @param category 查询的分类代码，取值范围见：分类代码对应表。
	 * @param count 单页返回的记录条数，默认为20，最大为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param listener
	 */
	public void poisSearch( String keyword, String city, String category, int count,
			int page, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("keyword", keyword);
		params.add("city", city);
		params.add("category", category);
		params.add("count", count);
		params.add("page", page);
		request( SERVER_URL_PRIX + "/pois/search.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取地点分类
	 * 
	 * @param pid 父分类ID，默认为0。
	 * @param returnALL 是否返回全部分类，false：只返回本级下的分类，true：返回全部分类，默认为false。
	 * @param listener
	 */
	public void poisCategory( int pid, boolean returnALL, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("pid", pid);
		if (returnALL) {
			params.add("flag", 1);
		} else {
			params.add("flag", 0);
		}
		request( SERVER_URL_PRIX + "/pois/category.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取附近地点
	 * 
	 * @param lat 纬度，有效范围：-90.0到+90.0，+表示北纬。
	 * @param lon 经度，有效范围：-180.0到+180.0，+表示东经。
	 * @param range 查询范围半径，默认为2000，最大为10000，单位米。
	 * @param q 查询的关键词
	 * @param category 查询的分类代码，取值范围见：分类代码对应表。
	 * @param count 单页返回的记录条数，默认为20，最大为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param offset 传入的经纬度是否是纠偏过，false：没纠偏、true：纠偏过，默认为false。
	 * @param listener
	 */
	public void nearbyPois( String lat, String lon, int range, String q,
			String category, int count, int page, boolean offset, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("lat", lat);
		params.add("long", lon);
		params.add("range", range);
		params.add("q", q);
		params.add("category", category);
		params.add("count", count);
		params.add("page", page);
		if (offset) {
			params.add("offset", 1);
		} else {
			params.add("offset", 0);
		}
		request( SERVER_URL_PRIX + "/nearby/pois.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取附近发位置微博的人
	 * 
	 * @param lat 纬度，有效范围：-90.0到+90.0，+表示北纬。
	 * @param lon 经度，有效范围：-180.0到+180.0，+表示东经。
	 * @param range 查询范围半径，默认为2000，最大为11132，单位米。
	 * @param starttime 开始时间，Unix时间戳。
	 * @param endtime 结束时间，Unix时间戳。
	 * @param sort 排序方式，0：按时间、1：按距离，默认为0。
	 * @param count 单页返回的记录条数，默认为20，最大为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param offset 传入的经纬度是否是纠偏过，false：没纠偏、true：纠偏过，默认为false。
	 * @param listener
	 */
	public void nearbyUsers( String lat, String lon, int range, long starttime,
	        long endtime, SORT3 sort, int count, int page, boolean offset, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("lat", lat);
		params.add("long", lon);
		params.add("range", range);
		params.add("starttime", starttime);
		params.add("endtime", endtime);
		params.add("sort", sort.ordinal());
		params.add("count", count);
		params.add("page", page);
		if (offset) {
			params.add("offset", 1);
		} else {
			params.add("offset", 0);
		}
		request( SERVER_URL_PRIX + "/nearby/users.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 获取附近照片
	 * 
	 * @param lat 纬度，有效范围：-90.0到+90.0，+表示北纬。
	 * @param lon 经度，有效范围：-180.0到+180.0，+表示东经。
	 * @param range 查询范围半径，默认为500，最大为11132，单位米。
	 * @param starttime 开始时间，Unix时间戳。
	 * @param endtime 结束时间，Unix时间戳。
	 * @param sort 排序方式，0：按时间、1：按距离，默认为0。
	 * @param count 单页返回的记录条数，默认为20，最大为50。
	 * @param page 返回结果的页码，默认为1。
	 * @param offset 传入的经纬度是否是纠偏过，false：没纠偏、true：纠偏过，默认为false。
	 * @param listener
	 */
	public void nearbyPhotos( String lat, String lon, int range, long starttime,
	        long endtime, SORT3 sort, int count, int page, boolean offset, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("lat", lat);
		params.add("long", lon);
		params.add("range", range);
		params.add("starttime", starttime);
		params.add("endtime", endtime);
		params.add("sort", sort.ordinal());
		params.add("count", count);
		params.add("page", page);
		if (offset) {
			params.add("offset", 1);
		} else {
			params.add("offset", 0);
		}
		request( SERVER_URL_PRIX + "/nearby/photos.json", params, HTTPMETHOD_GET, listener);
	}

	/**
	 * 签到同时可以上传一张图片
	 * 
	 * @param poiid 需要签到的POI地点ID。
	 * @param status 签到时发布的动态内容，内容不超过140个汉字
	 * @param pic 需要上传的图片路径，仅支持JPEG、GIF、PNG格式，图片大小小于5M。例如：/sdcard/pic.jgp；注意：pic不能为网络图片
	 * @param isPublic 是否同步到微博，默认为不同步。
	 * @param listener
	 */
	public void poisAddCheckin( String poiid, String status, String pic,
			boolean isPublic, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("poiid", poiid);
		params.add("status", status);
		params.add("pic", pic);
		if (isPublic) {
			params.add("public", 1);
		} else {
			params.add("public", 0);
		}
		request( SERVER_URL_PRIX + "/pois/add_checkin.json", params, HTTPMETHOD_POST,
				listener);
	}

	/**
	 * 添加照片
	 * 
	 * @param poiid 需要添加照片的POI地点ID。
	 * @param status 签到时发布的动态内容，内容不超过140个汉字。
	 * @param pic 需要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M。例如：/sdcard/pic.jgp；注意：pic不能为网络图片
	 * @param isPublic 是否同步到微博，默认为不同步。
	 * @param listener
	 */
	public void poisAddPhoto( String poiid, String status, String pic,
			boolean isPublic, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("poiid", poiid);
		params.add("status", status);
		params.add("pic", pic);
		if (isPublic) {
			params.add("public", 1);
		} else {
			params.add("public", 0);
		}
		request( SERVER_URL_PRIX + "/pois/add_photo.json", params, HTTPMETHOD_POST, listener);
	}

	/**
	 * 添加点评
	 * 
	 * @param poiid 需要点评的POI地点ID。
	 * @param status 点评时发布的动态内容，内容不超过140个汉字。
	 * @param isPublic 是否同步到微博，默认为不同步。
	 * @param listener
	 */
	public void poisAddTip( String poiid, String status, boolean isPublic,
			RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("poiid", poiid);
		params.add("status", status);
		if (isPublic) {
			params.add("public", 1);
		} else {
			params.add("public", 0);
		}
		request( SERVER_URL_PRIX + "/pois/add_tip.json", params, HTTPMETHOD_POST, listener);
	}
}
