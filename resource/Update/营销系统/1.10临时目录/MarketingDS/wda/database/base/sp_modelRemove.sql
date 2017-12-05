delimiter //
DROP PROCEDURE IF EXISTS sp_modelRemove;
CREATE PROCEDURE sp_modelRemove (
    out pRetCode            int,
    in  pMid                int          -- 不能用pid，因为不区分大小写
)
proc: BEGIN
    if(not exists(select * from t_ms_model where id=pMid)) then
        set pRetCode = 2001;
        leave proc;
    end if;

    if(exists(select * from t_ms_model where pid=pMid)) then
        set pRetCode = 7003;
        leave proc;
    end if;
    
    delete from t_ms_model where id = pMid;
    
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;
end//