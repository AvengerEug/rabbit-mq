package com.eugene.sumarry.sbrabbitmq.common;

import com.eugene.sumarry.sbrabbitmq.dto.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CommonListenser {

    private static final Logger logger = LoggerFactory.getLogger(CommonListenser.class);

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 配置消费者监听器,
     * @RabbitListener注解中
     *   queues: 指定到哪个队列中消费消息
     *   containerFactory: 指定监听容器工厂,
     *     值为singleListenerContainer的原因: 在RabbitConfig中配置了监听容器(该容器不支持并发, 最高预处理一条消息, 具体可查看singleListenerContainer对象的创建)
     * @param message
     */
    @RabbitListener(queues = "${basic.info.mq.queue.name}", containerFactory = "singleListenerContainer")
    public void consumeMessage(@Payload byte[] message) {
        try {
            // 获取普通String类型消息
            logger.info("接收的字符串消息: " + new String(message, "UTF-8"));
            // 获取map消息
            //Map<String, Object> map = objectMapper.readValue(message, Map.class);
            //logger.info("接收的map消息, map: " + map );

            // 获取对象消息
            //User user = objectMapper.readValue(message, User.class);
            //logger.info("接收的对象消息, user: " + user);
        } catch (IOException e) {
            logger.error("接收消息发生异常: ", e);
        }
    }
}
