package com.eugene.sumarry.directexchange.consumer;

import com.eugene.sumarry.directexchange.constants.Constants;
import com.rabbitmq.client.*;

import java.io.IOException;

public class FailedQueueConsumer {

    public static void main(String[] args) {
        try {
            // 1. 获取连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            // 2. 获取连接
            Connection connection = factory.newConnection();
            // 3. 创建渠道
            Channel channel = connection.createChannel();
            // 4. 绑定消费的队列(指定队列名，交换机名，路由名)
            channel.queueBind(Constants.FAILED_QUEUE, Constants.DIRECT_EXCHANGE_NAME, Constants.FAILED_ROUTING_KEY);
            // 5. 创建消费者
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("失败队列的信息, 内容: " + new String(body, "UTF-8"));
                }
            };
            // 6. 消费消息(指定消费的队列，确认模式, 消费者对象)
            channel.basicConsume(Constants.FAILED_QUEUE, true, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
