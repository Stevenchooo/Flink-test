use mysql;
DELETE FROM USER WHERE USER='tccdb45test';
DELETE FROM DB WHERE USER='tccdb45test';
FLUSH PRIVILEGES;

GRANT ALL ON tccdb45test.* TO 'tccdb45test'@'%' IDENTIFIED BY '5WGs25eE';
GRANT ALL ON tccdb45test.* TO 'tccdb45test'@'localhost' IDENTIFIED BY '5WGs25eE';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccdb45test.*  TO 'tccdb45test'@'%' IDENTIFIED BY '5WGs25eE';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccdb45test.*  TO 'tccdb45test'@'localhost' IDENTIFIED BY '5WGs25eE';
grant select on mysql.proc to 'tccdb45test'@'%' IDENTIFIED BY '5WGs25eE';
quit