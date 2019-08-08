package com.eugene.sumarry.hellorabbitmq.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OneConsumer {

    private static final String QUEUE_NAME = "rabbit:mq01:queue:eug";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("消费者收到消息 ----> " + message);
                }
            };

            // 第二个参数为, 告知队列这条消息已经被我消费了, 防止下次启动消费者时队列又会将此消息分发给我
            channel.basicConsume(QUEUE_NAME, false, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
