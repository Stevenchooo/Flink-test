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
    
     delete from t_mkt_user_ad_info where account=pAccount;
     
    -- 删除用户数据
    delete from t_ms_user where account=pAccount;
       
   
    
    set pRetCode = 0;
end;