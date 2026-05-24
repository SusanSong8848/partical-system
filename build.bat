@echo off
setlocal

set OUT_DIR=out
set SRC_DIR=src\main\java

echo [1/3] Cleaning up old build...
if exist "%OUT_DIR%" rmdir /s /q "%OUT_DIR%"
mkdir "%OUT_DIR%"

echo [2/3] Compiling Java source files...
:: Find all .java files and save to sources.txt
dir /s /B "%SRC_DIR%\*.java" > sources.txt
:: Compile all files at once into the output directory
javac -d "%OUT_DIR%" @sources.txt
if %errorlevel% neq 0 (
    echo Compilation failed!
    del sources.txt
    pause
    exit /b %errorlevel%
)
del sources.txt

echo [3/3] Running Particle System...
java -cp "%OUT_DIR%" com.susan.particle.Main

pause
