package com.eugene.sumarry.rabbitmq.config;

import com.alibaba.fastjson.JSON;
import com.eugene.sumarry.rabbitmq.common.Constants;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.json.JsonParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setVirtualHost("/eugene");
        cachingConnectionFactory.setUsername("guest");
        cachingConnectionFactory.setPassword("guest");
        cachingConnectionFactory.setHost("192.168.111.145");
        cachingConnectionFactory.setPublisherConfirms(true);
        return cachingConnectionFactory;
    }

    /**
     * 创建一个bean，使用的消费者的消息确认机制默认为手动
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 配置rabbit每次发送指定数量的消息给对应的消费者
        factory.setPrefetchCount(2500);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            // 第一个参数: 是生产消息是传入的CorrelationData对象，里面维护了一个id，
            // 可以自定义取值来标识某些业务
            // 第二个参数: 判断消息有没有发成功
            // 第三个参数: 发生异常的原因，cause为异常的原因
            System.out.println("ack: " + ack);
            System.out.println("cause: " + cause);
            System.out.println("correlationData: " + correlationData);
        });

        // 允许失败回调
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            // 第一个参数: message -> 消息主体
            // 第二个参数: 发送失败错误码
            // 第三个参数: 发送失败错误信息
            // 第四个参数: 发送的交换机名字
            // 第五个参数: 发送消息的routingKey
            System.out.println(message);
            System.out.println(replyCode);
            System.out.println(replyText);
            System.out.println(exchange);
            System.out.println(routingKey);
        });

        rabbitTemplate.setMessageConverter(new MessageConverter() {
            @Override
            public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
                // 指定发送的消息类型为test/plain
                messageProperties.setContentType("text/plain");
                // 指定消息的编码格式
                messageProperties.setContentEncoding("UTF-8");
                Message message = new Message(JSON.toJSONBytes(object), messageProperties);
                return message;
            }

            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                return message;
            }
        });

        return rabbitTemplate;
    }

    @Bean
    public TopicExchange topicExchange() {
        Map<String, Object> map = new HashMap<>();
        map.put("alternate-exchange", "defaultExchange");
        return (TopicExchange) ExchangeBuilder.topicExchange(Constants.TOPIC_EXCHANGE).withArguments(map).build();
    }

    @Bean(Constants.ORDER_QUEUE_NAME)
    public Queue queue() {
        return new Queue(Constants.ORDER_QUEUE_NAME);
    }

    @Bean
    public Binding binding() {
        // 将队列和routing key绑定至exchange中
        return BindingBuilder.bind(queue()).to(topicExchange()).with(Constants.ORDER_SERVICE_MATCH_PREFIX_ROUTING_KEY);
    }

    @Bean
    public FanoutExchange defaultExchange() {
        return (FanoutExchange) ExchangeBuilder.fanoutExchange(Constants.DEFAULT_EXCHANGE).build();
    }

    @Bean
    public Binding defaultExchangeBinding() {
        return BindingBuilder.bind(queue()).to(defaultExchange());
    }

}
