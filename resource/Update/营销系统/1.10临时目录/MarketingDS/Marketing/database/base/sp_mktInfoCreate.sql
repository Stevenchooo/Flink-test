DROP PROCEDURE IF EXISTS sp_mktInfoCreate;
CREATE PROCEDURE sp_mktInfoCreate (
    out pRetCode            int,
    
    in pOperator            varchar(50),
    in pName                varchar(50), -- 创建人
    in pProduct             varchar(50),
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
	
	 declare pId                      int;
	 declare vType                    int;
	 
	 select deptType into vType from t_ms_user where account = pOperator;
	 
    if(exists(select * from t_mkt_info where name =pName  and dept_type = ifnull(vType,dept_type) )) then
        select 8801 into pRetCode;
        leave proc;
    end if;
    
   
    
    SELECT 0 INTO pRetCode;

    -- 因为创建用户的用户，一定比比被创建用户权限高，能够看到它的所有数据
    -- 所以密码可以不必立刻过期
    -- 但是，当低级用户创建，高级用户赋权时，会出现安全问题，低级用户知道高级用户的密码
    -- 因为此时被他创建的用户可能具有比他还要高级的权限
    -- 所以密码必须立即过期

    
    
    insert into t_mkt_info (
        name,
        product, 
        sale_point,
        slogan,
        strategic_position,
        target_population,
        expected_price,
        market_pace,
        platform,
        budget,
        purchase_method,
        reserve_start_time,
        reserve_end_time,
        purchase_start_time,
        delivery_start_time,
        delivery_end_time,
        operator,
        create_time,
        update_time,
        dept_type
        )
    values (
        pName,
        pProduct,
        pSalePoint,
        pslogan,
        pStrategicPosition,
        pTargetPopulation,
        pExpectedPrice,
        pMarketPace,
        pPlatform,
        pBudget,
        pPurchaseMethod,
        pReserveStartTime,
        pReserveEndTime,
        pPurchaseStartTime,
        pDeliveryStartTime,
        pDeliveryEndTime,
        pOperator,
        now(),
        now(),
        vType);
        
    if(exists(select * from t_ms_model where name=pOperator and val REGEXP  '^ad')) then
	    select id into pId from  t_mkt_info where name = pName and dept_type= vType;
	    insert into t_mkt_user_ad_info(id, account, flag) values(pId,pOperator,1);
	end if;
    
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    
end;