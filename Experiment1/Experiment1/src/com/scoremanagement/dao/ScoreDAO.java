package com.scoremanagement.dao;

import com.scoremanagement.model.entity.Score;
import com.scoremanagement.model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreDAO {

    // 获取所有成绩
    public List<Score> getAllScores() {
        List<Score> scores = new ArrayList<>();
        String query = "SELECT * FROM scores";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Score score = new Score(
                        rs.getString("student_id"),
                        rs.getString("class_id"),
                        rs.getInt("usual_score"),
                        rs.getInt("midterm_score"),
                        rs.getInt("experiment_score"),
                        rs.getInt("final_exam_score"),
                        rs.getInt("total_score"),
                        rs.getDate("grade_date")
                );
                scores.add(score);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

    // 检查成绩是否已存在
    public boolean scoreExists(String studentId, String classId) {
        String query = "SELECT COUNT(*) FROM scores WHERE student_id = ? AND class_id = ?";
        
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            
            pstmt.setString(1, studentId);
            pstmt.setString(2, classId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 添加成绩（带重复检查）
    public boolean addScore(Score score) {
        // 先检查是否已存在
        if (scoreExists(score.getStudentId(), score.getClassId())) {
            System.out.println("成绩已存在，跳过插入: " + score.getStudentId() + "-" + score.getClassId());
            return false;
        }
        
        String query = "INSERT INTO scores (student_id, class_id, usual_score, midterm_score, experiment_score, final_exam_score, total_score, grade_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, score.getStudentId());
            pstmt.setString(2, score.getClassId());
            pstmt.setInt(3, score.getUsualScore());
            pstmt.setInt(4, score.getMidtermScore());
            pstmt.setInt(5, score.getExperimentScore());
            pstmt.setInt(6, score.getFinalExamScore());
            pstmt.setInt(7, score.getTotalScore());
            pstmt.setDate(8, new java.sql.Date(score.getGradeDate().getTime()));
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("插入成绩失败: " + e.getMessage());
            return false;
        }
    }
}
