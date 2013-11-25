# ReadMe
为了方便第三方开发者快速集成微博 SDK，我们提供了以下联系方式，协助开发者进行集成：  
**QQ群：284084420**  
**邮箱：sdk4wb@sina.cn**  
**微博：移动新技术**  
虽然我们提供了若干文档，但总有不尽人意的地方，为了快速上手，少走弯路，我们建议您采用以下方式来了解并集成微博SDK。  

* Step 1：浏览 ReadMe 了解大致情况
* Step 2：运行示例程序 [WeiboSDKDemo.apk][4] 或 [Demo][5] 了解 SDK 提供的所有功能
* Step 3：查看 [微博Android平台SDK文档V2.4.0.pdf][1] 深入了解如何使用
* Step 4：参照 [Demo][5] 进行开发  

**如果您在使用过程中有些问题不清楚如何解决**，请先仔细阅读：[常见问题FAQ][2]，尝试能否找到对应的答案。  
**如果您对 SDK 中提供的某个类、API 不明确**，请在 [WeiboSDK_API-V2.4.0.CHM][3] 中查找对应的注解。  

另外，关于 SDK 的 Bug 反馈、用户体验，以及建议与不足等，请大家尽量提交到 Github 上，充分利用好 Github 这一工具。
SDK，虽然有很多不足之处，但我们力争为第三方开发者提供一个规范、简单易用、稳定可靠、可扩展、可定制的 SDK，敬请期待。

------

# Release-Note: Android SDK V2.4.0  
## 重大版本变更：

1. 重构授权、分享等模块代码
2. 规范化代码、注释、文档等，提高可读性
3. 开源 OpenAPI 部分代码
4. 提供一键登录、登录/注销控件
5. 提供 FAQ 文档
6. 修正若干 Bug

注：本次版本升级，本着为开发者提供更一个规范、简单、易用、可靠 SDK 的原则，虽然变动较多，但这种学习成本是值得的。在下一个版本，我们将会开发者提供更加实用的功能以及开放接口。  
### 以下列举了一些变动：
* 包名变化：统一包名，以 com.sina.weibo.sdk 开头  
* 接口名称变化：  
授权：Weibo --> WeiboAuth  
分享：IWeiboAPI --> IWeiboShareAPI

------

# 快速上手

## 概述
微博 Android 平台 SDK 为第三方应用提供了简单易用的微博API调用服务，使第三方客户端无需了解复杂的验证机制即可进行授权登陆，并提供微博分享功能，可直接通过微博官方客户端分享微博。
>本文档详细内容请查阅：[微博Android平台SDK文档V2.4.0.pdf][1]

------

# 名词解释
| 名词        | 注解    | 
| --------    | :-----  | 
| AppKey      | 分配给每个第三方应用的 app key。用于鉴权身份，显示来源等功能。|
| RedirectURI | 第三方应用授权回调页面。授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，但是没有定义将无法使用SDK认证登录。建议使用默认回调页https://api.weibo.com/oauth2/default.html （可以在新浪微博开放平台->我的应用->应用信息->高级应用->授权设置->应用回调页中找到）。|
| Scope | Scope是OAuth2.0新版授权页提供的一个功能，通过scope，平台将开放更多的微博核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新OAuth2.0授权页中有权利选择赋予应用的功能。| 
| AccessToken | 表示用户身份的 token，用于微博 API 的调用。| 
| Expire in   | 过期时间，用于判断登录是否过期。| 
| Oauth2.0 Web 授权   | 通过WebView进行授权，并返回Token信息。| 
| SSO 授权   | 通过唤起微博客户端进行授权，并返回Token信息。| 


------

## 功能列表
### 1. 认证授权
目前微博SDK为开发者提供 Oauth2.0 Web 授权认证，并集成 SSO 登录功能。使用带SSO功能的SDK进行登录，只需调用登录接口，并完成回调方法对接收登录结果即可。  
**注意：SSO 需要微博客户端才能够进行。**

### 2. 微博分享
通过微博SDK，第三方应用能够分享文字、图片、视频、音乐等内容，目前分享有两种方式：    
 
* 通过第三方应用唤起微博客户端进行分享（该分享方式为第三方客户端通常的使用方式）    
* 通过微博客户端唤起第三方应用进行分享（该分享方式需要合作接入，详情请查看：http://t.cn/aex4JF）    

**注意：目前SDK只支持通过微博官方客户端进行分享。**

### 3. 登录/注销按钮
微博SDK目前提供了两类登录按钮：一种是一键登陆按钮，一种是登陆/注销按钮。  
两者实际上都是调用了SSO登录接口。另外，不再单独提供注销按钮，用户可以调用已封装好的OpenAPI接口直接注销。  

### 4. 开放接口
微博SDK目前提供了一个简单的OpenAPI接口调用框架，并封装了一些简单的开放接口，以供大家参考：  

* 注销接口：授权回收接口，帮助开发者主动取消用户的授权。  
* 邀请接口：提供好友邀请接口，支持登录用户向自己的微博互粉好友发送私信邀请、礼物。  

**注意**：该模块目前还需要进一步设计和完善，后续将会提供一个更加合理好用的OpenAPI框架，并封装更多常用的接口（如利用OpenAPI获取用户信息，分享微博等），敬请期待。

------

## 运行示例代码
为了方便第三方应用更快的集成微博 SDK，更清晰的了解目前微博 SDK 所提供的功能，我们在 GitHub 上提供了一个简单的 **示例工程** 以及对应的 **APK安装包** 。  
**方式一：**通过 adb install 命令直接安装 WeiboSDKDemo.apk   
**方式二：**在 Eclipse 中导入并运行 WeiboSDKDemo 工程（详情请查看[微博Android平台SDK文档V2.4.0.pdf][1]中：**运行示例代码**）  
***注意：通过方式二运行工程时，请务必替换默认的 debug.keystore文件，否则无法正确的授权成功。另外，该debug.keysotre 是新浪官方的，除了编译运行官方 DEMO 外，请不要直接使用它，出于安全的考虑，您应该为自己的应用提供一份 keysotre。***  
>在C:\Users\XXXXX\\.android目录下，把Android默认的debug.keystore替换成官方在GitHub上提供的debug.keystore。

## 微博SDK及DEMO工程目录结构及分析
微博SDK目前以是**部分开源**的形式提供给第三方开发者的，简单来说，可以分为以下三部分：  

* **闭源部分**：weibosdkcore.jar，该JAR包集成了微博授权、SSO登录以及分享等核心功能。
* **开源部分**：WeiboSDK工程（Library），**该工程引用了weibosdkcore.jar**，这里面主要是对OpenAPI进行了简单的封装，第三方可以参考使用流程，模仿并添加自己需要的接口，利用OpenAPI接口获取用户信息，分享微博等，另外，还提供了一键登录/注销功能。
* **Demo部分**：WeiboSDKDemo工程，**该工程引用了WeiboSDK工程**，提供了目前微博所支持的所有功能的示例代码。  

**请注意**：第三方在使用时，如果只想要授权和分享功能，可直接使用weibosdkcore.jar；如果想使用其它功能，可直接导入WeiboSDK工程。如何导入WeiboSDK工程请详见：[微博Android平台SDK文档V2.4.0.pdf][1]中：**选择您要集成的方式**

------

## 集成前准备
### 1. 申请应用程序的APP_KEY
在这一步中，您需要在微博开放平台上，对您的应用进行注册，并获取APP_KEY，添加应用的授权回调页（Redirect URI）。详情请仔细阅读：移动客户端接入（http://t.cn/aex4JF ）

### 2. 注册应用程序的包名和签名
您需要在微博开放平台上注册应用程序的包名和签名后，才能正确进行授权。  
请注意：包名和签名未注册，或者签名注册不正确，都会导致无法授权。  
应用程序包名：指AndroidManifest.xml文件中，package标签所代表的内容。  
应用程序签名：该签名是通过官方提供的签名工具生成的MD5值。  
详情请查看：[微博Android平台SDK文档V2.4.0.pdf][1] 中：**如何使用签名工具获取您应用的签名？**  

### 3. 选择您要集成的方式
在集成微博SDK前，您有两种可选的方式来集成微博SDK：

* 直接导入weibosdkcore.jar：适用于只需要微博授权及分享的项目
* 引用WeiboSDK工程（Library）：适用于微博授权、分享，以及需要登陆按钮、调用OpenAPI的项目  
详情请查看：[微博Android平台SDK文档V2.4.0.pdf][1] 中：**选择您要集成的方式** 

### 4. 在您的应用中添加 SDK 所需要的权限
```java
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
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
     * 
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "http://www.sina.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的
     * 微博核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权
     * 页中有权利选择赋予应用的功能。
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接
     * 口的使用权限，高级权限需要进行申请。
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * 
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI 
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope 
     */
    public static final String SCOPE = 
            "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
}

```

### 2. 创建微博API接口类对象
```java
mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
```
其中：APP_KEY、 REDIRECT_URL、 SCOPE需要替换成第三方应用申请的内容。

### 3. 实现WeiboAuthListener接口

```java
/**
 * 微博认证授权回调类。
 * 1.SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 
 *   后，该回调才会被执行。
 * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
 * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
 */
class AuthDialogListener implements WeiboAuthListener {
    
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
如果你不想使用SSO授权，直接调用以下函数：
```java
mWeiboAuth.anthorize(new AuthListener());
```
如果你使用SSO授权，需要调用以下函数：
```java
mSsoHandler = new SsoHandler(WBAuthActivity.this, mWeiboAuth);
mSsoHandler.authorize(new AuthListener());
```
并在Activity的onActivityResult函数中，调用以下方法：
```java
// SSO 授权回调
// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
if (mSsoHandler != null) {
    mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
}
```

## 其它功能
其它功能请相见文档：[微博Android平台SDK文档V2.4.0.pdf][1]

[1]:https://github.com/mobileresearch/weibo_android_sdk/blob/master/%E5%BE%AE%E5%8D%9AAndroid%E5%B9%B3%E5%8F%B0SDK%E6%96%87%E6%A1%A3V2.4.0.pdf
[2]:https://github.com/mobileresearch/weibo_android_sdk/blob/master/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98%20FAQ.md
[3]:https://github.com/mobileresearch/weibo_android_sdk/blob/master/WeiboSDK_API-V2.4.0.CHM
[4]:https://github.com/mobileresearch/weibo_android_sdk/blob/master/WeiboSDKDemo.apk
[5]:https://github.com/mobileresearch/weibo_android_sdk/tree/master/demo-src
