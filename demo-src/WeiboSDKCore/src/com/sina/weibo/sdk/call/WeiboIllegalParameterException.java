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
 * 当调用 WeiboPage 的方法时，传入了错误的参数，会抛出此异常，比如在必须参数的位置传入 null。
 * TODO：（To be design...）
 * 
 * @author SINA
 * @since 2013-11-05
 */
class WeiboIllegalParameterException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7965739598010960851L;


	/**
	 * 构造一个没有异常信息的<code>IllegalParameterException</code>实例
	 */
	public WeiboIllegalParameterException(){}
	
	
	/**
	 * 构造一个包含异常信息的<code>IllegalParameterException</code>实例
	 * @param msg 异常详细信息
	 */
	public WeiboIllegalParameterException(String msg){
		super(msg);
	}
}
