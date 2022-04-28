package com.echo.enjoy.chapter8.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EchoBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    /**
     * 实现对BeanFactory的增强
     * @param beanFactory
     * @throws BeansException
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("EchoBeanFactoryPostProcessor调用到了BeanFactoryPostProcessor.postProcessBeanFactory");
        //拿到BeanFactory中所有的bean的定义，但是所有的bean实例还没有创建
        int count = beanFactory.getBeanDefinitionCount();
        System.out.println("当前BeanFactory中有" + count + "个bean");
        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));
    }
}
