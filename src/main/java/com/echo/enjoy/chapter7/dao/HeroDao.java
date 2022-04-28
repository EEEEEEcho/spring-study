package com.echo.enjoy.chapter7.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HeroDao {
    /**
     *  ~@Resource 和 @Autowired都可以作为注入属性的修饰，在接口仅有单一实现类时，两个注解的修饰效果相同，可以互相替换，不影响使用。
     *
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(int number,String name,String country){
        String sql = "insert into `hero` (`number`,`name`,`country`) values (?,?,?)";
        jdbcTemplate.update(sql,number,name,country);
    }
}
