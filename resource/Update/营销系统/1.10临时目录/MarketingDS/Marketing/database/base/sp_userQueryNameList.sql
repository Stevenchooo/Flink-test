

DROP PROCEDURE IF EXISTS sp_userQueryNameList;
CREATE PROCEDURE sp_userQueryNameList (
    out pRetCode            int
)
proc: BEGIN
    select 
        account as  userName
     from 
        t_ms_user
     where 
        status <> 'del';
end;