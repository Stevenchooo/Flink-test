delimiter //
DROP PROCEDURE IF EXISTS sp_visitor_area_distribution_list;
CREATE PROCEDURE sp_visitor_area_distribution_list
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
    set vSelectSql = concat('select province_name, sum(pv) as pv, sum(visits) as visits,  round(sum(total_visit_time)/sum(visits),2) as avg_visit_time, round(sum(total_visit_pages)/sum(visits),2) as avg_visit_pages  from dw_wda_visitors_area_distribution_hm where 1=1 ');
    if(pSiteId is not null and '' != pSiteId) then
      set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
    end if;
    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pStartDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pEndDate, '%Y%m%d'));  
    set vSelectSql = concat(vSelectSql, ' group by province_name order by pv desc');
    
  elseif(pStartDate <> pEndDate) then
    set vSelectSql = concat('select province_name, sum(pv) as pv, sum(visits) as visits,  round(sum(total_visit_time)/sum(visits),2) as avg_visit_time, round(sum(total_visit_pages)/sum(visits),2) as avg_visit_pages  from dw_wda_visitors_area_distribution_dm where 1=1 ');
    if(pSiteId is not null and '' != pSiteId) then
      set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
    end if;
    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pStartDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pEndDate, '%Y%m%d'));  
    set vSelectSql = concat(vSelectSql, ' group by province_name order by pv desc');
    
    elseif(pPeriod = 99) then
        set vSelectSql = '';
        
    end if;
  
  SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
  
  
    set pRetCode = 0;
END//
