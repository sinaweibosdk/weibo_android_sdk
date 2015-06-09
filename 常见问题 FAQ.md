#常见问题FAQ：（整理中...）

###1. 一些概念
**Q：**什么是移动客户端接入？  
**Q：**什么是授权？  
**Q：**什么是 SSO 登录（授权）？  
**Q：**什么是 AppKey、AccessToken、RedirectURI（回调页）？  
**Q：**什么Scope？  
**Q：**微博开放平台API地址在哪？  
**Q：**什么是......  
**Q：**......  
**A：**亲，如果你还停留在 QQ 群里面问上述问题，那你已经被我深深的BS了，请先阅读以下文档，搞清楚概念再说。  

 - [移动应用][1]  
 - [移动客户端接入][2]
 - [授权机制][3]  
 - [SSO 授权][4]  
 - [Scope][11]
 - [微博 OpenAPI][5]  

###2. 关于审核
**Q：**我的审核怎么还没过？  
>**A：**[请查阅审核相关问题文档][8]

**Q：**应用程序还没开发完，审核表单里的下载地址该怎么填呢？  
>**A：**随便写个，以后在改，如：将某个 APK 上传到网盘中去，然后再获取其地址，填到上面去。

###3. 关于运行 DEMO
**Q：**为什么直接运行 weibo.sdk.android.demo.apk 能授权，但我直接运行 DEMO 工程，却无法授权？  
>**A：** debug.keystore 不正确。运行 DEMO 源代码时，请确保在 C:\Users\XXXXX\.android 目录下，把 Android 默认的 debug.keystore 替换成官方在 GitHub 上提供的 debug.keystore。另外，用户在替换前，最好先备份一下原始的 debug.keystore。

###4. 关于 keystore
**Q：**什么是 keystore？我的应用能否使用 Github 上提供的debug.keysotre？  
>**A：**
>
 - 问题一：这个概念总体上来说比较复杂，请 Google 之。  
 - 问题二：Github 上提供的 debug.keysotre 是新浪官方的，除了编译运行官方 DEMO 外，请不要直接使用它，出于安全的考虑，你应该为自己的应用提供一份 keysotre，至于如何生成 keysotre，请 Google 之。
    
**Q：**为什么我使用签名工具（[app_signatures.apk][10]），没有生成出来签名？  
>**A：**
>
 - 首先，要生成签名，前提条件是，该应用必须已安装到该设备上，没安装上对应APK，肯定是生成不出来的。我们也不能凭空给造一个，上帝造人也得需要道具对不。签名是根据APK的信息生成出来的。
 - 其次，针对于两个不同的应用，如 com.xxx.aaa 和 com.xxx.bbb，如果它们采用同一个 keystore，那么，它们最终生成出来的签名是一样的。一般情况下，对于一个公司来说，公司只需要保存两个 keystore
即可，debug.keystore和 release.keystore，前者用于测试，后者用于发布，对于多款产品，用同一份 keystore 就行。

**Q：**我有两个 keystore：release.keystore 和 debug.keystore 两个，我在官方注册了一个 Android 签名，它使用的是 release.keystore 签名过后的 APK 生成出来的，那我使用 debug.keystore 签名后的 APK 能否正确授权？  
>**A：**不可以，两者必须一一匹配。如果你用 release.keystore 生成出来的 APK，如果想正确授权，那么在官方注册的 Android 签名也必须是由 APK 生成的；反之亦然。目前在注册签名时，已提供了两组签名，debug 和 release，这样很方便大家测试。

###5. 关于授权
**Q：**为什么自己写的程序，无法正确授权？但是DEMO是可以的？  
>**A：**原因：签名和包名未注册，或者签名不正确，都会导致该问题。  
如何签名，请查看[《微博Android平台SDK文档V3.1.1.pdf》][9]中：如何注册应用程序的包名和签名。应用程序的包名就是 AndroidManifest.xml 文件中，package 标签所代表的内容。

**Q：**官方 SDK DEMO 中的 **onComplete(Bundle values)** 函数，怎么取不到 token，Bundle 中只包含 code，为什么？  
>**A：**原因同上。

**Q：**没审核通过的应用，能取到 Token 吗？  
>**A：**可以，只要你正确的注册了包名和签名。

**Q：**微博客户端升级到 V4.1，SSO 登录失败了，跳转到 Web 授权界面上去了，V4.0 版本是可以，为什么？  
>**A：**出于安全性的考虑，微博客户端 V4.1 做了一些改动，会将包名和签名等信息发送给 Server 进行验证。V4.1 以前的版本是没有传入这些参数的。Server目前会优先根据包名和签名信息验证你的应用是否合法。  
因此，如果你的应用还没有在平台上注册包名和签名，请火速注册包名和签名，以免给您的应用造成用户流失。如果注册后还是不行，毫无疑问，你的签名肯定不正确，请确保您的签名是由您所测试的 APK 生成出来的。

**Q：**授权时的 ErrorCode 地址可以在哪里查找，常见问题都有什么解决方案？  
>**A：**[OAuth2.0 错误码][13]。

| error_code | error_message | 解决方案 |
| --------   | :-----        | :-----   |
| 21322    | redirect_uri_mismatch（重定向地址不匹配） | 请确保你在平台上填写的授权回调地址与*代码中写的授权回调地址(RedirectURI)*一样。 回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，但是没有定义将无法使用SDK认证登录。建议使用默认回调页 https://api.weibo.com/oauth2/default.html （可以在新浪微博开放平台->我的应用->应用信息->高级应用->授权设置->应用回调页中找到）。 |
| 21321    | applications over the unaudited use restrictions（未审核的应用使用人数超过限制） | 应用没有审核通过，也没有添加测试账号，调用OpenAPI中的部分接口会出现此错误。如果应用未通过审核，需要在“管理中心->应用信息->测试账号”中添加当前授权的账号，才能调用相应的OpenAPI接口。 |
| 21325    | invalid_grant（提供的Access Grant是无效的、过期的或已撤销的）| 请确保相关审核已通过：(1) 通过开发者身份认证审核 (2)在平台进行相关修改后，如修改签名等操作，同样需要等待审核 |
| 21337      | appkey permission denied | 请确保您已通过开发者身份认证审核 |
| 21338      | sso package and sign error |出于安全性的考虑，微博客户端 V4.1 做了一些改动，会将包名和签名等信息发送给 Server 进行验证。V4.1 以前的版本是没有传入这些参数的。Server目前会优先根据包名和签名信息验证你的应用是否合法。因此，如果你的应用还没有在平台上注册包名和签名，请火速注册包名和签名，以免给您的应用造成用户流失。如果注册后还是不行，毫无疑问，你的签名肯定不正确，请确保您的签名是由您所测试的 APK 生成出来的。|

###6. 关于微博分享
**Q：**微博分享需要客户端吗？  
>**A：**是的，目前第三方分享微博需要客户端的支持，请确保已安装官方微博客户端。如果不想使用微博客户端进行分享，可以使用 OpenAPI，拼接 URL，通过 HTTP 请求发送微博。  
请参照：
>
 - 发布一条微博信息：      http://open.weibo.com/wiki/2/statuses/update
 - 上传图片并发布一条微博：http://open.weibo.com/wiki/2/statuses/upload

**Q：**为什么我分享出去的微博很难看，就一个链接，没有图片，也没有类似 Card 的样子，这是分享 API 的 BUG 吗？  
>**A：**首先，这不是 BUG。实际上，如果你想要你分享出去的内容展示成 LinkCard 的样子，其前提条件是你分享的链接支持 LinkCard。即 Linkcard 的分享开关，是基于分享出来的链接的域名。打个比方，开启 v.youku.com 的 LinkCard 开关，从而发布器中带这个链接的，都能呈现为 LinkCard，该功能并不是针对于某个特定的应用，而是针对于特殊的网内容的。  
如果某个网站需要进行 LinkCard 商务合作，请联系 BD。详情请阅读：[移动客户端接入][2]

###7. 关于 OpenAPI
**Q：**目前的 V2.3 版怎么没有 OpenAPI，以前的版本是有的，如果我想使用对应的接口，怎么办？  
>**A：**目前的版本的版本没有将这个 OpenAPI 接口开放出去，基于一些额外的原因。以后的版本会将一些常用的 OpenAPI 接口进行封装，并发布出去，我们到时会为开发者提供一个比较好的框架来做这件事情。  
对于目前想使用 OpenAPI 接口的开发者来说，请使用旧版本 SDK 的 OpenAPI 接口，依葫芦画瓢即可。

**Q：**有没有 OpenAPI 的测试工具？ErrorCode 都有哪些？  
>**A：**
>
 - [微博 OpenAPI Error Code][6]  
 - [微博 OpenAPI 测试工具][7]  

[1]:http://open.weibo.com/wiki/%E7%A7%BB%E5%8A%A8%E5%BA%94%E7%94%A8
[2]:http://open.weibo.com/wiki/%E7%A7%BB%E5%8A%A8%E5%AE%A2%E6%88%B7%E7%AB%AF%E6%8E%A5%E5%85%A5
[3]:http://open.weibo.com/wiki/%E6%8E%88%E6%9D%83%E6%9C%BA%E5%88%B6
[4]:http://open.weibo.com/wiki/%E7%A7%BB%E5%8A%A8%E5%BA%94%E7%94%A8SSO%E6%8E%88%E6%9D%83
[5]:http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
[6]:http://open.weibo.com/wiki/Error_code
[7]:http://open.weibo.com/tools/console
[8]:http://t.cn/zHW4aDG
[9]:https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/%E5%BE%AE%E5%8D%9AAndroid%E5%B9%B3%E5%8F%B0SDK%E6%96%87%E6%A1%A3V3.1.1.docx
[10]:/app_signatures.apk
[11]:http://open.weibo.com/wiki/Scope
[12]:/raw/FAQ/screenshot/error_redirect_uri_mismatch.png
[13]:http://t.cn/8kWBkoj
