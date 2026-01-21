@echo off
echo ========================================
echo   CLIENTE RMI MANUAL - TELEFONIA
echo ========================================
echo.

REM Verificar se foi compilado
if not exist client\ClienteRMI.class (
    echo ERRO: Projeto nao compilado!
    echo Execute primeiro o arquivo compilar.bat
    pause
    exit /b 1
)

echo Conectando ao servidor localhost:5000...
echo Protocolo: doOperation/getRequest/sendReply
echo Serializacao: JSON (Gson)
echo.
echo ========================================
echo.

cd ..
java -cp ".;tb2;tb2\lib\gson-2.10.1.jar" tb2.client.ClienteRMI
cd tb2

pause
