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

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//import com.sina.weibo.sdk.statistic.StatisticConfig;
import com.sina.weibo.sdk.statistic.WBAgent;
import com.sina.weibo.sdk.utils.LogUtil;

public class WBStatisticActivity extends Activity {
	/** 上传按钮 */
	private Button mUploadButton;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_log);
		mContext = this;
		mUploadButton = (Button) findViewById(R.id.upload_button);
		final Button eventButton = (Button) findViewById(R.id.event_button);
		final Button showButton = (Button) findViewById(R.id.btnCreate);
		final Button deleteButton = (Button) findViewById(R.id.btnDestroy);

//		WBAgent.setDebugMode(true, true);

		eventButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Map<String, String> extend = new HashMap<String, String>();
				extend.put("test", "click");
				WBAgent.onEvent(mContext, "click event", extend);
			}

		});

		// 生成fragment按钮
		showButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 步骤一：添加一个FragmentTransaction的实例
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction transaction = fragmentManager
						.beginTransaction();

				// 步骤二：用add()方法加上Fragment的对象rightFragment
				WBFragmentImp dynamic_fragment = new WBFragmentImp();
				transaction.add(R.id.dynamic_fragment, dynamic_fragment);

				// 步骤三：调用commit()方法使得FragmentTransaction实例的改变生效
				transaction.commit();
			}

		});

		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 步骤一：添加一个FragmentTransaction的实例
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction transaction = fragmentManager
						.beginTransaction();

				WBFragmentImp fragment = (WBFragmentImp) getFragmentManager()
						.findFragmentById(R.id.dynamic_fragment);
				// 步骤二：用remove()方法销毁Fragment的对象fragment
				transaction.remove(fragment);
				// 步骤三：调用commit()方法使得FragmentTransaction实例的改变生效
				transaction.commit();
			}

		});

		// 上传按钮
		mUploadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 强制上传
				WBAgent.uploadAppLogs(mContext);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		WBAgent.onPageStart(this.getClass().getName());
		WBAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		WBAgent.onPageEnd(this.getClass().getName());
		WBAgent.onPause(this);
	}

}
