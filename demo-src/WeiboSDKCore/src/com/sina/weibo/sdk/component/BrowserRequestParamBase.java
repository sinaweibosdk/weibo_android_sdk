package com.sina.weibo.sdk.component;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

enum BrowserLauncher {
    AUTH, SHARE, WIDGET, COMMON
}

public abstract class BrowserRequestParamBase {
    public static final int EXEC_REQUEST_ACTION_OK = 0x1;
    public static final int EXEC_REQUEST_ACTION_ERROR = 0x2;
    public static final int EXEC_REQUEST_ACTION_CANCEL = 0x3;

    
    public static final String EXTRA_KEY_LAUNCHER = "key_launcher";
    protected static final String EXTRA_KEY_URL = "key_url";
    protected static final String EXTRA_KEY_SPECIFY_TITLE = "key_specify_title";
    
    protected Context mContext;
    protected String mUrl;
    protected BrowserLauncher mLaucher;
    protected String mSpecifyTitle;
    
    public BrowserRequestParamBase(Context context) {
        this.mContext = context.getApplicationContext();
    }
    
    public void setupRequestParam(Bundle data) {
        this.mUrl = data.getString(EXTRA_KEY_URL);
        this.mLaucher = (BrowserLauncher) data.getSerializable(EXTRA_KEY_LAUNCHER);
        this.mSpecifyTitle = data.getString(EXTRA_KEY_SPECIFY_TITLE);
        
        onSetupRequestParam(data);
    }
    
    protected abstract void onSetupRequestParam(Bundle data);
    
    public Bundle createRequestParamBundle() {
        Bundle data = new Bundle();
        if (!TextUtils.isEmpty(mUrl)) {
            data.putString(EXTRA_KEY_URL, mUrl);
        }
        if (mLaucher != null) {
            data.putSerializable(EXTRA_KEY_LAUNCHER, mLaucher);
        }
        if (!TextUtils.isEmpty(mSpecifyTitle)) {
            data.putString(EXTRA_KEY_SPECIFY_TITLE, mSpecifyTitle);
        }
        
        onCreateRequestParamBundle(data);
        
        return data;
    }
    
    protected abstract void onCreateRequestParamBundle(Bundle data);
    
    public abstract void execRequest(Activity act, int action);
    
    public void setUrl(String url) {
        this.mUrl = url;
    }
    public String getUrl() {
        return this.mUrl;
    }
    
    public void setLauncher(BrowserLauncher launcher) {
        this.mLaucher = launcher;
    }
    public BrowserLauncher getLauncher() {
        return this.mLaucher;
    }
    
    public void setSpecifyTitle(String title) {
        this.mSpecifyTitle = title;
    }
    public String getSpecifyTitle() {
        return this.mSpecifyTitle;
    }
    
}
