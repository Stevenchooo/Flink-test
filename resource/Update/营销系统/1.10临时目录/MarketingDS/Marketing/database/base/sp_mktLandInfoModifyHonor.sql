DROP PROCEDURE IF EXISTS sp_mktLandInfoModifyHonor;
CREATE PROCEDURE sp_mktLandInfoModifyHonor (
    out pRetCode            int,
    
    in pOperator                       varchar(50),
    in pLandInfoAid                    int,
    in pLandInfoLandLink               varchar(255) 
)
proc: BEGIN
	
	declare autoCid                   varchar(255);
	
    declare vType           int;

    set vType = null;
    
    --  判断用户有没有权限
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    if(not exists(select * from t_mkt_ad_info where aid = pLandInfoAid and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
    end if;
    
	--  获取广告位对应的属性
	select dept_type into vType from t_mkt_ad_info where aid = pLandInfoAid;
	
	select concat(pLandInfoAid,UNIX_TIMESTAMP()) into autoCid;
	
	
    if(not exists(select 
                * 
              from 
                t_mkt_land_info 
              where 
                aid = pLandInfoAid)) then
    insert into t_mkt_land_info (
        aid,
        cid,
        land_link,
        create_time,
        update_time,
        operator,
        dept_type
        )
    values (
        pLandInfoAid,
        autoCid,
        pLandInfoLandLink,
        now(),
        now(),
        pOperator,
        vType
        );
    SELECT 0 INTO pRetCode;
    end if; 
    
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if; 

    
    SELECT 0 INTO pRetCode;
    
    update t_mkt_land_info set
      
        cid = autoCid,
        land_link = ifnull(pLandInfoLandLink, land_link),
        update_time = ifnull(now(),update_time)
     where aid = pLandInfoAid; 
    

    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;
end;