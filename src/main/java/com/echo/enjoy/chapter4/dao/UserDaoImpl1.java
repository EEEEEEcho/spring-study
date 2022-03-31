package com.echo.enjoy.chapter4.dao;

import org.springframework.stereotype.Repository;

public class UserDaoImpl1 implements UserDao{
    private String url = "1";

    @Override
    public void printUrl() {
        System.out.println(this.url);
    }
}
