package com.sina.weibo.sdk.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MusicObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.VoiceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseRequest;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.Base64;
import com.sina.weibo.sdk.utils.MD5;
import com.sina.weibo.sdk.utils.Utility;

public class ShareRequestParam extends BrowserRequestParamBase {
    /**
     * 网页请求参数
     */
    private static final String SHARE_URL = "http://service.weibo.com/share/mobilesdk.php";
    public static final String UPLOAD_PIC_URL = "http://service.weibo.com/share/mobilesdk_uppic.php";
    
    public static final String REQ_PARAM_TITLE = "title";
    public static final String REQ_PARAM_VERSION = "version";
    public static final String REQ_PARAM_SOURCE = "source";
    public static final String REQ_PARAM_AID = "aid";
    public static final String REQ_PARAM_PACKAGENAME = "packagename";
    public static final String REQ_PARAM_KEY_HASH = "key_hash";
    public static final String REQ_PARAM_TOKEN = "access_token";
    public static final String REQ_PARAM_PICINFO = "picinfo";
    public static final String REQ_UPLOAD_PIC_PARAM_IMG = "img";
    
    /**
     * 网页Respons参数
     */
    public static final String RESP_UPLOAD_PIC_PARAM_CODE = "code";
    public static final String RESP_UPLOAD_PIC_PARAM_DATA = "data";
    public static final int RESP_UPLOAD_PIC_SUCC_CODE = 1;
    
    
    private WeiboAuthListener mAuthListener;
    private String mAuthListenerKey;
    private String mAppPackage;
    private String mToken;
    private String mAppKey;
    private String mHashKey;
    private BaseRequest mBaseRequest;
    
    private String mShareContent;
    private byte[] mBase64ImgData;
    
    public ShareRequestParam(Context context) {
        super(context);
        mLaucher = BrowserLauncher.SHARE;
    }

    @Override
    protected void onSetupRequestParam( Bundle data ) {
        
        mAppKey = data.getString(REQ_PARAM_SOURCE);
        mAppPackage = data.getString(REQ_PARAM_PACKAGENAME);
        mHashKey = data.getString(REQ_PARAM_KEY_HASH);
        mToken = data.getString(REQ_PARAM_TOKEN);
        
        mAuthListenerKey = data.getString(AuthRequestParam.EXTRA_KEY_LISTENER);
        if (!TextUtils.isEmpty(mAuthListenerKey)) {
            mAuthListener = WeiboCallbackManager
                    .getInstance(mContext)
                    .getWeiboAuthListener(mAuthListenerKey);
        }
        
        handleSharedMessage(data);
        
        mUrl = buildUrl("");
    }
    
    private void handleSharedMessage( Bundle bundle ) {
        WeiboMultiMessage multiMessage = new WeiboMultiMessage();
        multiMessage.toObject(bundle);
        
        StringBuilder content = new StringBuilder();
        if (multiMessage.textObject instanceof TextObject) {
            TextObject textObject = (TextObject) multiMessage.textObject;
            content.append(textObject.text);
        } 
        if (multiMessage.imageObject instanceof ImageObject) {
            ImageObject imageObject = (ImageObject) multiMessage.imageObject;
            handleMblogPic(imageObject.imagePath, imageObject.imageData);
        } 
        if (multiMessage.mediaObject instanceof TextObject) {
            TextObject textObject = (TextObject) multiMessage.mediaObject;
            content.append(textObject.text);
        } 
        if (multiMessage.mediaObject instanceof ImageObject) {
            ImageObject imageObject = (ImageObject) multiMessage.mediaObject;
            handleMblogPic(imageObject.imagePath, imageObject.imageData);
        }
        if (multiMessage.mediaObject instanceof WebpageObject) {
            WebpageObject webPageObject = (WebpageObject) multiMessage.mediaObject;
            content.append(" ").append(webPageObject.actionUrl);
        }
        if (multiMessage.mediaObject instanceof MusicObject) {
            MusicObject musicObject = (MusicObject) multiMessage.mediaObject;
            content.append(" ").append(musicObject.actionUrl);
        }
        if (multiMessage.mediaObject instanceof VideoObject) {
            VideoObject videoObject = (VideoObject) multiMessage.mediaObject;
            content.append(" ").append(videoObject.actionUrl);
        }
        if (multiMessage.mediaObject instanceof VoiceObject) {
            VoiceObject voiceObject = (VoiceObject) multiMessage.mediaObject;
            content.append(" ").append(voiceObject.actionUrl);
        }
        
        mShareContent = content.toString();
    }
    
    private void handleMblogPic(String picPath, byte[] thumbData) {
        try {
            if (!TextUtils.isEmpty(picPath)) {
                File picFile = new File(picPath);
                if (picFile.exists() && picFile.canRead() && picFile.length() > 0) {
                    byte[] tmpPic = new byte[(int)picFile.length()];
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(picFile);
                        fis.read(tmpPic);
                        mBase64ImgData = Base64.encodebyte(tmpPic);
                        return;
                    } catch (IOException e) {
                        tmpPic = null;
                    } finally {
                        try {
                            if (fis != null) {
                                fis.close();
                            }
                        } catch (Exception e) {}
                    }
                    
                }
            }
        } catch (SecurityException e) {}

        if (thumbData != null && thumbData.length > 0) {
            mBase64ImgData = Base64.encodebyte(thumbData);
        }
    }
    
    public void onCreateRequestParamBundle(Bundle data) {
        if (mBaseRequest != null) {
            mBaseRequest.toBundle(data);
        }
        
        if (!TextUtils.isEmpty(mAppPackage)) {
            mHashKey = MD5.hexdigest(Utility.getSign(mContext, mAppPackage));
        }
        data.putString(REQ_PARAM_TOKEN, mToken);
        data.putString(REQ_PARAM_SOURCE, mAppKey);
        data.putString(REQ_PARAM_PACKAGENAME, mAppPackage);
        data.putString(REQ_PARAM_KEY_HASH, mHashKey);
        
        //data.putInt(WBConstants.Base.SDK_VER, WBConstants.WEIBO_SDK_VERSION_CODE);
        data.putString(WBConstants.Base.APP_PKG, mAppPackage);
        data.putString(WBConstants.Base.APP_KEY, mAppKey);
        data.putInt(WBConstants.SDK.FLAG, WBConstants.WEIBO_FLAG_SDK);
        data.putString(WBConstants.SIGN, mHashKey);
        
        if (mAuthListener != null) {
            WeiboCallbackManager manager = WeiboCallbackManager
                    .getInstance(mContext);
            mAuthListenerKey = manager.genCallbackKey();
            manager.setWeiboAuthListener(mAuthListenerKey, mAuthListener);
            data.putString(AuthRequestParam.EXTRA_KEY_LISTENER, mAuthListenerKey);
        }
    }
    
    public void execRequest(Activity act, int action) {
        if (action == EXEC_REQUEST_ACTION_CANCEL) {
            sendSdkCancleResponse(act);
            WeiboSdkBrowser.closeBrowser(act, mAuthListenerKey, null);
        }
    }

    public boolean hasImage() {
        if (mBase64ImgData != null && mBase64ImgData.length > 0) {
            return true;
        }
        return false;
    }
    
    public WeiboParameters buildUploadPicParam(WeiboParameters param) {
        if (!hasImage()) {
            return param;
        }
        String imgDataBase64Str = new String(mBase64ImgData);
        param.put(REQ_UPLOAD_PIC_PARAM_IMG, imgDataBase64Str);
        return param;
    }
    
    public String buildUrl(String picid) {
        StringBuilder url = new StringBuilder(SHARE_URL);
        
        url.append("?").append(REQ_PARAM_TITLE).append("=").append(mShareContent);
        
        url.append("&").append(REQ_PARAM_VERSION).append("=").append(WBConstants.WEIBO_SDK_VERSION_CODE);
        
        if (!TextUtils.isEmpty(mAppKey)) {
            url.append("&").append(REQ_PARAM_SOURCE).append("=").append(mAppKey);
        }
        if (!TextUtils.isEmpty(mToken)) {
            url.append("&").append(REQ_PARAM_TOKEN).append("=").append(mToken);
        }
        String aid = Utility.getAid(mContext, mAppKey);
        if (!TextUtils.isEmpty(aid)) {
            url.append("&").append(REQ_PARAM_AID).append("=").append(aid);
        }
        if (!TextUtils.isEmpty(mAppPackage)) {
            url.append("&").append(REQ_PARAM_PACKAGENAME).append("=").append(mAppPackage);
        }
        if (!TextUtils.isEmpty(mHashKey)) {
            url.append("&").append(REQ_PARAM_KEY_HASH).append("=").append(mHashKey);
        }
        if (!TextUtils.isEmpty(picid)) {
            url.append("&").append(REQ_PARAM_PICINFO).append("=").append(picid);
        }
        return url.toString();
    }
    
    private void sendSdkResponse( Activity activity, int errCode, String errMsg ) {
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Intent intent = new Intent(WBConstants.ACTIVITY_REQ_SDK);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setPackage(bundle.getString(WBConstants.Base.APP_PKG));

        intent.putExtras(bundle);
        intent.putExtra(WBConstants.Base.APP_PKG, activity.getPackageName());
        intent.putExtra(WBConstants.Response.ERRCODE, errCode);
        intent.putExtra(WBConstants.Response.ERRMSG, errMsg);
        try {
            activity.startActivityForResult(intent, WBConstants.SDK_ACTIVITY_FOR_RESULT_CODE);
        } catch (ActivityNotFoundException e) {
        }
    }
    
    public void sendSdkCancleResponse( Activity activity ) {
        sendSdkResponse(activity, WBConstants.ErrorCode.ERR_CANCEL, "send cancel!!!");
    }

    public void sendSdkOkResponse( Activity activity ) {
        sendSdkResponse(activity, WBConstants.ErrorCode.ERR_OK, "send ok!!!");
    }
    
    public void sendSdkErrorResponse( Activity activity, String msg ) {
        sendSdkResponse(activity, WBConstants.ErrorCode.ERR_FAIL, msg);
    }
    
    public void setBaseRequest(BaseRequest request) {
        this.mBaseRequest = request;
    }
    
    public String getAppPackage() {
        return mAppPackage;
    }

    public void setAppPackage( String mAppPackage ) {
        this.mAppPackage = mAppPackage;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken( String mToken ) {
        this.mToken = mToken;
    }

    public String getAppKey() {
        return mAppKey;
    }

    public void setAppKey( String mAppKey ) {
        this.mAppKey = mAppKey;
    }

    public String getHashKey() {
        return this.mHashKey;
    }
    
    public String getShareContent() {
        return this.mShareContent;
    }
    
    public byte[] getBase64ImgData() {
        return this.mBase64ImgData;
    }
    
    public WeiboAuthListener getAuthListener() {
        return mAuthListener;
    }
    
    public String getAuthListenerKey() {
        return mAuthListenerKey;
    }
    
    public void setAuthListener( WeiboAuthListener mAuthListener ) {
        this.mAuthListener = mAuthListener;
    }
    
    public static class UploadPicResult {
        private int code = ~RESP_UPLOAD_PIC_SUCC_CODE;
        private String picId; 
        
        private UploadPicResult() {}
        
        public int getCode() {
            return this.code;
        }
        
        public String getPicId() {
            return this.picId;
        }
        
        public static UploadPicResult parse(String resp) {
            if (TextUtils.isEmpty(resp)) {
                return null;
            }
            UploadPicResult result = new UploadPicResult();
            try {
                JSONObject obj = new JSONObject(resp);
                result.code = obj.optInt(RESP_UPLOAD_PIC_PARAM_CODE, 
                        ~RESP_UPLOAD_PIC_SUCC_CODE);
                result.picId = obj.optString(RESP_UPLOAD_PIC_PARAM_DATA, "");
            } catch (JSONException e) {
            }
            return result;
        }
    }
    
}
