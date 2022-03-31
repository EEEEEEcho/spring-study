package com.echo.enjoy.chapter5.aop;

import org.springframework.stereotype.Component;

@Component("calculator")
public class Calculator {
    //假设这是业务逻辑方法
    public int div(int i,int j){
        return i / j;
    }
}
