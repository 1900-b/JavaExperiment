package com.scoremanagement.service;

import com.scoremanagement.dao.ScoreDAO;
import com.scoremanagement.model.entity.Score;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 成绩业务逻辑服务类
 * 负责成绩相关的业务逻辑处理
 */
public class ScoreService {
    private ScoreDAO scoreDAO;
    private Random random;

    public ScoreService() {
        this.scoreDAO = new ScoreDAO();
        this.random = new Random();
    }

    /**
     * 获取所有成绩
     */
    public List<Score> getAllScores() {
        return scoreDAO.getAllScores();
    }

    /**
     * 添加成绩
     */
    public boolean addScore(Score score) {
        return scoreDAO.addScore(score);
    }

    /**
     * 为教学班生成成绩
     */
    public String generateScoresForClass(String classId, List<String> studentIds) {
        int totalStudents = studentIds.size();
        int newScores = 0;
        int existingScores = 0;
        
        for (String studentId : studentIds) {
            // 生成各项成绩（0-100分）
            int usualScore = generateScore(60, 100);      // 平时成绩：60-100
            int midtermScore = generateScore(50, 100);    // 期中成绩：50-100
            int experimentScore = generateScore(70, 100); // 实验成绩：70-100
            int finalExamScore = generateScore(40, 100);  // 期末成绩：40-100
            
            // 计算综合成绩（权重：平时20%，期中20%，实验20%，期末40%）
            int totalScore = (int) (usualScore * 0.2 + midtermScore * 0.2 + 
                                  experimentScore * 0.2 + finalExamScore * 0.4);
            
            // 确保综合成绩在0-100范围内
            totalScore = Math.max(0, Math.min(100, totalScore));
            
            Score score = new Score(studentId, classId, usualScore, midtermScore, 
                                   experimentScore, finalExamScore, totalScore, new Date());
            
            if (addScore(score)) {
                newScores++;
            } else {
                existingScores++;
            }
        }
        
        return String.format("教学班 %s 成绩生成完成：共 %d 名学生，新增 %d 条记录，跳过 %d 条已存在记录", 
                           classId, totalStudents, newScores, existingScores);
    }

    private int generateScore(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 根据学号获取学生所有成绩
     */
    public List<Score> getScoresByStudentId(String studentId) {
        return getAllScores().stream()
                .filter(score -> score.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    /**
     * 根据教学班编号获取该班所有成绩
     */
    public List<Score> getScoresByClassId(String classId) {
        return getAllScores().stream()
                .filter(score -> score.getClassId().equals(classId))
                .collect(Collectors.toList());
    }

    /**
     * 按学号排序显示成绩
     */
    public List<Score> getScoresSortedByStudentId(String classId) {
        return getScoresByClassId(classId).stream()
                .sorted(Comparator.comparing(Score::getStudentId))
                .collect(Collectors.toList());
    }       

    /**
     * 按平均成绩排序显示成绩
     */
    public List<Score> getScoresSortedByTotalScore(String classId) {
        return getScoresByClassId(classId).stream()
                .sorted(Comparator.comparing(Score::getTotalScore).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 统计分数段分布
     */
    public Map<String, Integer> getScoreDistribution(String classId) {
        List<Score> scores = getScoresByClassId(classId);
        Map<String, Integer> distribution = new HashMap<>();
        
        int excellent = 0;  // 90-100
        int good = 0;       // 80-89
        int medium = 0;      // 70-79
        int pass = 0;        // 60-69
        int fail = 0;        // 0-59
        
        for (Score score : scores) {
            int total = score.getTotalScore();
            if (total >= 90) excellent++;
            else if (total >= 80) good++;
            else if (total >= 70) medium++;
            else if (total >= 60) pass++;
            else fail++;
        }
        
        distribution.put("优秀(90-100)", excellent);
        distribution.put("良好(80-89)", good);
        distribution.put("中等(70-79)", medium);
        distribution.put("及格(60-69)", pass);
        distribution.put("不及格(0-59)", fail);
        
        return distribution;
    }

    /**
     * 计算学生平均成绩排名
     */
    public List<Score> getAllStudentsRanking() {
        return getAllScores().stream()
                .collect(Collectors.groupingBy(Score::getStudentId))
                .values().stream()
                .map(scores -> {
                    // 计算每个学生的平均平均成绩
                    double avgTotal = scores.stream()
                            .mapToInt(Score::getTotalScore)
                            .average()
                            .orElse(0.0);
                    
                    // 创建一个新的Score对象用于排名显示
                    Score firstScore = scores.get(0);
                    Score rankingScore = new Score(firstScore.getStudentId(), "RANKING", 
                                                0, 0, 0, 0, (int)avgTotal, new Date());
                    return rankingScore;
                })
                .sorted(Comparator.comparing(Score::getTotalScore).reversed())
                .collect(Collectors.toList());
    }
}
