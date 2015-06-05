# ReadMe

资料的下载入口在本网页的右侧,有一个 download-ZIP 按钮,即可下载到本地。↗↗

为了方便第三方开发者快速集成微博 SDK，我们提供了以下联系方式，协助开发者进行集成：  
**QQ群：248982250**  
**QQ群：284084420**  
**邮箱：sdk4wb@sina.cn**  
**微博：移动新技术**  
虽然我们提供了若干文档，但总有不尽人意的地方，为了快速上手，少走弯路，我们建议您采用以下方式来了解并集成微博SDK。  

* Step 1：浏览 ReadMe 了解大致情况
* Step 2：运行示例程序 [WeiboSDKDemo.apk][4] 或 [Demo][5] 了解 SDK 提供的所有功能
* Step 3：查看 [微博Android平台SDK文档V3.1.1.pdf][1] 深入了解如何使用
* Step 4：参照 [Demo][5] 进行开发  

**如果您在使用过程中有些问题不清楚如何解决**，请先仔细阅读：[常见问题FAQ][2]，尝试能否找到对应的答案。  
**如果您对 SDK 中提供的某个类、API 不明确**，请在 [API文档说明][8] 中查找对应的注解。  

另外，关于 SDK 的 Bug 反馈、用户体验，以及建议与不足等，请大家尽量提交到 Github 上，充分利用好 Github 这一工具。
目前 SDK 有很多不足之处，请给我们一些时间，我们会力争为第三方开发者提供一个规范、简单易用、稳定可靠、可扩展、可定制的 SDK。

------

# Release-Note: Android SDK V3.1.1  
## 版本变更：

1. 优化网页授权
2. 优化网页分享
3. 增加社会化评论组件
4. 增加社会化关注组件
5. 增加微博支付功能接口
6. 增加微博短信授权方式

------

# 快速上手

## 概述
微博 Android 平台 SDK 为第三方应用提供了简单易用的微博API调用服务，使第三方客户端无需了解复杂的验证机制即可进行授权登陆，并提供微博分享功能，可直接通过微博官方客户端分享微博。
>本文档详细内容请查阅：[微博Android平台SDK文档V3.1.1.pdf][1]

------

# 名词解释
| 名词        | 注解    | 
| --------    | :-----  | 
| AppKey      | 分配给每个第三方应用的 app key。用于鉴权身份，显示来源等功能。|
| RedirectURI | 第三方应用授权回调页面。建议使用默认回调页`https://api.weibo.com/oauth2/default.html` ，可以在新浪微博开放平台->我的应用->应用信息->高级应用->授权设置->应用回调页中找到。|
| Scope       | 通过scope，平台将开放更多的微博核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新OAuth2.0授权页中有权利选择赋予应用的功能。| 
| AccessToken | 表示用户身份的 token，用于微博 API 的调用。| 
| Web 授权    | 通过WebView进行授权，并返回Token信息。| 
| SSO 授权    | 通过唤起微博客户端进行授权，并返回Token信息。| 

------

## 功能列表
### 1. 认证授权
 - SSO 授权：在**有客户端**的情况下，使用 SSO 授权登陆；无客户端的情况下，自动唤起 Web 授权
 - Web 授权：在**没有客户端**的情况下，可直接使用该授权
 - SSO 授权+Web 授权 混合授权，（**推荐使用**）  ( 如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权 )   详情请查看Demo中`WBAuthActivity`中说明

### 2. 微博分享
通过微博SDK，第三方应用能够分享文字、图片、视频、音乐等内容，目前分享有三种方式：    
**有微博客户端情况**  
* 通过第三方应用唤起微博客户端进行分享（该分享方式为第三方客户端通常的使用方式）    
* 通过微博客户端唤起第三方应用进行分享（该分享方式需要合作接入，详情请查看：http://t.cn/aex4JF）    

**无微博客户端情况**  
* 通过`OpenAPI`进行分享，直接使用`StatusesAPI`类中的`upload`、`update`或`uploadUrlText`函数进行分享即可，或直接使用`AsyncWeiboRunner#requestAsync`方法，自己进行HTTP请求实现分享，详见：[使用异步接口来发送一条带图片的微博][7]。

### 3. 登录/注销按钮
微博SDK目前提供了两类登录按钮：一种是一键登陆按钮，一种是登陆/注销按钮。两者都是调用了SSO登录。

### 4. 开放接口
微博SDK目前提供了一个简单的OpenAPI接口调用框架，并封装了一些简单的开放接口，以供大家参考：  
微博OpenAPI接口地址：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI

| API类       | 说明    | 
| --------    | :-----  | 
| UsersAPI    | 用于获取用户的信息。|
| StatusesAPI | 用于发送微博、获取微博等。|
| CommentsAPI | 用于获取评论。|
| LogoutAPI   | 用于回收用户的授权。|
| InviteAPI   | 提供好友邀请功能，支持登录用户向自己的微博互粉好友发送私信邀请、礼物。|


**注意**：目前我们只提供了一部分接口，主要原因是API过于庞大，对每一个API做测试也是一件非常耗时的事情，因此，我们优先开放第三方在使用时，最关心的几个API（如上表格），其它API继续放在`com.sina.weibo.sdk.openapi.legacy`下。
实际上，我们将这些API没有全部开放出来，也是希望第三方开发者能够和我们一起维护这些API，将修改后的代码Push到Github上。  
另外，网络模块已重构了一个易用点的框架，第三方开发者可直接通过 Http 请求来调用 OpenAPI 接口，详见：[网络请求框架的使用][6]。  

------
## API文档说明
http://sinaweibosdk.github.io/weibo_android_sdk/doc


------


## 运行示例代码
为了方便第三方应用更快的集成微博 SDK，更清晰的了解目前微博 SDK 所提供的功能，我们在 GitHub 上提供了一个简单的 **示例工程** 以及对应的 **APK安装包** 。  
**方式一：**通过 adb install 命令直接安装 WeiboSDKDemo.apk   
**方式二：**在 Eclipse 中导入并运行 WeiboSDKDemo 工程（详情请查看[微博Android平台SDK文档3.1.1.pdf][1]中：**运行示例代码**）  
***注意：通过方式二运行工程时，请务必替换默认的 debug.keystore文件，否则无法正确的授权成功。另外，该debug.keysotre 是新浪官方的，除了编译运行官方 DEMO 外，请不要直接使用它，出于安全的考虑，您应该为自己的应用提供一份 keysotre。***  
>在C:\Users\XXXXX\\.android目录下，把Android默认的debug.keystore替换成官方在GitHub上提供的debug.keystore。

## 微博SDK及DEMO工程目录结构及分析
微博SDK目前以是**部分开源**的形式提供给第三方开发者的，简单来说，可以分为以下三部分：  

* **闭源部分**：weibosdkcore.jar，该JAR包集成了微博授权、SSO登录以及分享等核心功能。**另外，在V2.5.0以后的版本中，我们将网络模块框架也加入其中，方便开发者进行OpenAPI的网络请求**。
* **开源部分**：WeiboSDK工程（Library），**该工程引用了weibosdkcore.jar**，这里面主要是对OpenAPI进行了简单的封装，第三方可以参考使用流程，模仿并添加自己需要的接口，利用OpenAPI接口获取用户信息，分享微博等，另外，还提供了一键登录/注销功能。
* **Demo部分**：WeiboSDKDemo工程，**该工程引用了WeiboSDK工程**，提供了目前微博所支持的所有功能的示例代码。  

------

## 集成前准备
### 1. 申请应用程序的APP_KEY
在这一步中，您需要在微博开放平台上，对您的应用进行注册，并获取APP_KEY，添加应用的授权回调页（Redirect URI）。详情请仔细阅读：移动客户端接入（http://t.cn/aex4JF ）

### 2. 注册应用程序的包名和签名
您需要在微博开放平台上注册应用程序的包名和签名后，才能正确进行授权。  
请注意：包名和签名未注册，或者签名注册不正确，都会导致无法授权。  
应用程序包名：指`AndroidManifest.xml`文件中，`package`标签所代表的内容。  
应用程序签名：该签名是通过官方提供的签名工具生成的MD5值。  
详情请查看：[微博Android平台SDK文档V3.1.1.pdf][1] 中：**如何使用签名工具获取您应用的签名？**  

### 3. 选择您要集成的方式
在集成微博SDK前，您有两种可选的方式来集成微博SDK：

* 1直接导入weibosdkcore.jar：适用于只需要授权、分享、网络请求框架功能的项目
* 1.1**导入libs目录下三个文件夹包中的so文件，直接按照对应目录拷贝即可**   **非常重要！！**

* 2 直接引用WeiboSDK工程（Library）：适用于授权、分享，以及需要登陆按钮、调用OpenAPI的项目  

详情请查看：[微博Android平台SDK文档3.1.1.pdf][1] 中：**选择您要集成的方式** 

### 4. 在您的应用中添加 SDK 所需要的权限
```java
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

### 5. 在您的应用中注册 SDK 所需要的Activity,Service
```java
<activity android:name="com.sina.weibo.sdk.component.WeiboSdkBrowser" 
    android:configChanges="keyboardHidden|orientation"
    android:windowSoftInputMode="adjustResize"
    android:exported="false" >
</activity>
<service android:name="com.sina.weibo.sdk.net.DownloadService"
    android:exported="false">
</service>
```

------

## 第三方如何使用（认证授权）

### 1. 替换成自己应用的 APP_KEY 等参数
鉴于目前有很多第三方开发直接拷贝并使用Demo中的Constants类，因此，有必要说明，第三方开发者需要将Constants类中的各种参数替换成自己应用的参数，请仔细阅读代码注释。
```java
public interface Constants {
    /** 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
    public static final String APP_KEY      = "2045436852";

    /** 
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static final String REDIRECT_URL = "http://www.sina.com";

    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
}

```

### 2. 创建微博API接口类对象
```java
mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
```
其中：APP_KEY、 REDIRECT_URL、 SCOPE需要替换成第三方应用申请的内容。

### 3. 实现WeiboAuthListener接口

```java
class AuthListener  implements WeiboAuthListener {
    
    @Override
    public void onComplete(Bundle values) {
        // 从 Bundle 中解析 Token
        mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        if (mAccessToken.isSessionValid()) {
            // 保存 Token 到 SharedPreferences
            AccessTokenKeeper.writeAccessToken(WBAuthActivity.this, mAccessToken);
            .........
        } else {
        // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
            String code = values.getString("code", "");
            .........
        }
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onWeiboException(WeiboException e) {
    }
}
```
### 4. 调用方法，认证授权
* 1  Web 授权，直接调用以下函数：*
```java
mSsoHandler = new SsoHandler(WBAuthActivity.this, mAuthInfo);
mSsoHandler.authorizeWeb(new AuthListener());
```
* 2 SSO授权，需要调用以下函数：*
```java
mSsoHandler = new SsoHandler(WBAuthActivity.this, mAuthInfo);
mSsoHandler. authorizeClientSso(new AuthListener());
```

* 3 all In one方式授权，需要调用以下函数：*
```java
mSsoHandler = new SsoHandler(WBAuthActivity.this, mAuthInfo);
mSsoHandler. authorize(new AuthListener());
```
* 注：此种授权方式会根据手机是否安装微博客户端来决定使用sso授权还是网页授权，如果安装有微博客户端 则调用微博客户端授权，否则调用Web页面方式授权  参见pdf文档说明 *

以上三种授权需要在Activity的`onActivityResult`函数中，调用以下方法：
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (mSsoHandler != null) {
        mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
    }
}

```

## 网络请求框架的使用
在V2.5.0以后的版本，我们重构了网络模块，提供了同步和异步的网络请求接口。  
**异步接口：**`AsyncWeiboRunner#requestAsync(String, WeiboParameters, String, RequestListener)`  
**同步接口：**`AsyncWeiboRunner#request(String, WeiboParameters, String)`  
其中，同步接口用于开发者有自己的异步请求机制。
### 举例：使用异步接口来发送一条带图片的微博
```java
// 1. 获取要发送的图片
Drawable drawable = getResources().getDrawable(R.drawable.ic_com_sina_weibo_sdk_logo);
Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

// 2. 调用接口发送微博
WeiboParameters params = new WeiboParameters();
params.put("access_token", mAccessToken.getToken());
params.put("status",       "通过API发送微博-upload");
params.put("visible",      "0");
params.put("list_id",      "");
params.put("pic",          bitmap);
params.put("lat",          "14.5");
params.put("long",         "23.0");
params.put("annotations",  "");

AsyncWeiboRunner.requestAsync(
        "https://api.weibo.com/2/statuses/upload.json", 
        params, 
        "POST", 
        mListener);
```
上述代码中，请开发者自行补充`mAccessToken`和`mListener`

## 其它功能
其它功能请相见文档：[微博Android平台SDK文档V3.1.1.pdf][1]

[1]:https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/%E5%BE%AE%E5%8D%9AAndroid%E5%B9%B3%E5%8F%B0SDK%E6%96%87%E6%A1%A3V3.1.1.pdf
[2]:https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98%20FAQ.md
[3]:https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/WeiboSDK_API-V2.4.0.CHM
[4]:https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/WeiboSDKDemo_v3.1.1.apk
[5]:https://github.com/sinaweibosdk/weibo_android_sdk/tree/master/demo-src
[6]:https://github.com/sinaweibosdk/weibo_android_sdk/edit/master/README.md#%E7%BD%91%E7%BB%9C%E8%AF%B7%E6%B1%82%E6%A1%86%E6%9E%B6%E7%9A%84%E4%BD%BF%E7%94%A8
[7]:https://github.com/sinaweibosdk/weibo_android_sdk/edit/master/README.md#%E4%B8%BE%E4%BE%8B%E4%BD%BF%E7%94%A8%E5%BC%82%E6%AD%A5%E6%8E%A5%E5%8F%A3%E6%9D%A5%E5%8F%91%E9%80%81%E4%B8%80%E6%9D%A1%E5%B8%A6%E5%9B%BE%E7%89%87%E7%9A%84%E5%BE%AE%E5%8D%9A
[8]:http://sinaweibosdk.github.io/weibo_android_sdk/doc/
