DROP PROCEDURE IF EXISTS sp_adminResetPwd;
CREATE PROCEDURE sp_adminResetPwd (
    out pRetCode            int,
    
    in pAccount             varchar(50),
    in pPassword            varchar(50)
)
proc: BEGIN
	if(not exists(select * from t_ms_user where account=pAccount and status<>'del')) then
	    select 4001 into pRetCode;
	    leave proc;
	end if;
	
    set pRetCode = 0;

    -- 密码必须立即过期 && 清除randNum
    update t_ms_user set
        password = pPassword,
        updatePwdTime = '1975-1-1', -- 默认过期时间，立即过期
        randNum = 0
     where account = pAccount;

    if(row_count() <= 0) then
        set pRetCode = 4; -- 数据库错误
    end if;
    
    select pPassword as password;
end;