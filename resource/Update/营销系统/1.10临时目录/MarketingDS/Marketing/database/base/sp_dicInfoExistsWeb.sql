DROP PROCEDURE IF EXISTS sp_dicInfoExistsWeb;
CREATE PROCEDURE sp_dicInfoExistsWeb (
    out pRetCode            int,
    
    in  pOperator    varchar(50),
	in  pname        varchar(255),
	in  ptype        varchar(10) 
   
)
proc: BEGIN
	
	declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    
	set pRetCode = 0;
	select exists(select * from t_mkt_dic_info where name=pname and type = ptype and dept_type = ifnull(vType,dept_type)) as 'exists';
end;