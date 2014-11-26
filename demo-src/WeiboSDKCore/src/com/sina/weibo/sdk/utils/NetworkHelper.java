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

import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * 该类提供了一些通用的网络处理共通方法，如当前网络是否可用等。
 * 
 * @author SINA
 * @since 2013-10-16
 */
public class NetworkHelper {
    
    /**
     * 检查当前应用是否有网络权限。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 如果当前应用是否有网络权限，返回 true；
     *         注意：如果 context 为 null 时，默认也返回 true；
     */
    public static boolean hasInternetPermission(Context context) {
        if (context != null) {
            return PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission(Manifest.permission.INTERNET);
        }
        
        // 默认返回 true
        return true;
    }
    
    /**
     * 当前网络（WiFi/2-3G 等）是否可用。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 如果有可用网络，返回 true；否则，返回 false。
     */
    public static boolean isNetworkAvailable(Context context) {
        if (null != context) {
            NetworkInfo info = getActiveNetworkInfo(context);
            return ((null != info) && info.isConnected());
        }

        return false;
    }
    
    /**
     * 当前 WiFi 网络是否可用。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 如果 WiFi 可用，返回 true；否则，返回 false。
     */
    public static boolean isWifiValid(Context context) {
        if (null != context) {
            NetworkInfo info = getActiveNetworkInfo(context);

            return (info != null) 
                    && (ConnectivityManager.TYPE_WIFI == info.getType()) 
                    && info.isConnected();
        }

        return false;
    }
	
    /**
     * 当前 Mobile 网络是否可用。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 如果 Mobile 网络可用，返回 true；否则，返回 false。
     */    
    public static boolean isMobileNetwork(Context context) {
        if (null != context) {
            NetworkInfo info = getActiveNetworkInfo(context);

            if (null == info) {
                return false;
            }

            return (info != null) 
                    && (ConnectivityManager.TYPE_MOBILE == info.getType()) 
                    && info.isConnected();
        }

        return false;
    }

    /**
     * 获取当前活动的可用网络信息。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 当前活动的可用网络信息实例
     */
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivity = 
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivity.getActiveNetworkInfo();
    }
    
    /**
     * 获取指定网络类型的网络状态信息。
     * 
     * @param context     应用程序上下文环境
     * @param networkType 指定的网络类型，可以是以下中的一种：
     *                    {@link ConnectivityManager#TYPE_NONE}，（值为-1）
     *                    {@link ConnectivityManager#TYPE_MOBILE}，
     *                    {@link ConnectivityManager#TYPE_WIFI}，
     *                    {@link ConnectivityManager#TYPE_MOBILE_MMS}，
     *                    {@link ConnectivityManager#TYPE_MOBILE_SUPL}，
     *                    {@link ConnectivityManager#TYPE_MOBILE_DUN}，
     *                    {@link ConnectivityManager#TYPE_MOBILE_HIPRI}，
     *                    {@link ConnectivityManager#TYPE_WIMAX}，
     *                    {@link ConnectivityManager#TYPE_BLUETOOTH}，
     *                    {@link ConnectivityManager#TYPE_DUMMY}，
     *                    {@link ConnectivityManager#TYPE_ETHERNET}
     *                    
     * @return 当前网络状态信息实例
     */
    public static NetworkInfo getNetworkInfo(Context context, int networkType) {
        ConnectivityManager connectivityManager = 
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(networkType);
    }

    /**
     * 获取当前可用网络类型。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 当前可用网络类型，可以是以下中的一种：
     *                    {@link ConnectivityManager#TYPE_NONE}，（值为-1）
     *                    {@link ConnectivityManager#TYPE_MOBILE}，
     *                    {@link ConnectivityManager#TYPE_WIFI}，
     *                    {@link ConnectivityManager#TYPE_MOBILE_MMS}，
     *                    {@link ConnectivityManager#TYPE_MOBILE_SUPL}，
     *                    {@link ConnectivityManager#TYPE_MOBILE_DUN}，
     *                    {@link ConnectivityManager#TYPE_MOBILE_HIPRI}，
     *                    {@link ConnectivityManager#TYPE_WIMAX}，
     *                    {@link ConnectivityManager#TYPE_BLUETOOTH}，
     *                    {@link ConnectivityManager#TYPE_DUMMY}，
     *                    {@link ConnectivityManager#TYPE_ETHERNET}
     */
    public static int getNetworkType(Context context) {
        if (null != context) {
            NetworkInfo info = getActiveNetworkInfo(context);

            return (null == info) ? -1 : info.getType();
        }
        
        return -1;
    }

    /**
     * 获取 Wifi 可用、不可用等状态。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return Wifi 的状态。可以是以下状态中的一种：
     *         {@link WifiManager#WIFI_STATE_DISABLING}，
     *         {@link WifiManager#WIFI_STATE_DISABLED}，
     *         {@link WifiManager#WIFI_STATE_ENABLING}，
     *         {@link WifiManager#WIFI_STATE_ENABLED}，
     *         {@link WifiManager#WIFI_STATE_UNKNOWN}，
     */
    public static int getWifiState(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (null == wifi) {
            return WifiManager.WIFI_STATE_UNKNOWN;
        }

        return wifi.getWifiState();
    }

    /**
     * 获取 Wifi 连接状态。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return Wifi 连接状态，@see {@link DetailedState}
     */
    public static DetailedState getWifiConnectivityState(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context, ConnectivityManager.TYPE_WIFI);
        return (null == networkInfo) ? DetailedState.FAILED : networkInfo.getDetailedState();
    }
    
    /**
     * 使用指定的 SSID 和密码连接 WiFi。
     * XXX: TBT (To be test)
     * 
     * @param context     应用程序上下文环境
     * @param wifiSSID Wifi SSID
     * @param password Wifi 密码
     * 
     * @return 连接成功，返回 true；失败，返回 false。
     */
    public static boolean wifiConnection(Context context, String wifiSSID, String password) {
        boolean isConnection = false;
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String strQuotationSSID = "\"" + wifiSSID + "\"";
    
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        if (null != wifiInfo
                && (wifiSSID.equals(wifiInfo.getSSID()) || strQuotationSSID.equals(wifiInfo.getSSID()))) {
            isConnection = true;
        } else {
            List<ScanResult> scanResults = wifi.getScanResults();
            if (null != scanResults && 0 != scanResults.size()) {
                for (int nAllIndex = scanResults.size() - 1; nAllIndex >= 0; nAllIndex--) {
                    String strScanSSID = scanResults.get(nAllIndex).SSID;
                    if (wifiSSID.equals(strScanSSID) || strQuotationSSID.equals(strScanSSID)) {
                        WifiConfiguration config = new WifiConfiguration();
                        config.SSID = strQuotationSSID;
                        config.preSharedKey = "\"" + password + "\"";
                        config.status = WifiConfiguration.Status.ENABLED;
    
                        int nAddWifiId = wifi.addNetwork(config);
                        isConnection = wifi.enableNetwork(nAddWifiId, false);
                        break;
                    }
                }
            }
        }
    
        return isConnection;
    }

    /**
     * 清除指定 URL 的 Cookie。
     * @param context 应用程序上下文环境 
     */
    public static void clearCookies(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }
    
    /**
     * 生成 UA,目前暂无明确规则,返回含Android的字符串即可(注意大小写)
     * 
     * @param ctx
     * @return
     */
    public static String generateUA(Context ctx) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("Android");
        buffer.append("__");
        buffer.append("weibo");
        buffer.append("__");
        buffer.append("sdk");
        buffer.append("__");
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = null;
            pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_INSTRUMENTATION);
            String versionCode = pi.versionName;
            buffer.append(versionCode.replaceAll("\\s+", "_"));
        } catch (final Exception localE) {
            buffer.append("unknown");
        }
        return buffer.toString();
    }
}
