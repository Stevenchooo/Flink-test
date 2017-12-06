use mysql;
DELETE FROM USER WHERE USER='tccserverdb';
DELETE FROM DB WHERE USER='tccserverdb';
FLUSH PRIVILEGES;

GRANT ALL ON tccserverdb.* TO 'tccserverdb'@'%' IDENTIFIED BY 'xX,$dl12';
GRANT ALL ON tccserverdb.* TO 'tccserverdb'@'localhost' IDENTIFIED BY 'xX,$dl12';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccserverdb.*  TO 'tccserverdb'@'%' IDENTIFIED BY 'xX,$dl12';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccserverdb.*  TO 'tccserverdb'@'localhost' IDENTIFIED BY 'xX,$dl12';
grant select on mysql.proc to 'tccserverdb'@'%' IDENTIFIED BY 'xX,$dl12';
quit