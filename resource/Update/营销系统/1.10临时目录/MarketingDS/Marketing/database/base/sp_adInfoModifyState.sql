DROP PROCEDURE IF EXISTS sp_adInfoModifyState;
CREATE PROCEDURE sp_adInfoModifyState (
    out pRetCode             int,
    
    in  pOperator            varchar(50),
    in  adInfoId             int,
    in  adInfoState          int
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
    
    
    if(not exists(select 
                * 
              from 
                t_mkt_ad_info 
              where 
                aid = adInfoId )) then
        select 4001 into pRetCode;
        leave proc;
    end if; 
    
    SELECT 0 INTO pRetCode;
    
    update t_mkt_ad_info set
        state = ifnull(adInfoState, state)
     where aid = adInfoId; 
    

    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;
end;