DELIMITER // 
DROP PROCEDURE IF EXISTS sp_mktReportQuery;

CREATE PROCEDURE sp_mktReportQuery (
    out pRetCode               int,
    in pOperator               varchar(50),
    in pMktId                  int,
    in pAdWebName              int,
    in pAdPort                 int,
    in pAdPlatform             int,
    in pInputUser              varchar(50),
    -- in pAdInfoDeliveryBeginDay varchar(10),
    -- in pAdInfoDeliveryEndDay   varchar(10),
    in pInputCid               varchar(200),
    in pAdqueryDateBeginDay    varchar(10),
    in pAdqueryDateEndDay      varchar(10),
    in pInputSid               varchar(200),
    in pType                   int,
    in pFrom                   int,
    in pPerPage                int
)
proc: BEGIN
    declare vName                varchar(100);
    
    declare vType           int;
    
    declare vCount           int;
    
    set vCount = -1;
    
    set vType = null;
    
    if(pAdWebName != -1 && pAdWebName != '-1') then
        select count(*) into vCount from t_mkt_ad_info where id = pMktId and web_name = pAdWebName;
        if ( vCount <= 0 ) then
            set pRetCode = 0;
            leave proc;
        end if ;
    end if;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    
    set pRetCode = 0;

    
    if(pInputUser = '' || pInputUser = -1)then
    
        set pInputUser = null;
        
    end if;
    
     if(pMktId = '' || pMktId = -1 || pMktId = '-1')then
    
        set pMktId = null;
        
    end if;
    
    if(pType = -1) then
      select
          deptType into pType
      from t_ms_user where account = pOperator; 
    end if;
    
    if(pInputSid = '' || pInputSid = -1)then
    
        set pInputSid = null;
        
    end if;
    
    if(pInputCid = '' || pInputCid = -1)then
    
        set pInputCid = null;
        
    end if;
    


    
    if(pAdqueryDateBeginDay = '')then
    
        set pAdqueryDateBeginDay = null;
  
    end if;
    
    if(pAdqueryDateEndDay = '')then
    
        set pAdqueryDateEndDay = null;
        
    end if;
    
    
    if(pAdPlatform = -1 || pAdPlatform = '-1' )then
    
        set pAdPlatform = null;
        
    end if;
    
    if(pAdWebName = -1 || pAdWebName = '-1')then
    
        set pAdWebName = null;
        
    end if;
    
    if(pAdPort = -1 || pAdPort = '-1')then
    
        set pAdPort = null;
        
    end if;

    if (pInputSid is not null && pType != vType && vType is not null) then
       set  pInputSid = 'YXS';
    end if; 
    
    /*if( pMktId is  null && pInputSid is null && pInputCid is null) then
      set  pMktId = -1;
      set  pInputSid = 'YXS';
      set pRetCode = 50001;
      leave proc; 
    end if;*/
    
    
    
    
    
            

    
    if(exists(select * from t_ms_model where name=pOperator and val REGEXP  '^ad')) then
    select 
        *
    from
    (
        select 
            *
        from
        (
            select
                t3.name             as      mktInfoName,
                t1.aid              as      adInfoId,
                t2.name             as      adInfoWebName,
                t1.channel          as      adInfoChannel,
                t1.ad_position      as      adInfoPosition,
                t5.name             as      adInfoPort,
                fk.name             as      platform,                
                t6.pt_d             as      reportDate,
                t4.sid              as      mktLandInfoSID,
                t9.cid              as      mktLandInfoCID,
                t6.bgPv             as      bgPv,
                t6.bgUv             as      bgUv,
                t6.djPv             as      djPv,
                t6.djUv             as      djUv,
                t6.landingUv        as      landingUv,
                t6.landingRate      as      landingRate,
                t6.dsp              as      dsp,
                t8.name             as      resource,
                t1.operator         as      operator,
                2                   as      orderNum,
                1                   as      orderNum2
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
                    create_time,
                    resource,
                    operator
                from
                    t_mkt_ad_info
                where
                    operator=ifnull(pInputUser,operator) 
                    and id = ifnull(pMktId,id) 
                    and web_name = ifnull(pAdWebName,web_name) 
                    and port = ifnull(pAdPort,port) 
                    and platform=ifnull(pAdPlatform,platform) 
                    -- and ((date_format(delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) 
                    -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (date_format(delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) 
                    -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
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
             t_mkt_info t3
            on t1.id = t3.id
            join
            (
                select
                    aid,
                    bi_code as  sid
                from
                    t_mkt_monitor_info
                where
                    bi_code = ifnull(pInputSid,bi_code)    
            )t4
            on t1.aid = t4.aid
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
                    type = 'resource'
                
            )t8
            on t1.resource = t8.id
            
            left outer join
            (
                select
                    dic_key     as  id,
                    dic_value   as  name
                from
                    t_mkt_common_dic_info
                where
                    type = 'land_platform'
                
            ) fk
            on t1.platform = fk.id
            
            join
            (
                select 
                    sid,
                    bg_pv                           as      bgPv,
                    bg_uv                           as      bgUv,
                    dj_pv                           as      djPv,
                    dj_uv                           as      djUv,
                    landing_uv                      as      landingUv,
                    if(dj_uv=0,0,format(100*landing_uv/dj_uv,2))  as      landingRate,
                    pt_d
                    ,if(isdsp=1,'详细','')          as       dsp
                from 
                    dw_honor_marketing_result_hm
                where
                   sid=ifnull(pInputSid,sid) and 
                   pt_d between ifnull(pAdqueryDateBeginDay,pt_d) and ifnull(pAdqueryDateEndDay,pt_d)
                   and cid=ifnull(pInputCid,cid)
            )t6
            on t4.sid = t6.sid
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
            )t7
            on t1.aid = t7.aid
            left outer join
            (
                select 
                    aid,
                    cid
                from t_mkt_land_info where cid=ifnull(pInputCid,cid)
            )t9
            on t1.aid = t9.aid
            
            union all
            
            select
                t.mktInfoName                                                  as      mktInfoName,
                '-'                                                            as      adInfoId,
                concat (t.adInfoWebName,'-总计')                               as      adInfoWebName,
                '-'                                                            as      adInfoChannel,
                '-'                                                            as      adInfoPosition,
                '-'                                                            as      adInfoPort,
                '-'                                                            as      platform,
                '-'                                                            as      reportDate,
                '-'                                                            as      mktLandInfoSID,
                '-'                                                            as      mktLandInfoCID,
                sum(t.bgPv)                                                    as      bgPv,
                sum(t.bgUv)                                                    as      bgUv,
                sum(t.djPv)                                                    as      djPv,
                sum(t.djUv)                                                    as      djUv,
                sum(t.landingUv)                                               as      landingUv,
                if(sum(t.djUv)=0,0,format(100*sum(t.landingUv)/sum(t.djUv),2)) as      landingRate,
                ''                                                            as      dsp,
                '-'                                                            as      resource,
                '-'                                                            as      operator,
                1                                                              as      orderNum,
                1                                                              as      orderNum2
            from
            (
                select
                    t3.name             as      mktInfoName,
                    t1.aid              as      adInfoId,
                    t2.name             as      adInfoWebName,
                    t1.channel          as      adInfoChannel,
                    t1.ad_position      as      adInfoPosition,
                    t5.name             as      adInfoPort,
                    fk.name             as      platform,
                    t6.pt_d             as      reportDate,
                    t4.sid              as      mktLandInfoSID,
                    t9.cid              as      mktLandInfoCID,
                    t6.bgPv             as      bgPv,
                    t6.bgUv             as      bgUv,
                    t6.djPv             as      djPv,
                    t6.djUv             as      djUv,
                    t6.landingUv        as      landingUv,
                    t6.landingRate      as      landingRate,
                    t6.dsp              as      dsp,
                    t8.name             as      resource,
                    t1.operator         as      operator
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
                        create_time,
                        resource,
                        operator
                    from
                        t_mkt_ad_info
                    where
                        operator=ifnull(pInputUser,operator) 
                        and id = ifnull(pMktId,id) 
                        and web_name = ifnull(pAdWebName,web_name) 
                        and port = ifnull(pAdPort,port) 
                        and platform=ifnull(pAdPlatform,platform) 
                        -- and ((date_format(delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) 
                        -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (date_format(delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) 
                        -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
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
                 t_mkt_info t3
                on t1.id = t3.id
                join
                (
                    select
                        aid,
                        bi_code as  sid
                    from
                        t_mkt_monitor_info
                    where bi_code=ifnull(pInputSid,bi_code)
                )t4
                on t1.aid = t4.aid
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
                        type = 'resource'
                    
                )t8
                on t1.resource = t8.id
                
                left outer join
               (
                select
                    dic_key     as  id,
                    dic_value   as  name
                from
                    t_mkt_common_dic_info
                where
                    type = 'land_platform'
                
               ) fk
               on t1.platform = fk.id
               
                join
                (
                    select 
                        sid,
                        bg_pv                           as      bgPv,
                        bg_uv                           as      bgUv,
                        dj_pv                           as      djPv,
                        dj_uv                           as      djUv,
                        landing_uv                      as      landingUv,
                        if(dj_uv=0,0,format(100*landing_uv/dj_uv,2))  as      landingRate,
                        pt_d
                        ,if(isdsp=1,'详细','')          as       dsp
                    from 
                        dw_honor_marketing_result_hm
                    where 
                       sid=ifnull(pInputSid,sid) and
                       pt_d between ifnull(pAdqueryDateBeginDay,pt_d) and ifnull(pAdqueryDateEndDay,pt_d)
                       and cid=ifnull(pInputCid,cid)
                )t6
                on t4.sid = t6.sid
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
                )t7
                on t1.aid = t7.aid
                left outer join
                (
                    select 
                        aid,
                        cid
                    from t_mkt_land_info where cid=ifnull(pInputCid,cid)
                )t9
                on t1.aid = t9.aid
            )t
            group by mktInfoName,adInfoWebName
        )t
        union all
        
        select
            t.mktInfoName                                                  as      mktInfoName,
            '-'                                                            as      adInfoId,
            '总计'                                                         as      adInfoWebName,
            '-'                                                            as      adInfoChannel,
            '-'                                                            as      adInfoPosition,
            '-'                                                            as      adInfoPort,
            '-'                                                            as      platform,
            '-'                                                            as      reportDate,
            '-'                                                            as      mktLandInfoSID,
            '-'                                                            as      mktLandInfoCID,
            sum(t.bgPv)                                                    as      bgPv,
            sum(t.bgUv)                                                    as      bgUv,
            sum(t.djPv)                                                    as      djPv,
            sum(t.djUv)                                                    as      djUv,
            sum(t.landingUv)                                               as      landingUv,
            if(sum(t.djUv)=0,0,format(100*sum(t.landingUv)/sum(t.djUv),2)) as      landingRate,
            ''                                                            as      dsp,
            '-'                                                            as      resource,
            '-'                                                            as      operator,
            0                                                              as      orderNum,
            0                                                              as      orderNum2
        from
        (
            select
                t3.name             as      mktInfoName,
                t1.aid              as      adInfoId,
                t2.name             as      adInfoWebName,
                t1.channel          as      adInfoChannel,
                t1.ad_position      as      adInfoPosition,
                t5.name             as      adInfoPort,
                fk.name             as      platform,
                t6.pt_d             as      reportDate,
                t4.sid              as      mktLandInfoSID,
                t9.cid              as      mktLandInfoCID,
                t6.bgPv             as      bgPv,
                t6.bgUv             as      bgUv,
                t6.djPv             as      djPv,
                t6.djUv             as      djUv,
                t6.landingUv        as      landingUv,
                t6.landingRate      as      landingRate,
                t6.dsp              as      dsp,
                t8.name             as      resource,
                t1.operator         as      operator
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
                    create_time,
                    resource,
                    operator
                from
                    t_mkt_ad_info
                where
                    operator=ifnull(pInputUser,operator) 
                    and id = ifnull(pMktId,id) 
                    and web_name = ifnull(pAdWebName,web_name) 
                    and port = ifnull(pAdPort,port) 
                    and platform=ifnull(pAdPlatform,platform) 
                    -- and ((date_format(delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) 
                    -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (date_format(delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) 
                    -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
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
             t_mkt_info t3
            on t1.id = t3.id
            join
            (
                select
                    aid,
                    bi_code as  sid
                from
                    t_mkt_monitor_info
                where
                    bi_code = ifnull(pInputSid,bi_code)
            )t4
            on t1.aid = t4.aid
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
                    type = 'resource'
                
            )t8
            on t1.resource = t8.id
            
            left outer join
            (
                select
                    dic_key     as  id,
                    dic_value   as  name
                from
                    t_mkt_common_dic_info
                where
                    type = 'land_platform'
                
            ) fk
            on t1.platform = fk.id
                        
            join
            (
                select 
                    sid,
                    bg_pv                           as      bgPv,
                    bg_uv                           as      bgUv,
                    dj_pv                           as      djPv,
                    dj_uv                           as      djUv,
                    landing_uv                      as      landingUv,
                    if(dj_uv=0,0,format(100*landing_uv/dj_uv,2))  as      landingRate,
                    pt_d
                    ,if(isdsp=1,'详细','')          as       dsp
                from 
                    dw_honor_marketing_result_hm
                where 
                   sid=ifnull(pInputSid,sid) and
                   pt_d between ifnull(pAdqueryDateBeginDay,pt_d) and ifnull(pAdqueryDateEndDay,pt_d)
                   and cid=ifnull(pInputCid,cid)
            )t6
            on t4.sid = t6.sid
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
            )t7
            on t1.aid = t7.aid
            left outer join
            (
                select 
                    aid,
                    cid
                from t_mkt_land_info where cid=ifnull(pInputCid,cid)
            )t9
            on t1.aid = t9.aid
        )t
        group by mktInfoName
    )t
    order by orderNum2 desc,convert(adInfoWebName USING gbk) COLLATE gbk_chinese_ci,orderNum,t.adInfoId desc,t.reportDate desc
    limit pFrom,pPerPage;
    
    -- 总数
    select 
        count(*) + count(distinct t1.web_name) + 1 as total
    from
        t_mkt_ad_info t1
     join
    (
        select
            tt1.sid,
            tt2.aid
        from
        (
	        select 
	            sid,
	            bg_uv                           as      flowExposureAmount,
	            dj_uv                           as      flowClickAmount,
	            landing_uv                      as      landingUv,
	            if(dj_uv=0,0,format(100*landing_uv/dj_uv,2))  as      landingRate,
	            pt_d
	            ,if(isdsp=1,'详细','')          as       dsp
	        from 
	            dw_honor_marketing_result_hm
	        where 
	           sid=ifnull(pInputSid,sid) and
	           pt_d between ifnull(pAdqueryDateBeginDay,pt_d) and ifnull(pAdqueryDateEndDay,pt_d)
	           and cid=ifnull(pInputCid,cid)
        )tt1
        join
        t_mkt_monitor_info tt2
        on tt1.sid = tt2.bi_code
    )t6
    on t1.aid = t6.aid
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
    )t7
    on t1.aid = t7.aid
    where
       operator=ifnull(pInputUser,operator) and id = ifnull(pMktId,id) and web_name = ifnull(pAdWebName,web_name) and port = ifnull(pAdPort,port) and platform=ifnull(pAdPlatform,platform) 
       -- and ((date_format(delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) 
       -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (date_format(delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) 
       -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
    ;
    else
    
    select 
        *
    from
    (
        select 
            *
        from
        (
            select
                t3.name             as      mktInfoName,
                t1.aid              as      adInfoId,
                t2.name             as      adInfoWebName,
                t1.channel          as      adInfoChannel,
                t1.ad_position      as      adInfoPosition,
                t5.name             as      adInfoPort,
                fk.name             as      platform,
                t6.pt_d             as      reportDate,
                t4.sid              as      mktLandInfoSID,
                t9.cid              as      mktLandInfoCID,
                t6.bgPv             as      bgPv,
                t6.bgUv             as      bgUv,
                t6.djPv             as      djPv,
                t6.djUv             as      djUv,
                t6.landingUv        as      landingUv,
                t6.landingRate      as      landingRate,
                t6.dsp              as      dsp,
                t8.name             as      resource,
                t1.operator         as      operator,
                2                   as      orderNum,
                1                   as      orderNum2
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
                    create_time,
                    resource,
                    operator
                from
                    t_mkt_ad_info
                where
                    operator=ifnull(pInputUser,operator) 
                    and id = ifnull(pMktId,id) 
                    and web_name = ifnull(pAdWebName,web_name) 
                    and port = ifnull(pAdPort,port) 
                    and platform=ifnull(pAdPlatform,platform) 
                    -- and ((date_format(delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) 
                    -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (date_format(delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) 
                    -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
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
             t_mkt_info t3
            on t1.id = t3.id
            join
            (
                select
                    aid,
                    bi_code as  sid
                from
                    t_mkt_monitor_info
                where
                    bi_code = ifnull(pInputSid,bi_code)    
            )t4
            on t1.aid = t4.aid
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
                    type = 'resource'
                
            )t8
            on t1.resource = t8.id
            
            left outer join
            (
                select
                    dic_key     as  id,
                    dic_value   as  name
                from
                    t_mkt_common_dic_info
                where
                    type = 'land_platform'
                
            ) fk
            on t1.platform = fk.id
                         
            join
            (
                 select 
                    sid,
                    bg_pv                           as      bgPv,
                    bg_uv                           as      bgUv,
                    dj_pv                           as      djPv,
                    dj_uv                           as      djUv,
                    landing_uv                      as      landingUv,
                    if(dj_uv=0,0,format(100*landing_uv/dj_uv,2))  as      landingRate,
                    pt_d
                    ,if(isdsp=1,'详细','')          as       dsp
                from 
                    dw_honor_marketing_result_hm
                where
                   sid=ifnull(pInputSid,sid) and 
                   pt_d between ifnull(pAdqueryDateBeginDay,pt_d) and ifnull(pAdqueryDateEndDay,pt_d)
                   and cid=ifnull(pInputCid,cid)
            )t6
            on t4.sid = t6.sid
            left outer join
            (
                select 
                    aid,
                    cid
                from t_mkt_land_info where cid=ifnull(pInputCid,cid)
            )t9
            on t1.aid = t9.aid   
            
            union all
            
            select
                t.mktInfoName                                                  as      mktInfoName,
                '-'                                                            as      adInfoId,
                concat (t.adInfoWebName,'-总计')                               as      adInfoWebName,
                '-'                                                            as      adInfoChannel,
                '-'                                                            as      adInfoPosition,
                '-'                                                            as      adInfoPort,
                '-'                                                            as      platform,
                '-'                                                            as      reportDate,
                '-'                                                            as      mktLandInfoSID,
                '-'                                                            as      mktLandInfoCID,
                sum(t.bgPv)                                                    as      bgPv,
                sum(t.bgUv)                                                    as      bgUv,
                sum(t.djPv)                                                    as      djPv,
                sum(t.djUv)                                                    as      djUv,
                sum(t.landingUv)                                               as      landingUv,
                if(sum(t.djUv)=0,0,format(100*sum(t.landingUv)/sum(t.djUv),2)) as      landingRate,
                ''                                                            as      dsp,
                '-'                                                            as      resource,
                '-'                                                            as      operator,
                1                                                              as      orderNum,
                1                                                              as      orderNum2
            from
            (
                select
                    t3.name             as      mktInfoName,
                    t1.aid              as      adInfoId,
                    t2.name             as      adInfoWebName,
                    t1.channel          as      adInfoChannel,
                    t1.ad_position      as      adInfoPosition,
                    t5.name             as      adInfoPort,
                    fk.name             as      platform,
                    t6.pt_d             as      reportDate,
                    t4.sid              as      mktLandInfoSID,
                    t9.cid              as      mktLandInfoCID,
                    t6.bgPv             as      bgPv,
                    t6.bgUv             as      bgUv,
                    t6.djPv             as      djPv,
                    t6.djUv             as      djUv,
                    t6.landingUv        as      landingUv,
                    t6.landingRate      as      landingRate,
                    t6.dsp              as      dsp,
                    t8.name             as      resource,
                    t1.operator         as      operator
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
                        create_time,
                        resource,
                        operator
                    from
                        t_mkt_ad_info
                    where
                        operator=ifnull(pInputUser,operator) 
                        and id = ifnull(pMktId,id) 
                        and web_name = ifnull(pAdWebName,web_name) 
                        and port = ifnull(pAdPort,port) 
                        and platform=ifnull(pAdPlatform,platform) 
                        -- and ((date_format(delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) 
                        -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (date_format(delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) 
                        -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
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
                 t_mkt_info t3
                on t1.id = t3.id
                join
                (
                    select
                        aid,
                        bi_code as  sid
                    from
                        t_mkt_monitor_info
                    where
                        bi_code = ifnull(pInputSid,bi_code)    
                )t4
                on t1.aid = t4.aid
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
                        type = 'resource'
                    
                )t8
                on t1.resource = t8.id
                
                left outer join
                (
                    select
                        dic_key     as  id,
                        dic_value   as  name
                    from
                        t_mkt_common_dic_info
                    where
                        type = 'land_platform'
                    
                ) fk
                on t1.platform = fk.id
                                
                join
                (
                     select 
                        sid,
                        bg_pv                           as      bgPv,
                        bg_uv                           as      bgUv,
                        dj_pv                           as      djPv,
                        dj_uv                           as      djUv,
                        landing_uv                      as      landingUv,
                        if(dj_uv=0,0,format(100*landing_uv/dj_uv,2))  as      landingRate,
                        pt_d
                        ,if(isdsp=1,'详细','')          as       dsp
                    from 
                        dw_honor_marketing_result_hm
                    where 
                       sid=ifnull(pInputSid,sid) and
                       pt_d between ifnull(pAdqueryDateBeginDay,pt_d) and ifnull(pAdqueryDateEndDay,pt_d)
                       and cid=ifnull(pInputCid,cid)
                )t6
                on t4.sid = t6.sid
                left outer join
                (
                    select 
                        aid,
                        cid
                    from t_mkt_land_info where cid=ifnull(pInputCid,cid)
                )t9
                on t1.aid = t9.aid
            )t
            group by mktInfoName,adInfoWebName
        )t
    
        union all
        
        select
            t.mktInfoName                                                  as      mktInfoName,
            '-'                                                            as      adInfoId,
            '总计'                                                         as      adInfoWebName,
            '-'                                                            as      adInfoChannel,
            '-'                                                            as      adInfoPosition,
            '-'                                                            as      adInfoPort,
            '-'                                                            as      platform,
            '-'                                                            as      reportDate,
            '-'                                                            as      mktLandInfoSID,
            '-'                                                            as      mktLandInfoCID,
            sum(t.bgPv)                                                    as      bgPv,
            sum(t.bgUv)                                                    as      bgUv,
            sum(t.djPv)                                                    as      djPv,
            sum(t.djUv)                                                    as      djUv,
            sum(t.landingUv)                                               as      landingUv,
            if(sum(t.djUv)=0,0,format(100*sum(t.landingUv)/sum(t.djUv),2)) as      landingRate,
            ''                                                            as      dsp,
            '-'                                                            as      resource,
            '-'                                                            as      operator,
            0                                                              as      orderNum,
            0                                                              as      orderNum2
        from
        (
            select
                t3.name             as      mktInfoName,
                t1.aid              as      adInfoId,
                t2.name             as      adInfoWebName,
                t1.channel          as      adInfoChannel,
                t1.ad_position      as      adInfoPosition,
                t5.name             as      adInfoPort,
                fk.name             as      platform,
                t6.pt_d             as      reportDate,
                t4.sid              as      mktLandInfoSID,
                t9.cid              as      mktLandInfoCID,
                t6.bgPv             as      bgPv,
                t6.bgUv             as      bgUv,
                t6.djPv             as      djPv,
                t6.djUv             as      djUv,
                t6.landingUv        as      landingUv,
                t6.landingRate      as      landingRate,
                t6.dsp              as      dsp,
                t8.name             as      resource,
                t1.operator         as      operator
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
                    create_time,
                    resource,
                    operator
                from
                    t_mkt_ad_info
                where
                    operator=ifnull(pInputUser,operator) 
                    and id = ifnull(pMktId,id) 
                    and web_name = ifnull(pAdWebName,web_name)
                    and port = ifnull(pAdPort,port) 
                    and platform=ifnull(pAdPlatform,platform) 
                    -- and ((date_format(delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) 
                    -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (date_format(delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) 
                    -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
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
             t_mkt_info t3
            on t1.id = t3.id
            join
            (
                select
                    aid,
                    bi_code as  sid
                from
                    t_mkt_monitor_info
                where
                    bi_code = ifnull(pInputSid,bi_code)    
            )t4
            on t1.aid = t4.aid
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
                    type = 'resource'
                
            )t8
            on t1.resource = t8.id
            
            left outer join
            (
                select
                    dic_key     as  id,
                    dic_value   as  name
                from
                    t_mkt_common_dic_info
                where
                    type = 'land_platform'
                
            ) fk
            on t1.platform = fk.id
                        
            join
            (
                 select 
                    sid,
                    bg_pv                           as      bgPv,
                    bg_uv                           as      bgUv,
                    dj_pv                           as      djPv,
                    dj_uv                           as      djUv,
                    landing_uv                      as      landingUv,
                    if(dj_uv=0,0,format(100*landing_uv/dj_uv,2))  as      landingRate,
                    pt_d
                    ,if(isdsp=1,'详细','')          as       dsp
                from 
                    dw_honor_marketing_result_hm
                where 
                   sid=ifnull(pInputSid,sid) and
                   pt_d between ifnull(pAdqueryDateBeginDay,pt_d) and ifnull(pAdqueryDateEndDay,pt_d)
                   and cid=ifnull(pInputCid,cid)
            )t6
            on t4.sid = t6.sid
            left outer join
            (
                select 
                    aid,
                    cid
                from t_mkt_land_info where cid=ifnull(pInputCid,cid)
            )t9
            on t1.aid = t9.aid
        )t
        group by mktInfoName
    )t
    order by orderNum2 desc,convert(adInfoWebName USING gbk) COLLATE gbk_chinese_ci,orderNum,t.adInfoId desc,t.reportDate desc
    limit pFrom,pPerPage;
    
    -- 总数
     select 
        count(*) + count(distinct t1.web_name) + 1 as total
    from
        t_mkt_ad_info t1
     join
    (
        select
            tt1.sid,
            tt2.aid
        from
        (
            select 
                sid,
                bg_uv                           as      flowExposureAmount,
                dj_uv                           as      flowClickAmount,
                landing_uv                      as      landingUv,
                if(dj_uv=0,0,format(100*landing_uv/dj_uv,2))  as      landingRate,
                pt_d
                ,if(isdsp=1,'详细','')          as       dsp
            from 
                dw_honor_marketing_result_hm
            where 
               sid=ifnull(pInputSid,sid) and
               pt_d between ifnull(pAdqueryDateBeginDay,pt_d) and ifnull(pAdqueryDateEndDay,pt_d)
               and cid=ifnull(pInputCid,cid)
        )tt1
        join
        t_mkt_monitor_info tt2
        on tt1.sid = tt2.bi_code
    )t6
    on t1.aid = t6.aid
    where
      operator=ifnull(pInputUser,operator) 
      and id = ifnull(pMktId,id) 
      and web_name = ifnull(pAdWebName,web_name) 
      and port = ifnull(pAdPort,port) 
      and platform=ifnull(pAdPlatform,platform) 
      -- and ((date_format(delivery_begin_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_begin_day,'%Y%m%d')) 
      -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_begin_day,'%Y%m%d'))) or (date_format(delivery_end_day,'%Y%m%d') between ifnull(pAdInfoDeliveryBeginDay,date_format(delivery_end_day,'%Y%m%d')) 
      -- and ifnull(pAdInfoDeliveryEndDay,date_format(delivery_end_day,'%Y%m%d'))))
      and dept_type = ifnull(vType,dept_type)
   ;
    
    end if;
    
    
    
   end
   
   //  
DELIMITER ;
