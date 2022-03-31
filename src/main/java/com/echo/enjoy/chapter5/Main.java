package com.echo.enjoy.chapter5;

import com.echo.enjoy.chapter5.aop.Calculator;
import com.echo.enjoy.chapter5.config.Config;
import com.echo.enjoy.chapter5.pojo.Light;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    @Test
    public void test01(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        //根据类型去寻找bean
        Light bean = context.getBean(Light.class);
        System.out.println(bean);
    }

    @Test
    public void test02(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Calculator calculator = context.getBean("calculator", Calculator.class);
        int div = calculator.div(4, 2);
        System.out.println(div);
    }
}
