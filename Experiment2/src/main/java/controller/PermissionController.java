package controller;

import entity.Permission;
import entity.Resource;
import service.PermissionService;
import service.ResourceService;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 权限管理控制器
 */
public class PermissionController {
    private PermissionService permissionService = new PermissionService();
    private ResourceService resourceService = new ResourceService();
    private Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    private Integer currentUserId;
    private String currentUsername;
    
    public PermissionController(Integer currentUserId, String currentUsername) {
        this.currentUserId = currentUserId;
        this.currentUsername = currentUsername;
    }
    
    /**
     * 显示权限管理菜单
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n========== 权限管理 ==========");
            System.out.println("1. 创建权限");
            System.out.println("2. 查询所有权限");
            System.out.println("3. 根据权限ID查询权限");
            System.out.println("4. 根据权限编码查询权限");
            System.out.println("5. 根据权限类型查询权限");
            System.out.println("6. 更新权限");
            System.out.println("7. 删除权限");
            System.out.println("8. 为权限分配资源");
            System.out.println("9. 移除权限资源");
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    createPermission();
                    break;
                case 2:
                    listAllPermissions();
                    break;
                case 3:
                    getPermissionById();
                    break;
                case 4:
                    getPermissionByCode();
                    break;
                case 5:
                    getPermissionsByType();
                    break;
                case 6:
                    updatePermission();
                    break;
                case 7:
                    deletePermission();
                    break;
                case 8:
                    assignResourceToPermission();
                    break;
                case 9:
                    removeResourceFromPermission();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入！");
            }
        }
    }
    
    /**
     * 检查字符串是否包含中文字符
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
     * 创建权限
     */
    private void createPermission() {
        System.out.println("\n--- 创建权限 ---");
        
        String permissionName;
        while (true) {
            System.out.print("请输入权限名称: ");
            permissionName = scanner.nextLine().trim();
            if (permissionName.isEmpty()) {
                System.out.println("错误：权限名称不能为空，请重新输入！");
            } else {
                break;
            }
        }
        
        String permissionCode;
        while (true) {
            System.out.print("请输入权限编码: ");
            permissionCode = scanner.nextLine().trim();
            if (permissionCode.isEmpty()) {
                System.out.println("错误：权限编码不能为空，请重新输入！");
            } else if (containsChinese(permissionCode)) {
                System.out.println("错误：权限编码不能包含中文字符，请重新输入！");
            } else {
                break;
            }
        }
        
        System.out.println("权限类型: 1-MENU(菜单权限) 2-OPERATION(操作权限) 3-RESOURCE(资源权限)");
        System.out.print("请选择权限类型: ");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();
        
        String permissionType;
        switch (typeChoice) {
            case 1:
                permissionType = "MENU";
                break;
            case 2:
                permissionType = "OPERATION";
                break;
            case 3:
                permissionType = "RESOURCE";
                break;
            default:
                System.out.println("无效的类型，默认为OPERATION");
                permissionType = "OPERATION";
        }
        
        System.out.print("请输入权限描述: ");
        String description = scanner.nextLine();
        
        Permission permission = new Permission(permissionName, permissionCode, permissionType, description);
        boolean result = permissionService.createPermission(permission, currentUserId, currentUsername);
        
        if (result) {
            System.out.println("创建权限 [" + permissionName + "] 成功！");
        } else {
            System.out.println("创建权限 [" + permissionName + "] 失败！权限编码可能已存在。");
        }
    }
    
    /**
     * 查询所有权限
     */
    private void listAllPermissions() {
        System.out.println("\n--- 所有权限列表 ---");
        List<Permission> permissions = permissionService.getAllPermissions();
        if (permissions.isEmpty()) {
            System.out.println("暂无权限数据。");
        } else {
            System.out.printf("%-5s %-20s %-25s %-15s %-30s %-10s%n", 
                "ID", "权限名称", "权限编码", "权限类型", "描述", "状态");
            System.out.println("--------------------------------------------------------------------------------");
            for (Permission permission : permissions) {
                System.out.printf("%-5d %-20s %-25s %-15s %-30s %-10s%n",
                    permission.getId(), permission.getPermissionName(), permission.getPermissionCode(),
                    permission.getPermissionType(), permission.getDescription(),
                    permission.getStatus() == 1 ? "启用" : "禁用");
            }
        }
    }
    
    /**
     * 根据ID查询权限
     */
    private void getPermissionById() {
        System.out.println("\n--- 根据ID查询权限 ---");
        System.out.print("请输入权限ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Permission permission = permissionService.getPermissionById(id);
        if (permission != null) {
            System.out.println("权限信息:");
            System.out.println("  ID: " + permission.getId());
            System.out.println("  权限名称: " + permission.getPermissionName());
            System.out.println("  权限编码: " + permission.getPermissionCode());
            System.out.println("  权限类型: " + permission.getPermissionType());
            System.out.println("  描述: " + permission.getDescription());
            System.out.println("  状态: " + (permission.getStatus() == 1 ? "启用" : "禁用"));
            System.out.println("  创建时间: " + permission.getCreateTime());
        } else {
            System.out.println("未找到ID为 " + id + " 的权限！");
        }
    }
    
    /**
     * 根据权限编码查询权限
     */
    private void getPermissionByCode() {
        System.out.println("\n--- 根据权限编码查询权限 ---");
        System.out.print("请输入权限编码: ");
        String permissionCode = scanner.nextLine();
        
        Permission permission = permissionService.getPermissionByCode(permissionCode);
        if (permission != null) {
            System.out.println("权限信息:");
            System.out.println("  ID: " + permission.getId());
            System.out.println("  权限名称: " + permission.getPermissionName());
            System.out.println("  权限编码: " + permission.getPermissionCode());
            System.out.println("  权限类型: " + permission.getPermissionType());
            System.out.println("  描述: " + permission.getDescription());
            System.out.println("  状态: " + (permission.getStatus() == 1 ? "启用" : "禁用"));
            System.out.println("  创建时间: " + permission.getCreateTime());
        } else {
            System.out.println("未找到权限编码为 " + permissionCode + " 的权限！");
        }
    }
    
    /**
     * 根据权限类型查询权限
     */
    private void getPermissionsByType() {
        System.out.println("\n--- 根据权限类型查询权限 ---");
        System.out.println("权限类型: 1-MENU(菜单权限) 2-OPERATION(操作权限) 3-RESOURCE(资源权限)");
        System.out.print("请选择权限类型: ");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();
        
        String permissionType;
        switch (typeChoice) {
            case 1:
                permissionType = "MENU";
                break;
            case 2:
                permissionType = "OPERATION";
                break;
            case 3:
                permissionType = "RESOURCE";
                break;
            default:
                System.out.println("无效的类型");
                return;
        }
        
        List<Permission> permissions = permissionService.getPermissionsByType(permissionType);
        if (permissions.isEmpty()) {
            System.out.println("暂无该类型的权限数据。");
        } else {
            System.out.println("\n--- " + permissionType + " 类型权限列表 ---");
            System.out.printf("%-5s %-20s %-25s %-30s %-10s%n", 
                "ID", "权限名称", "权限编码", "描述", "状态");
            System.out.println("--------------------------------------------------------------------------------");
            for (Permission permission : permissions) {
                System.out.printf("%-5d %-20s %-25s %-30s %-10s%n",
                    permission.getId(), permission.getPermissionName(), permission.getPermissionCode(),
                    permission.getDescription(), permission.getStatus() == 1 ? "启用" : "禁用");
            }
        }
    }
    
    /**
     * 更新权限
     */
    private void updatePermission() {
        System.out.println("\n--- 更新权限 ---");
        System.out.print("请输入要更新的权限ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Permission permission = permissionService.getPermissionById(id);
        if (permission == null) {
            System.out.println("未找到ID为 " + id + " 的权限！");
            return;
        }
        
        System.out.print("请输入新权限名称 (当前: " + permission.getPermissionName() + ", 直接回车保持): ");
        String permissionName = scanner.nextLine();
        if (!permissionName.isEmpty()) {
            permission.setPermissionName(permissionName);
        }
        
        System.out.print("请输入新权限编码 (当前: " + permission.getPermissionCode() + ", 直接回车保持): ");
        String permissionCode = scanner.nextLine().trim();
        if (!permissionCode.isEmpty()) {
            if (containsChinese(permissionCode)) {
                System.out.println("错误：权限编码不能包含中文字符！");
                return;
            }
            permission.setPermissionCode(permissionCode);
        }
        
        System.out.print("请输入新描述 (当前: " + permission.getDescription() + ", 直接回车保持): ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            permission.setDescription(description);
        }
        
        boolean result = permissionService.updatePermission(permission, currentUserId, currentUsername);
        if (result) {
            System.out.println("更新权限 [" + permission.getPermissionName() + "] 成功！");
        } else {
            System.out.println("更新权限失败！");
        }
    }
    
    /**
     * 删除权限
     */
    private void deletePermission() {
        System.out.println("\n--- 删除权限 ---");
        System.out.print("请输入要删除的权限ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("确认删除权限ID " + id + "? (y/n): ");
        String confirm = scanner.nextLine();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("已取消删除操作。");
            return;
        }
        
        boolean result = permissionService.deletePermission(id, currentUserId, currentUsername);
        if (result) {
            System.out.println("删除权限成功！");
        } else {
            System.out.println("删除权限失败！");
        }
    }
    
    /**
     * 为权限分配资源
     */
    private void assignResourceToPermission() {
        System.out.println("\n--- 为权限分配资源 ---");
        
        System.out.println("\n所有权限列表:");
        List<Permission> permissions = permissionService.getAllPermissions();
        if (permissions.isEmpty()) {
            System.out.println("暂无权限数据。");
            return;
        }
        System.out.printf("%-5s %-20s %-25s%n", "ID", "权限名称", "权限编码");
        System.out.println("--------------------------------------------------");
        for (Permission permission : permissions) {
            System.out.printf("%-5d %-20s %-25s%n",
                permission.getId(), permission.getPermissionName(), permission.getPermissionCode());
        }
        
        System.out.print("\n请输入权限ID: ");
        int permissionId = scanner.nextInt();
        scanner.nextLine();
        
        Permission permission = permissionService.getPermissionById(permissionId);
        if (permission == null) {
            System.out.println("未找到ID为 " + permissionId + " 的权限！");
            return;
        }
        
        System.out.println("\n所有资源列表:");
        List<Resource> resources = resourceService.getAllResources();
        if (resources.isEmpty()) {
            System.out.println("暂无资源数据。");
            return;
        }
        System.out.printf("%-5s %-20s %-25s %-15s%n", "ID", "资源名称", "资源编码", "资源类型");
        System.out.println("----------------------------------------------------------------");
        for (Resource resource : resources) {
            System.out.printf("%-5d %-20s %-25s %-15s%n",
                resource.getId(), resource.getResourceName(), resource.getResourceCode(), resource.getResourceType());
        }
        
        List<Integer> assignedResourceIds = permissionService.getResourceIdsByPermissionId(permissionId);
        if (!assignedResourceIds.isEmpty()) {
            System.out.println("\n该权限已分配的资源ID: " + assignedResourceIds);
        }
        
        System.out.print("\n请输入要分配的资源ID: ");
        int resourceId = scanner.nextInt();
        scanner.nextLine();
        
        Resource resource = resourceService.getResourceById(resourceId);
        if (resource == null) {
            System.out.println("未找到ID为 " + resourceId + " 的资源！");
            return;
        }
        
        boolean result = permissionService.assignResourceToPermission(permissionId, resourceId, currentUserId, currentUsername);
        if (result) {
            System.out.println("为权限 [" + permission.getPermissionName() + "] 分配资源 [" + resource.getResourceName() + "] 成功！");
        } else {
            System.out.println("分配资源失败！权限可能已拥有该资源。");
        }
    }
    
    /**
     * 移除权限资源
     */
    private void removeResourceFromPermission() {
        System.out.println("\n--- 移除权限资源 ---");
        
        System.out.println("\n所有权限列表:");
        List<Permission> permissions = permissionService.getAllPermissions();
        if (permissions.isEmpty()) {
            System.out.println("暂无权限数据。");
            return;
        }
        System.out.printf("%-5s %-20s %-25s%n", "ID", "权限名称", "权限编码");
        System.out.println("--------------------------------------------------");
        for (Permission permission : permissions) {
            System.out.printf("%-5d %-20s %-25s%n",
                permission.getId(), permission.getPermissionName(), permission.getPermissionCode());
        }
        
        System.out.print("\n请输入权限ID: ");
        int permissionId = scanner.nextInt();
        scanner.nextLine();
        
        Permission permission = permissionService.getPermissionById(permissionId);
        if (permission == null) {
            System.out.println("未找到ID为 " + permissionId + " 的权限！");
            return;
        }
        
        List<Integer> assignedResourceIds = permissionService.getResourceIdsByPermissionId(permissionId);
        if (assignedResourceIds.isEmpty()) {
            System.out.println("该权限未分配任何资源。");
            return;
        }
        
        System.out.println("\n该权限已分配的资源:");
        System.out.printf("%-5s %-20s %-25s %-15s%n", "ID", "资源名称", "资源编码", "资源类型");
        System.out.println("----------------------------------------------------------------");
        for (Integer resourceId : assignedResourceIds) {
            Resource resource = resourceService.getResourceById(resourceId);
            if (resource != null) {
                System.out.printf("%-5d %-20s %-25s %-15s%n",
                    resource.getId(), resource.getResourceName(), resource.getResourceCode(), resource.getResourceType());
            }
        }
        
        System.out.print("\n请输入要移除的资源ID: ");
        int resourceId = scanner.nextInt();
        scanner.nextLine();
        
        Resource resource = resourceService.getResourceById(resourceId);
        if (resource == null) {
            System.out.println("未找到ID为 " + resourceId + " 的资源！");
            return;
        }
        
        boolean result = permissionService.removeResourceFromPermission(permissionId, resourceId, currentUserId, currentUsername);
        if (result) {
            System.out.println("移除权限 [" + permission.getPermissionName() + "] 的资源 [" + resource.getResourceName() + "] 成功！");
        } else {
            System.out.println("移除资源失败！权限可能未拥有该资源。");
        }
    }
}

