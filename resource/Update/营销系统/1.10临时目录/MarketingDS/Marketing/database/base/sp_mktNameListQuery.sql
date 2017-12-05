DROP PROCEDURE IF EXISTS sp_mktNameListQuery;
CREATE PROCEDURE sp_mktNameListQuery (
    out pRetCode            int,
    in  pOperator           varchar(50)
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
        name,
        dept_type 
    from 
        t_mkt_info
    where
        dept_type = ifnull(vType,dept_type);
    
end;