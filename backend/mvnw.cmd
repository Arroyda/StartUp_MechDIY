@REM ----------------------------------------------------------------------------
@REM Maven wrapper script for Windows
@REM ----------------------------------------------------------------------------
@IF "%__MVNW_ARG0_NAME__%"=="" (SET "BASE_DIR=%~dp0")

@SET MAVEN_PROJECTBASEDIR=%BASE_DIR%
@SET WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain
@SET WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

@FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") DO (
    @IF "%%A"=="wrapperUrl" SET WRAPPER_URL=%%B
)

@SET JAVA_HOME_CANDIDATES=^
    "%JAVA_HOME%" ^
    "%ProgramFiles%\Eclipse Adoptium\jdk-17*" ^
    "%ProgramFiles%\Java\jdk-17*" ^
    "%ProgramFiles%\Microsoft\jdk-17*"

@IF NOT "%JAVA_HOME%"=="" GOTO init

@FOR %%D IN (%JAVA_HOME_CANDIDATES%) DO (
    @IF EXIST "%%~D\bin\java.exe" (
        @SET "JAVA_HOME=%%~D"
        @GOTO init
    )
)

:init
@SET JAVA_EXE="%JAVA_HOME%\bin\java.exe"

@IF EXIST %JAVA_EXE% GOTO downloadWrapper

@ECHO Error: JAVA_HOME is not set or Java not found.
@ECHO Please install Java 17+ and set JAVA_HOME.
@EXIT /B 1

:downloadWrapper
@IF EXIST %WRAPPER_JAR% GOTO execute

@ECHO Downloading Maven Wrapper...
%JAVA_EXE% -Xms4m -Xmx4m ^
  -classpath "%MAVEN_PROJECTBASEDIR%\.mvn\wrapper" ^
  org.apache.maven.wrapper.Downloader ^
  "%WRAPPER_URL%" ^
  %WRAPPER_JAR%

@IF EXIST %WRAPPER_JAR% GOTO execute
@ECHO Trying curl fallback...
curl -L -o %WRAPPER_JAR% "%WRAPPER_URL%"

:execute
%JAVA_EXE% ^
  -classpath %WRAPPER_JAR% ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
