package com.eugene.sumarry.rabbitmq;

import com.eugene.sumarry.rabbitmq.controller.OrderController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(OrderServiceApplication.class);

        context.getBean(OrderController.class).createOrder(10054L);
    }
}
