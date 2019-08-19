package com.eugene.sumarry.sbrabbitmq.listerner;

import com.eugene.sumarry.sbrabbitmq.service.ConcurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * 消息监听器, 步骤:
 * 1. 创建实例至IOC容器
 * 2. 创建监听器并与队列绑定
 */
@Component
public class RabbitMqListerner {


    private static final Logger logger = LoggerFactory.getLogger(RabbitListener.class);

    @Autowired
    private ConcurrencyService concurrencyService;

    @RabbitListener(containerFactory = "singleListenerContainer", queues = "${basic.info.mq.queue.name}")
    public void handleRabbitMqMessage(@Payload byte[] message) {
        try {
            logger.info("开始处理抢单消息记录");
            concurrencyService.manageRobbing(new String(message, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("处理队列消息失败, ", e);
            e.printStackTrace();
        }
    }
}
