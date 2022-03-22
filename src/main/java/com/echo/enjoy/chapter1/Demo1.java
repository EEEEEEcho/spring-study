package com.echo.enjoy.chapter1;

import com.echo.enjoy.chapter1.config.MainConfig;
import com.echo.enjoy.chapter1.config.MainConfig2;
import com.echo.enjoy.chapter1.pojo.Person;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class Demo1 {
//    public static void main(String[] args) {
//        //useXML();
//        useAnnotation();
//    }

    private static void useXML() {
        ApplicationContext context = new ClassPathXmlApplicationContext("pojo.xml");
        Person person = context.getBean("person", Person.class);
        System.out.println(person);
    }

    public static void useAnnotation(){
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        //用配置文件配置的bean，bean的id使用的是方法的名字，比如说。
        //如果方法名字为person，那么这个bean的id就是person，如果是getPerson
        //那么这个bean的id就是getPerson
        Person person = context.getBean("person", Person.class);
        System.out.println(person);
        //获取bean的名称
        String[] namesForBean = context.getBeanNamesForType(Person.class);
        System.out.println(Arrays.toString(namesForBean));
    }

    @Test
    public void test01(){
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        //获取容器中所有bean的名字
        String[] names = context.getBeanDefinitionNames();
        for (String name: names){
            System.out.println(name);
        }
    }
    @Test
    public void test02(){
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig2.class);
        System.out.println("IOC容器创建完成");
        Person person1 = context.getBean("person", Person.class);
//        Person person2 = context.getBean("person", Person.class);
//        System.out.println(person1 == person2);
        System.out.println(person1);
    }
}
