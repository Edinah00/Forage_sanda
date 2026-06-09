@echo off
REM Deploiement Forage avec Maven

REM Forcer console UTF-8
chcp 65001 > nul

REM Config
set APP_NAME=forage
set TOMCAT_WEBAPPS=D:\apache-tomcat-10.1.52-windows-x64\apache-tomcat-10.1.52\webapps

echo [1/3] Nettoyage et Compilation avec Maven...
REM mvn clean install compile et cree le fichier .war dans le dossier /target
call mvn clean install

if %ERRORLEVEL% NEQ 0 (
    echo Erreur lors de la compilation Maven. Verifiez votre pom.xml ou votre code.
    pause
    exit /b
)

echo [2/3] Deploiement vers Tomcat...
REM On copie le fichier genere par Maven vers Tomcat
REM Note: Maven nomme souvent le fichier forage-1.0-SNAPSHOT.war, on le renomme en forage.war
copy target\%APP_NAME%-1.0-SNAPSHOT.war "%TOMCAT_WEBAPPS%\%APP_NAME%.war" /y

echo [3/3] Nettoyage...
echo Deploiement termine ! Le projet est dans Tomcat sous le nom %APP_NAME%.war
echo Accedez a : http://localhost:8080/%APP_NAME%/test

pause