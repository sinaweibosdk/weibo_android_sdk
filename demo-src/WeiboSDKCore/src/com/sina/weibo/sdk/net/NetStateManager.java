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

import org.apache.http.HttpHost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * 网络状态管理类。
 * TODO：（To be design...）
 * 
 * @author SINA
 * @since 2013-11-05
 */
public class NetStateManager {
    private static Context mContext;

    public static NetState CUR_NETSTATE = NetState.Mobile;

    public enum NetState {
        Mobile, WIFI, NOWAY
    }

    public class NetStateReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifiManager.getConnectionInfo();
                if (!wifiManager.isWifiEnabled() || -1 == info.getNetworkId()) {
                    CUR_NETSTATE = NetState.Mobile;
                }
            }
        }
    }

    /**
     * 获取当前 APN 并返回 {@link HttpHost} 对象
     * 
     * @return {@link HttpHost} 对象
     */
    public static HttpHost getAPN() {
        HttpHost proxy = null;
        Uri uri = Uri.parse("content://telephony/carriers/preferapn");
        Cursor mCursor = null;
        if (null != mContext) {
            mCursor = mContext.getContentResolver().query(uri, null, null, null, null);
        }
        if (mCursor != null && mCursor.moveToFirst()) {
            // 游标移至第一条记录，当然也只有一条
            String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
            if (proxyStr != null && proxyStr.trim().length() > 0) {
                proxy = new HttpHost(proxyStr, 80);
            }
            mCursor.close();
        }
        return proxy;
    }
}
