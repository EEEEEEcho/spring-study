package com.echo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {
    private String name = "A";
    public Main(){
        show();
    }
    public void show(){
        System.out.println("A" + name);
    }
}
class Sub extends Main{
    private String name = "B";
    public Sub(){
        show();
    }
    public void show(){
        System.out.println("B" + name);
    }

    public static void main(String[] args) {
//        new Sub();
//        int method = method(10, 1);
//        System.out.println(method);
//        List<Person> list = new ArrayList<>();
//        Person p = new Person();
//        p.setAge(23);
//        Person p2 = new Person();
//        p2.setAge(22);
//        Person p3 = new Person();
//        p3.setAge(25);
//        list.add(p);
//        list.add(p2);
//        list.add(p3);
//        list.sort(new Comparator<Person>() {
//            @Override
//            public int compare(Person p1, Person p2) {
//                if (p1.getAge() == p2.getAge()){
//                    //定义另一种规则
//                }
//                else{
//                    return Integer.compare(p1.getAge(),p2.getAge());
//                }
//
//            }
//        });
//        System.out.println(list);
        Integer[] nums = new Integer[]{2,1,3};
        Arrays.sort(nums, Integer::compare);
        System.out.println(Arrays.toString(nums));
    }

//    public static int method(int a,int b){
//        try{
//            System.out.println("try:取余");
//            int c = a % (--b);
//            System.out.println("try:余数");
//            return c;
//        }
//        catch (Exception e){
//            System.out.println("catch");
//            return 100;
//
//        }
//        finally {
//            System.out.println("finally");
//            //finally 中的return会覆盖try中的return
//            return -100;
//        }
//    }

}
class Person{
    private String name;
    private int age;
    private String address;
    private int count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                ", count=" + count +
                '}';
    }
}