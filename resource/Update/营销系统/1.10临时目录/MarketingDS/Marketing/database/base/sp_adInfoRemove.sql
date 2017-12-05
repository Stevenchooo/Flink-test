DROP PROCEDURE IF EXISTS sp_adInfoRemove;
CREATE PROCEDURE sp_adInfoRemove (
    out pRetCode            int,
    
    in pOperator           varchar(50),
    in adInfoId            int -- 活动ID
)
proc: BEGIN
   
    declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    if(not exists(select * from t_mkt_ad_info where aid = adInfoId and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
    end if;
    
    if(not exists(select * from t_mkt_ad_info where aid = adInfoId)) then
        set pRetCode = 2001;
        leave proc;
    end if;


    -- 删除数据
    delete from t_mkt_material_info where aid = adInfoId;
    delete from t_mkt_land_info where aid = adInfoId;
    delete from t_mkt_monitor_info where aid = adInfoId;
    delete from t_mkt_ad_info where aid = adInfoId;
       
    set pRetCode = 0;
end;