DELIMITER // 

DROP PROCEDURE IF EXISTS sp_adInfoMaterialQuery;

CREATE PROCEDURE sp_adInfoMaterialQuery (
    out pRetCode            int,
    in pOperator            varchar(50),
    in pMktId               int,
    in pAdState             int,
    in pAdWebName           int,
    in pAdPort              int,
    in pInputUser              varchar(50),
    in pAdInfoDeliveryBeginDay varchar(10),
    in pAdInfoDeliveryEndDay   varchar(10),
    in pAdqueryDateBeginDay    varchar(10),
    in pAdqueryDateEndDay      varchar(10),
    in plandPlatform        varchar(20)
)
proc: BEGIN
    declare vName           varchar(100);
    declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    set pRetCode = 0;
    
    if(pInputUser = '' || pInputUser = '-1')then
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
    
    if(plandPlatform = -1 || plandPlatform = '-1' || plandPlatform = '' )then
        set plandPlatform = null;   
    end if;
    
    
    if(exists(select * from t_ms_model where name=pOperator and val REGEXP  '^ad')) then
    select
        t1.aid                                as      adInfoId,
        t11.name                              as      mktinfoName,
        t2.mediaTye                           as      adInfoMediaType,
        t2.webName                            as      adInfoWebName,
        t1.channel                            as      adInfoChannel,
        t1.ad_position                        as      adInfoPosition,
        t8.show_name                          as      materialName,
        t8.path                               as      materialPath
    from
    (
        select *
        from t_mkt_ad_info
        where operator=ifnull(pInputUser,operator) 
            and id = ifnull(pMktId,id) 
            and web_name = ifnull(pAdWebName,web_name) 
            and port = ifnull(pAdPort,port) 
            and state = ifnull(pAdState,state) 
            and date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d')) 
            and date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) 
            and ((date_format(delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (date_format(delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
            and dept_type = ifnull(vType,dept_type)
            and platform = ifnull(plandPlatform,platform)
    )t1
    left outer join
    (
        select
            tt1.id      as      id,
            tt1.name    as      webName,
            tt2.name    as      mediaTye
        from
        (
	        select
	            id,
	            pid,
	            name
	        from t_mkt_dic_info
	        where type = 'web'
        )tt1
        left outer join
        (
            select
                id,
                name
            from t_mkt_dic_info
            where type = 'media'
        )tt2
        on tt1.pid = tt2.id
    )t2
    on t1.web_name = t2.id  
    join
    (
        select
            tt4.aid
        from
        (
            select
                id
            from t_mkt_user_ad_info
            where account = pOperator
        )tt3
        join
          t_mkt_ad_info tt4
        on tt3.id = tt4.id
    )t7
    on t1.aid = t7.aid
    join
    (
        select 
            aid,
            show_name,
            path
        from t_mkt_material_info
        where
            state = 1  and type = 0
    )t8
    on t1.aid = t8.aid
    left outer join
    (
        select
            id     as  id,
            name   as  name
        from t_mkt_info          
    )t11
    on t1.id = t11.id;
    
    
    
    
    else
    
    select
        t1.aid                                as      adInfoId,
        t11.name                              as      mktinfoName,
        t2.mediaTye                           as      adInfoMediaType,
        t2.webName                            as      adInfoWebName,
        t1.channel                            as      adInfoChannel,
        t1.ad_position                        as      adInfoPosition,
        t8.show_name                          as      materialName,
        t8.path                               as      materialPath
    from
    (
        select *
        from t_mkt_ad_info
        where operator=ifnull(pInputUser,operator) 
            and id = ifnull(pMktId,id) 
            and web_name = ifnull(pAdWebName,web_name) 
            and port = ifnull(pAdPort,port) 
            and state = ifnull(pAdState,state) 
            and date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d')) 
            and date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) 
            and ((date_format(delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (date_format(delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
            and dept_type = ifnull(vType,dept_type)
            and platform = ifnull(plandPlatform,platform)
    )t1
    left outer join
    (
        select
            tt1.id      as      id,
            tt1.name    as      webName,
            tt2.name    as      mediaTye
        from
        (
            select
                id,
                pid,
                name
            from t_mkt_dic_info
            where type = 'web'
        )tt1
        left outer join
        (
            select
                id,
                name
            from t_mkt_dic_info
            where type = 'media'
        )tt2
        on tt1.pid = tt2.id
    )t2
    on t1.web_name = t2.id  
    join
    (
        select 
            aid,
            show_name,
            path
        from t_mkt_material_info
        where
            state = 1  and type = 0
    )t8
    on t1.aid = t8.aid
    left outer join
    (
        select
            id     as  id,
            name   as  name
        from t_mkt_info          
    )t11
    on t1.id = t11.id;
    
    
    
    end if;
end

   //  
DELIMITER ;