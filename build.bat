
@set JAVA_HOME=C:\Program Files\Java\jdk1.8.0_211

@path=%JAVA_HOME%\bin;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;

cmd
@echo gradlew assembleRelease
@echo gradlew build
@echo cd desktop\build\distributions
cmd
gradlew build
cmd