package com.lilianghui.spring.starter.utils;


import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class BeanFactoryUtils {

    public static <T> T registerBeanDefinition(ApplicationContext applicationContext, Class<T> clazz) {
        return registerBeanDefinition(applicationContext, clazz.getSimpleName(), clazz);
    }

    public static <T> T registerBeanDefinition(ApplicationContext applicationContext, String name, Class<T> clazz) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
        beanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());
        return (T) beanFactory.getBean(name);
    }

    public static Object registerBean(ApplicationContext applicationContext, String name, Object instance) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();
        beanFactory.registerSingleton(name, instance);
        return instance;
    }
}
