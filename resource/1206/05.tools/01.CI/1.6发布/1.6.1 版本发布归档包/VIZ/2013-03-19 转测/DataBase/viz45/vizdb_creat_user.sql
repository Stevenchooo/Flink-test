﻿use mysql;
DELETE FROM USER WHERE USER='viz45';
DELETE FROM DB WHERE USER='viz45';
FLUSH PRIVILEGES;

GRANT ALL ON viz45.* TO 'viz45'@'10.120.5.45' IDENTIFIED BY 'XXXXXX';
GRANT ALL ON viz45.* TO 'viz45'@'localhost' IDENTIFIED BY 'XXXXXX';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON viz45.*  TO 'viz45'@'10.120.5.45' IDENTIFIED BY 'XXXXXX';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON viz45.*  TO 'viz45'@'localhost' IDENTIFIED BY 'XXXXXX';
grant select on mysql.proc to 'viz45'@'10.120.5.45' IDENTIFIED BY 'XXXXXX';
grant select on mysql.proc to 'viz45'@'localhost' IDENTIFIED BY 'XXXXXX';
FLUSH PRIVILEGES;