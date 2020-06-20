#### 基于Okhttp实现的微信支付v3接口的Spring boot starter
目前只完成直连商户h5/app/native/jsapi下单，支付通知

#### 使用方法（ide中需要安装lombok插件）
##### 1、mvn install，项目中引入该starter

##### 2、配置必要属性
wx.app-id=
##### 商户id
wx.pay.mchId=
##### 微信支付证书序列号
wx.pay.certificate-serial-no=
##### v3密钥
wx.pay.api-v3-key=
##### 微信支付证书私钥文件
wx.pay.private-key-path=classpath:/cert/xxx.pem
##### 支付结果通知地址
wx.pay.notify-url=https://xxx

##### 3、在业务Service中注入WxPayV3Service即可
