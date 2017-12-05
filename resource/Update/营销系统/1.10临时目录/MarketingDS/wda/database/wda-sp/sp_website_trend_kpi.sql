delimiter //
DROP PROCEDURE IF EXISTS sp_website_trend_kpi;
CREATE PROCEDURE sp_website_trend_kpi
(
    OUT pRetCode    INT, 
    IN  pIsHuanBi   VARCHAR(128),
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pTimeDim    INT,
    IN  pCurKpi1     VARCHAR(20),
    IN  pCurKpi2     VARCHAR(20),
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
        
        -- 选择一天 - 按时
        if(pTimeDim = 1) then
        
              set vSelectSql = concat('select  pt_h as x, ', pCurKpi1, ' as y from dw_wda_websites_summary_hm where 1=1');
              
              if(pFromDate is not null and pToDate is not null) then
                set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
              end if;
              
              if(pSiteId is not null and '' != pSiteId) then
                  set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
              end if;
              
              set vSelectSql = concat(vSelectSql, ' order by x');
              SET @Sql = vSelectSql;
              prepare stmt FROM @Sql;    
              execute stmt;
              
              
          -- 选择一天 - 按时 - 环比
              if('' != pIsHuanBi) then
              
                 set vSelectSql = concat('select pt_h as x, ', pCurKpi1, ' as y from dw_wda_websites_summary_hm where 1=1');
                  
                 if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
                      set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
                 end if;
              
                 if(pSiteId is not null and '' != pSiteId) then
                      set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
                 end if;
                 
                 set vSelectSql = concat(vSelectSql, ' order by x');
                 SET @Sql = vSelectSql;
                 prepare stmt FROM @Sql;    
                 execute stmt;
              
              -- 选择一天 - 按时 - kpi2
              elseif(pCurKpi2 != '') then
                 
                set vSelectSql = concat('select pt_h as x, ', pCurKpi2, ' as y from dw_wda_websites_summary_hm where 1=1');
              
                if(pFromDate is not null and pToDate is not null) then
                  set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
                end if;
                
                if(pSiteId is not null and '' != pSiteId) then
                    set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
                end if;
                
                set vSelectSql = concat(vSelectSql, ' order by x');
                SET @Sql = vSelectSql;
                prepare stmt FROM @Sql;    
                execute stmt;   
              end if;
        
        -- 选择一天 - 按日
        elseif(pTimeDim = 2) then
							-- 新访客比率
							if(pCurKpi1 = 'nuv_rate') then
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,snuv/suv as y from (select
											pt_d,sum(nuv) as snuv ,sum(uv) as suv from dw_wda_websites_summary_hm
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
							elseif(pCurKpi1 = 'avg_visit_time') then
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,stotal_visit_time/svisits as y from (select
											pt_d,sum(total_visit_time) as stotal_visit_time ,sum(visits) as svisits from dw_wda_websites_summary_hm
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
							elseif(pCurKpi1 = 'avg_visit_pages') then
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,stotal_visit_pages/svisits as y from (select
											pt_d,sum(total_visit_pages) as stotal_visit_pages ,sum(visits) as svisits from dw_wda_websites_summary_hm
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
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, SUM(', pCurKpi1, ') as y from dw_wda_websites_summary_hm where 1=1');       
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 判断 新访客比率/平均访问 结束
							end if;
              
									-- 如果环比不为空
									if('' != pIsHuanBi) then
											-- 新访客比率
											if(pCurKpi1 = 'nuv_rate') then
													set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,snuv/suv as y from (select
													pt_d,sum(nuv) as snuv ,sum(uv) as suv from dw_wda_websites_summary_hm
													where 1=1');
                  
													if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_d)t');
													end if;
                 
													set vSelectSql = concat(vSelectSql, ' order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;

											-- 平均访问时长
											elseif(pCurKpi1 = 'avg_visit_time') then
													set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,stotal_visit_time/svisits as y from (select
													pt_d,sum(total_visit_time) as stotal_visit_time ,sum(visits) as svisits from dw_wda_websites_summary_hm
													where 1=1');
                  
													if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_d)t');
													end if;
                 
													set vSelectSql = concat(vSelectSql, ' order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;

											-- 平均访问页面
											elseif(pCurKpi1 = 'avg_visit_pages') then
													set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,stotal_visit_pages/svisits as y from (select
													pt_d,sum(total_visit_pages) as stotal_visit_pages ,sum(visits) as svisits from dw_wda_websites_summary_hm
													where 1=1');
													if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_d)t');
													end if;
                 
													set vSelectSql = concat(vSelectSql, ' order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;
											-- 一般
											else
													set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, SUM(', pCurKpi1, ') as y from dw_wda_websites_summary_hm where 1=1');
                  
													if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
													end if;
                 
													set vSelectSql = concat(vSelectSql, ' order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;
											-- 判断 新访客比率/平均访问 结束
											end if;
											
              
									-- 如果选择两项，那么环比就为空
									elseif(pCurKpi2 != '') then
											-- 新访客比率
											if(pCurKpi2 = 'nuv_rate') then
													set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,snuv/suv as y from (select
													pt_d,sum(nuv) as snuv ,sum(uv) as suv from dw_wda_websites_summary_hm
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
											elseif(pCurKpi2 = 'avg_visit_time') then
													set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,stotal_visit_time/svisits as y from (select
															pt_d,sum(total_visit_time) as stotal_visit_time ,sum(visits) as svisits from dw_wda_websites_summary_hm
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
											elseif(pCurKpi2 = 'avg_visit_pages') then
													set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,stotal_visit_pages/svisits as y from (select
															pt_d,sum(total_visit_pages) as stotal_visit_pages ,sum(visits) as svisits from dw_wda_websites_summary_hm
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
											-- 一般
											else
													set vSelectSql = concat('select  DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, SUM(', pCurKpi2, ') as y from dw_wda_websites_summary_hm where 1=1');
              
													if(pFromDate is not null and pToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
													end if;
                
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
													end if;
                
													set vSelectSql = concat(vSelectSql, ' order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;   
											-- 判断 新访客比率/平均访问 结束
											end if;

									-- kpi2 结束		
									end if;
				-- 一天 按时按日 结束
				end if;
        
     
     -- 选择多天
     elseif(pFromDate <> pToDate) then
        
        -- 选择多天-按时
        if(pTimeDim = 1) then
						-- 新访客比率
											if(pCurKpi1 = 'nuv_rate') then
													set vSelectSql = concat('select pt_h as x,snuv/suv as y from (select
													pt_h,sum(nuv) as snuv ,sum(uv) as suv from dw_wda_websites_summary_hm
													where 1=1');
													-- set vSelectSql = concat('select pt_h as x, SUM(', pCurKpi1, ') as y from dw_wda_websites_summary_hm where 1=1');
													if(pFromDate is not null and pToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' group by pt_h)t');
													end if;
              
													set vSelectSql = concat(vSelectSql, ' order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;

											-- 平均访问时长
											elseif(pCurKpi1 = 'avg_visit_time') then
													set vSelectSql = concat('select pt_h as x,stotal_visit_time/svisits as y from (select
															pt_h,sum(total_visit_time) as stotal_visit_time ,sum(visits) as svisits from dw_wda_websites_summary_hm
															where 1=1');
													if(pFromDate is not null and pToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_h)t');
													end if;
              
													set vSelectSql = concat(vSelectSql, ' order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;
													execute stmt;

											-- 平均访问页面
											elseif(pCurKpi1 = 'avg_visit_pages') then
													set vSelectSql = concat('select pt_h as x,stotal_visit_pages/svisits as y from (select
															pt_h,sum(total_visit_pages) as stotal_visit_pages ,sum(visits) as svisits from dw_wda_websites_summary_hm
															where 1=1');
	
													if(pFromDate is not null and pToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_h)t');
													end if;
              
													set vSelectSql = concat(vSelectSql, ' order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;
											-- 一般
											else
													set vSelectSql = concat('select pt_h as x, SUM(', pCurKpi1, ') as y from dw_wda_websites_summary_hm where 1=1');
													if(pFromDate is not null and pToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
													end if;
                
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
													end if;
                
													set vSelectSql = concat(vSelectSql, ' group by pt_h order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;   
													
											-- 判断 新访客比率/平均访问 结束
											end if;
			
            
              
			-- 选择多天-按时-环比
            if(pIsHuanBi) then

								if(pCurKpi1 = 'nuv_rate') then
													set vSelectSql = concat('select pt_h as x,snuv/suv as y from (select
															pt_h,sum(nuv) as snuv ,sum(uv) as suv from dw_wda_websites_summary_hm
															where 1=1');
													-- set vSelectSql = concat('select pt_h as x, SUM(', pCurKpi1, ') as y from dw_wda_websites_summary_hm where 1=1');
													if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d < ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_h)t');
													end if;
                  
													set vSelectSql = concat(vSelectSql, ' group by pt_h order by x');
                  
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;

											-- 平均访问时长
											elseif(pCurKpi1 = 'avg_visit_time') then
													set vSelectSql = concat('select pt_h as x,stotal_visit_time/svisits as y from (select
															pt_h,sum(total_visit_time) as stotal_visit_time ,sum(visits) as svisits from dw_wda_websites_summary_hm
															where 1=1');
													if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d < ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_h)t');
													end if;
                  
													set vSelectSql = concat(vSelectSql, ' group by pt_h order by x');
                  
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;

											-- 平均访问页面
											elseif(pCurKpi1 = 'avg_visit_pages') then
													set vSelectSql = concat('select pt_h as x,stotal_visit_pages/svisits as y from (select
															pt_h,sum(total_visit_pages) as stotal_visit_pages ,sum(visits) as svisits from dw_wda_websites_summary_hm
															where 1=1');
	
													if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d < ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_h)t');
													end if;
                  
													set vSelectSql = concat(vSelectSql, ' group by pt_h order by x');
                  
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;
											-- 一般
											else
													set vSelectSql = concat('select pt_h as x, SUM(', pCurKpi1, ') as y from dw_wda_websites_summary_hm where 1=1');
													if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d < ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
													end if;
                  
													set vSelectSql = concat(vSelectSql, ' group by pt_h order by x');
                  
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;
													
											-- 判断 新访客比率/平均访问 结束
											end if;
              
              -- 选择多天 - 按时 - kpi2
              elseif(pCurKpi2 != '') then
											-- 新访客比率
											if(pCurKpi2 = 'nuv_rate') then
													set vSelectSql = concat('select pt_h as x,snuv/suv as y from (select
													pt_h,sum(nuv) as snuv ,sum(uv) as suv from dw_wda_websites_summary_hm
													where 1=1');
													-- set vSelectSql = concat('select pt_h as x, SUM(', pCurKpi1, ') as y from dw_wda_websites_summary_hm where 1=1');
													if(pFromDate is not null and pToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' group by pt_h)t');
													end if;
              
													set vSelectSql = concat(vSelectSql, ' order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;

											-- 平均访问时长
											elseif(pCurKpi2 = 'avg_visit_time') then
													set vSelectSql = concat('select pt_h as x,stotal_visit_time/svisits as y from (select
															pt_h,sum(total_visit_time) as stotal_visit_time ,sum(visits) as svisits from dw_wda_websites_summary_hm
															where 1=1');
													if(pFromDate is not null and pToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_h)t');
													end if;
              
													set vSelectSql = concat(vSelectSql, ' order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;
													execute stmt;

											-- 平均访问页面
											elseif(pCurKpi2 = 'avg_visit_pages') then
													set vSelectSql = concat('select pt_h as x,stotal_visit_pages/svisits as y from (select
															pt_h,sum(total_visit_pages) as stotal_visit_pages ,sum(visits) as svisits from dw_wda_websites_summary_hm
															where 1=1');
	
													if(pFromDate is not null and pToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
													end if;
              
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_h)t');
													end if;
              
													set vSelectSql = concat(vSelectSql, ' order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;
											-- 一般
											else
													set vSelectSql = concat('select pt_h as x, SUM(', pCurKpi2, ') as y from dw_wda_websites_summary_hm where 1=1');
													if(pFromDate is not null and pToDate is not null) then
															set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
													end if;
                
													if(pSiteId is not null and '' != pSiteId) then
															set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
													end if;
                
													set vSelectSql = concat(vSelectSql, ' group by pt_h order by x');
													SET @Sql = vSelectSql;
													prepare stmt FROM @Sql;    
													execute stmt;   
													
											-- 判断 新访客比率/平均访问 结束
											end if;
              end if;
    
        -- 选择多天 - 按日
        elseif(pTimeDim = 2) then
              
							-- 新访客比率
							if(pCurKpi1 = 'nuv_rate') then
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
							elseif(pCurKpi1 = 'avg_visit_time') then
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
							elseif(pCurKpi1 = 'avg_visit_pages') then
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
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, ', pCurKpi1, ' as y from dw_wda_websites_summary_dm where 1=1');
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 判断 新访客比率/平均访问 结束
							end if;
              
            -- 选择多天 - 按日 - 环比
             if(pIsHuanBi) then
              
									-- 新访客比率
							if(pCurKpi1 = 'nuv_rate') then
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,snuv/suv as y from (select
											pt_d,sum(nuv) as snuv ,sum(uv) as suv from dw_wda_websites_summary_dm
											where 1=1');

									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
                      set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
                  end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' group by pt_d)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;

							-- 平均访问时长
							elseif(pCurKpi1 = 'avg_visit_time') then
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,stotal_visit_time/svisits as y from (select
											pt_d,sum(total_visit_time) as stotal_visit_time ,sum(visits) as svisits from dw_wda_websites_summary_dm
											where 1=1');
									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
                      set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
                  end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_d)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;
									execute stmt;

							-- 平均访问页面
							elseif(pCurKpi1 = 'avg_visit_pages') then
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x,stotal_visit_pages/svisits as y from (select
											pt_d,sum(total_visit_pages) as stotal_visit_pages ,sum(visits) as svisits from dw_wda_websites_summary_dm
											where 1=1');

									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
                      set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
                  end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by pt_d)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;

							else
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, ', pCurKpi1, ' as y from dw_wda_websites_summary_dm where 1=1');
									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
                      set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
                  end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 判断 新访客比率/平均访问 结束
							end if;

              -- 选择多天 - 按日 - kpi2
              elseif(pCurKpi2 != '') then
									-- 新访客比率
							if(pCurKpi2 = 'nuv_rate') then
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
							elseif(pCurKpi2 = 'avg_visit_time') then
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
							elseif(pCurKpi2 = 'avg_visit_pages') then
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
									set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, ', pCurKpi2, ' as y from dw_wda_websites_summary_dm where 1=1');
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 判断 新访客比率/平均访问 结束
							end if;
           -- 环比、kpi2 结束
           end if; 
        
        
        -- 选择多天 - 按周
        elseif(pTimeDim = 3) then
						-- 新访客比率
							if(pCurKpi1 = 'nuv_rate') then
									set vSelectSql = concat('select week as x , snuv/suv as y  from
											(select CONCAT(DATE_FORMAT(DATE_SUB(pt_d, INTERVAL WEEKDAY(pt_d) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(pt_d, INTERVAL 6 - WEEKDAY(pt_d) DAY),''%Y/%m/%d'' )) as week,
											sum(uv) as suv, sum(nuv) as snuv from dw_wda_websites_summary_dm where 1=1');

									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' group by week)t ');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 平均访问时长
							elseif(pCurKpi1 = 'avg_visit_time') then
									set vSelectSql = concat('select week as x , stotal_visit_time /svisits as y   from
											(select CONCAT(DATE_FORMAT(DATE_SUB(pt_d, INTERVAL WEEKDAY(pt_d) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(pt_d, INTERVAL 6 - WEEKDAY(pt_d) DAY),''%Y/%m/%d'' )) as week,
											sum(total_visit_time) as stotal_visit_time, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by week)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;
									execute stmt;

							-- 平均访问页面
							elseif(pCurKpi1 = 'avg_visit_pages') then
									set vSelectSql = concat('select week as x , stotal_visit_pages /svisits as y  from
											(select CONCAT(DATE_FORMAT(DATE_SUB(pt_d, INTERVAL WEEKDAY(pt_d) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(pt_d, INTERVAL 6 - WEEKDAY(pt_d) DAY),''%Y/%m/%d'' )) as week,
											sum(total_visit_pages) as stotal_visit_pages, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by week)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;

							else
									set vSelectSql = CONCAT('select CONCAT(DATE_FORMAT(DATE_SUB(pt_d, INTERVAL WEEKDAY(pt_d) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(pt_d, INTERVAL 6 - WEEKDAY(pt_d) DAY),''%Y/%m/%d'' )) as x, SUM(',pCurKpi1,') as y from dw_wda_websites_summary_dm where 1=1');
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' group by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 判断 新访客比率/平均访问 结束
							end if;

        -- 选择多天 - 按周 - 环比
        if(pIsHuanBi) then
						-- 新访客比率
							if(pCurKpi1 = 'nuv_rate') then
									set vSelectSql = concat('select week as x , snuv/suv as y  from
											(select CONCAT(DATE_FORMAT(DATE_SUB(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day), INTERVAL WEEKDAY(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day)) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day), INTERVAL (6 - WEEKDAY(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day))) DAY),''%Y/%m/%d'' )) as week,
											sum(uv) as suv, sum(nuv) as snuv from dw_wda_websites_summary_dm where 1=1');

									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' group by week)t ');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 平均访问时长
							elseif(pCurKpi1 = 'avg_visit_time') then
									set vSelectSql = concat('select week as x , stotal_visit_time /svisits as y   from
											(select CONCAT(DATE_FORMAT(DATE_SUB(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day), INTERVAL WEEKDAY(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day)) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day), INTERVAL (6 - WEEKDAY(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day))) DAY),''%Y/%m/%d'' )) as week,
											sum(total_visit_time) as stotal_visit_time, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by week)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;
									execute stmt;

							-- 平均访问页面
							elseif(pCurKpi1 = 'avg_visit_pages') then
									set vSelectSql = concat('select week as x , stotal_visit_pages /svisits as y  from
											(select CONCAT(DATE_FORMAT(DATE_SUB(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day), INTERVAL WEEKDAY(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day)) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day), INTERVAL (6 - WEEKDAY(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day))) DAY),''%Y/%m/%d'' )) as week,
											sum(total_visit_pages) as stotal_visit_pages, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by week)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;

							else
									set vSelectSql = CONCAT('select CONCAT(DATE_FORMAT(DATE_SUB(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day), INTERVAL WEEKDAY(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day)) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day), INTERVAL (6 - WEEKDAY(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day))) DAY),''%Y/%m/%d'' )) as x, SUM(',pCurKpi1,') as y from dw_wda_websites_summary_dm where 1=1');
         
									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
									end if;
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
									set vSelectSql = concat(vSelectSql, ' group by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 判断 新访客比率/平均访问 结束
							end if;

        
        -- 选择多天 - 按周 - kpi2
        elseif (pCurKpi2 != '') then
						-- 新访客比率
							if(pCurKpi2 = 'nuv_rate') then
									set vSelectSql = concat('select week as x , snuv/suv as y  from
											(select CONCAT(DATE_FORMAT(DATE_SUB(pt_d, INTERVAL WEEKDAY(pt_d) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(pt_d, INTERVAL 6 - WEEKDAY(pt_d) DAY),''%Y/%m/%d'' )) as week,
											sum(uv) as suv, sum(nuv) as snuv from dw_wda_websites_summary_dm where 1=1');

									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' group by week)t ');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 平均访问时长
							elseif(pCurKpi2 = 'avg_visit_time') then
									set vSelectSql = concat('select week as x , stotal_visit_time /svisits as y   from
											(select CONCAT(DATE_FORMAT(DATE_SUB(pt_d, INTERVAL WEEKDAY(pt_d) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(pt_d, INTERVAL 6 - WEEKDAY(pt_d) DAY),''%Y/%m/%d'' )) as week,
											sum(total_visit_time) as stotal_visit_time, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by week)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;
									execute stmt;

							-- 平均访问页面
							elseif(pCurKpi2 = 'avg_visit_pages') then
									set vSelectSql = concat('select week as x , stotal_visit_pages /svisits as y  from
											(select CONCAT(DATE_FORMAT(DATE_SUB(pt_d, INTERVAL WEEKDAY(pt_d) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(pt_d, INTERVAL 6 - WEEKDAY(pt_d) DAY),''%Y/%m/%d'' )) as week,
											sum(total_visit_pages) as stotal_visit_pages, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by week)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;

							else
									set vSelectSql = CONCAT('select CONCAT(DATE_FORMAT(DATE_SUB(pt_d, INTERVAL WEEKDAY(pt_d) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(pt_d, INTERVAL 6 - WEEKDAY(pt_d) DAY),''%Y/%m/%d'' )) as x, SUM(',pCurKpi2,') as y from dw_wda_websites_summary_dm where 1=1');
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' group by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 判断 新访客比率/平均访问 结束
							end if;
         end if;


         -- 选择多天 - 按月
        elseif(pTimeDim = 4) then
							-- 新访客比率
							if(pCurKpi1 = 'nuv_rate') then
									set vSelectSql = concat('select months as x , snuv/suv as y  from
											(select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as months,
											sum(uv) as suv, sum(nuv) as snuv from dw_wda_websites_summary_dm where 1=1');

									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' group by months)t ');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 平均访问时长
							elseif(pCurKpi1 = 'avg_visit_time') then
									set vSelectSql = concat('select months as x , stotal_visit_time /svisits as y   from
											(select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as months,
											sum(total_visit_time) as stotal_visit_time, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by months)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;
									execute stmt;

							-- 平均访问页面
							elseif(pCurKpi1 = 'avg_visit_pages') then
									set vSelectSql = concat('select months as x , stotal_visit_pages /svisits as y  from
											(select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as months,
											sum(total_visit_pages) as stotal_visit_pages, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by months)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;

							else
									set vSelectSql =CONCAT('select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as x,SUM(',pCurKpi1,') as y from dw_wda_websites_summary_dm where 1=1');
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' group by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
									
							-- 判断 新访客比率/平均访问 结束
							end if;

          -- 选择多天 - 按月 - 环比
          if(pIsHuanBi) then
							-- 新访客比率
							if(pCurKpi1 = 'nuv_rate') then
									set vSelectSql = concat('select months as x , snuv/suv as y  from
											(select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as months,
											sum(uv) as suv, sum(nuv) as snuv from dw_wda_websites_summary_dm where 1=1');

									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' group by months)t ');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 平均访问时长
							elseif(pCurKpi1 = 'avg_visit_time') then
									set vSelectSql = concat('select months as x , stotal_visit_time /svisits as y   from
											(select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as months,
											sum(total_visit_time) as stotal_visit_time, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by months)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;
									execute stmt;

							-- 平均访问页面
							elseif(pCurKpi1 = 'avg_visit_pages') then
									set vSelectSql = concat('select months as x , stotal_visit_pages /svisits as y  from
											(select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as months,
											sum(total_visit_pages) as stotal_visit_pages, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by months)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;

							else
									set vSelectSql =CONCAT('select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as x,SUM(',pCurKpi1,') as y from dw_wda_websites_summary_dm where 1=1');
									if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' group by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
									
							-- 判断 新访客比率/平均访问 结束
							end if;
           
						-- 选择多天 - 按月 - kpi2
            elseif (pCurKpi2 != '') then
									-- 新访客比率
							if(pCurKpi2 = 'nuv_rate') then
									set vSelectSql = concat('select months as x , snuv/suv as y  from
											(select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as months,
											sum(uv) as suv, sum(nuv) as snuv from dw_wda_websites_summary_dm where 1=1');

									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' group by months)t ');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
							-- 平均访问时长
							elseif(pCurKpi2 = 'avg_visit_time') then
									set vSelectSql = concat('select months as x , stotal_visit_time /svisits as y   from
											(select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as months,
											sum(total_visit_time) as stotal_visit_time, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by months)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;
									execute stmt;

							-- 平均访问页面
							elseif(pCurKpi2 = 'avg_visit_pages') then
									set vSelectSql = concat('select months as x , stotal_visit_pages /svisits as y  from
											(select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as months,
											sum(total_visit_pages) as stotal_visit_pages, sum(visits) as svisits
											from dw_wda_websites_summary_dm where 1=1');
									
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, '''  group by months)t');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' order by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;

							else
									set vSelectSql =CONCAT('select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as x,SUM(',pCurKpi2,') as y from dw_wda_websites_summary_dm where 1=1');
									if(pFromDate is not null and pToDate is not null) then
											set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
									end if;
              
									if(pSiteId is not null and '' != pSiteId) then
											set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
									end if;
              
									set vSelectSql = concat(vSelectSql, ' group by x');
									SET @Sql = vSelectSql;
									prepare stmt FROM @Sql;    
									execute stmt;
									
							-- 判断 新访客比率/平均访问 结束
							end if;
           -- 选择多天 - 按月 环比、kpi2 结束     
           end if;
        end if;
     
     -- 
    elseif(pPeriod = 99) then
    set vSelectSql = '';
          
    end if;
    
    set pRetCode = 0;
END//