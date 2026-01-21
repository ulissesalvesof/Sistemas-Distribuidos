@echo off
echo ========================================
echo   COMPILANDO PROJETO RMI - TELEFONIA
echo ========================================
echo.

REM Baixar Gson se necessário
if not exist "lib\gson-2.10.1.jar" (
    echo [1/5] Baixando biblioteca Gson...
    mkdir lib 2>nul
    powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar' -OutFile 'lib\gson-2.10.1.jar'"
    echo Gson baixado com sucesso!
    echo.
)

REM Limpar arquivos antigos
echo [2/5] Limpando arquivos antigos...
if exist model\*.class del /Q model\*.class > nul 2>&1
if exist protocol\*.class del /Q protocol\*.class > nul 2>&1
if exist server\*.class del /Q server\*.class > nul 2>&1
if exist client\*.class del /Q client\*.class > nul 2>&1

REM Ir para diretório pai
cd ..

REM Compilar com Gson no classpath
echo [3/5] Compilando modelo de dados...
javac -cp ".;tb2\lib\gson-2.10.1.jar" tb2\model\*.java
if %errorlevel% neq 0 goto erro

echo [4/5] Compilando protocolo...
javac -cp ".;tb2\lib\gson-2.10.1.jar" tb2\protocol\*.java
if %errorlevel% neq 0 goto erro

echo [5/5] Compilando servidor e cliente...
javac -cp ".;tb2\lib\gson-2.10.1.jar" tb2\server\*.java tb2\client\*.java
if %errorlevel% neq 0 goto erro

echo.
echo ========================================
echo   COMPILACAO CONCLUIDA COM SUCESSO!
echo ========================================
echo.
echo Arquivos gerados:
dir /s /b tb2\*.class 2>nul | find /c ".class"
echo arquivos .class criados
echo.

cd tb2
pause
exit /b 0

:erro
echo.
echo ========================================
echo   ERRO NA COMPILACAO!
echo ========================================
cd tb2
pause
exit /b 1
