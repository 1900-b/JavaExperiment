package dao;

import entity.AuditLog;
import util.DatabaseUtil;
import util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 权限审计日志数据访问层
 */
public class AuditLogDao {
    private static final Logger logger = LoggerUtil.getLogger(AuditLogDao.class.getName());
    
    /**
     * 创建审计日志
     */
    public boolean createAuditLog(AuditLog auditLog) {
        String sql = "INSERT INTO audit_log (user_id, username, operation, target_type, target_id, description, result, create_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, auditLog.getUserId());
            pstmt.setString(2, auditLog.getUsername());
            pstmt.setString(3, auditLog.getOperation());
            pstmt.setString(4, auditLog.getTargetType());
            pstmt.setString(5, auditLog.getTargetId());
            pstmt.setString(6, auditLog.getDescription());
            pstmt.setString(7, auditLog.getResult());
            pstmt.setTimestamp(8, new Timestamp(auditLog.getCreateTime().getTime()));
            
            int result = pstmt.executeUpdate();
            logger.info("创建审计日志: " + auditLog.getDescription() + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("创建审计日志失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 查询所有审计日志
     */
    public List<AuditLog> getAllAuditLogs() {
        String sql = "SELECT * FROM audit_log ORDER BY create_time ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AuditLog> logs = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                logs.add(mapResultSetToAuditLog(rs));
            }
        } catch (SQLException e) {
            logger.severe("查询所有审计日志失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return logs;
    }
    
    /**
     * 根据用户ID查询审计日志
     */
    public List<AuditLog> getAuditLogsByUserId(Integer userId) {
        String sql = "SELECT * FROM audit_log WHERE user_id = ? ORDER BY create_time ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AuditLog> logs = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                logs.add(mapResultSetToAuditLog(rs));
            }
        } catch (SQLException e) {
            logger.severe("查询用户审计日志失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return logs;
    }
    
    /**
     * 根据操作类型查询审计日志
     */
    public List<AuditLog> getAuditLogsByOperation(String operation) {
        String sql = "SELECT * FROM audit_log WHERE operation = ? ORDER BY create_time ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<AuditLog> logs = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, operation);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                logs.add(mapResultSetToAuditLog(rs));
            }
        } catch (SQLException e) {
            logger.severe("查询操作审计日志失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return logs;
    }
    
    /**
     * 将ResultSet映射为AuditLog对象
     * 注意：会从user表查询真实用户名，确保显示的是当前数据库中的用户名
     */
    private AuditLog mapResultSetToAuditLog(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setId(rs.getInt("id"));
        Integer userId = rs.getObject("user_id") != null ? rs.getInt("user_id") : null;
        log.setUserId(userId);
        
        // 从user表查询真实用户名，确保显示的是当前数据库中的用户名
        // 这样可以避免历史数据中可能存在的用户名不一致问题
        String realUsername = getRealUsernameByUserId(userId);
        if (realUsername != null) {
            log.setUsername(realUsername);
        } else {
            // 如果查询不到，使用审计日志中存储的用户名作为备用
            log.setUsername(rs.getString("username"));
        }
        
        log.setOperation(rs.getString("operation"));
        log.setTargetType(rs.getString("target_type"));
        log.setTargetId(rs.getString("target_id"));
        log.setDescription(rs.getString("description"));
        log.setResult(rs.getString("result"));
        log.setCreateTime(rs.getTimestamp("create_time"));
        return log;
    }
    
    /**
     * 根据用户ID从user表查询真实用户名
     */
    private String getRealUsernameByUserId(Integer userId) {
        if (userId == null) {
            return null;
        }
        
        String sql = "SELECT username FROM user WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            logger.warning("查询用户ID=" + userId + "的真实用户名失败: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 关闭资源
     */
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) DatabaseUtil.closeConnection(conn);
        } catch (SQLException e) {
            logger.severe("关闭资源失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


