

DROP PROCEDURE IF EXISTS sp_mktUpdateUserRole;
CREATE PROCEDURE sp_mktUpdateUserRole (
    out pRetCode            int,
    
    in pOperator            varchar(50),
    in mktInfoID            int,
    in userName             varchar(50)
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
    
	if(exists(select * from t_ms_model where name=userName and val REGEXP  '^ad') and  not exists(select * from t_mkt_user_ad_info where id = mktInfoID and account = userName)) then
           insert into t_mkt_user_ad_info(id, account, flag) values(mktInfoID,userName,1);
    end if;
    
   
     
end;  