




DROP PROCEDURE IF EXISTS sp_adInfoSaveMaterialUrl;

CREATE PROCEDURE sp_adInfoSaveMaterialUrl (
    out pRetCode            int,
    
    in pOperator           varchar(50),
    in pAid                 int,
    in materialUrl          varchar(255)
   
)
proc: BEGIN
    declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    if(not exists(select * from t_mkt_ad_info where aid = pAid and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
    end if;
    
	if(exists(select  * from t_mkt_material_info where aid = pAid)) then
	
	   update
	       t_mkt_material_info
	   set
	       type = 1,
	       show_name = materialUrl,
	       operator = pOperator,
	       update_time = now(),
	       state = 1
	   where
	       aid = pAid;
	else
	    insert into t_mkt_material_info ( 
		       aid,
		       type,
		       show_name,
		       operator,
		       create_time,
		       update_time,
		       state
	       )
	       values(
		       pAid,
		       1,
		       materialUrl,
		       pOperator,
		       now(),
		       now(),
		       1
	       );
	end if; 
   
end;