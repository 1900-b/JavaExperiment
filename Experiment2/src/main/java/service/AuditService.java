package service;

import dao.AuditLogDao;
import entity.AuditLog;

import java.util.List;

/**
 * 权限审计服务
 */
public class AuditService {
    private AuditLogDao auditLogDao = new AuditLogDao();
    
    /**
     * 查询所有审计日志
     */
    public List<AuditLog> getAllAuditLogs() {
        return auditLogDao.getAllAuditLogs();
    }
    
    /**
     * 根据用户ID查询审计日志
     */
    public List<AuditLog> getAuditLogsByUserId(Integer userId) {
        return auditLogDao.getAuditLogsByUserId(userId);
    }
    
    /**
     * 根据操作类型查询审计日志
     */
    public List<AuditLog> getAuditLogsByOperation(String operation) {
        return auditLogDao.getAuditLogsByOperation(operation);
    }
    
    /**
     * 记录审计日志
     */
    public boolean recordAuditLog(AuditLog auditLog) {
        return auditLogDao.createAuditLog(auditLog);
    }
}


