package com.eugene.sumarry.rabbitmq.config;

import com.eugene.sumarry.rabbitmq.common.Constants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setVirtualHost("/eugene");
        cachingConnectionFactory.setUsername("guest");
        cachingConnectionFactory.setPassword("guest");
        cachingConnectionFactory.setHost("192.168.213.128");
        return cachingConnectionFactory;
    }

    @Bean(Constants.ORDER_SERVICE_RABBIT_MQ)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        return rabbitTemplate;
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(Constants.TOPIC_EXCHANGE);
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
}
