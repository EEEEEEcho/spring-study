package com.echo.enjoy.chapter4.dao;

import org.springframework.stereotype.Repository;

@Repository
public class TestDao {
    private String url = "1";

    public TestDao() {
    }

    public TestDao(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "TestDao{" +
                "url='" + url + '\'' +
                '}';
    }
}
