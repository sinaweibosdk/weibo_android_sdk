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

package com.sina.weibo.sdk.codestyle;

/**
 * 类的大体描述放在这里。
 *         
 * @author 作者名
 * @since 2013-XX-XX
 */
@SuppressWarnings("unused")
public class SampleCode {

    /** 公有的常量注释 */
    public static final String ACTION_MAIN = "android.intent.action.MAIN";
    
    /** 私有的常量注释（同类型的常量可以分块并紧凑定义） */
    private static final int MSG_AUTH_NONE    = 0;
    private static final int MSG_AUTH_SUCCESS = 1;
    private static final int MSG_AUTH_FAILED  = 2;
    
    /** 保护的成员变量注释 */
    protected Object mObject0;
    
    /** 私有的成员变量 mObject1 注释（同类型的成员变量可以分块并紧凑定义） */
    private Object mObject1;
    /** 私有的成员变量 mObject2 注释 */
    private Object mObject2;
    /** 私有的成员变量 mObject3 注释 */
    private Object mObject3;
    
    /**
     * 对于注释多于一行的，采用这种方式来
     * 定义该变量
     */
    private Object mObject4;

    /**
     * 公有方法描述...
     * 
     * @param param1  参数1描述...
     * @param param2  参数2描述...
     * @param paramXX 参数XX描述... （注意：请将参数、描述都对齐）
     */
    public void doSomething(int param1, float param2, String paramXX) {
        // ...implementation
    }
    
    /**
     * 保护方法描述...
     */
    protected void doSomething() {
        // ...implementation        
    }
    
    /**
     * 私有方法描述...
     * 
     * @param param1  参数1描述...
     * @param param2  参数2描述...
     */
    private void doSomethingInternal(int param1, float param2) {
        // ...implementation        
    }
}
