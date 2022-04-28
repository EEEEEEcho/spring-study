package com.echo.enjoy.chapter8.pojo;

import org.springframework.beans.factory.annotation.Value;

public class Sun {
    private String name;

    public Sun(@Value("Hello") String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
