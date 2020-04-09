package com.eugene.sumarry.hellorabbitmq.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class OneConsumer {

    private static final String QUEUE_NAME = "rabbit:mq01:queue:eug";

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("192.168.111.145");

            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("消费者收到消息 ----> " + message);

                    // multiple: 是否要批量确认
                    // 待确认: 是否一定要跟exchange、queue绑定好的channel才能进行消息确认？
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            };

            // 第二个参数为, 告知队列这条消息已经被我消费了, 防止下次启动消费者时队列又会将此消息分发给我
            // 若设置为false可能会出现消息我正在消费，但是业务出异常了最终导致消息消费完了，但是逻辑没走完
            // 最好是设置为true由手动确认消费完毕
            // 但是手动确认也有可能还未执行到确认逻辑，也抛异常了,
            // 所以此时可以把消息存入DB，由一个job定时去消费DB中的消息，消费完后再跟mq的服务器确认
            // 服务器消费完毕(因为autoAck为true的话，在mq的服务器中，那条消息只能由指定的channel
            // 来确认消费)
            channel.basicConsume(QUEUE_NAME, false, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
