delimiter //
DROP PROCEDURE IF EXISTS sp_website_summary_topPages;
CREATE PROCEDURE sp_website_summary_topPages
(
    OUT pRetCode    INT,
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pFromDate   VARCHAR(20),
    IN  pToDate     VARCHAR(20)   
)
COMMENT ''
proc: BEGIN

    declare vSelectSql varchar(1000);
    

    if(pFromDate = pToDate) then
    set vSelectSql = 'select page_url, sum(pv) as pv from dw_wda_pages_topn_hm where 1=1 ';
    set vSelectSql = concat(vSelectSql, ' and pt_d = ', DATE_FORMAT(pFromDate, '%Y%m%d'));
    
  
  else 
    set vSelectSql = 'select page_url, sum(pv) as pv from dw_wda_pages_topn_dm where 1=1 ';
    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
    end if;    
    
  set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
  set vSelectSql = concat(vSelectSql, ' and page_url is not null group by page_url order by pv desc limit 10');
        
    SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
        
    set pRetCode = 0;
    
END//
