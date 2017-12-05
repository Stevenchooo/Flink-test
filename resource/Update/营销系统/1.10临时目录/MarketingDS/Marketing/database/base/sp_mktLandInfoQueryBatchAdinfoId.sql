DELIMITER // 
DROP PROCEDURE IF EXISTS sp_mktLandInfoQueryBatchAdinfoId;
CREATE PROCEDURE sp_mktLandInfoQueryBatchAdinfoId (
    out pRetCode               int,
    in pOperator               varchar(50),
    in pMktId                  int,
    in pAdState                int,
    in pAdWebName              int,
    in pAdPort                 int,
    in pInputUser              varchar(50),
    in pAdInfoDeliveryBeginDay varchar(10),
    in pAdInfoDeliveryEndDay   varchar(10),
    in pAdqueryDateBeginDay    varchar(10),
    in pAdqueryDateEndDay      varchar(10),
    in pAdIsVmallPlat          int
)
proc: BEGIN
    declare vplatform        varchar(50);
    
    declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    
    set pRetCode = 0;

    if(pInputUser = '')then
    
        set pInputUser = null;
        
    end if;
    
    if(pAdInfoDeliveryBeginDay = '')then
    
        set pAdInfoDeliveryBeginDay = null;
  
    end if;
    
    if(pAdInfoDeliveryEndDay = '')then
    
        set pAdInfoDeliveryEndDay = null;
  
    end if;
    
    if(pAdqueryDateBeginDay = '')then
    
        set pAdqueryDateBeginDay = null;
  
    end if;
    
    if(pAdqueryDateEndDay = '')then
    
        set pAdqueryDateEndDay = null;
        
    end if;
    
    if(pMktId = -1) then
    
        set pMktId = null;
        
    end if;
    
    if(pAdState = -1)then
    
        set pAdState = null;
        
    end if;
    
    if(pAdWebName = -1)then
    
        set pAdWebName = null;
        
    end if;
    
    
    if(pAdPort = -1)then
    
        set pAdPort = null;
        
    end if;
    
    if(pAdIsVmallPlat = 1) then
        set vplatform = '0';
    end if;
    
    
    
    if(exists(select * from t_ms_model where name=pOperator and val REGEXP  '^ad')) then
    
   select
        t2.aid              as      adInfoId
        
    from
    (
    	select 
    		aid
    	from t_mkt_land_info
    	where
    	   dept_type = ifnull(vType,dept_type)
    )t2
    join
    (
        select
            aid
        from
            t_mkt_ad_info
        where
            id = ifnull(pMktId,id) 
            and web_name = ifnull(pAdWebName,web_name) 
            and port = ifnull(pAdPort,port) 
            and if(pAdIsVmallPlat=0 ,platform in ('1','2'),if(pAdIsVmallPlat=1,platform='0',platform in ('3','4','5'))) 
            and state = ifnull(pAdState,state) 
            and date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d')) 
            and date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) 
            and ((delivery_begin_day between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) 
            and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (delivery_end_day between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) 
            and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
            and dept_type = ifnull(vType,dept_type)
        order by create_time desc
    )t1
    on t2.aid = t1.aid
    join
    (
        select
            tt4.aid
        from
        (
            select
                id
            from
                t_mkt_user_ad_info
            where
                account = pOperator
        )tt3
        join
          t_mkt_ad_info tt4
        on tt3.id = tt4.id
    )t11
    on t2.aid = t11.aid;
    
   
    
    else
    
    select
            t2.aid              as      adInfoId
    from
    (
    	select 
    		aid
    	from t_mkt_land_info
    	where dept_type = ifnull(vType,dept_type)
    )t2
    join
    (
        select
            aid
        from
            t_mkt_ad_info
        where
            id = ifnull(pMktId,id) 
            and web_name = ifnull(pAdWebName,web_name) 
            and port = ifnull(pAdPort,port) 
            and if(pAdIsVmallPlat=0 ,platform in ('1','2'),if(pAdIsVmallPlat=1,platform='0',platform in ('3','4','5')))
            and state = ifnull(pAdState,state) 
            and date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d')) 
            and date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) 
            and ((delivery_begin_day between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) 
            and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (delivery_end_day between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) 
            and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
            and dept_type = ifnull(vType,dept_type)
        order by create_time desc
    )t1
    on t2.aid = t1.aid;
    
    
    end if;    
end
   //  
DELIMITER ;