package controller;

import entity.User;
import service.AuthService;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 登录控制器 - 处理登录、注册界面
 */
public class LoginController {
    private AuthService authService = new AuthService();
    private Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    
    /**
     * 显示登录菜单
     * @return 登录成功返回User对象，失败返回null
     */
    public User showLoginMenu() {
        while (true) {
            System.out.println("\n========== 登录/注册 ==========");
            System.out.println("1. 登录");
            System.out.println("2. 注册");
            System.out.println("0. 退出系统");
            System.out.print("请选择操作: ");
            
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("无效的选择，请输入数字！");
                continue;
            }
            
            switch (choice) {
                case 1:
                    User user = login();
                    if (user != null) {
                        return user;
                    }
                    break;
                case 2:
                    register();
                    break;
                case 0:
                    System.out.println("\n感谢使用权限管理系统，再见！");
                    System.exit(0);
                    return null;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }
    
    /**
     * 登录
     * @return 登录成功返回User对象，失败返回null
     */
    private User login() {
        System.out.println("\n========== 用户登录 ==========");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("请输入密码: ");
        String password = scanner.nextLine().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("用户名和密码不能为空！");
            return null;
        }
        
        User user = authService.login(username, password);
        if (user != null) {
            System.out.println("\n登录成功！欢迎, " + user.getUsername() + " (ID: " + user.getId() + ")");
            return user;
        } else {
            System.out.println("\n登录失败！用户名或密码错误，或账户已被禁用。");
            return null;
        }
    }
    
    /**
     * 注册
     */
    private void register() {
        System.out.println("\n========== 用户注册 ==========");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine().trim();
        
        if (username.isEmpty()) {
            System.out.println("用户名不能为空！");
            return;
        }
        
        System.out.print("请输入密码: ");
        String password = scanner.nextLine().trim();
        
        if (password.isEmpty()) {
            System.out.println("密码不能为空！");
            return;
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
        
        User user = authService.register(username, password, realName, email);
        if (user != null) {
            System.out.println("\n注册成功！用户名: " + user.getUsername() + " (ID: " + user.getId() + ")");
            System.out.println("请使用新账户登录。");
        } else {
            System.out.println("\n注册失败！用户名可能已存在，请尝试其他用户名。");
        }
    }
}


