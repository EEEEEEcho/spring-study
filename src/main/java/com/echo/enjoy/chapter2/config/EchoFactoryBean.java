package com.echo.enjoy.chapter2.config;

import com.echo.enjoy.chapter2.pojo.SpiderMan;
import org.springframework.beans.factory.FactoryBean;

public class EchoFactoryBean implements FactoryBean<SpiderMan> {
    @Override
    public SpiderMan getObject() throws Exception {
        return new SpiderMan();
    }

    @Override
    public Class<?> getObjectType() {
        return SpiderMan.class;
    }

    @Override
    public boolean isSingleton() {
        return FactoryBean.super.isSingleton();
    }
}
