package com.echo.enjoy.chapter2;

import com.echo.enjoy.chapter2.config.MainConfig1;
import com.echo.enjoy.chapter2.config.MainConfig2;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainTest {

    @Test
    public void test01(){
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        //运行时，如果是windows操作系统，让echo注入容器，如果是linux操作系统让jane注入容器
        //
        System.out.println("容器初始化完成");
    }

    @Test
    public void test02(){
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        System.out.println("容器初始化完成");
        //看看狗的bean是否注入到了容器中
        String[] names = context.getBeanDefinitionNames();
        for (String name : names){
            System.out.println(name);
        }
    }
}
