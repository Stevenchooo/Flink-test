DELIMITER // 

DROP PROCEDURE IF EXISTS sp_adInfoExportQuery;

CREATE PROCEDURE sp_adInfoExportQuery (
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
    in pAdExportType                int,
    in plandPlatform           varchar(50)
)
proc: BEGIN
    declare vName        varchar(100);
    
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
    
    if(pAdState = -1)then
        set pAdState = null;
    end if;
    
    
    if(pAdWebName = -1)then
        set pAdWebName = null;
    end if;
    
    
    if(pAdPort = -1)then
        set pAdPort = null;   
    end if;
    
    if(plandPlatform = -1 || plandPlatform = '' ||  plandPlatform = '-1')then
        set plandPlatform = null;   
    end if;
    
    
    
    if(exists(select * from t_ms_model where name=pOperator and val REGEXP  '^ad')) then
    select
        t1.aid                                as      adInfoId,
        t11.name                              as      mktinfoName,
        t2.name                               as      adInfoWebName,
        t1.channel                            as      adInfoChannel,
        t1.ad_position                        as      adInfoPosition,
        t15.name                              as      materialType,
        t8.description                        as      materialDesc,
        if(t8.state =1,t8.show_name,'待录入') as      materialState,
        t5.name                               as      adInfoPort,
        t6.name                               as      adInfoPlatform,
        t1.platform_desc                      as      adInfoPlatformDesc,
        t1.delivery_days                      as      adInfoDeliveryDays,
        t1.delivery_times                     as      adInfoDeliveryTimes,
        t1.operator                           as      operator,
        t1.update_time                        as      updateTime,
        t14.name                              as      adInfoFlowType,
        t1.exp_amount                         as      expAmount,
        t1.click_amount                       as      clickAmount,
        t1.publish_price                      as      publishPrice,
        t1.net_price                          as      netPrice,
        t9.name                               as      adInfoResource,
        t16.name                              as      isExposure,
        t17.name                              as      isClick,
        t3.name                               as      monitorPlatform,
        t12.sid                               as      sid,
        t12.cps                               as      cpsName,
        t12.source                            as      source,
        t12.channel_name                      as      channelName,
        t12.channel                           as      channel,
        t12.cid                               as      cid,
        t12.land_link                         as      landUrl,
        t13.exposure_url                      as      monitorExposureUrl,
        t13.click_url                         as      monitorClickUrl,
        t13.bi_code                           as      biCode,
        t4.name                               as      adInfoState       
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
            id,
            name
        from t_mkt_dic_info
        where type = 'web'
    )t2
    on t1.web_name = t2.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'monitor_platform'
    )t3
    on t1.monitor_platform = t3.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'ad_state'
    )t4
    on t1.state = t4.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'port'
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
            type = 'land_platform'
    )t6
    on t1.platform = t6.id
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
    left outer join
    (
        select 
            aid,
            show_name,
            state,
            description
        from t_mkt_material_info
    )t8
    on t1.aid = t8.aid
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'material_state'
    )t10
    on t8.state = t10.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'resource'
    )t9
    on t1.resource = t9.id
    left outer join
    (
        select
            id     as  id,
            name   as  name
        from t_mkt_info          
    )t11
    on t1.id = t11.id
    left outer join
        t_mkt_land_info t12
    on t1.aid = t12.aid
    left outer join
    (
        select
            aid,
            bi_code,
            exposure_url,
            click_url
        from t_mkt_monitor_info          
    )t13
    on t1.aid = t13.aid
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'flow'
    )t14
    on t1.flow_type = t14.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'material'
    )t15
    on t1.material_type = t15.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'boolean'
    )t16
    on t1.is_exposure = t16.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'boolean'
    )t17
    on t1.is_click = t17.id
    order by t11.id desc,convert(adInfoWebName USING gbk) COLLATE gbk_chinese_ci,t1.aid desc,operator;
    
    select pAdExportType as adExportType from dual;
    
    
    
    else
         select
            t1.aid                                as      adInfoId,
            t11.name                              as      mktinfoName,
            t2.name                               as      adInfoWebName,
            t1.channel                            as      adInfoChannel,
            t1.ad_position                        as      adInfoPosition,
            t15.name                              as      materialType,
            t8.description                        as      materialDesc,
            if(t8.state =1,t8.show_name,'待录入') as      materialState,
            t5.name                               as      adInfoPort,
            t6.name                               as      adInfoPlatform,
            t1.platform_desc                      as      adInfoPlatformDesc,
            t1.delivery_days                      as      adInfoDeliveryDays,
            t1.delivery_times                     as      adInfoDeliveryTimes,
            t1.operator                           as      operator,
            t1.update_time                        as      updateTime,
            t14.name                              as      adInfoFlowType,
            t1.exp_amount                         as      expAmount,
            t1.click_amount                       as      clickAmount,
            t1.publish_price                      as      publishPrice,
            t1.net_price                          as      netPrice,
            t9.name                               as      adInfoResource,
            t16.name                              as      isExposure,
            t17.name                              as      isClick,
            t3.name                               as      monitorPlatform,
            t12.sid                               as      sid,
            t12.cps                               as      cpsName,
            t12.source                            as      source,
            t12.channel_name                      as      channelName,
            t12.channel                           as      channel,
            t12.cid                               as      cid,
            t12.land_link                         as      landUrl,
            t13.exposure_url                      as      monitorExposureUrl,
            t13.click_url                         as      monitorClickUrl,
            t13.bi_code                           as      biCode,
            t4.name                               as      adInfoState                
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
            id,
            name
        from t_mkt_dic_info
        where type = 'web'
    )t2
    on t1.web_name = t2.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'monitor_platform'
    )t3
    on t1.monitor_platform = t3.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'ad_state'
    )t4
    on t1.state = t4.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'port'
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
            type = 'land_platform'
    )t6
    on t1.platform = t6.id
    left outer join
    (
        select 
            aid,
            show_name,
            state,
            description
        from t_mkt_material_info
    )t8
    on t1.aid = t8.aid
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'material_state'
    )t10
    on t8.state = t10.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'resource'
    )t9
    on t1.resource = t9.id
    left outer join
    (
        select
            id     as  id,
            name   as  name
        from t_mkt_info          
    )t11
    on t1.id = t11.id
    left outer join
        t_mkt_land_info t12
    on t1.aid = t12.aid
    left outer join
    (
        select
            aid,
            bi_code,
            exposure_url,
            click_url
        from t_mkt_monitor_info          
    )t13
    on t1.aid = t13.aid
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'flow'
    )t14
    on t1.flow_type = t14.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'material'
    )t15
    on t1.material_type = t15.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'boolean'
    )t16
    on t1.is_exposure = t16.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from t_mkt_common_dic_info
        where type = 'boolean'
    )t17
    on t1.is_click = t17.id
    order by t11.id desc,convert(adInfoWebName USING gbk) COLLATE gbk_chinese_ci,t1.aid desc,operator;
    
    select pAdExportType as adExportType from dual;
    
    
    
    end if;
end

   //  
DELIMITER ;