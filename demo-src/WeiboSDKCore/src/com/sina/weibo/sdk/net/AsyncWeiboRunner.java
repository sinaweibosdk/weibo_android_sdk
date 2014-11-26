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

package com.sina.weibo.sdk.net;

import android.content.Context;
import android.os.AsyncTask;

import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 异步回调框架类。
 * 
 * @author SINA
 * @since 2013-11-05
 */
public class AsyncWeiboRunner {
    
    private Context mContext;
    
    public AsyncWeiboRunner(Context context) {
        this.mContext = context;
    }
    
    /**
     * 根据 URL 异步请求数据，并在获取到数据后通过 {@link RequestListener} 接口进行回调。
     * 请注意：该回调函数是运行在后台线程的。
     * 
     * @param url        服务器地址
     * @param params     存放参数的容器
     * @param httpMethod "GET" or "POST"
     * @param listener   回调对象
     */
    @Deprecated    
    public void requestByThread(
            final String url, 
            final WeiboParameters params, 
            final String httpMethod, 
            final RequestListener listener) {
        
        new Thread() {
            @Override
            public void run() {
                try {
                    String resp = HttpManager.openUrl(mContext, url, httpMethod, params);
                    if (listener != null) {
                        listener.onComplete(resp);
                    }
                } catch (WeiboException e) {
                    if (listener != null) {
                        listener.onWeiboException(e);
                    }
                }
            }
        }.start();
    }    
    
    /**
     * 根据 URL 同步请求数据，如果开发者有自己的异步请求机制，请使用该函数。
     * 
     * @param url        服务器地址
     * @param params     存放参数的容器
     * @param httpMethod "GET" or "POST"
     * @param listener   回调对象
     */
    public String request(
            final String url, 
            final WeiboParameters params, 
            final String httpMethod) throws WeiboException {
        
        return HttpManager.openUrl(mContext, url, httpMethod, params);
    }

    /**
     * 根据 URL 异步请求数据，并在获取到数据后通过 {@link RequestListener} 接口进行回调。
     * 请注意：该函数是通过调用 {@link AsyncTask} 来实现异步请求。
     * 
     * @param url        服务器地址
     * @param params     存放参数的容器
     * @param httpMethod "GET" or "POST"
     * @param listener   回调对象
     */
    public void requestAsync(
            final String url, 
            final WeiboParameters params, 
            final String httpMethod, 
            final RequestListener listener) {
        new RequestRunner(mContext, url, params, httpMethod, listener).execute(new Void[] { null });
    }
    
    /**
     * 异步请求类，用于处理一些费时的操作。
     */
    private static class RequestRunner extends AsyncTask<Void, Void, AsyncTaskResult<String>> {

        //private static final String TAG = RequestRunner.class.getName();
        private final Context mContext;
        private final String mUrl;
        private final WeiboParameters mParams;
        private final String mHttpMethod;
        private final RequestListener mListener;
        
        public RequestRunner(
                Context context,
                String url, 
                WeiboParameters params, 
                String httpMethod, 
                RequestListener listener) {
            mContext = context;
            mUrl = url;
            mParams = params;
            mHttpMethod = httpMethod;
            mListener = listener;
        }

        @Override
        protected AsyncTaskResult<String> doInBackground(Void... params) {
            try {
                String result = HttpManager.openUrl(mContext, mUrl, mHttpMethod, mParams);
                return new AsyncTaskResult<String>(result);
            } catch (WeiboException e) {
                //LogUtil.e(TAG, e.getMessage());
                return new AsyncTaskResult<String>(e);
            }
        }
        
        @Override
        protected void onPreExecute() {
        }
        
        @Override
        protected void onPostExecute(AsyncTaskResult<String> result) {
            WeiboException exception = result.getError();
            if (exception != null) {
                mListener.onWeiboException(exception);
            } else {
                mListener.onComplete(result.getResult());
            }
        }
    }
    
    private static class AsyncTaskResult<T> {
        private T result;
        private WeiboException error;

        public T getResult() {
            return result;
        }

        public WeiboException getError() {
            return error;
        }

        public AsyncTaskResult(T result) {
            super();
            this.result = result;
        }

        public AsyncTaskResult(WeiboException error) {
            super();
            this.error = error;
        }
    }
}
