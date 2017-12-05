

DROP PROCEDURE IF EXISTS sp_monitorInfoModify;

CREATE PROCEDURE sp_monitorInfoModify (
    out pRetCode            int,
    
    in  pOperator          varchar(255),
    in  pAid               int,
    in  monitorBiCode      varchar(255),
    in  monitorExposureUrl varchar(255),
    in  monitorClickUrl    varchar(255)

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
    
	if(exists(
            select 
                * 
            from 
                t_mkt_monitor_info 
            where 
                aid = pAid
               )
        ) then
        
        update
	       t_mkt_monitor_info
	    set
	       bi_code = ifnull(monitorBiCode, bi_code),
	       exposure_url = ifnull(monitorExposureUrl, exposure_url),
	       click_url = ifnull(monitorClickUrl, click_url),
	       update_time = now()
	    where
	        aid = pAid;
        
        leave proc;
    end if;
    
   --  获取属性
   select  dept_type into vType from t_mkt_ad_info where aid = pAid;
    
   insert into t_mkt_monitor_info (
        aid,
        bi_code,
        exposure_url,
        click_url,
        create_time,
        update_time,
        operator,
        dept_type
        )
    values (
        pAid,
        monitorBiCode,
        monitorExposureUrl,
        monitorClickUrl,
        now(),
        now(),
        pOperator,
        vType);
    
	
end;