DELIMITER // 

DROP PROCEDURE IF EXISTS sp_adInfoNumQuery ;

CREATE PROCEDURE sp_adInfoNumQuery  (
 out pRetCode             int,
 
 in  pOperator            varchar(50),
    in  mktinfoId            int,
    in  adState              int
   
)
proc: BEGIN

    declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
   SELECT 0 INTO pRetCode;
    
    
     if(mktinfoId is null) then
        select 100 into pRetCode;
        leave proc;
    end if;
    
    if(adState is null) then
        select 100 into pRetCode;
        leave proc;
    end if;
    
        select 
            t1.adCount as totalCount,
            t2.adLandInfoCount as vmallCount,
            t3.adLandHonorCount as honorCount,
            t4.adLandHuaweiCount    as  huaweiCount
        from
        (
            select 
                count(aid) as adCount
            from t_mkt_ad_info
            where id = mktinfoId
                and state = adState
                and dept_type = ifnull(vType,dept_type)
        )t1
        left outer join
        (
            select 
                count(aid) as adLandInfoCount
            from t_mkt_ad_info
            where id = mktinfoId
                and state = adState
                and platform = 0
                and dept_type = ifnull(vType,dept_type)
        )t2
        on 1 = 1
        left outer join
        (
            select 
                count(aid) as adLandHonorCount
            from t_mkt_ad_info
            where id = mktinfoId
                and state = adState
                and platform = 1
                and dept_type = ifnull(vType,dept_type)
        )t3
        on 1 = 1
         left outer join
        (
            select 
                count(aid) as adLandHuaweiCount
            from t_mkt_ad_info
            where id = mktinfoId
                and state = adState
                and platform = 2
                and dept_type = ifnull(vType,dept_type)
        )t4
        on 1 = 1
        ;
end

   //
DELIMITER ;