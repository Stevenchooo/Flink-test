DROP PROCEDURE IF EXISTS sp_mktDicListQuery;
CREATE PROCEDURE sp_mktDicListQuery (
    out pRetCode            int,
    
    in  pOperator           varchar(50),
    in  pType               varchar(50)
)
proc: BEGIN
	
	 declare vType           int;
    
     set vType = null;
    
     if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
     end if;
     
     
    set pRetCode = 0;

    select 
        id,
        name
    from 
        t_mkt_dic_info
    where
        type = pType and dept_type = ifnull(vType,dept_type);
    
end;