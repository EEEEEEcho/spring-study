package com.echo.enjoy.chapter4.service;

import com.echo.enjoy.chapter4.dao.TestDao;
import com.echo.enjoy.chapter4.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    private TestDao testDao;
    @Autowired(required = false)
    @Qualifier("userDaoImpl2")
    private UserDao userDao;

    public void printDao(){
        System.out.println(testDao);
    }

    public void invokeUserDaoPrint(){
        userDao.printUrl();
    }
}
