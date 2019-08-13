package com.eugene.sumarry.sbrabbitmq.controller;

import com.eugene.sumarry.sbrabbitmq.dto.User;
import com.eugene.sumarry.sbrabbitmq.response.BaseResponse;
import com.eugene.sumarry.sbrabbitmq.response.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "rabbit-mq")
public class RabbitController {

    private static final Logger logger = LoggerFactory.getLogger(RabbitController.class);

    @Autowired
    private Environment env;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 发送简单消息
     * @param msg
     * @return
     */
    @GetMapping(value = "/send-msg")
    public BaseResponse sendMsg(@RequestParam(value = "msg") String msg) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        try {
            logger.info("发送的消息: {} " + msg);
            rabbitTemplate.setExchange(env.getProperty("basic.info.mq.exchange.name"));
            rabbitTemplate.setRoutingKey(env.getProperty("basic.info.mq.routing.key.name"));

            // 1. 简单消息的发送
//            Message message = MessageBuilder.withBody(msg.getBytes()).build();
//            rabbitTemplate.send(message);

            // 2. 使用消息转换器发送消息
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

            Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(msg)).build();
            rabbitTemplate.convertAndSend(message);

        } catch (Exception e) {
            logger.error("发送消息异常: {} " + e);
        }

        return baseResponse;
    }


    /**
     * 发送对象消息
     * @param user
     * @return
     */
    @PostMapping(value = "/object/send-msg")
    public BaseResponse sendObjectMsg(@RequestBody User user) {
        BaseResponse baseResponse = new BaseResponse(StatusCode.Success);
        try {
            logger.info("发送的对象消息: {} " + user);
            rabbitTemplate.setExchange(env.getProperty("basic.info.mq.exchange.name"));
            rabbitTemplate.setRoutingKey(env.getProperty("basic.info.mq.routing.key.name"));
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

            Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(user)).build();
            rabbitTemplate.convertAndSend(message);

        } catch (Exception e) {
            logger.error("发送对象消息发生异常: {} " + e);
        }

        return baseResponse;
    }
}
