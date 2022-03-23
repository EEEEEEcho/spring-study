package com.echo.enjoy.chapter3.pojo;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class Jeep {
    public Jeep(){
        System.out.println("Jeep Constructor ......");
    }
    @PostConstruct
    public void doInit(){
        System.out.println("Jeep doInit() ......");
    }
    @PreDestroy
    public void doDestroy(){
        System.out.println("Jeep doDestroy() ......");
    }
}
