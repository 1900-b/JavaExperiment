-- 修复 test_data.sql 初始化时插入的乱码审计日志数据
-- 问题原因：test_data.sql 文件可能不是 UTF-8 编码，导致中文字符插入时变成乱码
-- 解决方案：删除乱码记录，重新插入正确的数据

USE authority_management_system;

-- 设置会话字符集为utf8mb4，确保中文字符正确存储
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;
SET character_set_results=utf8mb4;
SET character_set_client=utf8mb4;

-- ============================================
-- 第一步：删除可能乱码的审计日志记录
-- ============================================
-- 删除 test_data.sql 中插入的审计日志记录（基于 operation、target_type、target_id 匹配）
-- 这样可以避免依赖可能乱码的 description 字段

-- 删除"创建用户：user2"的记录（ID 115, 127）
DELETE FROM audit_log 
WHERE operation = 'CREATE' 
  AND target_type = 'USER' 
  AND target_id = '4'
  AND (user_id = 2 OR username = 'manager')
  AND create_time >= DATE_SUB(NOW(), INTERVAL 4 DAY);

-- 删除"为用户user2分配角色：普通用户"的记录（ID 116, 128）
DELETE FROM audit_log 
WHERE operation = 'ASSIGN' 
  AND (target_type = 'USER' OR target_type = 'USER_ROLE')
  AND target_id = '4'
  AND (user_id = 2 OR username = 'manager')
  AND create_time >= DATE_SUB(NOW(), INTERVAL 4 DAY);

-- 删除"创建权限：用户管理菜单"的记录（如果也是乱码）
DELETE FROM audit_log 
WHERE operation = 'CREATE' 
  AND target_type = 'PERMISSION' 
  AND target_id = '22'
  AND description NOT LIKE '%创建权限%' COLLATE utf8mb4_unicode_ci;

-- 删除"为角色普通用户分配权限：用户查看"的记录（如果也是乱码）
DELETE FROM audit_log 
WHERE operation = 'ASSIGN' 
  AND target_type = 'ROLE' 
  AND target_id = '4'
  AND description NOT LIKE '%为角色%' COLLATE utf8mb4_unicode_ci;

-- ============================================
-- 第二步：重新插入正确的审计日志数据
-- ============================================
-- 使用正确的 UTF-8 编码重新插入数据
-- 注意：使用 INSERT 而不是 INSERT IGNORE，因为我们已经删除了旧记录

INSERT INTO `audit_log` (`user_id`, `username`, `operation`, `target_type`, `target_id`, `description`, `result`, `create_time`) VALUES
-- 重新插入"创建用户：user2"
(2, 'manager', 'CREATE', 'USER', '4', '创建用户：user2', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 3 DAY)),
-- 重新插入"为用户user2分配角色：普通用户"
-- 注意：根据代码逻辑，ASSIGN操作时target_type应该是USER_ROLE，但这里保持与test_data.sql一致
(2, 'manager', 'ASSIGN', 'USER', '4', '为用户user2分配角色：普通用户', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 3 DAY)),
-- 重新插入"创建权限：用户管理菜单"（如果被删除了）
(1, 'admin', 'CREATE', 'PERMISSION', '22', '创建权限：用户管理菜单', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 6 HOUR)),
-- 重新插入"为角色普通用户分配权限：用户查看"（如果被删除了）
(1, 'admin', 'ASSIGN', 'ROLE', '4', '为角色普通用户分配权限：用户查看', 'SUCCESS', DATE_SUB(NOW(), INTERVAL 3 HOUR));

-- ============================================
-- 第三步：验证修复结果
-- ============================================
SELECT '修复完成！请检查以下记录是否正确：' AS message;

SELECT id, user_id, username, operation, target_type, target_id, description, result, create_time 
FROM audit_log 
WHERE (operation = 'CREATE' AND target_type = 'USER' AND target_id = '4')
   OR (operation = 'ASSIGN' AND target_id = '4' AND (target_type = 'USER' OR target_type = 'USER_ROLE'))
   OR (operation = 'CREATE' AND target_type = 'PERMISSION' AND target_id = '22')
   OR (operation = 'ASSIGN' AND target_type = 'ROLE' AND target_id = '4')
ORDER BY create_time DESC;

-- ============================================
-- 建议
-- ============================================
-- 1. 确保 test_data.sql 文件以 UTF-8 编码保存
-- 2. 执行 test_data.sql 前，确保 MySQL 客户端字符集为 UTF-8
-- 3. 在 MySQL 命令行中执行: SET NAMES utf8mb4; 然后再执行 SOURCE 命令
-- 4. 或者使用支持 UTF-8 的数据库客户端工具执行 SQL 脚本

