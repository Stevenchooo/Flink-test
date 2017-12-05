DROP PROCEDURE IF EXISTS sp_dicInfoExists;
CREATE PROCEDURE sp_dicInfoExists (
    out pRetCode            int,
    
    in  pOperator    varchar(50),
	in  pname        varchar(255),
	in  parentId     varchar(10) ,
	in  ptype        varchar(10) 
   
)
proc: BEGIN
	
	declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
	set pRetCode = 0;
	select pname;
	select parentId;
	select ptype;
	
	if(parentId is null) then
	  select exists(select * from t_mkt_dic_info where name=pname and type = ptype and dept_type = ifnull(vType,dept_type)) as 'exists';
	else
		select exists(select * from t_mkt_dic_info where name=pname and pid = parentId and type = ptype and dept_type = ifnull(vType,dept_type)) as 'exists';
	end if;
end;