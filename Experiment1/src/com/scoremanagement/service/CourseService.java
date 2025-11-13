package com.scoremanagement.service;

import com.scoremanagement.dao.CourseDAO;
import com.scoremanagement.model.entity.Course;
import java.util.List;
import java.util.Random;

/**
 * 课程业务逻辑服务类
 * 负责课程相关的业务逻辑处理
 */
public class CourseService {
    private CourseDAO courseDAO;
    private Random random;

    public CourseService() {
        this.courseDAO = new CourseDAO();
        this.random = new Random();
    }

    /**
     * 获取所有课程
     */
    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    /**
     * 根据课程编号获取课程
     */
    public Course getCourseById(String courseId) {
        return courseDAO.getCourseById(courseId);
    }

    /**
     * 添加课程
     */
    public void addCourse(Course course) {
        courseDAO.addCourse(course);
    }

    /**
     * 批量生成课程数据
     */
    public void generateCourses(int count) {
        String[] courseNames = {
            "高等数学", "线性代数", "概率论与数理统计", "大学物理", "大学英语",
            "计算机导论", "程序设计基础", "数据结构", "算法设计与分析", "数据库原理",
            "操作系统", "计算机网络", "软件工程", "人工智能", "机器学习",
            "数字电路", "模拟电路", "信号与系统", "通信原理", "数字信号处理"
        };

        for (int i = 1; i <= count; i++) {
            String courseId = String.format("C%03d", i);
            String courseName = courseNames[random.nextInt(courseNames.length)];
            
            // 确保课程名称不重复
            while (courseExists(courseName)) {
                courseName = courseNames[random.nextInt(courseNames.length)];
            }

            Course course = new Course(courseId, courseName);
            addCourse(course);
        }
    }

    /**
     * 查询课程是否存在
     */
    private boolean courseExists(String courseName) {
        List<Course> courses = getAllCourses();
        for (Course course : courses) {
            if (course.getCourseName().equals(courseName)) {
                return true;
            }
        }
        return false;
    }
    
}
