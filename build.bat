
@set JAVA_HOME=C:\Users\zhang\Desktop\123\jdk1.8.0_211

@path=C:\Users\zhang\Desktop\123\gradle-5.3.1\bin;%JAVA_HOME%\bin;C:\Windows\system32;C:\Windows;C:\Windows\System32\Wbem;C:\Windows\System32\WindowsPowerShell\v1.0\;

cmd
@echo gradlew assembleRelease
@echo gradlew build
@echo cd desktop\build\distributions
cmd
gradlew build
cmd