@echo off
echo ========================================
echo   SERVIDOR REST API - PORTA 8080
echo ========================================
echo.

if not exist "servidor-java\servidor\ServidorAPI.class" (
    echo ERRO: Servidor nao compilado!
    echo Execute primeiro: compilar-servidor.bat
    pause
    exit /b 1
)

echo Iniciando servidor REST API...
echo Porta: 8080
echo Protocolo: HTTP REST
echo Formato: JSON
echo.
echo Pressione Ctrl+C para encerrar
echo ========================================
echo.

cd servidor-java
java -cp ".;..\lib\gson-2.10.1.jar" servidor.ServidorAPI
cd ..

pause
