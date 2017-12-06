use mysql;
DELETE FROM USER WHERE USER='tccdb53prod';
DELETE FROM DB WHERE USER='tccdb53prod';
FLUSH PRIVILEGES;

GRANT ALL ON tccdb53prod.* TO 'tccdb53prod'@'%' IDENTIFIED BY 'f3,sI84w';
GRANT ALL ON tccdb53prod.* TO 'tccdb53prod'@'localhost' IDENTIFIED BY 'f3,sI84w';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccdb53prod.*  TO 'tccdb53prod'@'%' IDENTIFIED BY 'f3,sI84w';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccdb53prod.*  TO 'tccdb53prod'@'localhost' IDENTIFIED BY 'f3,sI84w';
grant select on mysql.proc to 'tccdb53prod'@'%' IDENTIFIED BY 'f3,sI84w';
quit