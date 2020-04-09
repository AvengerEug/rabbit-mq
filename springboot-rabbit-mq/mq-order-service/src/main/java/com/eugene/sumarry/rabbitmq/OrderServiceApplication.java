package com.eugene.sumarry.rabbitmq;

import com.eugene.sumarry.rabbitmq.controller.OrderController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(OrderServiceApplication.class);

        context.getBean(OrderController.class).createOrder(10054L);
        System.in.read();
    }
}
