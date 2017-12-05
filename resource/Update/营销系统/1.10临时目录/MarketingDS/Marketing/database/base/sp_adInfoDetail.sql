DROP PROCEDURE IF EXISTS sp_adInfoDetail;

CREATE PROCEDURE sp_adInfoDetail (
    out pRetCode            int,
    
    in pOperator          varchar(50),
    in pAid               int

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
    
    
    select
        t1.aid                                as      detailsAdInfoId,
        t1.web_name                           as      detailsMktinfoNameId,
        t11.name                              as      detailsMktinfoName,
        t2.name                               as      detailsAdInfoWebName,
        t1.channel                            as      detailsAdInfoChannel,
        t1.ad_position                        as      detailsAdInfoPosition,
        t15.name                              as      detailsMaterialType,
        t1.material_type                      as      detailsMaterialTypeId,
        t8.description                        as      detailsMaterialDesc,
        if(t8.state =1,t8.show_name,'待录入')   as      detailsMaterialState,
        t5.name                               as      detailsAdInfoPort,
        t5.id                                 as      detailsAdInfoPortId,
        t6.name                               as      detailsAdInfoPlatform,
        t6.id                                 as      detailsAdInfoPlatformId,
        t1.platform_desc                      as      detailsAdInfoPlatformDesc,
        t1.delivery_days                      as      detailsAdInfoDeliveryDays,
        t1.delivery_times                     as      detailsAdInfoDeliveryTimes,
        t1.operator                           as      detailsOperator,
        t1.update_time                        as      detailsUpdateTime,
        t14.name                              as      detailsAdInfoFlowType,
        t1.flow_type                          as      detailsAdInfoFlowTypeId,
        t1.exp_amount                         as      detailsExpAmount,
        t1.click_amount                       as      detailsClickAmount,
        t1.publish_price                      as      detailsPublishPrice,
        t1.net_price                          as      detailsNetPrice,
        t9.name                               as      detailsAdInfoResource,
        t9.id                                 as      detailsAdInfoResourceId,
        t1.is_exposure                        as      detailsIsExposure,
        t1.is_click                           as      detailsIsClick,
        t16.name                              as      detailsIsExposureName,
        t17.name                              as      detailsIsClickName,
        t3.name                               as      detailsMonitorPlatform,
        t3.id                                 as      detailsMonitorPlatformId,
        t12.sid                               as      detailsSid,
        t12.cps                               as      detailsCpsName,
        t12.source                            as      detailsSource,
        t12.channel_name                      as      detailsChannelName,
        t12.channel                           as      detailsChannel,
        t12.cid                               as      detailsCid,
        t12.land_link                         as      detailsLandUrl,
        t13.exposure_url                      as      detailsMonitorExposureUrl,
        t13.click_url                         as      detailsMonitorClickUrl,
        t4.name                               as      detailsAdInfoState,
        t4.id                                 as      detailsAdInfoStateId,
        t1.media_type                         as      detailsAdInfoMediaType,
        t1.id                                 as      detailsmktinfoId
    from
    (
        select *
        from t_mkt_ad_info
        where aid = pAid
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
        from t_mkt_common_dic_info
        where type = 'land_platform'
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
    where
        t1.aid = pAid;
end