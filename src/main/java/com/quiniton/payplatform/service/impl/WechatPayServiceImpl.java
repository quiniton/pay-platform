package com.quiniton.payplatform.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.quiniton.payplatform.config.WechatPayConfing;
import com.quiniton.payplatform.constant.WechatPayRequestURL;
import com.quiniton.payplatform.entity.PayObject;
import com.quiniton.payplatform.entity.TradeTypeEnum;
import com.quiniton.payplatform.service.IWechatPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class WechatPayServiceImpl implements IWechatPayService<PayObject> {

    @Autowired
    private WechatPayConfing wechatPayConfing;

    private static Logger logger = LoggerFactory.getLogger(WechatPayServiceImpl.class);

    /**
     * 统一收单交易创建接口
     *
     * @param payObject
     */
    @Override
    public void create(PayObject payObject) {
        Map<String,String> params = new HashMap<>();
        params.put("body",payObject.getSubject());
        if (StrUtil.isNotBlank(payObject.getStoreId())){
            params.put("attach",payObject.getStoreId());
        }
        if (payObject.getTransCurrency() != null){
            params.put("fee_type",payObject.getTransCurrency().name());
        }
        if (StrUtil.isNotBlank(payObject.getTimeExpire())){
            params.put("time_expire",payObject.getTimeExpire());
        }
        params.put("out_trade_no",payObject.getOutTradeNo());
        params.put("total_fee",payObject.getTotalPrice());
        params.put("spbill_create_ip",payObject.getIpAddr());
        if (StrUtil.isNotBlank(payObject.getDesc())){
            params.put("detail",payObject.getDesc());
        }
        sendRequest(WechatPayRequestURL.CREATE_PAY,TradeTypeEnum.APP,params);
    }

    /**
     * 统一收单交易关闭接口
     *
     * @param payObject
     */
    @Override
    public void close(PayObject payObject) {
        Map<String,String> params = new HashMap<>();
        params.put("out_trade_no",payObject.getOutTradeNo());
        sendRequest(WechatPayRequestURL.CREATE_PAY,TradeTypeEnum.APP,params);
    }

    /**
     * 统一收单交易支付接口
     *
     * @param payObject
     */
    @Override
    @Deprecated
    public void pay(PayObject payObject) {

    }

    /**
     * 统一收单交易退款接口
     *
     * @param payObject
     */
    @Override
    public void refund(PayObject payObject) {
        Map<String,String> params = new HashMap<>();
        params.put("out_trade_no",payObject.getOutTradeNo());
        params.put("transaction_id",payObject.getTradeNo());
        params.put("total_fee",payObject.getTotalPrice());
        params.put("refund_fee",payObject.getRefundPrice());
        params.put("out_refund_no",payObject.getRefundNo());
        sendRequest(WechatPayRequestURL.REFUND,TradeTypeEnum.APP,params);
    }

    /**
     * 统一收单交易退款查询
     *
     * @param payObject
     */
    @Override
    public void refundQuery(PayObject payObject) {
        Map<String,String> params = new HashMap<>();
        params.put("out_trade_no",payObject.getOutTradeNo());
        sendRequest(WechatPayRequestURL.REFUND_QUERY,TradeTypeEnum.APP,params);
    }

    /**
     * 统一收单交易撤销接口
     *
     * @param payObject
     */
    @Override
    public void cancel(PayObject payObject) {
        Map<String,String> params = new HashMap<>();
        params.put("out_trade_no",payObject.getOutTradeNo());
        sendRequest(WechatPayRequestURL.CLOSE_PAY,TradeTypeEnum.APP,params);
    }

    /**
     * 统一收单交易查询
     *
     * @param payObject
     */
    @Override
    public void payQuery(PayObject payObject) {
        Map<String,String> params = new HashMap<>();
        params.put("out_trade_no",payObject.getOutTradeNo());
        params.put("transaction_id",payObject.getTradeNo());
        sendRequest(WechatPayRequestURL.PAY_QUERY,TradeTypeEnum.APP,params);
    }

    protected void sendRequest(String url,TradeTypeEnum tradeType,Map dataMap){
        TreeMap<String,String> params = new TreeMap<>(dataMap);
        params.put("appid",wechatPayConfing.getAppId());
        params.put("mch_id",wechatPayConfing.getMchId());
        params.put("nonce_str", RandomUtil.randomString(28));
        params.put("sign_type",wechatPayConfing.getSignType());
        params.put("trade_type",tradeType.name());
        params.put("notify_url",wechatPayConfing.getNotifyUrl());
        StringBuilder paramsStr = new StringBuilder();
        for (Map.Entry<String,String> entry: params.entrySet()) {
            paramsStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        String signStr = paramsStr.append("key=").append(wechatPayConfing.getAppSecret()).toString();
        params.put("sign", new String(DigestUtil.md5(signStr)));
        String xmlStr = XmlUtil.mapToXmlStr(params,"xml");
        if (xmlStr.contains("<xml>")){
            xmlStr = xmlStr.substring(xmlStr.indexOf("<xml>"));
        }
        logger.info("wxpay params:{}",xmlStr);
        HttpResponse resonpse = HttpUtil.createPost(url).body(xmlStr).execute();
        if (resonpse.isOk()){
            logger.info("wxpay respons:{}",resonpse.body());
        }
    }
}
