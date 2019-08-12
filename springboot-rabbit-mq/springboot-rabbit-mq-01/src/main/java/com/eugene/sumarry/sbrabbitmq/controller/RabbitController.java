package com.eugene.sumarry.sbrabbitmq.controller;

import com.eugene.sumarry.sbrabbitmq.response.BaseResponse;
import com.eugene.sumarry.sbrabbitmq.response.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "rabbit-mq")
public class RabbitController {

    private static final Logger logger = LoggerFactory.getLogger(RabbitController.class);

    @Autowired
    private Environment env;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping(value = "/send-msg")
    public BaseResponse sendMsg(@RequestParam(value = "msg") String msg) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        try {
            logger.info("发送的消息: {} " + msg);
            rabbitTemplate.setExchange(env.getProperty("basic.info.mq.exchange.name"));
            rabbitTemplate.setRoutingKey(env.getProperty("basic.info.mq.routing.key.name"));

            Message message = MessageBuilder.withBody(msg.getBytes()).build();
            rabbitTemplate.send(message);

        } catch (Exception e) {
            logger.error("发送消息异常: {} " + e);
        }

        return baseResponse;
    }
}
