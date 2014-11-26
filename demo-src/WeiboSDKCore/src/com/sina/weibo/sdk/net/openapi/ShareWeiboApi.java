package com.sina.weibo.sdk.net.openapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;

public class ShareWeiboApi {
    private static final String TAG = ShareWeiboApi.class.getName();
    
    private static final String UPDATE_URL = "https://api.weibo.com/2/statuses/update.json";
    private static final String UPLOAD_URL = "https://api.weibo.com/2/statuses/upload.json";
    private static final String REPOST_URL = "https://api.weibo.com/2/statuses/repost.json";
    
    private Context mContext;
    private String mAppKey;
    private String mAccessToken;
    
    private ShareWeiboApi(Context context, String appKey, String token) {
        this.mContext = context.getApplicationContext();
        this.mAppKey = appKey;
        this.mAccessToken = token;
    }
    
    public static ShareWeiboApi create(
            Context context, String appKey, String token) {
        return new ShareWeiboApi(context, appKey, token);
    }
    
    /**
     * 发布一条新微博（连续两次发布的微博不可以重复）。
     * 
     * @param content  要发布的微博文本内容，内容不超过140个汉字。
     * @param lat      纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
     * @param lon      经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
     * @param listener 异步请求回调接口
     */
    public void update(String content, String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        requestAsync(UPDATE_URL, params, "POST", listener);
    }
    
    /**
     * 上传图片并发布一条新微博。
     * 
     * @param content  要发布的微博文本内容，内容不超过140个汉字
     * @param bitmap   要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M
     * @param lat      纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
     * @param lon      经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
     * @param listener 异步请求回调接口
     */
    public void upload(String content, Bitmap bitmap, String lat, String lon, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(content, lat, lon);
        params.put("pic", bitmap);
        requestAsync(UPLOAD_URL, params, "POST", listener);
    }
    
    /**
     * 转发一条微博
     * 
     * @param repostBlogId  要转发的微博ID
     * @param repostContent 添加的转发文本，内容不超过140个汉字，不填则默认为“转发微博”
     * @param comment     是否在转发的同时发表评论，0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0
     * @param listener       异步请求回调接口
     */
    public void repost(String repostBlogId, String repostContent, int comment, RequestListener listener) {
        WeiboParameters params = buildUpdateParams(repostContent, null, null);
        params.put("id", repostBlogId);
        params.put("is_comment", String.valueOf(comment));
        requestAsync(REPOST_URL, params, "POST", listener);
    }

    /**
     * HTTP 异步请求。
     * 
     * @param url        请求的地址
     * @param params     请求的参数
     * @param httpMethod 请求方法
     * @param listener   请求后的回调接口
     */
    private void requestAsync(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        if (TextUtils.isEmpty(mAccessToken)
                || TextUtils.isEmpty(url)
                || null == params
                || TextUtils.isEmpty(httpMethod)
                || null == listener) {
            LogUtil.e(TAG, "Argument error!");
            return;
        }
        params.put("access_token", mAccessToken);
        new AsyncWeiboRunner(mContext).requestAsync(url, params, httpMethod, listener);
    }
    
    
    // 组装微博请求参数
    private WeiboParameters buildUpdateParams(String content, String lat, String lon) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        params.put("status", content);
        if (!TextUtils.isEmpty(lon)) {
            params.put("long", lon);
        }
        if (!TextUtils.isEmpty(lat)) {
            params.put("lat", lat);
        }
        if (!TextUtils.isEmpty(mAppKey)) {
            params.put("source", mAppKey);
        }
        return params;
    }
    
}
