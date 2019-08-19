package com.eugene.sumarry.sbrabbitmq.listerner.event;

import org.springframework.context.ApplicationEvent;

/**
 * 类似于 Message
 * 相当于-Rabbitmq的message-一串二进制数据流
 */
public class PushOrderRecordEvent extends ApplicationEvent{

    private String orderNo;

    private String orderType;

    public PushOrderRecordEvent(Object source, String orderNo, String orderType) {
        super(source);
        this.orderNo = orderNo;
        this.orderType = orderType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "PushOrderRecordEvent{" +
                "orderNo='" + orderNo + '\'' +
                ", orderType='" + orderType + '\'' +
                '}';
    }
}
