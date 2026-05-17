@echo off
title MechDIY Backend
set JAVA_HOME=C:\Program Files\Java\jdk-26
set PATH=C:\apache-maven-3.9.6\bin;%PATH%
cd /d "%~dp0backend"
echo ===========================================
echo   MechDIY Backend - Iniciando...
echo ===========================================
mvn spring-boot:run
pause
