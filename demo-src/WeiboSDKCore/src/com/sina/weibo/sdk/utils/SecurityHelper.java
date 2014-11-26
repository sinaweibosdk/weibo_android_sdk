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

package com.sina.weibo.sdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import com.sina.weibo.sdk.ApiUtils;
import com.sina.weibo.sdk.WeiboAppManager.WeiboInfo;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * 该类用于提供一些关于安全方面的函数，如检测否存在有合法的微博客户端等。
 * 
 * @author SINA
 * @since 2013-10-09
 */
public class SecurityHelper {
    
    /**
     * 提取 {@link Intent} 中 {@link Activity} 对应包名的签名（可能是一组），
     * 并检测是否包含现有微博的签名，从而判读是否存在有合法的微博客户端。
     * 
     * @param context 应用程序上下文环境
     * @param intent  Intent 实例
     * 
     * @return 如果存在合法的微博客户端，返回 true；其它情况返回 false。
     */
    public static boolean validateAppSignatureForIntent(Context context, Intent intent) {
        PackageManager pkgMgr = context.getPackageManager();
        if (null == pkgMgr) {
            return false;
        }
        
        ResolveInfo resolveInfo = pkgMgr.resolveActivity(intent, 0);
        if (resolveInfo == null) {
            return false;
        }

        String packageName = resolveInfo.activityInfo.packageName;
        try {
            PackageInfo packageInfo = pkgMgr.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            return containSign(packageInfo.signatures, WBConstants.WEIBO_SIGN);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // XXX：同上，冒名顶替的应用返回，也能通过该验证。是否会有安全隐患？？
    /**
     * 判断是否从微博客户端返回的数据。该函数在 SSO 认证时被调用。
     * 
     * @param intent Intent 实例，包含被调用者返回的数据
     * 
     * @return 如果该 Intent 对应的客户端是合法的微博客户端，则返回 true，否则返回 false。
     */
    public static boolean checkResponseAppLegal(Context context, WeiboInfo requestWeiboInfo, Intent intent) {
        
        WeiboInfo winfo = requestWeiboInfo;
        if (winfo != null && winfo.getSupportApi() <= ApiUtils.BUILD_INT_VER_2_3) {
            return true;
        } else if (winfo == null) {
            return true;
        }

        // 微博的包名验证是不是微博返回的
        String appPackage = (intent != null) ? intent.getStringExtra(WBConstants.Base.APP_PKG) : null;
        
        if (null == appPackage 
                || null == intent.getStringExtra(WBConstants.TRAN) 
                || !ApiUtils.validateWeiboSign(context, appPackage)) {
            return false;
        }

        return true;
    }
    
    /**
     * 查看签名列表中是否有指定的签名。
     * 
     * @param signatures 签名列表
     * @param destSign   需要比较的目标签名
     * 
     * @return 如果签名列表中是包含指定的签名
     */
    public static boolean containSign(Signature[] signatures, String destSign) {
        if (null == signatures || null == destSign) {
            return false;
        }
        
        for (Signature signature : signatures) {
            String s = MD5.hexdigest(signature.toByteArray());
            if (destSign.equals(s)) {
                return true;
            }
        }

        return false;
    }
}
