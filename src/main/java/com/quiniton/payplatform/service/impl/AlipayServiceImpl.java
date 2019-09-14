package com.quiniton.payplatform.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.quiniton.payplatform.common.RSAUtils;
import com.quiniton.payplatform.config.AlipayConfig;
import com.quiniton.payplatform.entity.PayMethodEnum;
import com.quiniton.payplatform.entity.PayObject;
import com.quiniton.payplatform.service.IAlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class AlipayServiceImpl implements IAlipayService<PayObject> {

    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 统一收单交易创建接口
     *
     * @param payObject
     */
    @Override
    public void create(PayObject payObject) {

    }

    /**
     * 统一收单交易关闭接口
     *
     * @param payObject
     */
    @Override
    public void close(PayObject payObject) {

    }

    /**
     * 统一收单交易支付接口
     *
     * @param payObject
     */
    @Override
    public void pay(PayObject payObject) {

    }

    /**
     * 统一收单交易退款接口
     *
     * @param payObject
     */
    @Override
    public void refund(PayObject payObject) {

    }

    /**
     * 统一收单交易退款查询
     *
     * @param payObject
     */
    @Override
    public void refundQuery(PayObject payObject) {

    }

    /**
     * 统一收单交易撤销接口
     *
     * @param payObject
     */
    @Override
    public void cancel(PayObject payObject) {

    }

    /**
     *
     * @param payMethod 请求方法
     * @param dataMap 请求参数
     * @return
     */
    protected Map createParamsMap(PayMethodEnum payMethod, Map dataMap) {
        Map<String,String> param = new TreeMap();
        param.put("app_id", alipayConfig.getAppId());
        param.put("method", payMethod.name());
        param.put("format", alipayConfig.getFormat());
        param.put("charset", alipayConfig.getCharset());
        param.put("sign_type", alipayConfig.getSignType());
        param.put("timestamp", DateUtil.date().toDateStr());
        param.put("version", alipayConfig.getVersion());
        param.put("app_auth_token", "");
        param.put("biz_content", JSONObject.toJSONString(dataMap));
        StringBuilder paramsStr = new StringBuilder();
        for (Map.Entry<String,String> entry: param.entrySet()) {
            paramsStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String siginStr = paramsStr.substring(0,paramsStr.length());
        siginStr = RSAUtils.sign(alipayConfig.getPrivateKey(),siginStr);
        param.put("sign",siginStr);
        return param;
    }
}
