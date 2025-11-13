package dao;

import entity.Permission;
import util.DatabaseUtil;
import util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 权限数据访问层
 */
public class PermissionDao {
    private static final Logger logger = LoggerUtil.getLogger(PermissionDao.class.getName());
    
    /**
     * 创建权限
     */
    public boolean createPermission(Permission permission) {
        String sql = "INSERT INTO permission (permission_name, permission_code, permission_type, description, status, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, permission.getPermissionName());
            pstmt.setString(2, permission.getPermissionCode());
            pstmt.setString(3, permission.getPermissionType());
            pstmt.setString(4, permission.getDescription());
            pstmt.setInt(5, permission.getStatus());
            pstmt.setTimestamp(6, new Timestamp(permission.getCreateTime().getTime()));
            pstmt.setTimestamp(7, new Timestamp(permission.getUpdateTime().getTime()));
            
            int result = pstmt.executeUpdate();
            logger.info("创建权限: " + permission.getPermissionName() + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("创建权限失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 根据ID查询权限
     */
    public Permission getPermissionById(Integer id) {
        String sql = "SELECT * FROM permission WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPermission(rs);
            }
        } catch (SQLException e) {
            logger.severe("查询权限失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 根据权限编码查询权限
     */
    public Permission getPermissionByCode(String permissionCode) {
        String sql = "SELECT * FROM permission WHERE permission_code = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, permissionCode);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPermission(rs);
            }
        } catch (SQLException e) {
            logger.severe("查询权限失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 查询所有权限
     */
    public List<Permission> getAllPermissions() {
        String sql = "SELECT * FROM permission ORDER BY id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Permission> permissions = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                permissions.add(mapResultSetToPermission(rs));
            }
        } catch (SQLException e) {
            logger.severe("查询所有权限失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return permissions;
    }
    
    /**
     * 根据权限类型查询权限
     */
    public List<Permission> getPermissionsByType(String permissionType) {
        String sql = "SELECT * FROM permission WHERE permission_type = ? ORDER BY id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Permission> permissions = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, permissionType);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                permissions.add(mapResultSetToPermission(rs));
            }
        } catch (SQLException e) {
            logger.severe("根据类型查询权限失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return permissions;
    }
    
    /**
     * 更新权限
     */
    public boolean updatePermission(Permission permission) {
        String sql = "UPDATE permission SET permission_name = ?, permission_code = ?, permission_type = ?, description = ?, status = ?, update_time = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, permission.getPermissionName());
            pstmt.setString(2, permission.getPermissionCode());
            pstmt.setString(3, permission.getPermissionType());
            pstmt.setString(4, permission.getDescription());
            pstmt.setInt(5, permission.getStatus());
            pstmt.setTimestamp(6, new Timestamp(permission.getUpdateTime().getTime()));
            pstmt.setInt(7, permission.getId());
            
            int result = pstmt.executeUpdate();
            logger.info("更新权限: " + permission.getPermissionName() + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("更新权限失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 删除权限
     */
    public boolean deletePermission(Integer id) {
        String sql = "DELETE FROM permission WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int result = pstmt.executeUpdate();
            logger.info("删除权限ID: " + id + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("删除权限失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 将ResultSet映射为Permission对象
     */
    private Permission mapResultSetToPermission(ResultSet rs) throws SQLException {
        Permission permission = new Permission();
        permission.setId(rs.getInt("id"));
        permission.setPermissionName(rs.getString("permission_name"));
        permission.setPermissionCode(rs.getString("permission_code"));
        permission.setPermissionType(rs.getString("permission_type"));
        permission.setDescription(rs.getString("description"));
        permission.setStatus(rs.getInt("status"));
        permission.setCreateTime(rs.getTimestamp("create_time"));
        permission.setUpdateTime(rs.getTimestamp("update_time"));
        return permission;
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


