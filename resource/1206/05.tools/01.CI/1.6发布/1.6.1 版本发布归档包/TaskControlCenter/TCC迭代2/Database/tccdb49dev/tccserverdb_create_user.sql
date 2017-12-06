use mysql;
DELETE FROM USER WHERE USER='tccdb49dev';
DELETE FROM DB WHERE USER='tccdb49dev';
FLUSH PRIVILEGES;

GRANT ALL ON tccdb49dev.* TO 'tccdb49dev'@'%' IDENTIFIED BY 'zE8OMel2';
GRANT ALL ON tccdb49dev.* TO 'tccdb49dev'@'localhost' IDENTIFIED BY 'zE8OMel2';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccdb49dev.*  TO 'tccdb49dev'@'%' IDENTIFIED BY 'zE8OMel2';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccdb49dev.*  TO 'tccdb49dev'@'localhost' IDENTIFIED BY 'zE8OMel2';
grant select on mysql.proc to 'tccdb49dev'@'%' IDENTIFIED BY 'zE8OMel2';
quit