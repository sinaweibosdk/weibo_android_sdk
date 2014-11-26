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

package com.sina.weibo.sdk.api.share;

import android.app.Activity;
import android.content.Intent;

import com.sina.weibo.sdk.ApiUtils;
import com.sina.weibo.sdk.api.CmdObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;

/**
 * 该类是进行微博分享的接口类。有以下两种方式进行微博分享：
 * <li>通过第三方应用唤起微博客户端进行分享；
 * <li>通过微博客户端唤起第三方应用进行分享。（注：该分享方式需要合作接入，详情请查看：
 * <a href="http://t.cn/aex4JF">2、微博原生分享 --> b) 集成分享</a>）
 * 
 * @author SINA
 * @since 2013-10-22
 */
public interface IWeiboShareAPI {
    
    /**
     * 检查用户是否安装了微博客户端程序
     * 
     * @return 如果安装了，返回 true；否则，返回 false。
     */
    public boolean isWeiboAppInstalled();

    /**
     * 检查微博是否支持此 SDK。
     * 
     * @return 如果支持，返回 true；否则，返回 false。
     */
    public boolean isWeiboAppSupportAPI();

    /**
     * 获取当前微博客户端程序支持的 SDK 的最高版本号。
     * 如果微博版本号 < {@link ApiUtils#BUILD_INT_VER_2_2}，不支持 {@link VoiceObject}
     * 如果微博版本号 < {@link ApiUtils#BUILD_INT_VER_2_3}，不支持 {@link CmdObject}
     * 
     * @return 版本号
     */
    public int getWeiboAppSupportAPI();

    /**
     * 注册第三方应用 到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
     * 默认情况下，如果未安装微博客户端，在注册时，会弹出对话框询问用户是否下载微博客户端。
     * 
     * @return 成功返回 true，会显示在微博第三方列表中，否则不显示
     */
    public boolean registerApp();

    /**
     * 反注册微博 APP，成功后将不再显示在微博的 APP 列表中
     */
    // public void unregisterApp();

    /**
     * 处理微博客户端分享后的响应数据。
     * <p>当从当前应用唤起微博发博器并进行分享后，返回到当前应用时，需要在 
     * {@link Activity#onNewIntent} 和 {@link Activity#onCreate} 函数中，主动调用该
     * 函数处理分享后的响应数据。在 {@link Activity#onCreate} 函数中调用该函数是为了防
     * 止该 Activity 处于后台时，可能会由于内存不足被杀掉了而导致的重新初始化。</p>
     * <p><em>执行流程：本应用->微博->本应用</em></p>
     * 
     * @param intent  数据内容
     * @param handler 处理应答对应的回调函数
     * 
     * @return 成功返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
     *         失败返回 false，不调用上述回调
     */
    public boolean handleWeiboResponse(Intent intent, IWeiboHandler.Response handler);

    /**
     * 处理微博客户端发送过来的请求。
     * <p>当微博客户端唤起当前应用并进行分享时，需要在 {@link Activity#onCreate} 和 
     * {@link Activity#onNewIntent} 函数中，主动调用该函数来处理客户端发送过来的请求。</p>
     * <p><em>执行流程：微博->本应用->微博</em></p>
     * 
     * @param intent  数据内容
     * @param handler 处理请求后对应的回调函数
     * 
     * @return 成功返回 true，失败返回 false，并调用 {@link IWeiboHandler.Request#onRequest}
     */
    public boolean handleWeiboRequest(Intent intent, IWeiboHandler.Request handler);

    /**
     * 启动微博客户端程序。
     * 
     * @return 启动成功，返回 true；否则，返回 false
     */
    public boolean launchWeibo(Activity act);

    /**
     * 发送微博分享请求给微博客户端程序。
     * 默认情况下，如果未安装微博客户端，会分享失败
     * 
     * @param request 请求的内容。
     * 
     * @return 发送成功，返回 true；否则，返回 false
     */
    public boolean sendRequest(Activity act, BaseRequest request);
    
    /**
     * 发送微博分享整套方案（包括 授权+分享）
     * 如果微博客户端安装，则会调用微博客户端进行分享
     * 如果微博客户端没有安装，则会弹出SDK中的分享微博页面进行分享
     * （这种情况如果 token 传空，则会帮助用户先进行授权）
     * 
     * @param act
     * @param request
     * @param authInfo
     * @param token
     * @param sharedListener
     * @return
     */
    public boolean sendRequest(Activity act, BaseRequest request, AuthInfo authInfo, String token, WeiboAuthListener authListener);

    /**
     * 收到微博客户端程序的请求后，发送对应的应答内容给微博客户端程序。
     * 
     * @param response 应答数据
     * 
     * @return 响应成功，返回 true；否则，返回 false
     */
    public boolean sendResponse(BaseResponse response);

    /**
     * 注册微博客户端下载的监听器。
     * 当用户取消下载时，该监听器的 {@link IWeiboDownloadListener#onCancel} 函数被调用。
     * 
     * @param listener 监听微博客户端下载的 Listener
     */
    //public void registerWeiboDownloadListener(IWeiboDownloadListener listener);
    
    /**
     * 检查微博客户端环境是否正常。
     * 
     * @return 如果微博客户端已安装并且合法，返回 true；否则，返回 false
     * 
     * @throws WeiboShareException 微博客户端不合法时，抛出异常
     */
    //public boolean checkEnvironment() throws WeiboShareException;
    
    /**
     * 检查微博客户端环境是否正常。
     * 
     * @param bShowDownloadDialog 如果未安装微博，是否弹出对话框询问用户下载微博客户端
     * 
     * @return 如果微博客户端已安装并且合法，返回 true；否则，返回 false
     * 
     * @throws WeiboShareException 微博客户端不合法时，抛出异常
     */
    //public boolean checkEnvironment(boolean bShowDownloadDialog) throws WeiboShareException;
    
    /**
     * 是否支持微博支付功能。
     * 
     * @return 支持返回 true，不支持返回 false
     */
    boolean isSupportWeiboPay();
    
    /**
     * 启动微博支付功能。
     * 
     * @param payArgs 支付功能参数
     * @return 启动功能返回 true，其它情况返回 false
     */
    boolean launchWeiboPay(Activity act, String payArgs);
}
