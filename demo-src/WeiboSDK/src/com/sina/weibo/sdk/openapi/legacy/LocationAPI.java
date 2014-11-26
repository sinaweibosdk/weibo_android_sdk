/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sina.weibo.sdk.openapi.legacy;

import android.content.Context;
import android.util.SparseArray;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * 地理信息相关接口。
 * 详情请参考<a href="http://t.cn/8FdRdk0">评论接口</a>
 * 
 * @author SINA
 * @since 2013-12-4
 */
public class LocationAPI extends AbsOpenAPI {
    /** API URL */
    private static final String API_BASE_URL = API_SERVER + "/location";
    
    /**
     * API 类型。
     * 命名规则：
     *      <li>读取接口：READ_API_XXX
     *      <li>写入接口：WRITE_API_XXX
     */
    private static final int READ_API_GPS_TO_OFFSET      = 0;
    private static final int READ_API_SEARCH_POIS_BY_GEO = 1;
    private static final int READ_API_GET_TO_ADDRESS     = 2;

    private static final SparseArray<String> sAPIList = new SparseArray<String>();
    static {
        sAPIList.put(READ_API_GPS_TO_OFFSET,      API_BASE_URL + "/geo/gps_to_offset.json");
        sAPIList.put(READ_API_SEARCH_POIS_BY_GEO, API_BASE_URL + "/pois/search/by_geo.json");
        sAPIList.put(READ_API_GET_TO_ADDRESS,     API_BASE_URL + "/geo/geo_to_address.json");
    }

    /**
     * 构造函数，使用各个 API 接口提供的服务前必须先获取 Token。
     * 
     * @param accesssToken 访问令牌
     */
    public LocationAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }

    /**
     * 根据GPS坐标获取偏移后的坐标
     * 
     * @param longtitude 纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
     * @param latitude   纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
     * @param listener   异步请求回调接口
     */
    public void gps2Offset(Double longtitude, Double latitude, RequestListener listener) {
        WeiboParameters params = buildGPS2OffsetParams(longtitude, latitude);
        requestAsync(sAPIList.get(READ_API_GPS_TO_OFFSET), params, HTTPMETHOD_GET, listener);
    }

    /**
     * 根据关键词按坐标点范围获取POI点的信息。
     * 
     * @param latitude   纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
     * @param longtitude 经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
     * @param keyWord    查询的关键词，必须进行URLencode。
     * @param listener   异步请求回调接口
     */
    public void searchPoisByGeo(Double longtitude, Double latitude, String keyWord, RequestListener listener) {
        WeiboParameters params = buildSerarPoiByGeoParmas(longtitude, latitude, keyWord);
        requestAsync(sAPIList.get(READ_API_SEARCH_POIS_BY_GEO), params, HTTPMETHOD_GET, listener);
    }

    /**
     * 根据地理信息坐标返回实际地址。
     * 
     * @param longtitude 经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
     * @param latitude   纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
     * @param listener   异步请求回调接口
     */
    public void geo2Address(Double longtitude, Double latitude, RequestListener listener) {
        WeiboParameters params = buildGeo2AddressParam(longtitude, latitude);
        requestAsync(sAPIList.get(READ_API_GET_TO_ADDRESS), params, HTTPMETHOD_GET, listener);
    }

    /**
     * @see #gps2Offset(Double, Double, RequestListener)
     */
    public String gps2OffsetSync(Double longtitude, Double latitude) {
        WeiboParameters params = buildGPS2OffsetParams(longtitude, latitude);
        return requestSync(sAPIList.get(READ_API_GPS_TO_OFFSET), params, HTTPMETHOD_GET);
    }

    /**
     * @see #searchPoisByGeo(Double, Double, String, RequestListener)
     */
    public String searchPoisByGeoSync(Double longtitude, Double latitude, String keyWord) {
        WeiboParameters params = buildSerarPoiByGeoParmas(longtitude, latitude, keyWord);
        return requestSync(sAPIList.get(READ_API_SEARCH_POIS_BY_GEO), params, HTTPMETHOD_GET);
    }

    /**
     * @see #geo2Address(Double, Double, RequestListener)
     */
    public String geo2AddressSync(Double longtitude, Double latitude) {
        WeiboParameters params = buildGeo2AddressParam(longtitude, latitude);
        return requestSync(sAPIList.get(READ_API_GET_TO_ADDRESS), params, HTTPMETHOD_GET);
    }

    private WeiboParameters buildGPS2OffsetParams(Double longtitude, Double latitude) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        String coordinate = longtitude + "," + latitude;
        params.put("coordinate", coordinate);
        return params;
    }

    private WeiboParameters buildSerarPoiByGeoParmas(Double longtitude, Double latitude, String keyWord) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        String coordinate = longtitude + "," + latitude;
        params.put("coordinate", coordinate);
        params.put("q", keyWord);
        return params;
    }

    private WeiboParameters buildGeo2AddressParam(Double longtitude, Double latitude) {
        WeiboParameters params = new WeiboParameters(mAppKey);
        String coordinate = longtitude + "," + latitude;
        params.put("coordinate", coordinate);
        return params;
    }
}
