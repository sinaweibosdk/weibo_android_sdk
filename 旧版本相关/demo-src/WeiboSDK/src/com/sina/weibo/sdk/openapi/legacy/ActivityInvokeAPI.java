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

package com.sina.weibo.sdk.openapi.legacy;

import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
/**
 * 此类封装了打开微博的各界面接口
 * 
 * @author SINA
 * @date 2014-03-03
 */
public class ActivityInvokeAPI {
    /**
     * 调起新浪微博客户端的发送微博界面，完成发送微博工作。
     * 
     * @param activity
     * @param content   微博内容
     */
    public static void openSendWeibo(Activity activity,String content){
        if(activity==null||null==content){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://sendweibo?content="+URLEncoder.encode(content)));
        activity.startActivity(intent);
    }
/**
 * 调起新浪微博客户端的发送微博界面，完成发送微博工作。
 * 
 * @param activity 
 * @param content   微博内容
 * @param xid       签到时 的地点id
 * @param poiId     POI点ID
 * @param poiName   POI点名称
 * @param longitude 经度
 * @param latitude  纬度
 */
    public static void openSendWeibo(Activity activity,String content,String xid,String poiId,String poiName,String longitude,String latitude){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://sendweibo?content="+content+"&xid="+xid+"&poiid="+poiId+"&poiname="+poiName+"&longitude="+longitude+"&latitude="+latitude));
        activity.startActivity(intent);
    }
    /**
     * 调用当前用户的周边的人的界面。
     * 
     * @param activity
     */
    public static void openNearbyPeople(Activity activity){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://nearbypeople"));
        activity.startActivity(intent);
    }
    /**
     * 调用当前用户的周边的微博的界面。
     * 
     * @param activity
     */
    public static void openNearbyWeibo(Activity activity){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://nearbyweibo"));
        activity.startActivity(intent);
    }
    /**
     *通过昵称 打开个人资料页面。
     *
     * @param activity
     * @param nickName  昵称
     */
    public static void openUserInfoByNickName(Activity activity,String nickName){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
//        intent.setData(Uri.parse("sinaweibo://userinfo?nick="+URLEncoder.encode(nickName)));
        intent.setData(Uri.parse("sinaweibo://userinfo?nick="+nickName));
        activity.startActivity(intent);
    }
    /**
     * 通过uid打开个人资料界面。
     * 
     * @param activity
     * @param uid   用户ID
     */
    public static void openUserInfoByUid(Activity activity,String uid){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://userinfo?uid="+uid));
        activity.startActivity(intent);
    }
    /**
     * 打开微博客户端内置浏览器。
     * 
     * @param activity
     * @param url       要打开的网页地址
     */
    public static void openWeiboBrowser(Activity activity,String url){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://browser?url="+url));
        activity.startActivity(intent);
    }
    
    /**
     * 打开微博客户端。
     * 
     * @param activity
     */
    public static void openWeibo(Activity activity){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://splash"));

        activity.startActivity(intent);
    }
    /**
     * 打开摇一摇界面。
     * 
     * @param activity
     */
    public static void openShake(Activity activity){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://shake"));

        activity.startActivity(intent);
    }
    /**
     * 打开通讯录界面。
     * 
     * @param activity
     */
    public static void openContact(Activity activity){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://contact"));

        activity.startActivity(intent);
        
    }
    /**
     * 打开用户话题列表界面。
     * 
     * @param activity
     * @param uid 用户uid
     */
    public static void openUserTrends(Activity activity,String uid){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://usertrends?uid="+uid));
        activity.startActivity(intent);
    }
    /**
     * 打开私信对话界面。
     * 
     * @param activity
     * @param uid 用户uid
     */
    public static void openMessageListByUid(Activity activity,String uid){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://messagelist?uid="+uid));
        activity.startActivity(intent);
    }
    /**
     * 打开私信对话界面。
     * 
     * @param activity
     * @param uid 用户uid
     */
    public static void openMessageListByNickName(Activity activity,String nickName){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://messagelist?nick="+(nickName)));
        activity.startActivity(intent);
    }
    /**
     * 打开某条微博正文。
     * 
     * @param activity
     * @param blogId 某条微博id
     */
    public static void openDetail(Activity activity,String blogId){
        if(activity==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("sinaweibo://detail?mblogid="+blogId));
        activity.startActivity(intent);
    }
}
