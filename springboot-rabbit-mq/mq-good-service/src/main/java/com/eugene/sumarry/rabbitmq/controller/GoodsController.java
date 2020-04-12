package com.eugene.sumarry.rabbitmq.controller;

import com.alibaba.fastjson.JSON;
import com.eugene.sumarry.rabbitmq.common.Constants;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Map;


@Controller
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);


    @Autowired
    private Jedis jedis;

    private static final String REPEAT_MESSAGE_KEY = "repeatMessageKey";


    @RabbitListener(
            queues = Constants.ORDER_QUEUE_NAME,
            errorHandler = "messageErrorHandler",
            containerFactory = "simpleRabbitListenerContainerFactory"
    )
    public void decrementCont(
            Message message,
            String content,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(AmqpHeaders.CHANNEL) Channel channel) throws IOException {
        logger.info("消费者收到{}队列的消息, Message对象", Constants.ORDER_QUEUE_NAME, message);
        logger.warn("消息内容如下: {}", content);
        // 确认消费完成
        if (true) {
            channel.basicAck(deliveryTag, false);
        } else {
            // 批量撤回
            // 第一个参数: 消息对应的tag
            // 第二个参数: 是否批量撤回
            // 第三个参数: 是否重回队列
            channel.basicNack(deliveryTag, false, true);

            // 单条撤回
            // 第一个参数: 消息对应detag
            // 第二个参数: 是否重回队列
            //channel.basicReject(deliveryTag, true);

            // 这种情况下，
            // 1. 可以把消息存入db，项目中的定时任务来执行
            // 2. 获取使用多个消费者同时消费一个队列，在rabbitmq将消息分发给消费者时
            //    采用的时轮询机制
        }
        System.out.println("decrementCont");
    }

    @RabbitListener(
            queues = Constants.ORDER_QUEUE_NAME
    )
    public void decrementCont1(
            Message message,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(AmqpHeaders.CHANNEL) Channel channel) throws IOException {
        logger.info("消费者收到{}队列的消息，消息如下: {}", Constants.ORDER_QUEUE_NAME, message);
        // 确认消费完成
        channel.basicAck(deliveryTag, false);
        System.out.println("decrementCont1");
    }

    @RabbitListener(
            queues = Constants.PRE_FETCH_QUEUE_NAME,
            errorHandler = "messageErrorHandler",
            containerFactory = "simpleRabbitListenerContainerFactory"
    )
    public void preFetchMessage(
            Message message,
            String content,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(AmqpHeaders.CHANNEL) Channel channel) throws IOException {
        logger.info("消费者收到{}队列的消息, Message对象", Constants.ORDER_QUEUE_NAME, message);
        logger.warn("消息内容如下: {}", content);
        Map<String, Object> messageContent = JSON.parseObject(content, Map.class);
        Integer orderId = (Integer) messageContent.get("orderId");

        // 先从redis中获取当前的key，如果存在的话，则表示重复消费，此时把消息拒绝，并不返回队列
        if (jedis.sismember(REPEAT_MESSAGE_KEY, orderId + "")) {
            logger.info("订单id为{}的消息重复消费了", orderId);
            channel.basicReject(deliveryTag, false);
            return;
        }

        // 处理正常逻辑，
        logger.info("处理正常逻辑");
        boolean isSuccess = true;

        if (isSuccess) {
            // 正常逻辑处理完毕后，将能唯一标识消息的变量存入redis中，这里不推荐deliveryId，
            // 因为消费者每次消费的时候deliveryId是递增的。当重启消费者时，拿到的deliveryId又是从0开始的
            // 推荐用自己的业务id来标识
            Long result = jedis.sadd(REPEAT_MESSAGE_KEY, orderId + "");
            if (result == 1L) {
                logger.info("订单id为{}的消息已消费完成并入redis.", orderId);

                // 需要定义确认消息的机制: TODO
                // 可能出现的情况:
                //   1. 消费者预取的消息 等于 2500
                //   2. 消费者预取的消息 小于 2500
                //   3. 若使用redis中已经消费的消息count来确认的话，也不行，若项目是集群部署的话，使用的key是同一个
                //      也就是会出现多个消费者往同一个key中set已经消费的队列
                // 解决方案:
                //   1. 每消费一条消息就确认一次 ---- 会影响效率，每次确认都会有网络资源的原因影响
                //   2. 最好的解决方案是批量确认，可是要什么时候才批量确认呢？
                //     目前没想到好的解决方案，暂时用一条一条确认的机制
                channel.basicAck(deliveryTag, false);
                logger.info("所有的消息已消费完成，需要模拟消费途中，断开连接，将消息重回队列的情况");
            } else {
                // result == 0 的情况，这种情况应该不会发生，因为在上面校验key是否存在时就return了
                // Nothing to do
            }
        } else {

            // 消息处理过程中失败了，此时可以进行消息持久化，或者将消息退回给队列
            // TODO 但要注意: 如果这么设置的话，有可能会出现死循环
            channel.basicReject(deliveryTag, true);
        }



    }
}
