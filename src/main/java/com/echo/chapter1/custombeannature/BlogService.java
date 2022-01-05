package com.echo.chapter1.custombeannature;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BlogService {
    private BlogDao blogDao;

    public void setBlogDao(BlogDao blogDao) {
        this.blogDao = blogDao;
    }

    public void init(){
        System.out.println("Blog Service init");
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("nature.xml");
        BlogDao blogDao = context.getBean("blogDao", BlogDao.class);
        //BlogService blogService = context.getBean("blogService", BlogService.class);
    }
}
