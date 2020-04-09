package com.eugene.sumarry.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GoodsServiceApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(GoodsServiceApplication.class);
        System.in.read();
    }
}
