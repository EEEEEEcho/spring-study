package com.echo.enjoy.chapter8.processor;

import com.echo.enjoy.chapter8.pojo.Sun;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

@Component
public class EchoBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    //对BeanDefinition的增强
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("EchoBeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry,bean的数量为:" + registry.getBeanDefinitionCount());

        RootBeanDefinition sunDefinition = new RootBeanDefinition(Sun.class);
        registry.registerBeanDefinition("sun",sunDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("EchoBeanDefinitionRegistryPostProcessor.postProcessBeanFactory,bean的数量为:" + beanFactory.getBeanDefinitionCount());
    }
}
