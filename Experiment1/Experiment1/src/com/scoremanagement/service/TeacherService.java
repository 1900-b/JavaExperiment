package com.scoremanagement.service;

import com.scoremanagement.dao.TeacherDAO;
import com.scoremanagement.model.entity.Teacher;
import java.util.List;
import java.util.Random;

/**
 * 教师业务逻辑服务类
 * 负责教师相关的业务逻辑处理
 */
public class TeacherService {
    private TeacherDAO teacherDAO;
    private Random random;

    public TeacherService() {
        this.teacherDAO = new TeacherDAO();
        this.random = new Random();
    }

    /**
     * 获取所有教师
     */
    public List<Teacher> getAllTeachers() {
        return teacherDAO.getAllTeachers();
    }

    /**
     * 根据教师编号获取教师
     */
    public Teacher getTeacherById(String teacherId) {
        return teacherDAO.getTeacherById(teacherId);
    }

    /**
     * 添加教师
     */
    public void addTeacher(Teacher teacher) {
        teacherDAO.addTeacher(teacher);
    }

    /**
     * 批量生成教师数据
     */
    public void generateTeachers(int count) {
        String[] surnames = {"张", "王", "李", "赵", "刘", "陈", "杨", "黄", "周", "吴", 
                           "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗"};
        String[] givenNames = {"教授", "老师", "导师", "博士", "硕士", "专家", "学者", "研究员"};

        for (int i = 1; i <= count; i++) {
            String teacherId = String.format("T%03d", i);
            String surname = surnames[random.nextInt(surnames.length)];
            String givenName = givenNames[random.nextInt(givenNames.length)];
            String name = surname + givenName;

            Teacher teacher = new Teacher(teacherId, name);
            addTeacher(teacher);
        }
    }

    /**
     * 根据教师编号查询教师是否存在
     */
    public boolean teacherExists(String teacherId) {
        return getTeacherById(teacherId) != null;
    }
}
