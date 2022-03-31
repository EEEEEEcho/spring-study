package com.echo.enjoy.chapter3.pojo;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

//@Component
public class Train implements InitializingBean, DisposableBean {
    public Train(){
        System.out.println("Train constructor...");
    }

    @Override
    //在bean销毁时，调用此方法
    public void destroy() throws Exception {
        System.out.println("Train DisposableBean destroy()...");
    }

    @Override
    //bean的属性设值,初始化完成之后调用
    public void afterPropertiesSet() throws Exception {
        System.out.println("Train InitializingBean afterPropertiesSet()...");
    }
}
