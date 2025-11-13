package service;

import dao.AuditLogDao;
import dao.ResourceDao;
import entity.AuditLog;
import entity.Resource;
import util.LoggerUtil;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * 资源业务逻辑层
 */
public class ResourceService {
    private static final Logger logger = LoggerUtil.getLogger(ResourceService.class.getName());
    private ResourceDao resourceDao = new ResourceDao();
    private AuditLogDao auditLogDao = new AuditLogDao();
    
    /**
     * 创建资源
     */
    public boolean createResource(Resource resource, Integer currentUserId, String currentUsername) {
        if (resourceDao.getResourceByCode(resource.getResourceCode()) != null) {
            logger.warning("创建资源失败: 资源编码已存在 - " + resource.getResourceCode());
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "RESOURCE", null, 
                "创建资源失败: 资源编码已存在 - " + resource.getResourceCode(), "FAILURE"));
            return false;
        }
        
        boolean result = resourceDao.createResource(resource);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "RESOURCE", 
                String.valueOf(resource.getId()), "创建资源: " + resource.getResourceName(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "CREATE", "RESOURCE", null, 
                "创建资源失败: " + resource.getResourceName(), "FAILURE"));
        }
        return result;
    }
    
    /**
     * 根据ID查询资源
     */
    public Resource getResourceById(Integer id) {
        return resourceDao.getResourceById(id);
    }
    
    /**
     * 根据资源编码查询资源
     */
    public Resource getResourceByCode(String resourceCode) {
        return resourceDao.getResourceByCode(resourceCode);
    }
    
    /**
     * 查询所有资源
     */
    public List<Resource> getAllResources() {
        return resourceDao.getAllResources();
    }
    
    /**
     * 根据资源类型查询资源
     */
    public List<Resource> getResourcesByType(String resourceType) {
        return resourceDao.getResourcesByType(resourceType);
    }
    
    /**
     * 更新资源
     */
    public boolean updateResource(Resource resource, Integer currentUserId, String currentUsername) {
        resource.setUpdateTime(new Date());
        boolean result = resourceDao.updateResource(resource);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "UPDATE", "RESOURCE", 
                String.valueOf(resource.getId()), "更新资源: " + resource.getResourceName(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "UPDATE", "RESOURCE", 
                String.valueOf(resource.getId()), "更新资源失败: " + resource.getResourceName(), "FAILURE"));
        }
        return result;
    }
    
    /**
     * 删除资源
     */
    public boolean deleteResource(Integer id, Integer currentUserId, String currentUsername) {
        Resource resource = resourceDao.getResourceById(id);
        if (resource == null) {
            logger.warning("删除资源失败: 资源不存在 - ID: " + id);
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "RESOURCE", 
                String.valueOf(id), "删除资源失败: 资源不存在", "FAILURE"));
            return false;
        }
        
        boolean result = resourceDao.deleteResource(id);
        if (result) {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "RESOURCE", 
                String.valueOf(id), "删除资源: " + resource.getResourceName(), "SUCCESS"));
        } else {
            auditLogDao.createAuditLog(new AuditLog(currentUserId, currentUsername, "DELETE", "RESOURCE", 
                String.valueOf(id), "删除资源失败: " + resource.getResourceName(), "FAILURE"));
        }
        return result;
    }
}

