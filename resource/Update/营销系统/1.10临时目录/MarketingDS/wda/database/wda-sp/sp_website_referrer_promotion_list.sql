delimiter //
DROP PROCEDURE IF EXISTS sp_website_referrer_promotion_list;
CREATE PROCEDURE sp_website_referrer_promotion_list
(
    OUT pRetCode    INT,
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pReferrerType     INT,
  IN  pStartDate  VARCHAR(20),
    IN  pEndDate    VARCHAR(20)
)
COMMENT ''
proc: BEGIN
  
  declare vSelectSql varchar(1000);
  
  
  if(pStartDate = pEndDate) then
    
    set vSelectSql = concat('select referrer_name, sum(pv) as pv, round(SUM(bounces)/SUM(visits), 4) as bounce_rate, round(SUM(total_visit_time)/SUM(visits),2) as avg_visit_time, round(SUM(total_visit_pages)/SUM(visits),2) as avg_visit_pages from dw_wda_websites_summary_by_referrer_hm where 1=1 and referrer_type =', pReferrerType, '');
    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pStartDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pEndDate, '%Y%m%d'));      
    set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
    set vSelectSql = concat(vSelectSql, ' group by referrer_name order by pv desc');
    set vSelectSql = concat(vSelectSql, ' limit 50');
    
  
  elseif(pStartDate <> pEndDate) then
    
    set vSelectSql = concat('select referrer_name, sum(pv) as pv, round(SUM(bounces)/SUM(visits), 4) as bounce_rate, round(SUM(total_visit_time)/SUM(visits),2) as avg_visit_time, round(SUM(total_visit_pages)/SUM(visits),2) as avg_visit_pages from dw_wda_websites_summary_by_referrer_dm where 1=1 and referrer_type =', pReferrerType, '');   
    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pStartDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pEndDate, '%Y%m%d'));  
    set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
    set vSelectSql = concat(vSelectSql, ' group by referrer_name order by pv desc');
    set vSelectSql = concat(vSelectSql, ' limit 50');
    
    elseif(pPeriod = 99) then
        set vSelectSql = '';
    end if;
  
  SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
  
    set pRetCode = 0;

End//
