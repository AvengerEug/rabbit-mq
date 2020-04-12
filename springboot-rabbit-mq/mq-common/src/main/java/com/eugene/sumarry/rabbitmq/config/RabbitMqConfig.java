package com.eugene.sumarry.rabbitmq.config;

import com.alibaba.fastjson.JSON;
import com.eugene.sumarry.rabbitmq.common.Constants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

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

        // 自己实现了一个消息转换器
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
        map.put("alternate-exchange", Constants.DEFAULT_EXCHANGE);
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

    /**
     * 备用交换机配置，因要测试消息预取，所以注释此段代码
     * @return
     */
    @Bean
    public FanoutExchange defaultExchange() {
        return (FanoutExchange) ExchangeBuilder.fanoutExchange(Constants.DEFAULT_EXCHANGE).build();
    }

    @Bean
    public Binding defaultExchangeBinding() {
        return BindingBuilder.bind(queue()).to(defaultExchange());
    }

    // ---------------------------预取消息相关---------------------------
    @Bean
    public Jedis jedis() {
        return new Jedis("192.168.111.145", 6379);
    }

    /**
     * 新建一个预取消息的topic交换机
     * @return
     */
    @Bean
    public TopicExchange preFetchTopicExchange() {
        return (TopicExchange) ExchangeBuilder.topicExchange(Constants.PRE_FETCH_EXCHANGE).build();
    }

    /**
     * 新建一个预取消息对应的队列
     * @return
     */
    @Bean
    public Queue preFetchQueue() {
        return QueueBuilder.durable(Constants.PRE_FETCH_QUEUE_NAME).build();
    }

    /**
     * 将预取消息队列与交换机进行绑定，并且监听的routingKey为preFetchRoutingKey
     * @return
     */
    @Bean
    public Binding preFetchBinding() {
        return BindingBuilder.bind(preFetchQueue()).to(preFetchTopicExchange()).with(Constants.PRE_FETCH_ROUTING_KEY);
    }

    // -------------------死信队列相关

    /**
     * 死信队列: direct类型的交换机
     * @return
     */
    @Bean
    public DirectExchange basicQueueDeadLatterExchange() {
        return (DirectExchange) ExchangeBuilder.directExchange(Constants.BASIC_QUEUE_DEAD_LETTER_EXCHANGE).build();
    }

    /**
     * 死信队列: 与死信交换机绑定的队列
     * @return
     */
    @Bean
    public Queue basicQueueDeadLetterQueue() {
        return QueueBuilder.durable(Constants.BASIC_QUEUE_DEAD_LETTER_QUEUE).build();
    }

    /**
     * 绑定死信交换机与队列，并队列监听的routingKey为preFetchDeadLetterRoutingKey
     * @return
     */
    @Bean
    public Binding basicQueueDeadLetterBinding() {
        return BindingBuilder.bind(basicQueueDeadLetterQueue()).to(basicQueueDeadLatterExchange()).with(Constants.BASIC_QUEUE_DEAD_LETTER_ROUTING_KEY);
    }

    @Bean
    public Queue basicQueue() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", Constants.BASIC_QUEUE_DEAD_LETTER_EXCHANGE);
        params.put("x-dead-letter-routing-key", Constants.BASIC_QUEUE_DEAD_LETTER_ROUTING_KEY);
        return QueueBuilder.durable(Constants.BASIC_QUEUE).withArguments(params).build();
    }

    @Bean
    public TopicExchange basicExchange() {
        return (TopicExchange) ExchangeBuilder.topicExchange(Constants.BASIC_QUEUE_EXCHANGE).build();
    }

    @Bean
    public Binding basicBinding() {
        return BindingBuilder.bind(basicQueue()).to(basicExchange()).with(Constants.BASIC_QUEUE_ROUTING_KEY);
    }


}
