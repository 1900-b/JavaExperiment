package dao;

import entity.Resource;
import util.DatabaseUtil;
import util.LoggerUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 资源数据访问层
 */
public class ResourceDao {
    private static final Logger logger = LoggerUtil.getLogger(ResourceDao.class.getName());
    
    /**
     * 创建资源
     */
    public boolean createResource(Resource resource) {
        String sql = "INSERT INTO resource (resource_name, resource_code, resource_type, resource_path, description, status, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, resource.getResourceName());
            pstmt.setString(2, resource.getResourceCode());
            pstmt.setString(3, resource.getResourceType());
            pstmt.setString(4, resource.getResourcePath());
            pstmt.setString(5, resource.getDescription());
            pstmt.setInt(6, resource.getStatus());
            pstmt.setTimestamp(7, new Timestamp(resource.getCreateTime().getTime()));
            pstmt.setTimestamp(8, new Timestamp(resource.getUpdateTime().getTime()));
            
            int result = pstmt.executeUpdate();
            logger.info("创建资源: " + resource.getResourceName() + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("创建资源失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 根据ID查询资源
     */
    public Resource getResourceById(Integer id) {
        String sql = "SELECT * FROM resource WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToResource(rs);
            }
        } catch (SQLException e) {
            logger.severe("查询资源失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 根据资源编码查询资源
     */
    public Resource getResourceByCode(String resourceCode) {
        String sql = "SELECT * FROM resource WHERE resource_code = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, resourceCode);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToResource(rs);
            }
        } catch (SQLException e) {
            logger.severe("查询资源失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }
    
    /**
     * 查询所有资源
     */
    public List<Resource> getAllResources() {
        String sql = "SELECT * FROM resource ORDER BY id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Resource> resources = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                resources.add(mapResultSetToResource(rs));
            }
        } catch (SQLException e) {
            logger.severe("查询所有资源失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return resources;
    }
    
    /**
     * 根据资源类型查询资源
     */
    public List<Resource> getResourcesByType(String resourceType) {
        String sql = "SELECT * FROM resource WHERE resource_type = ? ORDER BY id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Resource> resources = new ArrayList<>();
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, resourceType);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                resources.add(mapResultSetToResource(rs));
            }
        } catch (SQLException e) {
            logger.severe("根据类型查询资源失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return resources;
    }
    
    /**
     * 更新资源
     */
    public boolean updateResource(Resource resource) {
        String sql = "UPDATE resource SET resource_name = ?, resource_code = ?, resource_type = ?, resource_path = ?, description = ?, status = ?, update_time = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, resource.getResourceName());
            pstmt.setString(2, resource.getResourceCode());
            pstmt.setString(3, resource.getResourceType());
            pstmt.setString(4, resource.getResourcePath());
            pstmt.setString(5, resource.getDescription());
            pstmt.setInt(6, resource.getStatus());
            pstmt.setTimestamp(7, new Timestamp(resource.getUpdateTime().getTime()));
            pstmt.setInt(8, resource.getId());
            
            int result = pstmt.executeUpdate();
            logger.info("更新资源: " + resource.getResourceName() + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("更新资源失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 删除资源
     */
    public boolean deleteResource(Integer id) {
        String sql = "DELETE FROM resource WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            int result = pstmt.executeUpdate();
            logger.info("删除资源ID: " + id + ", 结果: " + (result > 0 ? "成功" : "失败"));
            return result > 0;
        } catch (SQLException e) {
            logger.severe("删除资源失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 将ResultSet映射为Resource对象
     */
    private Resource mapResultSetToResource(ResultSet rs) throws SQLException {
        Resource resource = new Resource();
        resource.setId(rs.getInt("id"));
        resource.setResourceName(rs.getString("resource_name"));
        resource.setResourceCode(rs.getString("resource_code"));
        resource.setResourceType(rs.getString("resource_type"));
        resource.setResourcePath(rs.getString("resource_path"));
        resource.setDescription(rs.getString("description"));
        resource.setStatus(rs.getInt("status"));
        resource.setCreateTime(rs.getTimestamp("create_time"));
        resource.setUpdateTime(rs.getTimestamp("update_time"));
        return resource;
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

