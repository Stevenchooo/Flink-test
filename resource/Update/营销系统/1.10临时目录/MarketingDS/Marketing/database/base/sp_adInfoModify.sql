DROP PROCEDURE IF EXISTS sp_adInfoModify;
CREATE PROCEDURE sp_adInfoModify (
    out pRetCode            int,
    
    in  pOperator              varchar(50),
    in  adInfoId               int,
    in  mktinfoId              int,
    in  adInfoMediaType        int,
    in  adInfoWeb              int,
    in  adInfoChannel          varchar(255),
    in  adInfoAdPosition       varchar(255),  
    in  adInfoAdMaterialType   varchar(255),
    in  adInfoAdMaterialDesc   varchar(2000),
    in  adInfoPort             varchar(255),  
    in  adInfoPlatform         int,
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
    in  adInfoMonitorPlatform  varchar(255) 
)
proc: BEGIN
	
	declare ad_state        int;
	
	declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    if(not exists(select * from t_mkt_ad_info where aid = adInfoId and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
    end if;
    
    
    if(exists(select 
                * 
              from 
                t_mkt_ad_info 
              where 
                id = mktinfoId  and  web_name = adInfoWeb and channel = adInfoChannel 
                and  ad_position = adInfoAdPosition and port = adInfoPort and aid != adInfoId)) then
        select 8801 into pRetCode;
        leave proc;
    end if; 
    
    SELECT 0 INTO pRetCode;
    
    select state into ad_state from t_mkt_ad_info where aid = adInfoId;
    
    
    
    if(ad_state = 0) then
        update t_mkt_ad_info set
	        id = ifnull(mktinfoId, id),
	        media_type = ifnull(adInfoMediaType,media_type),
	        web_name = ifnull(adInfoWeb, web_name), 
	        channel = ifnull(adInfoChannel, channel),
	        ad_position = ifnull(adInfoAdPosition, ad_position),
	        material_type = ifnull(adInfoAdMaterialType, material_type),
	        port = ifnull(adInfoPort, port),
	        platform = ifnull(adInfoPlatform, platform),
	        platform_desc = ifnull(adInfoPlatformDesc, platform_desc),
	        delivery_days = ifnull(adInfoDeliveryDays, delivery_days),
	        delivery_times = ifnull(adInfoDeliveryTimes, delivery_times),
	        flow_type = ifnull(adInfoFlowType, flow_type),
	        exp_amount = ifnull(adInfoExpAmount, exp_amount),
	        click_amount = ifnull(adInfoClickAmount, click_amount),
	        publish_price = ifnull(adInfoPublishPrice, publish_price),
	        net_price = ifnull(adInfoNetPrice, net_price),
	        resource = ifnull(adInfoResource, resource),
	        is_exposure = ifnull(adInfoIsExposure, is_exposure),
	        is_click = ifnull(adInfoIsClick, is_click),
	        monitor_platform = ifnull(adInfoMonitorPlatform, monitor_platform),
	        update_time = ifnull(now(), update_time)
	     where aid = adInfoId; 
	     
	    
   
	    if(exists(select  * from t_mkt_material_info where aid = adInfoId)) then
	      
	     update t_mkt_material_info set
	            description = ifnull(adInfoAdMaterialDesc,description),
	            update_time = now()
	         where aid = adInfoId;
	         
	    else
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
	    
	    end if;
    
	    if(row_count() <= 0) then
	     select 4 into pRetCode; -- 数据库错误
	        leave proc;
	    end if;
    
	     
        leave proc;
    end if;
    
    
    update t_mkt_ad_info set
        material_type = ifnull(adInfoAdMaterialType, material_type),
        delivery_days = ifnull(adInfoDeliveryDays, delivery_days),
        delivery_times = ifnull(adInfoDeliveryTimes, delivery_times),
        exp_amount = ifnull(adInfoExpAmount, exp_amount),
        click_amount = ifnull(adInfoClickAmount, click_amount),
        publish_price = ifnull(adInfoPublishPrice, publish_price),
        net_price = ifnull(adInfoNetPrice, net_price),
        resource = ifnull(adInfoResource, resource),
        is_exposure = ifnull(adInfoIsExposure, is_exposure),
        is_click = ifnull(adInfoIsClick, is_click),
        update_time = now()
     where aid = adInfoId; 
        
     
    if(exists(select  * from t_mkt_material_info where aid = adInfoId)) then
      
     update t_mkt_material_info set
	     	description = ifnull(adInfoAdMaterialDesc,description),
	     	update_time = now()
	     where aid = adInfoId;
	     
	else
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
	
	end if;
	     
	     

    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;
end;