package com.echo.chapter1.dependencies;

public class ExampleBean2 {
    private int year;
    private AnotherBean anotherBean;
    private YetAnotherBean yetAnotherBean;

    public void setYear(int year) {
        this.year = year;
    }

    public void setAnotherBean(AnotherBean anotherBean) {
        this.anotherBean = anotherBean;
    }

    public void setYetAnotherBean(YetAnotherBean yetAnotherBean) {
        this.yetAnotherBean = yetAnotherBean;
    }
}
