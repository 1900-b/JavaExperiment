package com.scoremanagement.model.entity;

public class Teacher {
    private String teacherId;  // 教师编号
    private String name;       // 教师姓名

    // 构造方法
    public Teacher(String teacherId, String name) {
        this.teacherId = teacherId;
        this.name = name;
    }

    // Getter 和 Setter 方法
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
