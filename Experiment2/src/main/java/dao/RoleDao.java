package dao;

import entity.Role;
import util.DatabaseUtil;
import util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 角色数据访问层
 */
public class RoleDao {
    private static final Logger logger = LoggerUtil.getLogger(RoleDao.class.getName());
    
    /**
     * 创建角色
     */
    public boolean createRole(Role role) {
        String sql = "INSERT INTO role (role_name, role_code, description, status, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role.getRoleName());
            pstmt.setString(2, role.getRoleCode());
            pstmt.setString(3, role.getDescription());
            pstmt.setInt(4, role.getStatus());
            pstmt.setTimestamp(5, new Timestamp(role.getCreateTime().getTime()));
            pstmt.setTimestamp(6, new Timestamp(role.getUpdateTime().getTime()));
            
            int result = pstmt.executeUpdate();
            logger.info("创建角色: " + role.getRoleName() + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("创建角色失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 根据ID查询角色
     */
    public Role getRoleById(Integer id) {
        String sql = "SELECT * FROM role WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRole(rs);
            }
        } catch (SQLException e) {
            logger.severe("查询角色失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 根据角色编码查询角色
     */
    public Role getRoleByCode(String roleCode) {
        String sql = "SELECT * FROM role WHERE role_code = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roleCode);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToRole(rs);
            }
        } catch (SQLException e) {
            logger.severe("查询角色失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 查询所有角色
     */
    public List<Role> getAllRoles() {
        String sql = "SELECT * FROM role ORDER BY id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Role> roles = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                roles.add(mapResultSetToRole(rs));
            }
        } catch (SQLException e) {
            logger.severe("查询所有角色失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return roles;
    }
    
    /**
     * 更新角色
     */
    public boolean updateRole(Role role) {
        String sql = "UPDATE role SET role_name = ?, role_code = ?, description = ?, status = ?, update_time = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role.getRoleName());
            pstmt.setString(2, role.getRoleCode());
            pstmt.setString(3, role.getDescription());
            pstmt.setInt(4, role.getStatus());
            pstmt.setTimestamp(5, new Timestamp(role.getUpdateTime().getTime()));
            pstmt.setInt(6, role.getId());
            
            int result = pstmt.executeUpdate();
            logger.info("更新角色: " + role.getRoleName() + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("更新角色失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 删除角色
     */
    public boolean deleteRole(Integer id) {
        String sql = "DELETE FROM role WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int result = pstmt.executeUpdate();
            logger.info("删除角色ID: " + id + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("删除角色失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 将ResultSet映射为Role对象
     */
    private Role mapResultSetToRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setId(rs.getInt("id"));
        role.setRoleName(rs.getString("role_name"));
        role.setRoleCode(rs.getString("role_code"));
        role.setDescription(rs.getString("description"));
        role.setStatus(rs.getInt("status"));
        role.setCreateTime(rs.getTimestamp("create_time"));
        role.setUpdateTime(rs.getTimestamp("update_time"));
        return role;
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

