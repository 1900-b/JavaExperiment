package controller;

import entity.Permission;
import entity.Role;
import service.PermissionService;
import service.RoleService;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 角色管理控制器
 */
public class RoleController {
    private RoleService roleService = new RoleService();
    private PermissionService permissionService = new PermissionService();
    private Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    private Integer currentUserId;
    private String currentUsername;
    
    public RoleController(Integer currentUserId, String currentUsername) {
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
     * 显示角色管理菜单
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n========== 角色管理 ==========");
            System.out.println("1. 创建角色");
            System.out.println("2. 查询所有角色");
            System.out.println("3. 根据角色ID查询角色");
            System.out.println("4. 根据角色编码查询角色");
            System.out.println("5. 更新角色");
            System.out.println("6. 删除角色");
            System.out.println("7. 为角色分配权限");
            System.out.println("8. 移除角色权限");
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    createRole();
                    break;
                case 2:
                    listAllRoles();
                    break;
                case 3:
                    getRoleById();
                    break;
                case 4:
                    getRoleByCode();
                    break;
                case 5:
                    updateRole();
                    break;
                case 6:
                    deleteRole();
                    break;
                case 7:
                    assignPermissionToRole();
                    break;
                case 8:
                    removePermissionFromRole();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }
    
    /**
     * 创建角色
     */
    private void createRole() {
        System.out.println("\n--- 创建角色 ---");
        
        String roleName;
        while (true) {
            System.out.print("请输入角色名称: ");
            roleName = scanner.nextLine().trim();
            if (roleName.isEmpty()) {
                System.out.println("错误：角色名称不能为空，请重新输入！");
            } else {
                break;
            }
        }
        
        String roleCode;
        while (true) {
            System.out.print("请输入角色编码: ");
            roleCode = scanner.nextLine().trim();
            if(roleCode.isEmpty()){
                System.out.println("错误：角色编码不能为空，请重新输入！");
            }
            else if (containsChinese(roleCode)) {
                System.out.println("错误：角色编码不能包含中文字符，请重新输入！");
            } else {
                break;
            }
        }
        
        System.out.print("请输入角色描述: ");
        String description = scanner.nextLine().trim();
        
        Role role = new Role(roleName, roleCode, description);
        boolean result = roleService.createRole(role, currentUserId, currentUsername);
        
        if (result) {
            System.out.println("创建角色 [" + roleName + "] 成功！");
        } else {
            System.out.println("创建角色 [" + roleName + "] 失败！角色编码可能已存在。");
        }
    }
    
    /**
     * 查询所有角色
     */
    private void listAllRoles() {
        System.out.println("\n--- 所有角色列表 ---");
        List<Role> roles = roleService.getAllRoles();
        if (roles.isEmpty()) {
            System.out.println("暂无角色数据。");
        } else {
            System.out.printf("%-5s %-15s %-20s %-30s %-10s %-20s%n", 
                "ID", "角色名称", "角色编码", "描述", "状态", "创建时间");
            System.out.println("--------------------------------------------------------------------------------");
            for (Role role : roles) {
                System.out.printf("%-5d %-15s %-20s %-30s %-10s %-20s%n",
                    role.getId(), role.getRoleName(), role.getRoleCode(), 
                    role.getDescription(), role.getStatus() == 1 ? "启用" : "禁用",
                    role.getCreateTime());
            }
        }
    }
    
    /**
     * 根据ID查询角色
     */
    private void getRoleById() {
        System.out.println("\n--- 根据ID查询角色 ---");
        System.out.print("请输入角色ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Role role = roleService.getRoleById(id);
        if (role != null) {
            System.out.println("角色信息:");
            System.out.println("  ID: " + role.getId());
            System.out.println("  角色名称: " + role.getRoleName());
            System.out.println("  角色编码: " + role.getRoleCode());
            System.out.println("  描述: " + role.getDescription());
            System.out.println("  状态: " + (role.getStatus() == 1 ? "启用" : "禁用"));
            System.out.println("  创建时间: " + role.getCreateTime());
        } else {
            System.out.println("未找到ID为 " + id + " 的角色！");
        }
    }
    
    /**
     * 根据角色编码查询角色
     */
    private void getRoleByCode() {
        System.out.println("\n--- 根据角色编码查询角色 ---");
        System.out.print("请输入角色编码: ");
        String roleCode = scanner.nextLine();
        
        Role role = roleService.getRoleByCode(roleCode);
        if (role != null) {
            System.out.println("角色信息:");
            System.out.println("  ID: " + role.getId());
            System.out.println("  角色名称: " + role.getRoleName());
            System.out.println("  角色编码: " + role.getRoleCode());
            System.out.println("  描述: " + role.getDescription());
            System.out.println("  状态: " + (role.getStatus() == 1 ? "启用" : "禁用"));
            System.out.println("  创建时间: " + role.getCreateTime());
        } else {
            System.out.println("未找到角色编码为 " + roleCode + " 的角色！");
        }
    }
    
    /**
     * 更新角色
     */
    private void updateRole() {
        System.out.println("\n--- 更新角色 ---");
        System.out.print("请输入要更新的角色ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Role role = roleService.getRoleById(id);
        if (role == null) {
            System.out.println("未找到ID为 " + id + " 的角色！");
            return;
        }
        
        System.out.print("请输入新角色名称 (当前: " + role.getRoleName() + ", 直接回车保持): ");
        String roleName = scanner.nextLine();
        if (!roleName.isEmpty()) {
            role.setRoleName(roleName);
        }
        
        String roleCode;
        while (true) {
            System.out.print("请输入新角色编码 (当前: " + role.getRoleCode() + ", 直接回车保持): ");
            roleCode = scanner.nextLine().trim();
            if (roleCode.isEmpty()) {
                break;
            } else if (containsChinese(roleCode)) {
                System.out.println("错误：角色编码不能包含中文字符，请重新输入！");
            } else {
                role.setRoleCode(roleCode);
                break;
            }
        }
        
        System.out.print("请输入新描述 (当前: " + role.getDescription() + ", 直接回车保持): ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            role.setDescription(description);
        }
        
        boolean result = roleService.updateRole(role, currentUserId, currentUsername);
        if (result) {
            System.out.println("更新角色 [" + role.getRoleName() + "] 成功！");
        } else {
            System.out.println("更新角色失败！");
        }
    }
    
    /**
     * 删除角色
     */
    private void deleteRole() {
        System.out.println("\n--- 删除角色 ---");
        System.out.print("请输入要删除的角色ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("确认删除角色ID " + id + "? (y/n): ");
        String confirm = scanner.nextLine();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("已取消删除操作。");
            return;
        }
        
        boolean result = roleService.deleteRole(id, currentUserId, currentUsername);
        if (result) {
            System.out.println("删除角色成功！");
        } else {
            System.out.println("删除角色失败！");
        }
    }
    
    /**
     * 为角色分配权限
     */
    private void assignPermissionToRole() {
        System.out.println("\n--- 为角色分配权限 ---");
        
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
        
        System.out.print("\n请输入角色ID: ");
        int roleId = scanner.nextInt();
        scanner.nextLine();
        
        Role role = roleService.getRoleById(roleId);
        if (role == null) {
            System.out.println("未找到ID为 " + roleId + " 的角色！");
            return;
        }
        
        System.out.println("\n所有权限列表:");
        List<Permission> permissions = permissionService.getAllPermissions();
        if (permissions.isEmpty()) {
            System.out.println("暂无权限数据。");
            return;
        }
        System.out.printf("%-5s %-20s %-25s %-15s%n", "ID", "权限名称", "权限编码", "权限类型");
        System.out.println("----------------------------------------------------------------");
        for (Permission permission : permissions) {
            System.out.printf("%-5d %-20s %-25s %-15s%n",
                permission.getId(), permission.getPermissionName(), permission.getPermissionCode(), permission.getPermissionType());
        }
        
        List<Integer> assignedPermissionIds = roleService.getRolePermissionIds(roleId);
        if (!assignedPermissionIds.isEmpty()) {
            System.out.println("\n该角色已分配的权限ID: " + assignedPermissionIds);
        }
        
        System.out.print("\n请输入要分配的权限ID: ");
        int permissionId = scanner.nextInt();
        scanner.nextLine();
        
        Permission permission = permissionService.getPermissionById(permissionId);
        if (permission == null) {
            System.out.println("未找到ID为 " + permissionId + " 的权限！");
            return;
        }
        
        boolean result = roleService.assignPermissionToRole(roleId, permissionId, currentUserId, currentUsername);
        if (result) {
            System.out.println("为角色 [" + role.getRoleName() + "] 分配权限 [" + permission.getPermissionName() + "] 成功！");
        } else {
            System.out.println("分配权限失败！角色可能已拥有该权限。");
        }
    }
    
    /**
     * 移除角色权限
     */
    private void removePermissionFromRole() {
        System.out.println("\n--- 移除角色权限 ---");
        
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
        
        System.out.print("\n请输入角色ID: ");
        int roleId = scanner.nextInt();
        scanner.nextLine();
        
        Role role = roleService.getRoleById(roleId);
        if (role == null) {
            System.out.println("未找到ID为 " + roleId + " 的角色！");
            return;
        }
        
        List<Integer> assignedPermissionIds = roleService.getRolePermissionIds(roleId);
        if (assignedPermissionIds.isEmpty()) {
            System.out.println("该角色未分配任何权限。");
            return;
        }
        
        System.out.println("\n该角色已分配的权限:");
        System.out.printf("%-5s %-20s %-25s %-15s%n", "ID", "权限名称", "权限编码", "权限类型");
        System.out.println("----------------------------------------------------------------");
        for (Integer permissionId : assignedPermissionIds) {
            Permission permission = permissionService.getPermissionById(permissionId);
            if (permission != null) {
                System.out.printf("%-5d %-20s %-25s %-15s%n",
                    permission.getId(), permission.getPermissionName(), permission.getPermissionCode(), permission.getPermissionType());
            }
        }
        
        System.out.print("\n请输入要移除的权限ID: ");
        int permissionId = scanner.nextInt();
        scanner.nextLine();
        
        Permission permission = permissionService.getPermissionById(permissionId);
        if (permission == null) {
            System.out.println("未找到ID为 " + permissionId + " 的权限！");
            return;
        }
        
        boolean result = roleService.removePermissionFromRole(roleId, permissionId, currentUserId, currentUsername);
        if (result) {
            System.out.println("移除角色 [" + role.getRoleName() + "] 的权限 [" + permission.getPermissionName() + "] 成功！");
        } else {
            System.out.println("移除权限失败！角色可能未拥有该权限。");
        }
    }
}


