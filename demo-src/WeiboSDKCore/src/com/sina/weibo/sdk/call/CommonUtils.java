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
import java.util.Set;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sina.weibo.sdk.constant.WBPageConstants;

/**
 * TODO：（To be design...）
 * 
 * @author SINA
 * @since 2013-11-07
 */
class CommonUtils {

	public static String buildUriQuery(HashMap<String, String> paramsMap) {
		StringBuilder queryBuilder = new StringBuilder();
		Set<String> keySet = paramsMap.keySet();
		for (String key : keySet) {
			String value = paramsMap.get(key);
			if (value != null) {
				queryBuilder.append("&").append(key).append("=").append(value);
			}
		}
		String query = queryBuilder.toString();
		return query.replaceFirst("&", "?");
	}

//	public static void openWeiboActivity(Context context, String action,
//			String packageUri, String uri) throws WeiboNotInstalledException {
//		try {
//			if (packageUri != null) {
//				Intent intent = new Intent();
//				intent.setAction(action);
//				intent.setData(Uri.parse(packageUri));
//				context.startActivity(intent);
//			} else {
//				Intent intent = new Intent();
//				intent.setAction(action);
//				intent.setData(Uri.parse(uri));
//				context.startActivity(intent);
//			}
//
//		} catch (ActivityNotFoundException exception) {
//			if (packageUri != null) {
//				try {
//					Intent intent = new Intent();
//					intent.setAction(action);
//					intent.setData(Uri.parse(uri));
//					context.startActivity(intent);
//				} catch (ActivityNotFoundException e) {
//					throw new WeiboNotInstalledException(
//							Constants.ExceptionMsg.WEIBO_NOT_INSTALLED);
//				}
//			} else {
//				throw new WeiboNotInstalledException(
//						Constants.ExceptionMsg.WEIBO_NOT_INSTALLED);
//			}
//
//		}
//
//	}
	
	public static void openWeiboActivity(Context context, String action,String uri,String packageName) throws WeiboNotInstalledException {
		try {
			if (packageName != null) {
				Intent intent = new Intent();
				intent.setAction(action);
				intent.setData(Uri.parse(uri));
				intent.setPackage(packageName);
				context.startActivity(intent);
			} else {
				Intent intent = new Intent();
				intent.setAction(action);
				intent.setData(Uri.parse(uri));
				context.startActivity(intent);
			}

		} catch (ActivityNotFoundException exception) {
			if (packageName != null) {
				try {
					Intent intent = new Intent();
					intent.setAction(action);
					intent.setData(Uri.parse(uri));
					context.startActivity(intent);
				} catch (ActivityNotFoundException e) {
					throw new WeiboNotInstalledException(
							WBPageConstants.ExceptionMsg.WEIBO_NOT_INSTALLED);
				}
			} else {
				throw new WeiboNotInstalledException(
						WBPageConstants.ExceptionMsg.WEIBO_NOT_INSTALLED);
			}

		}

	}

	public static void openWeiboActivity(Context context, String action,
			String uri) throws WeiboNotInstalledException {
		try {
			Intent intent = new Intent();
			intent.setAction(action);
			intent.setData(Uri.parse(uri));
			context.startActivity(intent);
		} catch (ActivityNotFoundException exception) {
			throw new WeiboNotInstalledException(
					WBPageConstants.ExceptionMsg.WEIBO_NOT_INSTALLED);
		}

	}
}
