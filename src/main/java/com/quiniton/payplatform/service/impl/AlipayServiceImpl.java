package com.quiniton.payplatform.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.quiniton.payplatform.common.RSAUtils;
import com.quiniton.payplatform.config.AlipayConfig;
import com.quiniton.payplatform.entity.PayObject;
import com.quiniton.payplatform.entity.TradeMethodEnum;
import com.quiniton.payplatform.service.IAlipayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class AlipayServiceImpl implements IAlipayService<PayObject> {

    @Autowired
    private AlipayConfig alipayConfig;

    private static final Logger logger = LoggerFactory.getLogger(AlipayServiceImpl.class);

    /**
     * 统一收单交易创建接口
     *
     * @param payObject
     */
    @Override
    public void create(PayObject payObject) {
        Map<String,String> params = new HashMap<>();
        params.put("out_trade_no",payObject.getTradeNo());
        params.put("total_amount",payObject.getTotalPrice());
        params.put("subject",payObject.getSubject());
        params.put("body",payObject.getDesc());
        params.put("operator_id",payObject.getOperatorId());
        params.put("store_id",payObject.getStoreId());
        params.put("terminal_id",payObject.getTerminalId());
        params.put("timeout_express",payObject.getTimeoutExpress());
        sendRequest(TradeMethodEnum.CREATE,params);
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
        params.put("trade_no",payObject.getTradeNo());
        sendRequest(TradeMethodEnum.CLOSE,params);
    }

    /**
     * 统一收单交易支付接口
     *
     * @param payObject
     */
    @Override
    public void pay(PayObject payObject) {
        Map<String,String> params = new HashMap<>();
        params.put("out_trade_no",payObject.getTradeNo());
        params.put("total_amount",payObject.getTotalPrice());
        params.put("subject",payObject.getSubject());
        params.put("body",payObject.getDesc());
        params.put("operator_id",payObject.getOperatorId());
        params.put("store_id",payObject.getStoreId());
        params.put("terminal_id",payObject.getTerminalId());
        params.put("timeout_express",payObject.getTimeoutExpress());
        sendRequest(TradeMethodEnum.PAY,params);
    }

    /**
     * 统一收单交易退款接口
     *
     * @param payObject
     */
    @Override
    public void refund(PayObject payObject) {
        Map<String,String> params = new HashMap<>();
        params.put("out_trade_no",payObject.getTradeNo());
        params.put("trade_no",payObject.getTradeNo());
        params.put("refund_amount ",payObject.getTotalPrice());
        params.put("operator_id",payObject.getOperatorId());
        params.put("store_id",payObject.getStoreId());
        params.put("out_request_no",payObject.getRefundNo());
        params.put("terminal_id",payObject.getTerminalId());
        sendRequest(TradeMethodEnum.REFUND,params);
    }

    /**
     * 统一收单交易退款查询
     *
     * @param payObject
     */
    @Override
    public void refundQuery(PayObject payObject) {
        Map<String,String> params = new HashMap<>();
        params.put("out_trade_no",payObject.getTradeNo());
        params.put("trade_no",payObject.getTradeNo());
        params.put("out_request_no",payObject.getRefundNo());
        sendRequest(TradeMethodEnum.REFUND_QUERY,params);
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
        params.put("trade_no",payObject.getTradeNo());
        sendRequest(TradeMethodEnum.CANCEL,params);
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
        params.put("trade_no",payObject.getTradeNo());
        params.put("buyer_logon_id",payObject.getBuyerId());
        sendRequest(TradeMethodEnum.OFFLINE_QUERY,params);
    }

    /**
     *
     * @param tradeMethod 请求方法
     * @param dataMap 请求参数
     * @return
     */
    protected void sendRequest(TradeMethodEnum tradeMethod, Map dataMap) {
        Map<String,Object> param = new TreeMap();
        param.put("app_id", alipayConfig.getAppId());
        param.put("method", tradeMethod.getMethod());
        param.put("format", alipayConfig.getFormat());
        param.put("charset", alipayConfig.getCharset());
        param.put("sign_type", alipayConfig.getSignType());
        param.put("timestamp", DateUtil.date().toString());
        param.put("version", alipayConfig.getVersion());
        param.put("biz_content", JSONObject.toJSONString(dataMap));
        StringBuilder paramsStr = new StringBuilder();
        for (Map.Entry<String,Object> entry: param.entrySet()) {
            if (StrUtil.isNotBlank((String)entry.getValue())){
                paramsStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        String siginStr = paramsStr.substring(0,paramsStr.length()-1);
        try {
            siginStr = RSAUtils.rsa256Sign(siginStr,alipayConfig.getPrivateKey(),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        param.put("sign",siginStr);
        logger.info("alipay url:{} method:{},params:{}", alipayConfig.getUrl(),tradeMethod.getMethod(),JSONObject.toJSONString(param));
        HttpResponse response =  HttpUtil.createPost(alipayConfig.getUrl()).form(param).execute();
        if (response.isOk()){
            logger.info("alipay response:{}",response.body());
        }
    }

}
