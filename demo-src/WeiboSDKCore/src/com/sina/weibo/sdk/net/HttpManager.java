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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.exception.WeiboHttpException;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.sina.weibo.sdk.utils.Utility;

/**
 * HTTP 请求类，用于管理通用的 HTTP 请求、图片上传下载等。。
 * 
 * @author SINA
 * @since 2013-11-05
 */
/*public*/ class HttpManager {
    static {
        System.loadLibrary("weibosdkcore");
    }
    
    private static final String TAG = "HttpManager";

    private static final String BOUNDARY            = getBoundry();
    private static final String MP_BOUNDARY         = "--" + BOUNDARY;
    private static final String END_MP_BOUNDARY     = "--" + BOUNDARY + "--";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    private static final String HTTP_METHOD_POST    = "POST";
    private static final String HTTP_METHOD_GET     = "GET";

    private static final int CONNECTION_TIMEOUT     = 25 * 1000;
    private static final int SOCKET_TIMEOUT         = 20 * 1000;
    /** 缓冲区大小 */
    private static final int BUFFER_SIZE            = 1024 * 8;
    
    /**
     * 根据 URL 调用异步请求函数来处理。
     * 
     * @param url    请求的地址
     * @param method "GET" or "POST", 可以是以下一种：{@link #HTTP_METHOD_GET}、 {@link #HTTP_METHOD_POST}
     * @param params 存放参数的容器
     * 
     * @return 返回响应结果，为String形式
     */
    public static String openUrl(Context context, String url, String method, WeiboParameters params) 
            throws WeiboException {
        HttpResponse response = requestHttpExecute(context, url, method, params);
        String ans = readRsponse(response);
        LogUtil.d(TAG, "Response : " + ans);
        return ans;
    }

    /**
     * 根据 URL 异步请求数据，请求服务器资源。
     * 
     * @param url    请求的地址
     * @param method "GET" or "POST", 可以是以下一种：{@link #HTTP_METHOD_GET}、 {@link #HTTP_METHOD_POST}
     * @param params 存放参数的容器
     * 
     * @return 返回响应结果，为HttpResponse形式
     * @throws WeiboException 如果发生错误，则以该异常抛出
     */    
    private static HttpResponse requestHttpExecute(Context context, String url, String method, WeiboParameters params) {
        HttpClient client = null;
        ByteArrayOutputStream baos = null;
        HttpResponse response = null;
        try {
            // Step 1: Create HTTP client
            client = getNewHttpClient();
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, NetStateManager.getAPN());
            
            HttpUriRequest request = null;
            
            /**
             * 设置共同请求参数
             */
            setHttpCommonParam(context, params);
            
            // Step 2: Prepare HTTP data
            if (method.equals(HTTP_METHOD_GET)) {
                url = url + "?" + params.encodeUrl();
                request = new HttpGet(url);
                LogUtil.d(TAG, "requestHttpExecute GET Url : " + url);
                
            } else if (method.equals(HTTP_METHOD_POST)) {
                LogUtil.d(TAG, "requestHttpExecute POST Url : " + url);
                HttpPost post = new HttpPost(url);
                request = post;

                baos = new ByteArrayOutputStream();
                if (params.hasBinaryData()) {
                    post.setHeader("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
                    buildParams(baos, params);
                } else {
                    Object value = params.get("content-type");
                    if (value != null && value instanceof String) {
                        params.remove("content-type");
                        post.setHeader("Content-Type", (String) value);
                    } else {
                        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    }

                    // Get方法和Post方法，组装URL一样
                    String postParam = params.encodeUrl();
                    LogUtil.d(TAG, "requestHttpExecute POST postParam : " + postParam);
                    baos.write(postParam.getBytes(HTTP.UTF_8));
                }
                post.setEntity(new ByteArrayEntity(baos.toByteArray()));
                
            } else if (method.equals("DELETE")) {
                request = new HttpDelete(url);
            }
            
            // Step 3: Execute HTTP request
            response = client.execute(request);
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();

            // Step 4: Get the response
            if (statusCode != HttpStatus.SC_OK) {
                String result = readRsponse(response);
                throw new WeiboHttpException(result, statusCode);
            }
        } catch (IOException e) {
            throw new WeiboException(e);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {}
            }
            
            shutdownHttpClient(client);
        }
        
        return response;
    }

    /**
     * 设置网络请求的通用参数
     * 
     * @param context
     * @param params
     */
    private static void setHttpCommonParam(Context context, WeiboParameters params) {
        /**
         * 在所有请求中，都加上AID参数
         */
        String aid = "";
        if (!TextUtils.isEmpty(params.getAppKey())) {
            aid = Utility.getAid(context, params.getAppKey());
            if (!TextUtils.isEmpty(aid)) {
                params.put("aid", aid);
            }
        }
        /**
         * 在所有请求中，都加上timestamp参数
         */
        String timestamp = getTimestamp();
        params.put("oauth_timestamp", timestamp);
        /**
         * 在所有请求中，都加上timestamp参数oauth_sign
         */
        String token = "";
        Object accessToken = params.get("access_token");
        Object refreshToken = params.get("refresh_token");
        if (accessToken != null 
                && (accessToken instanceof String)) {
            token = (String) accessToken;
        } 
        else if (refreshToken != null 
                && (refreshToken instanceof String)) {
            token = (String) refreshToken;
        }
        String oauthSign = getOauthSign(context, 
                aid, token, params.getAppKey(), timestamp);
        params.put("oauth_sign", oauthSign);
        
    }
    
    private static void shutdownHttpClient(HttpClient client) {
        if (client != null) {
            try {
                client.getConnectionManager().closeExpiredConnections();
            } catch (Exception e) {}
        }
    }
    
    public static String openUrl4RdirectURL(Context context, String url, String method, WeiboParameters params)
            throws WeiboException {
        DefaultHttpClient client = null;
        String result = "";
        try {
            client = (DefaultHttpClient)getNewHttpClient();
            client.setRedirectHandler(new RedirectHandler() {
                
                @Override
                public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
                    LogUtil.d(TAG, "openUrl4RdirectURL isRedirectRequested method");
                    return false;
                }
                
                @Override
                public URI getLocationURI(HttpResponse response, HttpContext context) throws ProtocolException {
                    LogUtil.d(TAG, "openUrl4RdirectURL getLocationURI method");
                    return null;
                }
            });/*
            client.setRedirectStrategy(new DefaultRedirectStrategy() {                
                public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)  {
                    boolean isRedirect=false;
                    try {
                        isRedirect = isRedirected(request, response, context);
                    } catch (ProtocolException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (!isRedirect) {
                        int responseCode = response.getStatusLine().getStatusCode();
                        if (responseCode == 301 || responseCode == 302) {
                            return true;
                        }
                    }
                    return isRedirect;
                }
            });*/
            
            /**
             * 设置共同请求参数
             */
            setHttpCommonParam(context, params);
            
            HttpUriRequest request = null;
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, NetStateManager.getAPN());
            if (method.equals(HTTP_METHOD_GET)) {
                url = url + "?" + params.encodeUrl();
                LogUtil.d(TAG, "openUrl4RdirectURL GET url : " + url);
                HttpGet get = new HttpGet(url);
                request = get;
            } else if (method.equals(HTTP_METHOD_POST)) {
                HttpPost post = new HttpPost(url);
                LogUtil.d(TAG, "openUrl4RdirectURL POST url : " + url);
                request = post;
            }
            HttpResponse response = client.execute(request);
            
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY 
                    || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                String redirectURL = response.getFirstHeader("Location").getValue();
                LogUtil.d(TAG, "RedirectURL = " + redirectURL);
                return redirectURL;
            } else if (statusCode == HttpStatus.SC_OK) {
                return readRsponse(response);
            } else {
                result = readRsponse(response);
                throw new WeiboHttpException(result, statusCode);
            }
        } catch (IOException e) {
            throw new WeiboException(e);
        } finally {
            shutdownHttpClient(client);
        }
    }

    /**
     * 获取Http请求的重定向地址
     * @param context
     * @param url 原始地址
     * @param method 请求方法
     * @param params
     * @return
     */
    public static String openRedirectUrl4LocationUri(Context context, String url, String method, WeiboParameters params){
        DefaultHttpClient client = null;
        CustomRedirectHandler redirectHandler = null; 
        try {
            redirectHandler = new CustomRedirectHandler(){

                @Override
                public boolean shouldRedirectUrl( String url ) {
                    return true;
                }

                @Override
                public void onReceivedException() {
                    //暂时不做处理
                }
                
            }; 
            client = (DefaultHttpClient)getNewHttpClient();
            client.setRedirectHandler(redirectHandler);
            /**
             * 设置共同请求参数
             */
            setHttpCommonParam(context, params);
            
            HttpUriRequest request = null;
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, NetStateManager.getAPN());
            if (method.equals(HTTP_METHOD_GET)) {
                url = url + "?" + params.encodeUrl();
                HttpGet get = new HttpGet(url);
                request = get;
            } else if (method.equals(HTTP_METHOD_POST)) {
                HttpPost post = new HttpPost(url);
                request = post;
            }
            request.setHeader("User-Agent", NetworkHelper.generateUA(context));
            client.execute(request);
            return redirectHandler.getRedirectUrl();
        } catch (IOException e) {
            throw new WeiboException(e);
        } finally {
            shutdownHttpClient(client);
        }
    }
    
    /**
     * 建立http请求，下载文件，支持断点续传 
     * @param context 上下文环境
     * @param url 文件的下载链接
     * @param saveDir 文件的本地保存目录
     * @param fileName 保存到本地的文件名
     * @return 下载成功后返回文件的路径，失败返回空字符串
     */
    public static synchronized String downloadFile(Context context, String url, String saveDir, String fileName) throws WeiboException{
        File savePathDir = new File(saveDir);
        if(!savePathDir.exists()){
            savePathDir.mkdirs();
        }
        File filePath = new File(savePathDir, fileName);
        if(filePath.exists()){
            return filePath.getPath();
        }
        
        if (!URLUtil.isValidUrl(url)) {
            return "";
        }
        
        HttpClient client = getNewHttpClient();

        long tempFileLength = 0L;
        File tempFile = new File(saveDir, fileName + "_temp");
        try {
            if (tempFile.exists()) {
                tempFileLength = tempFile.length();
            } else {
                tempFile.createNewFile();
            }
            HttpGet request = new HttpGet(url);
            request.setHeader("RANGE", "bytes=" + tempFileLength + "-");
            HttpResponse response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            
            long totalLength = 0;
            long startPosition = 0;
            if(statusCode == HttpStatus.SC_PARTIAL_CONTENT){
                startPosition = tempFileLength;
                Header[] rangeHeaders = response.getHeaders("Content-Range");
                if (rangeHeaders != null && rangeHeaders.length != 0) {
                    String rangValue = rangeHeaders[0].getValue();
                    totalLength = Long.parseLong(rangValue.substring(rangValue.indexOf('/') + 1));
                }
            }else if(statusCode == HttpStatus.SC_OK){
                startPosition = 0;
                Header lengthHeader = response.getFirstHeader("Content-Length");
                if (lengthHeader != null) {// 获得下载文件长度
                    totalLength = Integer.valueOf(lengthHeader.getValue());
                }
            }else{
                String result = readRsponse(response);
                throw new WeiboHttpException(result, statusCode);
            }
            
            InputStream inputStream = null;
            HttpEntity entity = response.getEntity();
            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {                   
                inputStream = new GZIPInputStream(entity.getContent());
            }else{
                inputStream = entity.getContent();
            }
            RandomAccessFile content = new RandomAccessFile(tempFile,"rw");
            content.seek(startPosition);// 偏移到制定位置。断点续传
//            long receivedLength = startPosition;
            byte[] sBuffer = new byte[1024];
            int readBytes = 0;
            
            while((readBytes = inputStream.read(sBuffer)) != -1){
                content.write(sBuffer, 0, readBytes);
            }
            content.close();
            inputStream.close();
            
            //下载完毕
            if (totalLength ==0 || tempFile.length() < totalLength) {// temp文件未完整下载抛出异常
                tempFile.delete();
            } else {// temp文件完整下载
                tempFile.renameTo(filePath);
                return filePath.getPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
            tempFile.delete();
        } finally{
            if(client != null){
                client.getConnectionManager().closeExpiredConnections();
                client.getConnectionManager().closeIdleConnections(5 * 60, TimeUnit.SECONDS);
            }
        }
        return "";
    }
    
    /**
     * 建立HttpClient对象。
     */
    private static HttpClient getNewHttpClient() {
        try {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", getSSLSocketFactory(), 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);
            
            HttpClient client = new DefaultHttpClient(ccm, params);
            return client;
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    /**
     * POST 请求时，将请求参数打包到输出流中。
     * 
     * @param baos   目标输出流
     * @param params 请求参数
     * 
     * @throws WeiboException 如果打包的过程出现异常，抛出该类型的 Exception
     */
    private static void buildParams(OutputStream baos, WeiboParameters params) throws WeiboException {
        try {
            Set<String> keys = params.keySet();
            
            // Step 1：将所有字符串数据打包到流中
            for (String key : keys) {
                Object value = params.get(key);
                if (value instanceof String) {
                    StringBuilder sb = new StringBuilder(100);
                    sb.setLength(0);
                    sb.append(MP_BOUNDARY).append("\r\n");
                    sb.append("content-disposition: form-data; name=\"").append(key).append("\"\r\n\r\n");
                    sb.append(params.get(key)).append("\r\n");
                    
                    baos.write(sb.toString().getBytes());
                }
            }
            
            // Step 2：将流数据（如图片、文件）最后打包到流中
            for (String key : keys) {
                Object value = params.get(key);
                if (value instanceof Bitmap) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(MP_BOUNDARY).append("\r\n");
                    sb.append("content-disposition: form-data; name=\"").append(key).append("\"; filename=\"file\"\r\n");
                    sb.append("Content-Type: application/octet-stream; charset=utf-8\r\n\r\n");
                    baos.write(sb.toString().getBytes());
                    
                    Bitmap bmp = (Bitmap) value;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(CompressFormat.PNG, 100, stream);
                    byte[] bytes = stream.toByteArray();
                    
                    baos.write(bytes);
                    baos.write("\r\n".getBytes());
                } else if (value instanceof ByteArrayOutputStream) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(MP_BOUNDARY).append("\r\n");
                    sb.append("content-disposition: form-data; name=\"").append(key).append("\"; filename=\"file\"\r\n");
                    sb.append("Content-Type: application/octet-stream; charset=utf-8\r\n\r\n");
                    baos.write(sb.toString().getBytes());
                    
                    ByteArrayOutputStream stream = (ByteArrayOutputStream) value;
                    baos.write(stream.toByteArray());
                    baos.write("\r\n".getBytes());
                    stream.close();
                }
            }
            baos.write(("\r\n" + END_MP_BOUNDARY).getBytes());
        } catch (IOException e) {
            throw new WeiboException(e);
        }
    }
    
    /**
     * 读取根据服务器响应结果。
     * 
     * @param response 服务器响应结果
     * 
     * @return 数据流形式
     * @throws WeiboException 如果发生错误，则以该异常抛出
     */
    private static String readRsponse(HttpResponse response) throws WeiboException {
        if (null == response) {
            return null;
        }
        
        HttpEntity entity = response.getEntity();
        InputStream inputStream = null;
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        try {
            inputStream = entity.getContent();
            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                inputStream = new GZIPInputStream(inputStream);
            }

            int readBytes = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((readBytes = inputStream.read(buffer)) != -1) {
                content.write(buffer, 0, readBytes);
            }
            String result = new String(content.toByteArray(), HTTP.UTF_8);
            LogUtil.d(TAG, "readRsponse result : " + result);
            return result;
        } catch (IOException e) {
            throw new WeiboException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 产生11位的boundary
     */
    private static String getBoundry() {
        StringBuffer sb = new StringBuffer();
        for (int t = 1; t < 12; t++) {
            long time = System.currentTimeMillis() + t;
            if (time % 3 == 0) {
                sb.append((char) time % 9);
            } else if (time % 3 == 1) {
                sb.append((char) (65 + time % 26));
            } else {
                sb.append((char) (97 + time % 26));
            }
        }
        return sb.toString();
    }

    private static SSLSocketFactory sSSLSocketFactory;
    private static SSLSocketFactory getSSLSocketFactory() {
        if(sSSLSocketFactory == null){
            try{
                // Create a KeyStore containing our trusted CAs
                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                
                Certificate cnCertificate = getCertificate("cacert_cn.cer");
                Certificate comCertificate = getCertificate("cacert_com.cer");
                keyStore.setCertificateEntry("cnca", cnCertificate);
                keyStore.setCertificateEntry("comca", comCertificate);
                
                sSSLSocketFactory = new SSLSocketFactory(keyStore);
                LogUtil.d(TAG, "getSSLSocketFactory noraml !!!!!");
            }catch(Exception e){
                e.printStackTrace();
                //如果加载内置证书失败，则返回系统默认
                sSSLSocketFactory = SSLSocketFactory.getSocketFactory();
                LogUtil.d(TAG, "getSSLSocketFactory error default !!!!!");
            }
        }
        return sSSLSocketFactory;
    }
    
    private static Certificate getCertificate(String name) 
            throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        final InputStream certInput = HttpManager.class.getResourceAsStream(name);
        Certificate certificate;
        try {
            certificate = cf.generateCertificate(certInput);
        } finally {
            if (certInput != null) {
                certInput.close();
            }
        }
        return certificate;
    }
    
    private static String getTimestamp() {
        long timestamp = (long)(System.currentTimeMillis() / 1000);
        return String.valueOf(timestamp);
    }
    
    private static String getOauthSign(Context context, 
            String aid, String accessToken, String appKey, String timestamp) {
        /**
         * 算法
         * (aid)+access_token+appkey+privateKey+timestamp(seconds)
         */
        StringBuilder part1 = new StringBuilder("");
        if (!TextUtils.isEmpty(aid)) {
            part1.append(aid);
        }
        if (!TextUtils.isEmpty(accessToken)) {
            part1.append(accessToken);
        }
        if (!TextUtils.isEmpty(appKey)) {
            part1.append(appKey);
        }
        
        return calcOauthSignNative(context, part1.toString(), timestamp);
    }
    
    private native static String calcOauthSignNative(Context context, String part1, String part2); 
    
}
