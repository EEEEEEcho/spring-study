package com.echo.enjoy.chapter2.config;

import com.echo.enjoy.chapter2.pojo.Car;
import com.echo.enjoy.chapter2.pojo.Robot;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class EchoBeanDefinitionRegister implements ImportBeanDefinitionRegistrar {
    /**
     * 自定义注入bean
     * @param importingClassMetadata 当前类的注解元数据
     * @param registry 把所有需要添加到容器中的bean加入
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
        //如果容器中包含Fish和Cow那么就注入Robot和Car
        boolean fish = registry.containsBeanDefinition("com.echo.enjoy.chapter2.pojo.Fish");
        boolean cow = registry.containsBeanDefinition("com.echo.enjoy.chapter2.pojo.Cow");
        if (fish && cow){
            //对于我们要注册的bean，要将其进行包装
            RootBeanDefinition car = new RootBeanDefinition(Car.class);
            RootBeanDefinition robot = new RootBeanDefinition(Robot.class);
            registry.registerBeanDefinition("robot",robot);
            registry.registerBeanDefinition("car",car);
        }
    }
}
