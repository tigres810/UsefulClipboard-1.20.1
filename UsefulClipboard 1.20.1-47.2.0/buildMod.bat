@echo off
SET var=%cd%
SET mcversion="1.19.2"
SET modname="UsefulClipboard"
SET modversion="0.0.24"

:choice
color 0A
echo.
set /P c=Compilar el mod [S/N]?
if /I "%c%" EQU "S" goto :continue
if /I "%c%" EQU "N" goto :skip3
goto :choice

:continue
color 0F
echo.
call gradlew build
:choice1
color 0C
echo.
set /P c=Cambiar nombre al mod [S/N]?
if /I "%c%" EQU "S" goto :continue1
if /I "%c%" EQU "N" goto :skip
goto :choice1

:continue1
color 0B
echo.
set /p "modname=Nuevo nombre del mod: "
:choice2
color 0B
echo.
set /P c=Cambiar version del mod [S/N]?
if /I "%c%" EQU "S" goto :continue2
if /I "%c%" EQU "N" goto :skip2
goto :choice2

:continue2
color 0D
echo.
set /p "modversion=Nueva version del mod: "
IF exist %var%\build\libs\modid-1.0.jar (
  ren "%var%\build\libs\modid-1.0.jar" "%modname%-%mcversion%-%modversion%.jar"
  cd %var%\build\libs\
  start .
 ) ELSE (
 echo "Archivo no encontrado."
 TIMEOUT /T 3 /NOBREAK
)
exit

:skip
goto :choice2

:skip2
IF exist %var%\build\libs\modid-1.0.jar (
  ren "%var%\build\libs\modid-1.0.jar" "%modname%-%mcversion%-%modversion%.jar"
  cd %var%\build\libs\
  start .
 ) ELSE (
 echo "Archivo no encontrado."
 TIMEOUT /T 3 /NOBREAK
)
exit

:skip3
:choice3
color 03
echo.
set /P c=Abrir carpeta del mod [S/N]?
if /I "%c%" EQU "S" goto :skip4
if /I "%c%" EQU "N" goto :quit
goto :choice3

:skip4
%SystemRoot%\explorer.exe "%var%\build\libs\"

:quit
exit