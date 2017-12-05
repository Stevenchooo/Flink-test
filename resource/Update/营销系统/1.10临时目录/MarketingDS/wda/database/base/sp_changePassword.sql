delimiter //
DROP PROCEDURE IF EXISTS sp_changePassword;
CREATE PROCEDURE sp_changePassword (
    out pRetCode            int,
    in pOperator            varchar(50),
    in pAccount             varchar(50),
    in pOldPassword         varchar(50), -- 原密码
    in pPassword            varchar(50), -- 新密码
    in pExpireDayNum        int,
    in pMaxDupPwdNum        int
)
proc: BEGIN
    declare vUpdPwdTime     timestamp default null;
    declare vOldPwd         char(50) default null;
    declare vOldPwds        varchar(255) default '';
    
    select oldPwd,password into vOldPwds, vOldPwd
     from t_ms_user
    where account=pAccount
      and password=pOldPassword;
      
    if(found_rows() <= 0 or vOldPwd is null) then
        set pRetCode = 3502;
        leave proc;
    end if;
    
    set vOldPwds = fun_cutOldPassword(concat(vOldPwd, ',', vOldPwds), pMaxDupPwdNum);
    if(instr(vOldPwds, concat(pPassword, ',')) > 0) then
        set pRetCode = 4005;
        leave proc;
    end if;
    
    if(pOperator = pAccount) then
        set vUpdPwdTime = DATE_ADD(now(), INTERVAL pExpireDayNum DAY);
    else
        set vUpdPwdTime = now(); -- 代修改密码，则立刻过期
    end if;
    
    update t_ms_user set
        password = ifnull(pPassword, password), 
        updatePwdTime = vUpdPwdTime,
        oldPwd = vOldPwds
     where account = pAccount; 

    if(row_count() <= 0) then
        set pRetCode = 4; -- 数据库错误
        leave proc;
    end if;

    set pRetCode = 0;
end//