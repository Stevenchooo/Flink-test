use mysql;
DELETE FROM USER WHERE USER='tccserverdb';
DELETE FROM DB WHERE USER='tccserverdb';
FLUSH PRIVILEGES;

GRANT ALL ON tccserverdb.* TO 'tccserverdb'@'%' IDENTIFIED BY 'tccserverdb';
GRANT ALL ON tccserverdb.* TO 'tccserverdb'@'localhost' IDENTIFIED BY 'tccserverdb';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccserverdb.*  TO 'tccserverdb'@'%' IDENTIFIED BY 'tccserverdb';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccserverdb.*  TO 'tccserverdb'@'localhost' IDENTIFIED BY 'tccserverdb';
grant select on mysql.proc to 'tccserverdb'@'%' IDENTIFIED BY 'tccserverdb';
quit