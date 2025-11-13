@echo off
REM Set console encoding to UTF-8 (code page 65001) to match Java program encoding
chcp 65001 >nul
echo ========================================
echo Running Permission Management System
echo ========================================
echo.

REM Check if compiled
if not exist "out" (
    echo Error: Output directory not found!
    echo Please run compile.bat first.
    pause
    exit /b 1
)

REM Run program with UTF-8 encoding to ensure correct Chinese character storage in database
REM Console is set to UTF-8 (chcp 65001), and JVM uses UTF-8 for processing
echo Starting Permission Management System...
echo.
java -Dfile.encoding=UTF-8 -cp "out;lib/mysql-connector-j-9.4.0.jar" Main

pause
