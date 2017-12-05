DELIMITER // 
DROP PROCEDURE IF EXISTS sp_mktDicMeaidQuery;
CREATE PROCEDURE sp_mktDicMeaidQuery (
    out pRetCode            int,
    
    in  pOperator           varchar(50),
    in  pNameQuery          varchar(50),
    in  pFrom               int,
    in  pPerPage            int
)
proc: BEGIN
	 declare vName        varchar(100);
	 declare vType           int;
    
     set vType = null;
    
     if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
     end if;
     
    set pRetCode = 0;
    
    if(pNameQuery is not null) then
        set vName = concat('%', pNameQuery, '%');
    else
        set vName = '%%';
    end if;

    select
        t1.id       as      pId,
        t1.name     as      pName,
        t2.id       as      id,
        t2.name     as      name,
        t3.name     as      type,
        if(vType is null,true,false)  as  flag
    from
    (
	    select 
	        id,
	        name,
	        dept_type
	    from 
	        t_mkt_dic_info
	    where
	        type = 'media'  and dept_type = ifnull(vType,dept_type)
    )t1
    left outer join
    (
        select 
            pid,
            id,
            name
        from 
            t_mkt_dic_info
        where
            type = 'web' and dept_type = ifnull(vType,dept_type)
            
    )t2
    on t1.id = t2.pid
    left outer join
    (
        select
            dic_key     as      id,
            dic_value   as      name
        from
            t_mkt_common_dic_info
        where
            type = 'department'
    )t3
    on t1.dept_type = t3.id
    where t2.name like vName  or t1.name like vName
    limit pFrom,pPerPage;
    
     -- 总数
    select 
        count(t1.id) as total
    from
    (
        select 
            id,
            name
        from 
            t_mkt_dic_info
        where
            type = 'media'  and dept_type = ifnull(vType,dept_type)
    )t1
    left outer join
    (
        select 
            pid,
            id,
            name
        from 
            t_mkt_dic_info 
        where
            type = 'web' and dept_type = ifnull(vType,dept_type)
    )t2
    on t1.id = t2.pid
    where t2.name like vName or t1.name like vName;
end


   //  
DELIMITER ;