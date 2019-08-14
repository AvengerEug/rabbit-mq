package com.eugene.sumarry.sbrabbitmq.controller;

import com.eugene.sumarry.sbrabbitmq.listener.event.PushOrderRecordEvent;
import com.eugene.sumarry.sbrabbitmq.response.BaseResponse;
import com.eugene.sumarry.sbrabbitmq.response.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "order")
public class OrderRecordController {

    private static final Logger log = LoggerFactory.getLogger(OrderRecordController.class);

    //TODO：类似于RabbitTemplate
    @Autowired
    private ApplicationEventPublisher publisher;


    /**
     * 下单
     * @return
     */
    @RequestMapping(value = "/push", method = RequestMethod.GET)
    public BaseResponse pushOrder(){
        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            log.info("当前线程名: " + Thread.currentThread().getName());
            PushOrderRecordEvent event = new PushOrderRecordEvent(this, "orderNo_20180821001", "物流12");
            publisher.publishEvent(event);

        } catch (Exception e) {
            log.error("下单发生异常：", e.fillInStackTrace());
        }

        return response;
    }

}










