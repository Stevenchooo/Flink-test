DROP PROCEDURE IF EXISTS sp_monitorInfoDetail;

CREATE PROCEDURE sp_monitorInfoDetail (
    out pRetCode            int,
    
    in  pOperator          varchar(50),
    in  pAid               int

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
        bi_code         as      monitorBiCode,
        exposure_url    as      monitorExposureUrl,
        click_url       as      monitorClickUrl
    from
        t_mkt_monitor_info
    where
        aid = pAid;
end;