package com.echo.chapter1.beanoverview;

import com.echo.dao.StudentDao;
import com.echo.pojo.Student;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ObjectFactory {
    private static Student student = new Student();
    private static StudentDao studentDao = new StudentDao();

    public Student getStudent(){
        return student;
    }

    public StudentDao getStudentDao(){
        return studentDao;
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("pojo.xml");
        Student student = context.getBean("student", Student.class);
        System.out.println(student);
        StudentDao studentDao = context.getBean("studentDao", StudentDao.class);
        System.out.println(studentDao);
    }
}
