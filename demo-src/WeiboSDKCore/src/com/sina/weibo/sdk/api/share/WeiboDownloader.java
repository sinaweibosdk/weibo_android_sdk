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

package com.sina.weibo.sdk.api.share;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 该类用于下载微博客户端。（目前只支持将下载页面唤起）
 * 
 * @author SINA
 * @since 2013-10-29
 */
public class WeiboDownloader {

    private static final String TITLE_CHINESS = "提示";
    private static final String PROMPT_CHINESS = "未安装微博客户端，是否现在去下载？";
    private static final String OK_CHINESS = "现在下载";
    private static final String CANCEL_CHINESS = "以后再说";

    private static final String TITLE_ENGLISH = "Notice";
    private static final String PROMPT_ENGLISH = "Sina Weibo client is not installed, download now?";
    private static final String OK_ENGLISH = "Download Now";
    private static final String CANCEL_ENGLISH = "Download Later";

    /**
     * 创建微博下载确认对话框。
     * 
     * @param context  创建该对话框的 Activity
     * @param listener 回调函数
     * 
     * @return 返回微博下载确认对话框
     */
    public static Dialog createDownloadConfirmDialog(final Context context, final IWeiboDownloadListener listener) {
        String title = TITLE_CHINESS;
        String prompt = PROMPT_CHINESS;
        String ok = OK_CHINESS;
        String cancel = CANCEL_CHINESS;

        if (!Utility.isChineseLocale(context.getApplicationContext())) {
            title = TITLE_ENGLISH;
            prompt = PROMPT_ENGLISH;
            ok = OK_ENGLISH;
            cancel = CANCEL_ENGLISH;
        }

        Dialog dialog = new AlertDialog.Builder(context)
        .setMessage(prompt)
        .setTitle(title)
        .setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadWeibo(context);
            }})
        .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onCancel();
                }
            }})
        .create();
        
        return dialog;
    }
    
    /**
     * 唤起下载页面。
     * 
     * @param context 下载页面的 Activity
     */
    private static void downloadWeibo(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri url = Uri.parse(WBConstants.WEIBO_DOWNLOAD_URL);
        intent.setData(url);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
