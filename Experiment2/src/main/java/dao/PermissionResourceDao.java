package dao;

import util.DatabaseUtil;
import util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 权限资源关联数据访问层
 */
public class PermissionResourceDao {
    private static final Logger logger = LoggerUtil.getLogger(PermissionResourceDao.class.getName());
    
    /**
     * 为权限分配资源
     */
    public boolean assignResourceToPermission(Integer permissionId, Integer resourceId) {
        String sql = "INSERT INTO permission_resource (permission_id, resource_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, permissionId);
            pstmt.setInt(2, resourceId);
            
            int result = pstmt.executeUpdate();
            logger.info("为权限ID: " + permissionId + " 分配资源ID: " + resourceId + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("分配资源失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 移除权限的资源
     */
    public boolean removeResourceFromPermission(Integer permissionId, Integer resourceId) {
        String sql = "DELETE FROM permission_resource WHERE permission_id = ? AND resource_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, permissionId);
            pstmt.setInt(2, resourceId);
            
            int result = pstmt.executeUpdate();
            logger.info("移除权限ID: " + permissionId + " 的资源ID: " + resourceId + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("移除资源失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 获取权限的所有资源ID
     */
    public List<Integer> getResourceIdsByPermissionId(Integer permissionId) {
        String sql = "SELECT resource_id FROM permission_resource WHERE permission_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> resourceIds = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, permissionId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                resourceIds.add(rs.getInt("resource_id"));
            }
        } catch (SQLException e) {
            logger.severe("查询权限资源失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return resourceIds;
    }
    
    /**
     * 检查权限是否已分配该资源
     */
    public boolean hasResource(Integer permissionId, Integer resourceId) {
        String sql = "SELECT COUNT(*) as count FROM permission_resource WHERE permission_id = ? AND resource_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, permissionId);
            pstmt.setInt(2, resourceId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            logger.severe("检查权限资源关联失败: " + e.getMessage());
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

