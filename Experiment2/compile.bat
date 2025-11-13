@echo off
echo ========================================
echo Compiling Permission Management System
echo ========================================
echo.

REM Create output directory
if not exist "out" mkdir out

REM Compile Java files
echo Compiling Java files...
javac -encoding UTF-8 -cp "lib/mysql-connector-j-9.4.0.jar" -d out src/main/java/entity/*.java src/main/java/util/*.java src/main/java/dao/*.java src/main/java/service/*.java src/main/java/controller/*.java src/main/java/Main.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Compilation successful!
    echo ========================================
    echo.
    echo Run the program with: run.bat
) else (
    echo.
    echo ========================================
    echo Compilation failed! Please check error messages.
    echo ========================================
)

pause
