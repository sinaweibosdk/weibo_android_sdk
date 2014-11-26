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

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 该类提供了好友邀请接口，支持登录用户向自己的微博互粉好友发送私信邀请、礼物。
 * 详情请参考： 
 * <li><a href="http://open.weibo.com/wiki/2/messages/invite">好友邀请接口</a>
 * <li><a href=http://t.cn/8F75vDo>申请接入流程</a>
 * 
 * @author SINA
 * @since 2013-11-04
 */
public class InviteAPI extends AbsOpenAPI {
    private final static String TAG = InviteAPI.class.getName();
    
    /** 邀请地址 */
    private final String INVITE_URL = "https://m.api.weibo.com/2/messages/invite.json";
    
    /**（必须）要回复的私信文本内容。文本大小必须小于300个汉字 */
    public final static String KEY_TEXT = "text";
    /**（可选）邀请点击后跳转链接。默认为当前应用地址 */
    public final static String KEY_URL  = "url";
    /**（可选）邀请 Card 展示时的图标地址，大小必须为 80px X 80px，仅支持 PNG、JPG 格式。默认为当前应用 Logo 地址 */
    public final static String KEY_INVITE_LOGO = "invite_logo";

    /**
     * 构造函数。
     * 
     * @param oauth2AccessToken Token 实例
     */
    public InviteAPI(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }
    
    /**
     * 向好友发送邀请。支持登录用户向自己的微博互粉好友发送私信邀请、礼物。
     * 
     * @param uid      被邀请人的 Uid，需要为当前用户互粉好友
     * @param jsonData 邀请数据。以 {@link JSONObject} 数据填充
     * @param listener 邀请接口对应的回调
     */
    public void sendInvite(String uid, JSONObject jsonData, RequestListener listener) {
        if (!TextUtils.isEmpty(uid) 
                && jsonData != null 
                && !TextUtils.isEmpty(jsonData.toString())) {
            
            WeiboParameters params = new WeiboParameters(mAppKey);
            params.put("uid", uid);
            params.put("data", jsonData.toString());
            requestAsync(INVITE_URL, params, HTTPMETHOD_POST, listener);
        } else {
            LogUtil.d(TAG, "Invite args error!");
        }
    }
}
