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