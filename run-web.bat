@echo off
cd /d "%~dp0"
call mvn -q -DskipTests compile
java -cp "target/classes" com.waifu.WebAppServer
