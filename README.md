## 基于Okhttp实现的微信支付v3接口的Spring boot starter

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/top.rwocj/wxpay-v3-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/top.rwocj/wxpay-v3-spring-boot-starter)

#### 目前只完成直连商户(普通商户)基础支付，包含下单、查询、关单、退款、支付通知、退款通知等

#### 使用方法

##### 1、pom引入组件

Spring Boot 3引入

```xml

<dependency>
    <groupId>top.rwocj</groupId>
    <artifactId>wxpay-v3-spring-boot-starter</artifactId>
    <version>0.8</version>
</dependency>
```

Spring Boot 2引入

```xml

<dependency>
    <groupId>top.rwocj</groupId>
    <artifactId>wxpay-v3-spring-boot-starter</artifactId>
    <version>0.8-javax</version>
</dependency>
```

注意：jdk1.8如报密钥太长问题，下载[jce_policy-8.zip](http://download.oracle.com/otn-pub/java/jce/8/jce_policy-8.zip)
解压，将其中的local_policy.jar和US_export_policy.jar两个文件替换掉自己%JAVE_HOME%\jre\lib\security文件夹下对应的原文件

其他Java项目，可参照WxPayV3AutoConfiguration，手动初始化WxPayV3Service类

##### 2、配置必要属性,不需要对应的支付方式，则不设置其对应的appid

* wx.pay.app-ids.jsapi=jsapi-appid
* wx.pay.app-ids.h5=h5-appid
* wx.pay.app-ids.natives=native-appid
* wx.pay.app-ids.app=app-appid
* wx.pay.mch-id=商户id
* wx.pay.api-v3-key=v3密钥
* wx.pay.p12-path=classpath:/cert/API证书.p12
* wx.pay.notify-url=https://xxx/支付结果通知地址
* wx.pay.refund-notify-url=https://xxx/退款通知地址(可选)

##### 3、在业务Service/Controller中注入WxPayV3Service即可

* jsapi支付示例

```java
import top.rwocj.wx.pay.dto.WxCreateOrderRequest;
import top.rwocj.wx.pay.dto.WxJSAPICreateOrderRes;
import top.rwocj.wx.pay.service.WxPayV3Service;

private final WxPayV3Service wxPayV3Service;

public void create() {
    WxCreateOrderRequest request = WxCreateOrderRequest.jsapiOrder("商品名称", "外部订单号", 100, "openid");
    WxJSAPICreateOrderRes result = wxPayV3Service.createJSAPIOrder(request);
}
```

* 其他支付示例，返回值根据类型不同而不同,参见OrderType

```java
import top.rwocj.wx.pay.dto.WxCreateOrderRequest;
import top.rwocj.wx.pay.enums.OrderType;
import top.rwocj.wx.pay.service.WxPayV3Service;

private final WxPayV3Service wxPayV3Service;

public void create() {
    WxCreateOrderRequest request = new WxCreateOrderRequest("商品名称", "外部订单号", OrderType.h5, 100, "openid");
    String result = wxPayV3Service.createOrder(request);
}
```

* 支付通知验签与解密示例

```java
import jakarta.servlet.http.HttpServletRequest;
import top.rwocj.wx.pay.core.WxPayException;
import top.rwocj.wx.pay.dto.Result;
import top.rwocj.wx.pay.dto.WxPayResult;
import top.rwocj.wx.pay.service.WxPayV3Service;

private final WxPayV3Service wxPayV3Service;

public Result payNotice(HttpServletRequest request) {
    try {
        WxPayResult wxPayResult = wxPayV3Service.validateAndDecryptPayNotification(request);
        //todo 增加自己的业务逻辑
        return Result.ok();
    } catch (WxPayException wxPayException) {
        return Result.fail();
    }
}
```

* 退款通知验签与解密示例：

```java
import jakarta.servlet.http.HttpServletRequest;
import top.rwocj.wx.pay.core.WxPayException;
import top.rwocj.wx.pay.dto.Result;
import top.rwocj.wx.pay.dto.WxRefundNoticeResult;
import top.rwocj.wx.pay.service.WxPayV3Service;

private final WxPayV3Service wxPayV3Service;

public Result refundNotice(HttpServletRequest request) {
    try {
        WxRefundNoticeResult wxRefundNoticeResult = wxPayV3Service.validateAndDecryptRefundNotification(request);
        //todo 增加自己的业务逻辑
        return Result.ok();
    } catch (WxPayException wxPayException) {
        return Result.fail();
    }
}
```

* 其他未封装的接口可调用: WxPayV3Service.post(String url, Object requestBody)和WxPayV3Service.get(String url)
* 自定义OkHttpClient配置，实现OkHttpClientCustomizer接口并注册为Spring Bean即可
