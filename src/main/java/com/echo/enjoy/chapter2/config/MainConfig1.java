package com.echo.enjoy.chapter2.config;


import com.echo.enjoy.chapter2.pojo.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig1 {

//    @Bean("echo")
//    //加一个bean注入容器时的条件
//    @Conditional(WindowsCondition.class)
//    public Person getEcho(){
//        System.out.println("给容器中添加echo......");
//        return new Person("echo",27);
//    }
//
//    @Bean("jane")
//    //加一个bean注入容器时的条件
//    @Conditional(LinuxCondition.class)
//    public Person getJane(){
//        System.out.println("给容器中添加jane......");
//        return new Person("jane",28);
//    }
//
//    @Bean("lucky")
//    public Person getLucky(){
//        System.out.println("给容器中添加lucky......");
//        return new Person("lucky",29);
//    }

    /**
     * 给容器中注册组件(Bean)的方式
     * 1.@Bean：适用于导入第三方的类或包的组件，例如Person为第三方的类，需要我们在
     * IOC容器中使用时，需要使用该注解
     * 2.包扫描(@ComponentScan) + 组件的标注注解(@Controller,@Service,@Repository,@Component)
     * 一般是针对我们自己写的类，
     * 3.@Import:能够快速给容器导入一个Bean，注意@Bean注册的方式过于简单
     *    3.1 @Import(value = {Dog.class, Cat.class})value中的值是要注入到容器中的bean的class，bean的ID为类的全限定类名
     *    3.2 ImportSelector:是一个接口，返回需要导入到容器的组件的全类名数组
     *    3.3 ImportBeanDefinitionRegister: 可以手动添加组件到IOC容器，所有Bean的注册可以使用BeanDefinitionRegistry
     * 4.使用Spring提供的FactoryBean(工厂Bean)进行注册
     */

    //容器启动时初始化lucky的bean实例
    @Bean("lucky")
    public Person getLucky(){
        System.out.println("给容器中添加lucky......");
        return new Person("lucky",29);
    }

}
