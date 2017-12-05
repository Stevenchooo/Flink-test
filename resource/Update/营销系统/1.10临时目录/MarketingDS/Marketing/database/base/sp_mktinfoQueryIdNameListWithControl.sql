
DROP PROCEDURE IF EXISTS sp_mktinfoQueryIdNameListWithControl;
CREATE PROCEDURE sp_mktinfoQueryIdNameListWithControl (

    out pRetCode            int,
    in pOperator            varchar(50)
)
proc: BEGIN
     declare vType           int;
    
     set vType = null;
    
     if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
     end if;
    
	 if(exists(select * from t_ms_model where name=pOperator and val REGEXP  '^ad')) then
	    select
            -1              as    mktinfoId,
            '全部'           as     mktinfoName
        from 
            dual
            
        union all  
          
        select 
            t1.id           as      mktinfoId,
            t1.name         as      mktinfoName
        from
        (
            select
                t_mkt_info.id,
                t_mkt_info.name,
                t_mkt_info.create_time
            from
                t_mkt_info
            join
                t_mkt_user_ad_info
            on t_mkt_info.id = t_mkt_user_ad_info.id
            where t_mkt_user_ad_info.account = pOperator  and t_mkt_info.dept_type = ifnull(vType,t_mkt_info.dept_type)
            order by t_mkt_info.create_time desc
        )t1;
	 
	 else
	 
	    select
	        -1              as     mktinfoId,
	        '全部'           as     mktinfoName
	    from 
	        dual
	        
	    union all  
	      
	    select 
	        t1.id           as      mktinfoId,
	        t1.name         as      mktinfoName
	    from
	    (
	        select
	            id,
	            name,
	            create_time
	        from
	            t_mkt_info
	        where
	           dept_type = ifnull(vType,dept_type)
	        order by create_time desc
	    )t1;
	 end if;
   
end;