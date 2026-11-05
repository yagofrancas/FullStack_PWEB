@echo off
chcp 65001 > nul
echo ========================================================
echo   Iniciando Projeto P2 - Yago_42386 (Spring Boot + JPA)
echo ========================================================
echo.

REM Tentativa 1: Executar via Maven
echo [1/2] Tentando executar via 'mvn spring-boot:run'...
call mvn spring-boot:run
if %ERRORLEVEL% equ 0 goto fim

echo.
echo ========================================================
echo [2/2] O comando Maven retornou erro de plugins locais.
echo       Iniciando aplicacao diretamente pelo Java...
echo ========================================================
echo.

setlocal enabledelayedexpansion
set "CP=target\classes"
for /R "%USERPROFILE%\.m2\repository" %%F in (*.jar) do (
    echo "%%~nxF" | findstr /i "sources javadoc slf4j-api-1.7 slf4j-nop slf4j-simple" >nul
    if errorlevel 1 (
        set "CP=!CP!;%%F"
    )
)

java -cp "%CP%" com.ProjetoP2.Yago_42386.Yago42386Application

:fim
pause
