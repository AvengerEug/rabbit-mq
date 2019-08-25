package com.eugene.sumarry.sbrabbitmq.config;

import com.eugene.sumarry.sbrabbitmq.listerner.SimpleMqListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RabbitmqConfig {

    private static final Logger log= LoggerFactory.getLogger(RabbitmqConfig.class);

    @Autowired
    private Environment env;

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private SimpleRabbitListenerContainerFactoryConfigurer factoryConfigurer;

    /**
     * 单一消费者
     * @return
     */
    @Bean(name = "singleListenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setTxSize(1);
        return factory;
    }

    /**
     * 多个消费者
     * @return
     */
    @Bean(name = "multiListenerContainer")
    public SimpleRabbitListenerContainerFactory multiListenerContainer(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factoryConfigurer.configure(factory,connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.NONE);
        factory.setConcurrentConsumers(env.getProperty("spring.rabbitmq.listerner.concurrency",int.class));
        factory.setMaxConcurrentConsumers(env.getProperty("spring.rabbitmq.listerner.max-concurrency",int.class));
        factory.setPrefetchCount(env.getProperty("spring.rabbitmq.listerner.prefetch",int.class));
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(){
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("消息发送成功:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
            }
        });
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
            }
        });
        return rabbitTemplate;
    }


    @Bean
    public DirectExchange basicDirectExchange() {
        return new DirectExchange(env.getProperty("basic.info.mq.exchange.name"), true, false);
    }

    @Bean
    public Queue basicQueue() {
        return new Queue(env.getProperty("basic.info.mq.queue.name"), true);
    }

    @Bean
    public Binding bindingQueueWithExchange() {
        return BindingBuilder.bind(basicQueue()).to(basicDirectExchange()).with(env.getProperty("basic.info.mq.routing.key.name"));
    }



    // 并发10个, 所以在rabbitmq中有10个channel相当于10个消费者
    @Value("${spring.rabbitmq.listener.simple.concurrency}")
    private Integer simpleConcurrency;

    @Value("${spring.rabbitmq.listener.simple.max-concurrency}")
    private Integer maxConcurrency;

    @Value("${spring.rabbitmq.listener.simple.prefetch}")
    private Integer prefetch;

    @Value("${basic.info.simpleMq.queue.name}")
    private String simpleContainerQueueName;

    @Value("${basic.info.simpleMq.exchange.name}")
    private String simpleContainerTopicExchangeName;

    @Value("${basic.info.simpleMq.routing.key.name}")
    private String simpleContainerTopicExchangeRoutingKeyName;

    @Autowired
    private SimpleMqListener simpleMqListener;

    /**
     * 配置simpleContainer的队列
     */
    @Bean(value = "simpleContainerQueue")
    public Queue simpleContainerQueue() {
        return new Queue(simpleContainerQueueName, true);
    }

    @Bean(value = "simpleContainerTopicExchange")
    public TopicExchange simpleContainerTopicExchange() {
        return new TopicExchange(simpleContainerTopicExchangeName, true, false);
    }

    /**
     * 将队列绑定到exchange上, 采用某种routing key
     * @return
     */
    @Bean
    public Binding bindingSimpleContainer() {
        return BindingBuilder.bind(simpleContainerQueue()).to(simpleContainerTopicExchange()).with(simpleContainerTopicExchangeRoutingKeyName);
    }

    /**
     * 配置高并发mq容器: SimpleMessageListenerContainer
     * 1. 配置并发
     * 2. 消息确认机制
     */
    @Bean(value = "simpleContainer")
    public SimpleMessageListenerContainer simpleContainer(@Qualifier(value = "simpleContainerQueue" ) Queue simpleContainerQueue) {
        SimpleMessageListenerContainer simpleContainer = new SimpleMessageListenerContainer();

        // 每一个mq容器都需要跟连接工厂进行绑定
        simpleContainer.setConnectionFactory(connectionFactory);

        // 并发的配置, 配置消费者的数量
        simpleContainer.setConcurrentConsumers(simpleConcurrency);
        simpleContainer.setMaxConcurrentConsumers(maxConcurrency);
        simpleContainer.setPrefetchCount(prefetch);

        // 配置消息确认机制: 手动 AcknowledgeMode: NONE, MANUAL, AUTH,
        simpleContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        simpleContainer.setQueues(simpleContainerQueue);
        simpleContainer.setMessageListener(simpleMqListener);

        return simpleContainer;
    }

}
