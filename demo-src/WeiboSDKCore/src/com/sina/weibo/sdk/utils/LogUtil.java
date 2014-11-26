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

import android.util.Log;

/**
 * Log工具类，支持文件名、行号、类名的输出。
 * 
 * @author SINA
 * @since 2013-09-27
 */
public class LogUtil {
    
    /** 标志Log输出是否启用，默认启用 */
    public static boolean sIsLogEnable = false;

    /**
     * 启用Log。
     */
    public static void enableLog() {
        sIsLogEnable = true;
    }
    
    /**
     * 禁用Log。
     */
    public static void disableLog() {
        sIsLogEnable = false;
    }

    /**
     * 输出 {@link Log#DEBUG} 信息的Log。
     * 
     * @param tag 用于标识日志消息的来源。
     * @param msg 你想输出的日志信息。
     */
    public static void d(String tag, String msg) {
        if (sIsLogEnable) {
            // Call the method of Log class directory.
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + 
                              stackTrace.getLineNumber() + ") " +
                              stackTrace.getMethodName();
            Log.d(tag, fileInfo + ": " + msg);
        }
    }

    /**
     * 输出 {@link Log#INFO} 信息的Log。
     * 
     * @param tag 用于标识日志消息的来源。
     * @param msg 你想输出的日志信息。
     */
    public static void i(String tag, String msg) {
        if (sIsLogEnable) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + 
                              stackTrace.getLineNumber() + ") " +
                              stackTrace.getMethodName();
            Log.i(tag, fileInfo + ": " + msg);
        }
    }
    
    /**
     * 输出 {@link Log#ERROR} 信息的Log。
     * 
     * @param tag 用于标识日志消息的来源。
     * @param msg 你想输出的日志信息。
     */
    public static void e(String tag, String msg) {
        if (sIsLogEnable) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + 
                              stackTrace.getLineNumber() + ") " +
                              stackTrace.getMethodName();
            Log.e(tag, fileInfo + ": " + msg);
        }
    }

    /**
     * 输出 {@link Log#WARN} 信息的Log。
     * 
     * @param tag 用于标识日志消息的来源。
     * @param msg 你想输出的日志信息。
     */
    public static void w(String tag, String msg) {
        if (sIsLogEnable) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + 
                              stackTrace.getLineNumber() + ") " +
                              stackTrace.getMethodName();
            Log.w(tag, fileInfo + ": " + msg);
        }
    }

    /**
     * 输出 {@link Log#VERBOSE} 信息的Log。
     * 
     * @param tag 用于标识日志消息的来源。
     * @param msg 你想输出的日志信息。
     */
    public static void v(String tag, String msg) {
        if (sIsLogEnable) {
            StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];
            String fileInfo = stackTrace.getFileName() + "(" + 
                              stackTrace.getLineNumber() + ") " +
                              stackTrace.getMethodName();
            Log.v(tag, fileInfo + ": " + msg);
        }
    }

    /**
     * 获取日志对应的文件名、行号、类名。
     * 
     * @return 返回文件名、行号、类名合并后的字符串。
     */
    public static String getStackTraceMsg() {
        StackTraceElement stackTrace = java.lang.Thread.currentThread().getStackTrace()[3];        
        String fileInfo = stackTrace.getFileName() + "(" + 
                stackTrace.getLineNumber() + ") " +
                stackTrace.getMethodName();        
        return fileInfo;
    }
}
