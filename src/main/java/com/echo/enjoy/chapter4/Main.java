package com.echo.enjoy.chapter4;

import com.echo.enjoy.chapter4.config.MainConfig1;
import com.echo.enjoy.chapter4.dao.TestDao;
import com.echo.enjoy.chapter4.pojo.Bird;
import com.echo.enjoy.chapter4.service.TestService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;

public class Main {
    @Test
    public void test01(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
        //从容器中获取所有bean
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String name: beanDefinitionNames){
            System.out.println(name);
        }
        //验证是否将bird.color属性加载到了环境变量中
        ConfigurableEnvironment environment = context.getEnvironment();
        System.out.println("environment : " + environment.getProperty("bird.color"));

        Bird bird = context.getBean("bird", Bird.class);
        System.out.println(bird);
        context.close();
    }

    @Test
    public void test02(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig1.class);
//        String[] beanDefinitionNames = context.getBeanDefinitionNames();
//        Arrays.stream(beanDefinitionNames).forEach(System.out::println);

        TestService testService = context.getBean("testService", TestService.class);
        testService.invokeUserDaoPrint();
//        TestDao testDao = context.getBean("testDao", TestDao.class);
//        System.out.println(testDao);
//        context.close();
    }
}
