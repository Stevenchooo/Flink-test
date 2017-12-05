DROP PROCEDURE IF EXISTS sp_mktInfoDetail;
CREATE PROCEDURE sp_mktInfoDetail (
    out pRetCode            int,
    
    in pOperator            varchar(50),
    in mktInfoID            int
)
proc: BEGIN
	
	
    declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    if(not exists(select * from t_mkt_info where id = mktInfoID and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
        
    end if;
    
    
    SELECT 0 INTO pRetCode;
    
    select
       
        name                        as      mktinfoName,
        product                     as      mktinfoProduct,
        sale_point                  as      mktinfoSalePoint,
        slogan                      as      mktinfoSlogan,
        strategic_position          as      mktinfoStrategicPosition,
        target_population           as      mktinfoTargetPopulation,
        expected_price              as      mktinfoExpectedPrice,
        market_pace                 as      mktinfoMarketPace,
        platform                    as      mktinfoPlatform,
        budget                      as      mktinfoBudget,
        purchase_method             as      mktinfoPurchaseMethod,
        reserve_start_time          as      mktinfoReserveStartTime,
        reserve_end_time            as      mktinfoReserveEndTime,
        purchase_start_time         as      mktinfoPurchaseStartTime,
        delivery_start_time         as      mktinfoDeliveryStartTime,
        delivery_end_time           as      mktinfoDeliveryEndTime,
        dept_type                   as      deptType
    from 
        t_mkt_info 
     where id = mktInfoID;
    
end;  