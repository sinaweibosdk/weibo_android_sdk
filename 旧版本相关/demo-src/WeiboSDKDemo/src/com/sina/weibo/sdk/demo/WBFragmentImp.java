package com.sina.weibo.sdk.demo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sina.weibo.sdk.statistic.WBAgent;
/**
 * fragment页面统计
 * @author xiaofei9
 *
 */
public class WBFragmentImp extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
    
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, null);
        Button button = (Button)view.findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "fragment click event", Toast.LENGTH_SHORT).show();
                WBAgent.onEvent(getActivity(), "click fragment");
            }
        });
        return view;
    }

	@Override
	public void onResume() {
		super.onResume();
		WBAgent.onPageStart("fragmentImp");
		Toast.makeText(getActivity(), "fragmentImp onResume", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPause() {
		super.onPause();
		WBAgent.onPageEnd("fragmentImp");
		Toast.makeText(getActivity(), "fragmentImp onPause", Toast.LENGTH_SHORT).show();
	}
}
