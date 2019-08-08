package com.eugene.sumarry.fanoutexchange.consumer;

import com.eugene.sumarry.fanoutexchange.constants.Constants;
import com.rabbitmq.client.*;

import java.io.IOException;

public class MultiConsumerTwo {

    public static void main(String[] args) {
        try {
            // 1. 获取连接工厂
            ConnectionFactory factory = new ConnectionFactory();

            // 2. 获取连接
            Connection connection = factory.newConnection();

            // 3. 创建渠道
            Channel channel = connection.createChannel();

            // 4. 定义交换机
            channel.exchangeDeclare(Constants.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            // 5. 定义队列
            channel.queueDeclare(Constants.QUEUE_NAME_02, true, false, false, null);

            // 6. 将队列(第一个参数)添加至交换机中(第二个参数), routing-key不允许为null, 具体代码在AMQImpl.java  line: 1708
            channel.queueBind(Constants.QUEUE_NAME_02, Constants.EXCHANGE_NAME, "");

            // 7. 创建消费者对象并重写分发方法
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("MultiConsumerTwo 分发了一条消息, 消息内容: " + new String(body));
                }
            };

            // 8. 通过channel消费消息, 需要指定从哪个队列中去消费
            channel.basicConsume(Constants.QUEUE_NAME_02, false, consumer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
