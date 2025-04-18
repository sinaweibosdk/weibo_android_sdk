## 13.10.5-ciopt-4 (2025-04-18)

- temp (962a302)
- temp (c8b5499)
- temp (27c64f7)
- temp (84463d2)
- temp (5de47f5)
- temp (f72d7d4)
- temp (96fdb58)
- temp (a7920b4)
- temp (261a030)
- temp (a0d6ad6)
- temp (dd0bdde)
- temp (cf421a5)
- temp (09c0ce5)
- temp (5984fcd)
- temp (0c14313)
- temp (7abf858)
- temp (e814eb5)
- temp (9bd1832)
- temp (f859773)
- temp (684a514)
- temp (ecc34f5)
- temp (fbf4aa7)
- temp (2ceb7e9)
- temp (ad313d7)
- temp (cf3e7ca)
- temp (f738c58)
- temp (ccf738c)
- temp (976a277)
- temp (23c94d4)
- temp (f3b7d01)
- temo (5b12e1b)
- temp (dec56c8)
- temp (84d5081)
- temp (5850f74)
- temp (268b6a4)
- temp (30c5486)

## 13.10.5-ciopt-3 (2025-04-15)

- temp (f320cdb)
- temp (c4f1e7b)
- temp (db00a7c)
- temp (a42d28a)
- temp (c7e7c7b)
- temp (3bc1c1a)
- temp (449af2c)
- temp (58b8899)
- temp (b4ac90a)

## 13.10.5-ciopt-2 (2025-04-15)

- temp (d2e9c59)
- temp (74cf7dc)
- temp (d4686cb)
- temp (32321e3)
- temp (fe63ca5)
- temp (325854f)
- temp (b2ebf2d)
- temp (fa1cb98)
- temp (ad456dc)
- temp (7bdd146)
- temp (fe02485)
- temp (27d486e)
- temp (352c3cb)

## 13.10.5-ciopt-1 (2025-04-10)

- temp (14530b7)
- temp (8f70c3b)
- temp (59df12a)
- temp (39fb9d0)
- tempo (08f5df6)
- temp (14bae01)
- temp (6198232)
- temp (35bbe3d)
- temo (d45c902)
- temo (0ec3647)
- temp (f421a04)
- temp (f8c46bb)
- temp (0b54fc9)
- temp (195b65b)
- temp (1017aeb)
- temp (1ccb730)
- temp (7d0f5c2)
- temp (74434b7)
- temp (8313ca0)
- temp (95948da)
- temp (5accef4)
- temp (408b7cd)

## 13.10.5-cicd-2 (2025-04-07)

- temp (3f1b802)
- temp (8c38108)
- temp (5e8dcea)
- temp (18cfbda)
- temp (b5af445)
- temp (afecf7c)
- temp (fdc2057)

## 13.10.5-cicd-1 (2025-04-03)



v13.10.5
> 1、targetSdk支持34
>
> 2、增加单图分享时的图片200k限制，可通过ImageObject.checkArgs检查
>
> 3、扩展超话分享文档


v13.10.4
> 1、新增默认authority：${applicationId}.wbsdk.fileprovider
> 
> 2、支持自定义authority
> 
> 3、支持相册分享
> 
> 4、移除so库，SDK不再包含so文件
> 
> 5、bugfix：包含中文路径的视频/图片分享失败
> 
> 6、新增函数：判断onActivityResult的回调源（IWBAPI.isShareResult、IWBAPI.isAuthorizeResult）


v13.10.2
> 1、小米应用市场合规
> 
> 2、ContentProvider的Androidx标准库迁徙（com.sina.weibo.sdk.content.FileProvider -> androidx.core.content.FileProvider）

v13.10.1

> 1、统一包路径：com.sina.weibo.sdk（不再会有com.sina.weibo.sdk以外的class）
>
> 2、修改SDK中FileProvider的filePath文件命名，防止文件冲突：filepaths->wbsdk_filepaths

v13.10.0

  > 1、扩展分享支持目录：ExternalFileDir子目录、getExternalCacheDir、getExternalCacheDir子目录

v13.6.1

  > 1、适配target 33  
  > 2、取消SDK明文请求（usesCleartextTraffic=true）的声明

v13.5.0  

> 1、升级浏览器网络安全配置

v12.5.0  

> 1、调整信息安全策略，下线部分设备信息校验

v12.3.1

> 1、修复bug

v12.3.0

> 1、修复bug  
> 2、增强安全性

v11.12.0

> 1、增强安全性

v11.11.2

> 1、修复bug

v11.11.1

> 1、优化SDK初始化逻辑，初始化不再强制依赖于Activity，可提前到Application初始化。
> 2、修复token有效期错误显示为0的问题  
> 3、解决空指针异常

11.8.1

> 1、修复部分场景下分享后无法正确返回回调结果的问题。

v11.6.0

> 1、修复Crash。  
> 2、新增超话分享功能（需和微博商务洽谈获取白名单权限后进行该功能开发，否则分享无效）

v10.10.0

> 1、适配Android 11 ,支持通过FileProvider分享。

v10.9.0

> 1、修复可能存在的安全问题。

v10.8.0

> 1、修复bug。


v10.7.0

> 1、20年第一个版本,在v9.12.0基础上的升级。  
> 2、主要解决9.12.0版本中的bug.  
> 3、v4.x.x版本系列之后不再维护，推荐升级。具体接入方式2019SDK目录下有文档。  
> 4、接入中遇到问题及时加群.


------

2019年末，微博SDK版本v9.12.0全新更新：

> 1、全新的SDK接入文档，解决同学们接入时候没有详细文档的痛点。  
> 2、全新的SDK API设计。  
> 3、全新的SDK Demo，实现傻瓜式接入。  
> 4、aar包瘦身,删除无用代码。
