package com.eugene.sumarry.topicexchange.consumer;

import com.eugene.sumarry.topicexchange.constants.Constants;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者1
 */
public class MultiConsumerOne {

    public static void main(String[] args) {

        try {
            // 1. 获取连接工厂
            ConnectionFactory factory = new ConnectionFactory();

            // 2. 获取连接
            Connection connection = factory.newConnection();

            // 3. 创建渠道
            Channel channel = connection.createChannel();

            // 4. 定义topic交换机
            channel.exchangeDeclare(Constants.TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);

            // 5. 定义两个队列
            channel.queueDeclare(Constants.QUEUE_ONE, true, false, false, null);

            // 6. 绑定交换机和队列
            channel.queueBind(Constants.QUEUE_ONE, Constants.TOPIC_EXCHANGE, Constants.ORANGE_ANIMALS_PATTEN, null);

            // 7. 创建消费者对象并重写分发方法
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("MultiConsumerOne 消费了一条消息, 内容: " + new String(body));
                }
            };

            // 8. 通过channel消费消息, 需要指定从哪个队列中去消费
            channel.basicConsume(Constants.QUEUE_ONE, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
