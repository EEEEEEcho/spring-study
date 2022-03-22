package com.echo.enjoy.chapter2.config;

import com.echo.enjoy.chapter2.pojo.Cat;
import com.echo.enjoy.chapter2.pojo.Dog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {Dog.class, Cat.class,EchoImportSelector.class,EchoBeanDefinitionRegister.class})
public class MainConfig2 {

    //将自定义的工厂注入到容器中，然后容器会解析这个FactoryBean中对bean的定义，将FactoryBean中定义的bean注入到容器中
    @Bean
    public EchoFactoryBean echoFactoryBean(){
        return new EchoFactoryBean();
    }
}
