DELIMITER // 
DROP PROCEDURE IF EXISTS sp_mktDicCommonQuery;
CREATE PROCEDURE sp_mktDicCommonQuery (
    out pRetCode            int,
    
    in  pOperator           varchar(50),
    in  pDicId              int,
    in  pType               varchar(50),
    in  pFrom               int,
    in  pPerPage            int
)
proc: BEGIN
	
	 declare vType           int;
    
     set vType = null;
    
     if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
     end if;
     
     
    set pRetCode = 0;

    select
        t1.id   as      id,
        t1.name as      name,
        t2.name as      type,
        t1.pid  as      pid,
        if(vType is null,true,false) as flag
    from
    (
	    select 
	        id,
	        name,
	        pid,
	        dept_type
	    from 
	        t_mkt_dic_info
	    where
	        if(pType = null, 1=1, type = pType) and if(pDicId is null, 1=1, id = pDicId)  and dept_type = ifnull(vType,dept_type)
	    limit pFrom,pPerPage
    )t1
    left outer join
    (
        select
            dic_key     as      id,
            dic_value   as      name
        from
            t_mkt_common_dic_info
        where
            type = 'department'
    )t2    
    on
    t1.dept_type = t2.id;
    
    
     -- 总数
    select 
        count(*) as total
    from
       t_mkt_dic_info
    where
       if(pType = null, 1=1, type = pType) and if(pDicId is null, 1=1, id = pDicId)  and dept_type = ifnull(vType,dept_type);
end


   //  
DELIMITER ;