import controller.AuditController;
import controller.LoginController;
import controller.PermissionCheckController;
import controller.PermissionController;
import controller.ResourceController;
import controller.RoleController;
import controller.UserController;
import entity.Role;
import entity.User;
import service.AuthService;
import service.PermissionCheckService;
import util.LoggerUtil;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * 基于命令行的权限管理系统 - 主程序入口
 */
public class Main {
    private static final Logger logger = LoggerUtil.getLogger(Main.class.getName());
    private static Scanner scanner;
    
    // 当前登录用户信息
    private static Integer currentUserId;
    private static String currentUsername;
    private static User currentUser;
    
    public static void main(String[] args) {
        scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        LoggerUtil.getLogger(Main.class.getName());
        
        logger.info("========== 权限管理系统启动 ==========");
        System.out.println("========================================");
        System.out.println("    基于命令行的权限管理系统");
        System.out.println("    Authority Management System");
        System.out.println("========================================");
        System.out.println();
        
        LoginController loginController = new LoginController();
        currentUser = loginController.showLoginMenu();
        
        if (currentUser == null) {
            logger.info("========== 权限管理系统关闭 ==========");
            return;
        }
        
        currentUserId = currentUser.getId();
        currentUsername = currentUser.getUsername();
        logger.info("当前登录用户: ID=" + currentUserId + ", 用户名=" + currentUsername);
        
        showMainMenu();
        
        logger.info("========== 权限管理系统关闭 ==========");
    }
    
    /**
     * 检查用户是否是管理员
     */
    private static boolean isAdmin() {
        return "admin".equalsIgnoreCase(currentUsername);
    }
    
    /**
     * 显示主菜单
     */
    private static void showMainMenu() {
        while (true) {
            if (isAdmin()) {
                showAdminMenu();
            } else {
                showUserMenu();
            }
        }
    }
    
    /**
     * 显示管理员菜单
     */
    private static void showAdminMenu() {
        System.out.println("\n========== 主菜单（管理员） ==========");
        System.out.println("当前用户: " + currentUsername + " (ID: " + currentUserId + ")");
        System.out.println("---------------------------");
        System.out.println("1. 用户管理");
        System.out.println("2. 角色管理");
        System.out.println("3. 权限管理");
        System.out.println("4. 资源管理");
        System.out.println("5. 权限校验");
        System.out.println("6. 权限审计");
        System.out.println("7. 切换账号");
        System.out.println("0. 退出系统");
        System.out.print("请选择操作: ");
        
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("无效的选择，请输入数字！");
            return;
        }
        
        switch (choice) {
            case 1:
                UserController userController = new UserController(currentUserId, currentUsername);
                userController.showMenu();
                break;
            case 2:
                RoleController roleController = new RoleController(currentUserId, currentUsername);
                roleController.showMenu();
                break;
            case 3:
                PermissionController permissionController = new PermissionController(currentUserId, currentUsername);
                permissionController.showMenu();
                break;
            case 4:
                ResourceController resourceController = new ResourceController(currentUserId, currentUsername);
                resourceController.showMenu();
                break;
            case 5:
                PermissionCheckController permissionCheckController = new PermissionCheckController();
                permissionCheckController.showMenu();
                break;
            case 6:
                AuditController auditController = new AuditController();
                auditController.showMenu();
                break;
            case 7:
                switchAccount();
                break;
            case 0:
                logout();
                System.out.println("\n感谢使用权限管理系统，再见！");
                System.exit(0);
            default:
                System.out.println("无效的选择，请重新输入！");
        }
    }
    
    /**
     * 显示普通用户菜单
     */
    private static void showUserMenu() {
        System.out.println("\n========== 权限验证菜单 ==========");
        System.out.println("当前用户: " + currentUsername + " (ID: " + currentUserId + ")");
        System.out.println("---------------------------");
        System.out.println("1. 查看我的角色");
        System.out.println("2. 查看我的权限");
        System.out.println("3. 权限校验");
        System.out.println("4. 切换账号");
        System.out.println("0. 退出系统");
        System.out.print("请选择操作: ");
        
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("无效的选择，请输入数字！");
            return;
        }
        
        switch (choice) {
            case 1:
                showMyRoles();
                break;
            case 2:
                showMyPermissions();
                break;
            case 3:
                checkMyPermission();
                break;
            case 4:
                switchAccount();
                break;
            case 0:
                logout();
                System.out.println("\n感谢使用权限管理系统，再见！");
                System.exit(0);
            default:
                System.out.println("无效的选择，请重新输入！");
        }
    }
    
    /**
     * 显示当前用户的角色
     */
    private static void showMyRoles() {
        System.out.println("\n========== 我的角色 ==========");
        PermissionCheckService permissionCheckService = new PermissionCheckService();
        List<Role> roles = permissionCheckService.getUserRoles(currentUserId);
        
        if (roles.isEmpty()) {
            System.out.println("您当前没有任何角色。");
        } else {
            System.out.println("您当前拥有以下角色：");
            System.out.println("---------------------------");
            System.out.printf("%-5s %-20s %-20s %-50s %-10s%n", "ID", "角色名称", "角色编码", "描述", "状态");
            System.out.println("---------------------------------------------------------------------------");
            for (Role role : roles) {
                String status = role.getStatus() == 1 ? "启用" : "禁用";
                System.out.printf("%-5d %-20s %-20s %-50s %-10s%n",
                    role.getId(),
                    role.getRoleName(),
                    role.getRoleCode(),
                    role.getDescription() != null ? role.getDescription() : "",
                    status);
            }
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("共 " + roles.size() + " 个角色");
        }
        System.out.println("\n按回车键返回...");
        scanner.nextLine();
    }
    
    /**
     * 显示当前用户的权限
     */
    private static void showMyPermissions() {
        System.out.println("\n========== 我的权限 ==========");
        PermissionCheckService permissionCheckService = new PermissionCheckService();
        List<String> permissionCodes = permissionCheckService.getUserPermissionCodes(currentUserId);
        
        if (permissionCodes.isEmpty()) {
            System.out.println("您当前没有任何权限。");
        } else {
            System.out.println("您当前拥有以下权限：");
            System.out.println("---------------------------");
            for (int i = 0; i < permissionCodes.size(); i++) {
                System.out.println((i + 1) + ". " + permissionCodes.get(i));
            }
            System.out.println("---------------------------");
            System.out.println("共 " + permissionCodes.size() + " 个权限");
        }
        System.out.println("\n按回车键返回...");
        scanner.nextLine();
    }
    
    /**
     * 检查当前用户是否有某个权限
     */
    private static void checkMyPermission() {
        System.out.println("\n========== 权限校验 ==========");
        System.out.print("请输入要检查的权限编码: ");
        String permissionCode = scanner.nextLine().trim();
        
        if (permissionCode.isEmpty()) {
            System.out.println("权限编码不能为空！");
            System.out.println("\n按回车键返回...");
            scanner.nextLine();
            return;
        }
        
        PermissionCheckService permissionCheckService = new PermissionCheckService();
        
        if (!permissionCheckService.permissionExists(permissionCode)) {
            System.out.println("错误：权限编码 [" + permissionCode + "] 不存在！");
            System.out.println("\n按回车键返回...");
            scanner.nextLine();
            return;
        }
        
        boolean hasPermission = permissionCheckService.hasPermission(currentUserId, permissionCode);
        if (hasPermission) {
            System.out.println("您拥有权限 [" + permissionCode + "]");
        } else {
            System.out.println("您没有权限 [" + permissionCode + "]");
        }
        
        System.out.println("\n按回车键返回...");
        scanner.nextLine();
    }
    
    /**
     * 切换账号
     */
    private static void switchAccount() {
        System.out.println("\n========== 切换账号 ==========");
        System.out.println("当前用户: " + currentUsername + " (ID: " + currentUserId + ")");
        System.out.println("---------------------------");
        System.out.println("1. 重新登录");
        System.out.println("2. 注册新账号");
        System.out.println("0. 返回主菜单");
        System.out.print("请选择操作: ");
        
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine(); // 消费换行符
        } catch (Exception e) {
            scanner.nextLine(); // 消费无效输入
            System.out.println("无效的选择，请输入数字！");
            return;
        }
        
        switch (choice) {
            case 1:
                logout();
                LoginController loginController = new LoginController();
                User newUser = loginController.showLoginMenu();
                if (newUser != null) {
                    currentUser = newUser;
                    currentUserId = newUser.getId();
                    currentUsername = newUser.getUsername();
                    logger.info("切换账号成功: ID=" + currentUserId + ", 用户名=" + currentUsername);
                    System.out.println("\n切换账号成功！当前用户: " + currentUsername + " (ID: " + currentUserId + ")");
                } else {
                    System.out.println("\n切换账号失败，保持当前用户登录状态。");
                }
                break;
            case 2:
                System.out.println("\n========== 用户注册 ==========");
                System.out.print("请输入用户名: ");
                String username = scanner.nextLine().trim();
                
                if (username.isEmpty()) {
                    System.out.println("用户名不能为空！");
                    break;
                }
                
                System.out.print("请输入密码: ");
                String password = scanner.nextLine().trim();
                
                if (password.isEmpty()) {
                    System.out.println("密码不能为空！");
                    break;
                }
                
                System.out.print("请输入真实姓名（可选）: ");
                String realName = scanner.nextLine().trim();
                if (realName.isEmpty()) {
                    realName = null;
                }
                
                System.out.print("请输入邮箱（可选）: ");
                String email = scanner.nextLine().trim();
                if (email.isEmpty()) {
                    email = null;
                }
                
                AuthService authService = new AuthService();
                User registeredUser = authService.register(username, password, realName, email);
                if (registeredUser != null) {
                    System.out.println("\n注册成功！用户名: " + registeredUser.getUsername() + " (ID: " + registeredUser.getId() + ")");
                    System.out.println("请使用" + (choice == 1 ? "重新登录" : "切换账号") + "功能登录新账户。");
                } else {
                    System.out.println("\n注册失败！用户名可能已存在，请尝试其他用户名。");
                }
                break;
            case 0:
                break;
            default:
                System.out.println("无效的选择，请重新输入！");
        }
    }
    
    /**
     * 登出当前用户
     */
    private static void logout() {
        if (currentUser != null) {
            AuthService authService = new AuthService();
            authService.logout(currentUserId, currentUsername);
        }
    }
}

