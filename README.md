# ReadMe
为了方便第三方开发者快速集成微博 SDK，我们提供了以下联系方式，协助开发者进行集成：  
**QQ群：284084420**  
**邮箱：sdk4wb@sina.cn**  
**微博：移动新技术**  
另外，关于SDK的Bug反馈、用户体验、以及好的建议，请大家尽量提交到 Github 上，我们会尽快解决。  
目前，我们正在逐步完善微博 SDK，争取为第三方开发者提供一个规范、简单易用、可靠、可扩展、可定制的 SDK，敬请期待。

# 概述
微博 Android 平台 SDK 为第三方应用提供了简单易用的微博API调用服务，使第三方客户端无需了解复杂的验证机制即可进行授权登陆，并提供微博分享功能，可直接通过微博官方客户端分享微博。
>本文档详细内容请查阅：[微博Android平台SDK文档V2.3.0.pdf](https://raw.github.com/mobileresearch/weibo_android_sdk/master/%E5%BE%AE%E5%8D%9AAndroid%E5%B9%B3%E5%8F%B0SDK%E6%96%87%E6%A1%A3V2.3.0.pdf)

# 名词解释
| 名词        | 注解    | 
| --------    | :-----  | 
| AppKey      | 分配给每个第三方应用的 app key。用于鉴权身份，显示来源等功能。|
| RedirectURI | 应用回调页面，可在新浪微博开放平台->我的应用->应用信息->高级应用->授权设置->应用回调页中找到。|
| AccessToken | 表示用户身份的 token，用于微博 API 的调用。| 
| Expire in   | 过期时间，用于判断登录是否过期。| 

# 功能列表
### 1. 认证授权
为开发者提供 Oauth2.0 授权认证，并集成 SSO 登录功能（注意：SSO 需要微博客户端才能够进行）。
### 2. 微博分享
从第三方应用分享信息到微博，目前只支持通过微博官方客户端进行分享。
### 3. 登入登出
微博登入按钮主要是简化用户进行 SSO 登陆，实际上，它内部是对 SSO 认证流程进行了简单的封装。  
微博登出按钮主要提供一键登出的功能，帮助开发者主动取消用户的授权。
### 4. 好友邀请
好友邀请接口，支持登录用户向自己的微博互粉好友发送私信邀请、礼物。

# 运行示例代码
为了方便第三方应用更快的集成微博 SDK，更清晰的了解目前微博 SDK 所提供的功能，我们在 GitHub 上提供了一个简单的 **示例工程** 以及对应的 **APK安装包** 。  
**方式一：**通过 adb install 命令直接安装 weibo.sdk.android.demo.apk  
**方式二：**在 Eclipse 中导入并运行 weibosdk_demo 工程  
***注意：通过方式二运行工程时，请务必替换默认的 debug.keystore文件，否则无法正确的授权成功。***  
>在C:\Users\XXXXX\\.android目录下，把Android默认的debug.keystore替换成官方在GitHub上提供的debug.keystore。

# 第三方如何使用（认证授权）
### 1. 导入SDK JAR包，添加权限
```java
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
### 2. 创建微博API接口类对象
```java
mWeibo = Weibo.getInstance(ConstantS.APP_KEY, ConstantS.REDIRECT_URL, ConstantS.SCOPE);
```
其中：APP_KEY、 REDIRECT_URL、 SCOPE需要替换成第三方应用申请的内容。
### 3. 注册应用程序的包名和签名
第三方应用程序 **包名** 和 **签名** 必须在新浪微博开放平台注册，***否则无法进行正确的授权***。
>可以在新浪微博开放平台-->我的应用-->应用信息-->应用基本信息处找到，点击编辑按钮即可注册。  
具体详见：[微博Android平台SDK文档V2.3.0.pdf](https://raw.github.com/mobileresearch/weibo_android_sdk/master/%E5%BE%AE%E5%8D%9AAndroid%E5%B9%B3%E5%8F%B0SDK%E6%96%87%E6%A1%A3V2.3.0.pdf) 中：**如何注册应用程序的包名和签名**

### 4. 实现WeiboAuthListener接口

```java
class AuthDialogListener implements WeiboAuthListener {
    
    @Override
    public void onComplete(Bundle values) {
        // 授权成功后会调用
        // TODO: 
        }
    }

    @Override
    public void onError(WeiboDialogError e) {
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onWeiboException(WeiboException e) {
    }
}
```
### 5. 调用authorize方法，认证并授权
如果你不想使用SSO授权，直接调用以下函数：
```java
mWeibo.anthorize(MainActivity.this, new AuthDialogListener());
```
如果你使用SSO授权，需要调用以下函数：
```java
mSsoHandler = new SsoHandler(MainActivity.this, mWeibo);
mSsoHandler.authorize(new AuthDialogListener(), null);
```
并在Activity的onActivityResult函数中，调用以下方法：
```java
// SSO 授权回调
// 重要：发起 SSO 登陆的Activity必须重写onActivityResult
if (mSsoHandler != null) {
    mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
}
```

# 其它功能
其它功能请相见文档：[微博Android平台SDK文档V2.3.0.pdf](https://raw.github.com/mobileresearch/weibo_android_sdk/master/%E5%BE%AE%E5%8D%9AAndroid%E5%B9%B3%E5%8F%B0SDK%E6%96%87%E6%A1%A3V2.3.0.pdf)

# 适用范围
使用此SDK需满足以下条件:  

- Android平台
- 在新浪微博开放平台注册并创建应用
- 已定义本应用的授权回调页  

注: 关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，但是没有定义将无法使用SDK认证登录。
