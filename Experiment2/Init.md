初始化步骤：
MySQL:
USE authority_management_system;
SOURCE C:/Users/1900/Desktop/JavaExperiment/Experiment2/database/init.sql;
SOURCE C:/Users/1900/Desktop/JavaExperiment/Experiment2/database/test_data.sql;
//可选：处理乱码
SOURCE C:/Users/1900/Desktop/JavaExperiment/Experiment2/database/fix_audit_log_encoding.sql;
SOURCE C:/Users/1900/Desktop/JavaExperiment/Experiment2/database/fix_test_data_encoding.sql;

Terminal:
.\compile.bat
.\run.bat
