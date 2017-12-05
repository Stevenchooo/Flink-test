

DROP PROCEDURE IF EXISTS sp_mktEmailUserInfo;
CREATE PROCEDURE sp_mktEmailUserInfo (
    out pRetCode            int,
    in  pid                 int,
    in  pAid                int,
    in  pageInfo            varchar(50)
)
proc: BEGIN
	
	 declare vType           int;
     
    
     
    SELECT 0 INTO pRetCode;
    
    
     if(pageInfo is null) then
        select 100 into pRetCode;
        leave proc;
    end if;
    
    if(pid is not null) then
       select dept_type into vType from t_mkt_info where id = pid;
    end if;
    
    if(pAid is not null) then
       select dept_type into vType from t_mkt_ad_info where aid = pAid;
    end if;
    
    
    select
        t3.account               as      account,
        t3.name                  as      name,
        t3.department            as      department,
        t4.role                  as      role
    from
    (
        select
            t2.name         as      name,
            max(t2.val)     as      role
        from
	    (
	       select
	            distinct role
	       from
	           t_ms_right
	       where
	            meta = pageInfo  and  (oprRight like '%|c|%'  or oprRight like '%|u|%') 
	    )t1
	    join
	    (
	        select
	           distinct name,val
	        from 
	            t_ms_model 
	         where meta = 'admin' and val is not null  and name in (select account from t_ms_user where deptType = vType)
	    )t2
	    on t1.role = t2.val
	    
	    group by t2.name
	    order by role,name
    )t4
    join
        t_ms_user t3
    on t4.name = t3.account;
    
end;  