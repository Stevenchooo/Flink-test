DROP PROCEDURE IF EXISTS sp_adminModify;
CREATE PROCEDURE sp_adminModify (
    out pRetCode            int,
    in pOperator            varchar(50),
    in pAdminId             int,
    in pRole                char(100)
)
proc: BEGIN
    declare vPid            int default -1;
    
    select pid into vPid from t_ms_model where id=pAdminId;
    if(vPid < 0) then -- not exists
        set pRetCode = 2001;
        leave proc;
    end if;
    
    update t_ms_model set
        val = pRole,
        modifyTime = now()
     where id = pAdminId;
    
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;
end;