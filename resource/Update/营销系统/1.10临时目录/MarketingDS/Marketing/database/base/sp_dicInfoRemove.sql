DROP PROCEDURE IF EXISTS sp_dicInfoRemove;
CREATE PROCEDURE sp_dicInfoRemove (
    out pRetCode            int,
    
    in  pOperator           varchar(50),
    in  mktDicId            int,
    in  mktDicPid           int
)
proc: BEGIN  
	
   declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    if(not exists(select * from t_mkt_dic_info where id = mktDicId and if(mktDicPid is null, 1=1, pid = mktDicPid) and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
    end if;
    
    if(not exists(select * from t_mkt_dic_info where id = mktDicId and if(mktDicPid is null, 1=1, pid = mktDicPid))) then
        set pRetCode = 2001;
        leave proc;
    end if;


    -- 删除数据
    delete from t_mkt_dic_info where id = mktDicId and if(mktDicPid is null, 1=1, pid = mktDicPid);
       
    set pRetCode = 0;
end;