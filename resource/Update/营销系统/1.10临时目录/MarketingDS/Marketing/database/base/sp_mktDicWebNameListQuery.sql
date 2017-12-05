

DROP PROCEDURE IF EXISTS sp_mktDicWebNameListQuery;
CREATE PROCEDURE sp_mktDicWebNameListQuery (
 out pRetCode             int,
 
 in  pOperator            varchar(50),
 in  mktinfoId            int
 
)
proc: BEGIN
    
    declare vType           int;
    
    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    set pRetCode = 0;
    
    if(vType is null) then
        select dept_type into vType from t_mkt_info where id = mktinfoId;
    end if;
    
    select
        concat(id,'_',pid) as id,
        name
    from 
        t_mkt_dic_info
    where
        type = 'web' and dept_type = ifnull(vType,dept_type)
     order by convert(name USING gbk) COLLATE gbk_chinese_ci;
    
end;