package com.sina.weibo.sdk.api.share;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboAuthException;
import com.sina.weibo.sdk.exception.WeiboDialogException;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 微博分享微博回调接口。
 * 
 * @author SINA
 * @since 2014-05-29
 */
public interface IWeiboShareListener {

    /**
     * 授权认证结束后将调用此方法。
     * 
     * @param values 保存从服务器返回的数据
     *               授权成功时，包含"access_token"、"expires_in"、"refresh_token"等信息；
     *               
     * @see http://t.cn/aHDHkl
     */
    public void onAuthorizeComplete(Oauth2AccessToken accessToken);
    
    /**
     * 当认证过程中捕获到 {@link WeiboException} 时调用。
     * 如：
     * <li>Web 授权时，加载 URL 异常，此时抛出 {@link WeiboDialogException}
     * <li>Web 授权时，服务器返回的数据不正确，此时抛出 {@link WeiboAuthException}
     * 
     * @param e WeiboException 微博认证错误异常
     */
    public void onAuthorizeException(WeiboException e);
    
    /**
     * Oauth2.0 认证过程中，如果认证窗口被关闭或认证取消时调用。
     */
    public void onAuthorizeCancel();
    
    /**
     * 如果当前使用的Token错误或者过期时调用
     * 
     * @param errorToken
     */
    public void onTokenError(String errorToken);

}
