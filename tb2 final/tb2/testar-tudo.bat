@echo off
cls
echo ========================================
echo    TESTE AUTOMATIZADO DO SISTEMA RMI
echo ========================================
echo.
echo Este script ira:
echo 1. Compilar o projeto
echo 2. Iniciar o servidor
echo 3. Executar testes automaticos
echo.
pause

REM Compila o projeto
echo.
echo [PASSO 1/3] Compilando projeto...
call compilar.bat
if %errorlevel% neq 0 (
    echo ERRO na compilacao!
    pause
    exit /b 1
)

echo.
echo [PASSO 2/3] Iniciando servidor em segundo plano...
start /B java tb2.server.Server > servidor.log 2>&1

REM Aguarda o servidor inicializar
timeout /t 3 /nobreak > nul

echo.
echo [PASSO 3/3] Servidor iniciado! Voce pode agora:
echo.
echo - Abrir outro terminal e executar: iniciar-cliente.bat
echo - Ou conectar manualmente com: java tb2.client.Client
echo.
echo Pressione qualquer tecla para ver o log do servidor...
pause > nul

type servidor.log
echo.
echo ========================================
echo Para encerrar o servidor, feche esta janela
echo ou pressione Ctrl+C
echo ========================================
pause
