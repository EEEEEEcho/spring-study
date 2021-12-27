package com.echo.dao;

import com.echo.pojo.Student;

public class StudentDao {
    private int id;
    private Student student;

    public StudentDao() {
    }

    public StudentDao(int id, Student student) {
        this.id = id;
        this.student = student;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString() {
        return "StudentDao{" +
                "id=" + id +
                ", student=" + student +
                '}';
    }
}
