delimiter //
DROP PROCEDURE IF EXISTS sp_website_referrer_trend2_summary;
CREATE PROCEDURE sp_website_referrer_trend2_summary
(
    OUT pRetCode    INT,    
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pIsHuanBi   VARCHAR(128),   
    IN  pFromDate   VARCHAR(20),
    IN  pToDate     VARCHAR(20),
    IN  pHuanBiFromDate   VARCHAR(20),
    IN  pHuanBiToDate     VARCHAR(20),
    IN  pReferrerName     VARCHAR(20)
)
COMMENT ''
proc: BEGIN

    declare vSelectSql varchar(1000);
    
    -- 今日或昨日
    if(pFromDate = pToDate and pFromDate = curdate()) then
        
        set vSelectSql = concat('select SUM(pv) as pv, round(SUM(bounces)/SUM(visits),4) as bounce_rate, round(SUM(total_visit_time)/SUM(visits),2) as avg_visit_time, round(SUM(total_visit_pages)/SUM(visits),2) as avg_visit_pages from dw_wda_websites_summary_by_referrer_hm where 1=1 ');
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        if(pReferrerName is not null and '' != pReferrerName) then
      set vSelectSql = concat(vSelectSql, ' and referrer_name = ''', pReferrerName, ''' ');
    end if;
        
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
       -- 查询环比数据
    if('' <> pIsHuanBi) then
          
      set vSelectSql = concat('select SUM(pv) as pv, round(SUM(bounces)/SUM(visits),4) as bounce_rate, round(SUM(total_visit_time)/SUM(visits),2) as avg_visit_time, round(SUM(total_visit_pages)/SUM(visits),2) as avg_visit_pages from dw_wda_websites_summary_by_referrer_hm where 1=1 ');
            
      if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
                set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
      end if;
        
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
           
      if(pReferrerName is not null and '' != pReferrerName) then
        set vSelectSql = concat(vSelectSql, ' and referrer_name = ''', pReferrerName, ''' ');
      end if;
           
      SET @Sql = vSelectSql;
      prepare stmt FROM @Sql;    
      execute stmt;
      
        end if;
        
    -- 最近7天或30天    
    else
        
        set vSelectSql = concat('select SUM(pv) as pv, round(SUM(bounces)/SUM(visits),4) as bounce_rate, round(SUM(total_visit_time)/SUM(visits),2) as avg_visit_time, round(SUM(total_visit_pages)/SUM(visits),2) as avg_visit_pages from dw_wda_websites_summary_by_referrer_dm where 1=1 ');
        
    if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        if(pReferrerName is not null and '' != pReferrerName) then
      set vSelectSql = concat(vSelectSql, ' and referrer_name = ''', pReferrerName, ''' ');
    end if;
        
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
    -- 查询环比数据
    if('' <> pIsHuanBi) then
    
      set vSelectSql = concat('select SUM(pv) as pv, round(SUM(bounces)/SUM(visits),4) as bounce_rate, round(SUM(total_visit_time)/SUM(visits),2) as avg_visit_time, round(SUM(total_visit_pages)/SUM(visits),2) as avg_visit_pages from dw_wda_websites_summary_by_referrer_dm where 1=1 ');
      
      if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
        set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
      end if;
        
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
           
      if(pReferrerName is not null and '' != pReferrerName) then
        set vSelectSql = concat(vSelectSql, ' and referrer_name = ''', pReferrerName, ''' ');
      end if;
           
           SET @Sql = vSelectSql;
           prepare stmt FROM @Sql;    
           execute stmt;
        
        end if;
           
    end if;
    
    
    set pRetCode = 0;
    
    
END//
