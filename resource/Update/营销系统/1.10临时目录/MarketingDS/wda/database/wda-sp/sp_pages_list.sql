delimiter //
DROP PROCEDURE IF EXISTS sp_pages_list;
CREATE PROCEDURE sp_pages_list
(
  OUT pRetCode    INT,
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pStartDate  VARCHAR(20),
    IN  pEndDate    VARCHAR(20)
)
COMMENT ''
proc: BEGIN

  declare vSelectSql varchar(1000);
    
  
  if(pStartDate = pEndDate) then
    set vSelectSql = concat('select page_url, sum(pv) as pv,  SUM(uv) as uv, SUM(exit_pages) as exit_pages, round(SUM(exit_pages)/sum(visits),4) as exit_rate from dw_wda_pages_summary_hm where 1=1 ');
    if(pSiteId is not null and '' != pSiteId) then
      set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
    end if;
    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pStartDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pEndDate, '%Y%m%d'));  
    set vSelectSql = concat(vSelectSql, ' group by page_url order by pv desc limit 50');
    
  elseif(pStartDate <> pEndDate) then
    set vSelectSql = concat('select page_url, sum(pv) as pv, SUM(uv) as uv, SUM(exit_pages) as exit_pages, round(SUM(exit_pages)/sum(visits),4) as exit_rate  from dw_wda_pages_summary_dm where 1=1 ');
    if(pSiteId is not null and '' != pSiteId) then
      set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
    end if;
    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pStartDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pEndDate, '%Y%m%d'));  
    set vSelectSql = concat(vSelectSql, ' group by page_url order by pv desc limit 50');
    
    elseif(pPeriod = 99) then
        set vSelectSql = '';
        
    end if;
  
  SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
  
    set pRetCode = 0;
  
END//