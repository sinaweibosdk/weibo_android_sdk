package com.weibo.sdk.android.demo;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

public class Util {
    protected static void showToast(final Activity activity,final String content) {
        activity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                Toast toast = Toast.makeText(activity, content, Toast.LENGTH_SHORT);
                toast.show();
                
            }
        });
       
    }
    protected static void setTextViewContent(final Activity activity,final TextView textView,final String content) {
        activity.runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
               if(textView!=null){
                   textView.setText(content);
               }
                
            }
        });
       
    }
   

}
