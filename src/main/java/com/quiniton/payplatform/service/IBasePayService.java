package com.quiniton.payplatform.service;

public interface IBasePayService<T> {

    /**
     * 统一收单交易创建接口
     * @param t
     */
   void create(T t);

    /**
     * 统一收单交易关闭接口
     * @param t
     */
   void close(T t);

    /**
     * 统一收单交易支付接口
     */
   void pay(T t);

    /**
     * 统一收单交易退款接口
     * @param t
     */
   void refund(T t);

    /**
     * 统一收单交易退款查询
     */
    void refundQuery(T t);

    /**
     * 统一收单交易撤销接口
     * @param t
     */
    void cancel(T t);

}
