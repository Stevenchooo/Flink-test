DROP PROCEDURE IF EXISTS sp_mktInputUserNameListQuery;
CREATE PROCEDURE sp_mktInputUserNameListQuery (
    out pRetCode            int,
    in  pOperator           varchar(50)
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
	       deptType = ifnull(vType,deptType)
	)t1
	on t.operator = t1.account;
    
end;