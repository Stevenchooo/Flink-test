DELIMITER // 
DROP PROCEDURE IF EXISTS sp_mktDicCommonListQuery;
CREATE PROCEDURE sp_mktDicCommonListQuery (
    out pRetCode            int,
    in  pType               varchar(50)
)
proc: BEGIN
    set pRetCode = 0;

    if(pType = 'ad_state') then
    
       select *
       from
       (
        select
	        -1             as    id,
	        '全部'           as     name
	    from 
	        dual
	        
        union all
        
        select 
	        dic_key        as      id,
	        dic_value      as      name
	    from 
	        t_mkt_common_dic_info
	    where
	        type = pType
	   ) t
	   order by id;

    else
        select 
	        dic_key        as      id,
	        dic_value      as      name
	    from 
	        t_mkt_common_dic_info
	    where
	        type = pType order by dic_key;
    
    end if;
    
    
end


//  
DELIMITER ;