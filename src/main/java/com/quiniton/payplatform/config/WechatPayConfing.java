package com.quiniton.payplatform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WechatPayConfing {

    @Value("${wechat-pay.appid}")
    private String appId;

    @Value("${wechat-pay.mch_id}")
    private String mchId;

    @Value("${wechat-pay.cert_path}")
    private String certPath;

    @Value("${wechat-pay.app_secret}")
    private String appSecret;

    @Value("${wechat-pay.sign_type}")
    private String signType;

    public String getAppId() {
        return appId;
    }

    public String getMchId() {
        return mchId;
    }

    public String getCertPath() {
        return certPath;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getSignType() {
        return signType;
    }
}
