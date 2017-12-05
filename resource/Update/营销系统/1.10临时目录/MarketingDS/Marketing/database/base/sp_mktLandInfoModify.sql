DROP PROCEDURE IF EXISTS `sp_mktLandInfoModify`;
DELIMITER //
CREATE PROCEDURE sp_mktLandInfoModify (
    out pRetCode                       int,
    
    in pOperator                       varchar(50),
    in pLandInfoAid                    int,
    in pLandInfoSID                    varchar(255),
    in pLandInfoCPS                    varchar(255),
    in pLandInfoSource                 varchar(255),
    in pLandInfoLandChannelName        varchar(255),
    in pLandInfoLandChannel            varchar(255),
    in pLandInfoCID                    varchar(255),
    in pLandInfoLandLink               varchar(255) 
)
proc: BEGIN
	
	declare vType           int;

    set vType = null;
    
    --  检查权限
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    if(not exists(select * from t_mkt_ad_info where aid = pLandInfoAid and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
    end if;
    
    
    --  获取类型
    select dept_type into vType from t_mkt_ad_info where aid = pLandInfoAid;
    
    if(not exists(select 
                * 
              from 
                t_mkt_land_info 
              where 
                aid = pLandInfoAid)) then
    insert into t_mkt_land_info (
        aid,
        sid, 
        cps,
        source,
        channel_name,
        channel,
        cid,
        land_link,
        create_time,
        update_time,
        operator,
        dept_type
        )
    values (
        pLandInfoAid,
        pLandInfoSID,
        pLandInfoCPS,
        pLandInfoSource,
        pLandInfoLandChannelName,
        pLandInfoLandChannel,
        pLandInfoCID,
        pLandInfoLandLink,
        now(),
        now(),
        pOperator,
        vType
        );
    SELECT 0 INTO pRetCode;
    
    end if;
    
    SELECT 0 INTO pRetCode;
    
    update t_mkt_land_info set
        sid = pLandInfoSID, 
        cps = pLandInfoCPS,
        source = pLandInfoSource,
        channel_name = pLandInfoLandChannelName,
        channel = pLandInfoLandChannel,
        cid = ifnull(pLandInfoCID, cid),
        land_link = ifnull(pLandInfoLandLink, land_link),
        update_time = ifnull(now(),update_time)
     where aid = pLandInfoAid; 
    

    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;
end
//  
DELIMITER ;