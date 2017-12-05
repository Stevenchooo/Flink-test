delimiter //
DROP PROCEDURE IF EXISTS sp_getPwdByMail;
CREATE PROCEDURE sp_getPwdByMail (
    out pRetCode            int,
    
    in pAccount             varchar(50),
    in pPassport		    int

)
proc: BEGIN
	if(not exists(select * from t_ms_user where account=pAccount and status<>'del')) then
	    select 4001 into pRetCode;
	    leave proc;
	end if;
	
    set pRetCode = 0;

    -- 设置randNum
    update t_ms_user set
        randNum = pPassport
     where account = pAccount;

    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
	-- 要返回email
    select email from t_ms_user where account=pAccount;
end//