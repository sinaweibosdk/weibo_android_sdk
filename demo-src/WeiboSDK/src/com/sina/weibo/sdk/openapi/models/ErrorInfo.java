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

package com.sina.weibo.sdk.openapi.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 错误信息结构体。
 * 
 * @author SINA
 * @since 2013-11-24
 */
public class ErrorInfo {
    public String error;
    public String error_code;
    public String request;

    public static ErrorInfo parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        ErrorInfo errorInfo = new ErrorInfo();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            errorInfo.error      = jsonObject.optString("error");
            errorInfo.error_code = jsonObject.optString("error_code");
            errorInfo.request    = jsonObject.optString("request");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorInfo;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "error: " + error + 
               ", error_code: " + error_code + 
               ", request: " + request;
    }
}
