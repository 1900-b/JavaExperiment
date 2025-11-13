package controller;

import entity.Role;
import entity.User;
import service.RoleService;
import service.UserService;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 用户管理控制器
 */
public class UserController {
    private UserService userService = new UserService();
    private RoleService roleService = new RoleService();
    private Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    private Integer currentUserId;
    private String currentUsername;
    
    public UserController(Integer currentUserId, String currentUsername) {
        this.currentUserId = currentUserId;
        this.currentUsername = currentUsername;
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
        // 匹配中文字符（包括CJK统一汉字、中文标点符号等）
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5\\u3000-\\u303f\\uff00-\\uffef]");
        return pattern.matcher(str).find();
    }
    
    /**
     * 显示用户管理菜单
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n========== 用户管理 ==========");
            System.out.println("1. 创建用户");
            System.out.println("2. 查询所有用户");
            System.out.println("3. 根据用户ID查询用户");
            System.out.println("4. 根据用户名查询用户");
            System.out.println("5. 更新用户");
            System.out.println("6. 删除用户");
            System.out.println("7. 为用户分配角色");
            System.out.println("8. 移除用户角色");
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    listAllUsers();
                    break;
                case 3:
                    getUserById();
                    break;
                case 4:
                    getUserByUsername();
                    break;
                case 5:
                    updateUser();
                    break;
                case 6:
                    deleteUser();
                    break;
                case 7:
                    assignRoleToUser();
                    break;
                case 8:
                    removeRoleFromUser();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }
    
    /**
     * 创建用户
     */
    private void createUser() {
        System.out.println("\n--- 创建用户 ---");
        
        String username;
        while (true) {
            System.out.print("请输入用户名: ");
            username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("错误：用户名不能为空，请重新输入！");
            } else {
                break;
            }
        }
        
        String password;
        while (true) {
            System.out.print("请输入密码: ");
            password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("错误：密码不能为空，请重新输入！");
            } else if (containsChinese(password)) {
                System.out.println("错误：密码不能包含中文字符，请重新输入！");
            } else {
                break;
            }
        }
        
        System.out.print("请输入真实姓名: ");
        String realName = scanner.nextLine().trim();
        
        String email;
        while (true) {
            System.out.print("请输入邮箱: ");
            email = scanner.nextLine().trim();
            if (containsChinese(email)) {
                System.out.println("错误：邮箱不能包含中文字符，请重新输入！");
            } else {
                break;
            }
        }
        
        User user = new User(username, password, realName, email);
        boolean result = userService.createUser(user, currentUserId, currentUsername);
        
        if (result) {
            System.out.println("创建用户 [" + username + "] 成功！");
        } else {
            System.out.println("创建用户 [" + username + "] 失败！用户名已存在。");
        }
    }
    
    /**
     * 查询所有用户
     */
    private void listAllUsers() {
        System.out.println("\n--- 所有用户列表 ---");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("暂无用户数据。");
        } else {
            System.out.printf("%-5s %-15s %-15s %-25s %-10s %-20s%n", 
                "ID", "用户名", "真实姓名", "邮箱", "状态", "创建时间");
            System.out.println("--------------------------------------------------------------------------------");
            for (User user : users) {
                System.out.printf("%-5d %-15s %-15s %-25s %-10s %-20s%n",
                    user.getId(), user.getUsername(), user.getRealName(), 
                    user.getEmail(), user.getStatus() == 1 ? "启用" : "禁用",
                    user.getCreateTime());
            }
        }
    }
    
    /**
     * 根据ID查询用户
     */
    private void getUserById() {
        System.out.println("\n--- 根据ID查询用户 ---");
        System.out.print("请输入用户ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        User user = userService.getUserById(id);
        if (user != null) {
            System.out.println("用户信息:");
            System.out.println("  ID: " + user.getId());
            System.out.println("  用户名: " + user.getUsername());
            System.out.println("  真实姓名: " + user.getRealName());
            System.out.println("  邮箱: " + user.getEmail());
            System.out.println("  状态: " + (user.getStatus() == 1 ? "启用" : "禁用"));
            System.out.println("  创建时间: " + user.getCreateTime());
        } else {
            System.out.println("未找到ID为 " + id + " 的用户！");
        }
    }
    
    /**
     * 根据用户名查询用户
     */
    private void getUserByUsername() {
        System.out.println("\n--- 根据用户名查询用户 ---");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        
        User user = userService.getUserByUsername(username);
        if (user != null) {
            System.out.println("用户信息:");
            System.out.println("  ID: " + user.getId());
            System.out.println("  用户名: " + user.getUsername());
            System.out.println("  真实姓名: " + user.getRealName());
            System.out.println("  邮箱: " + user.getEmail());
            System.out.println("  状态: " + (user.getStatus() == 1 ? "启用" : "禁用"));
            System.out.println("  创建时间: " + user.getCreateTime());
        } else {
            System.out.println("未找到用户名为 " + username + " 的用户！");
        }
    }
    
    /**
     * 更新用户
     */
    private void updateUser() {
        System.out.println("\n--- 更新用户 ---");
        System.out.print("请输入要更新的用户ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        User user = userService.getUserById(id);
        if (user == null) {
            System.out.println("未找到ID为 " + id + " 的用户！");
            return;
        }
        
        System.out.print("请输入新用户名 (当前: " + user.getUsername() + ", 直接回车保持): ");
        String username = scanner.nextLine();
        if (!username.isEmpty()) {
            user.setUsername(username);
        }
        
        System.out.print("请输入新密码 (直接回车保持): ");
        String password = scanner.nextLine();
        if (!password.isEmpty()) {
            if (containsChinese(password)) {
                System.out.println("错误：密码不能包含中文字符！");
                return;
            }
            user.setPassword(password);
        }
        
        System.out.print("请输入新真实姓名 (当前: " + user.getRealName() + ", 直接回车保持): ");
        String realName = scanner.nextLine();
        if (!realName.isEmpty()) {
            user.setRealName(realName);
        }
        
        System.out.print("请输入新邮箱 (当前: " + user.getEmail() + ", 直接回车保持): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            if (containsChinese(email)) {
                System.out.println("错误：邮箱不能包含中文字符！");
                return;
            }
            user.setEmail(email);
        }
        
        boolean result = userService.updateUser(user, currentUserId, currentUsername);
        if (result) {
            System.out.println("更新用户 [" + user.getUsername() + "] 成功！");
        } else {
            System.out.println("更新用户失败！");
        }
    }
    
    /**
     * 删除用户
     */
    private void deleteUser() {
        System.out.println("\n--- 删除用户 ---");
        System.out.print("请输入要删除的用户ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("确认删除用户ID " + id + "? (y/n): ");
        String confirm = scanner.nextLine();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("已取消删除操作。");
            return;
        }
        
        boolean result = userService.deleteUser(id, currentUserId, currentUsername);
        if (result) {
            System.out.println("删除用户成功！");
        } else {
            System.out.println("删除用户失败！");
        }
    }
    
    /**
     * 为用户分配角色
     */
    private void assignRoleToUser() {
        System.out.println("\n--- 为用户分配角色 ---");
        
        System.out.println("\n所有用户列表:");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("暂无用户数据。");
            return;
        }
        System.out.printf("%-5s %-15s %-15s %-25s%n", "ID", "用户名", "真实姓名", "邮箱");
        System.out.println("--------------------------------------------------");
        for (User user : users) {
            System.out.printf("%-5d %-15s %-15s %-25s%n",
                user.getId(), user.getUsername(), user.getRealName(), user.getEmail());
        }
        
        System.out.print("\n请输入用户ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        
        User user = userService.getUserById(userId);
        if (user == null) {
            System.out.println("未找到ID为 " + userId + " 的用户！");
            return;
        }
        
        System.out.println("\n所有角色列表:");
        List<Role> roles = roleService.getAllRoles();
        if (roles.isEmpty()) {
            System.out.println("暂无角色数据。");
            return;
        }
        System.out.printf("%-5s %-20s %-25s%n", "ID", "角色名称", "角色编码");
        System.out.println("--------------------------------------------------");
        for (Role role : roles) {
            System.out.printf("%-5d %-20s %-25s%n",
                role.getId(), role.getRoleName(), role.getRoleCode());
        }
        
        List<Integer> assignedRoleIds = userService.getUserRoleIds(userId);
        if (!assignedRoleIds.isEmpty()) {
            System.out.println("\n该用户已分配的角色ID: " + assignedRoleIds);
        }
        
        System.out.print("\n请输入要分配的角色ID: ");
        int roleId = scanner.nextInt();
        scanner.nextLine();
        
        Role role = roleService.getRoleById(roleId);
        if (role == null) {
            System.out.println("未找到ID为 " + roleId + " 的角色！");
            return;
        }
        
        boolean result = userService.assignRoleToUser(userId, roleId, currentUserId, currentUsername);
        if (result) {
            System.out.println("为用户 [" + user.getUsername() + "] 分配角色 [" + role.getRoleName() + "] 成功！");
        } else {
            System.out.println("分配角色失败！用户可能已拥有该角色。");
        }
    }
    
    /**
     * 移除用户角色
     */
    private void removeRoleFromUser() {
        System.out.println("\n--- 移除用户角色 ---");
        
        System.out.println("\n所有用户列表:");
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("暂无用户数据。");
            return;
        }
        System.out.printf("%-5s %-15s %-15s %-25s%n", "ID", "用户名", "真实姓名", "邮箱");
        System.out.println("--------------------------------------------------");
        for (User user : users) {
            System.out.printf("%-5d %-15s %-15s %-25s%n",
                user.getId(), user.getUsername(), user.getRealName(), user.getEmail());
        }
        
        System.out.print("\n请输入用户ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        
        User user = userService.getUserById(userId);
        if (user == null) {
            System.out.println("未找到ID为 " + userId + " 的用户！");
            return;
        }
        
        List<Integer> assignedRoleIds = userService.getUserRoleIds(userId);
        if (assignedRoleIds.isEmpty()) {
            System.out.println("该用户未分配任何角色。");
            return;
        }
        
        System.out.println("\n该用户已分配的角色:");
        System.out.printf("%-5s %-20s %-25s%n", "ID", "角色名称", "角色编码");
        System.out.println("--------------------------------------------------");
        for (Integer roleId : assignedRoleIds) {
            Role role = roleService.getRoleById(roleId);
            if (role != null) {
                System.out.printf("%-5d %-20s %-25s%n",
                    role.getId(), role.getRoleName(), role.getRoleCode());
            }
        }
        
        System.out.print("\n请输入要移除的角色ID: ");
        int roleId = scanner.nextInt();
        scanner.nextLine();
        
        Role role = roleService.getRoleById(roleId);
        if (role == null) {
            System.out.println("未找到ID为 " + roleId + " 的角色！");
            return;
        }
        
        boolean result = userService.removeRoleFromUser(userId, roleId, currentUserId, currentUsername);
        if (result) {
            System.out.println("移除用户 [" + user.getUsername() + "] 的角色 [" + role.getRoleName() + "] 成功！");
        } else {
            System.out.println("移除角色失败！用户可能未拥有该角色。");
        }
    }
}

