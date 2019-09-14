package com.quiniton.payplatform.constant;

public final class WechatPayRequestURL {

    public static final String CREATE_PAY = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String PAY_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";

    public static final String CLOSE_PAY = "https://api.mch.weixin.qq.com/pay/closeorder";

    public static final String REFUND =  "https://api.mch.weixin.qq.com/secapi/pay/refund";

    public static final String REFUND_QUERY = "https://api.mch.weixin.qq.com/pay/refundquery";

    public static final String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";

    public static final String MICRO_PAY = "https://api.mch.weixin.qq.com/pay/micropay";
}
