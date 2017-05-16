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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.sina.weibo.sdk.WeiboAppManager.WeiboInfo;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 该类演示了第三方应用如何通过微博客户端  分享   信息   到私信好友
 * 执行流程： 从本应用->微博->本应用
 * 
 * @author SINA
 * @since 2015-11-16
 */
public class WBShareToMessageFriendActivity extends Activity implements OnClickListener{

    /** 分享按钮 */
    private Button          mSharedBtn;
    /** 微博微博分享接口实例 */
    private IWeiboShareAPI  mWeiboShareAPI = null;
    /** 微博应用信息 **/
    private WeiboInfo mWeiboInfo;
    
    public static  final String ACTION_SHEAR_RESULT = "extend_third_share_result";
    public static final String SHARE_APP_NAME = "shareAppName";
    public static final String PARAM_SHARE_FROM = "share_from";//标记分享来源点
    public static final String EXTEND_SHARE_570 = "extend_share_570";//标记分享来源点
    
    public  ShearMessageReceiver  shearMessageReceiver;
    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_page);
//       mWeiboInfo = WeiboAppManager.getInstance(this).getWeiboInfo();
        
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);
        mWeiboShareAPI.registerApp();
        
        shearMessageReceiver = new ShearMessageReceiver();
        IntentFilter filter = new IntentFilter();  
        filter.addAction(ACTION_SHEAR_RESULT);  
        registerReceiver(shearMessageReceiver, filter); 

        this.findViewById(R.id.shear_card).setOnClickListener(this);
        this.findViewById(R.id.shear_url).setOnClickListener(this);
    }

    @Override
    protected void onNewIntent( Intent intent ) {
        super.onNewIntent(intent);
    }

    /**
     * 用户点击分享按钮，唤起微博客户端进行分享。
     */
    @Override
    public void onClick(View v) {
        if (R.id.shear_url == v.getId()) {
//            if( isWeiboAppInstalled()){
            if(!mWeiboShareAPI.isWeiboAppInstalled()){
                Toast.makeText(this, "微博未安装", Toast.LENGTH_LONG).show();
            }
            
            //显示
              if(true){
                Bundle  bundle  = new Bundle();
                Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
                bundle.putString(WBConstants.SDK_WEOYOU_SHARETITLE, "测试标题");
                bundle.putString(WBConstants.SDK_WEOYOU_SHAREDESC, "我要分享的私信内容");
//                bundle.putString(WBConstants.SDK_WEOYOU_SHAREURL, "http://i2.api.weibo.com/2/short_url/info.json?source=2796559090&url_short=http://t.cn/RUEbk59");
//                bundle.putString(WBConstants.SDK_WEOYOU_SHAREURL, "http://weibo.com/p/1001603910171175841314");
                bundle.putString(WBConstants.SDK_WEOYOU_SHAREURL, "http://www.baidu.com");
//                bundle.putString(WBConstants.SDK_WEOYOU_SHARECARDID, "1008085766e60d6825fa86c6923a91bcff6f85");
                bundle.putString(SHARE_APP_NAME, "测试demo");
                bundle.putString("shareBackScheme", "weiboDemo://share");
                bundle.putString(PARAM_SHARE_FROM, EXTEND_SHARE_570);
                bundle.putByteArray(WBConstants.SDK_WEOYOU_SHAREIMAGE, bitMapToBytes(bitmap));
                mWeiboShareAPI.shareMessageToWeiyou(WBShareToMessageFriendActivity.this, bundle);
            }
        }
        
        if (R.id.shear_card == v.getId()) {
//            if( isWeiboAppInstalled()){
            if(!mWeiboShareAPI.isWeiboAppInstalled()){
                Toast.makeText(this, "微博未安装", Toast.LENGTH_LONG).show();
            }
            //显示
            if(true){
                Bundle  bundle  = new Bundle();
                Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo);
                bundle.putString(WBConstants.SDK_WEOYOU_SHARETITLE, "测试标题");
                bundle.putString(WBConstants.SDK_WEOYOU_SHAREDESC, "我要分享的私信内容");
//                bundle.putString(WBConstants.SDK_WEOYOU_SHAREURL, "http://i2.api.weibo.com/2/short_url/info.json?source=2796559090&url_short=http://t.cn/RUEbk59");
//                bundle.putString(WBConstants.SDK_WEOYOU_SHAREURL, "http://weibo.com/p/1001603910171175841314");
                bundle.putString(WBConstants.SDK_WEOYOU_SHAREURL, "http://t.cn/RUEbk59");
                bundle.putString("shareBackScheme", "weiboDemo://share");
//                bundle.putString(WBConstants.SDK_WEOYOU_SHARECARDID, "1008085766e60d6825fa86c6923a91bcff6f85");
                bundle.putString(SHARE_APP_NAME, "测试demo");
                bundle.putString(PARAM_SHARE_FROM, EXTEND_SHARE_570);
                bundle.putByteArray(WBConstants.SDK_WEOYOU_SHAREIMAGE, bitMapToBytes(bitmap));
                mWeiboShareAPI.shareMessageToWeiyou(WBShareToMessageFriendActivity.this, bundle);
            }else{
                Toast.makeText(this, "微博未安装", Toast.LENGTH_LONG).show();
            }
        }
    }



    public boolean isWeiboAppInstalled() {
        return (mWeiboInfo != null && mWeiboInfo.isLegal()) ? true : false;
    }
    
    public byte[] bitMapToBytes(Bitmap  bitmap) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            return  os.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("Weibo.ImageObject", "put thumb failed");
        }finally{
            try {
                if(os!=null){
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       return null;
    }
    
    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
  
    class ShearMessageReceiver extends BroadcastReceiver {
        
        // 这里可以用handler  处理
        @Override
        public void onReceive(Context context, Intent intent) {
         //resultCode    分享状态：0成功；1失败；2取消；
         //actionCode    分享完成：0，返回app；1，留在微博
        Bundle bundle  =   intent.getExtras();
        
        if(bundle!=null){
          final  int  resultCode = bundle.getInt("resultCode",-1);
          final  int  actionCode = bundle.getInt("actionCode",-1);
          String  resultStr = "";
          String  actionStr = "";
          switch (resultCode) {
            case 0:
                resultStr ="成功";
                break;
            case 1:
                resultStr ="失败";
                break;
            case 2:
                resultStr ="取消";
                break;
            default:
                break;
          }
          
          switch (actionCode) {
              case 0:
                  actionStr ="返回app";
                  break;
              case 1:
                  actionStr ="留在微博";
                  break;
              default:
                  break;
          }
          
          
          final String resultShowStr =resultStr ;
      //    Toast.makeText(WBShareToMessageFriendActivity.this, "分享结果   "+resultShowStr+"  操作结果    " + actionStr  , Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(WBShareToMessageFriendActivity.this, "分享失败", Toast.LENGTH_LONG).show();
        }
        
        
//         final String  result =   intent.getStringExtra("resultCode");
//            if(ACTION_SHEAR_RESULT.equals(intent.getAction())){
//                  runOnUiThread(new Runnable() {
//                    public void run() {
//                         Toast.makeText(WBShareToMessageFriendActivity.this, "收到分享结果信息  result"+result, Toast.LENGTH_LONG).show();
//                    }
//                });
//                
//            }
        }
    }
    
    

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(shearMessageReceiver);
    }
        
    

}
