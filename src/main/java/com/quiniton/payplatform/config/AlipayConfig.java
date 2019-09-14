package com.quiniton.payplatform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {

    @Value("${alipay.url}")
    private String url;

    @Value("${alipay.app_id}")
    private String appId;

    @Value("${alipay.format}")
    private String format;

    @Value("${alipay.charset}")
    private String charset;

    @Value("${alipay.sign_type}")
    private String signType;

    @Value("${alipay.version}")
    private String version;

    @Value("${alipay.private_key}")
    private String privateKey;

    @Value("${alipay.public_key}")
    private String publicKey;

    public String getUrl() {
        return url;
    }

    public String getAppId() {
        return appId;
    }

    public String getFormat() {
        return format;
    }

    public String getCharset() {
        return charset;
    }

    public String getSignType() {
        return signType;
    }

    public String getVersion() {
        return version;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
