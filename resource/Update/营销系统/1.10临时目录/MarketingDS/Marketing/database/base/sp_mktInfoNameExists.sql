DROP PROCEDURE IF EXISTS sp_mktInfoNameExists;
CREATE PROCEDURE sp_mktInfoNameExists(
    out pRetCode            int,
    
    in pOperator            varchar(255),
    in pName                varchar(255)
   
)
proc: BEGIN
    
     declare vType                    int;
     
     set vType = null;
    
    select deptType into vType from t_ms_user where account = pOperator;
  
    select exists(select * from t_mkt_info where name=pName and dept_type = ifnull(vType,dept_type) ) as 'exists';
    
end;