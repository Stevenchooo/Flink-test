DROP PROCEDURE IF EXISTS sp_mktLandInfoQueryModifyContents;
CREATE PROCEDURE sp_mktLandInfoQueryModifyContents (
    out pRetCode            int,
    
    in pOperator            varchar(50),
    in mktLandInfoAid       int,
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
	
    select 
        t2.aid               as      mktLandInfoAid,
        t1.sid               as      mktLandInfoSID,
        t1.cps               as      mktLandInfoCPS,
        t1.source            as      mktLandInfoSource,
        t1.channel_name      as      mktLandInfoLandChannelName,
        t1.channel           as      mktLandInfoLandChannel,
        t1.cid               as      mktLandInfoCID,
        t1.land_link         as      mktLandInfoLandLink,
        t2.ad_position       as      adInfoPosition,
        t3.name              as      adInfoWebName,
        t4.name              as      mktinfoName,
        t5.name              as      adInfoPort,
        t2.channel           as      adInfoChannel
    from
        
        t_mkt_ad_info t2
    left outer join
        t_mkt_land_info t1 
    on  t1.aid = t2.aid
    left outer join
    (
        select
            id,
            name
        from
            t_mkt_dic_info
        where
            type = 'web'
    )t3
    
    on t2.web_name = t3.id
    left outer join
    (
        select
            id,
            name
        from
            t_mkt_info
    )t4
    on t2.id = t4.id
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
    on t2.port = t5.id
    where t2.aid = mktLandInfoAid  and t2.dept_type = ifnull(vType,t2.dept_type)
    limit pFrom,pPerPage;

end;