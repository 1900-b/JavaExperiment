package com.scoremanagement.service;

import com.scoremanagement.dao.StudentDAO;
import com.scoremanagement.model.entity.Student;
import java.util.List;
import java.util.Random;

/**
 * 学生业务逻辑服务类
 * 负责学生相关的业务逻辑处理
 */
public class StudentService {
    private StudentDAO studentDAO;
    private Random random;

    public StudentService() {
        this.studentDAO = new StudentDAO();
        this.random = new Random();
    }

    /**
     * 获取所有学生
     */
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    /**
     * 根据学号获取学生
     */
    public Student getStudentById(String studentId) {
        return studentDAO.getStudentById(studentId);
    }

    /**
     * 添加学生
     */
    public void addStudent(Student student) {
        studentDAO.addStudent(student);
    }

    /**
     * 更新学生信息
     */
    public void updateStudent(Student student) {
        studentDAO.updateStudent(student);
    }

    /**
     * 删除学生
     */
    public void deleteStudent(String studentId) {
        studentDAO.deleteStudent(studentId);
    }

    /**
     * 批量生成学生数据
     */
    public void generateStudents(int count) {
        String[] surnames = {"张", "王", "李", "赵", "刘", "陈", "杨", "黄", "周", "吴", 
                           "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗"};
        String[] givenNames = {"伟", "芳", "娜", "秀英", "敏", "静", "丽", "强", "磊", "军", 
                             "洋", "勇", "艳", "杰", "娟", "涛", "明", "超", "秀兰", "霞"};
        String[] genders = {"男", "女"};
        String[] grades = {"2021", "2022", "2023", "2024"};

        for (int i = 1; i <= count; i++) {
            String studentId = String.format("S%06d", i);
            String surname = surnames[random.nextInt(surnames.length)];
            String givenName = givenNames[random.nextInt(givenNames.length)];
            String name = surname + givenName;
            String gender = genders[random.nextInt(genders.length)];
            String grade = grades[random.nextInt(grades.length)];

            Student student = new Student(studentId, name, gender, grade);
            addStudent(student);
        }
    }

    /**
     * 根据学号查询学生是否存在
     */
    public boolean studentExists(String studentId) {
        return getStudentById(studentId) != null;
    }
}
