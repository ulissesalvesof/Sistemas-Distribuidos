@echo off
echo ========================================
echo   SERVIDOR RMI MANUAL - TELEFONIA
echo ========================================
echo.

REM Verificar se foi compilado
if not exist server\ServidorRMI.class (
    echo ERRO: Projeto nao compilado!
    echo Execute primeiro o arquivo compilar.bat
    pause
    exit /b 1
)

echo Iniciando servidor RMI manual...
echo Porta: 5000
echo Protocolo: doOperation/getRequest/sendReply
echo Serializacao: JSON (Gson)
echo.
echo Pressione Ctrl+C para encerrar o servidor
echo ========================================
echo.

cd ..
java -cp ".;tb2;tb2\lib\gson-2.10.1.jar" tb2.server.ServidorRMI
cd tb2

pause
