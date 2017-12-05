DROP PROCEDURE IF EXISTS sp_adminUnlock;
CREATE PROCEDURE sp_adminUnlock (
    out pRetCode            int,
    
    in pAccount             varchar(50)
)
proc: BEGIN
	if(not exists(select * from t_ms_lockuser where val=pAccount)) then
	    select 20001 into pRetCode;
	    leave proc;
	end if;
	
    set pRetCode = 0;

    -- 清除锁
	delete from t_ms_lockuser where val=pAccount;

    if(row_count() <= 0) then
        set pRetCode = 4; -- 数据库错误
    end if;
end;