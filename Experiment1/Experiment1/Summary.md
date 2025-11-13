# 学生成绩管理系统 - 实验报告

## 1. 软件功能

### 1.1 功能概述
本学生成绩管理系统是一个基于Java开发的教学管理软件，实现了学生选课、成绩管理、排名统计等核心功能。系统采用MVC架构模式，使用MySQL数据库进行数据持久化存储，提供直观的菜单式操作界面。

### 1.2 主要功能模块
- **系统初始化**：自动生成学生、教师、课程、教学班等基础数据
- **学生选课**：基于随机算法的智能选课系统，确保每个学生选择3-5门课程
- **成绩生成**：自动生成平时成绩、期中成绩、实验成绩、期末成绩
- **成绩查询**：支持按学生、课程、教学班等多种方式查询成绩
- **排名统计**：提供按平均成绩和学号两种排序方式的排名功能
- **数据导出**：支持将排名数据导出为CSV格式，便于数据分析

### 1.3 功能组织图
```
学生成绩管理系统
├── 系统管理
│   ├── 系统初始化
│   └── 数据导出
├── 选课管理
│   └── 学生选课
├── 成绩管理
│   ├── 成绩生成
│   └── 成绩查询
│       ├── 按学生查询
│       ├── 按课程查询
│       └── 按教学班查询
└── 统计分析
    ├── 全校学生排名
    ├── 按课程排名
    └── 分数段统计
```

## 2. 创新点或特色

### 2.1 核心创新点
- **智能选课算法**：基于随机算法的学生选课功能，确保每个学生选择3-5门课程，避免重复选课
- **多维度成绩管理**：支持平时成绩、期中成绩、实验成绩、期末成绩的综合计算
- **灵活排名系统**：提供按平均成绩和学号两种排序方式的排名功能
- **数据导出功能**：支持将排名数据导出为CSV格式，便于数据分析

### 2.2 技术特色
- **企业级架构**：采用MVC分层架构，职责分离清晰
- **数据持久化**：使用MySQL数据库，支持数据持久化存储
- **智能选课**：基于随机算法的智能选课系统
- **多维度成绩管理**：支持四种成绩类型的综合计算
- **数据导出**：支持CSV格式数据导出，便于数据分析

### 2.3 系统特色
- **完整的教学管理流程**：从数据初始化到成绩统计的完整流程
- **灵活的排序方式**：支持按学号、成绩等多种排序方式
- **详细的统计分析**：提供分数段分布、排名统计等功能
- **用户友好界面**：直观的菜单式操作界面

## 3. 设计思想（思路）

### 3.1 面向对象设计
每个实体类都封装了相应的属性和方法，通过接口和抽象类实现代码复用，并通过接口实现多态调用。

### 3.2 分层设计思想
- **表现层（View）**：负责用户界面显示和交互
- **控制层（Controller）**：负责业务流程控制和协调
- **服务层（Service）**：负责业务逻辑处理
- **数据访问层（DAO）**：负责数据库操作
- **实体层（Entity）**：负责数据模型定义

### 3.3 单一职责原则
每个类都有明确的职责，避免功能耦合。服务层专注于业务逻辑，DAO层专注于数据访问。

### 3.4 数据驱动设计
系统采用数据驱动的方式，通过数据库存储和管理所有业务数据，确保数据的一致性和完整性。

## 4. 设计模式的使用

### 4.1 MVC模式
- **Model**：实体类（Student、Teacher、Course、TeachingClass、Score）
- **View**：MenuView类负责用户界面
- **Controller**：SystemController类负责业务控制

### 4.2 DAO模式
每个实体都有对应的DAO类（StudentDAO、TeacherDAO等），并封装了数据库操作，提供统一的数据访问接口。

### 4.3 服务层模式
每个业务领域都有对应的Service类，并封装业务逻辑，提供高级业务操作接口。

### 4.4 单例模式
DatabaseConnection类使用静态方法提供数据库连接，确保数据库连接的一致性和资源管理。


## 5. 程序的结构或者架构

### 5.1 包结构
```
com.scoremanagement/
├── Main.java                    # 程序入口
├── controller/                  # 控制层
│   └── SystemController.java   # 系统控制器
├── view/                       # 视图层
│   └── MenuView.java          # 菜单视图
├── service/                    # 服务层
│   ├── StudentService.java    # 学生服务
│   ├── TeacherService.java    # 教师服务
│   ├── CourseService.java     # 课程服务
│   ├── TeachingClassService.java # 教学班服务
│   └── ScoreService.java      # 成绩服务
├── dao/                        # 数据访问层
│   ├── StudentDAO.java        # 学生数据访问
│   ├── TeacherDAO.java        # 教师数据访问
│   ├── CourseDAO.java         # 课程数据访问
│   ├── TeachingClassDAO.java  # 教学班数据访问
│   └── ScoreDAO.java          # 成绩数据访问
└── model/                      # 模型层
    ├── DatabaseConnection.java # 数据库连接
    └── entity/                 # 实体类
        ├── Student.java       # 学生实体
        ├── Teacher.java       # 教师实体
        ├── Course.java        # 课程实体
        ├── TeachingClass.java # 教学班实体
        └── Score.java         # 成绩实体
```

### 5.2 架构层次关系
```
┌─────────────────┐
│   MenuView      │ ← 用户界面层
├─────────────────┤
│ SystemController│ ← 控制层
├─────────────────┤
│   Service层     │ ← 业务逻辑层
├─────────────────┤
│    DAO层        │ ← 数据访问层
├─────────────────┤
│   Entity层      │ ← 数据模型层
├─────────────────┤
│   Database      │ ← 数据存储层
└─────────────────┘
```

## 6. 程序主要执行流程图

### 6.1 系统启动流程
```
Main.main() → MenuView.start() → MenuView.showMainMenu()
```

### 6.2 系统初始化流程
```
用户选择"1" → MenuView.initializeSystem() → SystemController.initializeSystem()
    ↓
StudentService.generateStudents() → StudentDAO.addStudent()
TeacherService.generateTeachers() → TeacherDAO.addTeacher()
CourseService.generateCourses() → CourseDAO.addCourse()
TeachingClassService.createTeachingClassesForCourses() → TeachingClassDAO.addTeachingClass()
```

### 6.3 学生选课流程
```
用户选择"2" → MenuView.studentCourseSelection() → SystemController.studentCourseSelection()
    ↓
获取所有学生和教学班 → 随机分配学生到教学班 → 更新教学班学生人数
```

### 6.4 成绩生成流程
```
用户选择"3" → MenuView.generateScores() → SystemController.generateScores()
    ↓
获取所有教学班 → 为每个教学班生成成绩 → ScoreService.generateScoresForClass()
    ↓
ScoreDAO.addScore() → 保存成绩到数据库
```

### 6.5 成绩查询流程
```
用户选择"4/5/6/7" → 相应的MenuView方法 → SystemController对应方法
    ↓
Service层查询数据 → DAO层数据库操作 → 返回结果并显示
```

### 6.6 数据流向图
```
用户输入 → MenuView → SystemController → Service层 → DAO层 → MySQL数据库
                ↓
            结果展示 ← 数据处理 ← 业务逻辑 ← 数据查询 ← 数据库操作
```

## 7. 类的说明和类之间的关系图

### 7.1 实体类（Entity）
- **Student**：学生实体，包含学号、姓名、性别、年级
- **Teacher**：教师实体，包含教师编号、姓名
- **Course**：课程实体，包含课程编号、课程名称
- **TeachingClass**：教学班实体，包含班级编号、课程编号、教师编号、学期、学生人数
- **Score**：成绩实体，包含学生编号、班级编号、各项成绩、总成绩、成绩日期

### 7.2 服务类（Service）
- **StudentService**：学生业务逻辑处理
- **TeacherService**：教师业务逻辑处理
- **CourseService**：课程业务逻辑处理
- **TeachingClassService**：教学班业务逻辑处理
- **ScoreService**：成绩业务逻辑处理

### 7.3 数据访问类（DAO）
- **StudentDAO**：学生数据访问操作
- **TeacherDAO**：教师数据访问操作
- **CourseDAO**：课程数据访问操作
- **TeachingClassDAO**：教学班数据访问操作
- **ScoreDAO**：成绩数据访问操作

### 7.4 控制类（Controller）
- **SystemController**：系统控制器，协调各服务层

### 7.5 视图类（View）
- **MenuView**：菜单视图，处理用户交互

### 7.6 类关系图
```
MenuView ──→ SystemController ──→ Service层 ──→ DAO层 ──→ Entity层
    │              │                │           │
    │              │                │           └── Student
    │              │                │           └── Teacher
    │              │                │           └── Course
    │              │                │           └── TeachingClass
    │              │                │           └── Score
    │              │                │
    │              │                ├── StudentService
    │              │                ├── TeacherService
    │              │                ├── CourseService
    │              │                ├── TeachingClassService
    │              │                └── ScoreService
    │              │
    │              └── 业务流程控制
    │
    └── 用户界面交互
```

## 8. 核心源代码及说明

### 8.1 主程序入口
**功能说明**：程序启动入口，负责初始化系统并启动主菜单界面。
```java
// Main.java
package com.scoremanagement;

import com.scoremanagement.view.MenuView;

public class Main {
    public static void main(String[] args) {
        try {
            MenuView menuView = new MenuView();
            menuView.start();
        } catch (Exception e) {
            System.err.println("系统启动失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

### 8.2 系统控制器核心方法
**功能说明**：系统控制器的核心方法，负责协调各个服务层完成系统初始化。
```java
// SystemController.java - 系统初始化
public void initializeSystem() {
    System.out.println("=== 开始初始化系统数据 ===");
    
    // 生成学生数据（120名学生）
    studentService.generateStudents(120);
    
    // 生成教师数据（8名教师）
    teacherService.generateTeachers(8);
    
    // 生成课程数据（5门课程）
    courseService.generateCourses(5);
    
    // 创建教学班
    List<String> courseIds = courseService.getAllCourses().stream()
            .map(Course::getCourseId)
            .collect(Collectors.toList());
    List<String> teacherIds = teacherService.getAllTeachers().stream()
            .map(Teacher::getTeacherId)
            .collect(Collectors.toList());
    
    teachingClassService.createTeachingClassesForCourses(courseIds, teacherIds);
    
    System.out.println("=== 系统数据初始化完成 ===");
}
```

### 8.3 学生选课核心算法
**功能说明**：实现智能选课算法，确保每个学生选择3-5门课程，避免重复选课。
```java
// SystemController.java - 学生选课
public void studentCourseSelection() {
    List<Student> students = studentService.getAllStudents();
    List<TeachingClass> teachingClasses = teachingClassService.getAllTeachingClasses();
    
    for (Student student : students) {
        // 每个学生选择3-5门课程
        int courseCount = 3 + random.nextInt(3);
        List<String> selectedClasses = new ArrayList<>();
        
        // 随机选择教学班
        Collections.shuffle(teachingClasses);
        for (int i = 0; i < courseCount && i < teachingClasses.size(); i++) {
            TeachingClass teachingClass = teachingClasses.get(i);
            selectedClasses.add(teachingClass.getClassId());
        }
        
        // 记录学生选课信息
        studentClassMapping.put(student.getStudentId(), selectedClasses);
    }
    
    // 更新教学班学生人数
    for (TeachingClass teachingClass : teachingClasses) {
        long studentCount = studentClassMapping.values().stream()
                .flatMap(List::stream)
                .filter(classId -> classId.equals(teachingClass.getClassId()))
                .count();
        teachingClass.setStudentCount((int) studentCount);
        teachingClassService.updateTeachingClass(teachingClass);
    }
}
```

### 8.4 成绩生成核心算法
**功能说明**：为选课学生生成各项成绩，包括平时成绩、期中成绩、实验成绩、期末成绩，并计算加权总成绩。
```java
// ScoreService.java - 成绩生成
public String generateScoresForClass(String classId, List<String> studentIds) {
    int totalStudents = studentIds.size();
    int newScores = 0;
    
    for (String studentId : studentIds) {
        // 检查成绩是否已存在
        if (!scoreDAO.scoreExists(studentId, classId)) {
            // 生成各项成绩
            int usualScore = 60 + random.nextInt(41);      // 60-100
            int midtermScore = 60 + random.nextInt(41);    // 60-100
            int experimentScore = 60 + random.nextInt(41); // 60-100
            int finalExamScore = 60 + random.nextInt(41);  // 60-100
            
            // 计算总成绩（加权平均）
            int totalScore = (int) (usualScore * 0.2 + midtermScore * 0.2 + 
                                  experimentScore * 0.2 + finalExamScore * 0.4);
            
            // 创建成绩对象
            Score score = new Score(studentId, classId, usualScore, 
                                  midtermScore, experimentScore, 
                                  finalExamScore, totalScore, new Date());
            
            // 保存成绩
            if (scoreDAO.addScore(score)) {
                newScores++;
            }
        }
    }
    
    return String.format("教学班 %s：共 %d 名学生，新增 %d 条成绩记录", 
                        classId, totalStudents, newScores);
}
```

### 8.5 排名计算核心算法
**功能说明**：计算全校学生排名，按平均成绩降序排序，支持多种排序方式。
```java
// ScoreService.java - 获取全校学生排名
public List<Score> getAllStudentsRanking() {
    List<Score> allScores = scoreDAO.getAllScores();
    
    // 按学生分组计算平均成绩
    Map<String, List<Score>> studentScoresMap = allScores.stream()
            .collect(Collectors.groupingBy(Score::getStudentId));
    
    List<Score> rankings = new ArrayList<>();
    
    for (Map.Entry<String, List<Score>> entry : studentScoresMap.entrySet()) {
        String studentId = entry.getKey();
        List<Score> scores = entry.getValue();
        
        // 计算平均成绩
        double averageScore = scores.stream()
                .mapToInt(Score::getTotalScore)
                .average()
                .orElse(0.0);
        
        // 创建排名记录（使用第一门课程的成绩记录作为载体）
        Score rankingScore = new Score(studentId, scores.get(0).getClassId(),
                scores.get(0).getUsualScore(), scores.get(0).getMidtermScore(),
                scores.get(0).getExperimentScore(), scores.get(0).getFinalExamScore(),
                (int) averageScore, scores.get(0).getGradeDate());
        
        rankings.add(rankingScore);
    }
    
    // 按平均成绩降序排序
    return rankings.stream()
            .sorted((s1, s2) -> Integer.compare(s2.getTotalScore(), s1.getTotalScore()))
            .collect(Collectors.toList());
}
```

### 8.6 数据库连接管理
**功能说明**：管理数据库连接，使用单例模式确保连接的一致性和资源管理。
```java
// DatabaseConnection.java
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/score_management_system";
    private static final String USER = "root";
    private static final String PASSWORD = "BJY1998918";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
}
```

## 9. 人工智能应用情况

### 9.1 AI辅助编程应用
本学生成绩管理系统在开发过程中**大量使用了AI辅助编程技术**，主要体现在以下几个方面：

### 9.2 具体应用场景
- **代码生成**：使用AI工具生成基础代码框架和模板
- **代码优化**：AI辅助进行代码重构和性能优化
- **错误修复**：利用AI工具快速定位和修复编译错误
- **文档生成**：AI辅助生成代码注释和项目文档
- **架构设计**：AI协助进行系统架构设计和模式选择
- **数据库设计**：AI辅助设计数据库表结构和关系

### 9.3 AI工具使用情况
- **代码补全**：使用AI代码补全工具提高开发效率
- **代码审查**：AI辅助进行代码质量检查和规范验证
- **测试用例生成**：AI协助生成单元测试用例
- **文档编写**：AI辅助编写技术文档和用户手册

### 9.4 开发效率提升
- **开发速度**：AI辅助编程显著提高了代码编写速度
- **代码质量**：AI工具帮助发现潜在问题，提升代码质量
- **学习效率**：通过AI辅助，快速学习和应用新的编程技术
- **最佳实践**：AI推荐最佳编程实践和设计模式

## 10. 其他

### 10.1 技术栈
- **开发语言**：Java 25
- **数据库**：MySQL 8.0.43
- **数据库驱动**：MySQL Connector/J (com.mysql.cj.jdbc.Driver)
- **设计模式**：MVC、DAO、Service Layer、单例模式
- **数据持久化**：JDBC + MySQL
- **设计工具**：PowerDesigner（类图和时序图设计）、Mermaid（流程图绘制）

### 10.2 系统特点
- **企业级架构**：采用MVC分层架构，职责分离清晰
- **数据持久化**：使用MySQL数据库，支持数据持久化存储
- **智能选课**：基于随机算法的智能选课系统
- **多维度成绩管理**：支持四种成绩类型的综合计算
- **数据导出**：支持CSV格式数据导出，便于数据分析

### 10.3 项目总结
本学生成绩管理系统采用分层架构设计，实现了完整的学生选课、成绩管理、排名统计等功能。系统具有良好的可扩展性和维护性，代码结构清晰，职责分离明确。通过使用MVC模式、DAO模式等设计模式，确保了系统的稳定性和可维护性。系统支持数据持久化存储，提供了友好的用户界面，能够满足实际的教学管理需求。

### 10.4 代码质量
- **良好的注释**：代码注释详细，便于理解和维护
- **异常处理**：完善的异常处理机制
- **代码规范**：遵循Java编码规范
- **模块化设计**：高内聚低耦合的模块化设计

