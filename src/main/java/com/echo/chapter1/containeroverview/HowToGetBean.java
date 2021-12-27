package com.echo.chapter1.containeroverview;

import com.echo.dao.StudentDao;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HowToGetBean {
    public static void main(String[] args) {
        //使用路径资源来加载容器初始化时需要的元数据
        //这样就初始化好了容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("pojo.xml","dao.xml");
        StudentDao studentDao = applicationContext.getBean("studentDao", StudentDao.class);
        System.out.println(studentDao);
    }
}
