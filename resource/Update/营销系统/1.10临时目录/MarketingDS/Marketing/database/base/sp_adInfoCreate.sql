DROP PROCEDURE IF EXISTS sp_adInfoCreate;
CREATE PROCEDURE sp_adInfoCreate (
    out pRetCode            int,
    
	in  pOperator              varchar(50),
	in  mktinfoId              int,
	in  adInfoMediaType        int,
	in  adInfoWeb              int,
	in  adInfoChannel          varchar(255),
	in  adInfoAdPosition       varchar(255),  
	in  adInfoAdMaterialType   varchar(255),  
	in  adInfoPort             varchar(255),  
	in  adInfoPlatform         varchar(255),
	in  adInfoPlatformDesc     varchar(255),  
	in  adInfoDeliveryDays     int,
	in  adInfoDeliveryTimes    varchar(255),  
	in  adInfoFlowType         varchar(255),
	in  adInfoExpAmount        int,
	in  adInfoClickAmount      int,
	in  adInfoPublishPrice     float,  
	in  adInfoNetPrice         float,  
	in  adInfoResource         varchar(255),  
	in  adInfoIsExposure       int,
	in  adInfoIsClick          int,
	in  adInfoMonitorPlatform  varchar(255),
	in  adInfoAdMaterialDesc   varchar(2048),
	in  adInfoDeliveryBeginTime varchar(255),
	in  adInfoDeliveryEndTime   varchar(255)
   
)
proc: BEGIN
	
	declare adInfoId                      int;
	declare vType                    int;
     
     select deptType into vType from t_ms_user where account = pOperator;
     
    if(exists(
            select 
                * 
            from 
                t_mkt_ad_info 
            where 
                id=mktinfoId and web_name = adInfoWeb 
                and channel = adInfoChannel and ad_position = adInfoAdPosition 
                and port = adInfoPort
               )
        ) then
        select 8801 into pRetCode;
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;

    --  获取属性
    select dept_type into vType from t_mkt_info where id = mktinfoId;

    
    --  插入对应的记录
    insert into t_mkt_ad_info (
        id,
		media_type,
		web_name,
		channel,
		ad_position,
		material_type,
		port,
		platform,
		platform_desc,
		delivery_days,
		delivery_times,
		flow_type,
		exp_amount,
		click_amount,
		publish_price,
		net_price,
		resource,
		is_exposure,
		is_click,
		monitor_platform,
		state,
		create_time,
		update_time,
		operator,
		delivery_begin_day,
		delivery_end_day,
		dept_type
        )
    values (
        mktinfoId,
        adInfoMediaType,
        adInfoWeb,
        adInfoChannel,
        adInfoAdPosition,
		adInfoAdMaterialType,
		adInfoPort,
		adInfoPlatform,
		adInfoPlatformDesc,
		adInfoDeliveryDays,
		adInfoDeliveryTimes,
		adInfoFlowType,
		adInfoExpAmount,
		adInfoClickAmount,
		adInfoPublishPrice,
		adInfoNetPrice,
		adInfoResource,
		adInfoIsExposure,
		adInfoIsClick,
		adInfoMonitorPlatform,
		0,
		now(),
		now(),
		pOperator,
		adInfoDeliveryBeginTime,
		adInfoDeliveryEndTime,
		vType
        );
    
        select 
            aid into adInfoId
        from 
            t_mkt_ad_info 
        where 
            id=mktinfoId and web_name = adInfoWeb 
            and channel = adInfoChannel and ad_position = adInfoAdPosition 
            and port = adInfoPort;
            
       insert into t_mkt_material_info ( 
       aid,
       description,
       operator,
       create_time,
       update_time,
       state
       )
       values(
       adInfoId,
       adInfoAdMaterialDesc,
       pOperator,
       now(),
       now(),
       0
       );
       
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
end;