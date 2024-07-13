## 基于Okhttp实现的微信支付v3接口的Spring boot starter

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/top.rwocj/wxpay-v3-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/top.rwocj/wxpay-v3-spring-boot-starter)

#### 目前只完成直连商户(普通商户)基础支付，包含下单、查询、关单、支付通知、调用等

#### 使用方法

* 1、pom引入组件

Spring Boot 3引入
```xml
<dependency>
  <groupId>top.rwocj</groupId>
    <artifactId>wxpay-v3-spring-boot-starter</artifactId>
  <version>0.6</version>
</dependency>
```

Spring Boot 2引入
```xml
<dependency>
  <groupId>top.rwocj</groupId>
    <artifactId>wxpay-v3-spring-boot-starter</artifactId>
  <version>0.6-javax</version>
</dependency>
```
注意：jdk1.8如报密钥太长问题，下载[jce_policy-8.zip](http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip)解压，将其中的local_policy.jar和US_export_policy.jar两个文件替换掉自己%JAVE_HOME%\jre\lib\security文件夹下对应的原文件

其他Java项目，可参照WxPayV3AutoConfiguration，手动初始化WxPayV3Service类

* 2、配置必要属性
  * wx.pay.app-id=appid
  * wx.pay.mch-id=商户id
  * wx.pay.api-v3-key=v3密钥
  * wx.pay.p12-path=classpath:/cert/API证书.p12
  * wx.pay.notify-url=https://xxx/支付结果通知地址
  * wx.pay.refund-notify-url=https://xxx/退款通知地址(可选)


* 3、在业务Service/Controller中注入WxPayV3Service即可
  * jsapi支付: JSAPICreateOrderRes res = WxPayV3Service.createJSAPIOrder(createOrderRequest)
  * 其他支付：String res = WxPayV3Service.createOrder(createOrderRequest)，返回值根据类型不同而不同,参见OrderType
  * 支付回调验签与解密:WxPayResult payResult = WxPayV3Service.validateAndDecryptPayNotification(request)
  * 其他未封装的接口可调用: WxPayV3Service.post(String url, Object requestBody)和WxPayV3Service.get(String url)
