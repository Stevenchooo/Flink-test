

DROP PROCEDURE IF EXISTS `sp_getMktNameListByDept`;
DELIMITER //
CREATE PROCEDURE sp_getMktNameListByDept (

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
            t1.id           as      mktinfoId,
            t1.name         as      mktinfoName,
            t1.create_time  as      create_time,
	        t1.create_year  as      create_year,
	        t1.create_month as      create_month
        from
        (
            select
                t_mkt_info.id,
                t_mkt_info.name,
                DATE_FORMAT(t_mkt_info.create_time,'%Y年%m月') as create_time,
                DATE_FORMAT(t_mkt_info.create_time,'%Y') as create_year,
                DATE_FORMAT(t_mkt_info.create_time,'%m月') as create_month
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
	        t1.id           as      mktinfoId,
	        t1.name         as      mktinfoName,
	        t1.create_time  as      create_time,
	        t1.create_year  as      create_year,
	        t1.create_month as      create_month
 	    from
	    (
	        select
	            id,
	            name,
	            DATE_FORMAT(t_mkt_info.create_time,'%Y年%m月') as create_time,
	            DATE_FORMAT(t_mkt_info.create_time,'%Y') as create_year,
                DATE_FORMAT(t_mkt_info.create_time,'%m月') as create_month
	        from
	            t_mkt_info
	        where
	           dept_type = ifnull(vType,dept_type)  
	        order by t_mkt_info.create_time desc
	    )t1;
	 end if;
   

   end//
DELIMITER ;