DROP PROCEDURE IF EXISTS sp_mktInfoModify;
CREATE PROCEDURE sp_mktInfoModify (
    out pRetCode            int,
    
    in pOperator             varchar(50),
    in pId                  int,
    in pName                varchar(50), -- 活动名称
    in pProduct             int,
    in pSalePoint           varchar(255),
    in pslogan              varchar(255),
    in pStrategicPosition   varchar(255),
    in pTargetPopulation    varchar(255),
    in pExpectedPrice       float,
    in pMarketPace          varchar(255),
    in pPlatform            varchar(50),
    in pBudget              float,
    in pPurchaseMethod      varchar(50),
    in pReserveStartTime    date,
    in pReserveEndTime      date,
    in pPurchaseStartTime   date,
    in pDeliveryStartTime   date,
    in pDeliveryEndTime     date
)
proc: BEGIN
	
	declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    
    if(exists(select * from t_mkt_info where name = pName and id != pId)) then
        select 2000 into pRetCode;
        leave proc;
    end if; 
    
    
    if(not exists(select * from t_mkt_info where id = pId and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
        
    end if;
    
    SELECT 0 INTO pRetCode;
    
    update t_mkt_info set
        name = ifnull(pName, name), 
        product = ifnull(pProduct, product), 
        sale_point = ifnull(pSalePoint, sale_point),
        slogan = ifnull(pslogan, slogan),
        strategic_position = ifnull(pStrategicPosition, strategic_position),
        target_population = ifnull(pTargetPopulation, target_population),
        expected_price = ifnull(pExpectedPrice, expected_price),
        market_pace = ifnull(pMarketPace, market_pace),
        platform = ifnull(pPlatform, platform),
        budget = ifnull(pBudget, budget),
        purchase_method = ifnull(pPurchaseMethod, purchase_method),
        reserve_start_time = ifnull(pReserveStartTime, reserve_start_time),
        reserve_end_time = ifnull(pReserveEndTime, reserve_end_time),
        purchase_start_time = ifnull(pPurchaseStartTime, purchase_start_time),
        delivery_start_time = ifnull(pDeliveryStartTime, delivery_start_time),
        delivery_end_time = ifnull(pDeliveryEndTime, delivery_end_time),
        update_time = ifnull(now(), update_time)
     where id = pId; 

    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;
end;