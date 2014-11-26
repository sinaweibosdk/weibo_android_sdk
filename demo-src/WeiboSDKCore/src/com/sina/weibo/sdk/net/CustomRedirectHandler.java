package com.sina.weibo.sdk.net;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.protocol.HttpContext;

import android.text.TextUtils;

import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 处理http请求的重定向 跳转
 * @author sinadev
 * @since 2014/11/19
 *
 */
public abstract class CustomRedirectHandler implements RedirectHandler{

    private static final String TAG = CustomRedirectHandler.class.getCanonicalName();
    
    private static final int MAX_REDIRECT_COUNT = 15;
    
    int redirectCount;//重定向次数
    String redirectUrl;//重定向地址
    
    private String tempRedirectUrl;
    
    @Override
    public URI getLocationURI( HttpResponse response, HttpContext context )
            throws ProtocolException {
        LogUtil.d(TAG, "CustomRedirectHandler getLocationURI getRedirectUrl : "+tempRedirectUrl);
        if(!TextUtils.isEmpty(tempRedirectUrl)){
            return URI.create(tempRedirectUrl);
        }else{
            return null;
        }
    }

    @Override
    public boolean isRedirectRequested( HttpResponse response, HttpContext context ) {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY 
                || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
            tempRedirectUrl = response.getFirstHeader("Location").getValue();
            if(!TextUtils.isEmpty(tempRedirectUrl) && redirectCount < MAX_REDIRECT_COUNT
                    && shouldRedirectUrl(tempRedirectUrl)){
                redirectCount++;
                return true;
            }
        }else if(statusCode == HttpStatus.SC_OK){
            redirectUrl = tempRedirectUrl;
        }else{
            onReceivedException();
        }
        return false;
    }
    
    /*
     * 是否需要重定向，子类实现改方法可以对url实现拦截
     */
    public abstract boolean shouldRedirectUrl(String url);
    
    /*
     * 当请求过程中出现网络异常时，调用此方法
     * To be design
     */
    public abstract void onReceivedException();
    
    public String getRedirectUrl(){
        return this.redirectUrl;
    }
    
    public int getRedirectCount(){
        return this.redirectCount;
    }
}
