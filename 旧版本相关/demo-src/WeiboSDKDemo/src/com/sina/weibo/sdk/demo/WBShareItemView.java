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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 该类简单的封装了分享中，用于显示文字、图片、视频、音乐等内容的组合控件。
 * 
 * @author SINA
 * @since 2013-10-22
 */
public class WBShareItemView extends LinearLayout {
    
    /** UI 元素 */
    private TextView    mTitleView;
    private ImageView   mThumbView;
    private TextView    mShareTitleView;
    private TextView    mShareDescView;
    private TextView    mShareUrlView;
    private RadioButton mCheckedBtn;
    
    /** RadioButton 在点击时的回调函数 */
    private OnCheckedChangeListener mOnCheckedChangeListener;
    
    /**
     * 当 RadioButton 点击时，该接口对应的的回调函数被调用。
     */
    public interface OnCheckedChangeListener {
        public void onCheckedChanged(WBShareItemView view, boolean isChecked);
    }
    
    /**
     * 创建一个组合控件。
     * 
     * @see View#View(Context)
     */
    public WBShareItemView(Context context) {
        this(context, null);
    }
    
    /**
     * 从 XML 配置文件中创建一个组合控件。
     * 
     * @see View#View(Context, AttributeSet)
     */
    public WBShareItemView(Context context, AttributeSet attrs) {
        // Need API Level > 8
        //this(context, attrs, 0);
        super(context, attrs);
        initialize(context);
    }
    
    /**
     * 从 XML 配置文件中创建一个组合控件。
     * 
     * @see View#View(Context, AttributeSet)
     */
    // Need API Level > 8
    /*
    public WBShareItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }
     */
    /**
     * 用资源 ID 来初始化界面元素。
     */
    public void initWithRes(
            int titleResId, 
            int thumbResId, 
            int shareTitleResId, 
            int shareDescResId, 
            int shareUrlResId) {
        mTitleView.setText(titleResId);
        mThumbView.setImageResource(thumbResId);
        mShareTitleView.setText(shareTitleResId);
        mShareDescView.setText(shareDescResId);
        mShareUrlView.setText(shareUrlResId);
    }
    
    /**
     * 初始化界面元素。
     */
    public void initWithData(
            String title, 
            Drawable thumb, 
            String shareTitle, 
            String shareDesc, 
            String shareUrl) {
        mTitleView.setText(title);
        mThumbView.setImageDrawable(thumb);
        mShareTitleView.setText(shareTitle);
        mShareDescView.setText(shareDesc);
        mShareUrlView.setText(shareUrl);
    }
    
    /**
     * 设置 RadioButton 在点击时的回调函数。
     * 
     * @param listener 回调监听器
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }
    
    /**
     * 获取当前控件的 RadioButton 是否被选中的状态。
     * 
     * @return 选中，返回 true；否则，返回 false
     */
    public boolean isChecked() {
        return mCheckedBtn.isChecked();
    }
    
    /**
     * 设置当前控件的 RadioButton 的被选中状态。
     * 
     * @param isChecked 选中状态
     */
    public void setIsChecked(boolean isChecked) {
        mCheckedBtn.setChecked(isChecked);
    }
    
    /**
     * 获取当前界面的 Title。
     */
    public String getTitle() {
        return mTitleView.getText().toString();
    }
    
    /**
     * 获取当前界面的缩略图对应的 Drawable。
     */
    public Drawable getThumbDrawable() {
        return mThumbView.getDrawable();
    }
    
    /**
     * 获取当前界面的缩略图对应的 Bitmap。
     */
    public Bitmap getThumbBitmap() {
        return ((BitmapDrawable) mThumbView.getDrawable()).getBitmap();
    }
    
    /**
     * 获取当前界面的分享 Title。
     */
    public String getShareTitle() {
        return mShareTitleView.getText().toString();
    }
    
    /**
     * 获取当前界面的分享描述。
     */
    public String getShareDesc() {
        return mShareDescView.getText().toString();
    }
    
    /**
     * 获取当前界面的分享 URL。
     */
    public String getShareUrl() {
        return mShareUrlView.getText().toString();
    }

    /**
     * 初始化界面。
     */
    private void initialize(Context context) {
        LayoutInflater.from(context).inflate(R.layout.share_item_template, this);

        mTitleView      = (TextView)findViewById(R.id.item_title_view);
        mThumbView      = (ImageView)findViewById(R.id.item_thumb_image_btn);
        mShareTitleView = (TextView)findViewById(R.id.item_share_title_view);
        mShareDescView  = (TextView)findViewById(R.id.item_share_desc_view);
        mShareUrlView   = (TextView)findViewById(R.id.item_share_url_view);
        mCheckedBtn     = (RadioButton)findViewById(R.id.item_checked_btn);
        
        mCheckedBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckedChanged(WBShareItemView.this, isChecked);
                }
            }
        });
    }
}
