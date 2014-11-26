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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.sina.weibo.sdk.constant.WBPageConstants;

/**
 * WeiboPageUtils 包含一些静态函数，通过调用这些函数，可以访问和使用微博 
 * Android 客户端提供的一些功能和服务。
 * TODO：（To be design...）
 * 
 * @author SINA
 * @since 2013-11-07
 */
public final class WeiboPageUtils {
	
	private WeiboPageUtils(){}
	
	/**
	 * 发送微博,如要发送带位置的微博，position不能为空
	 * @param  context   必须，当前上下文，通常是正在使用的Activity或Application实例
	 * @param  content   可选，要发送的微博内容
	 * @param  poiId     可选，poi点的唯一标识
	 * @param  poiName   可选，poi点的名字，与poiid对应
	 * @param  position  可选，微博所属的地理位置
	 * @param  pageId    可选，page页面的唯一标识
	 * @param  extParam  可选，用户自定义参数类型
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用该方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void postNewWeibo(Context context, String content, 
			String poiId, String poiName, Position position, 
			String pageId, String extParam)
			throws WeiboNotInstalledException{
		
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.SENDWEIBO);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		try {
            paramMap.put(WBPageConstants.ParamKey.CONTENT, URLEncoder.encode(content, "UTF-8").replaceAll("\\+","%20"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		paramMap.put(WBPageConstants.ParamKey.POIID, poiId);
		paramMap.put(WBPageConstants.ParamKey.POINAME, poiName);
		if(position != null){
			paramMap.put(WBPageConstants.ParamKey.LONGITUDE, position.getStrLongitude());
			paramMap.put(WBPageConstants.ParamKey.LATITUDE, position.getStrLatitude());
		}
		paramMap.put(WBPageConstants.ParamKey.PAGEID, pageId);
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	/**
	 * 查看周边的人
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例
	 * @param position 可选，想要显示在地图上的地理位置
	 * @param extParam 可选，用户自定义参数类型
	 *            
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void viewNearbyPeople(Context context, Position position, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.NEARBYPEOPLE);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		if(position != null){
			paramMap.put(WBPageConstants.ParamKey.LONGITUDE, position.getStrLongitude());
			paramMap.put(WBPageConstants.ParamKey.LATITUDE, position.getStrLatitude());
			paramMap.put(WBPageConstants.ParamKey.OFFSET, position.getStrOffset());
		}
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	
	/**
	 * 查看周边的微博
	 * @param context  必须，当前上下文，通常是正在使用的Activity或Application实例
	 * @param position 可选，想要显示在地图上的地理位置
	 * @param extParam 可选，用户自定义参数类型       
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void viewNearbyWeibo(Context context, Position position, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.NEARBYWEIBO);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		if(position != null){
			paramMap.put(WBPageConstants.ParamKey.LONGITUDE, position.getStrLongitude());
			paramMap.put(WBPageConstants.ParamKey.LATITUDE, position.getStrLatitude());
			paramMap.put(WBPageConstants.ParamKey.OFFSET, position.getStrOffset());
		}
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	/**
	 * 查看个人主页
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例
	 * @param uid  用户唯一的标识，uid和nick必须至少有一个不为空
	 * @param nick 用户昵称，uid和nick必须至少有一个不为空
	 * @param extParam 可选，用户自定义参数类型       
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	
	public static void viewUserInfo(Context context, String uid, String nick, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		if(TextUtils.isEmpty(uid) && TextUtils.isEmpty(nick)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.UID_NICK_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.USERINFO);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put(WBPageConstants.ParamKey.UID, uid);
		paramMap.put(WBPageConstants.ParamKey.NICK, nick);
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	/**
	 * 查看话题列表
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例
	 * @param uid     必须，用户唯一的标识
	 * @param extParam 可选，用户自定义参数类型 
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void viewUsertrends(Context context, String uid, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		if(TextUtils.isEmpty(uid)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.UID_NICK_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.USERTRENDS);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		
		paramMap.put(WBPageConstants.ParamKey.UID, uid);
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	/**
	 * 查看page页面,例如：地点，产品，图书的介绍等。   
	 * @param context  必须，当前上下文，通常是正在使用的Activity或Application实例                   
	 * @param pageId   必须，page页面唯一的标识                             
	 * @param title    可选，page页面的标题
	 * @param extParam 可选，用户自定义参数类型   
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void viewPageInfo(Context context, String pageId, String title, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		if(TextUtils.isEmpty(pageId)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEINFO);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put(WBPageConstants.ParamKey.PAGEID, pageId);
		paramMap.put(WBPageConstants.ParamKey.TITLE, title);
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	/**
	 * 查看page页面的产品列表，二级页面
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例             
	 * @param pageId  必须，page页面唯一的标识               
	 * @param cardId  必须，card唯一的标识   
	 * @param title   可选，page页面的标题                        
	 * @param count   可选，一页显示的元素个数
	 * @param extParam 可选，用户自定义参数类型   
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void viewPageProductList(Context context, String pageId, String cardId,
			String title, Integer count, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		if(TextUtils.isEmpty(pageId)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
		}
		if(TextUtils.isEmpty(cardId)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CARDID_ERROR);
		}
		if(count != null && count < 0){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.COUNT_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEPRODUCTLIST);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put(WBPageConstants.ParamKey.PAGEID, pageId);
		paramMap.put(WBPageConstants.ParamKey.CARDID, cardId);
		paramMap.put(WBPageConstants.ParamKey.TITLE, title);
		paramMap.put(WBPageConstants.ParamKey.PAGE, "1");
		paramMap.put(WBPageConstants.ParamKey.COUNT, String.valueOf(count));
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	/**
	 * 查看page页面的用户列表，二级页面
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例             
	 * @param pageId  必须，page页面唯一的标识                 
	 * @param cardId  必须，card唯一的标识 
	 * @param title   可选，page页面的标题                   
	 * @param count   可选，一页显示的元素个数
	 * @param extParam 可选，用户自定义参数类型   
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void viewPageUserList(Context context, String pageId, String cardId,
			String title, Integer count, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		if(TextUtils.isEmpty(pageId)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
		}
		if(TextUtils.isEmpty(cardId)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CARDID_ERROR);
		}
		if(count != null && count < 0){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.COUNT_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEUSERLIST);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put(WBPageConstants.ParamKey.PAGEID, pageId);
		paramMap.put(WBPageConstants.ParamKey.CARDID, cardId);
		paramMap.put(WBPageConstants.ParamKey.TITLE, title);
		paramMap.put(WBPageConstants.ParamKey.PAGE, "1");
		paramMap.put(WBPageConstants.ParamKey.COUNT, String.valueOf(count));
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	/**
	 * 查看page页面的微博列表，二级页面
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例             
	 * @param pageId  必须，page页面唯一的标识               
	 * @param cardId  必须，card唯一的标识   
	 * @param title   可选，page页面的标题                
	 * @param count   可选，一页显示的元素个数
	 * @param extParam 可选，用户自定义参数类型   
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void viewPageWeiboList(Context context, String pageId, String cardId,
			String title, Integer count, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		if(TextUtils.isEmpty(pageId)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
		}
		if(TextUtils.isEmpty(cardId)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CARDID_ERROR);
		}
		if(count != null && count < 0){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.COUNT_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEWEIBOLIST);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put(WBPageConstants.ParamKey.PAGEID, pageId);
		paramMap.put(WBPageConstants.ParamKey.CARDID, cardId);
		paramMap.put(WBPageConstants.ParamKey.TITLE, title);
		paramMap.put(WBPageConstants.ParamKey.PAGE, "1");
		paramMap.put(WBPageConstants.ParamKey.COUNT, String.valueOf(count));
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	/**
	 * 查看page页面的图片列表，二级页面
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例             
	 * @param pageId  必须，page页面唯一的标识                 
	 * @param cardId  必须，card唯一的标识   
	 * @param title   可选，page页面的标题                         
	 * @param count   可选，一页显示的元素个数
	 * @param extParam 可选，用户自定义参数类型   
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void viewPagePhotoList(Context context, String pageId, String cardId,
			String title, Integer count, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		if(TextUtils.isEmpty(pageId)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
		}
		if(TextUtils.isEmpty(cardId)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CARDID_ERROR);
		}
		if(count != null && count < 0){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.COUNT_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEPHOTOLIST);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put(WBPageConstants.ParamKey.PAGEID, pageId);
		paramMap.put(WBPageConstants.ParamKey.CARDID, cardId);
		paramMap.put(WBPageConstants.ParamKey.TITLE, title);
		paramMap.put(WBPageConstants.ParamKey.PAGE, "1");
		paramMap.put(WBPageConstants.ParamKey.COUNT, String.valueOf(count));
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	
	/**
	 * 查看page页面的详细信息，二级页面
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例             
	 * @param pageId  必须，page页面唯一的标识                 
	 * @param cardId  必须，card唯一的标识
	 * @param title   可选，page页面的标题
	 * @param extParam 可选，用户自定义参数类型 
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void viewPageDetailInfo(Context context, String pageId, String cardId,
			String title, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		if(TextUtils.isEmpty(pageId)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
		}
		if(TextUtils.isEmpty(cardId)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CARDID_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.PAGEDETAILINFO);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put(WBPageConstants.ParamKey.PAGEID, pageId);
		paramMap.put(WBPageConstants.ParamKey.CARDID, cardId);
		paramMap.put(WBPageConstants.ParamKey.TITLE, title);
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	/**
	 * 使用微博内置浏览器打开链接
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例
	 * @param url     必须，链接地址                                        
	 * @param sinainternalbrowser  可选，浏览器显示模式
	 * 	参数范围："topnav":顶部导航栏模式，"default":切换手机版和电脑版模式，"fullscreen": 全屏模式
	 * @param extParam 可选，用户自定义参数类型 
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void openInWeiboBrowser(Context context, String url, String sinainternalbrowser,
			String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		
		if(TextUtils.isEmpty(url)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.URL_ERROR);
		}
		
		if(!TextUtils.isEmpty(sinainternalbrowser)){
			if(!"topnav".equals(sinainternalbrowser) 
					&& !"default".equals(sinainternalbrowser)
					&& !"fullscreen".equals(sinainternalbrowser)){
				throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.SINAINTERNALBROWSER);
			}
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.BROWSER);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		
		paramMap.put(WBPageConstants.ParamKey.URL, url);
		paramMap.put(WBPageConstants.ParamKey.SINAINTERNALBROWSER, sinainternalbrowser);
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	
	/**
	 * 使用微博内置地图显示地点
	 * @param context  必须，当前上下文，通常是正在使用的Activity或Application实例
	 * @param position 可选，想要显示在地图上的地理位置  
	 * @param extParam 可选，用户自定义参数类型   
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void displayInWeiboMap(Context context, Position position, String extParam)
	 	throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		//116.4075f,39.9040f
		String mapUrl = "http://weibo.cn/dpool/ttt/maps.php?xy=%s,%s&amp;size=320x320&amp;offset=%s";
        String lon="";
        String lat="";
        String offset="";
       
        if (position != null) {
            lon = position.getStrLongitude();
            lat = position.getStrLatitude();
            offset = position.getStrOffset();
        }
        
        openInWeiboBrowser(context, String.format(mapUrl, lon, lat, offset), "default", extParam);
	}
	
	
	/**
	 * 打开二维码扫描界面
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例
	 * @param extParam 可选，用户自定义参数类型 
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.0
	 */
	public static void openQrcodeScanner(Context context, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.QRCODE);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
	
	
	/**
	 * 查看周边照片
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例             
	 * @param longitude_X  必须，x坐标值（经度）                 
	 * @param latitude_Y  必须，y坐标值（纬度）
	 * @param count   可选，一页显示的元素个数
	 * @param extParam 可选，用户自定义参数类型   
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.1
	 */
	public static void viewNearPhotoList(Context context, String longitude_X , String latitude_Y, Integer count, String extParam) throws WeiboNotInstalledException{
		viewPagePhotoList(context, "100101"+longitude_X+"_"+latitude_Y, "nearphoto", "周边热图", count, extParam);
	}
	
	/**
	 * 查看周边照片
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例             
	 * @param longitude_X  必须，x坐标值（经度）                 
	 * @param latitude_Y  必须，y坐标值（纬度）
	 * @param count   可选，一页显示的元素个数
	 * @param extParam 可选，用户自定义参数类型   
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.1
	 */
	public static void viewPoiPhotoList(Context context, String poiid, Integer count, String extParam) throws WeiboNotInstalledException{
		viewPagePhotoList(context, "100101"+poiid, "nearphoto", "周边热图", count, extParam);
	}
	/**
	 * 查看周边POI页面 
	 * @param context  必须，当前上下文，通常是正在使用的Activity或Application实例                   
	 * @param longitude_X  必须，x坐标值（经度）                 
	 * @param latitude_Y  必须，y坐标值（纬度）                             
	 * @param title    可选，page页面的标题
	 * @param extParam 可选，用户自定义参数类型   
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.1
	 */
	public static void viewPoiPage(Context context, String longitude_X , String latitude_Y, String title, String extParam) throws WeiboNotInstalledException{
		viewPageInfo(context, "100101"+longitude_X+"_"+latitude_Y, title, extParam);
	}
	
	/**
	 * 查看微博正文页
	 * @param context 必须，当前上下文，通常是正在使用的Activity或Application实例             
	 * @param mblogid  必须，微博的唯一标识                  
	 * @param extParam 可选，用户自定义参数类型 
	 * 
	 * @exception WeiboNotInstalledException 如果用户调用改方法的环境(手机)中，没有安装官方的微博客户端，抛出此异常
	 * 
	 * @since WeiboPageSDK 1.1
	 */
	public static void weiboDetail(Context context, String mblogid, String extParam) throws WeiboNotInstalledException{
		if(context == null){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.CONTEXT_ERROR);
		}
		if(TextUtils.isEmpty(mblogid)){
			throw new WeiboIllegalParameterException(WBPageConstants.ExceptionMsg.PAGEID_ERROR);
		}
		
		StringBuilder uri = new StringBuilder(WBPageConstants.Scheme.MBLOGDETAIL);
		
		HashMap<String,String> paramMap = new HashMap<String,String>();
		paramMap.put(WBPageConstants.ParamKey.MBLOGID, mblogid);
		paramMap.put(WBPageConstants.ParamKey.EXTPARAM, extParam);
		
		uri.append(CommonUtils.buildUriQuery(paramMap));
		CommonUtils.openWeiboActivity(context, Intent.ACTION_VIEW, uri.toString());
	}
}
