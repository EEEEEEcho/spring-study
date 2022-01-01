package com.echo.chapter1.dependencies;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;

public class ComplexObject {
    private Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("dependencies.xml");
        ComplexObject childProperties = context.getBean("childProperties", ComplexObject.class);
        System.out.println(childProperties.getProperties());
    }
}
