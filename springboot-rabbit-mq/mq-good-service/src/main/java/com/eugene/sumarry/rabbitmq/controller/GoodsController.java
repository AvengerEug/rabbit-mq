package com.eugene.sumarry.rabbitmq.controller;

import com.eugene.sumarry.rabbitmq.common.Constants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;

import java.io.IOException;


@Controller
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @RabbitListener(
            queues = Constants.ORDER_QUEUE_NAME,
            errorHandler = "messageErrorHandler",
            containerFactory = "simpleRabbitListenerContainerFactory"
    )
    public void decrementCont(
            Message message,
            String content,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(AmqpHeaders.CHANNEL) Channel channel) throws IOException {
        logger.info("消费者收到{}队列的消息, Message对象", Constants.ORDER_QUEUE_NAME, message);
        logger.warn("消息内容如下: {}", content);
        // 确认消费完成
        if (true) {
            channel.basicAck(deliveryTag, false);
        } else {
            // 批量撤回
            // 第一个参数: 消息对应的tag
            // 第二个参数: 是否批量撤回
            // 第三个参数: 是否重回队列
            channel.basicNack(deliveryTag, false, true);

            // 单条撤回
            // 第一个参数: 消息对应detag
            // 第二个参数: 是否重回队列
            //channel.basicReject(deliveryTag, true);

            // 这种情况下，
            // 1. 可以把消息存入db，项目中的定时任务来执行
            // 2. 获取使用多个消费者同时消费一个队列，在rabbitmq将消息分发给消费者时
            //    采用的时轮询机制
        }
        System.out.println("decrementCont");
    }

    @RabbitListener(
            queues = Constants.ORDER_QUEUE_NAME
    )
    public void decrementCont1(
            Message message,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(AmqpHeaders.CHANNEL) Channel channel) throws IOException {
        logger.info("消费者收到{}队列的消息，消息如下: {}", Constants.ORDER_QUEUE_NAME, message);
        // 确认消费完成
        channel.basicAck(deliveryTag, false);
        System.out.println("decrementCont1");
    }
}
