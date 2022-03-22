package com.echo.enjoy.chapter1.config;

import com.echo.enjoy.chapter1.filter.MyTypeFilter;
import com.echo.enjoy.chapter1.pojo.Person;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(value = "com.echo.enjoy.chapter1")
//@ComponentScan(
//        value = "com.echo.enjoy.chapter1",
//        includeFilters = {
//                @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class})
//        },
//        useDefaultFilters = false
//)

//@ComponentScan(value = "com.echo.enjoy.chapter1",
//        excludeFilters = {
//            @ComponentScan.Filter
//        }
//)
//@ComponentScan(
//        value = "com.echo.enjoy.chapter1",
//        includeFilters = {
//                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {MyTypeFilter.class})
//        },
//        useDefaultFilters = false
//)
public class MainConfig2 {
    @Bean
    @Lazy
    public Person person() {
        System.out.println("给容器中添加person");
        return new Person("james", 20);
    }
}
