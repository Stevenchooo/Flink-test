DROP PROCEDURE IF EXISTS sp_adExportQuery;
CREATE PROCEDURE sp_adExportQuery (
    out pRetCode            int,
    
    in pOperator            varchar(50),
    in pMktId               int,
    in pAdState             int,
    in pAdWebName           int,
    in pAdPort              int,
    in pFrom                int,
    in pPerPage             int
)
proc: BEGIN
     
	declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    set pRetCode = 0;
    
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
            t1.aid              as      adInfoId,
            t3.name             as      mktinfoName,
            t2.name             as      adInfoWebName,
            t1.channel          as      adInfoChannel,
            t1.ad_position      as      adInfoPosition,
            t5.name             as      adInfoPort,
            t9.name             as      adInfoPlatform,
            t1.platform_desc    as      adInfoPlatformDesc,
            t6.name             as      adInfoFlowType,
            t1.delivery_days    as      adInfoDeliveryDays,
            t1.delivery_times   as      adInfoDeliveryTimes,
            t4.name             as      adInfoState,
            t7.cid              as      mktLandInfoCID,
            t7.land_link        as      mktLandInfoLandUrl,
            t8.exposure_url     as      monitorExposureUrl,
            t8.click_url        as      monitorClickUrl
    from
    (
        select
            id,
            aid,
            web_name,
            channel,
            ad_position,
            port,
            platform,
            platform_desc,
            delivery_days,
            delivery_times,
            monitor_platform,
            state,
            flow_type,
            create_time
        from
            t_mkt_ad_info
        where
            id = ifnull(pMktId,id) and state = ifnull(pAdState,state) and web_name = ifnull(pAdWebName,web_name) and port = ifnull(pAdPort,port) and dept_type = ifnull(vType,dept_type)
        order by create_time desc
    )t1
    left outer join
    (
        select
            id,
            name
        from
            t_mkt_dic_info
        where
            type = 'web'
    )t2
    on t1.web_name = t2.id
    left outer join
    (
         select
            id,
            name
        from
            t_mkt_info
    )t3
    on t1.id = t3.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'ad_state'
    )t4
    on t1.state = t4.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'port'
    )t5
    on t1.port = t5.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'flow'
    )t6
    on t1.flow_type = t6.id
    left outer join
        t_mkt_land_info t7
    on t1.aid = t7.aid
    left outer join
        t_mkt_monitor_info t8
    on t1.aid = t8.aid
    left outer join
    (
        select
            id,
            name
        from
            t_mkt_dic_info
        where
            type = 'platform' and pid is null
    )t9
    on t1.platform = t9.id
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
    on t1.aid = t11.aid
	limit pFrom,pPerPage;
    
    -- 总数
    select 
        count(*) as total
    from
        t_mkt_ad_info t1
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
    
    on t1.aid = t11.aid
    where
         t1.id = ifnull(pMktId,t1.id) and t1.state = ifnull(pAdState,t1.state) and t1.web_name = ifnull(pAdWebName,t1.web_name) and t1.port = ifnull(pAdPort,t1.port) and t1.dept_type = ifnull(vType,t1.dept_type);
    
    
    
    else
    
    select
            t1.aid              as      adInfoId,
            t3.name             as      mktinfoName,
            t2.name             as      adInfoWebName,
            t1.channel          as      adInfoChannel,
            t1.ad_position      as      adInfoPosition,
            t5.name             as      adInfoPort,
            t9.name             as      adInfoPlatform,
            t1.platform_desc    as      adInfoPlatformDesc,
            t6.name             as      adInfoFlowType,
            t1.delivery_days    as      adInfoDeliveryDays,
            t1.delivery_times   as      adInfoDeliveryTimes,
            t4.name             as      adInfoState,
            t7.cid              as      mktLandInfoCID,
			t7.land_link        as      mktLandInfoLandUrl,
			t8.exposure_url     as      monitorExposureUrl,
			t8.click_url        as      monitorClickUrl
    from
    (
        select
            id,
            aid,
            web_name,
            channel,
            ad_position,
            port,
            platform,
            platform_desc,
            delivery_days,
            delivery_times,
            monitor_platform,
            state,
            flow_type,
            create_time
        from
            t_mkt_ad_info
        where
            id = ifnull(pMktId,id) and state = ifnull(pAdState,state) and web_name = ifnull(pAdWebName,web_name) and port = ifnull(pAdPort,port) and dept_type = ifnull(vType,dept_type)
        order by create_time desc
    )t1
    left outer join
    (
        select
            id,
            name
        from
            t_mkt_dic_info
        where
            type = 'web'
    )t2
    on t1.web_name = t2.id
    left outer join
    (
         select
            id,
            name
        from
            t_mkt_info
    )t3
    on t1.id = t3.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'ad_state'
    )t4
    on t1.state = t4.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'port'
    )t5
    on t1.port = t5.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'flow'
    )t6
    on t1.flow_type = t6.id
    left outer join
        t_mkt_land_info t7
    on t1.aid = t7.aid
    left outer join
        t_mkt_monitor_info t8
    on t1.aid = t8.aid
    left outer join
    (
        select
            id,
            name
        from
            t_mkt_dic_info
        where
            type = 'platform' and pid is null
    )t9
    on t1.platform = t9.id
	 limit pFrom,pPerPage;
    
    -- 总数
    select 
        count(*) as total
    from
        t_mkt_ad_info
    where
         id = ifnull(pMktId,id) and state = ifnull(pAdState,state) and web_name = ifnull(pAdWebName,web_name) and port = ifnull(pAdPort,port) and dept_type = ifnull(vType,dept_type);
    end if;
end;