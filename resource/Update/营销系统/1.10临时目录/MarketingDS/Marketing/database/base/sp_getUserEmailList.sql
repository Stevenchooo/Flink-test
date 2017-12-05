DROP PROCEDURE IF EXISTS sp_getUserEmailList;
CREATE PROCEDURE sp_getUserEmailList (
    out pRetCode            int,
    
    in  pOperator            varchar(50),
    in  mktId                int,
    in  pageType             int

)
proc: BEGIN
	
	declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    
    if(pageType = 0)then
    
        
        select 
            t1.email 
        from 
            t_ms_user t1 
        join 
        (
            select 
                distinct operator as account 
             from 
                t_mkt_info 
             where 
                id = mktId
         )t2 
         on t1.account = t2.account
         where
            t1.deptType = ifnull(vType,t1.deptType);
        
    end if;
    
    
     if(pageType = 1)then
     
        select 
            t1.email 
        from 
            t_ms_user t1 
        join 
        (
            select
                distinct tt2.operator as account
            from
           (
            select 
                operator
             from 
                t_mkt_info 
             where 
                id = mktId
                
             union all
             
             select 
                operator
             from 
                t_mkt_ad_info 
             where 
                id = mktId
           )tt2   
         )t2 
         on t1.account = t2.account
         where
            t1.deptType = ifnull(vType,t1.deptType);
        
    end if;
    
    
    
    if(pageType = 2)then
     
        select 
            t1.email 
        from 
            t_ms_user t1 
        join 
        (
            select
                distinct tt2.operator as account
            from
           (
            select 
                operator
             from 
                t_mkt_info 
             where 
                id = mktId
                
             union all
             
             select 
                operator
             from 
                t_mkt_ad_info 
             where 
                id = mktId
                
            union all
            
            
            select
                ttt1.operator
            from
                t_mkt_land_info   ttt1
            join
            (
                select
                    *
                from
                    t_mkt_ad_info
                where 
                    id = mktId
            )ttt2
            on ttt1.aid = ttt2.aid
           )tt2   
         )t2 
         on t1.account = t2.account
         where
            t1.deptType = ifnull(vType,t1.deptType);
        
    end if;
    
    
    
    if(pageType = 3)then
     
        select 
            t1.email 
        from 
            t_ms_user t1 
        join 
        (
            select
                distinct tt2.operator as account
            from
           (
            select 
                operator
             from 
                t_mkt_info 
             where 
                id = mktId
                
             union all
             
             select 
                operator
             from 
                t_mkt_ad_info 
             where 
                id = mktId
                
            union all
            
            
            select
                ttt1.operator
            from
                t_mkt_land_info   ttt1
            join
            (
                select
                    *
                from
                    t_mkt_ad_info
                where 
                    id = mktId
            )ttt2
            on ttt1.aid = ttt2.aid
            
            
            union all
            
             select
                ttt1.operator
            from
                t_mkt_monitor_info   ttt1
            join
            (
                select
                    *
                from
                    t_mkt_ad_info
                where 
                    id = mktId
            )ttt2
            on ttt1.aid = ttt2.aid
            
            
           )tt2   
         )t2 
         on t1.account = t2.account
         where
            t1.deptType = ifnull(vType,t1.deptType);
        
    end if;
    
    
    
end;