delimiter //
DROP PROCEDURE IF EXISTS sp_pages_trend_list;
CREATE PROCEDURE sp_pages_trend_list
(
  OUT pRetCode      INT,
    IN  pSiteId       VARCHAR(2000),
    IN  pPeriod       VARCHAR(128),
  IN  pTimeDim      INT,
    IN  pPageUrl        VARCHAR(128),
  IN  pStartDate    VARCHAR(20),
    IN  pEndDate      VARCHAR(20)
)
COMMENT ''
proc: BEGIN
  
  declare vSelectSql varchar(1000);
    
  
  if(pStartDate = pEndDate) then
    
    if(pTimeDim = 1) then
      set vSelectSql = concat('select pt_h, SUM(pv) as pv, SUM(uv) as uv, SUM(exit_pages) as exit_pages, round(SUM(exit_pages)/sum(visits),4) as exit_rate from dw_wda_pages_summary_hm where 1=1 ');
      
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      
      set vSelectSql = concat(vSelectSql, ' and pt_d = ', DATE_FORMAT(pStartDate, '%Y%m%d'));  
      
      if(pPageUrl is not null and '' != pPageUrl) then
        set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
          end if;
      
      set vSelectSql = concat(vSelectSql, ' group by pt_h order by pt_h desc');
      
    
    elseif(pTimeDim = 2) then
      set vSelectSql = concat('select pt_d, SUM(pv) as pv, SUM(uv) as uv, SUM(exit_pages) as exit_pages, round(SUM(exit_pages)/sum(visits),4) as exit_rate from dw_wda_pages_summary_hm where 1=1 ');
      
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      
      set vSelectSql = concat(vSelectSql, ' and pt_d = ', DATE_FORMAT(pStartDate, '%Y%m%d'));  
      
      if(pPageUrl is not null and '' != pPageUrl) then
        set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
          end if;
    end if;
    
  
  elseif(pStartDate <> pEndDate) then
  
    
    if(pTimeDim = 1) then
      set vSelectSql = concat('select pt_h, SUM(pv) as pv, SUM(uv) as uv, SUM(exit_pages) as exit_pages, round(SUM(exit_pages)/sum(visits),4) as exit_rate  from dw_wda_pages_summary_hm where 1=1 ');
      
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      
      set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pStartDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pEndDate, '%Y%m%d'));  
      
      if(pPageUrl is not null and '' != pPageUrl) then
        set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
          end if;
      
      set vSelectSql = concat(vSelectSql, ' group by pt_h order by pt_h desc'); 
      
    
    elseif(pTimeDim = 2 or pTimeDim = 3 or pTimeDim = 4) then
      
      set vSelectSql = concat('select pt_d, SUM(pv) as pv, SUM(uv) as uv, SUM(exit_pages) as exit_pages, round(SUM(exit_pages)/sum(visits),4) as exit_rate from dw_wda_pages_summary_dm where 1=1 ');
      
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      
      set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pStartDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pEndDate, '%Y%m%d'));  
      
      if(pPageUrl is not null and '' != pPageUrl) then
        set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
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
  
END//
