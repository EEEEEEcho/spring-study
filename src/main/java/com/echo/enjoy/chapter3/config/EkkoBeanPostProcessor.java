package com.echo.enjoy.chapter3.config;

import com.echo.enjoy.chapter3.pojo.Loser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class EkkoBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        if (beanName.equals("loser")){
            Loser loser = (Loser) bean;
            Loser.count ++;
            System.out.println("EkkoBeanPostProcessor postProcessBeforeInitialization() 处理了 " + beanName  +
                     " name属性值为：" + loser.getName() + " count 的值为: " + Loser.count);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
        if (beanName.equals("loser")){
            Loser loser = (Loser) bean;
            Loser.count ++;
            System.out.println("EkkoBeanPostProcessor postProcessAfterInitialization() 处理了 " + beanName  +
                    " name属性值为：" + loser.getName() + " count 的值为: " + Loser.count);
        }
        return bean;
    }
}
