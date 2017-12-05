

DROP PROCEDURE IF EXISTS sp_mktMonitorEmailUserInfo;
CREATE PROCEDURE sp_mktMonitorEmailUserInfo (
    out pRetCode            int,
    in  mktInfoId            varchar(50)
)
proc: BEGIN
   
    
    declare vType           int;
    
    select dept_type into vType from t_mkt_info where id = mktInfoId;
    
    
     SELECT 0 INTO pRetCode;
     
     if(mktInfoId is null) then
        select 100 into pRetCode;
        leave proc;
    end if;
    
    
    select
        t3.account               as      account,
        t3.name                  as      name,
        t3.department            as      department,
        t4.role                  as      role
    from
    (
        select
            t1.operator         as      name,
            max(t2.val)         as      role
        from
	    (
	        select
	             operator
	        from
	            t_mkt_info
	        where
	             id = mktInfoId
                 
            union 
            
            select
	             operator
	        from
	            t_mkt_ad_info
	        where
	             id = mktInfoId
                 
            union 
            
            select
	             operator
	        from
	            t_mkt_land_info
	        where
	             aid in (
                    select
                         aid
                    from
                        t_mkt_ad_info
                    where
                         id = mktInfoId
                 )
                 
            union 
            
            select
	             operator
	        from
	            t_mkt_monitor_info
	        where
	             aid in (
                    select
                         aid
                    from
                        t_mkt_ad_info
                    where
                         id = mktInfoId
                 )     
	    )t1
	    join
	    (
	        select
	           distinct name,val
	        from 
	            t_ms_model 
	         where meta = 'admin' and val is not null  
	    )t2
	    on t1.operator = t2.name
	    
	    group by t2.name
	    order by role,name
    )t4
    join
        t_ms_user t3
    on t4.name = t3.account
    where
        t3.deptType = vType;
    
end;  