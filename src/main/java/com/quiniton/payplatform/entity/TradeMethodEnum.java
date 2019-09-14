package com.quiniton.payplatform.entity;

public enum TradeMethodEnum {
    //统一收单交易关闭接口
    CLOSE("alipay.trade.close"),
    //统一收单交易创建接口
    CREATE("alipay.trade.create"),
    //统一收单交易支付接口
    PAY("alipay.trade.pay"),
    //统一收单交易退款接口
    REFUND("alipay.trade.refund"),
    //统一收单交易退款查询
    REFUND_QUERY("alipay.trade.fastpay.refund.query"),
    //统一收单退款页面接口
    PAGE_REFUND("alipay.trade.page.refund"),
    //手机网站支付接口2.0
    WAP_PAY("alipay.trade.wap.pay"),
    //统一收单线下交易预创建
    OFFLINE_PRECREATE("alipay.trade.precreate"),
    //统一收单交易撤销接口
    CANCEL("alipay.trade.cancel"),
    //统一收单交易结算接口
    SETTLE("alipay.trade.order.settle"),
    //统一收单线下交易查询
    OFFLINE_QUERY("alipay.trade.query"),
    //app支付接口2.0
    APP_PAY("alipay.trade.app.pay"),
    //统一收单下单并支付页面接口
    PAGE_PAY("alipay.trade.page.pay");

    private String method;

    TradeMethodEnum(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
