package com.scoremanagement.model.entity;

import java.util.Date;

public class Score {
    private String studentId;        // 学生学号
    private String classId;          // 教学班编号
    private int usualScore;          // 平时成绩
    private int midtermScore;        // 期中成绩
    private int experimentScore;     // 实验成绩
    private int finalExamScore;      // 期末成绩
    private int totalScore;          // 综合成绩
    private Date gradeDate;          // 成绩记录日期

    // 构造方法
    public Score(String studentId, String classId, int usualScore, int midtermScore, int experimentScore, 
                 int finalExamScore, int totalScore, Date gradeDate) {
        this.studentId = studentId;
        this.classId = classId;
        this.usualScore = usualScore;
        this.midtermScore = midtermScore;
        this.experimentScore = experimentScore;
        this.finalExamScore = finalExamScore;
        this.totalScore = totalScore;
        this.gradeDate = gradeDate;
    }

    // Getter 和 Setter 方法
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public int getUsualScore() {
        return usualScore;
    }

    public void setUsualScore(int usualScore) {
        this.usualScore = usualScore;
    }

    public int getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(int midtermScore) {
        this.midtermScore = midtermScore;
    }

    public int getExperimentScore() {
        return experimentScore;
    }

    public void setExperimentScore(int experimentScore) {
        this.experimentScore = experimentScore;
    }

    public int getFinalExamScore() {
        return finalExamScore;
    }

    public void setFinalExamScore(int finalExamScore) {
        this.finalExamScore = finalExamScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public Date getGradeDate() {
        return gradeDate;
    }

    public void setGradeDate(Date gradeDate) {
        this.gradeDate = gradeDate;
    }
}
