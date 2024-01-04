# ReadMe


## 概述

微博 Android 平台 SDK 为第三方应用提供了简单易用的微博API调用服务，使第三方客户端无需了解复杂的验证机制即可进行授权登陆，并提供微博分享功能，可直接通过微博官方客户端分享微博。


---
## 快速上手

最新版本：13.10.1

[账号注册](doc/app_register.pdf)

[SDK-文档](doc/sdk_doc.pdf)

[SDK-下载](doc/core-13.10.1.aar)

[SDK-DEMO下载](doc/Weibo_SDK_DEMO.zip)

---

## 名词解释

| 名词        | 注解                                                         |
| ----------- | :----------------------------------------------------------- |
| AppKey      | 分配给每个第三方应用的 app key。用于鉴权身份，显示来源等功能。 |
| RedirectURI | 第三方应用授权回调页面。建议使用默认回调页`https://api.weibo.com/oauth2/default.html` ，可以在新浪微博开放平台->我的应用->应用信息->高级应用->授权设置->应用回调页中找到。 |
| Scope       | 通过scope，平台将开放更多的微博核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新OAuth2.0授权页中有权利选择赋予应用的功能。 |
| AccessToken | 表示用户身份的 token，用于微博 API 的调用。                  |
| Web 授权    | 通过WebView进行授权，并返回Token信息。                       |
| SSO 授权    | 通过唤起微博客户端进行授权，并返回Token信息。                |

使用微博sdk授权和分享时，请确保你的AppKey、RedirectURI、Scope、PackageName和开发者官网中填写的一致.

---

## 帮助

为了方便第三方开发者快速集成微博 SDK，我们提供了以下联系方式，协助开发者进行集成：

**QQ群：146521642**
**邮箱：sdk4wb@sina.cn**  
**微博：移动新技术**  

**如果您在使用过程中有些问题不清楚如何解决**，请先仔细阅读：[常见问题FAQ](FAQ.md)，尝试能否找到对应的答案。   

另外，关于 SDK 的 Bug 反馈、用户体验，以及建议与不足等，请大家尽量提交到 Github 上，充分利用好 Github 这一工具。
目前 SDK 有很多不足之处，请给我们一些时间，我们会力争为第三方开发者提供一个规范、简单易用、稳定可靠、可扩展、可定制的 SDK。