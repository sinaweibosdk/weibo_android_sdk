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


/**
 * Position 表示一个地理位置，包括经纬度，以及是否偏移的信息。
 * TODO：（To be design...）
 * 
 * @author SINA
 * @since 2013-11-07
 */
public class Position {

	/**
	 * 地图上位置的经度
	 */
	private float mLongitude;
	/**
	 * 地图上位置的纬度
	 */
	private float mLatitude;
	/**
	 * 该经纬度坐标是否偏移的标识
	 */
	private boolean mOffset;
	
	/**
	 * 构造一个指定经纬度的<code>Position</code>实例，是否偏移默认为true
	 */
	public Position(float longitude, float latitude){
		mLongitude = longitude;
		mLatitude = latitude;
		mOffset = true;
	}
	
	/**
	 * 构造一个指定经纬度,且是否偏移的<code>Position</code>实例
	 */
	public Position(float longitude, float latitude, boolean offset){
		mLongitude = longitude;
		mLatitude = latitude;
		mOffset = offset;
	} 
	
	/**
	 * 获取该位置的经度
	 * @return 该位置的经度
	 * @since  WeiboPageSDK 1.0
	 */
	public float getLongitude(){
		return mLongitude;
	}
	
	/**
	 * 获取该位置的纬度
	 * @return 该位置的纬度
	 * @since  WeiboPageSDK 1.0
	 */
	public float getLatitude(){
		return mLatitude;
	}
	
	/**
	 * 获取该经纬度坐标是否偏移的标识
	 * @return 经纬度坐标是否偏移的标识，是返回true,否返回false
	 * @since  WeiboPageSDK 1.0
	 */
	public boolean isOffset(){
		return mOffset;
	}
	
	/**
	 * 获取该位置的经度，字符形式
	 * @return 该位置的经度，String类型
	 * @since  WeiboPageSDK 1.0
	 */
	public String getStrLongitude(){
		return String.valueOf(mLongitude);
	}
	
	/**
	 * 获取该位置的纬度，字符形式
	 * @return 该位置的纬度，String类型
	 * @since  WeiboPageSDK 1.0
	 */
	public String getStrLatitude(){
		return String.valueOf(mLatitude);
	}
	
	/**
	 * 获取该经纬度坐标是否偏移的标识，字符形式
	 * @return 经纬度坐标是否偏移的标识，是返回"1",否返回"0"
	 * @since  WeiboPageSDK 1.0
	 */
	public String getStrOffset(){
		return mOffset ? "1" : "0";
	}
	
	/**
	 * 检测该位置是否有效
	 * @return 该位置是否合法，比如经纬度是否在-180到180之间，合法返回true,否则返回false
	 * @since  WeiboPageSDK 1.0
	 */
	boolean checkValid(){
		if(Float.isNaN(mLongitude) || mLongitude < -180 || mLongitude > 180){
			return false;
		}
		if(Float.isNaN(mLatitude) || mLatitude < -180 || mLatitude > 180){
			return false;
		}
		return true;
	}
}
