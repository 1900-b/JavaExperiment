package entity;

import java.util.Date;

/**
 * 权限审计日志实体类
 */
public class AuditLog {
    private Integer id;
    private Integer userId;
    private String username;
    private String operation; // 操作类型：CREATE, UPDATE, DELETE, QUERY等
    private String targetType; // 操作目标类型：USER, ROLE, PERMISSION等
    private String targetId; // 操作目标ID
    private String description; // 操作描述
    private String result; // 操作结果：SUCCESS, FAILURE
    private Date createTime;
    
    public AuditLog() {
    }
    
    public AuditLog(Integer userId, String username, String operation, String targetType, String targetId, String description, String result) {
        this.userId = userId;
        this.username = username;
        this.operation = operation;
        this.targetType = targetType;
        this.targetId = targetId;
        this.description = description;
        this.result = result;
        this.createTime = new Date();
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public String getTargetType() {
        return targetType;
    }
    
    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
    
    public String getTargetId() {
        return targetId;
    }
    
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", operation='" + operation + '\'' +
                ", targetType='" + targetType + '\'' +
                ", targetId='" + targetId + '\'' +
                ", description='" + description + '\'' +
                ", result='" + result + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

