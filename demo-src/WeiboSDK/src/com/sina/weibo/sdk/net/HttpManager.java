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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
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

import android.content.Context;
import android.text.TextUtils;

import com.sina.weibo.sdk.auth.WeiboParameters;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.exception.WeiboHttpException;
import com.sina.weibo.sdk.utils.Utility;

/**
 * HTTP 请求类，用于管理通用的 HTTP 请求、图片上传下载等。
 * TODO：（To be design...）
 * 
 * @author SINA
 * @since 2013-11-05
 */
public class HttpManager {

    // private static final String BOUNDARY = "7cd4a6d158c";
    private static final String BOUNDARY = getBoundry();
    private static final String MP_BOUNDARY = "--" + BOUNDARY;
    private static final String END_MP_BOUNDARY = "--" + BOUNDARY + "--";
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    private static final String HTTPMETHOD_POST = "POST";
    private static final String HTTPMETHOD_GET = "GET";

    private static final int SET_CONNECTION_TIMEOUT = 5 * 1000;
    private static final int SET_SOCKET_TIMEOUT = 20 * 1000;

    /**
     * 根据 URL 异步请求数据。
     * 
     * @param url    请求的地址
     * @param method "GET" or "POST"
     * @param params 存放参数的容器
     * @param file   文件路径，如果 是发送带有照片的微博的话，此参数为图片在 SdCard 里的绝对路径
     * 
     * @return 返回响应结果
     * @throws WeiboException 如果发生错误，则以该异常抛出
     */
    public static String openUrl(String url, String method, WeiboParameters params, String file)
            throws WeiboException {
        String result = "";
        try {
            HttpClient client = getNewHttpClient();
            HttpUriRequest request = null;
            ByteArrayOutputStream bos = null;
            client.getParams()
                    .setParameter(ConnRoutePNames.DEFAULT_PROXY, NetStateManager.getAPN());
            if (method.equals(HTTPMETHOD_GET)) {
                url = url + "?" + Utility.encodeUrl(params);
                HttpGet get = new HttpGet(url);
                request = get;
            } else if (method.equals(HTTPMETHOD_POST)) {
                HttpPost post = new HttpPost(url);
                request = post;
                byte[] data = null;
                String _contentType = params.getValue("content-type");

                bos = new ByteArrayOutputStream();
                if (!TextUtils.isEmpty(file)) {
                    paramToUpload(bos, params);
                    post.setHeader("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
                    // 不在进行图片压缩
                    // Utility.UploadImageUtils.revitionPostImageSize(file);
                    imageContentToUpload(bos, file);
                } else {
                    if (_contentType != null) {
                        params.remove("content-type");
                        post.setHeader("Content-Type", _contentType);
                    } else {
                        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    }

                    String postParam = Utility.encodeParameters(params);
                    data = postParam.getBytes("UTF-8");
                    bos.write(data);
                }
                data = bos.toByteArray();
                bos.close();
                ByteArrayEntity formEntity = new ByteArrayEntity(data);
                post.setEntity(formEntity);
            } else if (method.equals("DELETE")) {
                request = new HttpDelete(url);
            }
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();

            if (statusCode != 200) {
                result = readHttpResponse(response);
                throw new WeiboHttpException(result, statusCode);
            }
            result = readHttpResponse(response);
            return result;
        } catch (IOException e) {
            throw new WeiboException(e);
        }
    }

    /**
     * 由于发私信和发微博对 formdata name="pic" 还是 name="file" 要求不一致，所以提供一个新的接口。
     * 
     * @param url    请求的地址
     * @param method "GET" or "POST"
     * @param params 存放参数的容器
     * @param file   文件路径，如果是发送带有照片的微博的话，此参数为图片在 SdCard 里的绝对路径
     * 
     * @return 返回响应结果
     * @throws WeiboException 如果发生错误，则以该异常抛出
     */
    public static String uploadFile(String url, String method, WeiboParameters params, String file)
            throws WeiboException {
        String result = "";
        try {
            HttpClient client = getNewHttpClient();
            HttpUriRequest request = null;
            ByteArrayOutputStream bos = null;
            client.getParams()
                    .setParameter(ConnRoutePNames.DEFAULT_PROXY, NetStateManager.getAPN());
            if (method.equals(HTTPMETHOD_GET)) {
                url = url + "?" + Utility.encodeUrl(params);
                HttpGet get = new HttpGet(url);
                request = get;
            } else if (method.equals(HTTPMETHOD_POST)) {
                HttpPost post = new HttpPost(url);
                request = post;
                byte[] data = null;
                String _contentType = params.getValue("content-type");

                bos = new ByteArrayOutputStream();
                if (!TextUtils.isEmpty(file)) {
                    paramToUpload(bos, params);
                    post.setHeader("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
                    // 不在进行图片压缩
                    // Utility.UploadImageUtils.revitionPostImageSize(file);
                    fileToUpload(bos, file);
                } else {
                    if (_contentType != null) {
                        params.remove("content-type");
                        post.setHeader("Content-Type", _contentType);
                    } else {
                        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    }

                    String postParam = Utility.encodeParameters(params);
                    data = postParam.getBytes("UTF-8");
                    bos.write(data);
                }
                data = bos.toByteArray();
                bos.close();
                ByteArrayEntity formEntity = new ByteArrayEntity(data);
                post.setEntity(formEntity);
            } else if (method.equals("DELETE")) {
                request = new HttpDelete(url);
            }
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();

            if (statusCode != 200) {
                result = readHttpResponse(response);
                throw new WeiboHttpException(result, statusCode);
            }
            result = readHttpResponse(response);
            return result;
        } catch (IOException e) {
            throw new WeiboException(e);
        }
    }

    /**
     * 根据 URL 异步请求数据。
     * 
     * @param context 应用程序上下文
     * @param url     请求的地址
     * @param method  "GET" or "POST"
     * @param params  存放参数的容器
     * @param file    文件路径，如果是发送带有照片的微博的话，此参数为图片在 SdCard 里的绝对路径
     * 
     * @return 返回响应结果
     * @throws WeiboException 如果发生错误，则以该异常抛出
     */
    public static ByteArrayOutputStream openUrl4Binary(Context context, String url, String method,
            WeiboParameters params, String file) throws WeiboException {
        ByteArrayOutputStream result = null;
        try {
            HttpClient client = getNewHttpClient();
            HttpUriRequest request = null;
            ByteArrayOutputStream bos = null;
            client.getParams()
                    .setParameter(ConnRoutePNames.DEFAULT_PROXY, NetStateManager.getAPN());
            if (method.equals(HTTPMETHOD_GET)) {
                url = url + "?" + Utility.encodeUrl(params);
                HttpGet get = new HttpGet(url);
                request = get;
            } else if (method.equals(HTTPMETHOD_POST)) {
                HttpPost post = new HttpPost(url);
                request = post;
                byte[] data = null;
                String _contentType = params.getValue("content-type");

                bos = new ByteArrayOutputStream();
                if (!TextUtils.isEmpty(file)) {
                    paramToUpload(bos, params);
                    post.setHeader("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
                    Utility.UploadImageUtils.revitionPostImageSize(context, file);
                    imageContentToUpload(bos, file);
                } else {
                    if (_contentType != null) {
                        params.remove("content-type");
                        post.setHeader("Content-Type", _contentType);
                    } else {
                        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    }

                    String postParam = Utility.encodeParameters(params);
                    data = postParam.getBytes("UTF-8");
                    bos.write(data);
                }
                data = bos.toByteArray();
                bos.close();
                ByteArrayEntity formEntity = new ByteArrayEntity(data);
                post.setEntity(formEntity);
            } else if (method.equals("DELETE")) {
                request = new HttpDelete(url);
            }
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            int statusCode = status.getStatusCode();

            if (statusCode != 200) {
                String resultStr = readHttpResponse(response);
                throw new WeiboHttpException(resultStr, statusCode);
            }
            result = readBytesFromHttpResponse(response);
            return result;
        } catch (IOException e) {
            throw new WeiboException(e);
        }
    }

    private static HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();

            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            HttpConnectionParams.setConnectionTimeout(params, SET_CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, SET_SOCKET_TIMEOUT);
            HttpClient client = new DefaultHttpClient(ccm, params);
            // if (NetState.Mobile == NetStateManager.CUR_NETSTATE) {
            // // 获取当前正在使用的APN接入点
            // HttpHost proxy = NetStateManager.getAPN();
            // if (null != proxy) {
            // client.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
            // proxy);
            // }
            // }
            return client;
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    private static void paramToUpload(OutputStream baos, WeiboParameters params)
            throws WeiboException {
        String key = "";
        for (int loc = 0; loc < params.size(); loc++) {
            key = params.getKey(loc);
            StringBuilder temp = new StringBuilder(10);
            temp.setLength(0);
            temp.append(MP_BOUNDARY).append("\r\n");
            temp.append("content-disposition: form-data; name=\"").append(key).append("\"\r\n\r\n");
            temp.append(params.getValue(key)).append("\r\n");
            byte[] res = temp.toString().getBytes();
            try {
                baos.write(res);
            } catch (IOException e) {
                throw new WeiboException(e);
            }
        }
    }

    private static void imageContentToUpload(OutputStream out, String imgpath)
            throws WeiboException {
        if (imgpath == null) {
            return;
        }
        StringBuilder temp = new StringBuilder();

        temp.append(MP_BOUNDARY).append("\r\n");
        temp.append("Content-Disposition: form-data; name=\"pic\"; filename=\"")
                .append("news_image").append("\"\r\n");
        String filetype = "image/png";
        temp.append("Content-Type: ").append(filetype).append("\r\n\r\n");
        // temp.append("content-disposition: form-data; name=\"file\"; filename=\"")
        // .append(imgpath).append("\"\r\n");
        // temp.append("Content-Type: application/octet-stream; charset=utf-8\r\n\r\n");
        byte[] res = temp.toString().getBytes();
        FileInputStream input = null;
        try {
            out.write(res);
            input = new FileInputStream(imgpath);
            byte[] buffer = new byte[1024 * 50];
            while (true) {
                int count = input.read(buffer);
                if (count == -1) {
                    break;
                }
                out.write(buffer, 0, count);
            }
            out.write("\r\n".getBytes());
            out.write(("\r\n" + END_MP_BOUNDARY).getBytes());
        } catch (IOException e) {
            throw new WeiboException(e);
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new WeiboException(e);
                }
            }
        }
    }

    private static void fileToUpload(OutputStream out, String filepath) throws WeiboException {
        if (filepath == null) {
            return;
        }
        StringBuilder temp = new StringBuilder();

        temp.append(MP_BOUNDARY).append("\r\n");

        temp.append("content-disposition: form-data; name=\"file\"; filename=\"").append(filepath)
                .append("\"\r\n");
        temp.append("Content-Type: application/octet-stream; charset=utf-8\r\n\r\n");
        byte[] res = temp.toString().getBytes();
        FileInputStream input = null;
        try {
            out.write(res);
            input = new FileInputStream(filepath);
            byte[] buffer = new byte[1024 * 50];
            while (true) {
                int count = input.read(buffer);
                if (count == -1) {
                    break;
                }
                out.write(buffer, 0, count);
            }
            out.write("\r\n".getBytes());
            out.write(("\r\n" + END_MP_BOUNDARY).getBytes());
        } catch (IOException e) {
            throw new WeiboException(e);
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    throw new WeiboException(e);
                }
            }
        }
    }

    /**
     * 读取HttpResponse数据
     * 
     * @param response
     * @return
     */
    private static String readHttpResponse(HttpResponse response) {
        String result = "";
        HttpEntity entity = response.getEntity();
        InputStream inputStream;
        try {
            inputStream = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();

            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                inputStream = new GZIPInputStream(inputStream);
            }

            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }
            result = new String(content.toByteArray(), "UTF-8");
            return result;
        } catch (IllegalStateException e) {
        } catch (IOException e) {
        }
        return result;
    }

    /**
     * 读取HttpResponse 字节流ByteArrayOutputStream
     * 
     * @param response
     * @return
     */
    private static ByteArrayOutputStream readBytesFromHttpResponse(HttpResponse response) {
        HttpEntity entity = response.getEntity();
        InputStream inputStream;
        try {
            inputStream = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();

            Header header = response.getFirstHeader("Content-Encoding");
            if (header != null && header.getValue().toLowerCase().indexOf("gzip") > -1) {
                inputStream = new GZIPInputStream(inputStream);
            }

            int readBytes = 0;
            byte[] sBuffer = new byte[512];
            while ((readBytes = inputStream.read(sBuffer)) != -1) {
                content.write(sBuffer, 0, readBytes);
            }
            return content;
        } catch (IllegalStateException e) {
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 产生11位的boundary
     */
    static String getBoundry() {
        StringBuffer _sb = new StringBuffer();
        for (int t = 1; t < 12; t++) {
            long time = System.currentTimeMillis() + t;
            if (time % 3 == 0) {
                _sb.append((char) time % 9);
            } else if (time % 3 == 1) {
                _sb.append((char) (65 + time % 26));
            } else {
                _sb.append((char) (97 + time % 26));
            }
        }
        return _sb.toString();
    }

}
