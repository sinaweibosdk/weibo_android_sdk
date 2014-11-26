//package com.sina.weibo.sdk.api.share.ui;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.List;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.sina.weibo.sdk.api.CmdObject;
//import com.sina.weibo.sdk.api.ImageObject;
//import com.sina.weibo.sdk.api.MusicObject;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.VideoObject;
//import com.sina.weibo.sdk.api.VoiceObject;
//import com.sina.weibo.sdk.api.WebpageObject;
//import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.api.share.IWeiboShareListener;
//import com.sina.weibo.sdk.api.share.WeiboShareCallbackManager;
//import com.sina.weibo.sdk.constant.WBConstants;
//import com.sina.weibo.sdk.exception.WeiboException;
//import com.sina.weibo.sdk.net.AsyncWeiboRunner;
//import com.sina.weibo.sdk.net.RequestListener;
//import com.sina.weibo.sdk.net.WeiboParameters;
//import com.sina.weibo.sdk.utils.LogUtil;
//import com.sina.weibo.sdk.utils.ResourceManager;
//import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.ColorStateList;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.text.Editable;
//import android.text.Selection;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.text.TextUtils.TruncateAt;
//import android.util.DisplayMetrics;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.view.ViewTreeObserver.OnPreDrawListener;
//import android.view.Window;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.ImageView.ScaleType;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//@SuppressWarnings( "unused" )
//public class WeiboShareActivity extends Activity implements View.OnClickListener {
//
//    private static final int TOKEN_ERR_REJECTED = 21317;
//    private static final int TOKEN_ERR_EXPIRED   = 21327;
//    private static final int TOKEN_ERR_INVALID    = 21332;
//    
//    public static final String KEY_APPKEY = "key_appkey";
//    public static final String KEY_TOKEN = "key_token";
//    public static final String KEY_SHARE_CALLBACK_ID = "key_share_callback_id";
//    
//    private static final int MAX_LIMIT = 140;
//    
//    private InputMethodManager mInputMethodManager;
//    
//    private RelativeLayout rlTitleBar;
//    private TextView tvTitleText;
//    private TextView tvSendBtn;
//    private TextView tvBackBtn;
//    
//    private EditBlogView etMblog;
//    private ImageView ivInsertPic;
//    private TextView tvTextLimit;
//    
//    private boolean dispatchTouchWhenCreated = false;
//    
//    private IWeiboShareListener shareListener;
//    private String callBackId;
//    private String token;
//    private String appKey;
//    private Bitmap insertPicBmp;
//    
//    private void refreshTextLimit() {
//        int numFilter = getSurplusNums();
//        if(numFilter <= 10){
//            tvTextLimit.setText(String.valueOf(numFilter));
//            tvTextLimit.setVisibility(View.VISIBLE);
//        }else{
//            tvTextLimit.setVisibility(View.INVISIBLE);
//        }
//        
//        if(numFilter >= 0){
//            tvTextLimit.setTextColor(0xFF828282);
//        }else{
//            tvTextLimit.setTextColor(0xFFE14123);
//        }
//    }
//    
//    public int getSurplusNums() {
//        int numFilter = MAX_LIMIT - getContentLength(etMblog.getText().toString());
//        return numFilter;
//    }
//    
//    private int getContentLength(String text) {
//        if (text == null) {
//            return 0;
//        }
//        int totle = 0;
//        boolean isEmptyText = true;
//        char ch = 0;
//        for (int i = 0, len = text.length(); i < len; i++) {
//            ch = (char) text.codePointAt(i);
//            if (isEmptyText && ch != ' ' && ch != '\n') { // 文本含有不是空格和回车的字符
//                isEmptyText = false;
//            }
//            totle += ch > 255 ? 2 : 1;
//        }
//        if (isEmptyText) { // 文本只含有空格和回车
//            return 0;
//        }
//        return (int) Math.ceil(totle / 2.);
//    }
//    
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            sendSdkCancleResponse(this);
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//    
//    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        super.onCreate(savedInstanceState);
//        
//        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        
//        setContentView(initView());
//        
//        initData();
//
//        tvBackBtn.setOnClickListener(this);
//        tvSendBtn.setOnClickListener(this);
//        
//        final ViewTreeObserver viewTreeObserver = etMblog.getViewTreeObserver();
//        /*
//         * 为了解决在4.1及以上rom中第一次长按输入框以外的view不能弹出"粘贴"选项的bug，在onPreDraw方法中模拟一次touch事件
//         */
//        viewTreeObserver.addOnPreDrawListener(new OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                if (!dispatchTouchWhenCreated ) {
//                    long downtime = SystemClock.uptimeMillis();
//                    int selectionStart = etMblog.getSelectionStart();
//                    int y = etMblog.getHeight();
//                    int x = etMblog.getWidth();
//                    final MotionEvent downEvent = MotionEvent.obtain(downtime, downtime,
//                            MotionEvent.ACTION_DOWN, x, y, 0);
//                    etMblog.dispatchTouchEvent(downEvent);
//                    final MotionEvent upEvent = MotionEvent.obtain(downtime + 1, downtime + 1,
//                            MotionEvent.ACTION_UP, x, y, 0);
//                    etMblog.dispatchTouchEvent(upEvent);
//                    //恢复光标位置
//                    if( selectionStart != -1){
//                        Selection.setSelection(etMblog.getText(), selectionStart);
//                    }
//                    dispatchTouchWhenCreated = true;
//                }
//                return true;
//            }
//        });
//
//        etMblog.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                    int count) {
//                
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                    int after) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                refreshTextLimit();
//                setSendButtonState();
//            }
//        });
//        
//    }
//    
//    private void initData() {
//        Intent intent = getIntent();
//        appKey = intent.getStringExtra(KEY_APPKEY);
//        token = intent.getStringExtra(KEY_TOKEN);
//        callBackId = intent.getStringExtra(KEY_SHARE_CALLBACK_ID);
//        if (TextUtils.isEmpty(token)) {
//            sendSdkErrorResponse(this, "send error token is null!!!");
//            finish();
//            return;
//        }
//        if (TextUtils.isEmpty(callBackId)) {
//            sendSdkErrorResponse(this, "send error internal!!!");
//            finish();
//            return;
//        }
//        
//        shareListener = WeiboShareCallbackManager.getInstance(this).getWeiboShareListener(callBackId);
//        WeiboShareCallbackManager.getInstance(this).removeWeiboShareListener(callBackId);
//        if (shareListener == null) {
//            sendSdkErrorResponse(this, "send error internal!!!");
//            finish();
//            return;
//        }
//        
//        handleSharedMessage(intent.getExtras());
//    }
//    
//    private void handleSharedMessage( Bundle bundle ) {
//        WeiboMultiMessage multiMessage = new WeiboMultiMessage();
//        multiMessage.toObject(bundle);
//        
//        if (multiMessage.textObject instanceof TextObject) {
//            TextObject textObject = (TextObject) multiMessage.textObject;
//            insertSendTxt(textObject.text);
//        } 
//        if (multiMessage.imageObject instanceof ImageObject) {
//            ImageObject imageObject = (ImageObject) multiMessage.imageObject;
//            insertMblogPic(imageObject.imagePath, imageObject.imageData);
//        } 
//        if (multiMessage.mediaObject instanceof TextObject) {
//            TextObject textObject = (TextObject) multiMessage.mediaObject;
//            insertSendTxt(textObject.text);
//        } 
//        if (multiMessage.mediaObject instanceof ImageObject) {
//            ImageObject imageObject = (ImageObject) multiMessage.mediaObject;
//            insertMblogPic(imageObject.imagePath, imageObject.imageData);
//        }
//        if (multiMessage.mediaObject instanceof WebpageObject) {
//            WebpageObject webPageObject = (WebpageObject) multiMessage.mediaObject;
//            insertSharedUrl(webPageObject.actionUrl);
//        }
//        if (multiMessage.mediaObject instanceof MusicObject) {
//            MusicObject musicObject = (MusicObject) multiMessage.mediaObject;
//            insertSharedUrl(musicObject.actionUrl);
//        }
//        if (multiMessage.mediaObject instanceof VideoObject) {
//            VideoObject videoObject = (VideoObject) multiMessage.mediaObject;
//            insertSharedUrl(videoObject.actionUrl);
//        }
//        if (multiMessage.mediaObject instanceof VoiceObject) {
//            VoiceObject voiceObject = (VoiceObject) multiMessage.mediaObject;
//            insertSharedUrl(voiceObject.actionUrl);
//        }
//        
//        setSendButtonState();
//    }
//    
//    private int dp2px(float density, int dp) {
//        return (int) Math.ceil(density * dp);
//    }
//    
//    private void setButtonColorStateList(TextView button) {
//        int[] titleBtnColors = new int[] { 0x66ff8200, 0x88525252, 0xFFFF8200 };
//        int[][] titleBtnStates = new int[3][];
//        titleBtnStates[0] = new int[] { android.R.attr.state_focused, android.R.attr.state_selected, android.R.attr.state_pressed };  
//        titleBtnStates[1] = new int[] { -1 * android.R.attr.state_enabled };
//        titleBtnStates[2] = new int[] {};
//        ColorStateList titleBtnColorStateList = new ColorStateList(titleBtnStates, titleBtnColors);
//        button.setTextColor(titleBtnColorStateList);
//    }
//    
//    private View initView() {
//        final int titleBarId = 1;
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        final float density = dm.density;
//        
//        RelativeLayout contentView = new RelativeLayout(this);
//        contentView.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.FILL_PARENT, 
//                ViewGroup.LayoutParams.FILL_PARENT));
//        
//        RelativeLayout titleBar = new RelativeLayout(this);
//        titleBar.setId(titleBarId);
//        titleBar.setGravity(Gravity.CENTER_VERTICAL);
//        titleBar.setBackgroundDrawable(
//                ResourceManager.getNinePatchDrawable(this, 
//                        ResourceManager.drawable_share_weibo_title_bg));
//        RelativeLayout.LayoutParams titleBarViewLp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.FILL_PARENT, dp2px(density, 45));
//        titleBarViewLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//        titleBar.setLayoutParams(titleBarViewLp);
//        rlTitleBar = titleBar;
//
//        final TextView backBtn = new TextView(this);
//        backBtn.setGravity(Gravity.CENTER);
//        setButtonColorStateList(backBtn);
//        backBtn.setText(ResourceManager.getString(this, ResourceManager.string_cancel_btn));
//        backBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
//        backBtn.setClickable(true);
//        RelativeLayout.LayoutParams backBtnLp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, 
//                RelativeLayout.LayoutParams.FILL_PARENT);
//        backBtnLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        backBtnLp.addRule(RelativeLayout.CENTER_VERTICAL);
//        backBtnLp.leftMargin = dp2px(density, 12);
//        backBtnLp.rightMargin = dp2px(density, 10);
//        backBtn.setLayoutParams(backBtnLp);
//        tvBackBtn = backBtn;
//        
//        TextView titleText = new TextView(this);
//        titleText.setEllipsize(TruncateAt.END);
//        titleText.setGravity(Gravity.CENTER);
//        titleText.setIncludeFontPadding(false);
//        titleText.setMaxWidth(dp2px(density, 130));
//        titleText.setSingleLine(true);
//        titleText.setText(ResourceManager.getString(this, ResourceManager.string_share_weibo_title));
//        titleText.setTextColor(0xFF525252);
//        titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//        RelativeLayout.LayoutParams titleTextLp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, 
//                RelativeLayout.LayoutParams.FILL_PARENT);
//        titleTextLp.addRule(RelativeLayout.CENTER_IN_PARENT);
//        titleTextLp.leftMargin = dp2px(density, 40);
//        titleTextLp.rightMargin = dp2px(density, 40);
//        titleText.setLayoutParams(titleTextLp);
//        tvTitleText = titleText;
//        
//        final TextView sendBtn = new TextView(this);
//        sendBtn.setGravity(Gravity.CENTER);
//        setButtonColorStateList(sendBtn);
//        sendBtn.setText(ResourceManager.getString(this, ResourceManager.string_send_btn));
//        sendBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
//        sendBtn.setClickable(true);
//        RelativeLayout.LayoutParams sendBtnLp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, 
//                RelativeLayout.LayoutParams.FILL_PARENT);
//        sendBtnLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        sendBtnLp.addRule(RelativeLayout.CENTER_VERTICAL);
//        sendBtnLp.leftMargin = dp2px(density, 10);
//        sendBtnLp.rightMargin = dp2px(density, 12);
//        sendBtn.setLayoutParams(sendBtnLp);
//        tvSendBtn = sendBtn;
//        
//        LinearLayout contentView1 = new LinearLayout(this);
//        contentView1.setOrientation(LinearLayout.VERTICAL);
//        RelativeLayout.LayoutParams contentViewLp1 = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
//        contentViewLp1.addRule(RelativeLayout.BELOW, titleBarId);
//        contentView1.setLayoutParams(contentViewLp1);
//        
//        RelativeLayout contentView2 = new RelativeLayout(this);
//        contentView2.setBackgroundColor(0xFFFFFFFF);
//        LinearLayout.LayoutParams contentViewLp2 = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.FILL_PARENT, 0);
//        contentViewLp2.weight = 1;
//        contentView2.setLayoutParams(contentViewLp2);
//        
//        ScrollView scrollView = new ScrollView(this);
//        scrollView.setFadingEdgeLength(0);
//        scrollView.setVerticalFadingEdgeEnabled(false);
//        scrollView.setHorizontalFadingEdgeEnabled(false);
//        scrollView.setVerticalScrollBarEnabled(false);
//        scrollView.setHorizontalScrollBarEnabled(false);
//        scrollView.setBackgroundColor(0xFFFFFFFF);
//        RelativeLayout.LayoutParams scrollViewLp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
//        scrollViewLp.bottomMargin = dp2px(density, 32);
//        scrollView.setLayoutParams(scrollViewLp);
//
//        LinearLayout contentView3 = new LinearLayout(this);
//        contentView3.setOrientation(LinearLayout.VERTICAL);
//        contentView3.setBackgroundColor(0xFFFFFFFF);
//        FrameLayout.LayoutParams contentViewLp3 = new FrameLayout.LayoutParams(
//                FrameLayout.LayoutParams.FILL_PARENT, dp2px(density, 80));
//        contentView3.setLayoutParams(contentViewLp3);
//        
//        final EditBlogView editBlogView = new EditBlogView(this);
//        editBlogView.setBackgroundColor(0xFFFFFFFF);
//        editBlogView.setGravity(Gravity.TOP | Gravity.LEFT);
//        editBlogView.setHint(ResourceManager.getString(this, ResourceManager.string_say_something));
//        editBlogView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        editBlogView.setScrollContainer(true);
//        editBlogView.setMinLines(2);
//        editBlogView.setSingleLine(false);
//        editBlogView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//        LinearLayout.LayoutParams editBlogViewLp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        editBlogView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                editBlogView.setPadding(
//                        dp2px(density, 10), dp2px(density, 3), dp2px(density, 8), dp2px(density, 3));
//            }
//        });
//        editBlogView.setLayoutParams(editBlogViewLp);
//        etMblog = editBlogView;
//        
//        ImageView insertPicView = new ImageView(this);
//        insertPicView.setScaleType(ScaleType.FIT_XY);
//        LinearLayout.LayoutParams insertPicViewLp = new LinearLayout.LayoutParams(dp2px(density, 83), dp2px(density, 83));
//        insertPicViewLp.leftMargin = dp2px(density, 5);
//        insertPicViewLp.rightMargin = dp2px(density, 5);
//        insertPicViewLp.topMargin = dp2px(density, 6);
//        insertPicViewLp.bottomMargin = dp2px(density, 10);
//        insertPicView.setVisibility(View.GONE);
//        insertPicView.setLayoutParams(insertPicViewLp);
//        ivInsertPic = insertPicView;
//        
//        RelativeLayout bottomContent = new RelativeLayout(this);
//        bottomContent.setGravity(Gravity.CENTER_VERTICAL);
//        RelativeLayout.LayoutParams bottomContentLp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        bottomContentLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        bottomContent.setLayoutParams(bottomContentLp);
//        
//        TextView limitText = new TextView(this);
//        limitText.setClickable(true);
//        limitText.setFocusable(true);
//        limitText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//        RelativeLayout.LayoutParams limitTextLp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        limitTextLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//        limitTextLp.leftMargin = dp2px(density, 15);
//        limitTextLp.rightMargin = dp2px(density, 15);
//        limitTextLp.bottomMargin = dp2px(density, 8);
//        limitText.setLayoutParams(limitTextLp);
//        tvTextLimit = limitText;
//        
//        titleBar.addView(backBtn);
//        titleBar.addView(titleText);
//        titleBar.addView(sendBtn);
//        contentView.addView(titleBar);
//        
//        contentView3.addView(editBlogView);
//        contentView3.addView(insertPicView);
//        scrollView.addView(contentView3);
//        contentView2.addView(scrollView);
//        bottomContent.addView(limitText);
//        contentView2.addView(bottomContent);
//        contentView1.addView(contentView2);
//        contentView.addView(contentView1);
//        
//        return contentView;
//    }
//    
//    public static Bitmap safeDecodeBimtapFile( String bmpFile, BitmapFactory.Options opts ) {
//        BitmapFactory.Options optsTmp = opts;
//        if ( optsTmp == null ) {
//            optsTmp = new BitmapFactory.Options();
//            optsTmp.inSampleSize = 1;
//        }
//        
//        Bitmap bmp = null;
//        FileInputStream input = null;
//        
//        final int MAX_TRIAL = 5;
//        for( int i = 0; i < MAX_TRIAL; ++i ) {
//            try {
//                input = new FileInputStream( bmpFile );
//                bmp = BitmapFactory.decodeStream(input, null, opts);
//                try {
//                    if (input != null) {
//                        input.close();
//                    }
//                } catch (IOException e) {}
//                break;
//            }
//            catch( OutOfMemoryError e ) {
//                e.printStackTrace();
//                optsTmp.inSampleSize *= 2;
//                try {
//                    if (input != null) {
//                        input.close();
//                    }
//                } catch (IOException e1) {}
//            }
//            catch (FileNotFoundException e) {
//                break;
//            }
//        }
//        
//        return bmp;
//    }
//    
//    private void insertMblogPic(String picPath, byte[] thumbData) {
//        try {
//            if (!TextUtils.isEmpty(picPath)) {
//                File picFile = new File(picPath);
//                if (picFile.exists() && picFile.canRead()) {
//                    Bitmap bmp = safeDecodeBimtapFile(picPath, null);
//                    if (bmp != null && !bmp.isRecycled()) {
//                        insertPicBmp = bmp;
//                        ivInsertPic.setImageBitmap(bmp);
//                        ivInsertPic.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        } catch (SecurityException e) {}
//
//        if (thumbData != null && thumbData.length > 0) {
//            try {
//                Bitmap bmp = BitmapFactory.decodeByteArray(thumbData, 0, thumbData.length);
//                if (bmp != null && !bmp.isRecycled()) {
//                    insertPicBmp = bmp;
//                    ivInsertPic.setImageBitmap(bmp);
//                    ivInsertPic.setVisibility(View.VISIBLE);
//                }
//            } catch (OutOfMemoryError e) {}
//        }
//        
//        setSendButtonState();
//    }
//    
//    private void insertSharedUrl(String url) {
//        if (!TextUtils.isEmpty(url)) {
//            etMblog.getText().append(" " + url);
//        }
//    }
//    
//    private void insertSendTxt(String text) {
//        if (!TextUtils.isEmpty(text)) {
//            etMblog.setText(text);
//        }
//    }
//    
//    private void setSendButtonState(){
//        String content = etMblog.getText().toString();
//        if (TextUtils.isEmpty(content) && !isPicShow()) {
//            tvSendBtn.setEnabled(false);
//        } else {
//            if (!TextUtils.isEmpty(content)) {
//                if (getSurplusNums() < 0) {
//                    tvSendBtn.setEnabled(false);
//                } else {
//                    tvSendBtn.setEnabled(true);
//                }
//            } else if (isPicShow()) {
//                tvSendBtn.setEnabled(true);
//            }
//        }
//        
//    }
//    
//    //判断当前是否有图片显示
//    private boolean isPicShow() {
//        return ivInsertPic.getVisibility() == View.VISIBLE;
//    }
//    
//    private void sendSdkResponse( Activity activity, int errCode, String errMsg ) {
//        Bundle bundle = getIntent().getExtras();
//        if (bundle == null) {
//            return;
//        }
//        Intent intent = new Intent(WBConstants.ACTIVITY_REQ_SDK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        intent.setPackage(bundle.getString(WBConstants.Base.APP_PKG));
//
//        intent.putExtras(bundle);
//        intent.putExtra(WBConstants.Base.APP_PKG, activity.getPackageName());
//        intent.putExtra(WBConstants.Response.ERRCODE, errCode);
//        intent.putExtra(WBConstants.Response.ERRMSG, errMsg);
//        try {
//            activity.startActivityForResult(intent, WBConstants.SDK_ACTIVITY_FOR_RESULT_CODE);
//        } catch (ActivityNotFoundException e) {
//        }
//    }
//    
//    private void sendSdkCancleResponse( Activity activity ) {
//        sendSdkResponse(activity, WBConstants.ErrorCode.ERR_CANCEL, "send cancel!!!");
//    }
//
//    private void sendSdkOkResponse( Activity activity ) {
//        sendSdkResponse(activity, WBConstants.ErrorCode.ERR_OK, "send ok!!!");
//    }
//    
//    private void sendSdkErrorResponse( Activity activity, String msg ) {
//        sendSdkResponse(activity, WBConstants.ErrorCode.ERR_FAIL, msg);
//    }
//    
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }
//
//    protected void onDestroy() {    
//        super.onDestroy();
//    }
//
//    protected void onResume() {
//        super.onResume();
//    }
//    
////    private boolean setInputMethodVisibility(boolean visibility) {
////        if ((mInputMethodManager != null) && (etMblog != null)) {
////            if (visibility) {
////                getWindow().setSoftInputMode(
////                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE 
////                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
////                mInputMethodManager.showSoftInput(etMblog, 0);
////                mBottomPanelHelper.setPanelType(BottomPanelHelper.PANEL_SOFTKEYBOARD);
////            } else {
////                if (mInputMethodManager.isActive(etMblog)) {
////                    getWindow().setSoftInputMode(
////                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN 
////                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
////                    mInputMethodManager.hideSoftInputFromWindow(etMblog
////                            .getWindowToken(),
////                            0);
////                }
////            }
////        }
////        return false;
////    }
//    
//    @Override
//    public void onClick( View v ) {
//        if (tvBackBtn == v) {
//            sendSdkCancleResponse(this);
//            finish();
//        }
//        else if (tvSendBtn == v) {
//            sendSdkOkResponse(this);
//            startSendWeibo();
//            finish();
//        }
//    }
//    
//    private boolean isTokenError(String errMsg) {
//        if (TextUtils.isEmpty(errMsg)) {
//            return false;
//        }
//        try {
//            JSONObject obj = new JSONObject(errMsg);
//            int errCode = obj.optInt("error_code", 0);
//            if (errCode == TOKEN_ERR_EXPIRED 
//                    || errCode == TOKEN_ERR_REJECTED 
//                    || errCode == TOKEN_ERR_INVALID) {
//                return true;
//            }
//        } catch (JSONException e) {
//        }
//        return false;
//    }
//    
//    private void startSendWeibo() {
//        final String content = etMblog.getText().toString();
//        final RequestListener listener = new RequestListener() {
//            @Override
//            public void onWeiboException( WeiboException e ) {
//                if (shareListener != null && e != null) {
//                    if (isTokenError(e.getMessage())) {
//                        shareListener.onTokenError(token);
//                    }
//                }
//            }
//            @Override
//            public void onComplete( String response ) {
//            }
//        };
//        if (isPicShow()) {
//            Bitmap bmp = insertPicBmp;
//            if (bmp != null && !bmp.isRecycled()) {
//                upload(content, bmp, null, null, listener);
//            }
//        } else {
//            update(content, null, null, listener);
//        }
//    }
//    
//    /**
//     * 发布一条新微博（连续两次发布的微博不可以重复）。
//     * 
//     * @param content  要发布的微博文本内容，内容不超过140个汉字。
//     * @param lat      纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
//     * @param lon      经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
//     * @param listener 异步请求回调接口
//     */
//    private void update(String content, String lat, String lon, RequestListener listener) {
//        WeiboParameters params = buildUpdateParams(content, lat, lon);
//        requestAsync("https://api.weibo.com/2/statuses/update.json", params, "POST", listener);
//    }
//    
//    private static final String KEY_ACCESS_TOKEN = "access_token";
//    private void requestAsync(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
//        if (TextUtils.isEmpty(token)
//                || TextUtils.isEmpty(url)
//                || null == params
//                || TextUtils.isEmpty(httpMethod)
//                || null == listener) {
//            return;
//        }
//        params.put(KEY_ACCESS_TOKEN, token);
//        new AsyncWeiboRunner(this).requestAsync(url, params, httpMethod, listener);
//    }
//    
//    private WeiboParameters buildUpdateParams(String content, String lat, String lon) {
//        WeiboParameters params = new WeiboParameters(appKey);
//        params.put("status", content);
//        if (!TextUtils.isEmpty(lon)) {
//            params.put("long", lon);
//        }
//        if (!TextUtils.isEmpty(lat)) {
//            params.put("lat", lat);
//        }
//        return params;
//    }
//
//    /**
//     * 上传图片并发布一条新微博。
//     * 
//     * @param content  要发布的微博文本内容，内容不超过140个汉字
//     * @param bitmap   要上传的图片，仅支持JPEG、GIF、PNG格式，图片大小小于5M
//     * @param lat      纬度，有效范围：-90.0到+90.0，+表示北纬，默认为0.0。
//     * @param lon      经度，有效范围：-180.0到+180.0，+表示东经，默认为0.0。
//     * @param listener 异步请求回调接口
//     */
//    public void upload(String content, Bitmap bitmap, String lat, String lon, RequestListener listener) {
//        WeiboParameters params = buildUpdateParams(content, lat, lon);
//        params.put("pic", bitmap);
//        requestAsync("https://api.weibo.com/2/statuses/upload.json", params, "POST", listener);
//    }
//    
//}