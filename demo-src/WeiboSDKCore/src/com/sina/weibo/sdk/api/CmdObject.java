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

package com.sina.weibo.sdk.api;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 命令数据对象。
 * 
 * @author SINA
 * @since 2013-10-22
 */
public class CmdObject extends BaseMediaObject {

    /** 文本不得超过 1KB */
    public String cmd;
    public final static String CMD_HOME = "home";

    public static final Parcelable.Creator<CmdObject> CREATOR = new Parcelable.Creator<CmdObject>() {
        public CmdObject createFromParcel(Parcel in) {
            return new CmdObject(in);
        }
    
        public CmdObject[] newArray(int size) {
            return new CmdObject[size];
        }
    };

    public CmdObject() {
    }

    public CmdObject(Parcel in) {
        cmd = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cmd);
    }

    public boolean checkArgs() {
        if (cmd == null || cmd.length() == 0 || cmd.length() > 1024) {
            return false;
        }
        return true;
    }

    @Override
    public int getObjType() {
        return MEDIA_TYPE_CMD;
    }

    @Override
    protected BaseMediaObject toExtraMediaObject(String str) {
        return this;
    }

    @Override
    protected String toExtraMediaString() {
        return "";
    }
}
