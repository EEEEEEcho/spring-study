package com.echo.enjoy.chapter2.config;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LinuxCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        //能获取到IOC容器正在使用的beanFactory
        ConfigurableListableBeanFactory beanFactory = conditionContext.getBeanFactory();
        //获取当前的环境变量，包括操作系统类型
        Environment environment = conditionContext.getEnvironment();
        //获取当前环境的操作系统
        String os = environment.getProperty("os.name");
        if (os.contains("linux")){
            return true;
        }
        return false;
    }
}
