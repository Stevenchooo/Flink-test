DROP PROCEDURE IF EXISTS sp_mktinfoQuery;
CREATE PROCEDURE sp_mktinfoQuery (
    out pRetCode            int,
    
    in pOperator            varchar(50),
    in pName                varchar(50),
    in pFrom                int,
    in pPerPage             int
)
proc: BEGIN
    declare vName        varchar(100);
     declare vType           int;
    
    set vType = null;
    
    set pRetCode = 0;
    if(pName is not null) then
        set vName = concat('%', pName, '%');
    else
        set vName = '%%';
    end if;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
  if(exists(select * from t_ms_model where name=pOperator and val REGEXP  '^ad')) then
     select 
        t1.id           as      mktinfoId,
        t1.name         as      mktinfoName,
        t2.name         as      mktinfoProduct,
        t1.sale_point   as      mktinfoSalePoint,
        t3.name         as      mktinfoPlatform,
        t1.delivery_start_time     as      mktinfoDeliveryStartTime,
        t1.delivery_end_time       as      mktinfoDeliveryEndTime,
        t1.create_time             as      mktinfoCreateTime,
        if(t1.operator = pOperator,true,false)                       as      flag
    from
        t_mkt_info t1
    left outer join
    (
        select
            id,
            name
        from
            t_mkt_dic_info
        where
            type = 'product' and pid is null
    ) t2
    on t1.product = t2.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'platform'
    ) t3
    on t1.platform = t3.id
    where t1.name like vName and t1.dept_type = ifnull(vType,t1.dept_type)
    
    order by t1.create_time desc
    
    limit pFrom,pPerPage;
    
  else
    select 
        t1.id           as      mktinfoId,
        t1.name         as      mktinfoName,
        t2.name         as      mktinfoProduct,
        t1.sale_point   as      mktinfoSalePoint,
        t3.name         as      mktinfoPlatform,
        t1.delivery_start_time     as      mktinfoDeliveryStartTime,
        t1.delivery_end_time       as      mktinfoDeliveryEndTime,
        t1.create_time             as      mktinfoCreateTime,
        true                       as      flag
    from
        t_mkt_info t1
    left outer join
    (
        select
            id,
            name
        from
            t_mkt_dic_info
        where
            type = 'product' and pid is null
    ) t2
    on t1.product = t2.id
    left outer join
    (
        select
            dic_key     as  id,
            dic_value   as  name
        from
            t_mkt_common_dic_info
        where
            type = 'platform'
    ) t3
    on t1.platform = t3.id
    where t1.name like vName and t1.dept_type = ifnull(vType,t1.dept_type)
    
    order by t1.create_time desc
    
    limit pFrom,pPerPage;
    
  end if;

    

    -- 总数
    select 
        count(*) as total
    from 
        t_mkt_info
    where name like vName and dept_type = ifnull(vType,dept_type);
end;