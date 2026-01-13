@echo off
echo ========================================
echo   CLIENTE NODE.JS - REST API
echo ========================================
echo.

REM Verificar se Node.js está instalado
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Node.js nao encontrado!
    echo Instale Node.js em https://nodejs.org/
    pause
    exit /b 1
)

REM Instalar dependências se necessário
if not exist "cliente-nodejs\node_modules" (
    echo Instalando dependencias Node.js...
    cd cliente-nodejs
    npm install
    cd ..
    echo.
)

echo Iniciando cliente Node.js...
echo Conectando em: http://localhost:8080
echo.
echo ========================================
echo.

cd cliente-nodejs
node cliente.js
cd ..

pause
