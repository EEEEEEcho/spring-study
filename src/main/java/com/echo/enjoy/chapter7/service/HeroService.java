package com.echo.enjoy.chapter7.service;

import com.echo.enjoy.chapter7.dao.HeroDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HeroService {

    @Autowired
    private HeroDao heroDao;

    @Transactional
    public void addHero(int number,String name,String country){
        heroDao.insert(number,name,country);
        System.out.println("添加完成");

        int i = 1 / 0;
    }
}
