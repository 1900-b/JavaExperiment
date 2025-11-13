
package util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * 日志工具类
 * 配置Java Logging API，将日志输出到文件
 */
public class LoggerUtil {
    private static final String LOG_FILE = "logs/authority_management.log";
    private static Logger rootLogger = Logger.getLogger("");
    
    static {
        try {
            // 创建logs目录（如果不存在）
            java.io.File logDir = new java.io.File("logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            // 配置FileHandler，追加模式
            FileHandler fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            
            // 配置控制台Handler
            java.util.logging.ConsoleHandler consoleHandler = new java.util.logging.ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            consoleHandler.setLevel(Level.INFO);
            
            // 移除默认的Handler，添加自定义Handler
            java.util.logging.Handler[] handlers = rootLogger.getHandlers();
            for (java.util.logging.Handler handler : handlers) {
                rootLogger.removeHandler(handler);
            }
            rootLogger.addHandler(fileHandler);
            rootLogger.addHandler(consoleHandler);
            rootLogger.setLevel(Level.ALL);
            
            rootLogger.info("日志系统初始化成功");
        } catch (IOException e) {
            System.err.println("日志系统初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 获取Logger实例
     * @param className 类名
     * @return Logger实例
     */
    public static Logger getLogger(String className) {
        return Logger.getLogger(className);
    }
}

