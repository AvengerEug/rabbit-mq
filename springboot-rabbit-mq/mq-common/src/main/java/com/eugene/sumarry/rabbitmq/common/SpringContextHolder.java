package com.eugene.sumarry.rabbitmq.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Inherited;

@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static  <T> T getBean(Class<?> clazz) {
        return (T)getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String beanName) {
        return (T)getApplicationContext().getBean(beanName);
    }
}
