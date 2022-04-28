package com.echo.enjoy.chapter7;

import com.echo.enjoy.chapter7.config.MainConfig;
import com.echo.enjoy.chapter7.service.HeroService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    @Test
    public void test01(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        HeroService heroService = context.getBean(HeroService.class);
        heroService.addHero(22,"c曹丕","魏");
    }
}
