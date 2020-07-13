## 基于Okhttp实现的微信支付v3接口的Spring boot starter

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.rwocj/wxpay-v3-spring-boot-starter/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.rwocj/wxpay-v3-spring-boot-starter)

#### 目前只完成直连商户(普通商户)基础支付，包含下单、查询、关单、支付通知、调用等

#### 使用方法

* 1、pom引入组件

* 2、配置必要属性
    * wx.app-id=appid
    * wx.pay.mchId=商户id
    * wx.pay.certificate-serial-no=微信支付证书序列号
    * wx.pay.api-v3-key=v3密钥
    * wx.pay.private-key-path=classpath:/cert/微信支付证书私钥文件.pem
    * wx.pay.notify-url=https://xxx/支付结果通知地址

* 3、在业务Service/Controller中注入WxPayV3Service即可
