delimiter //
DROP PROCEDURE IF EXISTS sp_website_referrer_kpi_pie;
CREATE PROCEDURE sp_website_referrer_kpi_pie
(
    OUT pRetCode    INT, 
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pCurKpi     VARCHAR(20),
    IN  pFromDate   VARCHAR(20),
    IN  pToDate     VARCHAR(20)    
)
COMMENT ''
proc: BEGIN

     declare vSelectSql varchar(1000);
          
     
     if(pFromDate = pToDate) then
   
    set vSelectSql = concat('select referrer_type as ''key'', SUM(', pCurKpi, ') as y, case referrer_type when 1 then ''#E3563A'' when 2 then ''#48BEF4'' when 3 then ''#19AA5F'' end as color  from dw_wda_websites_summary_by_referrer_hm where 1=1');
    if(pFromDate is not null and pToDate is not null) then
      set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
    end if;
              
    if(pSiteId is not null and '' != pSiteId) then
      set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
    end if;
              
    set vSelectSql = concat(vSelectSql, ' group by referrer_type');
    
    SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
              
     
     elseif(pFromDate <> pToDate) then
   
        set vSelectSql = concat('select referrer_type as ''key'', SUM(', pCurKpi, ') as y, case referrer_type when 1 then ''#E3563A'' when 2 then ''#48BEF4'' when 3 then ''#19AA5F'' end as color  from dw_wda_websites_summary_by_referrer_dm where 1=1');
    if(pFromDate is not null and pToDate is not null) then
      set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
    end if;
              
    if(pSiteId is not null and '' != pSiteId) then
      set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
    end if;
              
    set vSelectSql = concat(vSelectSql, ' group by referrer_type');
    
    SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
    
     
     elseif(pPeriod = 99) then
        set vSelectSql = '';
          
     end if;
     
     
     
     

     set pRetCode = 0;
END//
