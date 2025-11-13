package com.scoremanagement.model.entity;

public class TeachingClass {
    private String classId;     // 教学班编号
    private String courseId;    // 课程编号
    private String teacherId;   // 教师编号
    private String semester;    // 学期
    private int studentCount;   // 学生人数

    // 构造方法
    public TeachingClass(String classId, String courseId, String teacherId, String semester, int studentCount) {
        this.classId = classId;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.semester = semester;
        this.studentCount = studentCount;
    }

    // Getter 和 Setter 方法
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }

    public void addStudent() {
        this.studentCount++;
    }

}
