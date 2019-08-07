package com.eugene.sumarry.hellorabbitmq.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OneProducer {

    private static final String QUEUE_NAME = "rabbit:mq01:quene:eug";

    public static void main(String[] args) {

        try {
            // ConnectionFactory中默认使用了guest/guest的用户名和密码
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("127.0.0.1");

            Connection connection = factory.newConnection();
            // 消息发送端，接收端的连接渠道
            Channel channel = connection.createChannel();

            // 声明队列并将消息发送到队列中
            channel.queueDeclare(QUEUE_NAME, true, false, false,null);
            String message = "我的第一条消息-Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("生产者发送消息成功 --->");

            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
