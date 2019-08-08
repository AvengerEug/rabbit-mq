package com.eugene.sumarry.fanoutexchange.producer;

import com.eugene.sumarry.fanoutexchange.constants.Constants;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class FanoutExchangeProducer {

    public static void main(String[] args) {
        // 1. 获取连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        try {
            // 2. 获取rabbitmq连接
            Connection connection = factory.newConnection();

            // 3. 创建渠道
            Channel channel = connection.createChannel();

            // 4. 绑定fanout交换机, 指定交换机名称和类别
            channel.exchangeDeclare(Constants.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            // 5. 发布消息至交换机, 第二个参数为routing-key, 不允许为null, 具体代码再AMQImpl.java line: 3197
            String message = "Fanout exchange message";
            System.out.println("生产者发送消息至交换机 -----> " + message);
            channel.basicPublish(Constants.EXCHANGE_NAME, "", null, message.getBytes());

            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
