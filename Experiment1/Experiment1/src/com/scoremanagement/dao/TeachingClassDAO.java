package com.scoremanagement.dao;

import com.scoremanagement.model.entity.TeachingClass;
import com.scoremanagement.model.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeachingClassDAO {

    // 获取所有教学班
    public List<TeachingClass> getAllTeachingClasses() {
        List<TeachingClass> teachingClasses = new ArrayList<>();
        String query = "SELECT * FROM teaching_classes";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                TeachingClass teachingClass = new TeachingClass(
                        rs.getString("class_id"),
                        rs.getString("course_id"),
                        rs.getString("teacher_id"),
                        rs.getString("semester"),
                        rs.getInt("student_count")
                );
                teachingClasses.add(teachingClass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teachingClasses;
    }

    // 根据教学班编号获取教学班
    public TeachingClass getTeachingClassById(String classId) {
        String query = "SELECT * FROM teaching_classes WHERE class_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, classId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new TeachingClass(
                            rs.getString("class_id"),
                            rs.getString("course_id"),
                            rs.getString("teacher_id"),
                            rs.getString("semester"),
                            rs.getInt("student_count")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 添加教学班
    public void addTeachingClass(TeachingClass teachingClass) {
        String query = "INSERT INTO teaching_classes (class_id, course_id, teacher_id, semester, student_count) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, teachingClass.getClassId());
            pstmt.setString(2, teachingClass.getCourseId());
            pstmt.setString(3, teachingClass.getTeacherId());
            pstmt.setString(4, teachingClass.getSemester());
            pstmt.setInt(5, teachingClass.getStudentCount());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 更新教学班信息
    public void updateTeachingClass(TeachingClass teachingClass) {
        String query = "UPDATE teaching_classes SET course_id = ?, teacher_id = ?, semester = ?, student_count = ? WHERE class_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, teachingClass.getCourseId());
            pstmt.setString(2, teachingClass.getTeacherId());
            pstmt.setString(3, teachingClass.getSemester());
            pstmt.setInt(4, teachingClass.getStudentCount());
            pstmt.setString(5, teachingClass.getClassId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除教学班
    public void deleteTeachingClass(String classId) {
        String query = "DELETE FROM teaching_classes WHERE class_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, classId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
