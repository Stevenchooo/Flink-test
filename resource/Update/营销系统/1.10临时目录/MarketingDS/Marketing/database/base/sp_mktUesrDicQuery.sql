DROP PROCEDURE IF EXISTS sp_mktUesrDicQuery;
CREATE PROCEDURE sp_mktUesrDicQuery (
    out pRetCode            int,
    
    in pOperator            varchar(50),
    in pFrom                int,
    in pPerPage             int
)
proc: BEGIN
   
    
    declare vType           int;
    
    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
     set pRetCode = 0;
     
      select
            t1.account,
            t1.name,
            t1.department,
            t1.phoneNum,
            t1.email,
            t1.description,
            t2.name as  type
      from
      (
	      select
	        account,
	        name,
	        department,
	        phoneNum,
	        email,
	        description,
	        deptType
	      from
	         t_ms_user
	      where
	        showFlag = 1  and deptType = ifnull(vType,deptType)
	      limit pFrom, pPerPage
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
    on t1.deptType = t2.id;
    
    -- 总数
    select count(*) as total
     from
         t_ms_user
      where
        showFlag = 1 and deptType = ifnull(vType,deptType);
end;