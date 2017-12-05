@echo off


setlocal enabledelayedexpansion

set BINPATH=%cd%
set WEBPATH=%BINPATH%/../
set LIBPATH=%WEBPATH%/lib
set CP=./;%WEBPATH%/classes

for %%j in (%LIBPATH%\*.jar) do (
    set CP=!CP!;%%j
)
@echo %CP% 

@java -classpath %cp% com.huawei.util.SecureUtil %1 %2