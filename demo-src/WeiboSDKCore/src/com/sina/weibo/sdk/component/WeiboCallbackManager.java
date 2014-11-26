package com.sina.weibo.sdk.component;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.utils.Utility;
import android.content.Context;
import android.text.TextUtils;

@SuppressWarnings( "unused" )
public class WeiboCallbackManager {

    private static WeiboCallbackManager sInstance;
    private Context mContext;
    private Map<String, WeiboAuthListener> mWeiboAuthListenerMap;
    private Map<String, WidgetRequestParam.WidgetRequestCallback> mWidgetRequestCallbackMap;
    
    private WeiboCallbackManager(Context context) {
        this.mContext = context;
        this.mWeiboAuthListenerMap = new HashMap<String, WeiboAuthListener>();
        this.mWidgetRequestCallbackMap = new HashMap<String, WidgetRequestParam.WidgetRequestCallback>();
    }
    
    public static synchronized WeiboCallbackManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WeiboCallbackManager(context);
        }
        return sInstance;
    }
    
    public synchronized WeiboAuthListener getWeiboAuthListener(String callbackId) {
        if (TextUtils.isEmpty(callbackId)) {
            return null;
        }
        return mWeiboAuthListenerMap.get(callbackId);
    }
    
    public synchronized void setWeiboAuthListener(String callbackId, WeiboAuthListener authListener) {
        if (TextUtils.isEmpty(callbackId) || authListener == null) {
            return;
        }
        mWeiboAuthListenerMap.put(callbackId, authListener);
    }
    
    public synchronized void removeWeiboAuthListener(String callbackId) {
        if (TextUtils.isEmpty(callbackId)) {
            return;
        }
        mWeiboAuthListenerMap.remove(callbackId);
    }
    
    public synchronized WidgetRequestParam.WidgetRequestCallback getWidgetRequestCallback(String callbackId) {
        if (TextUtils.isEmpty(callbackId)) {
            return null;
        }
        return mWidgetRequestCallbackMap.get(callbackId);
    }
    
    public synchronized void setWidgetRequestCallback(String callbackId, WidgetRequestParam.WidgetRequestCallback l) {
        if (TextUtils.isEmpty(callbackId) || l == null) {
            return;
        }
        mWidgetRequestCallbackMap.put(callbackId, l);
    }
    
    public synchronized void removeWidgetRequestCallback(String callbackId) {
        if (TextUtils.isEmpty(callbackId)) {
            return;
        }
        mWidgetRequestCallbackMap.remove(callbackId);
    }
    
    public String genCallbackKey() {
        return String.valueOf(System.currentTimeMillis());
    }
    
}
