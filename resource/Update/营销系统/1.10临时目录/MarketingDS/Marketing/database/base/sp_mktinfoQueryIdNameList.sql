DROP PROCEDURE IF EXISTS sp_mktinfoQueryIdNameList;
CREATE PROCEDURE sp_mktinfoQueryIdNameList (
    out pRetCode            int,
    
    in pOperator            varchar(50)
)
proc: BEGIN
    
	declare vType           int;
    
    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    select
        -1              as    mktinfoId,
        '全部'           as     mktinfoName
    from 
        dual
        
    union all  
      
    select 
        t1.id           as      mktinfoId,
        t1.name         as      mktinfoName
    from
    (
        select
            id,
            name,
            create_time
        from
            t_mkt_info
        where
            dept_type = ifnull(vType,dept_type)
        order by create_time desc
    )t1;
end;