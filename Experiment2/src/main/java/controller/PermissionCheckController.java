package controller;

import service.PermissionCheckService;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 权限校验控制器
 */
public class PermissionCheckController {
    private PermissionCheckService permissionCheckService = new PermissionCheckService();
    private Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    
    /**
     * 显示权限校验菜单
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n========== 权限校验 ==========");
            System.out.println("1. 检查用户权限");
            System.out.println("2. 检查用户角色");
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    checkUserPermission();
                    break;
                case 2:
                    checkUserRole();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }
    
    /**
     * 检查用户权限
     */
    private void checkUserPermission() {
        System.out.println("\n--- 检查用户权限 ---");
        System.out.print("请输入用户ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("请输入权限编码: ");
        String permissionCode = scanner.nextLine();
        
        if (!permissionCheckService.userExists(userId)) {
            System.out.println("错误：用户ID " + userId + " 不存在！");
            return;
        }
        
        if (!permissionCheckService.permissionExists(permissionCode)) {
            System.out.println("错误：权限编码 [" + permissionCode + "] 不存在！");
            return;
        }
        
        boolean hasPermission = permissionCheckService.hasPermission(userId, permissionCode);
        if (hasPermission) {
            System.out.println("用户ID " + userId + " 拥有权限 [" + permissionCode + "]");
        } else {
            System.out.println("用户ID " + userId + " 没有权限 [" + permissionCode + "]");
        }
    }
    
    /**
     * 检查用户角色
     */
    private void checkUserRole() {
        System.out.println("\n--- 检查用户角色 ---");
        System.out.print("请输入用户ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("请输入角色ID: ");
        int roleId = scanner.nextInt();
        scanner.nextLine();
        
        if (!permissionCheckService.userExists(userId)) {
            System.out.println("错误：用户ID " + userId + " 不存在！");
            return;
        }
        
        if (!permissionCheckService.roleExists(roleId)) {
            System.out.println("错误：角色ID " + roleId + " 不存在！");
            return;
        }
        
        boolean hasRole = permissionCheckService.hasRole(userId, roleId);
        if (hasRole) {
            System.out.println("用户ID " + userId + " 拥有角色ID " + roleId);
        } else {
            System.out.println("用户ID " + userId + " 没有角色ID " + roleId);
        }
    }
}


