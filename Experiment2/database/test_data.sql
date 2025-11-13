-- 权限管理系统测试数据脚本
-- 用于填充测试数据，便于功能测试
-- 
-- 重要提示：执行此脚本前，请确保MySQL客户端字符集为UTF-8
-- 在MySQL命令行中执行: SET NAMES utf8mb4;
-- 或者在执行SOURCE命令前先执行: SET NAMES utf8mb4;

USE authority_management_system;

-- 设置会话字符集为utf8mb4，确保中文字符正确存储
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;
SET character_set_results=utf8mb4;
SET character_set_client=utf8mb4;

-- ============================================
-- 1. 用户数据
-- ============================================
-- 注意：如果admin用户已存在，使用INSERT IGNORE避免冲突
INSERT IGNORE INTO `user` (`id`, `username`, `password`, `real_name`, `email`, `status`, `create_time`, `update_time`) VALUES
(1, 'admin', 'admin', '系统管理员', 'admin@system.com', 1, NOW(), NOW()),
(2, 'manager', 'manager', '部门经理', 'manager@system.com', 1, NOW(), NOW()),
(3, 'user1', 'user1', '普通用户1', 'user1@system.com', 1, NOW(), NOW()),
(4, 'user2', 'user2', '普通用户2', 'user2@system.com', 1, NOW(), NOW()),
(5, 'guest', 'guest', '访客用户', 'guest@system.com', 1, NOW(), NOW()),
(6, 'disabled_user', 'disabled', '禁用用户', 'disabled@system.com', 0, NOW(), NOW()),
(7, 'test_user', 'test', '测试用户', 'test@system.com', 1, NOW(), NOW());

-- ============================================
-- 2. 角色数据
-- ============================================
INSERT IGNORE INTO `role` (`id`, `role_name`, `role_code`, `description`, `status`, `create_time`, `update_time`) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有所有权限的超级管理员', 1, NOW(), NOW()),
(2, '系统管理员', 'ADMIN', '系统管理员，可以管理用户、角色、权限', 1, NOW(), NOW()),
(3, '部门经理', 'MANAGER', '部门经理，可以管理本部门用户', 1, NOW(), NOW()),
(4, '普通用户', 'USER', '普通用户，只有基本查看权限', 1, NOW(), NOW()),
(5, '访客', 'GUEST', '访客用户，只有只读权限', 1, NOW(), NOW()),
(6, '测试角色', 'TEST_ROLE', '用于测试的角色', 1, NOW(), NOW());

-- ============================================
-- 3. 权限数据
-- ============================================
INSERT IGNORE INTO `permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `description`, `status`, `create_time`, `update_time`) VALUES
-- 用户管理权限
(1, '用户查看', 'USER_VIEW', 'OPERATION', '查看用户列表和详情', 1, NOW(), NOW()),
(2, '用户创建', 'USER_CREATE', 'OPERATION', '创建新用户', 1, NOW(), NOW()),
(3, '用户修改', 'USER_UPDATE', 'OPERATION', '修改用户信息', 1, NOW(), NOW()),
(4, '用户删除', 'USER_DELETE', 'OPERATION', '删除用户', 1, NOW(), NOW()),
(5, '用户启用/禁用', 'USER_TOGGLE', 'OPERATION', '启用或禁用用户', 1, NOW(), NOW()),

-- 角色管理权限
(6, '角色查看', 'ROLE_VIEW', 'OPERATION', '查看角色列表和详情', 1, NOW(), NOW()),
(7, '角色创建', 'ROLE_CREATE', 'OPERATION', '创建新角色', 1, NOW(), NOW()),
(8, '角色修改', 'ROLE_UPDATE', 'OPERATION', '修改角色信息', 1, NOW(), NOW()),
(9, '角色删除', 'ROLE_DELETE', 'OPERATION', '删除角色', 1, NOW(), NOW()),

-- 权限管理权限
(10, '权限查看', 'PERMISSION_VIEW', 'OPERATION', '查看权限列表和详情', 1, NOW(), NOW()),
(11, '权限创建', 'PERMISSION_CREATE', 'OPERATION', '创建新权限', 1, NOW(), NOW()),
(12, '权限修改', 'PERMISSION_UPDATE', 'OPERATION', '修改权限信息', 1, NOW(), NOW()),
(13, '权限删除', 'PERMISSION_DELETE', 'OPERATION', '删除权限', 1, NOW(), NOW()),

-- 资源管理权限
(14, '资源查看', 'RESOURCE_VIEW', 'OPERATION', '查看资源列表和详情', 1, NOW(), NOW()),
(15, '资源创建', 'RESOURCE_CREATE', 'OPERATION', '创建新资源', 1, NOW(), NOW()),
(16, '资源修改', 'RESOURCE_UPDATE', 'OPERATION', '修改资源信息', 1, NOW(), NOW()),
(17, '资源删除', 'RESOURCE_DELETE', 'OPERATION', '删除资源', 1, NOW(), NOW()),

-- 角色分配权限
(18, '角色分配', 'ROLE_ASSIGN', 'OPERATION', '为用户分配角色', 1, NOW(), NOW()),
(19, '权限分配', 'PERMISSION_ASSIGN', 'OPERATION', '为角色分配权限', 1, NOW(), NOW()),
(20, '资源分配', 'RESOURCE_ASSIGN', 'OPERATION', '为权限分配资源', 1, NOW(), NOW()),

-- 审计日志权限
(21, '审计日志查看', 'AUDIT_VIEW', 'OPERATION', '查看审计日志', 1, NOW(), NOW()),

-- 菜单权限
(22, '用户管理菜单', 'MENU_USER', 'MENU', '用户管理菜单访问权限', 1, NOW(), NOW()),
(23, '角色管理菜单', 'MENU_ROLE', 'MENU', '角色管理菜单访问权限', 1, NOW(), NOW()),
(24, '权限管理菜单', 'MENU_PERMISSION', 'MENU', '权限管理菜单访问权限', 1, NOW(), NOW()),
(25, '资源管理菜单', 'MENU_RESOURCE', 'MENU', '资源管理菜单访问权限', 1, NOW(), NOW()),
(26, '审计日志菜单', 'MENU_AUDIT', 'MENU', '审计日志菜单访问权限', 1, NOW(), NOW());

-- ============================================
-- 4. 资源数据
-- ============================================
INSERT IGNORE INTO `resource` (`id`, `resource_name`, `resource_code`, `resource_type`, `resource_path`, `description`, `status`, `create_time`, `update_time`) VALUES
-- 菜单资源
(1, '用户管理菜单', 'RES_MENU_USER', 'MENU', '/user', '用户管理菜单', 1, NOW(), NOW()),
(2, '角色管理菜单', 'RES_MENU_ROLE', 'MENU', '/role', '角色管理菜单', 1, NOW(), NOW()),
(3, '权限管理菜单', 'RES_MENU_PERMISSION', 'MENU', '/permission', '权限管理菜单', 1, NOW(), NOW()),
(4, '资源管理菜单', 'RES_MENU_RESOURCE', 'MENU', '/resource', '资源管理菜单', 1, NOW(), NOW()),
(5, '审计日志菜单', 'RES_MENU_AUDIT', 'MENU', '/audit', '审计日志菜单', 1, NOW(), NOW()),

-- 按钮资源
(6, '创建用户按钮', 'RES_BTN_USER_CREATE', 'BUTTON', '/user/create', '创建用户按钮', 1, NOW(), NOW()),
(7, '编辑用户按钮', 'RES_BTN_USER_EDIT', 'BUTTON', '/user/edit', '编辑用户按钮', 1, NOW(), NOW()),
(8, '删除用户按钮', 'RES_BTN_USER_DELETE', 'BUTTON', '/user/delete', '删除用户按钮', 1, NOW(), NOW()),
(9, '创建角色按钮', 'RES_BTN_ROLE_CREATE', 'BUTTON', '/role/create', '创建角色按钮', 1, NOW(), NOW()),
(10, '编辑角色按钮', 'RES_BTN_ROLE_EDIT', 'BUTTON', '/role/edit', '编辑角色按钮', 1, NOW(), NOW()),
(11, '删除角色按钮', 'RES_BTN_ROLE_DELETE', 'BUTTON', '/role/delete', '删除角色按钮', 1, NOW(), NOW()),

-- 数据资源
(12, '用户数据', 'RES_DATA_USER', 'DATA', '/api/user/*', '用户数据访问', 1, NOW(), NOW()),
(13, '角色数据', 'RES_DATA_ROLE', 'DATA', '/api/role/*', '角色数据访问', 1, NOW(), NOW()),
(14, '权限数据', 'RES_DATA_PERMISSION', 'DATA', '/api/permission/*', '权限数据访问', 1, NOW(), NOW()),
(15, '审计日志数据', 'RES_DATA_AUDIT', 'DATA', '/api/audit/*', '审计日志数据访问', 1, NOW(), NOW());

-- ============================================
-- 5. 用户角色关联
-- ============================================
INSERT IGNORE INTO `user_role` (`user_id`, `role_id`, `create_time`) VALUES
(1, 1, NOW()),  -- admin -> 超级管理员
(2, 3, NOW()),  -- manager -> 部门经理
(3, 4, NOW()),  -- user1 -> 普通用户
(4, 4, NOW()),  -- user2 -> 普通用户
(5, 5, NOW()),  -- guest -> 访客
(7, 4, NOW());  -- test_user -> 普通用户

-- ============================================
-- 6. 角色权限关联
-- ============================================
-- 超级管理员：拥有所有权限
INSERT IGNORE INTO `role_permission` (`role_id`, `permission_id`, `create_time`) VALUES
(1, 1, NOW()), (1, 2, NOW()), (1, 3, NOW()), (1, 4, NOW()), (1, 5, NOW()),  -- 用户管理
(1, 6, NOW()), (1, 7, NOW()), (1, 8, NOW()), (1, 9, NOW()),  -- 角色管理
(1, 10, NOW()), (1, 11, NOW()), (1, 12, NOW()), (1, 13, NOW()),  -- 权限管理
(1, 14, NOW()), (1, 15, NOW()), (1, 16, NOW()), (1, 17, NOW()),  -- 资源管理
(1, 18, NOW()), (1, 19, NOW()), (1, 20, NOW()),  -- 分配权限
(1, 21, NOW()),  -- 审计日志
(1, 22, NOW()), (1, 23, NOW()), (1, 24, NOW()), (1, 25, NOW()), (1, 26, NOW());  -- 所有菜单

-- 系统管理员：拥有大部分管理权限
INSERT IGNORE INTO `role_permission` (`role_id`, `permission_id`, `create_time`) VALUES
(2, 1, NOW()), (2, 2, NOW()), (2, 3, NOW()), (2, 5, NOW()),  -- 用户管理（无删除）
(2, 6, NOW()), (2, 7, NOW()), (2, 8, NOW()),  -- 角色管理（无删除）
(2, 10, NOW()), (2, 11, NOW()), (2, 12, NOW()),  -- 权限管理（无删除）
(2, 14, NOW()), (2, 15, NOW()), (2, 16, NOW()),  -- 资源管理（无删除）
(2, 18, NOW()), (2, 19, NOW()), (2, 20, NOW()),  -- 分配权限
(2, 21, NOW()),  -- 审计日志
(2, 22, NOW()), (2, 23, NOW()), (2, 24, NOW()), (2, 25, NOW()), (2, 26, NOW());  -- 所有菜单

-- 部门经理：可以查看和管理本部门用户
INSERT IGNORE INTO `role_permission` (`role_id`, `permission_id`, `create_time`) VALUES
(3, 1, NOW()), (3, 2, NOW()), (3, 3, NOW()),  -- 用户管理（查看、创建、修改）
(3, 6, NOW()),  -- 角色查看
(3, 10, NOW()),  -- 权限查看
(3, 14, NOW()),  -- 资源查看
(3, 22, NOW()), (3, 23, NOW()), (3, 24, NOW()), (3, 25, NOW());  -- 部分菜单

-- 普通用户：只有查看权限
INSERT IGNORE INTO `role_permission` (`role_id`, `permission_id`, `create_time`) VALUES
(4, 1, NOW()),  -- 用户查看
(4, 6, NOW()),  -- 角色查看
(4, 10, NOW()),  -- 权限查看
(4, 14, NOW()),  -- 资源查看
(4, 22, NOW()), (4, 23, NOW()), (4, 24, NOW()), (4, 25, NOW());  -- 部分菜单

-- 访客：只有只读权限
INSERT IGNORE INTO `role_permission` (`role_id`, `permission_id`, `create_time`) VALUES
(5, 1, NOW()),  -- 用户查看
(5, 6, NOW()),  -- 角色查看
(5, 22, NOW()), (5, 23, NOW());  -- 部分菜单

-- 测试角色：用于测试
INSERT IGNORE INTO `role_permission` (`role_id`, `permission_id`, `create_time`) VALUES
(6, 1, NOW()), (6, 2, NOW()), (6, 6, NOW()), (6, 22, NOW());

-- ============================================
-- 7. 权限资源关联
-- ============================================
-- 用户管理权限对应的资源
INSERT IGNORE INTO `permission_resource` (`permission_id`, `resource_id`, `create_time`) VALUES
(1, 1, NOW()), (1, 12, NOW()),  -- 用户查看 -> 用户菜单 + 用户数据
(2, 6, NOW()), (2, 12, NOW()),  -- 用户创建 -> 创建按钮 + 用户数据
(3, 7, NOW()), (3, 12, NOW()),  -- 用户修改 -> 编辑按钮 + 用户数据
(4, 8, NOW()), (4, 12, NOW()),  -- 用户删除 -> 删除按钮 + 用户数据
(22, 1, NOW());  -- 用户管理菜单 -> 用户菜单

-- 角色管理权限对应的资源
INSERT IGNORE INTO `permission_resource` (`permission_id`, `resource_id`, `create_time`) VALUES
(6, 2, NOW()), (6, 13, NOW()),  -- 角色查看 -> 角色菜单 + 角色数据
(7, 9, NOW()), (7, 13, NOW()),  -- 角色创建 -> 创建按钮 + 角色数据
(8, 10, NOW()), (8, 13, NOW()),  -- 角色修改 -> 编辑按钮 + 角色数据
(9, 11, NOW()), (9, 13, NOW()),  -- 角色删除 -> 删除按钮 + 角色数据
(23, 2, NOW()); -- 角色管理菜单 -> 角色菜单

-- 权限管理权限对应的资源
INSERT IGNORE INTO `permission_resource` (`permission_id`, `resource_id`, `create_time`) VALUES
(10, 3, NOW()), (10, 14, NOW()),  -- 权限查看 -> 权限菜单 + 权限数据
(24, 3, NOW()); -- 权限管理菜单 -> 权限菜单

-- 资源管理权限对应的资源
INSERT IGNORE INTO `permission_resource` (`permission_id`, `resource_id`, `create_time`) VALUES
(14, 4, NOW()), (14, 14, NOW()),  -- 资源查看 -> 资源菜单 + 资源数据
(25, 4, NOW());  -- 资源管理菜单 -> 资源菜单

-- 审计日志权限对应的资源
INSERT IGNORE INTO `permission_resource` (`permission_id`, `resource_id`, `create_time`) VALUES
(21, 5, NOW()), (21, 15, NOW()),  -- 审计日志查看 -> 审计菜单 + 审计数据
(26, 5, NOW());  -- 审计日志菜单 -> 审计菜单

-- ============================================
-- 8. 审计日志数据（示例）
-- ============================================
INSERT IGNORE INTO `audit_log` (`user_id`, `username`, `operation`, `target_type`, `target_id`, `description`, `result`, `create_time`) VALUES
(1, 'admin', 'CREATE', 'USER', '2', '创建用户：manager', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 7 DAY)),
(1, 'admin', 'CREATE', 'ROLE', '2', '创建角色：系统管理员', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 6 DAY)),
(1, 'admin', 'ASSIGN', 'USER', '2', '为用户manager分配角色：部门经理', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 5 DAY)),
(1, 'admin', 'CREATE', 'USER', '3', '创建用户：user1', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(1, 'admin', 'ASSIGN', 'USER', '3', '为用户user1分配角色：普通用户', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 4 DAY)),
(2, 'manager', 'CREATE', 'USER', '4', '创建用户：user2', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 'manager', 'ASSIGN', 'USER', '4', '为用户user2分配角色：普通用户', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 3 DAY)),
(1, 'admin', 'UPDATE', 'USER', '6', '禁用用户：disabled_user', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, 'admin', 'QUERY', 'USER', NULL, '查询用户列表', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(3, 'user1', 'QUERY', 'ROLE', NULL, '查询角色列表', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 12 HOUR)),
(1, 'admin', 'CREATE', 'PERMISSION', '22', '创建权限：用户管理菜单', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
(1, 'admin', 'ASSIGN', 'ROLE', '4', '为角色普通用户分配权限：用户查看', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 3 HOUR));

-- ============================================
-- 数据插入完成提示
-- ============================================
SELECT '测试数据插入完成！' AS message;
SELECT 
    (SELECT COUNT(*) FROM `user`) AS user_count,
    (SELECT COUNT(*) FROM `role`) AS role_count,
    (SELECT COUNT(*) FROM `permission`) AS permission_count,
    (SELECT COUNT(*) FROM `resource`) AS resource_count,
    (SELECT COUNT(*) FROM `user_role`) AS user_role_count,
    (SELECT COUNT(*) FROM `role_permission`) AS role_permission_count,
    (SELECT COUNT(*) FROM `permission_resource`) AS permission_resource_count,
    (SELECT COUNT(*) FROM `audit_log`) AS audit_log_count;

