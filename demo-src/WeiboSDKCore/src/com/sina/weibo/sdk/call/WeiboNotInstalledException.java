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
 * 在没有安装官方微博App的环境中，调用 WeiboPageUtils 中的方法时，会抛出此异常。
 * TODO：（To be design...）
 * 
 * @author SINA
 * @since 2013-11-05
 */
public class WeiboNotInstalledException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1249685537943340248L;
	
	/**
	 * 构造一个没有异常信息的<code>WeiboNotInstalledException</code>实例
	 */
	public WeiboNotInstalledException(){}
	
	
	/**
	 * 构造一个包含异常信息的<code>WeiboNotInstalledException</code>实例
	 * @param msg 异常详细信息
	 */
	public WeiboNotInstalledException(String msg){
		super(msg);
	}

}
