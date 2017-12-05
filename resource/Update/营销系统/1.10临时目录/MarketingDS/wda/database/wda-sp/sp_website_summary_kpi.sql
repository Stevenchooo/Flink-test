delimiter //
DROP PROCEDURE IF EXISTS sp_website_summary_kpi;
CREATE PROCEDURE sp_website_summary_kpi
(
    OUT pRetCode    INT,
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pCurKpi     VARCHAR(50),
    IN  pHuanBi     INT,
    IN  pFromDate   VARCHAR(20),
    IN  pToDate     VARCHAR(20),
    IN  pHuanBiFromDate   VARCHAR(20),
    IN  pHuanBiToDate     VARCHAR(20)
)
COMMENT ''
proc: BEGIN

    declare vSelectSql varchar(1000);
    
    -- 选择一天
    if(pFromDate = pToDate) then
    
        set vSelectSql = concat('select pt_h as x, ', pCurKpi, ' as y from dw_wda_websites_summary_hm where 1=1');        
        set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));        
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');        
        set vSelectSql = concat(vSelectSql, ' order by x');
        
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
         
        if(pHuanBi = 0) then
        	set vSelectSql = concat('select pt_h as x, ', pCurKpi, ' as y from dw_wda_websites_summary_hm where 1=1');
        	set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
        	set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        	set vSelectSql = concat(vSelectSql, ' order by x');
        	
          SET @Sql = vSelectSql;
          prepare stmt FROM @Sql;    
          execute stmt;
           
        elseif(pHuanBi = 1) then
        	set vSelectSql = concat('select pt_h as x, ', pCurKpi, ' as y from dw_wda_websites_summary_hm where 1=1');
        	set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
        	set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        	set vSelectSql = concat(vSelectSql, ' order by x');
        	
          SET @Sql = vSelectSql;
          prepare stmt FROM @Sql;    
          execute stmt;
        
      	end if;
  
    else
				-- 新访客比率
							if(pCurKpi = 'nuv_rate') then
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,snuv/suv as y from (select
											pt_d,sum(nuv) as snuv ,sum(uv) as suv from dw_wda_websites_summary_dm
											where 1=1');

									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' group by pt_d)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;

							-- 平均访问时长
							elseif(pCurKpi = 'avg_visit_time') then
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,stotal_visit_time/svisits as y from (select
											pt_d,sum(total_visit_time) as stotal_visit_time ,sum(visits) as svisits from dw_wda_websites_summary_dm
											where 1=1');
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_d)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;
									execute stmt;

							-- 平均访问页面
							elseif(pCurKpi = 'avg_visit_pages') then
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,stotal_visit_pages/svisits as y from (select
											pt_d,sum(total_visit_pages) as stotal_visit_pages ,sum(visits) as svisits from dw_wda_websites_summary_dm
											where 1=1');

									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_d)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;

							else
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, ', pCurKpi, ' as y from dw_wda_websites_summary_dm where 1=1');
									set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
									set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 判断 新访客比率/平均访问 结束
							end if;         
    end if;
    
    set pRetCode = 0;
    
END//