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

package com.sina.weibo.sdk.demo;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.component.GameManager;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.WeiboParameters;

/**
 * 该类主要演示game类  部分接口的调用
 * 
 * @author SINA
 * @since 2013-09-29
 */
public class WBGameActivity extends Activity {
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // 游戏id     2025358007:23e260e9
        
          final  TextView   result_msg  = (TextView)findViewById(R.id.result_msg_info);
        

          
          /***
           *    游戏成就对象入库  接口  1
           * @param context
           * @param access_token         第三方的 登录token
           * @param source               第三方的 appKey
           * @param achievement_id       游戏成就id
           * @param game_id              游戏id
           * @param title                成就标题名
           * @param imageUrl  string     图像url信息，图片大小200*200像素，支持PNG、JPEG、JPG，  需要  urlencode处理
           * @param description          游戏成就描述信息
           * @param game_point   string  游戏取得的 点数
           * @param AchievementTypeUrl   成就类型url地址信息， 需要  urlencode处理
           * @param create_time  string  初次入库的时间    add添加不需要传入,   update需要传入(如果有)          yyyy-MM-dd HH:mm:ss
           * @return
           */   
          
        findViewById(R.id.achievement_add).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                  // 测试数据参考如下：
                String token  =   AccessTokenKeeper.readAccessToken(WBGameActivity.this).getToken();
                WeiboParameters params  = new WeiboParameters("");
                if(!TextUtils.isEmpty(token)){
                    params.put(WBConstants.AUTH_ACCESS_TOKEN, token);
                }
                params.put(WBConstants.GAME_PARAMS_SOURCE, Constants.APP_KEY);
                //组成规则  domainid:achievement_id    com.sina.weibo.demo  的测试domainId 2025358007
                if(!TextUtils.isEmpty("2025358007:23e260e9001")){
                    params.put(WBConstants.GAME_PARAMS_ACHIEVEMENT_ID, "2025358007:23e260e9001");
                }
                //组成规则  domainid:gameid
                if(!TextUtils.isEmpty("2025358007:23e260e9")){
                    params.put(WBConstants.GAME_PARAMS_GAME_ID, "2025358007:23e260e9");
                }
                if(!TextUtils.isEmpty("title")){
                    params.put(WBConstants.GAME_PARAMS_GAME_ACHIEVEMENT_TITLE, "title");
                }
                if(!TextUtils.isEmpty("description_test")){
                    params.put(WBConstants.GAME_PARAMS_DESCRIPTION, "description_test");
                }
                if(!TextUtils.isEmpty("10")){
                    params.put(WBConstants.GAME_PARAMS_GAME_POINT, "10");
                }
                //添加不需要传入  更新如果有的话需要传入
                SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();       
                params.put(WBConstants.GAME_PARAMS_GAME_CREATE_TIME, myFmt.format(date));
                
                //  注意key的值  iamge-->iamgeURL     url--> AchievementTypeUrl
                try {
                   if (!TextUtils.isEmpty("image_test")) {
                       params.put(WBConstants.GAME_PARAMS_GAME_IMAGE_URL, URLEncoder.encode("image_test", GameManager.DEFAULT_CHARSET));
                   }
                   //AchievementTypeUrl
                   if (!TextUtils.isEmpty("http://url.cn")) {
                       params.put(WBConstants.GAME_PARAMS_GAME_ACHIEVEMENT_TYPE_URL, URLEncoder.encode("http://url.cn", GameManager.DEFAULT_CHARSET));
                   }
                      String result =   GameManager.AddOrUpdateGameAchievement(WBGameActivity.this,params);
                      // 成功结果:{"result":true}
                      if(result != null){
                          result_msg.setText(result);
                      }  
                  } catch (Exception e) {
                      System.out.println("异常信息"+e.getMessage());  
                  }
            }
        });
        
        
        /***  接口2 
         *  用户获得游戏成就关系 入库
         * @param context
         * @param access_token         第三方的 登录token
         * @param source               第三方的 appKey
         * @param achievement_id       戏成就id
         * @param uid                   用户id
         * @param create_time  string  初次入库的时间    add  添加不需要传入,   update需要传入(如果有)          yyyy-MM-dd HH:mm:ss
         * @return
         */
        findViewById(R.id.game_relation_add).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                  // 测试数据参考如下：
                 String token  =   AccessTokenKeeper.readAccessToken(WBGameActivity.this).getToken();
                 
                 WeiboParameters params  = new WeiboParameters("");

                 if(!TextUtils.isEmpty(token)){
                     params.put(WBConstants.AUTH_ACCESS_TOKEN, token);
                 }
                 
                 if(!TextUtils.isEmpty(Constants.APP_KEY)){
                     params.put(WBConstants.GAME_PARAMS_SOURCE, Constants.APP_KEY);
                 }
                 if(!TextUtils.isEmpty("23e260e9001")){
                     params.put(WBConstants.GAME_PARAMS_ACHIEVEMENT_ID, "23e260e9001");
                 }
                 if(!TextUtils.isEmpty("3164868113")){
                     params.put(WBConstants.GAME_PARAMS_UID, "3164868113");
                 }
                 
                 //添加不需要传入  更新如果有的话需要传入
                 SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 Date date = new Date();       
                 params.put(WBConstants.GAME_PARAMS_GAME_CREATE_TIME, myFmt.format(date));
                 
                try {
                    String result =   GameManager.addOrUpdateGameAchievementRelation(WBGameActivity.this,params);
                    // 成功结果:{"result":true}
                    if(result != null){
                        result_msg.setText(result);
                    }  
                } catch (Exception e) {
                    System.out.println("异常信息"+e.getMessage());  
                }
                 
            }
        });
        
        
        /**     
                必选  类型  说明
           source  true     string  申请应用时分配的AppKey，
           game_id  true    string  游戏id
           user_id  true    string  用户id
           score    true    string  用户得分
        */
        //  用户游戏得分关系入库/更新  接口3
        findViewById(R.id.game_score_add).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String token  =   AccessTokenKeeper.readAccessToken(WBGameActivity.this).getToken();
                try {
                    String result =  GameManager.addOrUpdateAchievementScore(WBGameActivity.this, token,  Constants.APP_KEY, "23e260e9", "3164868113", "5");
                    // 成功结果:{"result":true}
                    if(result != null){
                        result_msg.setText(result);
                    }  
                } catch (Exception e) {
                    System.out.println("异常信息"+e.getMessage());  
                }
               
            }
        });
         
        
        /***
         * @param context
         * @param access_token
         * @param appKey  参数说明同上
         * @param game_id  游戏id
         * @param user_id  玩家的 微博uid
         * @return
         */
        //读取玩家游戏分数  接口4
        findViewById(R.id.read_player_score_info).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 测试数据参考如下：
                String token  =   AccessTokenKeeper.readAccessToken(WBGameActivity.this).getToken();
                try {
                    String result =   GameManager.readPlayerScoreInfo(WBGameActivity.this, token, Constants.APP_KEY, "23e260e9", "3164868113");
                    // 结果示例: {"score":5}
                    if(result != null){
                        result_msg.setText(result);
                    }  
                } catch (Exception e) {
                    System.out.println("异常信息"+e.getMessage());  
                }
            }
        });

        /**  读取玩家互粉好友  游戏分数
            source  true    string  申请应用时分配的AppKey，
            game_id  true    string  游戏id
            uid  true    string  用户id
         */
        //读取玩家  好友的游戏分数  接口5
        findViewById(R.id.read_player_friend_score).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 测试数据参考如下：
                String token  =   AccessTokenKeeper.readAccessToken(WBGameActivity.this).getToken();
                try {
                    String result =  GameManager.readPlayerFriendsScoreInfo(WBGameActivity.this, token, Constants.APP_KEY, "23e260e9", "3164868113");
                    //{"results":[],"total_count":0}
                    //结果示例：{"results":[{"score":5,"user":{"name":"Togachy","profile_image_url":"http://tp2.sinaimg.cn/1473615205/50/5724209381/1","uid":"1473615205"}},{"score":15,"user":{"name":"_Will_Wang_","profile_image_url":"http://tp4.sinaimg.cn/1845307915/50/5717341560/1","uid":"1845307915"}}],"total_count":2}
                    if(result != null){
                        result_msg.setText(result);
                    }  
                } catch (Exception e) {
                    System.out.println("异常信息"+e.getMessage());  
                }
                
            }
        });
        
        /***  读取玩家获取成就列表
            source true    string  申请应用时分配的AppKey，
            game_id  true    string  游戏id
            user_id  true    string  用户id
         */
        //读取玩家游戏分数  接口6
        findViewById(R.id.read_achievement_user_gain).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String token  =   AccessTokenKeeper.readAccessToken(WBGameActivity.this).getToken();
                try {
                    String result =    GameManager.readPlayerAchievementGain(WBGameActivity.this,token, Constants.APP_KEY, "23e260e9", "3164868113");
                    // 成功：{"achievements":[{"achievement":{"achievement_id":"2025358007:23e260e9001","description":"description_test","game_point":10,"image":"image_test","object_type":"achievement","title":"title","url":"http%3A%2F%2Furl.cn"},"publish_time":"1970-01-17 23:19:23"}],"total_count":1}
                    if(result != null){
                        result_msg.setText(result);
                    }  
                } catch (Exception e) {
                    System.out.println("异常信息"+e.getMessage());  
                }
                
            }
        });
        
        
        //邀请好友列表 h5  接口
        findViewById(R.id.invatation_friend_list).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String token  =   AccessTokenKeeper.readAccessToken(WBGameActivity.this).getToken();
                AuthListener  ls = new AuthListener(); //邀请结果回调函数
                GameManager  manager  = new GameManager();
                manager.invatationWeiboFriendsByList(WBGameActivity.this, token, Constants.APP_KEY,"邀请好友",ls);
            }
        });
        
        //邀请       单个 h5页面  接口    (上限一次5个好友  uid 最多一次五个 )  
        findViewById(R.id.invatation_one_friend).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String token  =   AccessTokenKeeper.readAccessToken(WBGameActivity.this).getToken();
                
                  AuthListener  ls = new AuthListener();
                  GameManager  manager  = new GameManager();
                  ArrayList<String>  userIdList  = new ArrayList<String>();
                  EditText  uid_list  = (EditText)findViewById(R.id.uid_list);
                  String  uids =  uid_list.getText().toString();
                  String []  uidStr ;
                  try {
                    if(!TextUtils.isEmpty(uids)){
                        if(uids.indexOf(",")>0){
                            uidStr = uids.split(",");
                            for (int i = 0; i < uidStr.length; i++) {
                                userIdList.add(uidStr[i]);
                            }
                        }else{
                            userIdList.add(uids);
                        }
                    }
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                  if(userIdList.size()>0){
                      manager.invatationWeiboFriendsInOnePage(WBGameActivity.this, token, Constants.APP_KEY,"邀请好友",ls,userIdList);
                  }
            }
        });
    }
   
    
    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {
        
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            Toast.makeText(WBGameActivity.this, "邀请成功", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(WBGameActivity.this, "取消邀请", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(WBGameActivity.this, "邀请失败", Toast.LENGTH_LONG).show();
        }
    }
    
}