package service;

import dao.AuditLogDao;
import dao.RoleDao;
import dao.RolePermissionDao;
import entity.AuditLog;
import entity.Role;
import util.LoggerUtil;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * 角色业务逻辑层
 */
public class RoleService {
    private static final Logger logger = LoggerUtil.getLogger(RoleService.class.getName());
    private RoleDao roleDao = new RoleDao();
    private RolePermissionDao rolePermissionDao = new RolePermissionDao();
    private AuditLogDao auditLogDao = new AuditLogDao();
    
    /**
     * 创建角色
     */
    public boolean createRole(Role role, Integer currentUserId, String currentUsername) {
        if (roleDao.getRoleByCode(role.getRoleCode()) != null) {
            logger.warning("创建角色失败: 角色编码已存在 - " + role.getRoleCode());
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "ROLE", null, 
                "创建角色失败: 角色编码已存在 - " + role.getRoleCode(), "FAILURE"));
            return false;
        }
        
        boolean result = roleDao.createRole(role);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "ROLE", 
                String.valueOf(role.getId()), "创建角色: " + role.getRoleName(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "ROLE", null, 
                "创建角色失败: " + role.getRoleName(), "FAILURE"));
        }
        return result;
    }
    
    /**
     * 根据ID查询角色
     */
    public Role getRoleById(Integer id) {
        return roleDao.getRoleById(id);
    }
    
    /**
     * 根据角色编码查询角色
     */
    public Role getRoleByCode(String roleCode) {
        return roleDao.getRoleByCode(roleCode);
    }
    
    /**
     * 查询所有角色
     */
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }
    
    /**
     * 更新角色
     */
    public boolean updateRole(Role role, Integer currentUserId, String currentUsername) {
        role.setUpdateTime(new Date());
        boolean result = roleDao.updateRole(role);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "UPDATE", "ROLE", 
                String.valueOf(role.getId()), "更新角色: " + role.getRoleName(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "UPDATE", "ROLE", 
                String.valueOf(role.getId()), "更新角色失败: " + role.getRoleName(), "FAILURE"));
        }
        return result;
    }
    
    /**
     * 删除角色
     */
    public boolean deleteRole(Integer id, Integer currentUserId, String currentUsername) {
        Role role = roleDao.getRoleById(id);
        if (role == null) {
            logger.warning("删除角色失败: 角色不存在 - ID: " + id);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "ROLE", 
                String.valueOf(id), "删除角色失败: 角色不存在", "FAILURE"));
            return false;
        }
        
        boolean result = roleDao.deleteRole(id);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "ROLE", 
                String.valueOf(id), "删除角色: " + role.getRoleName(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "ROLE", 
                String.valueOf(id), "删除角色失败: " + role.getRoleName(), "FAILURE"));
        }
        return result;
    }
    
    /**
     * 为角色分配权限
     */
    public boolean assignPermissionToRole(Integer roleId, Integer permissionId, Integer currentUserId, String currentUsername) {
        Role role = roleDao.getRoleById(roleId);
        if (role == null) {
            logger.warning("分配权限失败: 角色不存在 - ID: " + roleId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "ROLE_PERMISSION", 
                String.valueOf(roleId), "分配权限失败: 角色不存在", "FAILURE"));
            return false;
        }
        
        if (rolePermissionDao.hasPermission(roleId, permissionId)) {
            logger.warning("分配权限失败: 角色已拥有该权限 - 角色ID: " + roleId + ", 权限ID: " + permissionId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "ROLE_PERMISSION", 
                String.valueOf(roleId), "分配权限失败: 角色已拥有该权限", "FAILURE"));
            return false;
        }
        
        boolean result = rolePermissionDao.assignPermissionToRole(roleId, permissionId);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "ROLE_PERMISSION", 
                String.valueOf(roleId), "为角色分配权限: 角色ID=" + roleId + ", 权限ID=" + permissionId, "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "ROLE_PERMISSION", 
                String.valueOf(roleId), "分配权限失败: 角色ID=" + roleId + ", 权限ID=" + permissionId, "FAILURE"));
        }
        return result;
    }
    
    /**
     * 移除角色的权限
     */
    public boolean removePermissionFromRole(Integer roleId, Integer permissionId, Integer currentUserId, String currentUsername) {
        Role role = roleDao.getRoleById(roleId);
        if (role == null) {
            logger.warning("移除权限失败: 角色不存在 - ID: " + roleId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "ROLE_PERMISSION", 
                String.valueOf(roleId), "移除权限失败: 角色不存在", "FAILURE"));
            return false;
        }
        
        if (!rolePermissionDao.hasPermission(roleId, permissionId)) {
            logger.warning("移除权限失败: 角色未拥有该权限 - 角色ID: " + roleId + ", 权限ID: " + permissionId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "ROLE_PERMISSION", 
                String.valueOf(roleId), "移除权限失败: 角色未拥有该权限", "FAILURE"));
            return false;
        }
        
        boolean result = rolePermissionDao.removePermissionFromRole(roleId, permissionId);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "ROLE_PERMISSION", 
                String.valueOf(roleId), "移除角色权限: 角色ID=" + roleId + ", 权限ID=" + permissionId, "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "ROLE_PERMISSION", 
                String.valueOf(roleId), "移除权限失败: 角色ID=" + roleId + ", 权限ID=" + permissionId, "FAILURE"));
        }
        return result;
    }
    
    /**
     * 获取角色的所有权限ID
     */
    public List<Integer> getRolePermissionIds(Integer roleId) {
        return rolePermissionDao.getPermissionIdsByRoleId(roleId);
    }
}


