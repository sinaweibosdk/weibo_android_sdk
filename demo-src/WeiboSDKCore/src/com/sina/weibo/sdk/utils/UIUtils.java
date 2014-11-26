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

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 该类提供了一些 UI 相关的工具函数。
 * 
 * @author SINA
 * @since 2013-10-16
 */
public class UIUtils {

    /**
     * 显示警告对话框。
     * 
     * @param context 创建该对话框的上下文环境
     * @param title   要显示的标题
     * @param text    要显示的内容
     */
    public static void showAlert(Context context, String title, String text) {
        if (context != null) {
            new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(text)
            .create()
            .show();
        }
    }
    
    /**
     * 显示警告对话框。
     * 
     * @param context 创建该对话框的上下文环境
     * @param titleId 要显示的标题的资源 ID
     * @param textId  要显示的内容的资源 ID
     */
    public static void showAlert(Context context, int titleId, int textId) {
        if (context != null) {
            showAlert(context, context.getString(titleId), context.getString(textId));
        }
    }

    /**
     * 弹出 Toast 界面。
     * 
     * @param context  创建该 Toast 界面的上下文环境
     * @param resId    要显示的文本内容的资源 ID
     * @param duration 要显示的时间，可以是 {@link #LENGTH_SHORT} 或 {@link #LENGTH_LONG}
     */
    public static void showToast(Context context, int resId, int duration) {
        if (context != null) {
            Toast.makeText(context, resId, duration).show();
        }
    }

    /**
     * 弹出 Toast 界面。
     * 
     * @param context  创建该 Toast 界面的上下文环境
     * @param text     要显示的文本内容
     * @param duration 要显示的时间，可以是 {@link #LENGTH_SHORT} 或 {@link #LENGTH_LONG}
     */
    public static void showToast(Context context, CharSequence text, int duration) {
        if (context != null) {
            Toast.makeText(context, text, duration).show();
        }
    }

    /**
     * 在屏幕中间弹出 Toast 界面。
     * 
     * @param context  创建该 Toast 界面的上下文环境
     * @param resId    要显示的文本内容的资源 ID
     * @param duration 要显示的时间，可以是 {@link #LENGTH_SHORT} 或 {@link #LENGTH_LONG}
     */
    public static void showToastInCenter(Context context, int resId, int duration) {
        if (context != null) {
            Toast toast = Toast.makeText(context, resId, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
