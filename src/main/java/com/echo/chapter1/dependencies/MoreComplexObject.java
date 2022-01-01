package com.echo.chapter1.dependencies;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MoreComplexObject {
    private List<String> list;
    private Map<String,String> map;
    private Set<String> set;

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Set<String> getSet() {
        return set;
    }

    public void setSet(Set<String> set) {
        this.set = set;
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("dependencies.xml");
        MoreComplexObject moreComplexObject = context.getBean("moreComplexObject", MoreComplexObject.class);
        System.out.println(moreComplexObject.getList());
        System.out.println(moreComplexObject.getMap());
        System.out.println(moreComplexObject.getSet());
    }
}
