DROP PROCEDURE IF EXISTS sp_adInfoPublish;

CREATE PROCEDURE sp_adInfoPublish (
 	out   pRetCode            int,
 	
 	in    pOperator           varchar(50),
    in    pAid                int
   
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
	
	    if(!exists(select 
                * 
              from 
                t_mkt_ad_info 
              where 
                aid = pAid  and  id is not null and media_type is not null and web_name is not null  
                and channel is not null and ad_position is not null and port is not null and platform is not null
                and delivery_days is not null and delivery_times is not null )) then
        select 8802 into pRetCode;
        leave proc;
    end if; 
    
    
   update   t_mkt_ad_info
   set     state = 1
   where   aid = pAid;
end;