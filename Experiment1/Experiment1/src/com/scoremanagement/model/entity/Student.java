package com.scoremanagement.model.entity;

public class Student {
    private String studentId;  // 学号
    private String name;       // 姓名
    private String gender;     // 性别
    private String grade;      // 年级

    // 构造方法
    public Student(String studentId, String name, String gender, String grade) {
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.grade = grade;
    }

    // Getter 和 Setter 方法
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
