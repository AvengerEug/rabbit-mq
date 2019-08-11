package com.eugene.sumarry.directexchange.consumer;

import com.eugene.sumarry.directexchange.constants.Constants;
import com.rabbitmq.client.*;

import java.io.IOException;

public class SuccessQueueConsumer {

    public static void main(String[] args) {
        try {
            // 1. 获取连接工厂
            ConnectionFactory factory = new ConnectionFactory();

            // 2. 获取连接对象
            Connection connection = factory.newConnection();

            // 3. 创建渠道
            Channel channel = connection.createChannel();

            // 4. 绑定成功队列
            channel.queueBind(Constants.SUCCESS_QUEUE, Constants.DIRECT_EXCHANGE_NAME, Constants.SUCCESS_ROUTING_KEY);

            // 5. 创建消费者
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("成功队列消费消息成功, 内容: " + new String(body, "UTF-8"));
                }
            };

            // 6. 消费指定队列的消息(指定队列，确认方式，消费者)
            channel.basicConsume(Constants.SUCCESS_QUEUE, true, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
