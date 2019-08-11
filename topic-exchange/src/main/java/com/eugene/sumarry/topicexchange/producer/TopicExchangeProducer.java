package com.eugene.sumarry.topicexchange.producer;

import com.eugene.sumarry.topicexchange.constants.Constants;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sun.org.apache.bcel.internal.classfile.Constant;

public class TopicExchangeProducer {

    public static void main(String[] args) {
        try {
            // 1. 获取连接工厂
            ConnectionFactory factory = new ConnectionFactory();

            // 2. 获取连接
            Connection connection = factory.newConnection();

            // 3. 创建渠道
            Channel channel = connection.createChannel();

            // 4. 发布消息至交换机, 分别指定不同的routing-key

            // 发送routing-key为 quick.orange.rabbit 符合 ORANGE_ANIMALS_PATTEN 和 RABBIT_PATTEN两个适配规则, 所以队列一和队列二都会收到消息
            //channel.basicPublish(Constants.TOPIC_EXCHANGE, "quick.orange.rabbit", null, ("发送消息至topic交换机, routingKey为: quick.orange.rabbit").getBytes());

            // 发送routing-key为 lazy.orange.elephant 符合 ORANGE_ANIMALS_PATTEN 和 LAZY_ANIMALS两个适配器规则, 所以队列一和队列二都会收到消息
            //channel.basicPublish(Constants.TOPIC_EXCHANGE, "lazy.orange.elephant", null, ("发送消息至topic交换机, routingKey为: lazy.orange.elephant").getBytes());

            // 发送routing-key为 lazy.orange.elephant 符合 ORANGE_ANIMALS_PATTEN 和 LAZY_ANIMALS规则, 所以队列一和队列二都会收到消息
            //channel.basicPublish(Constants.TOPIC_EXCHANGE, "lazy.orange.elephant", null, ("发送消息至topic交换机, routingKey为: lazy.orange.elephant").getBytes());

            // 发送routing-key为 lazy.brown.fox 符合 LAZY_ANIMALS规则, 只有队列二会收到消息
            //channel.basicPublish(Constants.TOPIC_EXCHANGE, "lazy.brown.fox", null, ("发送消息至topic交换机, routingKey为: lazy.brown.fox").getBytes());

            // 发送routing-key为 lazy.pink.rabbit 符合 RABBIT_PATTEN 和 LAZY_ANIMALS规则, 虽然他们都是映射到队列2, 但只能接收到一次, 要如何确定接收的是哪一条呢？
            channel.basicPublish(Constants.TOPIC_EXCHANGE, "lazy.pink.rabbit", null, ("发送消息至topic交换机, routingKey为: lazy.pink.rabbit").getBytes());

            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
