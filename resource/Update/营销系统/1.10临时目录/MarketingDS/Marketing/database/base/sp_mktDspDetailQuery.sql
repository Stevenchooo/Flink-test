DELIMITER // 
DROP PROCEDURE IF EXISTS sp_mktDspDetailQuery;
CREATE PROCEDURE sp_mktDspDetailQuery (
    out pRetCode               int,
    in pOperator               varchar(50),
    in pSid                    varchar(30),
    in pAdqueryDateBeginDay    varchar(10),
    in pAdqueryDateEndDay      varchar(10),
    in pDate                   varchar(10)
)
proc: BEGIN

        
    
    
    set pRetCode = 0;
    if(pSid = '-1' || pSid = '' || pSid = -1 ) then
    	    select -1 into pRetCode;
	       leave proc;
	  end if;
	  
	  if(pAdqueryDateBeginDay = '-1' || pAdqueryDateBeginDay = '' || pAdqueryDateBeginDay = -1 ) then
    	    select -1 into pRetCode;
	       leave proc;
	  end if;
    
    select 
        dns_name   as webDns,
        channel    as Channel,
        sum(bg_pv)      as bgPv,
	      sum(bg_uv)      as bgUv,
        sum(dj_pv)      as djPv,
	      sum(dj_uv)      as djUv
    
    from dw_honor_marketing_result_dsp_hm
    where 
    sid =  pSid and pt_d = pDate and 
    pt_d between pAdqueryDateBeginDay and pAdqueryDateEndDay 
    group by dns_name,channel
    order by dns_name desc,channel desc;
    
    
    
   end
   //  
DELIMITER ;