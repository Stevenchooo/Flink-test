use mysql;
DELETE FROM USER WHERE USER='tccserverdb4snd';
DELETE FROM DB WHERE USER='tccserverdb4snd';
FLUSH PRIVILEGES;

GRANT ALL ON tccserverdb4snd.* TO 'tccserverdb4snd'@'%' IDENTIFIED BY 'gW#c%l3';
GRANT ALL ON tccserverdb4snd.* TO 'tccserverdb4snd'@'localhost' IDENTIFIED BY 'gW#c%l3';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccserverdb4snd.*  TO 'tccserverdb4snd'@'%' IDENTIFIED BY 'gW#c%l3';
GRANT EXECUTE,ALTER ROUTINE,CREATE ROUTINE ON tccserverdb4snd.*  TO 'tccserverdb4snd'@'localhost' IDENTIFIED BY 'gW#c%l3';
grant select on mysql.proc to 'tccserverdb4snd'@'%' IDENTIFIED BY 'gW#c%l3';
quit