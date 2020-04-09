package com.eugene.sumarry.rabbitmq.controller;

import com.eugene.sumarry.rabbitmq.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Random;

@Controller
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void createOrder(Long currentUserId) {
        int orderId = new Random().nextInt( 100);
        logger.info("用户 {} 开始下单, 订单编号 {}", currentUserId, orderId);
        logger.debug("开始创建下订单消息");
        // 第一个参数: topic的名字
        // 第二个参数: 当前这条消息的key
        // 第三个参数: 传递的信息
        rabbitTemplate.convertAndSend(Constants.TOPIC_EXCHANGE, Constants.ORDER_CREATE_ROUTING_KEY, orderId);
        logger.info("下单成功");
    }
}
