DROP PROCEDURE IF EXISTS sp_mktLandInfoQuery;
CREATE PROCEDURE sp_mktLandInfoQuery (
    out pRetCode            int,
    in pOperator            varchar(50),
    in pMktId               int,
    in pAdState             int,
    in pAdIsVmallPlat       int,
    in pAdWebName           int,
    in pAdPort              int,
    in pInputUser              varchar(50),
    in pAdInfoDeliveryBeginDay varchar(10),
    in pAdInfoDeliveryEndDay   varchar(10),
    in pAdqueryDateBeginDay    varchar(10),
    in pAdqueryDateEndDay      varchar(10),
    in pFrom                int,
    in pPerPage             int
)
proc: BEGIN
    
    declare vplatform        varchar(50);
    
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
    
    if(pAdIsVmallPlat = 1) then
        set vplatform = '0';
    end if;
    
    
    if(exists(select * from t_ms_model where name=pOperator and val REGEXP  '^ad')) then
    select 
        t2.aid               as      mktLandInfoAid,
        t1.id                as      mktId,
        t1.name              as      mktLandInfoName,
        t4.webName           as      mktLandInfoWebName,
        t2.channel           as      mktLandInfoChannel,
        t2.ad_position       as      mktLandInfoAdPosition,
        t5.portName          as      mktLandInfoPort,
        t2.platform_desc     as      mktLandInfoPlatformDesc,
        t6.flowName          as      mktLandInfoFlowType,
        t2.delivery_times    as      mktLandInfoDeliveryTimes,
        t7.adStateName       as      mktLandInfoState,
        t2.state             as      mktLandInfoStateId,
        t3.sid               as      mktLandInfoSID,
        t3.cps               as      mktLandInfoCPS,
        t3.source            as      mktLandInfoSource,
        t3.channel_name      as      mktLandInfoLandChannelName,
        t3.channel           as      mktLandInfoLandChannel,
        t3.cid               as      mktLandInfoCID,
        t3.land_link         as      mktLandInfoLandLink,
        t2.create_time       as      mktAdInfoCreateTime
    from
        (
            select 
                id, 
                name 
            from 
                t_mkt_info 
            where id = ifnull(pMktId, id) and dept_type = ifnull(vType,dept_type)
         ) t1
    left outer join
        t_mkt_ad_info t2
    on t1.id = t2.id
    left outer join
        t_mkt_land_info t3
    on t2.aid = t3.aid
    
    left outer join
    (
        select
            id,
            name as webName
        from
            t_mkt_dic_info
        where
            type = 'web'
    ) t4
    on t2.web_name = t4.id
    
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  portName
        from
            t_mkt_common_dic_info
        where
            type = 'port'
    ) t5
    on t2.port = t5.id
    
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  flowName
        from
            t_mkt_common_dic_info
        where
            type = 'flow'
    ) t6
    on t2.flow_type = t6.id
    
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  adStateName
        from
            t_mkt_common_dic_info
        where
            type = 'ad_state'
    ) t7
    on t2.state = t7.id
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
    )t8
    on t2.aid = t8.aid
    where 
        t2.operator=ifnull(pInputUser,t2.operator) 
        and t2.state = ifnull(pAdState, t2.state)
		  and if(pAdIsVmallPlat=0 ,t2.platform in ('1','2'),if(pAdIsVmallPlat=1,t2.platform='0',t2.platform in ('3','4','5')))  
        and t2.web_name = ifnull(pAdWebName,t2.web_name) 
        and t2.port = ifnull(pAdPort,t2.port) 
        and date_format(t2.update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(t2.update_time,'%Y%m%d')) 
        and date_format(t2.update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(t2.update_time,'%Y%m%d')) 
        and ((date_format(t2.delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(t2.delivery_begin_day,'%Y%m%d')) and ifnull(pAdInfoDeliveryEndDay,date_format(t2.delivery_begin_day,'%Y%m%d'))) or (date_format(t2.delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(t2.delivery_end_day,'%Y%m%d')) 
        and ifnull(pAdInfoDeliveryEndDay,date_format(t2.delivery_end_day,'%Y%m%d')))) 
        and t2.dept_type = ifnull(vType,t2.dept_type)
    order by mktId desc, convert(mktLandInfoWebName USING gbk) COLLATE gbk_chinese_ci, mktLandInfoAid desc
    limit pFrom,pPerPage;
    

    -- 总数
    select 
        count(*) as total
    from 
        t_mkt_info t1
    left outer join
        t_mkt_ad_info t2
    on t1.id = t2.id
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
    )t8
    on t2.aid = t8.aid
    where 
        t2.operator=ifnull(pInputUser,t2.operator) 
        and t1.id = ifnull(pMktId,t1.id) 
        and t1.dept_type = ifnull(vType,t1.dept_type)
        and t2.state = ifnull(pAdState, t2.state) 
        and if(pAdIsVmallPlat=0 ,t2.platform in ('1','2'),if(pAdIsVmallPlat=1,t2.platform='0',t2.platform in ('3','4','5')))  
        and t2.web_name = ifnull(pAdWebName,t2.web_name) 
        and t2.port = ifnull(pAdPort,t2.port) 
        and date_format(t2.update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(t2.update_time,'%Y%m%d')) 
        and date_format(t2.update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(t2.update_time,'%Y%m%d')) 
        and ((date_format(t2.delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(t2.delivery_begin_day,'%Y%m%d')) and ifnull(pAdInfoDeliveryEndDay,date_format(t2.delivery_begin_day,'%Y%m%d'))) or (date_format(t2.delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(t2.delivery_end_day,'%Y%m%d')) 
        and ifnull(pAdInfoDeliveryEndDay,date_format(t2.delivery_end_day,'%Y%m%d'))))
        and t2.dept_type = ifnull(vType,t2.dept_type)
    ;
    
    else
    
    select 
        t2.aid               as      mktLandInfoAid,
        t1.id                as      mktId,
        t1.name              as      mktLandInfoName,
        t4.webName           as      mktLandInfoWebName,
        t2.channel           as      mktLandInfoChannel,
        t2.ad_position       as      mktLandInfoAdPosition,
        t5.portName          as      mktLandInfoPort,
        t2.platform_desc     as      mktLandInfoPlatformDesc,
        t6.flowName          as      mktLandInfoFlowType,
        t2.delivery_times    as      mktLandInfoDeliveryTimes,
        t7.adStateName       as      mktLandInfoState,
        t2.state             as      mktLandInfoStateId,
        t3.sid               as      mktLandInfoSID,
        t3.cps               as      mktLandInfoCPS,
        t3.source            as      mktLandInfoSource,
        t3.channel_name      as      mktLandInfoLandChannelName,
        t3.channel           as      mktLandInfoLandChannel,
        t3.cid               as      mktLandInfoCID,
        t3.land_link         as      mktLandInfoLandLink,
        t2.create_time       as      mktAdInfoCreateTime
    from
        (
            select id, name from t_mkt_info where id = ifnull(pMktId, id) and dept_type = ifnull(vType,dept_type)) t1
    left outer join
        t_mkt_ad_info t2
    on t1.id = t2.id
    left outer join
        t_mkt_land_info t3
    on t2.aid = t3.aid
    
    left outer join
    (
        select
            id,
            name as webName
        from
            t_mkt_dic_info
        where
            type = 'web'
    ) t4
    on t2.web_name = t4.id
    
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  portName
        from
            t_mkt_common_dic_info
        where
            type = 'port'
    ) t5
    on t2.port = t5.id
    
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  flowName
        from
            t_mkt_common_dic_info
        where
            type = 'flow'
    ) t6
    on t2.flow_type = t6.id
    
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  adStateName
        from
            t_mkt_common_dic_info
        where
            type = 'ad_state'
    ) t7
    on t2.state = t7.id
    
    where t2.operator=ifnull(pInputUser,t2.operator) 
    and t2.state = ifnull(pAdState, t2.state) 
    and if(pAdIsVmallPlat=0 ,t2.platform in ('1','2'),if(pAdIsVmallPlat=1,t2.platform='0',t2.platform in ('3','4','5')))   
    and t2.web_name = ifnull(pAdWebName,t2.web_name) 
    and t2.port = ifnull(pAdPort,t2.port) 
    and date_format(t2.update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(t2.update_time,'%Y%m%d')) 
    and date_format(t2.update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(t2.update_time,'%Y%m%d')) 
    and ((date_format(t2.delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(t2.delivery_begin_day,'%Y%m%d')) 
    and ifnull(pAdInfoDeliveryEndDay,date_format(t2.delivery_begin_day,'%Y%m%d'))) or (date_format(t2.delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(t2.delivery_end_day,'%Y%m%d')) 
    and ifnull(pAdInfoDeliveryEndDay,date_format(t2.delivery_end_day,'%Y%m%d')))) 
    and t2.dept_type = ifnull(vType,t2.dept_type)
    order by mktId desc, convert(mktLandInfoWebName USING gbk) COLLATE gbk_chinese_ci, mktLandInfoAid desc
    limit pFrom,pPerPage;
    

    -- 总数
    select 
        count(*) as total
    from 
        t_mkt_info t1
    left outer join
        t_mkt_ad_info t2
    on t1.id = t2.id
    where t2.operator=ifnull(pInputUser,t2.operator) 
    and t1.id = ifnull(pMktId,t1.id) 
    and t1.dept_type = ifnull(vType,t1.dept_type)
    and t2.state = ifnull(pAdState, t2.state) 
    and if(pAdIsVmallPlat=0 ,t2.platform in ('1','2'),if(pAdIsVmallPlat=1,t2.platform='0',t2.platform in ('3','4','5')))    
    and t2.web_name = ifnull(pAdWebName,t2.web_name) 
    and t2.port = ifnull(pAdPort,t2.port) 
    and date_format(t2.update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(t2.update_time,'%Y%m%d')) 
    and date_format(t2.update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(t2.update_time,'%Y%m%d')) 
    and ((date_format(t2.delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(t2.delivery_begin_day,'%Y%m%d')) 
    and ifnull(pAdInfoDeliveryEndDay,date_format(t2.delivery_begin_day,'%Y%m%d'))) or (date_format(t2.delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(t2.delivery_end_day,'%Y%m%d')) 
    and ifnull(pAdInfoDeliveryEndDay,date_format(t2.delivery_end_day,'%Y%m%d')))) 
    and t2.dept_type = ifnull(vType,t2.dept_type)
    ;

   end if;
end