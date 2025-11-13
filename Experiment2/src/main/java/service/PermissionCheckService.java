package service;

import dao.PermissionDao;
import dao.RoleDao;
import dao.RolePermissionDao;
import dao.UserDao;
import dao.UserRoleDao;
import util.LoggerUtil;
import entity.Role;

import java.util.List;
import java.util.logging.Logger;

/**
 * 权限校验服务
 */
public class PermissionCheckService {
    private static final Logger logger = LoggerUtil.getLogger(PermissionCheckService.class.getName());
    private RolePermissionDao rolePermissionDao = new RolePermissionDao();
    private UserRoleDao userRoleDao = new UserRoleDao();
    private UserDao userDao = new UserDao();
    private PermissionDao permissionDao = new PermissionDao();
    private RoleDao roleDao = new RoleDao();
    
    /**
     * 检查用户是否拥有指定权限
     * @param userId 用户ID
     * @param permissionCode 权限编码
     * @return true-有权限，false-无权限
     */
    public boolean hasPermission(Integer userId, String permissionCode) {
        if (userId == null || permissionCode == null) {
            logger.warning("权限校验失败: 参数为空");
            return false;
        }
        
        boolean hasPermission = rolePermissionDao.hasPermissionByUser(userId, permissionCode);
        logger.info("权限校验 - 用户ID: " + userId + ", 权限编码: " + permissionCode + ", 结果: " + hasPermission);
        return hasPermission;
    }
    
    /**
     * 检查用户是否拥有指定角色
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return true-有角色，false-无角色
     */
    public boolean hasRole(Integer userId, Integer roleId) {
        if (userId == null || roleId == null) {
            logger.warning("角色校验失败: 参数为空");
            return false;
        }
        
        List<Integer> userRoleIds = userRoleDao.getRoleIdsByUserId(userId);
        boolean hasRole = userRoleIds.contains(roleId);
        logger.info("角色校验 - 用户ID: " + userId + ", 角色ID: " + roleId + ", 结果: " + hasRole);
        return hasRole;
    }
    
    /**
     * 获取用户的所有权限编码
     * @param userId 用户ID
     * @return 权限编码列表
     */
    public List<String> getUserPermissionCodes(Integer userId) {
        List<Integer> roleIds = userRoleDao.getRoleIdsByUserId(userId);
        
        java.util.Set<String> permissionCodes = new java.util.HashSet<>();
        for (Integer roleId : roleIds) {
            List<Integer> permissionIds = rolePermissionDao.getPermissionIdsByRoleId(roleId);
            for (Integer permissionId : permissionIds) {
                entity.Permission permission = permissionDao.getPermissionById(permissionId);
                if (permission != null && permission.getStatus() == 1) {
                    permissionCodes.add(permission.getPermissionCode());
                }
            }
        }
        
        return new java.util.ArrayList<>(permissionCodes);
    }
    
    /**
     * 获取用户的所有角色信息
     * @param userId 用户ID
     * @return 角色列表
     */
    public List<Role> getUserRoles(Integer userId) {
        List<Integer> roleIds = userRoleDao.getRoleIdsByUserId(userId);
        List<entity.Role> roles = new java.util.ArrayList<>();
        for (Integer roleId : roleIds) {
            entity.Role role = roleDao.getRoleById(roleId);
            if (role != null) {
                roles.add(role);
            }
        }
        return roles;
    }
    
    /**
     * 检查用户是否存在
     * @param userId 用户ID
     * @return true-存在，false-不存在
     */
    public boolean userExists(Integer userId) {
        if (userId == null) {
            return false;
        }
        return userDao.getUserById(userId) != null;
    }
    
    /**
     * 检查权限是否存在
     * @param permissionCode 权限编码
     * @return true-存在，false-不存在
     */
    public boolean permissionExists(String permissionCode) {
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return false;
        }
        return permissionDao.getPermissionByCode(permissionCode) != null;
    }
    
    /**
     * 检查角色是否存在
     * @param roleId 角色ID
     * @return true-存在，false-不存在
     */
    public boolean roleExists(Integer roleId) {
        if (roleId == null) {
            return false;
        }
        return roleDao.getRoleById(roleId) != null;
    }
}


