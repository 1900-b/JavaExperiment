package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * 数据库连接工具类
 * 使用单例模式管理数据库连接
 */
public class DatabaseUtil {
    private static final Logger logger = Logger.getLogger(DatabaseUtil.class.getName());
    
    // 数据库连接信息
    // 添加字符编码参数，确保正确处理中文字符
    // useUnicode=true: 使用Unicode编码
    // characterEncoding=UTF-8: 使用Java标准的UTF-8编码（MySQL JDBC驱动会自动映射到utf8mb4）
    // useSSL=false: 禁用SSL（本地开发环境）
    // serverTimezone=UTC: 设置服务器时区
    // allowPublicKeyRetrieval=true: 允许检索公钥（MySQL 8.0+）
    // useServerPrepStmts=true: 使用服务器端预处理语句，确保字符编码正确传递
    private static final String URL = "jdbc:mysql://localhost:3306/authority_management_system?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useServerPrepStmts=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "BJY1998918";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    /**
     * 获取数据库连接
     * @return Connection对象
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            // 设置会话字符集为utf8mb4，确保正确处理中文
            try (java.sql.Statement stmt = conn.createStatement()) {
                stmt.execute("SET NAMES 'utf8mb4' COLLATE 'utf8mb4_unicode_ci'");
                stmt.execute("SET CHARACTER SET utf8mb4");
                stmt.execute("SET character_set_connection=utf8mb4");
                stmt.execute("SET character_set_results=utf8mb4");
                stmt.execute("SET character_set_client=utf8mb4");
            }
            logger.info("数据库连接成功");
        } catch (ClassNotFoundException e) {
            logger.severe("MySQL驱动未找到: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            logger.severe("数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }
    
    /**
     * 关闭数据库连接
     * @param conn 数据库连接
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                logger.info("数据库连接已关闭");
            } catch (SQLException e) {
                logger.severe("关闭数据库连接失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

