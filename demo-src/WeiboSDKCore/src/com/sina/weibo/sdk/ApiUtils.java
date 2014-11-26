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

package com.sina.weibo.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.utils.MD5;

/**
 * 该类提供了一些公用的方法，用于获取微博信息等。
 * 
 * @author SINA
 * @since 2013-10-24
 */
public class ApiUtils {

    private static final String TAG = ApiUtils.class.getName();

    /** v2.1 版 SDK，支持的微博版本 */
    public static final int BUILD_INT         = 10350;
    /** v2.2 版 SDK，支持的微博版本 */
    public static final int BUILD_INT_VER_2_2 = 10351;
    /** v2.3 版 SDK，支持的微博版本 */
    public static final int BUILD_INT_VER_2_3 = 10352;
    /** v2.5 版 SDK，支持的微博版本 */
    public static final int BUILD_INT_VER_2_5 = 10353;
    
    /** 微博440版本，对应的SDK的版本号 **/
    public static final int BUILD_INT_440 = 10355;
    
    /**
     * 验证微博客户端签名是否正确。
     * 
     * @param context 应用程序上下文环境
     * @param pkgName 指定应用的包名
     * 
     * @return 如果微博客户端签名是正确，返回 true；否则，返回 false。
     */
    public static boolean validateWeiboSign(Context context, String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    pkgName, PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            return false;
        }
        
        return containSign(packageInfo.signatures, WBConstants.WEIBO_SIGN);
    }

    /**
     * 查看签名列表中是否有指定的签名。
     * 
     * @param signatures 签名列表
     * @param destSign   需要比较的目标签名
     * 
     * @return 如果签名列表中是包含指定的签名
     */
    private static boolean containSign(Signature[] signatures, String destSign) {
        if (null == signatures || null == destSign) {
            return false;
        }
        for (Signature signature : signatures) {
            String s = MD5.hexdigest(signature.toByteArray());
            if (destSign.equals(s)) {
                LogUtil.d(TAG, "check pass");
                return true;
            }
        }
        return false;
    }

    
}
