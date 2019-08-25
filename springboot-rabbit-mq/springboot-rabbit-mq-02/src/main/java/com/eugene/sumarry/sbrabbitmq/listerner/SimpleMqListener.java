package com.eugene.sumarry.sbrabbitmq.listerner;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class SimpleMqListener implements ChannelAwareMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(SimpleMqListener.class);


    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        Long deliveryTag = null;
        try {
            String msg = new String(message.getBody(), "UTF-8");

            logger.info("简单消息监听到消息: " + msg);
            deliveryTag = message.getMessageProperties().getDeliveryTag();

            int i = 1/0;

            // 确认消息, 告知mq我处理了, mq则会删除此条信息, 若不确认时, 此条消息会一直保留在mq服务器(不管监听器有没有处理完)
            channel.basicAck(deliveryTag, true);

        } catch (Exception e) {
            logger.error("简单消息监听发生异常: ", e.fillInStackTrace());
            // 发生异常时, 一般会使用一种策略, 比如重入了多少次就把它删除, 因为一直重复重入, 说明这条信息确实有问题

            // 将消息
            channel.basicRecover();

            // 第二个参数, 不需要重入队列, 那么会通知mq在自己的服务器中将此条信息删除
            // channel.basicReject(deliveryTag, false);
        }



    }
}
