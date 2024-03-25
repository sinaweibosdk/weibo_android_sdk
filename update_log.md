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

