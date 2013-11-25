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

package com.sina.weibo.sdk.openapi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.os.Handler;
import android.os.Message;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;


/**
 * 微博 OpenAPI 的基类，每个接口类都继承了此抽象类。
 * TODO：（To be design...）
 * 
 * @author SINA
 * @since 2013-11-05
 */
public abstract class AbsOpenAPI {
    
    /** 用于转发回调函数的消息 */
    private static final int MSG_ON_COMPLETE            = 1;
    private static final int MSG_ON_COMPLETE_FOR_BINARY = 2;
    private static final int MSG_ON_IOEXCEPTION         = 3;
    private static final int MSG_ON_ERROR               = 4;
    
    /** 访问微博服务接口的地址 */
    protected static final String API_SERVER = "https://api.weibo.com/2";
    /** POST 请求方式 */
    protected static final String HTTPMETHOD_POST = "POST";
    /** GET 请求方式 */
    protected static final String HTTPMETHOD_GET = "GET";
    /** HTTP 参数 */
    protected static final String KEY_ACCESS_TOKEN  = "access_token";
    /** 当前的 Token */
    protected Oauth2AccessToken mAccessToken;
    /** 异步请求回调接口 */
    private RequestListener mRequestListener;

    /**
     * 构造函数，使用各个 API 接口提供的服务前必须先获取 Token。
     * 
     * @param accesssToken 访问令牌
     */
    public AbsOpenAPI(Oauth2AccessToken accessToken) {
        mAccessToken = accessToken;
    }

    /**
     * HTTP 异步请求。
     * 
     * @param url        请求的地址
     * @param params     请求的参数
     * @param httpMethod 请求方法
     * @param listener   请求后的回调接口
     */
    protected void request(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        mRequestListener = listener;
        
        // 异步请求
        params.add(KEY_ACCESS_TOKEN, mAccessToken.getToken());
        AsyncWeiboRunner.request(url, params, httpMethod, mInternalListener);
    }

    /**
     * 该 Handler 用于将后台线程回调转发到 UI 线程。
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (null == mRequestListener) {
                return;
            }
            
            switch (msg.what) {
            case MSG_ON_COMPLETE:
                mRequestListener.onComplete((String)msg.obj);
                break;
    
            case MSG_ON_COMPLETE_FOR_BINARY:
                mRequestListener.onComplete4binary((ByteArrayOutputStream)msg.obj);
                break;
    
            case MSG_ON_IOEXCEPTION:
                mRequestListener.onIOException((IOException)msg.obj);
                break;
    
            case MSG_ON_ERROR:
                mRequestListener.onError((WeiboException)msg.obj);
                break;
    
            default:
                break;
            }
        }
    };
    
    /**
     * 请注意：默认情况下，{@link RequestListener} 对应的回调是运行在后台线程中的，
     *        因此，需要使用 Handler 来配合更新 UI。
     */
    private RequestListener mInternalListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            mHandler.obtainMessage(MSG_ON_COMPLETE, response).sendToTarget();
        }
    
        @Override
        public void onComplete4binary(ByteArrayOutputStream responseOS) {
            mHandler.obtainMessage(MSG_ON_COMPLETE_FOR_BINARY, responseOS).sendToTarget();
        }
    
        @Override
        public void onIOException(IOException e) {
            mHandler.obtainMessage(MSG_ON_IOEXCEPTION, e).sendToTarget();
        }
    
        @Override
        public void onError(WeiboException e) {
            mHandler.obtainMessage(MSG_ON_ERROR, e).sendToTarget();
        }
    };
}
