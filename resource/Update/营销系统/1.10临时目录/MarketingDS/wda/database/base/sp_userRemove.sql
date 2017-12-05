delimiter //
DROP PROCEDURE IF EXISTS sp_userRemove;
CREATE PROCEDURE sp_userRemove (
    out pRetCode            int,
    
    in pOperator            varchar(50), -- 创建人
    in pAccount             varchar(50)
)
proc: BEGIN
	if(pOperator = pAccount) then
	    set pRetCode = 4100; -- 不能删除自己，防止管理员删除的一个都不剩
	    leave proc;
	end if;
	
	if(not exists(select * from t_ms_user where account=pAccount)) then
	    set pRetCode = 2001;
	    leave proc;
	end if;

	-- 删除当前用户的管理员
    delete from t_ms_model where name=pAccount and meta='admin';
    
    -- 先将用户数据移到删除用户表中，然后删除用户信息
    insert into t_ms_deluser(account,creator,createTime,lastLoginTime,phoneNum,email,description)
    select account,creator,createTime,lastLoginTime,phoneNum,email,description
     from t_ms_user
    where account=pAccount;
    
    if(row_count() <= 0) then
        set pRetCode = 4; -- 数据库错误
        leave proc;
    end if;

    -- 删除用户数据
    delete from t_ms_user where account=pAccount;
       
    if(row_count() <= 0) then
        set pRetCode = 4; -- 数据库错误
        leave proc;
    end if;
    
    set pRetCode = 0;
end//

