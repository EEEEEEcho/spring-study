package com.echo.enjoy.chapter4.dao;

public class UserDaoImpl2 implements UserDao{
    private String url = "2";
    @Override
    public void printUrl() {
        System.out.println(this.url);
    }
}
