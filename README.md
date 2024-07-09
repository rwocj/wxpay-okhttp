## 基于Okhttp实现的微信支付v3接口的Spring boot starter

### 需要JDK17+ Spring Boot 3.x

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/top.rwocj/wxpay-v3-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/top.rwocj/wxpay-v3-spring-boot-starter)

#### 目前只完成直连商户(普通商户)基础支付，包含下单、查询、关单、支付通知、调用等

#### 使用方法

* 1、pom引入组件

```xml

<dependency>
  <groupId>top.rwocj</groupId>
    <artifactId>wxpay-v3-spring-boot-starter</artifactId>
  <version>0.6</version>
</dependency>
```

* 2、配置必要属性
  * wx.pay.app-id=appid
  * wx.pay.mch-id=商户id
  * wx.pay.certificate-serial-no=API证书序列号
  * wx.pay.api-v3-key=v3密钥
  * wx.pay.private-key-path=classpath:/cert/API证书私钥文件apiclient_key.pem
  * wx.pay.notify-url=https://xxx/支付结果通知地址

* 3、在业务Service/Controller中注入WxPayV3Service即可
  * jsapi支付: JSAPICreateOrderRes res = WxPayV3Service.createJSAPIOrder(createOrderRequest)
  * 其他支付：String res = WxPayV3Service.createOrder(createOrderRequest)，返回值根据类型不同而不同,参见OrderType
  * 支付回调验签与解密:WxPayResult payResult = WxPayV3Service.validateAndDecryptPayNotification(request)
  * 其他未封装的接口可调用: WxPayV3Service.post(String url, Object requestBody)和WxPayV3Service.get(String url)
