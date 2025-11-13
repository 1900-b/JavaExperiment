package controller;

import entity.Resource;
import service.ResourceService;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 资源管理控制器
 */
public class ResourceController {
    private ResourceService resourceService = new ResourceService();
    private Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    private Integer currentUserId;
    private String currentUsername;
    
    public ResourceController(Integer currentUserId, String currentUsername) {
        this.currentUserId = currentUserId;
        this.currentUsername = currentUsername;
    }
    
    /**
     * 显示资源管理菜单
     */
    public void showMenu() {
        while (true) {
            System.out.println("\n========== 资源管理 ==========");
            System.out.println("1. 创建资源");
            System.out.println("2. 查询所有资源");
            System.out.println("3. 根据资源ID查询资源");
            System.out.println("4. 根据资源编码查询资源");
            System.out.println("5. 根据资源类型查询资源");
            System.out.println("6. 更新资源");
            System.out.println("7. 删除资源");
            System.out.println("0. 返回主菜单");
            System.out.print("请选择操作: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    createResource();
                    break;
                case 2:
                    listAllResources();
                    break;
                case 3:
                    getResourceById();
                    break;
                case 4:
                    getResourceByCode();
                    break;
                case 5:
                    getResourcesByType();
                    break;
                case 6:
                    updateResource();
                    break;
                case 7:
                    deleteResource();
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
     * 创建资源
     */
    private void createResource() {
        System.out.println("\n--- 创建资源 ---");
        
        String resourceName;
        while (true) {
            System.out.print("请输入资源名称: ");
            resourceName = scanner.nextLine().trim();
            if (resourceName.isEmpty()) {
                System.out.println("错误：资源名称不能为空，请重新输入！");
            } else {
                break;
            }
        }
        
        String resourceCode;
        while (true) {
            System.out.print("请输入资源编码: ");
            resourceCode = scanner.nextLine().trim();
            if (resourceCode.isEmpty()) {
                System.out.println("错误：资源编码不能为空，请重新输入！");
            } else if (containsChinese(resourceCode)) {
                System.out.println("错误：资源编码不能包含中文字符，请重新输入！");
            } else {
                break;
            }
        }
        
        System.out.println("资源类型: 1-MENU(菜单) 2-BUTTON(按钮) 3-DATA(数据)");
        System.out.print("请选择资源类型: ");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();
        
        String resourceType;
        switch (typeChoice) {
            case 1:
                resourceType = "MENU";
                break;
            case 2:
                resourceType = "BUTTON";
                break;
            case 3:
                resourceType = "DATA";
                break;
            default:
                System.out.println("无效的类型，默认为MENU");
                resourceType = "MENU";
        }
        
        System.out.print("请输入资源路径 (可选): ");
        String resourcePath = scanner.nextLine().trim();
        
        System.out.print("请输入资源描述: ");
        String description = scanner.nextLine();
        
        Resource resource = new Resource(resourceName, resourceCode, resourceType, resourcePath, description);
        boolean result = resourceService.createResource(resource, currentUserId, currentUsername);
        
        if (result) {
            System.out.println("创建资源 [" + resourceName + "] 成功！");
        } else {
            System.out.println("创建资源 [" + resourceName + "] 失败！资源编码可能已存在。");
        }
    }
    
    /**
     * 查询所有资源
     */
    private void listAllResources() {
        System.out.println("\n--- 所有资源列表 ---");
        List<Resource> resources = resourceService.getAllResources();
        if (resources.isEmpty()) {
            System.out.println("暂无资源数据。");
        } else {
            System.out.printf("%-5s %-20s %-25s %-15s %-30s %-30s %-10s%n", 
                "ID", "资源名称", "资源编码", "资源类型", "资源路径", "描述", "状态");
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
            for (Resource resource : resources) {
                System.out.printf("%-5d %-20s %-25s %-15s %-30s %-30s %-10s%n",
                    resource.getId(), resource.getResourceName(), resource.getResourceCode(),
                    resource.getResourceType(), 
                    resource.getResourcePath() != null ? resource.getResourcePath() : "",
                    resource.getDescription() != null ? resource.getDescription() : "",
                    resource.getStatus() == 1 ? "启用" : "禁用");
            }
        }
    }
    
    /**
     * 根据ID查询资源
     */
    private void getResourceById() {
        System.out.println("\n--- 根据ID查询资源 ---");
        System.out.print("请输入资源ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Resource resource = resourceService.getResourceById(id);
        if (resource != null) {
            System.out.println("资源信息:");
            System.out.println("  ID: " + resource.getId());
            System.out.println("  资源名称: " + resource.getResourceName());
            System.out.println("  资源编码: " + resource.getResourceCode());
            System.out.println("  资源类型: " + resource.getResourceType());
            System.out.println("  资源路径: " + (resource.getResourcePath() != null ? resource.getResourcePath() : ""));
            System.out.println("  描述: " + (resource.getDescription() != null ? resource.getDescription() : ""));
            System.out.println("  状态: " + (resource.getStatus() == 1 ? "启用" : "禁用"));
            System.out.println("  创建时间: " + resource.getCreateTime());
        } else {
            System.out.println("未找到ID为 " + id + " 的资源！");
        }
    }
    
    /**
     * 根据资源编码查询资源
     */
    private void getResourceByCode() {
        System.out.println("\n--- 根据资源编码查询资源 ---");
        System.out.print("请输入资源编码: ");
        String resourceCode = scanner.nextLine();
        
        Resource resource = resourceService.getResourceByCode(resourceCode);
        if (resource != null) {
            System.out.println("资源信息:");
            System.out.println("  ID: " + resource.getId());
            System.out.println("  资源名称: " + resource.getResourceName());
            System.out.println("  资源编码: " + resource.getResourceCode());
            System.out.println("  资源类型: " + resource.getResourceType());
            System.out.println("  资源路径: " + (resource.getResourcePath() != null ? resource.getResourcePath() : ""));
            System.out.println("  描述: " + (resource.getDescription() != null ? resource.getDescription() : ""));
            System.out.println("  状态: " + (resource.getStatus() == 1 ? "启用" : "禁用"));
            System.out.println("  创建时间: " + resource.getCreateTime());
        } else {
            System.out.println("未找到资源编码为 " + resourceCode + " 的资源！");
        }
    }
    
    /**
     * 根据资源类型查询资源
     */
    private void getResourcesByType() {
        System.out.println("\n--- 根据资源类型查询资源 ---");
        System.out.println("资源类型: 1-MENU(菜单) 2-BUTTON(按钮) 3-DATA(数据)");
        System.out.print("请选择资源类型: ");
        int typeChoice = scanner.nextInt();
        scanner.nextLine();
        
        String resourceType;
        switch (typeChoice) {
            case 1:
                resourceType = "MENU";
                break;
            case 2:
                resourceType = "BUTTON";
                break;
            case 3:
                resourceType = "DATA";
                break;
            default:
                System.out.println("无效的类型");
                return;
        }
        
        List<Resource> resources = resourceService.getResourcesByType(resourceType);
        if (resources.isEmpty()) {
            System.out.println("暂无该类型的资源数据。");
        } else {
            System.out.println("\n--- " + resourceType + " 类型资源列表 ---");
            System.out.printf("%-5s %-20s %-25s %-30s %-30s %-10s%n", 
                "ID", "资源名称", "资源编码", "资源路径", "描述", "状态");
            System.out.println("------------------------------------------------------------------------------------------------------------------------");
            for (Resource resource : resources) {
                System.out.printf("%-5d %-20s %-25s %-30s %-30s %-10s%n",
                    resource.getId(), resource.getResourceName(), resource.getResourceCode(),
                    resource.getResourcePath() != null ? resource.getResourcePath() : "",
                    resource.getDescription() != null ? resource.getDescription() : "",
                    resource.getStatus() == 1 ? "启用" : "禁用");
            }
        }
    }
    
    /**
     * 更新资源
     */
    private void updateResource() {
        System.out.println("\n--- 更新资源 ---");
        System.out.print("请输入要更新的资源ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Resource resource = resourceService.getResourceById(id);
        if (resource == null) {
            System.out.println("未找到ID为 " + id + " 的资源！");
            return;
        }
        
        System.out.print("请输入新资源名称 (当前: " + resource.getResourceName() + ", 直接回车保持): ");
        String resourceName = scanner.nextLine();
        if (!resourceName.isEmpty()) {
            resource.setResourceName(resourceName);
        }
        
        System.out.print("请输入新资源编码 (当前: " + resource.getResourceCode() + ", 直接回车保持): ");
        String resourceCode = scanner.nextLine().trim();
        if (!resourceCode.isEmpty()) {
            if (containsChinese(resourceCode)) {
                System.out.println("错误：资源编码不能包含中文字符！");
                return;
            }
            resource.setResourceCode(resourceCode);
        }
        
        System.out.print("请输入新资源路径 (当前: " + (resource.getResourcePath() != null ? resource.getResourcePath() : "") + ", 直接回车保持): ");
        String resourcePath = scanner.nextLine().trim();
        if (!resourcePath.isEmpty()) {
            resource.setResourcePath(resourcePath);
        }
        
        System.out.print("请输入新描述 (当前: " + (resource.getDescription() != null ? resource.getDescription() : "") + ", 直接回车保持): ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) {
            resource.setDescription(description);
        }
        
        boolean result = resourceService.updateResource(resource, currentUserId, currentUsername);
        if (result) {
            System.out.println("更新资源 [" + resource.getResourceName() + "] 成功！");
        } else {
            System.out.println("更新资源失败！");
        }
    }
    
    /**
     * 删除资源
     */
    private void deleteResource() {
        System.out.println("\n--- 删除资源 ---");
        System.out.print("请输入要删除的资源ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("确认删除资源ID " + id + "? (y/n): ");
        String confirm = scanner.nextLine();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("已取消删除操作。");
            return;
        }
        
        boolean result = resourceService.deleteResource(id, currentUserId, currentUsername);
        if (result) {
            System.out.println("删除资源成功！");
        } else {
            System.out.println("删除资源失败！");
        }
    }
}

