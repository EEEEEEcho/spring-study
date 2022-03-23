package com.echo.enjoy.chapter3.pojo;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Loser implements InitializingBean, DisposableBean {
    public static int count = 0;
    private String name;
    public Loser(String name){
        count++;
        System.out.println("这是构造方法: " + " count 的值为: " + count);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void customInit(){
        count++;
        System.out.println("这是我自定义的init方法,在该方法里的name属性值为： " + this.name + " count 的值为: " + count);
    }

    public void customDestroy(){
        count++;
        System.out.println("这是我自定义的destroy方法,在该方法里的name属性值为：  " + this.name + " count 的值为: " + count);

    }

    @Override
    public void destroy() throws Exception {
        count++;
        System.out.println("这是DisposableBean的destroy方法,在该方法里的name属性值为：  " + this.name + " count 的值为: " + count);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        count++;
        System.out.println("这是InitializingBean的afterPropertiesSet方法,在该方法里的name属性值为： " + this.name  + " count 的值为: " + count);
    }

    @PostConstruct
    public void customPostConstruct(){
        count++;
        System.out.println("这是我自定义的由@PostConstruct标记的方法,在该方法里的name属性值为：  " + this.name  + " count 的值为: " + count);
    }

    @PreDestroy
    public void customPreDestroy(){
        count++;
        System.out.println("这是我自定义的由@PreDestroy标记的方法,在该方法里的name属性值为：  " + this.name  + " count 的值为: " + count);
    }
}
