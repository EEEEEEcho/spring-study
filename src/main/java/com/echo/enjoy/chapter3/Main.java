package com.echo.enjoy.chapter3;

import com.echo.enjoy.chapter3.config.MainConfig1;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    @Test
    public void test01(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        System.out.println("IOC容器创建完成");
        //关掉容器
        context.close();
        System.out.println("IOC容器被关闭");
    }

    @Test
    @Autowired
    public void test02(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        System.out.println("IOC容器创建完成");
        //关掉容器
        context.close();
        System.out.println("IOC容器被关闭");
    }
}
