delimiter //
DROP PROCEDURE IF EXISTS sp_website_referrer_trend2_list;
CREATE PROCEDURE sp_website_referrer_trend2_list
(
  OUT pRetCode      INT,
    IN  pSiteId       VARCHAR(2000),
    IN  pPeriod       VARCHAR(128),
  IN  pTimeDim      INT,
    IN  pReferrerName   VARCHAR(20),
  IN  pStartDate    VARCHAR(20),
    IN  pEndDate      VARCHAR(20)
)
COMMENT ''

proc: BEGIN
  
  declare vSelectSql varchar(1000);
        
  
  if(pStartDate = pEndDate) then
    
    if(pTimeDim = 1) then
      set vSelectSql = concat('select pt_h, sum(pv) as pv, round(sum(bounces)/sum(visits),4) as bounce_rate, round(sum(total_visit_time)/sum(visits),2) as avg_visit_time, round(sum(total_visit_pages)/sum(visits),2) as avg_visit_pages  from dw_wda_websites_summary_by_referrer_hm where 1=1 ');
      
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      
      set vSelectSql = concat(vSelectSql, ' and pt_d = ', DATE_FORMAT(pStartDate, '%Y%m%d'));  
      
      if(pReferrerName is not null and '' != pReferrerName) then
        set vSelectSql = concat(vSelectSql, ' and referrer_name = ''', pReferrerName, ''' ');
          end if;
      
      set vSelectSql = concat(vSelectSql, ' group by pt_h order by pt_h desc');
      
    
    elseif(pTimeDim = 2) then
      set vSelectSql = concat('select pt_d, sum(pv) as pv, round(sum(bounces)/sum(visits),4) as bounce_rate, round(sum(total_visit_time)/sum(visits),2) as avg_visit_time, round(sum(total_visit_pages)/sum(visits),2) as avg_visit_pages  from dw_wda_websites_summary_by_referrer_hm where 1=1 ');
      
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      
      set vSelectSql = concat(vSelectSql, ' and pt_d = ', DATE_FORMAT(pStartDate, '%Y%m%d'));  
      
      if(pReferrerName is not null and '' != pReferrerName) then
        set vSelectSql = concat(vSelectSql, ' and referrer_name = ''', pReferrerName, ''' ');
          end if; 
      
      set vSelectSql = concat(vSelectSql, ' group by pt_d');
    end if;
    
  
  elseif(pStartDate <> pEndDate) then
  
    
    if(pTimeDim = 1) then
      set vSelectSql = concat('select pt_h, sum(pv) as pv, round(sum(bounces)/sum(visits),4) as bounce_rate, round(sum(total_visit_time)/sum(visits),2) as avg_visit_time, round(sum(total_visit_pages)/sum(visits),2) as avg_visit_pages  from dw_wda_websites_summary_by_referrer_hm where 1=1 ');
      
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      
      set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pStartDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pEndDate, '%Y%m%d'));  
      
      if(pReferrerName is not null and '' != pReferrerName) then
        set vSelectSql = concat(vSelectSql, ' and referrer_name = ''', pReferrerName, ''' ');
          end if;
      
      set vSelectSql = concat(vSelectSql, ' group by pt_h order by pt_h desc'); 
      
    
    elseif(pTimeDim = 2 or pTimeDim = 3 or pTimeDim = 4) then
      
      set vSelectSql = concat('select pt_d, sum(pv) as pv, round(sum(bounces)/sum(visits),4) as bounce_rate, round(sum(total_visit_time)/sum(visits),2) as avg_visit_time, round(sum(total_visit_pages)/sum(visits),2) as avg_visit_pages  from dw_wda_websites_summary_by_referrer_dm where 1=1 ');
      
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      
      set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pStartDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pEndDate, '%Y%m%d'));  
      
      if(pReferrerName is not null and '' != pReferrerName) then
        set vSelectSql = concat(vSelectSql, ' and referrer_name = ''', pReferrerName, ''' ');
          end if;
      
      set vSelectSql = concat(vSelectSql, ' group by pt_d order by pt_d desc'); 
      
    end if;
    
    elseif(pPeriod = 99) then
        set vSelectSql = '';
    end if;
  
  SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
  
    set pRetCode = 0;
   
End//
