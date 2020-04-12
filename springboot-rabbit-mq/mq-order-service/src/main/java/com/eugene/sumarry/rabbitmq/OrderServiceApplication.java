package com.eugene.sumarry.rabbitmq;

import com.eugene.sumarry.rabbitmq.controller.OrderController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(OrderServiceApplication.class);

        // 正常生产消息测试
        // context.getBean(OrderController.class).createOrder(10054L);

        // 预取消息测试
        // context.getBean(OrderController.class).createOrder();

        // 死信队列测试
        context.getBean(OrderController.class).createBasicQueueMessage();
    }
}
