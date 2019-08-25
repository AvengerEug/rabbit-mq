package com.eugene.sumarry.sbrabbitmq.controller;

import com.eugene.sumarry.sbrabbitmq.response.BaseResponse;
import com.eugene.sumarry.sbrabbitmq.response.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "simple-mq-container")
public class SimpleMqContainerController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleMqContainerController.class);

    @Autowired
    private SimpleMessageListenerContainer simpleMessageListenerContainer;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${basic.info.simpleMq.exchange.name}")
    private String simpleContainerTopicExchangeName;

    @Value("${basic.info.simpleMq.routing.key.name}")
    private String simpleContainerTopicExchangeRoutingKeyName;

    /**
     * 事件产生源, 测试消息确认机制
     * @param msg
     * @return
     */
    @GetMapping(value = "/test")
    public BaseResponse test(@RequestParam(value = "msg") String msg) {
        rabbitTemplate.setExchange(simpleContainerTopicExchangeName);
        rabbitTemplate.setRoutingKey(simpleContainerTopicExchangeRoutingKeyName);

        Message message = MessageBuilder.withBody(msg.getBytes()).build();
        rabbitTemplate.send(message);

        return new BaseResponse(StatusCode.Success);
    }

}
