@echo off
@echo DROP DATABASE IF EXISTS pms;>mysqlcmd
@echo create database pms;>>mysqlcmd
@echo use pms;>>mysqlcmd
@echo source pms.sql;>>mysqlcmd
@echo exit>>mysqlcmd
@mysql -uroot -proot <mysqlcmd
rem @mysql -h127.0.0.1 -uroot -proot <mysqlcmd 
@del mysqlcmd
@echo finish!
pause; 