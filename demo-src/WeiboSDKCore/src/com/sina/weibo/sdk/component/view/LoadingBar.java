package com.sina.weibo.sdk.component.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

public class LoadingBar extends TextView {
	private static final int MAX_PROGRESS = 100;
	
	private int mProgress;
	private int mProgressColor;
	private Paint mPaint;
	
	public LoadingBar(Context context) {
		super(context);
		init(context);
	}
	
	public LoadingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public LoadingBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

    private void init(Context context) {
        mHander = new Handler();
        mPaint = new Paint();
        initSkin();
    }
    
    public void initSkin() {
        mProgressColor = 0xFF4d90fe;
    }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setColor(mProgressColor);
		Rect r = getRect();
		canvas.drawRect(r, mPaint);
	}
	
	private Rect getRect() {
		int left = getLeft();
		int top = getTop();
		int right = getLeft() + (getRight() - getLeft()) * mProgress / MAX_PROGRESS;
		int bottom = getBottom();
		return new Rect(0, 0, right - left, bottom - top);
	}

	private Handler mHander;
	private Runnable mRunnable = new Runnable() {
		
		@Override
		public void run() {
			mProgress ++;
			drawProgress(mProgress);
		}
	};
	
	public void drawProgress(int progress) {
		if (progress < 7) {
			mHander.postDelayed(mRunnable, 70);
		} else {
			mHander.removeCallbacks(mRunnable);
			mProgress = progress;
		}
		invalidate();
	}
}
