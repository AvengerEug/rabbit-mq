package com.eugene.sumarry.rabbitmq.controller;

import com.eugene.sumarry.rabbitmq.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

@Controller
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @RabbitListener(queues = Constants.ORDER_QUEUE_NAME)
    public void decrementCont(Message message) {
        logger.info("消费者收到{}队列的消息，消息如下: {}", Constants.ORDER_QUEUE_NAME, message);
    }
}
