package service;

import dao.AuditLogDao;
import dao.UserDao;
import dao.UserRoleDao;
import entity.AuditLog;
import entity.User;
import util.LoggerUtil;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * 用户业务逻辑层
 */
public class UserService {
    private static final Logger logger = LoggerUtil.getLogger(UserService.class.getName());
    private UserDao userDao = new UserDao();
    private UserRoleDao userRoleDao = new UserRoleDao();
    private AuditLogDao auditLogDao = new AuditLogDao();
    
    /**
     * 创建用户
     */
    public boolean createUser(User user, Integer currentUserId, String currentUsername) {
        if (userDao.getUserByUsername(user.getUsername()) != null) {
            logger.warning("创建用户失败: 用户名已存在 - " + user.getUsername());
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "USER", null, 
                "创建用户失败: 用户名已存在 - " + user.getUsername(), "FAILURE"));
            return false;
        }
        
        boolean result = userDao.createUser(user);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "USER", 
                String.valueOf(user.getId()), "创建用户: " + user.getUsername(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "USER", null, 
                "创建用户失败: " + user.getUsername(), "FAILURE"));
        }
        return result;
    }
    
    /**
     * 根据ID查询用户
     */
    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }
    
    /**
     * 根据用户名查询用户
     */
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }
    
    /**
     * 查询所有用户
     */
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
    
    /**
     * 更新用户
     */
    public boolean updateUser(User user, Integer currentUserId, String currentUsername) {
        user.setUpdateTime(new Date());
        boolean result = userDao.updateUser(user);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "UPDATE", "USER", 
                String.valueOf(user.getId()), "更新用户: " + user.getUsername(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "UPDATE", "USER", 
                String.valueOf(user.getId()), "更新用户失败: " + user.getUsername(), "FAILURE"));
        }
        return result;
    }
    
    /**
     * 删除用户
     */
    public boolean deleteUser(Integer id, Integer currentUserId, String currentUsername) {
        User user = userDao.getUserById(id);
        if (user == null) {
            logger.warning("删除用户失败: 用户不存在 - ID: " + id);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "USER", 
                String.valueOf(id), "删除用户失败: 用户不存在", "FAILURE"));
            return false;
        }
        
        boolean result = userDao.deleteUser(id);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "USER", 
                String.valueOf(id), "删除用户: " + user.getUsername(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "USER", 
                String.valueOf(id), "删除用户失败: " + user.getUsername(), "FAILURE"));
        }
        return result;
    }
    
    /**
     * 为用户分配角色
     */
    public boolean assignRoleToUser(Integer userId, Integer roleId, Integer currentUserId, String currentUsername) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            logger.warning("分配角色失败: 用户不存在 - ID: " + userId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "USER_ROLE", 
                String.valueOf(userId), "分配角色失败: 用户不存在", "FAILURE"));
            return false;
        }
        
        if (userRoleDao.hasRole(userId, roleId)) {
            logger.warning("分配角色失败: 用户已拥有该角色 - 用户ID: " + userId + ", 角色ID: " + roleId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "USER_ROLE", 
                String.valueOf(userId), "分配角色失败: 用户已拥有该角色", "FAILURE"));
            return false;
        }
        
        boolean result = userRoleDao.assignRoleToUser(userId, roleId);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "USER_ROLE", 
                String.valueOf(userId), "为用户分配角色: 用户ID=" + userId + ", 角色ID=" + roleId, "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "ASSIGN", "USER_ROLE", 
                String.valueOf(userId), "分配角色失败: 用户ID=" + userId + ", 角色ID=" + roleId, "FAILURE"));
        }
        return result;
    }
    
    /**
     * 移除用户的角色
     */
    public boolean removeRoleFromUser(Integer userId, Integer roleId, Integer currentUserId, String currentUsername) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            logger.warning("移除角色失败: 用户不存在 - ID: " + userId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "USER_ROLE", 
                String.valueOf(userId), "移除角色失败: 用户不存在", "FAILURE"));
            return false;
        }
        
        if (!userRoleDao.hasRole(userId, roleId)) {
            logger.warning("移除角色失败: 用户未拥有该角色 - 用户ID: " + userId + ", 角色ID: " + roleId);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "USER_ROLE", 
                String.valueOf(userId), "移除角色失败: 用户未拥有该角色", "FAILURE"));
            return false;
        }
        
        boolean result = userRoleDao.removeRoleFromUser(userId, roleId);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "USER_ROLE", 
                String.valueOf(userId), "移除用户角色: 用户ID=" + userId + ", 角色ID=" + roleId, "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "REMOVE", "USER_ROLE", 
                String.valueOf(userId), "移除角色失败: 用户ID=" + userId + ", 角色ID=" + roleId, "FAILURE"));
        }
        return result;
    }
    
    /**
     * 获取用户的所有角色ID
     */
    public List<Integer> getUserRoleIds(Integer userId) {
        return userRoleDao.getRoleIdsByUserId(userId);
    }
}

