@echo off
echo [INFO] Use maven jetty-plugin run the project.

%~d0
cd %~dp0
cd ..



set MAVEN_OPTS=%MAVEN_OPTS% -Xms256m -Xmx512m -XX:PermSize=128m -XX:MaxPermSize=256m 


set JAVA_REBEL_HOME=C:\vriche\java\javarebel-2.0
set MAVEN_OPTS=%MAVEN_OPTS%   -noverify  -javaagent:%JAVA_REBEL_HOME%\jrebel.jar -Drebel.dirs=C:\vriche\work\111\jeesite\src\main\webapp\WEB-INF\classes -Drebel.spring_plugin=true
rem set MAVEN_OPTS=%MAVEN_OPTS%   -noverify  -javaagent:%JAVA_REBEL_HOME%\jrebel.jar -Drebel.dirs=C:\vriche\work\111\jeesite\src\main\webapp\WEB-INF\classes

call mvn jetty:run

cd bin
pause
