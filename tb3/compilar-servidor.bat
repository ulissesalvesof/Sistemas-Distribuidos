@echo off
echo ========================================
echo   COMPILANDO SERVIDOR JAVA REST API
echo ========================================
echo.

REM Baixar Gson se necessário
if not exist "lib\gson-2.10.1.jar" (
    echo [1/2] Baixando biblioteca Gson...
    mkdir lib 2>nul
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar' -OutFile 'lib\gson-2.10.1.jar'"
    echo Gson baixado com sucesso!
    echo.
)

echo [2/2] Compilando servidor Java...
cd servidor-java
javac -cp ".;..\lib\gson-2.10.1.jar" -d . *.java

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo   ERRO NA COMPILACAO!
    echo ========================================
    cd ..
    pause
    exit /b 1
)

cd ..

echo.
echo ========================================
echo   COMPILACAO CONCLUIDA COM SUCESSO!
echo ========================================
echo.
echo Arquivos .class gerados em servidor-java/servidor/
echo.

pause
