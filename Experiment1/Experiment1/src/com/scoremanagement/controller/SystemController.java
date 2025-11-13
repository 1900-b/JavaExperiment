package com.scoremanagement.controller;

import com.scoremanagement.service.*;
import com.scoremanagement.model.entity.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 系统控制器
 * 负责协调各个服务层，处理业务流程
 */
public class SystemController {
    private StudentService studentService;
    private TeacherService teacherService;
    private CourseService courseService;
    private TeachingClassService teachingClassService;
    private ScoreService scoreService;
    private Random random;
    
    // 学生-教学班映射表：记录每个学生选择了哪些教学班
    private Map<String, List<String>> studentClassMapping;

    public SystemController() {
        this.studentService = new StudentService();
        this.teacherService = new TeacherService();
        this.courseService = new CourseService();
        this.teachingClassService = new TeachingClassService();
        this.scoreService = new ScoreService();
        this.random = new Random();
        this.studentClassMapping = new HashMap<>(); 
    }

    /**
     * 初始化系统数据
     */
    public void initializeSystem() {
        System.out.println("=== 开始初始化系统数据 ===");
        
        // 1. 生成学生数据（100+学生）
        System.out.println("正在生成学生数据...");
        studentService.generateStudents(120);
        System.out.println("学生数据生成完成，共120名学生");
        
        // 2. 生成教师数据（6+教师）
        System.out.println("正在生成教师数据...");
        teacherService.generateTeachers(8);
        System.out.println("教师数据生成完成，共8名教师");
        
        // 3. 生成课程数据（3+课程）
        System.out.println("正在生成课程数据...");
        courseService.generateCourses(5);
        System.out.println("课程数据生成完成，共5门课程");
        
        // 4. 创建教学班
        System.out.println("正在创建教学班...");
        List<String> courseIds = courseService.getAllCourses().stream()
                .map(Course::getCourseId)
                .collect(Collectors.toList());
        List<String> teacherIds = teacherService.getAllTeachers().stream()
                .map(Teacher::getTeacherId)
                .collect(Collectors.toList());
        
        teachingClassService.createTeachingClassesForCourses(courseIds, teacherIds);
        System.out.println("教学班创建完成");
        
        System.out.println("=== 系统数据初始化完成 ===");
    }

    /**
     * 学生选课功能
     */
    public void studentCourseSelection() {
        System.out.println("=== 开始学生选课 ===");
        
        List<Student> students = studentService.getAllStudents();
        List<TeachingClass> allClasses = teachingClassService.getAllTeachingClasses();
        
        if (students.isEmpty()) {
            System.out.println("错误：学生数据为空！请先初始化系统数据。");
            return;
        }
        
        if (allClasses.isEmpty()) {
            System.out.println("错误：教学班数据为空！请先初始化系统数据。");
            return;
        }
        
        // 为每个学生分配3-5门课程
        for (Student student : students) {
            int courseCount = 3 + random.nextInt(3); // 3-5门课程
            List<TeachingClass> selectedClasses = new ArrayList<>();
            List<String> studentClassIds = new ArrayList<>();
            
            // 随机选择教学班，确保不重复选同一门课程
            Set<String> selectedCourses = new HashSet<>();
            int attempts = 0;
            int maxAttempts = allClasses.size() * 3; // 增加尝试次数
            
            while (selectedClasses.size() < courseCount && attempts < maxAttempts) {
                TeachingClass randomClass = allClasses.get(random.nextInt(allClasses.size()));
                
                // 只检查是否已选择该课程，不限制教学班
                if (!selectedCourses.contains(randomClass.getCourseId())) {
                    selectedClasses.add(randomClass);
                    selectedCourses.add(randomClass.getCourseId());
                    studentClassIds.add(randomClass.getClassId());
                }
                attempts++;
            }
            
            // 如果无法选择足够的课程，强制选择
            if (selectedClasses.size() < 3) {
                // 从剩余课程中强制选择
                for (TeachingClass tc : allClasses) {
                    if (selectedClasses.size() >= 3) break;
                    if (!selectedCourses.contains(tc.getCourseId())) {
                        selectedClasses.add(tc);
                        selectedCourses.add(tc.getCourseId());
                        studentClassIds.add(tc.getClassId());
                    }
                }
            }
            
            // 记录学生选择的教学班
            studentClassMapping.put(student.getStudentId(), studentClassIds);
            
            // 更新教学班学生人数
            for (TeachingClass teachingClass : selectedClasses) {
                teachingClass.addStudent();
                teachingClassService.updateTeachingClass(teachingClass);
            }
            
            System.out.println("学生 " + student.getName() + " 已选择 " + selectedClasses.size() + " 门课程");
        }
        
        // 检查并调整教学班学生数量，确保每个教学班至少有20个学生
        adjustClassStudentCount();
        
        System.out.println("=== 学生选课完成 ===");
        System.out.println("总共处理了 " + students.size() + " 个学生");
    }
    
    /**
     * 调整教学班学生数量，确保每个教学班至少有20个学生
     */
    private void adjustClassStudentCount() {
        System.out.println("\n=== 调整教学班学生数量 ===");
        
        List<TeachingClass> allClasses = teachingClassService.getAllTeachingClasses();
        List<Student> allStudents = studentService.getAllStudents();
        
        for (TeachingClass tc : allClasses) {
            if (tc.getStudentCount() < 20) {
                System.out.println("教学班 " + tc.getClassId() + " 学生数不足，当前: " + tc.getStudentCount());
                
                // 为该教学班添加更多学生
                int needMore = 20 - tc.getStudentCount();
                Set<String> alreadyInClass = new HashSet<>();
                
                // 获取已在该教学班的学生
                for (Map.Entry<String, List<String>> entry : studentClassMapping.entrySet()) {
                    if (entry.getValue().contains(tc.getClassId())) {
                        alreadyInClass.add(entry.getKey());
                    }
                }
                
                // 随机选择其他学生加入该教学班
                int added = 0;
                for (Student student : allStudents) {
                    if (added >= needMore) break;
                    if (!alreadyInClass.contains(student.getStudentId())) {
                        // 检查该学生是否已选择该课程
                        boolean hasThisCourse = false;
                        for (String classId : studentClassMapping.getOrDefault(student.getStudentId(), new ArrayList<>())) {
                            TeachingClass existingClass = teachingClassService.getTeachingClassById(classId);
                            if (existingClass != null && existingClass.getCourseId().equals(tc.getCourseId())) {
                                hasThisCourse = true;
                                break;
                            }
                        }
                        
                        if (!hasThisCourse) {
                            // 添加学生到该教学班
                            if (!studentClassMapping.containsKey(student.getStudentId())) {
                                studentClassMapping.put(student.getStudentId(), new ArrayList<>());
                            }
                            studentClassMapping.get(student.getStudentId()).add(tc.getClassId());
                            tc.addStudent();
                            teachingClassService.updateTeachingClass(tc);
                            alreadyInClass.add(student.getStudentId());
                            added++;
                        }
                    }
                }
                
                System.out.println("为教学班 " + tc.getClassId() + " 添加了 " + added + " 个学生，现在有 " + tc.getStudentCount() + " 个学生");
            }
        }
    }

    /**
     * 生成成绩数据
     */
    public void generateScores() {
        System.out.println("=== 开始生成成绩数据 ===");
        
        if (studentClassMapping.isEmpty()) {
            System.out.println("错误：请先进行学生选课！");
            return;
        }
        
        // 基于选课记录生成成绩
        for (Map.Entry<String, List<String>> entry : studentClassMapping.entrySet()) {
            String studentId = entry.getKey();
            List<String> classIds = entry.getValue();
            
            // 为每个学生-教学班组合生成成绩
            for (String classId : classIds) {
                List<String> studentIds = new ArrayList<>();
                studentIds.add(studentId);
                
                String result = scoreService.generateScoresForClass(classId, studentIds);
                if (result.contains("成功")) {
                    System.out.println("学生 " + studentId + " 在教学班 " + classId + " 的成绩生成成功");
                }
            }
        }
        
        System.out.println("=== 成绩数据生成完成 ===");
        System.out.println("基于真实选课记录，共生成 " + getTotalScoreCount() + " 条成绩记录");
    }
    
    private int getTotalScoreCount() {
        return studentClassMapping.values().stream()
                .mapToInt(List::size)
                .sum();
    }

    /**
     * 显示教学班成绩（按学号排序）
     */
    public void displayClassScoresByStudentId(String classId) {
        List<Score> scores = scoreService.getScoresSortedByStudentId(classId);
        System.out.println("\n=== 教学班 " + classId + " 成绩单（按学号排序） ===");
        System.out.printf("%-12s %-8s %-8s %-8s %-8s %-8s %-8s %-8s%n", 
                         "学号", "姓名", "平时", "期中", "实验", "期末", "综合", "日期");
        System.out.println("----------------------------------------------------------------");
        
        for (Score score : scores) {
            // 获取学生姓名
            Student student = studentService.getStudentById(score.getStudentId());
            String studentName = (student != null) ? student.getName() : "未知";
            
            System.out.printf("%-12s %-8s %-8d %-8d %-8d %-8d %-8d %-10s%n",
                             score.getStudentId(),
                             studentName,
                             score.getUsualScore(),
                             score.getMidtermScore(),
                             score.getExperimentScore(),
                             score.getFinalExamScore(),
                             score.getTotalScore(),
                             score.getGradeDate().toString().substring(0, 10));
        }
    }

    /**
     * 显示教学班成绩（按平均成绩排序）
     */
    public void displayClassScoresByTotalScore(String classId) {
        List<Score> scores = scoreService.getScoresSortedByTotalScore(classId);
        System.out.println("\n=== 教学班 " + classId + " 成绩单（按平均成绩排序） ===");
        System.out.printf("%-12s %-8s %-8s %-8s %-8s %-8s %-8s %-8s%n", 
                         "学号", "姓名", "平时", "期中", "实验", "期末", "综合", "日期");
        System.out.println("----------------------------------------------------------------");
        
        for (Score score : scores) {
            // 获取学生姓名
            Student student = studentService.getStudentById(score.getStudentId());
            String studentName = (student != null) ? student.getName() : "未知";
            
            System.out.printf("%-12s %-8s %-8d %-8d %-8d %-8d %-8d %-10s%n",
                             score.getStudentId(),
                             studentName,
                             score.getUsualScore(),
                             score.getMidtermScore(),
                             score.getExperimentScore(),
                             score.getFinalExamScore(),
                             score.getTotalScore(),
                             score.getGradeDate().toString().substring(0, 10));
        }
    }

    /**
     * 显示分数段统计
     */
    public void displayScoreDistribution(String classId) {
        Map<String, Integer> distribution = scoreService.getScoreDistribution(classId);
        System.out.println("\n=== 教学班 " + classId + " 分数段统计 ===");
        
        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " 人");
        }
    }

    /**
     * 查询学生成绩
     */
    public void queryStudentScores(String studentId) {
        List<Score> scores = scoreService.getScoresByStudentId(studentId);
        if (scores.isEmpty()) {
            System.out.println("未找到学号为 " + studentId + " 的学生成绩记录");
            return;
        }
        
        System.out.println("\n=== 学生 " + studentId + " 的成绩单 ===");
        System.out.printf("%-15s %-12s %-8s %-8s %-8s %-8s %-8s %-8s%n", 
                         "课程名称", "教学班", "平时", "期中", "实验", "期末", "综合", "日期");
        System.out.println("----------------------------------------------------------------------------");
        
        int totalSum = 0;
        for (Score score : scores) {
            // 获取课程名称
            String courseName = getCourseNameByClassId(score.getClassId());
            
            System.out.printf("%-15s %-12s %-8d %-8d %-8d %-8d %-8d %-10s%n",
                             courseName,
                             score.getClassId(),
                             score.getUsualScore(),
                             score.getMidtermScore(),
                             score.getExperimentScore(),
                             score.getFinalExamScore(),
                             score.getTotalScore(),
                             score.getGradeDate().toString().substring(0, 10));
            totalSum += score.getTotalScore();
        }
        
        double average = (double) totalSum / scores.size();
        System.out.println("平均成绩: " + String.format("%.2f", average));
    }

    /**
     * 显示全校学生排名（按平均成绩排序）
     */
    public void displayAllStudentsRankingByScore() {
        List<Score> rankings = scoreService.getAllStudentsRanking();
        System.out.println("\n=== 全校学生成绩排名（按平均成绩排序） ===");
        
        for (int i = 0; i < rankings.size(); i++) {
            Score score = rankings.get(i);
            String studentId = score.getStudentId();
            
            // 获取学生姓名
            Student student = studentService.getStudentById(studentId);
            String studentName = (student != null) ? student.getName() : "未知";
            
            System.out.println("\n排名: " + (i + 1));
            System.out.println("学号: " + studentId);
            System.out.println("姓名: " + studentName);
            System.out.println("平均成绩: " + score.getTotalScore());
            
            // 显示该学生的各科成绩
            System.out.println("各科成绩:");
            displayStudentScores(studentId);
            System.out.println("  ----------------------------------------");
        }
    }
    
    /**
     * 显示全校学生排名（按学号排序）
     */
    public void displayAllStudentsRankingByStudentId() {
        List<Student> students = studentService.getAllStudents();
        System.out.println("\n=== 全校学生排名（按学号排序） ===");
        
        // 按学号排序
        students.sort((s1, s2) -> s1.getStudentId().compareTo(s2.getStudentId()));
        
        // 获取所有学生的排名信息，用于计算排名
        List<Score> allRankings = scoreService.getAllStudentsRanking();
        Map<String, Integer> studentRankingMap = new HashMap<>();
        for (int i = 0; i < allRankings.size(); i++) {
            studentRankingMap.put(allRankings.get(i).getStudentId(), i + 1);
        }
        
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            String studentId = student.getStudentId();
            String studentName = student.getName();
            
            // 计算该学生的平均成绩
            List<Score> studentScores = scoreService.getScoresByStudentId(studentId);
            if (studentScores.isEmpty()) {
                System.out.println("\n学号: " + studentId);
                System.out.println("姓名: " + studentName);
                System.out.println("平均成绩: 暂无成绩记录");
                System.out.println("排名: 无排名");
                System.out.println("各科成绩: 无");
                System.out.println("  ----------------------------------------");
                continue;
            }
            
            int totalSum = studentScores.stream().mapToInt(Score::getTotalScore).sum();
            double average = (double) totalSum / studentScores.size();
            
            // 获取该学生的排名
            Integer ranking = studentRankingMap.get(studentId);
            String rankingStr = (ranking != null) ? String.valueOf(ranking) : "无排名";
            
            System.out.println("\n学号: " + studentId);
            System.out.println("姓名: " + studentName);
            System.out.println("平均成绩: " + String.format("%.2f", average));
            System.out.println("排名: " + rankingStr);
            
            // 显示该学生的各科成绩
            System.out.println("各科成绩:");
            displayStudentScores(studentId);
            System.out.println("  ----------------------------------------");
        }
    }
    
    /**
     * 显示指定学生的各科成绩
     */
    private void displayStudentScores(String studentId) {
        List<Score> studentScores = scoreService.getScoresByStudentId(studentId);
        
        if (studentScores.isEmpty()) {
            System.out.println("  该学生暂无成绩记录");
            return;
        }
        
        System.out.printf("  %-15s %-15s %-10s%n", "课程名称", "教学班", "成绩");
        System.out.println("  " + "----------------------------------------");
        
        for (Score score : studentScores) {
            // 获取教学班信息以显示课程名称
            TeachingClass tc = teachingClassService.getTeachingClassById(score.getClassId());
            if (tc != null) {
                Course course = courseService.getCourseById(tc.getCourseId());
                String courseName = (course != null) ? course.getCourseName() : "未知课程";
                System.out.printf("  %-15s %-15s %-10d%n", 
                    courseName, 
                    score.getClassId(), 
                    score.getTotalScore());
            } else {
                System.out.printf("  %-15s %-15s %-10d%n", 
                    "未知课程", 
                    score.getClassId(), 
                    score.getTotalScore());
            }
        }
    }

    /**
     * 获取所有教学班列表
     */
    public List<TeachingClass> getAllTeachingClasses() {
        return teachingClassService.getAllTeachingClasses();
    }

    /**
     * 获取所有学生列表
     */
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
    
    /**
     * 根据教学班获取课程名称
     */
    public String getCourseNameByClassId(String classId) {
        TeachingClass tc = teachingClassService.getTeachingClassById(classId);
        if (tc != null) {
            Course course = courseService.getCourseById(tc.getCourseId());
            return (course != null) ? course.getCourseName() : "未知课程";
        }
        return "未知课程";
    }
    
    
    /**
     * 导出全校学生排名到CSV文件（按平均成绩排序）
     */
    public void exportAllStudentsRankingToCSV() {
        List<Score> rankings = scoreService.getAllStudentsRanking();
        String filename = "全校学生排名（平均成绩排序）";
        exportRankingToCSV(rankings, filename);
    }
    
    /**
     * 导出全校学生排名到CSV文件（按学号排序）
     */
    public void exportAllStudentsRankingByStudentIdToCSV() {
        List<Student> students = studentService.getAllStudents();
        String filename = "全校学生排名（学号排序）";
        exportStudentRankingToCSV(students, filename);
    }
    
    /**
     * 导出排名数据到CSV文件（按平均成绩排序）
     */
    private void exportRankingToCSV(List<Score> rankings, String filename) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String csvFilename = filename + "_" + timestamp + ".csv";
            
            // 确保目标目录存在
            String dataDir = "C:\\Users\\1900\\Desktop\\JavaExperiment\\Experiment1\\data";
            File dataDirectory = new File(dataDir);
            if (!dataDirectory.exists()) {
                dataDirectory.mkdirs();
            }
            
            String fullPath = dataDir + File.separator + csvFilename;
            FileWriter writer = new FileWriter(fullPath);
            
            // 写入CSV头部
            writer.append("排名,学号,姓名,平均成绩,各科成绩详情\n");
            
            for (int i = 0; i < rankings.size(); i++) {
                Score score = rankings.get(i);
                String studentId = score.getStudentId();
                
                // 获取学生姓名
                Student student = studentService.getStudentById(studentId);
                String studentName = (student != null) ? student.getName() : "未知";
                
                // 获取各科成绩详情
                List<Score> studentScores = scoreService.getScoresByStudentId(studentId);
                StringBuilder scoreDetails = new StringBuilder();
                for (Score s : studentScores) {
                    String courseName = getCourseNameByClassId(s.getClassId());
                    scoreDetails.append(courseName).append(":").append(s.getTotalScore()).append(";");
                }
                
                // 写入CSV行
                writer.append(String.valueOf(i + 1)).append(",");
                writer.append(studentId).append(",");
                writer.append(studentName).append(",");
                writer.append(String.valueOf(score.getTotalScore())).append(",");
                writer.append("\"").append(scoreDetails.toString()).append("\"\n");
            }
            
            writer.close();
            System.out.println("排名数据已成功导出到文件: " + fullPath);
            System.out.println("共导出 " + rankings.size() + " 条排名记录");
            
        } catch (IOException e) {
            System.out.println("导出CSV文件时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 导出学生排名数据到CSV文件（按学号排序）
     */
    private void exportStudentRankingToCSV(List<Student> students, String filename) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String csvFilename = filename + "_" + timestamp + ".csv";
            
            // 确保目标目录存在
            String dataDir = "C:\\Users\\1900\\Desktop\\JavaExperiment\\Experiment1\\data";
            File dataDirectory = new File(dataDir);
            if (!dataDirectory.exists()) {
                dataDirectory.mkdirs();
            }
            
            String fullPath = dataDir + File.separator + csvFilename;
            FileWriter writer = new FileWriter(fullPath);
            
            // 写入CSV头部
            writer.append("学号,姓名,平均成绩,排名,各科成绩详情\n");
            
            // 按学号排序
            students.sort((s1, s2) -> s1.getStudentId().compareTo(s2.getStudentId()));
            
            // 获取所有学生的排名信息，用于计算排名
            List<Score> allRankings = scoreService.getAllStudentsRanking();
            Map<String, Integer> studentRankingMap = new HashMap<>();
            for (int i = 0; i < allRankings.size(); i++) {
                studentRankingMap.put(allRankings.get(i).getStudentId(), i + 1);
            }
            
            for (Student student : students) {
                String studentId = student.getStudentId();
                String studentName = student.getName();
                
                // 计算该学生的平均成绩
                List<Score> studentScores = scoreService.getScoresByStudentId(studentId);
                String averageStr = "暂无成绩记录";
                String rankingStr = "无排名";
                StringBuilder scoreDetails = new StringBuilder();
                
                if (!studentScores.isEmpty()) {
                    int totalSum = studentScores.stream().mapToInt(Score::getTotalScore).sum();
                    double average = (double) totalSum / studentScores.size();
                    averageStr = String.format("%.2f", average);
                    
                    // 获取该学生的排名
                    Integer ranking = studentRankingMap.get(studentId);
                    rankingStr = (ranking != null) ? String.valueOf(ranking) : "无排名";
                    
                    // 获取各科成绩详情
                    for (Score s : studentScores) {
                        String courseName = getCourseNameByClassId(s.getClassId());
                        scoreDetails.append(courseName).append(":").append(s.getTotalScore()).append(";");
                    }
                }
                
                // 写入CSV行
                writer.append(studentId).append(",");
                writer.append(studentName).append(",");
                writer.append(averageStr).append(",");
                writer.append(rankingStr).append(",");
                writer.append("\"").append(scoreDetails.toString()).append("\"\n");
            }
            
            writer.close();
            System.out.println("排名数据已成功导出到文件: " + fullPath);
            System.out.println("共导出 " + students.size() + " 条学生记录");
            
        } catch (IOException e) {
            System.out.println("导出CSV文件时发生错误: " + e.getMessage());
        }
    }
}
