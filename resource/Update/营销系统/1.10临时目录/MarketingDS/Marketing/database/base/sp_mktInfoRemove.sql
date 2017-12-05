DROP PROCEDURE IF EXISTS sp_mktInfoRemove;
CREATE PROCEDURE sp_mktInfoRemove (
    out pRetCode            int,
    
    in pOperator            varchar(50),
    in mktInfoId            int -- 活动ID
)
proc: BEGIN
   
	declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    if(not exists(select * from t_mkt_info where id = mktInfoId and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
        
    end if;
    
    
    if(exists(select * from t_mkt_ad_info where id = mktInfoId)) then
        set pRetCode = 8001;
        leave proc;
    end if;


    -- 删除数据
    
    delete from t_mkt_user_ad_info where id = mktInfoId;
    
    
    delete from t_mkt_info where id = mktInfoId;
    
    

    
       
    set pRetCode = 0;
end;