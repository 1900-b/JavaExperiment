package entity;

import java.util.Date;

/**
 * 权限实体类
 */
public class Permission {
    private Integer id;
    private String permissionName;
    private String permissionCode;
    private String permissionType; // MENU-菜单权限, OPERATION-操作权限, RESOURCE-资源权限
    private String description;
    private Date createTime;
    private Date updateTime;
    private Integer status; // 0-禁用, 1-启用
    
    public Permission() {
    }
    
    public Permission(String permissionName, String permissionCode, String permissionType, String description) {
        this.permissionName = permissionName;
        this.permissionCode = permissionCode;
        this.permissionType = permissionType;
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
    
    public String getPermissionName() {
        return permissionName;
    }
    
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
    
    public String getPermissionCode() {
        return permissionCode;
    }
    
    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
    
    public String getPermissionType() {
        return permissionType;
    }
    
    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
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
        return "Permission{" +
                "id=" + id +
                ", permissionName='" + permissionName + '\'' +
                ", permissionCode='" + permissionCode + '\'' +
                ", permissionType='" + permissionType + '\'' +
                ", description='" + description + '\'' +
                ", status=" + (status == 1 ? "启用" : "禁用") +
                ", createTime=" + createTime +
                '}';
    }
}


