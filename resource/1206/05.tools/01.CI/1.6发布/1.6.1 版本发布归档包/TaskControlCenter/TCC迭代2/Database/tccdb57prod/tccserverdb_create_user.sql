use mysql;
DELETE FROM USER WHERE USER='tccdb57prod';
DELETE FROM DB WHERE USER='tccdb57prod';
FLUSH PRIVILEGES;

GRANT ALL ON tccdb57prod.* TO 'tccdb57prod'@'%' IDENTIFIED BY 'pOPs19WI';
GRANT ALL ON tccdb57prod.* TO 'tccdb57prod'@'localhost' IDENTIFIED BY 'pOPs19WI';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccdb57prod.*  TO 'tccdb57prod'@'%' IDENTIFIED BY 'pOPs19WI';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccdb57prod.*  TO 'tccdb57prod'@'localhost' IDENTIFIED BY 'pOPs19WI';
grant select on mysql.proc to 'tccdb57prod'@'%' IDENTIFIED BY 'pOPs19WI';
quit