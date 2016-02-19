

package com.sina.weibo.sdk.demo;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.sina.weibo.sdk.api.pay.WeiboPayImpl;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

/**
 * 该类用于演示微博支付。
 * 
 * @author SINA
 * @since 2014-02-21
 */
public class WBPayActivity extends Activity implements OnClickListener,IWeiboHandler.Response {

    /** 微博分享的接口实例 */
    private EditText orderUri, order;
    
    String payParam="" ;  
    String orderString="";
    
    public IWeiboShareAPI    mWeiboShareAPI=null;
    public  WeiboPayImpl   payImpl=null;
    
    String  url_2 = "http://pay.sc.weibo.com/api/client/thirdapp/demo";
    String  tempRequest = "";
//2.0de 支付参数    
    private class GetOrderTask extends AsyncTask<Void, Void, String> {
        String uri = "";
        GetOrderTask(String uri) {
            this.uri = uri;
        }
        
        @Override
        protected String doInBackground(Void... params) {
            tempRequest =  OrderParams.httpGet(url_2);
            try {
                JSONObject  resultJson = new  JSONObject(tempRequest);
                payParam = resultJson.optString("params_str").toString();
            } catch (Exception e) {
               e.printStackTrace();
            }
            return "";
        }
        
        
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Order params get success" + payParam, Toast.LENGTH_SHORT).show();
        }
        
    }

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY);
        mWeiboShareAPI.registerApp(); 
        
        orderUri = (EditText) findViewById(R.id.get_order_uri);
        order = (EditText) findViewById(R.id.order);
        findViewById(R.id.weibo_get_order).setOnClickListener(this);
        findViewById(R.id.weibo_pay).setOnClickListener(this);
    }
    
    
    @Override
    public void onClick(View v) {
        if (R.id.weibo_get_order == v.getId()) {
            GetOrderTask task = new GetOrderTask(orderUri.getText().toString());
            task.execute();
        }
        
        if (R.id.weibo_pay == v.getId()) {
           mWeiboShareAPI.launchWeiboPayLogin(WBPayActivity.this,payParam);
        }
        
        
    }
    

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {

    }
        
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse response) {
        
        switch (response.errCode) {
        case WBConstants.ErrorCode.ERR_OK:
            Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
            break;
        case WBConstants.ErrorCode.ERR_CANCEL:
            Toast.makeText(this, "支付取消", Toast.LENGTH_LONG).show();
            break;
        case WBConstants.ErrorCode.ERR_FAIL:
            Toast.makeText(this,"支付失败     " + "Error Message: " + response.errMsg, Toast.LENGTH_LONG).show();
            break;
        }
    }

    
}
