package com.eugene.sumarry.hellorabbitmq.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OneProducer {

    private static final String QUEUE_NAME = "rabbit:mq01:queue:eug";

    public static void main(String[] args) {

        try {
            // ConnectionFactory中默认使用了guest/guest的用户名和密码
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.111.145");

            Connection connection = factory.newConnection();
            // 消息发送端，接收端的连接渠道
            Channel channel = connection.createChannel();

            // 声明队列并将消息发送到队列中
            // durable 队列持久化，当消息宕机了，重启了也还存在
            // exclusive 排他队列，只能当前这个链接这个队列能访问到它，其他的链接访问不到，所以一般设置为false
            // autoDelete 自动删除  => 所以exclusive和autoDelete配置使用是为了服务临时队列的
            // arguments 队列参数: 比如配置队列最大的消息数量
            channel.queueDeclare(QUEUE_NAME, true, false, false,null);
            String message = "我的第一条消息-Hello World!";

            // 发布一个消息，
            // exchange 若为空。则使用默认的交换机，其中绑定的key为队列的名称
            // routingKey 绑定这条消息的key
            // props:
            // byte[]: 消息内容的byte数组
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("生产者发送消息成功 --->" + message);

            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
