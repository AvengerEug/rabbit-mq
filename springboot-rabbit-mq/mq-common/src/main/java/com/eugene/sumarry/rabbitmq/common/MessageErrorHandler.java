package com.eugene.sumarry.rabbitmq.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

@Component
public class MessageErrorHandler implements RabbitListenerErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageErrorHandler.class);

    @Override
    public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException exception) throws Exception {
        logger.warn("处理消息异常, 异常消息为: {}, 异常信息为: ", amqpMessage, exception);
        logger.info("可以在此处对消息进行持久化存入db，并使用job定时去消费");

        return null;
    }
}
