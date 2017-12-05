DELIMITER // 
DROP PROCEDURE IF EXISTS sp_logQuery;
CREATE PROCEDURE sp_logQuery (
    out pRetCode                 int,
    in pqueryName                varchar(120),
    in pqueryDateBeginDay        varchar(10),
    in pqueryDateEndDay          varchar(10),
     in pFrom                    int,
    in pPerPage                  int
)
proc: BEGIN
    
    declare vName        varchar(100);
    set pRetCode = 0; 
     

   if( pqueryDateBeginDay = '' || pqueryDateBeginDay is null ) then
       set  pqueryDateBeginDay  = DATE_FORMAT(now(),"%Y%m%d");
    end if;
    if( pqueryDateEndDay = '' || pqueryDateEndDay is null ) then
       set  pqueryDateEndDay  = DATE_FORMAT(now(),"%Y%m%d");
    end if;
    
    if(pqueryName is not null || pqueryName!='') then
        set vName = concat('%', pqueryName, '%');
    else
        set vName = '%%';
    end if;
    
    select 
        logTime                             as operTime,
        account                             as operator,
        if(resultCode=0,'成功','失败')      as operRes,
	      apiName                             as opeRequest,
        info                                as opeRrsponse
    
    from t_ms_log
    where 
    account like vName  and
    DATE_FORMAT(logTime,"%Y%m%d") between pqueryDateBeginDay and pqueryDateEndDay 
    order by logTime desc
    limit pFrom,pPerPage;
    
    
    -- 总数
    select 
        count(*) as total
    from 
        t_ms_log
    where 
    account like vName  and
    DATE_FORMAT(logTime,"%Y%m%d") between pqueryDateBeginDay and pqueryDateEndDay ;
    
   end
   //  
DELIMITER ;