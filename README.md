# ReadMe

------
v10.9.0

1. 修复可能存在的安全问题。

v10.8.0

1. 修复bug.


v10.7.0

1. 20年第一个版本,在v9.12.0基础上的升级。
2. 主要解决9.12.0版本中的bug.
3。v4.x.x版本系列之后不再维护，推荐升级。具体接入方式2019SDK目录下有文档。
4. 接入中遇到问题及时联系我解决.(QQ：879073159)


------

2019年末，微博SDK版本v9.12.0全新更新：

1.全新的SDK接入文档，解决同学们接入时候没有详细文档的痛点。

2.全新的SDK API设计。

3.全新的SDK Demo，实现傻瓜式接入。

4.aar包瘦身,删除无用代码。

注意点：

1.新版本API有变动，如需升级到最新版本，需要看着文档一步一步来升级。

2.新版本位置：2019SDK。

3.新版本接入遇到问题，请及时联系我，我看到会及时回复大家。（QQ：879073159）

------

资料的下载入口在本网页的右侧,有一个 download-ZIP 按钮,即可下载到本地。↗↗

为了方便第三方开发者快速集成微博 SDK，我们提供了以下联系方式，协助开发者进行集成：  
**QQ群：248982250**  
**QQ群：284084420**  
**QQ群：109094998**
**邮箱：sdk4wb@sina.cn**  
**微博：移动新技术**  
虽然我们提供了若干文档，但总有不尽人意的地方，为了快速上手，少走弯路，我们建议您采用以下方式来了解并集成微博SDK。  

* Step 1：浏览 ReadMe 了解大致情况
* Step 2：运行示例程序 [WeiboSDKDemo.apk][4] 或 [Demo][5] 了解 SDK 提供的所有功能
* Step 3：查看 [微博Android平台SDK文档V4.1.pdf][1] 深入了解如何使用
* Step 4：参照 [Demo][5] 进行开发  

**如果您在使用过程中有些问题不清楚如何解决**，请先仔细阅读：[常见问题FAQ][2]，尝试能否找到对应的答案。   

另外，关于 SDK 的 Bug 反馈、用户体验，以及建议与不足等，请大家尽量提交到 Github 上，充分利用好 Github 这一工具。
目前 SDK 有很多不足之处，请给我们一些时间，我们会力争为第三方开发者提供一个规范、简单易用、稳定可靠、可扩展、可定制的 SDK。

------

# Release-Note: Android SDK V4.1 
## 版本变更：

v4.4.4

1.删除异常打印堆栈信息。（接入新版本如果没有包名报备（加入白名单），请联系QQ（879073159））


v4.4.3

1.解决空指针异常。（接入新版本如果没有包名报备（加入白名单），请联系QQ（879073159））


v4.4.2

1.修复恶意启动可能造成的crash.（接入新版本如果没有包名报备（加入白名单），请联系QQ（879073159））


v4.4.1

1.默认支持armeabi，arm64-v8a,armeabi-v7a架构。

2.修改无法获取到权限时可能造成的异常。（接入新版本如果没有包名报备（加入白名单），请联系QQ（879073159））    


v4.4.0

1.默认支持armeabi，arm64-v8a两种架构。

2.修复分享时横竖屏切换导致的异常。

3.修复其他若干bug（接入新版本如果没有包名报备（加入白名单），请联系QQ（879073159））

4.4.3.8已经报备过的，之后不用报备。

v4.3.9

1.修复分享遇到的bug若干。（接入新版本如果没有包名报备（加入白名单），请联系QQ（879073159））

v4.3.8

1.修复若干bug
2.默认只支持armeabi架构
3.使用4.3.8时，请联系客服进行包名报备。联系方式QQ（879073159）

v4.3.7

1.修复bug

2.增加64位的so

v4.3.6

1.修复bug

v4.3.5

1.修复Android4.4.4以下版本分享后没有回调.

v4.3.4

1.修复bug

v4.3.1
1.修复bug
2.关于分享之后的回调，4.3.0开始用如下方法接受:

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        shareHandler.doResultIntent(data,this);
    }

v4.3.0
1.修复bug

v4.2.7
1.支持微博极速版授权登录

v4.1
1.修复了一些已知的bug
2.支持多图分享和视频分享
3.支持分享视频到微博故事

v4.0
1.新的授权和分享逻辑，使你的接入更明确
2.修改了一些已知的问题

v3.2
1. 新的网页授权和分享
3. 移除open api相关文档，如果你还想继续使用open api请参考旧版本目录 [旧版本相关/demo-src]接入放自己维护
4. 移除LinkCard模式分享，当前只支持文字和图片混合（我们不建议你继续使用老的sdk接入微博进行LinkCard分享，微博客户端可能会在未来取消该功能，对你的程序可能会造成异常）
5. 移除微博支付功能
6. 精简sdk,解决已知的bug

------

# 快速上手

## 概述
微博 Android 平台 SDK 为第三方应用提供了简单易用的微博API调用服务，使第三方客户端无需了解复杂的验证机制即可进行授权登陆，并提供微博分享功能，可直接通过微博官方客户端分享微博。
>本文档详细内容请查阅：[微博Android平台SDK文档V4.1.pdf][1]

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
使用微博sdk授权和分享时，请确保你的AppKey、RedirectURI、Scope、PackageName和开发者官网中填写的一致.

------

## 功能列表
### 1. 认证授权
 - SSO 授权：在**有客户端**的情况下，使用 SSO 授权登陆；无客户端的情况下，自动唤起 Web 授权
 - Web 授权：在**没有客户端**的情况下，可直接使用该授权
 - SSO 授权+Web 授权 混合授权，（**推荐使用**）  ( 如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权 )   详情请查看Demo中`WBAuthActivity`中说明
 # 特别说明：新版本的sdk已经移除了openapi功能（包括获取用户信息等方法），如果你想再授权后获取用户信息，请参考open api接口文档 [微博开放平台api][10]，使用自己的网络引擎请求数据


### 2. 微博分享
通过微博SDK，第三方应用能够分享文字、图片、视频：    
**有微博客户端情况**  
* 通过第三方应用唤起微博客户端进行分享（该分享方式为第三方客户端通常的使用方式）    
**无微博客户端情况**  

OpenApi openApi已经不在微博sdk中维护，如果你想使用OpenApi,请参考开发者网站使用自己的网络接入
* 通过`OpenAPI`进行分享，如果你要接入openAPI 进行分享，请参考开放平台接口[微博开放平台api][9]。


------


## 运行示例代码
为了方便第三方应用更快的集成微博 SDK，更清晰的了解目前微博 SDK 所提供的功能，我们在 GitHub 上提供了一个简单的 **示例工程** 以及对应的 **APK安装包** 。  
**方式一：**通过 adb install 命令直接安装 app_debug.apk   (在新文档文件夹下面)
**方式二：**在 Android Studio 中导入并运行 weibosdkdemo 工程（详情请查看[微博Android平台SDK文档4.1.pdf][1]中：**运行示例代码**）  


## 微博SDK及DEMO工程目录结构及分析
微博SDK目前以是**部分开源**的形式提供给第三方开发者的，简单来说，可以分为以下三部分：  

------

## 集成前准备
### 1. 申请应用程序的APP_KEY
在这一步中，您需要在微博开放平台上，对您的应用进行注册，并获取APP_KEY，添加应用的授权回调页（Redirect URI）。详情请仔细阅读：移动客户端接入（http://t.cn/aex4JF ）

### 2. 注册应用程序的包名和签名
您需要在微博开放平台上注册应用程序的包名和签名后，才能正确进行授权。  
请注意：包名和签名未注册，或者签名注册不正确，都会导致无法授权。  
应用程序包名：指`AndroidManifest.xml`文件中，`package`标签所代表的内容。  
应用程序签名：该签名是通过官方提供的签名工具生成的MD5值。  
详情请查看：[微博Android平台SDK文档V4.0pdf][1] 中：**如何使用签名工具获取您应用的签名？**  

### 3. 集成sdk 
1:Android Studio接入
    在你工程的主模块下面修改build.gradle文件，添加微博sdk的依赖
```java
allprojects {
    repositories {
        jcenter()
        mavenCentral()
        maven { url "https://dl.bintray.com/thelasterstar/maven/" }
    }
}
compile 'com.sina.weibo.sdk:core:4.4.3:openDefaultRelease@aar'
```
或者将新文档目录下的openDefault-4.4.1.aar复制到工程libs目录下，修改build.gradle文件如下：
```java
repositories{
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:24.2.1'
    compile(name: 'openDefault-4.4.3', ext: 'aar')

}

```
2:Eclipse:

  复制github项目下【eclipse集成】目录下的文件到你的工程中

3:关于so

  微博sdk aar中默认直提供了[armeabi] [armeabi-v7a] [arm64-v8a]三个平台的so，如果你需要适配更多版本的so，请到github->so目录中获取全部平台的so文件

  如果你只想引入特别的平台在gradle 中配置如下
  
```java
  splits {
        abi {
            enable true
            reset()
            include 'armeabi' //根据需求自己修改
            universalApk true 
        }
    }

```

4:关于混淆

```java
-keep class com.sina.weibo.sdk.** { *; }
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
WbSdk.install(this,mAuthInfo);
```
其中：APP_KEY、 REDIRECT_URL、 SCOPE需要替换成第三方应用申请的内容。

### 3. 实现WbAuthListener接口

```java
private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener{
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            WBAuthActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
                        updateTokenView(false);
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(WBAuthActivity.this, mAccessToken);
                        Toast.makeText(WBAuthActivity.this,
                                R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public void cancel() {
            Toast.makeText(WBAuthActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Toast.makeText(WBAuthActivity.this, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }
```
### 4. 调用方法，认证授权
* 1  Web 授权，直接调用以下函数：*
```java
mSsoHandler = new SsoHandler(WBAuthActivity.this);
mSsoHandler.authorizeWeb(new WbAuthListener());
```
* 2 SSO授权，需要调用以下函数：*
```java
mSsoHandler = new SsoHandler(WBAuthActivity.this);
mSsoHandler. authorizeClientSso(new WbAuthListener());
```

* 3 all In one方式授权，需要调用以下函数：*
```java
mSsoHandler = new SsoHandler(WBAuthActivity.this);
mSsoHandler. authorize(new WbAuthListener());
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



## 其它功能
其它功能请相见文档：[微博Android平台SDK文档V4.1.pdf][1]

[1]:https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/%E6%96%B0%E6%96%87%E6%A1%A3/%E5%BE%AE%E5%8D%9ASDK%204.0%E6%96%87%E6%A1%A3.pdf
[2]:https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98%20FAQ.md
[3]:https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/WeiboSDK_API-V2.4.0.CHM
[4]:https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/%E6%96%B0%E6%96%87%E6%A1%A3/app-debug.apk
[5]:https://github.com/sinaweibosdk/weibo_android_sdk/tree/master/weibosdkdemo
[6]:https://github.com/sinaweibosdk/weibo_android_sdk/edit/master/README.md#%E7%BD%91%E7%BB%9C%E8%AF%B7%E6%B1%82%E6%A1%86%E6%9E%B6%E7%9A%84%E4%BD%BF%E7%94%A8
[7]:https://github.com/sinaweibosdk/weibo_android_sdk/edit/master/README.md#%E4%B8%BE%E4%BE%8B%E4%BD%BF%E7%94%A8%E5%BC%82%E6%AD%A5%E6%8E%A5%E5%8F%A3%E6%9D%A5%E5%8F%91%E9%80%81%E4%B8%80%E6%9D%A1%E5%B8%A6%E5%9B%BE%E7%89%87%E7%9A%84%E5%BE%AE%E5%8D%9A
[8]:http://sinaweibosdk.github.io/weibo_android_sdk/doc/
[9]:http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
[10]:http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
