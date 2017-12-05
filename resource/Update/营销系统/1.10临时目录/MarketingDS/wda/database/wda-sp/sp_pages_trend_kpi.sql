delimiter //
DROP PROCEDURE IF EXISTS sp_pages_trend_kpi;
CREATE PROCEDURE sp_pages_trend_kpi
(
    OUT pRetCode    INT, 
    IN  pSiteId     VARCHAR(2000),    
    IN  pPeriod     VARCHAR(128),    
    IN  pIsHuanBi   VARCHAR(128),    
    IN  pTimeDim    INT,
    IN  pCurKpi1     VARCHAR(20),
    IN  pCurKpi2     VARCHAR(20),
  IN  pPageUrl     VARCHAR(128),
    IN  pFromDate   VARCHAR(20),
    IN  pToDate     VARCHAR(20),
    IN  pHuanBiFromDate   VARCHAR(20),
    IN  pHuanBiToDate     VARCHAR(20)
)
COMMENT ''
proc: BEGIN
  
  declare vSelectSql varchar(1000);
          
    
    if(pFromDate = pToDate) then
        
        if(pTimeDim = 1) then
      set vSelectSql = concat('select pt_h as x, ', pCurKpi1, ' as y from dw_wda_pages_summary_hm where 1=1');
      if(pFromDate is not null and pToDate is not null) then
        set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
      end if;
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      if(pPageUrl is not null and '' != pPageUrl) then
        set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
      end if;       
      set vSelectSql = concat(vSelectSql, ' order by x');
      SET @Sql = vSelectSql;
      prepare stmt FROM @Sql;    
      execute stmt;
      
      if('' <> pIsHuanBi) then
        set vSelectSql = concat('select pt_h as x, ', pCurKpi1, ' as y from dw_wda_pages_summary_hm where 1=1');
          if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
          set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
          end if;    
          if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
          end if;
          if(pPageUrl is not null and '' != pPageUrl) then
          set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
        end if;
          set vSelectSql = concat(vSelectSql, ' order by x');
          SET @Sql = vSelectSql;
          prepare stmt FROM @Sql;    
          execute stmt;
      
      elseif(pCurKpi2 != '') then
        set vSelectSql = concat('select pt_h as x, ', pCurKpi2, ' as y from dw_wda_pages_summary_hm where 1=1');
          if(pFromDate is not null and pToDate is not null) then
          set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
        end if;
        if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        if(pPageUrl is not null and '' != pPageUrl) then
          set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
        end if;
        set vSelectSql = concat(vSelectSql, ' order by x');
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;   
      end if;
        
        elseif(pTimeDim = 2) then
          set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, SUM(', pCurKpi1, ') as y from dw_wda_pages_summary_hm where 1=1');
      if(pFromDate is not null and pToDate is not null) then
        set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
      end if;
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;        
      if(pPageUrl is not null and '' != pPageUrl) then
        set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
      end if;
          set vSelectSql = concat(vSelectSql, ' order by x');
      SET @Sql = vSelectSql;
      prepare stmt FROM @Sql;    
      execute stmt;
      
      if('' <> pIsHuanBi) then
        set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, SUM(', pCurKpi1, ') as y from dw_wda_pages_summary_hm where 1=1');        
          if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
          set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
          end if;
          if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
          end if;
        if(pPageUrl is not null and '' != pPageUrl) then
          set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
        end if;
            set vSelectSql = concat(vSelectSql, ' order by x');
          SET @Sql = vSelectSql;
          prepare stmt FROM @Sql;    
          execute stmt;
      
      elseif(pCurKpi2 != '') then
        set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, SUM(', pCurKpi2, ') as y from dw_wda_pages_summary_hm where 1=1');
        if(pFromDate is not null and pToDate is not null) then
          set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
        end if;
        if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        if(pPageUrl is not null and '' != pPageUrl) then
          set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
        end if;
            set vSelectSql = concat(vSelectSql, ' order by x');
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;   
      end if;
        end if;
     
     elseif(pFromDate <> pToDate) then
        
        if(pTimeDim = 1) then
      set vSelectSql = concat('select pt_h as x, SUM(', pCurKpi1, ') as y from dw_wda_pages_summary_hm where 1=1');
      if(pFromDate is not null and pToDate is not null) then
        set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
      end if;
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      if(pPageUrl is not null and '' != pPageUrl) then
        set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
      end if;
      set vSelectSql = concat(vSelectSql, ' group by pt_h order by x');
      SET @Sql = vSelectSql;
      prepare stmt FROM @Sql;    
      execute stmt;
      
      if('' <> pIsHuanBi) then
        set vSelectSql = concat('select pt_h as x, SUM(', pCurKpi1, ') as y from dw_wda_pages_summary_hm where 1=1');
          if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
              set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
          end if;
          if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
          end if;
          if(pPageUrl is not null and '' != pPageUrl) then
          set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
        end if;  
          set vSelectSql = concat(vSelectSql, ' group by pt_h order by x');
          SET @Sql = vSelectSql;
          prepare stmt FROM @Sql;    
          execute stmt;
      
      elseif(pCurKpi2 != '') then
          set vSelectSql = concat('select pt_h as x, SUM(', pCurKpi2, ') as y from dw_wda_pages_summary_hm where 1=1');
          if(pFromDate is not null and pToDate is not null) then
          set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
        end if;
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        if(pPageUrl is not null and '' != pPageUrl) then
          set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
        end if;
            set vSelectSql = concat(vSelectSql, ' group by pt_h order by x');
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;   
      end if;
        
        elseif(pTimeDim = 2) then
      set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, ', pCurKpi1, ' as y from dw_wda_pages_summary_dm where 1=1');
        if(pFromDate is not null and pToDate is not null) then
        set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
      end if;
      if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      if(pPageUrl is not null and '' != pPageUrl) then
        set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
      end if;
          set vSelectSql = concat(vSelectSql, ' order by x');
      SET @Sql = vSelectSql;
      prepare stmt FROM @Sql;    
      execute stmt;
      
      if('' <> pIsHuanBi) then
        set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, ', pCurKpi1, ' as y from dw_wda_pages_summary_dm where 1=1');
          if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
          set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
          end if;
          if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
          end if;
          if(pPageUrl is not null and '' != pPageUrl) then
          set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
        end if;
            set vSelectSql = concat(vSelectSql, ' order by x');
          SET @Sql = vSelectSql;
          prepare stmt FROM @Sql;
          execute stmt;
      
      elseif(pCurKpi2 != '') then
          set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, ', pCurKpi2, ' as y from dw_wda_pages_summary_dm where 1=1');    
        if(pFromDate is not null and pToDate is not null) then
          set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
        end if;
        if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        if(pPageUrl is not null and '' != pPageUrl) then
          set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
        end if;
            set vSelectSql = concat(vSelectSql, ' order by x');
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;   
      end if; 
        
        
        -- 按周返回记录
        elseif(pTimeDim = 3) then
          set vSelectSql = CONCAT('select CONCAT(DATE_FORMAT(DATE_SUB(pt_d, INTERVAL WEEKDAY(pt_d) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(pt_d, INTERVAL 6 - WEEKDAY(pt_d) DAY),''%Y/%m/%d'' )) as x, SUM(',pCurKpi1,') as y from dw_wda_pages_summary_dm where 1=1');
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

        --
        if(pIsHuanBi) then
        set vSelectSql = CONCAT('select CONCAT(DATE_FORMAT(DATE_SUB(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day), INTERVAL WEEKDAY(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day)) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day), INTERVAL 6 - WEEKDAY(DATE_ADD(pt_d, INTERVAL ',datediff(pFromDate,pHuanBiFromDate),' day)) DAY),''%Y/%m/%d'' )) as x, SUM(',pCurKpi1,') as y from dw_wda_pages_summary_dm where 1=1');
         
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
        --
        elseif (pCurKpi2 != '') then
         set vSelectSql = CONCAT('select CONCAT(DATE_FORMAT(DATE_SUB(pt_d, INTERVAL WEEKDAY(pt_d) DAY),''%Y/%m/%d'' ) , ''-'', DATE_FORMAT(DATE_ADD(pt_d, INTERVAL 6 - WEEKDAY(pt_d) DAY),''%Y/%m/%d'' )) as x, SUM(',pCurKpi2,') as y from dw_wda_pages_summary_dm where 1=1');
         
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
         end if;
         -- 按月记录
        elseif(pTimeDim = 4) then
          set vSelectSql =CONCAT('select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as x,SUM(',pCurKpi1,') as y from dw_wda_pages_summary_dm where 1=1');
           
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
          --
          if(pIsHuanBi) then
           set vSelectSql = CONCAT('select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as x,SUM(',pCurKpi1,') as y from dw_wda_pages_summary_dm where 1=1');
           
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
            elseif (pCurKpi2 != '') then
                set vSelectSql =CONCAT('select concat(substr(pt_d,1,4),''/'',substr(pt_d,5,2)) as x,SUM(',pCurKpi2,') as y from dw_wda_pages_summary_dm where 1=1');
           
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
           end if;
        end if;
    
    
    elseif(pPeriod = 99) then
    set vSelectSql = '';  
    end if;
    
  set pRetCode = 0;
  
END//
