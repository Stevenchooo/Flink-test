use mysql;
DELETE FROM USER WHERE USER='odpdatadb';
DELETE FROM DB WHERE USER='odpdatadb';
FLUSH PRIVILEGES;

GRANT ALL ON odpdatadb.* TO 'odpdatadb'@'%' IDENTIFIED BY 'XXXXXX';
GRANT ALL ON odpdatadb.* TO 'odpdatadb'@'localhost' IDENTIFIED BY 'XXXXXX';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON odpdatadb.*  TO 'odpdatadb'@'%' IDENTIFIED BY 'XXXXXX';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON odpdatadb.*  TO 'odpdatadb'@'localhost' IDENTIFIED BY 'XXXXXX';
grant select on mysql.proc to 'odpdatadb'@'%' IDENTIFIED BY 'XXXXXX';


use mysql;
DELETE FROM USER WHERE USER='odpdataquery';
DELETE FROM DB WHERE USER='odpdataquery';
FLUSH PRIVILEGES;

GRANT SELECT ON odpdatadb.* TO 'odpdataquery'@'%' IDENTIFIED BY 'XXXXXX';
GRANT SELECT ON odpdatadb.* TO 'odpdataquery'@'localhost' IDENTIFIED BY 'XXXXXX';

quit