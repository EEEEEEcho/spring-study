package com.echo.enjoy.chapter5.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(value = "com.echo.enjoy.chapter5")
@EnableAspectJAutoProxy //开启切面，原生态的Spring需要手动开启，SpringBoot中已经封装好了
public class Config {
}
