DROP PROCEDURE IF EXISTS sp_userQuery;
CREATE PROCEDURE sp_userQuery (
    out pRetCode            int,
    in pAccount             varchar(50),
    in pOperator            varchar(50),
    in pFrom                int,
    in pPerPage             int
)
proc: BEGIN
    declare vAccount        varchar(100);
    declare vType           int;
    
    set vType = null;
    set pRetCode = 0;
    if(pAccount is not null) then
        set vAccount = concat('%', pAccount, '%');
    else
        set vAccount = '%%';
    end if;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
     select
        t1.account,
        t1.createTime,
        t1.phoneNum,
        t1.email,
        t1.creator,
        t1.name,
        t1.department,
        t2.name as  deptType
     from
     (
	     select account,createTime,phoneNum,email,creator,name,department,deptType
	      from t_ms_user
	     where status <> 'del'
	       and account like vAccount
	       and deptType=ifnull(vType,deptType)
	     order by createTime desc
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
     on t1.deptType = t2.id;
    
    -- 总数
    select count(*) as total
      from t_ms_user
     where status <> 'del'
       and account like vAccount;
end;