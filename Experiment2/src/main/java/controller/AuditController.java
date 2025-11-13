package controller;

import entity.AuditLog;
import service.AuditService;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * 权限审计控制器
 */
public class AuditController {
    private AuditService auditService = new AuditService();
    private Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    
    /**
     * 显示权限审计菜单
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n========== 权限审计 ==========");
            System.out.println("1. 查询所有审计日志");
            System.out.println("2. 根据用户ID查询审计日志");
            System.out.println("3. 根据操作类型查询审计日志");
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    listAllAuditLogs();
                    break;
                case 2:
                    getAuditLogsByUserId();
                    break;
                case 3:
                    getAuditLogsByOperation();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }
    
    /**
     * 查询所有审计日志
     */
    private void listAllAuditLogs() {
        System.out.println("\n--- 所有审计日志 ---");
        System.out.println("说明：用户ID和用户名是指执行操作的用户（操作者），目标ID是指被操作的对象ID");
        System.out.println();
        List<AuditLog> logs = auditService.getAllAuditLogs();
        if (logs.isEmpty()) {
            System.out.println("暂无审计日志数据。");
        } else {
            System.out.printf("%-5s %-15s %-15s %-15s %-15s %-15s %-40s %-10s %-20s%n", 
                "ID", "操作者ID", "操作者", "操作", "目标类型", "目标ID", "描述", "结果", "创建时间");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            for (AuditLog log : logs) {
                System.out.printf("%-5d %-15s %-15s %-15s %-15s %-15s %-40s %-10s %-20s%n",
                    log.getId(), log.getUserId() != null ? log.getUserId() : "N/A",
                    log.getUsername() != null ? log.getUsername() : "N/A", 
                    log.getOperation(), log.getTargetType(),
                    log.getTargetId() != null ? log.getTargetId() : "N/A",
                    log.getDescription(), log.getResult(), log.getCreateTime());
            }
        }
    }
    
    /**
     * 根据用户ID查询审计日志
     */
    private void getAuditLogsByUserId() {
        System.out.println("\n--- 根据用户ID查询审计日志 ---");
        System.out.println("说明：查询的是执行操作的用户（操作者）的审计日志");
        System.out.print("请输入操作者用户ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        
        List<AuditLog> logs = auditService.getAuditLogsByUserId(userId);
        if (logs.isEmpty()) {
            System.out.println("暂无用户ID " + userId + " 作为操作者的审计日志数据。");
        } else {
            System.out.println("\n--- 用户ID " + userId + " 作为操作者的审计日志 ---");
            System.out.printf("%-5s %-15s %-15s %-15s %-15s %-15s %-40s %-10s %-20s%n", 
                "ID", "操作者ID", "操作者", "操作", "目标类型", "目标ID", "描述", "结果", "创建时间");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            for (AuditLog log : logs) {
                System.out.printf("%-5d %-15s %-15s %-15s %-15s %-15s %-40s %-10s %-20s%n",
                    log.getId(), log.getUserId() != null ? log.getUserId() : "N/A",
                    log.getUsername() != null ? log.getUsername() : "N/A", 
                    log.getOperation(), log.getTargetType(),
                    log.getTargetId() != null ? log.getTargetId() : "N/A",
                    log.getDescription(), log.getResult(), log.getCreateTime());
            }
        }
    }
    
    /**
     * 根据操作类型查询审计日志
     */
    private void getAuditLogsByOperation() {
        System.out.println("\n--- 根据操作类型查询审计日志 ---");
        System.out.println("请选择操作类型：");
        System.out.println("1. CREATE");
        System.out.println("2. UPDATE");
        System.out.println("3. DELETE");
        System.out.println("4. QUERY");
        System.out.println("5. ASSIGN");
        System.out.println("6. REMOVE");
        System.out.print("请输入数字选项(1-6): ");
        int opChoice = scanner.nextInt();
        scanner.nextLine();
        
        String operation;
        switch (opChoice) {
            case 1:
                operation = "CREATE";
                break;
            case 2:
                operation = "UPDATE";
                break;
            case 3:
                operation = "DELETE";
                break;
            case 4:
                operation = "QUERY";
                break;
            case 5:
                operation = "ASSIGN";
                break;
            case 6:
                operation = "REMOVE";
                break;
            default:
                System.out.println("无效的选择，请返回菜单重试。");
                return;
        }
        
        List<AuditLog> logs = auditService.getAuditLogsByOperation(operation);
        if (logs.isEmpty()) {
            System.out.println("暂无该操作类型的审计日志数据。");
        } else {
            System.out.println("\n--- " + operation + " 操作类型的审计日志 ---");
            System.out.printf("%-5s %-15s %-15s %-15s %-15s %-15s %-40s %-10s %-20s%n", 
                "ID", "操作者ID", "操作者", "操作", "目标类型", "目标ID", "描述", "结果", "创建时间");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            for (AuditLog log : logs) {
                System.out.printf("%-5d %-15s %-15s %-15s %-15s %-15s %-40s %-10s %-20s%n",
                    log.getId(), log.getUserId() != null ? log.getUserId() : "N/A",
                    log.getUsername() != null ? log.getUsername() : "N/A", 
                    log.getOperation(), log.getTargetType(),
                    log.getTargetId() != null ? log.getTargetId() : "N/A",
                    log.getDescription(), log.getResult(), log.getCreateTime());
            }
        }
    }
}

