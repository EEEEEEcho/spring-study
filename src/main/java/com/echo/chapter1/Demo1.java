package com.echo.chapter1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo1 {
    public static void main(String[] args) {
        //使用路径资源来加载容器初始化时需要的元数据
        //这样就初始化好了容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("pojo.xml","dao.xml");

    }
}
