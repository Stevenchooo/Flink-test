DELIMITER // 
DROP PROCEDURE IF EXISTS sp_adInfoQueryBatchAdinfoId;
CREATE PROCEDURE sp_adInfoQueryBatchAdinfoId (
    out pRetCode            int,
    in pOperator            varchar(50),
    in pMktId               int,
    in pAdState             int,
    in pAdWebName           int,
    in pAdPort              int,
    in pInputUser              varchar(50),
    in landPlatform            varchar(10),
    in pAdqueryDateBeginDay    varchar(10),
    in pAdqueryDateEndDay      varchar(10)
)
proc: BEGIN
    declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    set pRetCode = 0;

    if(pInputUser = '')then
    
        set pInputUser = null;
        
    end if;
    
    if(landPlatform = '' || landPlatform = -1 || landPlatform = '-1')then
    
        set landPlatform = null;
  
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
    
    if(exists(select * from t_ms_model where name=pOperator and val REGEXP  '^ad')) then
    
   select
        t1.aid              as      adInfoId
        
    from
    (
        select
            aid
        from
            t_mkt_ad_info
        where
            id = ifnull(pMktId,id) 
        and web_name = ifnull(pAdWebName,web_name) 
        and port = ifnull(pAdPort,port) 
        and state = ifnull(pAdState,state) 
        and date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d'))
        and date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) 
        and platform = ifnull(landPlatform,platform) 
        and dept_type = ifnull(vType,dept_type)
        order by create_time desc
    )t1
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
    on t1.aid = t11.aid;
    
   
    
    else
    
    select
            t1.aid              as      adInfoId
    from
    (
        select
            aid
        from
            t_mkt_ad_info
        where
            id = ifnull(pMktId,id) 
        and web_name = ifnull(pAdWebName,web_name) 
        and port = ifnull(pAdPort,port) 
        and state = ifnull(pAdState,state) 
        and date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d')) 
        and date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) 
        and platform = ifnull(landPlatform,platform)  
        and dept_type = ifnull(vType,dept_type)
        order by create_time desc
    )t1;
    
    
    end if; 
end   
   //  
DELIMITER ;