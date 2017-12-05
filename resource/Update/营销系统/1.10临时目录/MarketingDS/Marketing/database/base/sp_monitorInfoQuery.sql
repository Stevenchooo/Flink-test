

DELIMITER // 
DROP PROCEDURE IF EXISTS sp_monitorInfoQuery;
CREATE PROCEDURE sp_monitorInfoQuery (
    out pRetCode            int,
    in pOperator            varchar(50),
    in pMktId               int,
    in pAdState             int,
    in pMonitorPlatform     int,
    in pAdWebName           int,
    in pAdPort              int,
    in pInputUser              varchar(50),
    in plandFont               varchar(50),
    in pAdqueryDateBeginDay    varchar(10),
    in pAdqueryDateEndDay      varchar(10),
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

    if(pInputUser = '' || pInputUser = '-1')then
    
        set pInputUser = null;
        
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
    
    if(plandFont = '' || plandFont = '-1' || plandFont = -1)then
	    
	        set plandFont = null;
	        
	    end if;
    
    if(exists(select * from t_ms_model where name=pOperator and val REGEXP  '^ad')) then
    
     select
            t1.aid              as      adInfoId,
            t2.name             as      adInfoWebName,
            t1.channel          as      adInfoChannel,
            t1.ad_position      as      adInfoPosition,
            t5.name             as      adInfoPort,
            t6.name             as      adInfoPlatform,
            t1.platform_desc    as      adInfoPlatformDesc,
            t1.delivery_times   as      adInfoDeliveryTimes,
            t3.name             as      adInfoMonitorPlatform,
            t4.name             as      adInfoState,
            t7.bi_code          as      monitorBiCode,
            t7.exposure_url     as      monitorExposureUrl,
            t7.click_url        as      monitorClickUrl,
            t8.name             as      adInfoFlowType,
            t9.name             as      mktLandInfoName,
            t1.state            as      adInfoStateId,
            t10.cid             as      cid,
            t10.land_link       as      landLink
            
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
            delivery_times,
            monitor_platform,
            state,
            flow_type,
            create_time,
            update_time,
            operator
        from
            t_mkt_ad_info
        where
            operator=ifnull(pInputUser,operator) 
            and id = ifnull(pMktId,id) 
            and state = ifnull(pAdState,state) 
            and monitor_platform = ifnull(pMonitorPlatform,monitor_platform) 
            and web_name = ifnull(pAdWebName,web_name) and port = ifnull(pAdPort,port) 
            and date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d')) 
            and date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) 
            and platform = ifnull(plandFont,platform)
            and dept_type = ifnull(vType,dept_type)
        
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
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'monitor_platform'
    )t3
    on t1.monitor_platform = t3.id
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
            id,
            name
        from
            t_mkt_dic_info
        where
            type = 'platform' and pid is null
    )t6
    on t1.platform = t6.id
    left outer join
    (
        select
            aid,
            bi_code,
            exposure_url,
            click_url
        from
            t_mkt_monitor_info
    )t7
    on t1.aid = t7.aid
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'flow'
    )t8
    on t1.flow_type = t8.id
    left outer join
    (
        select
            id,
            name
        from
            t_mkt_info
    )t9
    on t1.id = t9.id
    left outer join
        t_mkt_land_info t10
    on t1.aid = t10.aid
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
    order by t9.id desc, convert(t2.name USING gbk) COLLATE gbk_chinese_ci,t1.aid desc 
    limit pFrom,pPerPage;
    
    -- 总数
    select 
        count(*) as total
    from
        t_mkt_ad_info t1
     left outer join
        t_mkt_land_info t10
    on t1.aid = t10.aid
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
         t1.operator=ifnull(pInputUser,t1.operator) 
         and  t1.id = ifnull(pMktId,t1.id) 
         and t1.state = ifnull(pAdState,t1.state) 
         and monitor_platform = ifnull(pMonitorPlatform,monitor_platform) 
         and web_name = ifnull(pAdWebName,web_name) 
         and port = ifnull(pAdPort,port) 
         and date_format(t1.update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(t1.update_time,'%Y%m%d')) 
         and date_format(t1.update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(t1.update_time,'%Y%m%d')) 
         and platform = ifnull(plandFont,platform) 
         and t1.dept_type = ifnull(vType,t1.dept_type)
       ;
    
    else
    
    select
            t1.aid              as      adInfoId,
            t2.name             as      adInfoWebName,
            t1.channel          as      adInfoChannel,
            t1.ad_position      as      adInfoPosition,
            t5.name             as      adInfoPort,
            t6.name             as      adInfoPlatform,
            t1.platform_desc    as      adInfoPlatformDesc,
            t1.delivery_times   as      adInfoDeliveryTimes,
            t3.name             as      adInfoMonitorPlatform,
            t4.name             as      adInfoState,
            t7.bi_code          as      monitorBiCode,
            t7.exposure_url     as      monitorExposureUrl,
            t7.click_url        as      monitorClickUrl,
            t8.name             as      adInfoFlowType,
            t9.name             as      mktLandInfoName,
            t1.state            as      adInfoStateId,
            t10.cid             as      cid,
            t10.land_link       as      landLink
            
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
            delivery_times,
            monitor_platform,
            state,
            flow_type,
            create_time,
            update_time,
            operator
        from
            t_mkt_ad_info
        where
            operator=ifnull(pInputUser,operator) 
            and id = ifnull(pMktId,id) 
            and state = ifnull(pAdState,state) 
            and monitor_platform = ifnull(pMonitorPlatform,monitor_platform) 
            and web_name = ifnull(pAdWebName,web_name) 
            and port = ifnull(pAdPort,port) 
            and date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d')) 
            and date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) 
            and platform = ifnull(plandFont,platform)       
            and dept_type = ifnull(vType,dept_type)
        
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
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'monitor_platform'
    )t3
    on t1.monitor_platform = t3.id
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
            id,
            name
        from
            t_mkt_dic_info
        where
            type = 'platform' and pid is null
    )t6
    on t1.platform = t6.id
    left outer join
    (
        select
            aid,
            bi_code,
            exposure_url,
            click_url
        from
            t_mkt_monitor_info
    )t7
    on t1.aid = t7.aid
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'flow'
    )t8
    on t1.flow_type = t8.id
    left outer join
    (
        select
            id,
            name
        from
            t_mkt_info
    )t9
    on t1.id = t9.id
    left outer join
        t_mkt_land_info t10
    on t1.aid = t10.aid
    order by t9.id desc,convert(t2.name USING gbk) COLLATE gbk_chinese_ci,t1.aid desc 
    limit pFrom,pPerPage;
    
    -- 总数
    select 
        count(*) as total
    from
        t_mkt_ad_info t1
     left outer join
        t_mkt_land_info t10
    on t1.aid = t10.aid
    where
         t1.operator=ifnull(pInputUser,t1.operator) 
         and  t1.id = ifnull(pMktId,t1.id) 
         and t1.state = ifnull(pAdState,t1.state) 
         and monitor_platform = ifnull(pMonitorPlatform,monitor_platform) 
         and web_name = ifnull(pAdWebName,web_name) 
         and port = ifnull(pAdPort,port) 
         and date_format(t1.update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(t1.update_time,'%Y%m%d')) 
         and date_format(t1.update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(t1.update_time,'%Y%m%d')) 
         and platform = ifnull(plandFont,platform) 
         and t1.dept_type = ifnull(vType,t1.dept_type)
       ;   
    end if;    
end

	   //
	DELIMITER ;