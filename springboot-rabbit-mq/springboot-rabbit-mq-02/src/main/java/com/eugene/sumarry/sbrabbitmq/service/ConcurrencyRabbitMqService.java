package com.eugene.sumarry.sbrabbitmq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 将消息队列写进rabbitmq
 */
@Component
public class ConcurrencyRabbitMqService {

    private static final Logger logger = LoggerFactory.getLogger(ConcurrencyRabbitMqService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${basic.info.mq.routing.key.name}")
    private String routingKey;

    @Value("${basic.info.mq.exchange.name}")
    private String exchangeName;

    @Value("${basic.info.mq.queue.name}")
    private String queueName;

    /**
     * 发送手机号至rabbitmq, 所以可以不使用消息转换器
     * @param mobile
     */
    public void sendMessage(String mobile) {

        // 1. 将消息发送至交换机, 只需要绑定交换机和routingkey即可
        try {
            rabbitTemplate.setRoutingKey(routingKey);
            rabbitTemplate.setExchange(exchangeName);

            // 发送消息并指定发送模式为持久化
            Message message = MessageBuilder.withBody(mobile.getBytes()).setDeliveryMode(MessageDeliveryMode.PERSISTENT).build();

            rabbitTemplate.send(message);
        } catch (AmqpException e) {
            logger.error("发送消息失败: ", e);
        }
    }

}
