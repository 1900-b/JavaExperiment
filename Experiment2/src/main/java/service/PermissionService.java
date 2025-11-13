package service;

import dao.AuditLogDao;
import dao.PermissionDao;
import dao.PermissionResourceDao;
import entity.AuditLog;
import entity.Permission;
import util.LoggerUtil;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * 权限业务逻辑层
 */
public class PermissionService {
    private static final Logger logger = LoggerUtil.getLogger(PermissionService.class.getName());
    private PermissionDao permissionDao = new PermissionDao();
    private PermissionResourceDao permissionResourceDao = new PermissionResourceDao();
    private AuditLogDao auditLogDao = new AuditLogDao();
    
    /**
     * 创建权限
     */
    public boolean createPermission(Permission permission, Integer currentUserId, String currentUsername) {
        if (permissionDao.getPermissionByCode(permission.getPermissionCode()) != null) {
            logger.warning("创建权限失败: 权限编码已存在 - " + permission.getPermissionCode());
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "PERMISSION", null, 
                "创建权限失败: 权限编码已存在 - " + permission.getPermissionCode(), "FAILURE"));
            return false;
        }
        
        boolean result = permissionDao.createPermission(permission);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "PERMISSION", 
                String.valueOf(permission.getId()), "创建权限: " + permission.getPermissionName(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "PERMISSION", null, 
                "创建权限失败: " + permission.getPermissionName(), "FAILURE"));
        }
        return result;
    }
    
    /**
     * 根据ID查询权限
     */
    public Permission getPermissionById(Integer id) {
        return permissionDao.getPermissionById(id);
    }
    
    /**
     * 根据权限编码查询权限
     */
    public Permission getPermissionByCode(String permissionCode) {
        return permissionDao.getPermissionByCode(permissionCode);
    }
    
    /**
     * 查询所有权限
     */
    public List<Permission> getAllPermissions() {
        return permissionDao.getAllPermissions();
    }
    
    /**
     * 根据权限类型查询权限
     */
    public List<Permission> getPermissionsByType(String permissionType) {
        return permissionDao.getPermissionsByType(permissionType);
    }
    
    /**
     * 更新权限
     */
    public boolean updatePermission(Permission permission, Integer currentUserId, String currentUsername) {
        permission.setUpdateTime(new Date());
        boolean result = permissionDao.updatePermission(permission);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "UPDATE", "PERMISSION", 
                String.valueOf(permission.getId()), "更新权限: " + permission.getPermissionName(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "UPDATE", "PERMISSION", 
                String.valueOf(permission.getId()), "更新权限失败: " + permission.getPermissionName(), "FAILURE"));
        }
        return result;
    }
    
    /**
     * 删除权限
     */
    public boolean deletePermission(Integer id, Integer currentUserId, String currentUsername) {
        Permission permission = permissionDao.getPermissionById(id);
        if (permission == null) {
            logger.warning("删除权限失败: 权限不存在 - ID: " + id);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "PERMISSION", 
                String.valueOf(id), "删除权限失败: 权限不存在", "FAILURE"));
            return false;
        }
        
        boolean result = permissionDao.deletePermission(id);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "PERMISSION", 
                String.valueOf(id), "删除权限: " + permission.getPermissionName(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "PERMISSION", 
                String.valueOf(id), "删除权限失败: " + permission.getPermissionName(), "FAILURE"));
        }
        return result;
    }
    
    /**
     * 为权限分配资源
     */
    public boolean assignResourceToPermission(Integer permissionId, Integer resourceId, Integer currentUserId, String currentUsername) {
        Permission permission = permissionDao.getPermissionById(permissionId);
        if (permission == null) {
            logger.warning("分配资源失败: 权限不存在 - ID: " + permissionId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "PERMISSION_RESOURCE", 
                String.valueOf(permissionId), "分配资源失败: 权限不存在", "FAILURE"));
            return false;
        }
        
        if (permissionResourceDao.hasResource(permissionId, resourceId)) {
            logger.warning("分配资源失败: 权限已拥有该资源 - 权限ID: " + permissionId + ", 资源ID: " + resourceId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "PERMISSION_RESOURCE", 
                String.valueOf(permissionId), "分配资源失败: 权限已拥有该资源", "FAILURE"));
            return false;
        }
        
        boolean result = permissionResourceDao.assignResourceToPermission(permissionId, resourceId);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "PERMISSION_RESOURCE", 
                String.valueOf(permissionId), "为权限分配资源: 权限ID=" + permissionId + ", 资源ID=" + resourceId, "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "PERMISSION_RESOURCE", 
                String.valueOf(permissionId), "分配资源失败: 权限ID=" + permissionId + ", 资源ID=" + resourceId, "FAILURE"));
        }
        return result;
    }
    
    /**
     * 移除权限的资源
     */
    public boolean removeResourceFromPermission(Integer permissionId, Integer resourceId, Integer currentUserId, String currentUsername) {
        Permission permission = permissionDao.getPermissionById(permissionId);
        if (permission == null) {
            logger.warning("移除资源失败: 权限不存在 - ID: " + permissionId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "PERMISSION_RESOURCE", 
                String.valueOf(permissionId), "移除资源失败: 权限不存在", "FAILURE"));
            return false;
        }
    
        if (!permissionResourceDao.hasResource(permissionId, resourceId)) {
            logger.warning("移除资源失败: 权限未拥有该资源 - 权限ID: " + permissionId + ", 资源ID: " + resourceId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "PERMISSION_RESOURCE", 
                String.valueOf(permissionId), "移除资源失败: 权限未拥有该资源", "FAILURE"));
            return false;
        }
        
        boolean result = permissionResourceDao.removeResourceFromPermission(permissionId, resourceId);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "PERMISSION_RESOURCE", 
                String.valueOf(permissionId), "移除权限资源: 权限ID=" + permissionId + ", 资源ID=" + resourceId, "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "PERMISSION_RESOURCE", 
                String.valueOf(permissionId), "移除资源失败: 权限ID=" + permissionId + ", 资源ID=" + resourceId, "FAILURE"));
        }
        return result;
    }
    
    /**
     * 获取权限的所有资源ID
     */
    public List<Integer> getResourceIdsByPermissionId(Integer permissionId) {
        return permissionResourceDao.getResourceIdsByPermissionId(permissionId);
    }
}

