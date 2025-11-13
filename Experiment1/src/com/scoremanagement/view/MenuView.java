package com.scoremanagement.view;

import com.scoremanagement.controller.SystemController;
import com.scoremanagement.model.entity.TeachingClass;
import com.scoremanagement.model.entity.Student;
import java.util.List;
import java.util.Scanner;

/**
 * 菜单视图类
 * 负责用户界面显示和用户交互
 */
public class MenuView {
    private SystemController systemController;
    private Scanner scanner;

    public MenuView() {
        this.systemController = new SystemController();
        this.scanner = new Scanner(System.in);
    }

    /**
     * 显示主菜单
     */
    public void showMainMenu() {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           学生成绩管理系统");
            System.out.println("=".repeat(50));
            System.out.println("1. 初始化系统数据");
            System.out.println("2. 学生选课");
            System.out.println("3. 生成成绩数据");
            System.out.println("4. 查看教学班成绩");
            System.out.println("5. 查看分数段统计");
            System.out.println("6. 查询学生成绩");
            System.out.println("7. 查看全校学生排名");
            System.out.println("8. 查看系统信息");
            System.out.println("0. 退出系统");
            System.out.println("=".repeat(50));
            System.out.print("请选择操作（0-8）: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                handleMenuChoice(choice);
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误，请输入数字！");
            }
        }
    }

    /**
     * 处理菜单选择
     */
    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                initializeSystem();
                break;
            case 2:
                studentCourseSelection();
                break;
            case 3:
                generateScores();
                break;
            case 4:
                displayClassScores();
                break;
            case 5:
                displayScoreDistribution();
                break;
            case 6:
                queryStudentScores();
                break;
            case 7:
                displayAllStudentsRanking();
                break;
            case 8:
                displaySystemInfo();
                break;
            case 0:
                System.out.println("感谢使用学生成绩管理系统，再见！");
                System.exit(0);
                break;
            default:
                System.out.println("无效选择，请重新输入！");
        }
    }

    /**
     * 初始化系统数据
     */
    private void initializeSystem() {
        System.out.println("\n正在初始化系统数据，请稍候...");
        systemController.initializeSystem();
        System.out.println("系统数据初始化完成！");
        pressEnterToContinue();
    }

    /**
     * 学生选课
     */
    private void studentCourseSelection() {
        System.out.println("\n正在执行学生选课...");
        systemController.studentCourseSelection();
        System.out.println("学生选课完成！");
        pressEnterToContinue();
    }

    /**
     * 生成成绩数据
     */
    private void generateScores() {
        System.out.println("\n正在生成成绩数据，请稍候...");
        systemController.generateScores();
        System.out.println("成绩数据生成完成！");
        pressEnterToContinue();
    }

    /**
     * 显示教学班成绩
     */
    private void displayClassScores() {
        while (true) {
            List<TeachingClass> classes = systemController.getAllTeachingClasses();
            if (classes.isEmpty()) {
                System.out.println("没有找到教学班数据，请先初始化系统！");
                if (showSubMenu("查看教学班成绩")) {
                    return;
                }
                continue;
            }

            System.out.println("\n可用的教学班：");
            for (int i = 0; i < classes.size(); i++) {
                TeachingClass tc = classes.get(i);
                String courseName = systemController.getCourseNameByClassId(tc.getClassId());
                System.out.println((i + 1) + ". " + tc.getClassId() + " - " + courseName + " (学生数: " + tc.getStudentCount() + ")");
            }

            System.out.print("请选择教学班编号（输入序号）: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= classes.size()) {
                    TeachingClass selectedClass = classes.get(choice - 1);
                    
                    // 显示排序选项
                    System.out.println("\n请选择排序方式：");
                    System.out.println("1. 按学号排序");
                    System.out.println("2. 按成绩排序");
                    System.out.print("请选择（1-2）: ");
                    
                    try {
                        int sortChoice = Integer.parseInt(scanner.nextLine().trim());
                        if (sortChoice == 1) {
                            systemController.displayClassScoresByStudentId(selectedClass.getClassId());
                        } else if (sortChoice == 2) {
                            systemController.displayClassScoresByTotalScore(selectedClass.getClassId());
                        } else {
                            System.out.println("无效选择，默认按学号排序！");
                            systemController.displayClassScoresByStudentId(selectedClass.getClassId());
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("输入格式错误，默认按学号排序！");
                        systemController.displayClassScoresByStudentId(selectedClass.getClassId());
                    }
                    
                    // 显示子菜单
                    if (showSubMenu("查看教学班成绩")) {
                        return;
                    }
                } else {
                    System.out.println("无效选择！");
                }
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误！");
            }
        }
    }

    /**
     * 显示分数段统计
     */
    private void displayScoreDistribution() {
        while (true) {
            List<TeachingClass> classes = systemController.getAllTeachingClasses();
            if (classes.isEmpty()) {
                System.out.println("没有找到教学班数据，请先初始化系统！");
                if (showSubMenu("查看分数段统计")) {
                    return;
                }
                continue;
            }

            System.out.println("\n可用的教学班：");
            for (int i = 0; i < classes.size(); i++) {
                TeachingClass tc = classes.get(i);
                String courseName = systemController.getCourseNameByClassId(tc.getClassId());
                System.out.println((i + 1) + ". " + tc.getClassId() + " - " + courseName + " (学生数: " + tc.getStudentCount() + ")");
            }

            System.out.print("请选择教学班编号（输入序号）: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= classes.size()) {
                    TeachingClass selectedClass = classes.get(choice - 1);
                    systemController.displayScoreDistribution(selectedClass.getClassId());
                    
                    // 显示子菜单
                    if (showSubMenu("查看分数段统计")) {
                        return;
                    }
                } else {
                    System.out.println("无效选择！");
                }
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误！");
            }
        }
    }

    /**
     * 查询学生成绩
     */
    private void queryStudentScores() {
        while (true) {
            System.out.print("请输入学号: ");
            String studentId = scanner.nextLine().trim();
            
            if (studentId.isEmpty()) {
                System.out.println("学号不能为空！");
                if (showSubMenu("查询学生成绩")) {
                    return;
                }
                continue;
            }

            systemController.queryStudentScores(studentId);
            
            // 显示子菜单
            if (showSubMenu("查询学生成绩")) {
                return;
            }
        }
    }

    /**
     * 显示全校学生排名
     */
    private void displayAllStudentsRanking() {
        String sortType = null; // 记录用户选择的排序方式
        
        while (true) {
            // 如果还没有选择排序方式，则显示排序选择菜单
            if (sortType == null) {
                System.out.println("\n=== 全校学生排名 ===");
                System.out.println("请选择排序方式：");
                System.out.println("1. 按学号排序");
                System.out.println("2. 按平均成绩排序");
                System.out.print("请输入选择 (1-2): ");
                
                String choice = scanner.nextLine().trim();
                
                if (choice.equals("1")) {
                    System.out.println("\n正在显示全校学生排名（按学号排序）...");
                    systemController.displayAllStudentsRankingByStudentId();
                    sortType = "studentId";
                } else if (choice.equals("2")) {
                    System.out.println("\n正在计算全校学生排名（按平均成绩排序）...");
                    systemController.displayAllStudentsRankingByScore();
                    sortType = "score";
                } else {
                    System.out.println("无效选择，默认按学号排序");
                    System.out.println("\n正在显示全校学生排名（按学号排序）...");
                    systemController.displayAllStudentsRankingByStudentId();
                    sortType = "studentId";
                }
            }
            
            // 显示子菜单，传递排序方式信息
            Boolean result = showSubMenu("查看全校学生排名", sortType);
            if (result == null) {
                // 用户选择重新选择排序方式，重置sortType
                sortType = null;
            } else if (result) {
                // 返回上级菜单
                return;
            }
            // 如果result为false，继续操作（保持当前排序方式）
        }
    }

    /**
     * 显示系统信息
     */
    private void displaySystemInfo() {
        while (true) {
            System.out.println("\n=== 系统信息 ===");
            
            List<Student> students = systemController.getAllStudents();
            List<TeachingClass> classes = systemController.getAllTeachingClasses();
            
            System.out.println("学生总数: " + students.size());
            System.out.println("教学班总数: " + classes.size());
            
            if (!classes.isEmpty()) {
                int totalStudents = classes.stream().mapToInt(tc -> tc.getStudentCount()).sum();
                System.out.println("选课总人次: " + totalStudents);
            }
            
            System.out.println("\n系统功能说明：");
            System.out.println("1. 初始化系统数据 - 生成学生、教师、课程、教学班等基础数据");
            System.out.println("2. 学生选课 - 为每个学生分配3-5门课程");
            System.out.println("3. 生成成绩数据 - 为所有教学班生成学生成绩");
            System.out.println("4. 查看教学班成绩 - 支持按学号或成绩排序查看");
            System.out.println("5. 查看分数段统计 - 显示各分数段学生分布情况");
            System.out.println("6. 查询学生成绩 - 根据学号查询单个学生的所有成绩");
            System.out.println("7. 查看全校学生排名 - 支持按平均成绩或学号排序显示");
            System.out.println("8. 查看系统信息 - 显示系统统计信息和功能说明");
            
            // 按回车键继续，不显示子菜单
            pressEnterToContinue();
            return;
        }
    }

    /**
     * 等待用户按回车键继续
     */
    private void pressEnterToContinue() {
        System.out.print("\n按回车键继续...");
        scanner.nextLine();
    }

    /**
     * 显示子菜单
     */
    private Boolean showSubMenu(String menuTitle) {
        return showSubMenu(menuTitle, null);
    }
    
    /**
     * 显示子菜单（带排序方式参数）
     */
    private Boolean showSubMenu(String menuTitle, String sortType) {
        while (true) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("           " + menuTitle);
            System.out.println("=".repeat(40));
            System.out.println("1. 返回上级菜单");
            
            // 如果是全校学生排名菜单，添加更多选项
            if ("查看全校学生排名".equals(menuTitle)) {
                System.out.println("2. 导出为CSV文件");
                System.out.println("3. 重新选择排序方式");
            } else {
                System.out.println("2. 继续操作");
            }
            
            System.out.println("=".repeat(40));
            System.out.print("请选择操作（1-" + ("查看全校学生排名".equals(menuTitle) ? "3" : "2") + "）: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1:
                        return true; // 返回上级菜单
                    case 2:
                        if ("查看全校学生排名".equals(menuTitle)) {
                            handleCSVExport(sortType);
                            return false; // 导出后继续操作
                        } else {
                            return false; // 继续操作
                        }
                    case 3:
                        if ("查看全校学生排名".equals(menuTitle)) {
                            return null; // 重新选择排序方式
                        } else {
                            System.out.println("无效选择，请重新输入！");
                        }
                        break;
                    default:
                        System.out.println("无效选择，请重新输入！");
                }
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误，请输入数字！");
            }
        }
    }

    
    /**
     * 处理CSV导出选择（带排序方式参数）
     */
    private void handleCSVExport(String sortType) {
        System.out.println("\n=== 导出全校学生排名为CSV文件 ===");
        
        if (sortType != null) {
            // 根据用户之前的选择自动确定导出方式
            if ("score".equals(sortType)) {
                System.out.println("正在导出全校学生排名（按平均成绩排序）...");
                systemController.exportAllStudentsRankingToCSV();
            } else if ("studentId".equals(sortType)) {
                System.out.println("正在导出全校学生排名（按学号排序）...");
                systemController.exportAllStudentsRankingByStudentIdToCSV();
            } else {
                // 默认按平均成绩排序导出
                System.out.println("正在导出全校学生排名（按平均成绩排序）...");
                systemController.exportAllStudentsRankingToCSV();
            }
        } else {
            // 如果没有排序方式信息，则询问用户
            System.out.println("请选择导出方式：");
            System.out.println("1. 按平均成绩排序导出");
            System.out.println("2. 按学号排序导出");
            System.out.print("请选择（1-2）: ");
            
            String choice = scanner.nextLine().trim();
            
            if ("1".equals(choice)) {
                System.out.println("\n正在导出全校学生排名（按平均成绩排序）...");
                systemController.exportAllStudentsRankingToCSV();
            } else if ("2".equals(choice)) {
                System.out.println("\n正在导出全校学生排名（按学号排序）...");
                systemController.exportAllStudentsRankingByStudentIdToCSV();
            } else {
                System.out.println("无效选择，默认按平均成绩排序导出");
                System.out.println("\n正在导出全校学生排名（按平均成绩排序）...");
                systemController.exportAllStudentsRankingToCSV();
            }
        }
        
        System.out.println("\n导出完成！文件已保存到 C:\\Users\\1900\\Desktop\\Experiment1\\Experiment1\\data 目录");
    }

    /**
     * 启动系统
     */
    public void start() {
        System.out.println("欢迎使用学生成绩管理系统！");
        showMainMenu();
    }
}
