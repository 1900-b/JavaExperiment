package entity;

import java.util.Date;

/**
 * 资源实体类
 */
public class Resource {
    private Integer id;
    private String resourceName;
    private String resourceCode;
    private String resourceType; // MENU-菜单, BUTTON-按钮, DATA-数据
    private String resourcePath;
    private String description;
    private Date createTime;
    private Date updateTime;
    private Integer status; // 0-禁用, 1-启用
    
    public Resource() {
    }
    
    public Resource(String resourceName, String resourceCode, String resourceType, String resourcePath, String description) {
        this.resourceName = resourceName;
        this.resourceCode = resourceCode;
        this.resourceType = resourceType;
        this.resourcePath = resourcePath;
        this.description = description;
        this.status = 1; // 默认启用
        this.createTime = new Date();
        this.updateTime = new Date();
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getResourceName() {
        return resourceName;
    }
    
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
    
    public String getResourceCode() {
        return resourceCode;
    }
    
    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
    
    public String getResourcePath() {
        return resourcePath;
    }
    
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", resourceName='" + resourceName + '\'' +
                ", resourceCode='" + resourceCode + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", resourcePath='" + resourcePath + '\'' +
                ", description='" + description + '\'' +
                ", status=" + (status == 1 ? "启用" : "禁用") +
                ", createTime=" + createTime +
                '}';
    }
}


