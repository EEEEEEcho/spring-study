package com.echo.enjoy.chapter4.pojo;

import org.springframework.beans.factory.annotation.Value;

public class Bird {
    //使用@Value进行初始化赋值，赋值的类型包括：1.基本字符串，2.SpringEL表达式 3.读取配置文件
    @Value("James")
    private String name;
    @Value("#{20-2}")   //SpringEL表达式
    private Integer age;
    @Value("${bird.color}")
    private String color;


    public Bird(){

    }

    public Bird(String name, Integer age,String color) {
        this.name = name;
        this.age = age;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Bird{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", color='" + color + '\'' +
                '}';
    }
}
