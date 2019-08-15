package com.eugene.sumarry.sbrabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

/**
 * @MapperScan注解 不使用dao impl实现类, 直接将启动时框架自动创建bean
 */
@SpringBootApplication
@MapperScan(basePackages = "com.eugene.sumarry.sbrabbitmq.mapper")
@ImportResource(locations = {"classpath:spring/spring-jdbc.xml"})
@EnableCaching
public class Application extends SpringBootServletInitializer {

    /**
     * 注册json单例对象至spring容器
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /*
     * 不使用springboot内嵌tomcat启动方式, 因为用java -jar的方式启动服务
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
