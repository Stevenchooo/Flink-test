DROP PROCEDURE IF EXISTS sp_dicInfoRemoveProduct;
CREATE PROCEDURE sp_dicInfoRemoveProduct (
    out pRetCode            int,
    
    in  pOperator           varchar(50),
    in  mktDicId            int
)
proc: BEGIN  
	
	declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    -- 判断用户有没有权限
    if(not exists(select * from t_mkt_dic_info where id = mktDicId and dept_type = ifnull(vType,dept_type))) then
        set  pRetCode = 4100;
        leave proc;
    end if;
    
    
	-- 判断活动表里是否还有依赖的项
	if(exists(select * from t_mkt_info where product = mktDicId)) then
	    set  pRetCode = 9002;
	    leave proc;
    end if;
	
    -- 判断是否存在该平台
    if(not exists(select * from t_mkt_dic_info where id = mktDicId)) then
        set pRetCode = 2001;
        leave proc;
    end if;


    -- 删除数据
    delete from t_mkt_dic_info where id = mktDicId;
       
    set pRetCode = 0;
end;