# ReadMe

------

公告：

鉴于Jcenter仓库停止服务，我们重新提供了基于MavenCentral仓库的依赖方式，并更新了SDK版本

[SDK 最新文档](https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/2019SDK/%E6%96%87%E6%A1%A3/%E5%BE%AE%E5%8D%9AAndroid%20SDK%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97_v11.12.0.pdf)

1. 在project根目录的build.gradle文件中添加依赖配置
```gradle
    allprojects {
        repositories {
            mavenCentral()
            ……
        }
    }
```
2. 在module的build.gradle文件中添加依赖和属性配置
```gradle
    android {
        ……
        defaultConfig {
            ndk {
                abiFilters 'armeabi' //, 'armeabi-v7a','arm64-v8a'
            }
        }
    }

    dependencies {
        implementation 'io.github.sinaweibosdk:core:12.5.0@aar'
    }
```
文档地址：https://github.com/sinaweibosdk/weibo_android_sdk/tree/master/2019SDK/文档

新包地址：https://github.com/sinaweibosdk/weibo_android_sdk/tree/master/2019SDK/aar

旧包地址：https://github.com/sinaweibosdk/weibo_android_sdk/tree/master/新版本以及文档

v12.5.0  
调整信息安全策略，下线部分设备信息校验

v12.3.1
1. 修复bug

v12.3.0

1. 修复bug
2. 增强安全性

v11.12.0
1. 增强安全性

v11.11.2
1. 修复bug

v11.11.1

1.优化SDK初始化逻辑，初始化不再强制依赖于Activity，可提前到Application初始化。（具体见文档[SDK v11.11.1文档](https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/2019SDK/%E6%96%87%E6%A1%A3/%E5%BE%AE%E5%8D%9AAndroid%20SDK%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97_v11.11.1.pdf)）
1.修复token有效期错误显示为0的问题
1.解决空指针异常

v11.8.1

1. 修复部分场景下分享后无法正确返回回调结果的问题。

v11.6.0

1. 修复Crash。
2. 新增超话分享功能（需和微博商务洽谈获取白名单权限后进行该功能开发，否则分享无效）

v10.10.0

1. 适配Android 11 ,支持通过FileProvider分享。


v10.9.0

1. 修复可能存在的安全问题。

v10.8.0

1. 修复bug.


v10.7.0

1. 20年第一个版本,在v9.12.0基础上的升级。

2. 主要解决9.12.0版本中的bug.
    3。v4.x.x版本系列之后不再维护，推荐升级。具体接入方式2019SDK目录下有文档。

3. 接入中遇到问题及时加群.




------

2019年末，微博SDK版本v9.12.0全新更新：

1.全新的SDK接入文档，解决同学们接入时候没有详细文档的痛点。

2.全新的SDK API设计。

3.全新的SDK Demo，实现傻瓜式接入。

4.aar包瘦身,删除无用代码。

注意点：

1.新版本API有变动，如需升级到最新版本，需要看着文档一步一步来升级。

2.新版本位置：2019SDK。

------

资料的下载入口在本网页的右侧,有一个 download-ZIP 按钮,即可下载到本地。↗↗

为了方便第三方开发者快速集成微博 SDK，我们提供了以下联系方式，协助开发者进行集成：

**QQ群：109094998**
**邮箱：sdk4wb@sina.cn**  
**微博：移动新技术**  

**如果您在使用过程中有些问题不清楚如何解决**，请先仔细阅读：[常见问题FAQ][2]，尝试能否找到对应的答案。   

另外，关于 SDK 的 Bug 反馈、用户体验，以及建议与不足等，请大家尽量提交到 Github 上，充分利用好 Github 这一工具。
目前 SDK 有很多不足之处，请给我们一些时间，我们会力争为第三方开发者提供一个规范、简单易用、稳定可靠、可扩展、可定制的 SDK。

------

# 快速上手

## 概述
微博 Android 平台 SDK 为第三方应用提供了简单易用的微博API调用服务，使第三方客户端无需了解复杂的验证机制即可进行授权登陆，并提供微博分享功能，可直接通过微博官方客户端分享微博。
>本文档详细内容请查阅：[微博Android平台SDK文档v11.12.0.pdf](https://github.com/sinaweibosdk/weibo_android_sdk/blob/master/2019SDK/%E6%96%87%E6%A1%A3/%E5%BE%AE%E5%8D%9AAndroid%20SDK%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97_v11.12.0.pdf)

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
