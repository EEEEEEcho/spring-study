package com.echo.enjoy.chapter3.config;

import com.echo.enjoy.chapter3.pojo.Bike;
import com.echo.enjoy.chapter3.pojo.Loser;
import com.echo.enjoy.chapter3.pojo.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
//@ComponentScan("com.echo.enjoy.chapter3")
public class MainConfig1 {

//    @Bean
//    public Person person(){
//        return new Person("Ekko",18);
//    }
//
//    @Scope("prototype")
//    @Bean(initMethod = "init",destroyMethod = "destroy")
//    public Bike bike(){
//        return new Bike();
//    }
    @Bean
    public EkkoBeanPostProcessor ekkoBeanPostProcessor(){
        return new EkkoBeanPostProcessor();
    }

    @Bean(initMethod = "customInit",destroyMethod = "customDestroy")
    public Loser loser(){
        return new Loser("Ekko");
    }
}
