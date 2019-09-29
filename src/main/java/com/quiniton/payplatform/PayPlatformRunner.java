package com.quiniton.payplatform;

import com.quiniton.payplatform.entity.PayObject;
import com.quiniton.payplatform.service.IAlipayService;
import com.quiniton.payplatform.service.IWechatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PayPlatformRunner implements ApplicationRunner {

    @Autowired
    private IAlipayService<PayObject> alipayService;

    @Autowired
    private IWechatPayService wechatPayService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        PayObject payObject = new PayObject();
        payObject.setOutTradeNo("tesst");
        payObject.setTotalPrice("11.80");
        payObject.setSubject("test");
        payObject.setIpAddr("127.0.0.1");
        alipayService.create(payObject);

        wechatPayService.create(payObject);
    }
}
