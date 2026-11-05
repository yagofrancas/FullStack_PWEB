@echo off
echo ========================================
echo  Iniciando Yago_42386 (Spring Boot)
echo  Modo offline - sem internet necessaria
echo ========================================

REM Monta o classpath com todos os JARs do .m2
set M2=%USERPROFILE%\.m2\repository
set CP=target\classes

for /R "%M2%" %%f in (*.jar) do (
    echo %%f | findstr /i "sources javadoc slf4j-api-1.7 slf4j-nop" >nul
    if errorlevel 1 (
        set CP=!CP!;%%f
    )
)

echo.
echo Classpath montado. Iniciando...
java -cp "%CP%" com.ProjetoP2.Yago_42386.Yago42386Application

pause

