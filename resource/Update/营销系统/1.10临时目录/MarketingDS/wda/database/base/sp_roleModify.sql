delimiter //
DROP PROCEDURE IF EXISTS sp_roleModify;
CREATE PROCEDURE sp_roleModify (
    out pRetCode            int,
    
    in pRole                varchar(50),
    in pName                varchar(50),
    in pDesc                varchar(255)
)
proc: BEGIN
    if(exists(select * from t_ms_role where name=pName and role<>pRole)) then
        select 3504 into pRetCode;
        leave proc;
    end if;
	
    set pRetCode = 0;

    update t_ms_role set
        name = ifnull(pName, name),
        description = ifnull(pDesc, description)
    where role = pRole;

    if(row_count() <= 0) then
        set pRetCode = 4; -- 数据库错误
    end if;
end//