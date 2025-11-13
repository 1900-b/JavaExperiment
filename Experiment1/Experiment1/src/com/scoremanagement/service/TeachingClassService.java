package com.scoremanagement.service;

import com.scoremanagement.dao.TeachingClassDAO;
import com.scoremanagement.model.entity.TeachingClass;
import java.util.List;
import java.util.Random;

/**
 * 教学班业务逻辑服务类
 * 负责教学班相关的业务逻辑处理
 */
public class TeachingClassService {
    private TeachingClassDAO teachingClassDAO;
    private Random random;

    public TeachingClassService() {
        this.teachingClassDAO = new TeachingClassDAO();
        this.random = new Random();
    }

    /**
     * 获取所有教学班
     */
    public List<TeachingClass> getAllTeachingClasses() {
        return teachingClassDAO.getAllTeachingClasses();
    }

    /**
     * 根据教学班编号获取教学班
     */
    public TeachingClass getTeachingClassById(String classId) {
        return teachingClassDAO.getTeachingClassById(classId);
    }

    /**
     * 添加教学班
     */
    public void addTeachingClass(TeachingClass teachingClass) {
        teachingClassDAO.addTeachingClass(teachingClass);
    }

    /**
     * 更新教学班信息
     */
    public void updateTeachingClass(TeachingClass teachingClass) {
        teachingClassDAO.updateTeachingClass(teachingClass);
    }

    /**
     * 删除教学班
     */
    public void deleteTeachingClass(String classId) {
        teachingClassDAO.deleteTeachingClass(classId);
    }

    /**
     * 为课程创建教学班
     */
    public void createTeachingClassesForCourses(List<String> courseIds, List<String> teacherIds) {
        String[] semesters = {"2024春季", "2024秋季", "2025春季", "2025秋季"};
        
        for (String courseId : courseIds) {
            // 为每门课程创建至少2个教学班
            int classCount = 2 + random.nextInt(2); // 2-3个教学班
            
            for (int i = 0; i < classCount; i++) {
                String classId = courseId + "_" + (i + 1);
                String teacherId = teacherIds.get(random.nextInt(teacherIds.size()));
                String semester = semesters[random.nextInt(semesters.length)];
                int studentCount = 0; // 初始学生数为0，后续选课时会增加
                
                TeachingClass teachingClass = new TeachingClass(classId, courseId, teacherId, semester, studentCount);
                addTeachingClass(teachingClass);
            }
        }
    }

}
