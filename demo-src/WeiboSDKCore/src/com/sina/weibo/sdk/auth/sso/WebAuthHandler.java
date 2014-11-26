package com.sina.weibo.sdk.auth.sso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.sina.weibo.sdk.WbAppInstallActivator;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.component.AuthRequestParam;
import com.sina.weibo.sdk.component.WeiboSdkBrowser;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.NetworkHelper;
import com.sina.weibo.sdk.utils.ResourceManager;
import com.sina.weibo.sdk.utils.UIUtils;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 该类是进行 OAuth2.0 Web 授权认证的类
 * 
 * @author SINA
 * @since 2014-05-06
 */
@SuppressWarnings("unused")
class WebAuthHandler {
    private final static String TAG = WebAuthHandler.class.getName();
    
    /** Strings **/
    private static final String NETWORK_NOT_AVAILABLE_EN    = "Network is not available";
    private static final String NETWORK_NOT_AVAILABLE_ZH_CN = "无法连接到网络，请检查网络配置"; 
    private static final String NETWORK_NOT_AVAILABLE_ZH_TW = "無法連接到網络，請檢查網络配置";
    
    /** 获取 code 或 token 的 Base URL */
    private static final String OAUTH2_BASE_URL = "https://open.weibo.cn/oauth2/authorize?";
    /** 获取 code */
    private static final int OBTAIN_AUTH_CODE  = 0;
    /** 获取 token */
    private static final int OBTAIN_AUTH_TOKEN = 1;
    
    /** 应用程序上下文环境 */
    private Context mContext;
    /** 微博授权认证所需信息 */
    private AuthInfo mAuthInfo;
    
    public WebAuthHandler(Context context, AuthInfo authInfo) {
        this.mContext = context;
        this.mAuthInfo = authInfo;
    }
    
    public AuthInfo getAuthInfo() {
        return this.mAuthInfo;
    }
    
    /**
     * 微博授权认证函数，用于获取 Token。
     * 
     * @param listener 微博授权认证的回调接口
     */
    public void anthorize(WeiboAuthListener listener) {
        authorize(listener, OBTAIN_AUTH_TOKEN);
    }
    
    /**
     * 微博授权认证函数，用于获取 Token 或者 Code。
     * <b>对于只想获取 Code 的情况，第三方需要自己通过 Code 来换取 Token，请参考 DEMO 实现。</b>
     * 
     * @param listener 微博授权认证的回调接口
     * @param type     微博授权时，指定或者 code 还是 token，可以是以下常量中的一种：
     *                 {@link #OBTAIN_AUTH_CODE}，{@link #OBTAIN_AUTH_TOKEN}。
     */
    public void authorize(WeiboAuthListener listener, int type) {
        startDialog(listener, type);
        WbAppInstallActivator.getInstance(mContext, mAuthInfo.getAppKey()).activateWeiboInstall();
    }

    /**
     * 启动微博认证对话框。
     * 
     * @param listener 微博授权认证的回调接口
     * @param type     微博授权时，指定或者 code 还是 token，可以是以下常量中的一种：
     *                 {@link #OBTAIN_AUTH_CODE}，{@link #OBTAIN_AUTH_TOKEN}。
     */
    private void startDialog(WeiboAuthListener listener, int type) {
        if (null == listener) {
            return;
        }
        
        WeiboParameters requestParams = new WeiboParameters(mAuthInfo.getAppKey());
        requestParams.put(WBConstants.AUTH_PARAMS_CLIENT_ID,     mAuthInfo.getAppKey());
        requestParams.put(WBConstants.AUTH_PARAMS_REDIRECT_URL,  mAuthInfo.getRedirectUrl());
        requestParams.put(WBConstants.AUTH_PARAMS_SCOPE,         mAuthInfo.getScope());
        requestParams.put(WBConstants.AUTH_PARAMS_RESPONSE_TYPE, "code");
        //requestParams.put(WBConstants.AUTH_PARAMS_DISPLAY,       "mobile");
        requestParams.put(WBConstants.AUTH_PARAMS_VERSION,       WBConstants.WEIBO_SDK_VERSION_CODE);
        
        /**
         * 添加AID参数
         */
        String aid = Utility.getAid(mContext, mAuthInfo.getAppKey());
        if (!TextUtils.isEmpty(aid)) {
            requestParams.put(WBConstants.AUTH_PARAMS_AID, aid);
        }
        
        // 对于想直接获取 Token 的情况，需要以下参数；只想获取 Code 的时，不需要以下参数
        if (OBTAIN_AUTH_TOKEN == type) {
            requestParams.put(WBConstants.AUTH_PARAMS_PACKAGE_NAME, mAuthInfo.getPackageName());
            requestParams.put(WBConstants.AUTH_PARAMS_KEY_HASH,     mAuthInfo.getKeyHash());
        }

        String url = OAUTH2_BASE_URL + requestParams.encodeUrl();
        if (!NetworkHelper.hasInternetPermission(mContext)) {
            UIUtils.showAlert(mContext, "Error", "Application requires permission to access the Internet");
        } else {
            /**if (NetworkHelper.isNetworkAvailable(mContext)) {**/
                //new WeiboDialog(mContext, url, listener, mAuthInfo).show();
                AuthRequestParam req = new AuthRequestParam(mContext);
                req.setAuthInfo(mAuthInfo);
                req.setAuthListener(listener);
                req.setUrl(url);
                req.setSpecifyTitle("微博登录");
                Bundle data = req.createRequestParamBundle();
                
                Intent intent = new Intent(mContext, WeiboSdkBrowser.class);
                intent.putExtras(data);
                mContext.startActivity(intent);
            /**} else {
                String networkNotAvailable = ResourceManager.getString(mContext, 
                        NETWORK_NOT_AVAILABLE_EN, 
                        NETWORK_NOT_AVAILABLE_ZH_CN, 
                        NETWORK_NOT_AVAILABLE_ZH_TW);
                LogUtil.i(TAG, "String: " + networkNotAvailable);
                UIUtils.showToast(mContext, networkNotAvailable, Toast.LENGTH_SHORT);
            }**/
        }
    }
    
}
