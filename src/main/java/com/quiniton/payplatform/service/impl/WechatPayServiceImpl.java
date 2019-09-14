package com.quiniton.payplatform.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.quiniton.payplatform.config.WechatPayConfing;
import com.quiniton.payplatform.entity.PayObject;
import com.quiniton.payplatform.entity.TradeTypeEnum;
import com.quiniton.payplatform.service.IWechatPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
public class WechatPayServiceImpl implements IWechatPayService<PayObject> {

    @Autowired
    private WechatPayConfing wechatPayConfing;

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

    protected Map createParamsMap(TradeTypeEnum tradeType,Map dataMap){
        TreeMap<String,String> params = (TreeMap)dataMap;
        params.put("appid",wechatPayConfing.getAppId());
        params.put("mch_id",wechatPayConfing.getMchId());
        params.put("nonce_str", RandomUtil.randomString(28));
        params.put("sign_type",wechatPayConfing.getSignType());
        params.put("trade_type",tradeType.name());
        StringBuilder paramsStr = new StringBuilder();
        for (Map.Entry<String,String> entry: params.entrySet()) {
            paramsStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String signStr = paramsStr.append("key=").append(wechatPayConfing.getAppSecret()).toString();
        dataMap.put("sign", DigestUtil.md5(signStr));
        return dataMap;
    }

}
