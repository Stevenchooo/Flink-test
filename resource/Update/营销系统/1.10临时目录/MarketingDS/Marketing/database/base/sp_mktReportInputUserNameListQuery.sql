
DROP PROCEDURE IF EXISTS sp_mktReportInputUserNameListQuery;
CREATE PROCEDURE sp_mktReportInputUserNameListQuery (
    out pRetCode            int,
    in  pOperator           varchar(50),
    in  pType               int
)
proc: BEGIN
    
     declare vType           int;
    
     set vType = null;
    
     if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
     end if;
     
     
    set pRetCode = 0;
    
    select
        t.operator as id,
        t.operator as name
    from
    (
        select 
            distinct operator
        from 
            t_mkt_ad_info
        order by operator
    )t
    join
    (
       select
           account
       from
           t_ms_user
       where
           deptType = ifnull(vType,deptType)  and deptType = ifnull(pType,deptType)
    )t1
    on t.operator = t1.account;
    
end;