@echo off
echo ========================================
echo   CLIENTE PYTHON - REST API
echo ========================================
echo.

REM Verificar se Python está instalado
python --version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERRO: Python nao encontrado!
    echo Instale Python 3.x em https://www.python.org/
    pause
    exit /b 1
)

REM Instalar dependências se necessário
if not exist "cliente-python\venv" (
    echo Instalando dependencias Python...
    cd cliente-python
    pip install -r requirements.txt
    cd ..
    echo.
)

echo Iniciando cliente Python...
echo Conectando em: http://localhost:8080
echo.
echo ========================================
echo.

cd cliente-python
python cliente.py
cd ..

pause
