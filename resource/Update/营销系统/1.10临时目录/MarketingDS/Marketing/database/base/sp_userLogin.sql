DROP PROCEDURE IF EXISTS sp_userLogin;
CREATE PROCEDURE sp_userLogin (
    out pRetCode            int,
    
    in pAccount             varchar(40),
    in pPassword            varchar(50),
    in pIp                  varchar(50),
    in pLockType            char(10),  -- 当前只支持按用户锁定，没有支持按ip锁定
    in pLockInterval        int,
    in pMaxReloginTimes     int
)
proc: BEGIN
    declare vExpiredDays    int default 0;
    declare vLockExpireTime timestamp default null;

    if(not exists(select * from t_ms_user where account=pAccount and status='actived')) then
        set pRetCode = 4001;
        leave proc;
    end if;

    -- 先删除过期的锁定
    delete from t_ms_lockuser
    where val=pAccount
      and type=pLockType
      and expireTime < now();
    
    -- 错误尝试次数大于pMaxReloginTimes，且仍然在锁定期中，不可以重新尝试
    if(exists(select * from t_ms_lockuser where val=pAccount and type=pLockType and tryTimes>=pMaxReloginTimes)) then
    	set pRetCode = 4004;
        leave proc;
    end if;
    
    select datediff(now(), updatePwdTime) into vExpiredDays
      from t_ms_user
     where account=pAccount and password=pPassword;
    
    if(found_rows() <= 0) then
        set pRetCode = 4001;
        set vLockExpireTime = DATE_ADD(now(), INTERVAL pLockInterval SECOND);
        
        INSERT into t_ms_lockuser(type, tryTimes, val, expireTime)
        values(pLockType, 1, pAccount, vLockExpireTime)
        ON DUPLICATE KEY UPDATE
        	tryTimes = tryTimes + 1,
            expireTime = vLockExpireTime;
        
        leave proc;
    else
        -- 输入正确密码，则清除尝试锁定的记录
	    delete from t_ms_lockuser where val=pAccount and type=pLockType;
    end if;
    
    if(vExpiredDays is null) then
        set vExpiredDays = 10000;
    end if;

    update t_ms_user set
        lastLoginTime = now(),
        randNum = 0   -- 成功输入密码后，清除重置密码的状态，使得重置连接失败
     where account = pAccount;
     
    insert into t_ms_log(account, apiName, info)
    values(pAccount, '/user/login', concat('expiredDays:', vExpiredDays));
    
    set pRetCode = 0;  -- 用户名密码正确

    select vExpiredDays as expiredDays;
end;