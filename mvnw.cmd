@ECHO OFF
SETLOCAL

SET MVNW_REPOURL=https://repo.maven.apache.org/maven2
SET WRAPPER_DIR=%~dp0.mvn\wrapper
SET WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar
SET PROPS=%WRAPPER_DIR%\maven-wrapper.properties

FOR /F "usebackq tokens=1,* delims==" %%A IN ("%PROPS%") DO (
  IF "%%A"=="distributionUrl" SET DIST_URL=%%B
)

IF "%DIST_URL%"=="" (
  ECHO distributionUrl is missing in %PROPS%
  EXIT /B 1
)

IF NOT EXIST "%WRAPPER_JAR%" (
  IF NOT EXIST "%WRAPPER_DIR%" mkdir "%WRAPPER_DIR%"
  ECHO Downloading maven-wrapper.jar...
  powershell -NoProfile -ExecutionPolicy Bypass -Command ^
    "$u='%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar';" ^
    "Invoke-WebRequest -UseBasicParsing -Uri $u -OutFile '%WRAPPER_JAR%';"
)

SET JAVA_EXE=java
IF NOT "%JAVA_HOME%"=="" SET JAVA_EXE=%JAVA_HOME%\bin\java

"%JAVA_EXE%" -Dmaven.multiModuleProjectDirectory=%~dp0 ^
  -classpath "%WRAPPER_JAR%" ^
  org.apache.maven.wrapper.MavenWrapperMain %*

ENDLOCAL
