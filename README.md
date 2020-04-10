#### rabbitmq系列, 从初识到实战

## 一、rabbitmq安装
* 采用的docker化安装，参照官网提供的如下命令:

  ```shell
  # 使用的是3.x版本的rabbitmq
  docker run -it -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
  ```

## 二、Exchange类型

### 2.1默认Exchange

* 默认的Exchange为`Direct`，当我们不显示指定**exchange**, 通常会使用rabbitmq默认的**exchange**，名字叫**amq.gen.xxxxxxxxxxxx**, 类型为**Direct**。 我们可能很疑惑，**Direct**不是需要指定`Routing key`吗？是的，我们需要指定**队列的名字**为`Routing key`。 所以默认的**exchange**需要我们把队列名字和`Routing key`起相同的名字，才能被绑定队列中的消费者进行消费

### 2.2 Direct

* 上面说了，rabbitmq默认的exchange就是Direct。**Direct**的特点就是只将消息生产至与exchange绑定的队列中。即需要使用`channel.queueBind`方法将队列和key进行绑定。当生产者发布了对应key的消息的时候，就会把消息生产至队列中去

### 2.3 Fanout

* fanout指定只要队列与Exchange绑定了，在生产消息的时候就会往所有绑定的队列中生产

### 2.4 Topic

* topic是Direct的升级版，支持了模糊匹配。匹配符为`#`和`*`。 `#`代表多个，`*`代表一个。比如有两个队列绑定了topic的exchange。其中队列1绑定的Routing key为`eug.test.#`. 队列2绑定的Routing key为`eug.test.*`。

  那么假设，生产者生产了两条消息，Routing key分别为: `eug.test.1.2`和`eug.test.1`。那么队列1中可以添加`eug.test.1.2`和`eug.test.1`这两个消息。而队列2只能添加`eug.test.1`消息

### 2.5 注意

* 所有的`Routing Key`必须要使用`.`隔开。是Rabbit mq的规范。

## 三、Spring Boot集成rabbit mq流程

### 3.1 创建队列 queue

* 直接创建一个`org.springframework.amqp.core.Queue`类型的bean即可

### 3.2 创建交换机exchange

* 直接创建一个对应类型的exchange即可。

  ```txt
  eg: spring-amqp jar包中包含了如下exchange:
  1. org.springframework.amqp.core.TopicExchange
  2. org.springframework.amqp.core.DirectExchange
  3. org.springframework.amqp.core.FanoutExchange
  4. org.springframework.amqp.core.HeadersExchange
  ```

  或者使用`ExchangeBuilder`也可以创建一个exchange，采用build语法, 如下:

  ```java
  (TopicExchange) ExchangeBuilder.topicExchange("topicExchangeName").withArgument("参数key", "value").build();
  ```

### 3.3 将绑定交换机、队列和routing key

* 使用提供的`Binding`对象即可，如下:

  ```java
  BindingBuilder.bind(绑定的队列).to(交换机).with(绑定的key);
  // 大致的语法就是将队列绑定到交换机中并监听具体的key
  ```

### 3.4 RabbitTemplate

* 在springboot集成rabbit mq的过程中，**RabbitTemplate**的主要作用就是和rabbit mq链接，后续将使用它来发送消息。具体语法如下:

  ```java
  rabbitTemplate.convertAndSend("topic名字", "routingKey", "传递的消息");
  // 通过这行代码，rabbitTemplate就能知道将消息发送到哪个交换机上，然后交换机就能根据自己的特性，将routingKey将消息发送给符合条件的队列
  ```

### 3.5 使用@RabbitListener指定消费者手动确认消息

* 注解添加listenter参数，eg如下:

  ```java
  /**
   * messageErrorHandler为一个实现了RabbitListenerErrorHandler接口的消息发生异常的处理器的名字(是一个bean)
   * simpleRabbitListenerContainerFactory是一个实现了RabbitListenerContainerFactory接口的消费者容器工厂
   * (它是一个bean，并且内部设置了消息确认模式为手动
  **/
  @RabbitListener(
      queues = Constants.ORDER_QUEUE_NAME,
      errorHandler = "messageErrorHandler",
      containerFactory = "simpleRabbitListenerContainerFactory"
  )	
  ```

* name为messageErrorHandler的bean

  ```java
  @Component
  public class MessageErrorHandler implements RabbitListenerErrorHandler {
  
      private static final Logger logger = LoggerFactory.getLogger(MessageErrorHandler.class);
  
      @Override
      public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException exception) throws Exception {
          logger.warn("处理消息异常, 异常消息为: {}, 异常信息为: ", amqpMessage, exception);
          logger.info("可以在此处对消息进行持久化存入db，并使用job定时去消费");
  
          return null;
      }
  }
  ```

* name为

  ```java
  @Bean
  public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory() {
      SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
      factory.setConnectionFactory(connectionFactory());
      factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
      return factory;
  }
  ```

### 3.6 注意事项

* rabbitTemplate能够直接与rabbit服务端进行交互，直接使用**convertAndSend**方法就能实现消息发送

* 使用`@RabbitListener`注解即可实现消息消费，但要指定消费的队列。

  ```txt
  这里有点想吐槽，它的@RabbitListener注解压根没有@Inherited修饰，都无法对它进行扩展。
  ```

## 四、引入消息中间件可能出现的问题

### 4.1 发送消息时，消息中间件挂了，导致消息没有正常发送

* 有可能用户在下单完成后，触发了发送消息的步骤。但是有可能在刚好发消息的时候，消息中间件挂了，导致消息没有发送出去

* 可能出现的问题方向：

  1. 要确认消息发送到交换机 过程中失败(eg: 消息发送的过程中，项目挂了或者rabbitmq挂了)
  2. 交换机将消息发送至队列 过程中失败(eg: message对应的key没有队列能绑定)

* 解决方案: 

  1. 针对方向1：在将消息发送至交换机时，rabbit mq会进行一次回调  ---- `发送方确认`
  2. 针对方向2：在交换机将消息推送给队列失败时，rabbit mq也会进行回调  ---- `失败回调`

* 实践: 

  1. 针对方向1：

     ```txt
     1. 连接工厂要开启发送方确认模式: cachingConnectionFactory.setPublisherConfirms(true)
     2. rabbitmqTemplate提供发送消息的回调: 
     	rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
             // 第一个参数: 是生产消息是传入的CorrelationData对象，里面维护了一个id，可以自定义取值来标识某些业务
             // 第二个参数: 判断消息有没有发成功
             // 第三个参数: 发生异常的原因，cause为异常的原因
             System.out.println("123");
         });
     ```

  2. 针对方向2:

     ```txt
     1. 同上，需要设置发送方确认模式
     2. 设置允许失败回调: rabbitTemplate.setMandatory(true);
     3. 设置失败回调函数:
         rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
             // 第一个参数: message -> 消息主体
             // 第二个参数:
             // 第三个参数:
             // 第四个参数: 发送的交换机名字
             // 第五个参数: 发送消息的routingKey
             System.out.println(message);
             System.out.println(replyCode);
             System.out.println(replyText);
             System.out.println(exchange);
             System.out.println(routingKey);
         });
     ```

* 综上所述，一个消息发生成功需要这两个回调中都没发生异常(发送成功的情况下，第二个方向中的回调不会进入)

### 4.2 spring-amqp中自带的消息转换器

* 在`spring-amqp`中，spring会使用反射调用@RabbitListener注解标识的方法。当我们的参数中存在一个String类型的参数时，spring会认为这是想要注入的消息内容。最后会自己执行一套逻辑来注入这个参数(**使用消息转换器**)，具体逻辑如下: 

  ```java
  // SimpleMessageConverter.java
  public Object fromMessage(Message message) throws MessageConversionException {
      Object content = null;
      // 拿到消息的MessageProperties对象
      MessageProperties properties = message.getMessageProperties();
      if (properties != null) {
          String contentType = properties.getContentType();
          // 判断消息的ContentType类型是否为text打头，eg: text/html, text/xml等等
          if (contentType != null && contentType.startsWith("text")) {
              // 拿到消息内部的编码格式，我们可以指定格式。eg: UTF-8, GBK等等
              String encoding = properties.getContentEncoding();
              if (encoding == null) {
                  // 默认为utf-8
                  encoding = this.defaultCharset;
              }
              try {
                  // 最终使用编码格式进行编码
                  content = new String(message.getBody(), encoding);
              }
              catch (UnsupportedEncodingException e) {
                  throw new MessageConversionException(
                      "failed to convert text-based Message content", e);
              }
          }
          else if (contentType != null &&
                   contentType.equals(MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT)) {
              try {
                  content = SerializationUtils.deserialize(
                      createObjectInputStream(new ByteArrayInputStream(message.getBody()), this.codebaseUrl));
              }
              catch (IOException | IllegalArgumentException | IllegalStateException e) {
                  throw new MessageConversionException(
                      "failed to convert serialized Message content", e);
              }
          }
      }
      if (content == null) {
          content = message.getBody();
      }
      return content;
  }
  ```

* 通过看了部分源码，大致知道了spring在消费rabbitmq消息时的处理，可以在rabbitmqTemplate中添加指定的消息转换器，其中包含，发送消息时的转换器回调以及接收消息时的转换器回调