package dao;

import util.DatabaseUtil;
import util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 用户角色关联数据访问层
 */
public class UserRoleDao {
    private static final Logger logger = LoggerUtil.getLogger(UserRoleDao.class.getName());
    
    /**
     * 为用户分配角色
     */
    public boolean assignRoleToUser(Integer userId, Integer roleId) {
        String sql = "INSERT INTO user_role (user_id, role_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, roleId);
            
            int result = pstmt.executeUpdate();
            logger.info("为用户ID: " + userId + " 分配角色ID: " + roleId + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("分配角色失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 移除用户的角色
     */
    public boolean removeRoleFromUser(Integer userId, Integer roleId) {
        String sql = "DELETE FROM user_role WHERE user_id = ? AND role_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, roleId);
            
            int result = pstmt.executeUpdate();
            logger.info("移除用户ID: " + userId + " 的角色ID: " + roleId + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("移除角色失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 获取用户的所有角色ID
     */
    public List<Integer> getRoleIdsByUserId(Integer userId) {
        String sql = "SELECT role_id FROM user_role WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> roleIds = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                roleIds.add(rs.getInt("role_id"));
            }
        } catch (SQLException e) {
            logger.severe("查询用户角色失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return roleIds;
    }
    
    /**
     * 获取角色的所有用户ID
     */
    public List<Integer> getUserIdsByRoleId(Integer roleId) {
        String sql = "SELECT user_id FROM user_role WHERE role_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> userIds = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                userIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            logger.severe("查询角色用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return userIds;
    }
    
    /**
     * 检查用户是否已分配该角色
     */
    public boolean hasRole(Integer userId, Integer roleId) {
        String sql = "SELECT COUNT(*) as count FROM user_role WHERE user_id = ? AND role_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, roleId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            logger.severe("检查用户角色关联失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return false;
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


