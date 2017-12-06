use mysql;
DELETE FROM USER WHERE USER='tccdb53test';
DELETE FROM DB WHERE USER='tccdb53test';
FLUSH PRIVILEGES;

GRANT ALL ON tccdb53test.* TO 'tccdb53test'@'%' IDENTIFIED BY '397WdiuQ';
GRANT ALL ON tccdb53test.* TO 'tccdb53test'@'localhost' IDENTIFIED BY '397WdiuQ';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccdb53test.*  TO 'tccdb53test'@'%' IDENTIFIED BY '397WdiuQ';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccdb53test.*  TO 'tccdb53test'@'localhost' IDENTIFIED BY '397WdiuQ';
grant select on mysql.proc to 'tccdb53test'@'%' IDENTIFIED BY '397WdiuQ';
quit