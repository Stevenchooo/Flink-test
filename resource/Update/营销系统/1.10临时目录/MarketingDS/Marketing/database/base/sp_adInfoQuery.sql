DELIMITER // 
DROP PROCEDURE IF EXISTS sp_adInfoQuery;
CREATE PROCEDURE sp_adInfoQuery (
	    out pRetCode            int,
	    in pOperator            varchar(50),
	    in pMktId               int,
	    in pAdState             int,
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
	        t1.aid                                as      adInfoId,
	        t2.name                               as      adInfoWebName,
	        t1.channel                            as      adInfoChannel,
	        t1.ad_position                        as      adInfoPosition,
	        t5.name                               as      adInfoPort,
	        t6.name                               as      adInfoPlatform,
	        t1.platform_desc                      as      adInfoPlatformDesc,
	        t1.delivery_days                      as      adInfoDeliveryDays,
	        t1.delivery_times                     as      adInfoDeliveryTimes,
	        t3.name                               as      adInfoMonitorPlatform,
	        t4.name                               as      adInfoState,
	        t1.state                              as      adInfoStateId,
	        t1.update_time                        as      updateTime,
	        if(t8.state =1,t8.show_name,'待录入') as       materialState,
	        t8.state                              as      materialStateId,
	        t8.type                               as      materialType,
	        t9.name                               as      adInfoResource,
	        t1.operator                           as      operator,
	        t11.name                              as      mktinfoName,
	        t8.operator                           as      materialOperator
	        
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
	            update_time,
	            resource,
	            operator
	        from
	            t_mkt_ad_info
	        where
	           operator=ifnull(pInputUser,operator) and id = ifnull(pMktId,id) and web_name = ifnull(pAdWebName,web_name) 
	           and port = ifnull(pAdPort,port) and state = ifnull(pAdState,state) and 
	           date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d')) and 
	           date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) and 
	           platform = ifnull(plandFont,platform)
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
	            show_name,
	            state,
	            type,
	            operator
	        from t_mkt_material_info
	    )t8
	    on t1.aid = t8.aid
	    left outer join
	    (
	        select
	            dic_key     as  id,
	            dic_value   as  name
	        from
	            t_mkt_common_dic_info
	        where
	            type = 'material_state'
	    )t10
	    on t8.state = t10.id
	    left outer join
	    (
	        select
	            dic_key     as  id,
	            dic_value   as  name
	        from
	            t_mkt_common_dic_info
	        where
	            type = 'resource'
	    )t9
	    on t1.resource = t9.id
	    left outer join
	    (
	        select
	            id     as  id,
	            name   as  name
	        from
	            t_mkt_info          
	    )t11
	    on t1.id = t11.id
	    order by t11.id desc,convert(adInfoWebName USING gbk) COLLATE gbk_chinese_ci,t1.aid desc,operator
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
	    )t7
	    on t1.aid = t7.aid
	    left outer join
	    (
	        select 
	            aid,
	            show_name,
	            state,
	            type
	        from t_mkt_material_info
	    )t8
	    on t1.aid = t8.aid
	    left outer join
	    (
	        select
	            dic_key     as  id,
	            dic_value   as  name
	        from
	            t_mkt_common_dic_info
	        where
	            type = 'material_state'
	    )t10
	    on t8.state = t10.id
	    left outer join
	    (
	        select
	            dic_key     as  id,
	            dic_value   as  name
	        from
	            t_mkt_common_dic_info
	        where
	            type = 'resource'
	    )t9
	    on t1.resource = t9.id
	    left outer join
	    (
	        select
	            id     as  id,
	            name   as  name
	        from
	            t_mkt_info          
	    )t11
	    on t1.id = t11.id
	    where
	        t1.operator=ifnull(pInputUser,t1.operator) and 
	        t1.id = ifnull(pMktId,t1.id) and 
	        t1.web_name = ifnull(pAdWebName,t1.web_name) and 
	        t1.port = ifnull(pAdPort,t1.port) and 
	        t1.state = ifnull(pAdState,t1.state) and 
	        date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d')) and 
	        date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) and 
	        platform = ifnull(plandFont,platform) and
	        t1.dept_type = ifnull(vType,t1.dept_type)
	                ; 
	    else
	         select
	            t1.aid                                as      adInfoId,
	            t2.name                               as      adInfoWebName,
	            t1.channel                            as      adInfoChannel,
	            t1.ad_position                        as      adInfoPosition,
	            t5.name                               as      adInfoPort,
	            t6.name                               as      adInfoPlatform,
	            t1.platform_desc                      as      adInfoPlatformDesc,
	            t1.delivery_days                      as      adInfoDeliveryDays,
	            t1.delivery_times                     as      adInfoDeliveryTimes,
	            t3.name                               as      adInfoMonitorPlatform,
	            t4.name                               as      adInfoState,
	            t1.state                              as      adInfoStateId,
	            t1.update_time                        as      updateTime,
	            if(t8.state =1,t8.show_name,'待录入') as      materialState,
	            t8.state                              as      materialStateId,
	            t8.type                               as      materialType,
	            t9.name                               as      adInfoResource,
	            t1.operator                           as      operator,
	            t11.name                              as      mktinfoName,
	        	t8.operator                           as      materialOperator
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
	            update_time,
	            resource,
	            operator
	        from
	            t_mkt_ad_info
	        where
	            operator=ifnull(pInputUser,operator) and 
	            id = ifnull(pMktId,id) and web_name = ifnull(pAdWebName,web_name) and 
	            port = ifnull(pAdPort,port) and state = ifnull(pAdState,state) and 
	            date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d')) and 
	            date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) and 
	            platform = ifnull(plandFont,platform) and
	            dept_type = ifnull(vType,dept_type)
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
	            type,
	            operator
	        from t_mkt_material_info
	    )t8
	    on t1.aid = t8.aid
	    left outer join
	    (
	        select
	            dic_key     as  id,
	            dic_value   as  name
	        from
	            t_mkt_common_dic_info
	        where
	            type = 'material_state'
	    )t10
	    on t8.state = t10.id
	    left outer join
	    (
	        select
	            dic_key     as  id,
	            dic_value   as  name
	        from
	            t_mkt_common_dic_info
	        where
	            type = 'resource'
	    )t9
	    on t1.resource = t9.id
	    left outer join
	    (
	        select
	            id     as  id,
	            name   as  name
	        from
	            t_mkt_info          
	    )t11
	    on t1.id = t11.id
	    order by t11.id desc,convert(adInfoWebName USING gbk) COLLATE gbk_chinese_ci,t1.aid desc,operator
	    limit pFrom,pPerPage;
	    
	    -- 总数
	    select 
	        count(*) as total
	    from
	        t_mkt_ad_info t1
	    where
	         operator=ifnull(pInputUser,operator) and 
	         id = ifnull(pMktId,id) and web_name = ifnull(pAdWebName,web_name) and 
	         port = ifnull(pAdPort,port) and state = ifnull(pAdState,state) and 
	         date_format(update_time,'%Y%m%d')>= ifnull(pAdqueryDateBeginDay,date_format(update_time,'%Y%m%d')) and 
	         date_format(update_time,'%Y%m%d')<= ifnull(pAdqueryDateEndDay,date_format(update_time,'%Y%m%d')) and 
	         platform = ifnull(plandFont,platform) and
	         dept_type = ifnull(vType,dept_type)
	    ;
	         end if;
	    
	    
	    
	   end
	   
	   //
	DELIMITER ;