DROP PROCEDURE IF EXISTS sp_mktInfoUserInfo;
CREATE PROCEDURE sp_mktInfoUserInfo (
    out pRetCode            int,
    in mktInfoID            int
)
proc: BEGIN
	
	 declare vType           int;
	 
	 select dept_type into vType from t_mkt_info where id = mktInfoID;
	 
    SELECT 0 INTO pRetCode;
    
    select
        t1.account               as      account,
        mktInfoID                as      mktinfoId,
        if(t2.flag is null,2,t2.flag)                  as      flag,
        t3.name                  as      name,
        t3.department            as      department
    from
    (
	    select
	        distinct name   as  account
	    from
	        t_ms_model
	    where
	       val REGEXP  '^ad'  and meta = 'admin' and name in (select account from t_ms_user where deptType = vType)
    )t1
    left outer join
    (
	    select
	       account          as      account,
	       flag             as      flag
	    from 
	        t_mkt_user_ad_info 
	     where id = mktInfoID  
    )t2
    on t1.account = t2.account
    left outer join
        t_ms_user t3
    on t1.account = t3.account;
    
end;  