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

package com.sina.weibo.sdk.call;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.sina.weibo.sdk.constant.WBPageConstants;

/**
 * WeiboPageUtils 包含一些静态函数，通过调用这些函数，可以访问和使用微博 Android
 * 客户端提供的一些功能和服务。（WeiboPageSDK 3.0） TODO：（To be design...）
 * 
 * @author SINA
 * @since 2013-11-07
 */
public final class WeiboPageUtilsV2 {

    private WeiboPageUtilsV2() {
    }

    /**
     * 发送微博,如要发送带位置的微博，position不能为空
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param content
     *            可选，要发送的微博内容
     * @param poiId
     *            可选，poi点的唯一标识
     * @param poiName
     *            可选，poi点的名字，与poiid对应
     * @param longitude
     *            可选，微博所属的地理位置的经度
     * @param latitude
     *            可选，微博所属的地理位置的纬度
     * @param pageId
     *            可选，page页面的唯一标识
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用该方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void postNewWeibo(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {

        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }

        StringBuilder uri = null;

        uri = new StringBuilder(WBPageConstants.Scheme.SENDWEIBO);
        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }

        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.SENDWEIBO);
            // packageuri.append("." +
            // params.get(Constants.ParamKey.PACKAGENAME));
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }

    }

    /**
     * 查看周边的人
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param longitude
     *            可选，微博所属的地理位置的经度
     * @param latitude
     *            可选，微博所属的地理位置的纬度
     * @param offset
     *            可行，该经纬度坐标是否偏移的标识
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void viewNearbyPeople(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.NEARBYPEOPLE);
        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }
        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.NEARBYPEOPLE);
            // packageuri.append("." +
            // params.get(Constants.ParamKey.PACKAGENAME));
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }

    }

    /**
     * 查看周边的微博
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param longitude
     *            可选，微博所属的地理位置的经度
     * @param latitude
     *            可选，微博所属的地理位置的纬度
     * @param offset
     *            可行，该经纬度坐标是否偏移的标识
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void viewNearbyWeibo(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.NEARBYWEIBO);

        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }

        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.NEARBYWEIBO);
            // packageuri.append("." +
            // params.get(Constants.ParamKey.PACKAGENAME));
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }

    }

    /**
     * 查看个人主页
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param uid
     *            用户唯一的标识，uid和nick必须至少有一个不为空
     * @param nick
     *            用户昵称，uid和nick必须至少有一个不为空
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */

    public static void viewUserInfo(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }

        if (params == null || TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.UID))
                && TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.NICK))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.UID_NICK_ERROR);
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.USERINFO);

        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }
        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.USERINFO);
            // packageuri.append("." +
            // params.get(Constants.ParamKey.PACKAGENAME));
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }
    }

    /**
     * 查看话题列表
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param uid
     *            必须，用户唯一的标识
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void viewUsertrends(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }
        if (params == null || TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.UID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.UID_NICK_ERROR);
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.USERTRENDS);
        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }

        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.USERTRENDS);
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }
    }

    /**
     * 查看page页面,例如：地点，产品，图书的介绍等。
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param pageId
     *            必须，page页面唯一的标识
     * @param title
     *            可选，page页面的标题
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void viewPageInfo(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }
        if (params == null || TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PAGEID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEINFO);

        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }

        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.PAGEINFO);
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }
    }

    /**
     * 查看page页面的产品列表，二级页面
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param pageId
     *            必须，page页面唯一的标识
     * @param cardId
     *            必须，card唯一的标识
     * @param title
     *            可选，page页面的标题
     * @param count
     *            可选，一页显示的元素个数
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void viewPageProductList(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }

        if (params == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
        }

        if (TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PAGEID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
        }
        if (TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.CARDID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CARDID_ERROR);
        }
        int count = -1;
        try {
            count = Integer.parseInt(params.get(WBPageConstants.ParamKey.COUNT));
        } catch (NumberFormatException e) {
            count = -1;
        }
        if (count < 0) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.COUNT_ERROR);
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEPRODUCTLIST);

        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }

        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.PAGEPRODUCTLIST);
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }
    }

    /**
     * 查看page页面的用户列表，二级页面
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param pageId
     *            必须，page页面唯一的标识
     * @param cardId
     *            必须，card唯一的标识
     * @param title
     *            可选，page页面的标题
     * @param count
     *            可选，一页显示的元素个数
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void viewPageUserList(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }
        if (params == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
        }

        if (TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PAGEID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
        }
        if (TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.CARDID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CARDID_ERROR);
        }
        int count = -1;
        try {
            count = Integer.parseInt(params.get(WBPageConstants.ParamKey.COUNT));
        } catch (NumberFormatException e) {
            count = -1;
        }
        if (count < 0) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.COUNT_ERROR);
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEUSERLIST);

        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }

        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.PAGEUSERLIST);
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }
    }

    /**
     * 查看page页面的微博列表，二级页面
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param pageId
     *            必须，page页面唯一的标识
     * @param cardId
     *            必须，card唯一的标识
     * @param title
     *            可选，page页面的标题
     * @param count
     *            可选，一页显示的元素个数
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void viewPageWeiboList(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }
        if (params == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
        }

        if (TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PAGEID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
        }
        if (TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.CARDID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CARDID_ERROR);
        }
        int count = -1;
        try {
            count = Integer.parseInt(params.get(WBPageConstants.ParamKey.COUNT));
        } catch (NumberFormatException e) {
            count = -1;
        }
        if (count < 0) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.COUNT_ERROR);
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEWEIBOLIST);

        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }

        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.PAGEWEIBOLIST);
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }
    }

    /**
     * 查看page页面的图片列表，二级页面
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param pageId
     *            必须，page页面唯一的标识
     * @param cardId
     *            必须，card唯一的标识
     * @param title
     *            可选，page页面的标题
     * @param count
     *            可选，一页显示的元素个数
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void viewPagePhotoList(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }
        if (params == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
        }

        if (TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PAGEID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
        }
        if (TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.CARDID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CARDID_ERROR);
        }
        int count = -1;
        try {
            count = Integer.parseInt(params.get(WBPageConstants.ParamKey.COUNT));
        } catch (NumberFormatException e) {
            count = -1;
        }
        if (count < 0) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.COUNT_ERROR);
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEPHOTOLIST);
        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }

        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.PAGEPHOTOLIST);
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }
    }

    /**
     * 查看page页面的详细信息，二级页面
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param pageId
     *            必须，page页面唯一的标识
     * @param cardId
     *            必须，card唯一的标识
     * @param title
     *            可选，page页面的标题
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void viewPageDetailInfo(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }
        if (params == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
        }

        if (TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PAGEID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
        }
        if (TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.CARDID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CARDID_ERROR);
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEDETAILINFO);

        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }

        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.PAGEDETAILINFO);
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }
    }

    /**
     * 使用微博内置浏览器打开链接
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param url
     *            必须，链接地址
     * @param sinainternalbrowser
     *            可选，浏览器显示模式
     *            参数范围："topnav":顶部导航栏模式，"default":切换手机版和电脑版模式，"fullscreen": 全屏模式
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void openInWeiboBrowser(Context context, String url, String sinainternalbrowser,
            String extParam, String packageName) throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }

        if (TextUtils.isEmpty(url)) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.URL_ERROR);
        }

        if (!TextUtils.isEmpty(sinainternalbrowser)) {
            if (!"topnav".equals(sinainternalbrowser) && !"default".equals(sinainternalbrowser)
                    && !"fullscreen".equals(sinainternalbrowser)) {
                throw new WeiboIllegalParameterException(
                        WBPageConstants.ExceptionMsg.SINAINTERNALBROWSER);
            }
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.BROWSER);

        HashMap<String, String> paramMap = new HashMap<String, String>();

        paramMap.put(WBPageConstants.ParamKey.URL, url);
        paramMap.put(WBPageConstants.ParamKey.SINAINTERNALBROWSER, sinainternalbrowser);
        paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);

        uri.append(CommonUtils.buildUriQuery(paramMap));

        StringBuilder packageuri = null;
        if (!TextUtils.isEmpty(packageName)) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.BROWSER);
            if (paramMap != null) {
                packageuri.append(CommonUtils.buildUriQuery(paramMap));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), packageName);
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }
    }

    /**
     * 使用微博内置地图显示地点
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param longitude
     *            可选，微博所属的地理位置的经度 @param latitude 可选，微博所属的地理位置的纬度
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void displayInWeiboMap(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }
        // 116.4075f,39.9040f
        String mapUrl = "http://weibo.cn/dpool/ttt/maps.php?xy=%s,%s&amp;size=320x320&amp;offset=%s";
        String lon = "";
        String lat = "";
        String offset = "";

        if (params != null) {
            lon = params.get(WBPageConstants.ParamKey.LONGITUDE);
            lat = params.get(WBPageConstants.ParamKey.LATITUDE);
            offset = params.get(WBPageConstants.ParamKey.OFFSET);
        }

        String packageName = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageName = params.get(WBPageConstants.ParamKey.PACKAGENAME);
        }
        if(params != null){
            openInWeiboBrowser(context, String.format(mapUrl, lon, lat, offset), "default",
                    params.get(WBPageConstants.ParamKey.EXTPARAM), packageName);
        }
        
    }

    /**
     * 打开二维码扫描界面
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.0
     */
    public static void openQrcodeScanner(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }
        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.QRCODE);

        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }

        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.QRCODE);
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }
    }

    /**
     * 查看微博正文页
     * 
     * @param context
     *            必须，当前上下文，通常是正在使用的Activity或Application实例
     * @param mblogid
     *            必须，微博的唯一标识
     * @param extParam
     *            可选，用户自定义参数类型
     * 
     * @exception WeiboNotInstalledException
     *                如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
     * 
     * @since WeiboPageSDK 1.1
     */
    public static void weiboDetail(Context context, HashMap<String, String> params)
            throws WeiboNotInstalledException {
        if (context == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
        }
        if (params == null) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.MBLOGID_ERROR);
        }

        if (TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.MBLOGID))) {
            throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.MBLOGID_ERROR);
        }

        StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.MBLOGDETAIL);

        if (params != null) {
            uri.append(CommonUtils.buildUriQuery(params));
        }
        StringBuilder packageuri = null;
        if (params != null && !TextUtils.isEmpty(params.get(WBPageConstants.ParamKey.PACKAGENAME))) {
            packageuri = new StringBuilder(WBPageConstants.Scheme.MBLOGDETAIL);
            if (params != null) {
                packageuri.append(CommonUtils.buildUriQuery(params));
            }

            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(),
                    params.get(WBPageConstants.ParamKey.PACKAGENAME));
        } else {
            CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString(), null);
        }
    }
}
