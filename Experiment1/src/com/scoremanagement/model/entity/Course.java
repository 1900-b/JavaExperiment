package com.scoremanagement.model.entity;

public class Course {
    private String courseId;    // 课程编号
    private String courseName;  // 课程名称

    // 构造方法
    public Course(String courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }

    // Getter 和 Setter 方法
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
