package service;

import dao.AuditLogDao;
import dao.UserDao;
import entity.AuditLog;
import entity.User;
import util.LoggerUtil;

import java.util.Date;
import java.util.regex.Pattern;
import java.util.logging.Logger;

/**
 * 认证服务 - 处理登录、注册等认证相关业务逻辑
 */
public class AuthService {
    private static final Logger logger = LoggerUtil.getLogger(AuthService.class.getName());
    private UserDao userDao = new UserDao();
    private AuditLogDao auditLogDao = new AuditLogDao();
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回User对象，失败返回null
     */
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            logger.warning("登录失败: 用户名为空");
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            logger.warning("登录失败: 密码为空");
            return null;
        }
        
        User user = userDao.verifyUser(username.trim(), password);
        if (user != null) {
            logger.info("用户登录成功: " + username);
            auditLogDao.createAuditLog(new AuditLog(user.getId(), username, "LOGIN", "SYSTEM", 
                String.valueOf(user.getId()), "用户登录: " + username, "SUCCESS"));
            return user;
        } else {
            logger.warning("用户登录失败: 用户名或密码错误 - " + username);
            // 记录登录失败（不记录用户ID，因为验证失败）
            auditLogDao.createAuditLog(new AuditLog(null, username, "LOGIN", "SYSTEM", 
                null, "用户登录失败: 用户名或密码错误 - " + username, "FAILURE"));
            return null;
        }
    }
    
    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param realName 真实姓名
     * @param email 邮箱
     * @return 注册成功返回User对象，失败返回null
     */
    public User register(String username, String password, String realName, String email) {
        if (username == null || username.trim().isEmpty()) {
            logger.warning("注册失败: 用户名为空");
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            logger.warning("注册失败: 密码为空");
            return null;
        }
        // 密码不能包含中文字符
        if (containsChinese(password)) {
            logger.warning("注册失败: 密码包含中文字符");
            return null;
        }
        // 邮箱可为空，但若提供则不能包含中文字符
        if (email != null && !email.trim().isEmpty() && containsChinese(email)) {
            logger.warning("注册失败: 邮箱包含中文字符");
            return null;
        }
        
        if (userDao.getUserByUsername(username.trim()) != null) {
            logger.warning("注册失败: 用户名已存在 - " + username);
            auditLogDao.createAuditLog(new AuditLog(null, username, "REGISTER", "USER", null, 
                "用户注册失败: 用户名已存在 - " + username, "FAILURE"));
            return null;
        }
        
        User user = new User(username.trim(), password, realName, email);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setStatus(1);
        
        boolean result = userDao.createUser(user);
        if (result) {
            User createdUser = userDao.getUserByUsername(username.trim());
            logger.info("用户注册成功: " + username);
            auditLogDao.createAuditLog(new AuditLog(createdUser != null ? createdUser.getId() : null, username, "REGISTER", "USER", 
                createdUser != null ? String.valueOf(createdUser.getId()) : null, "用户注册: " + username, "SUCCESS"));
            return createdUser;
        } else {
            logger.warning("用户注册失败: " + username);
            auditLogDao.createAuditLog(new AuditLog(null, username, "REGISTER", "USER", null, 
                "用户注册失败: " + username, "FAILURE"));
            return null;
        }
    }
    
    /**
     * 检查字符串是否包含中文字符
     * @param str 要检查的字符串
     * @return 如果包含中文字符返回true，否则返回false
     */
    private boolean containsChinese(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5\\u3000-\\u303f\\uff00-\\uffef]");
        return pattern.matcher(str).find();
    }
    
    /**
     * 用户登出
     * @param userId 用户ID
     * @param username 用户名
     */
    public void logout(Integer userId, String username) {
        logger.info("用户登出: " + username);
        auditLogDao.createAuditLog(new AuditLog(userId, username, "LOGOUT", "SYSTEM", 
            String.valueOf(userId), "用户登出: " + username, "SUCCESS"));
    }
}


