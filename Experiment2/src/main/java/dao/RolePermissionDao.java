package dao;

import util.DatabaseUtil;
import util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 角色权限关联数据访问层
 */
public class RolePermissionDao {
    private static final Logger logger = LoggerUtil.getLogger(RolePermissionDao.class.getName());
    
    /**
     * 为角色分配权限
     */
    public boolean assignPermissionToRole(Integer roleId, Integer permissionId) {
        String sql = "INSERT INTO role_permission (role_id, permission_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleId);
            pstmt.setInt(2, permissionId);
            
            int result = pstmt.executeUpdate();
            logger.info("为角色ID: " + roleId + " 分配权限ID: " + permissionId + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("分配权限失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 移除角色的权限
     */
    public boolean removePermissionFromRole(Integer roleId, Integer permissionId) {
        String sql = "DELETE FROM role_permission WHERE role_id = ? AND permission_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleId);
            pstmt.setInt(2, permissionId);
            
            int result = pstmt.executeUpdate();
            logger.info("移除角色ID: " + roleId + " 的权限ID: " + permissionId + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("移除权限失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 获取角色的所有权限ID
     */
    public List<Integer> getPermissionIdsByRoleId(Integer roleId) {
        String sql = "SELECT permission_id FROM role_permission WHERE role_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> permissionIds = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                permissionIds.add(rs.getInt("permission_id"));
            }
        } catch (SQLException e) {
            logger.severe("查询角色权限失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return permissionIds;
    }
    
    /**
     * 获取权限的所有角色ID
     */
    public List<Integer> getRoleIdsByPermissionId(Integer permissionId) {
        String sql = "SELECT role_id FROM role_permission WHERE permission_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Integer> roleIds = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, permissionId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                roleIds.add(rs.getInt("role_id"));
            }
        } catch (SQLException e) {
            logger.severe("查询权限角色失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return roleIds;
    }
    
    /**
     * 检查角色是否已分配该权限
     */
    public boolean hasPermission(Integer roleId, Integer permissionId) {
        String sql = "SELECT COUNT(*) as count FROM role_permission WHERE role_id = ? AND permission_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roleId);
            pstmt.setInt(2, permissionId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            logger.severe("检查角色权限关联失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return false;
    }
    
    /**
     * 检查用户是否拥有指定权限（通过角色）
     */
    public boolean hasPermissionByUser(Integer userId, String permissionCode) {
        String sql = "SELECT COUNT(*) as count FROM role_permission rp " +
                     "INNER JOIN user_role ur ON rp.role_id = ur.role_id " +
                     "INNER JOIN permission p ON rp.permission_id = p.id " +
                     "WHERE ur.user_id = ? AND p.permission_code = ? AND p.status = 1";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, permissionCode);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            logger.severe("权限校验失败: " + e.getMessage());
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

