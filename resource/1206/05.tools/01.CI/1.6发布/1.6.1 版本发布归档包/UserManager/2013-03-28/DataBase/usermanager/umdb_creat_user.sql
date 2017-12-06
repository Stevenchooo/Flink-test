use mysql;
DELETE FROM USER WHERE USER='usermanager';
DELETE FROM DB WHERE USER='usermanager';


GRANT ALL ON usermanager.* TO 'usermanager'@'10.120.5.21' IDENTIFIED BY 'Az3N)sn3H';
GRANT ALL ON usermanager.* TO 'usermanager'@'10.120.5.16' IDENTIFIED BY '6Mah%hzn';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON usermanager.*  TO 'usermanager'@'192.168.%' IDENTIFIED BY '6Mah%hzn';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON usermanager.*  TO 'usermanager'@'localhost' IDENTIFIED BY '6Mah%hzn';
grant select on mysql.proc to 'usermanager'@'192.168.%' IDENTIFIED BY '6Mah%hzn';
grant select on mysql.proc to 'usermanager'@'localhost' IDENTIFIED BY '6Mah%hzn';

FLUSH PRIVILEGES;