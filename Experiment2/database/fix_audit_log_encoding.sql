-- 修复审计日志表中的乱码数据
-- 执行前请确保MySQL客户端字符集为UTF-8: SET NAMES utf8mb4;

USE authority_management_system;

-- 设置会话字符集为utf8mb4，确保中文字符正确存储
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;
SET character_set_results=utf8mb4;
SET character_set_client=utf8mb4;

-- 修复乱码的审计日志记录
-- 根据实际乱码情况，更新description字段
-- 注意：使用COLLATE统一排序规则，避免字符集冲突
-- 优先使用operation、target_type、target_id匹配，避免依赖可能乱码的description字段

-- 修复"创建权限：用户管理菜单"的乱码
-- 方法1：根据operation、target_type、target_id精确匹配（推荐）
UPDATE audit_log 
SET description = '创建权限：用户管理菜单' COLLATE utf8mb4_unicode_ci
WHERE operation = 'CREATE' 
  AND target_type = 'PERMISSION' 
  AND target_id = '22'
  AND description NOT LIKE '%创建权限%' COLLATE utf8mb4_unicode_ci;

-- 方法2：如果方法1不够精确，可以使用二进制匹配乱码字符串
-- UPDATE audit_log 
-- SET description = '创建权限：用户管理菜单' COLLATE utf8mb4_unicode_ci
-- WHERE operation = 'CREATE' 
--   AND target_type = 'PERMISSION' 
--   AND target_id = '22'
--   AND HEX(description) LIKE HEX('%鍒涘缓%');

-- 修复"为角色普通用户分配权限：用户查看"的乱码
-- 方法1：根据operation、target_type、target_id精确匹配（推荐）
UPDATE audit_log 
SET description = '为角色普通用户分配权限：用户查看' COLLATE utf8mb4_unicode_ci
WHERE operation = 'ASSIGN' 
  AND target_type = 'ROLE' 
  AND target_id = '4'
  AND description NOT LIKE '%为角色%' COLLATE utf8mb4_unicode_ci;

-- 方法2：如果方法1不够精确，可以使用二进制匹配乱码字符串
-- UPDATE audit_log 
-- SET description = '为角色普通用户分配权限：用户查看' COLLATE utf8mb4_unicode_ci
-- WHERE operation = 'ASSIGN' 
--   AND target_type = 'ROLE' 
--   AND target_id = '4'
--   AND HEX(description) LIKE HEX('%涓鸿?鑹%');

-- 修复"创建用户：user2"的乱码（鍒涘缓鐢ㄦ埛锛歶ser2）
-- 使用二进制排序规则（utf8mb4_bin）来匹配乱码字符串，避免排序规则冲突
UPDATE audit_log 
SET description = '创建用户：user2' COLLATE utf8mb4_unicode_ci
WHERE operation = 'CREATE' 
  AND target_type = 'USER' 
  AND target_id = '4'
  AND BINARY description LIKE BINARY '%鍒涘缓%'
  AND description NOT LIKE '%创建用户%' COLLATE utf8mb4_unicode_ci;

-- 修复"为用户user2分配角色：普通用户"的乱码（涓虹敤鎴穟ser2鍒嗛厤瑙掕壊锛氭櫘閫氱敤鎴）
-- 注意：根据代码，ASSIGN操作的target_type应该是USER_ROLE，但查询结果显示为USER
-- 这里同时匹配两种可能的target_type
-- 使用二进制排序规则来匹配乱码字符串
UPDATE audit_log 
SET description = '为用户user2分配角色：普通用户' COLLATE utf8mb4_unicode_ci
WHERE operation = 'ASSIGN' 
  AND (target_type = 'USER' OR target_type = 'USER_ROLE')
  AND target_id = '4'
  AND BINARY description LIKE BINARY '%涓虹敤%'
  AND description NOT LIKE '%为用户%' COLLATE utf8mb4_unicode_ci;

-- 如果还有其他乱码记录，可以手动修复
-- 建议：删除所有乱码记录，重新通过Java程序创建，或者确保在UTF-8字符集下重新执行test_data.sql

-- 查看修复结果
SELECT id, user_id, username, operation, target_type, target_id, description, result, create_time 
FROM audit_log 
ORDER BY create_time DESC 
LIMIT 20;

