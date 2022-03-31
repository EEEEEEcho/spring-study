package com.echo.enjoy.chapter4.config;

import com.echo.enjoy.chapter4.dao.TestDao;
import com.echo.enjoy.chapter4.dao.UserDao;
import com.echo.enjoy.chapter4.dao.UserDaoImpl1;
import com.echo.enjoy.chapter4.dao.UserDaoImpl2;
import com.echo.enjoy.chapter4.pojo.Bird;
import com.echo.enjoy.chapter4.pojo.Hero;
import org.springframework.context.annotation.*;

@Configuration
//当容器启动时，会将这个配置加载到Environment中
//@PropertySource(value = "classpath:/application.properties")
@ComponentScan(value =
        {"com.echo.enjoy.chapter4.controller",
        "com.echo.enjoy.chapter4.service",
        "com.echo.enjoy.chapter4.dao"}
)
public class MainConfig1 {
//    @Bean
//    public Hero hero(){
//        return new Hero();
//    }
//
//    @Bean
//    public Bird bird(){
//        return new Bird();
//    }

//    @Bean("testDao")
//    public TestDao testDao1(){
//        return new TestDao("2");
//    }
//
//    @Bean("testDao")
//    @Primary
//    public TestDao testDao2(){
//        return new TestDao("3");
//    }
    @Bean
    public UserDao userDaoImpl1(){
        return new UserDaoImpl1();
    }
    @Bean
    public UserDao userDaoImpl2(){
        return new UserDaoImpl2();
    }
}
