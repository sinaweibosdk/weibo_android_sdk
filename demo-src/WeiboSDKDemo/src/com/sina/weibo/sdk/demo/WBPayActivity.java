/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package com.sina.weibo.sdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
public class WBPayActivity extends Activity implements IWeiboHandler.Response {

    /** 微博分享的接口实例 */
    private IWeiboShareAPI mWeiboShareAPI = null;
    private EditText orderUri, order;
    
    private class GetOrderTask extends AsyncTask<Void, Void, String> {
        String uri = "";
        GetOrderTask(String uri) {
            this.uri = uri;
        }

        @Override
        protected String doInBackground(Void... params) {
//            return  HttpManager.openUrl(uri, "GET", new WeiboParameters());
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            order.setText(result);
        }

    }

    /**
     * @see {@link Activity#onCreate}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        // 创建微博 SDK 接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.APP_KEY, true);
        mWeiboShareAPI.registerApp();
        final boolean isSupportPay = mWeiboShareAPI.isSupportWeiboPay();
        orderUri = (EditText) findViewById(R.id.get_order_uri);
        order = (EditText) findViewById(R.id.order);
        findViewById(R.id.weibo_get_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetOrderTask task = new GetOrderTask(orderUri.getText().toString());
                task.execute();
            }
        });

        findViewById(R.id.weibo_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSupportPay) {
 //                   mWeiboShareAPI.launchWeiboPay(order.getText().toString());
                }
            }
        });

        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }

        ((TextView) findViewById(R.id.weibosdk_demo_support_weibo_pay)).setText(isSupportPay ? "true" : "false");
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
            Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
            break;
        case WBConstants.ErrorCode.ERR_CANCEL:
            Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled, Toast.LENGTH_LONG).show();
            break;
        case WBConstants.ErrorCode.ERR_FAIL:
            Toast.makeText(this,
                    getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: " + response.errMsg,
                    Toast.LENGTH_LONG).show();
            break;
        }
    }
    
}
