package com.eugene.sumarry.directexchange.producer;

import com.eugene.sumarry.directexchange.constants.Constants;
import com.rabbitmq.client.*;

/**
 * 生产者: 绑定directExchange和key
 */
public class Producer {

    public static void main(String[] args) {
        try {
            // 1. 获取连接工厂
            ConnectionFactory factory = new ConnectionFactory();

            // 2. 获取连接对象
            Connection connection = factory.newConnection();

            // 3. 获取渠道
            Channel channel = connection.createChannel();

            // 4. 定义directExchange交换机
            channel.exchangeDeclare(Constants.DIRECT_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

            // 5. 定义成功、失败队列AS
            channel.queueDeclare(Constants.SUCCESS_QUEUE, true, false, false, null);
            channel.queueDeclare(Constants.FAILED_QUEUE, true, false, false, null);

            // 6. 绑定队列成功/失败队列、交换机、routingKey
            channel.queueBind(Constants.SUCCESS_QUEUE, Constants.DIRECT_EXCHANGE_NAME, Constants.SUCCESS_ROUTING_KEY);
            channel.queueBind(Constants.FAILED_QUEUE, Constants.DIRECT_EXCHANGE_NAME, Constants.FAILED_ROUTING_KEY);

            // 7. 分别发送消息至成功失败队列
            String successMsg1 = "这是发送给成功队列的信息1";
            String successMsg2 = "这是发送给成功队列的信息2";
            String failedMsg1 = "这是发送给失败队列的信息1";

            channel.basicPublish(Constants.DIRECT_EXCHANGE_NAME, Constants.SUCCESS_ROUTING_KEY, null,successMsg1.getBytes());
            channel.basicPublish(Constants.DIRECT_EXCHANGE_NAME, Constants.SUCCESS_ROUTING_KEY, null,successMsg2.getBytes());
            channel.basicPublish(Constants.DIRECT_EXCHANGE_NAME, Constants.FAILED_ROUTING_KEY, null,failedMsg1.getBytes());

            channel.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
