package com.echo.enjoy.chapter8.config;

import com.echo.enjoy.chapter8.pojo.Moon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(value = "com.echo.enjoy.chapter8")
public class MainConfig {

    @Bean
    public Moon moon(){
        return new Moon();
    }
}
