@echo off
setlocal EnableExtensions
cd /d "%~dp0"
set "ROOT=%~dp0"
set "PORT=8080"
set "JAVA="

rem Busca Java ya instalado.
where java.exe >nul 2>nul
if not errorlevel 1 for /f "delims=" %%J in ('where java.exe') do if not defined JAVA set "JAVA=%%J"

rem JAVA_HOME
if not defined JAVA if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set "JAVA=%JAVA_HOME%\bin\java.exe"

rem Java incluido con Red Hat Java de VS Code
if not defined JAVA for /d %%D in ("%USERPROFILE%\.vscode\extensions\redhat.java-*\jre\*\bin") do if exist "%%~fD\java.exe" set "JAVA=%%~fD\java.exe"

if not defined JAVA goto nojava
if not exist "%ROOT%app\classes\com\waifu\WebAppServer.class" goto noclasses
if not exist "%ROOT%web\index.html" goto noweb

start "Waifu Nexus Server" /b "%JAVA%" -cp "%ROOT%app\classes" com.waifu.WebAppServer %PORT% "%ROOT%web"
timeout /t 2 /nobreak >nul
start "" "http://localhost:%PORT%"
exit /b 0

:nojava
echo.
echo =====================================================
echo       WAIFU NEXUS - JAVA NO ENCONTRADO
echo =====================================================
echo.
echo No se encontro Java en esta PC.
echo.
echo Esta version NO descarga ni crea runtimes temporales.
echo Instala Java 21 o coloca un runtime en portable\runtime.
echo.
pause
exit /b 1

:noclasses
echo ERROR: faltan las clases compiladas del servidor web.
pause
exit /b 1

:noweb
echo ERROR: faltan los archivos de la aplicacion web.
pause
exit /b 1
