delimiter //
DROP PROCEDURE IF EXISTS sp_pages_trend_summary;
CREATE PROCEDURE sp_pages_trend_summary
(
    OUT pRetCode    INT,    
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pIsHuanBi   VARCHAR(128),
	IN  pPageUrl     VARCHAR(128),
    IN  pFromDate   VARCHAR(20),
    IN  pToDate     VARCHAR(20),
    IN  pHuanBiFromDate   VARCHAR(20),
    IN  pHuanBiToDate     VARCHAR(20)
)
COMMENT ''
proc: BEGIN

    declare vSelectSql varchar(1000);
    
    -- 今日或昨日
    if(pFromDate = pToDate) then
    
        set vSelectSql = concat('select SUM(pv) as pv, SUM(uv) as uv, SUM(exit_pages) as exit_pages, round(SUM(exit_pages)/sum(visits),4) as exit_rate from dw_wda_pages_summary_hm where 1=1');
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        if(pPageUrl is not null and '' != pPageUrl) then
            set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
        end if;
        
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
       -- 查询环比数据
       if('' <> pIsHuanBi) then
        
           
           set vSelectSql = concat('select SUM(pv) as pv, SUM(uv) as uv, SUM(exit_pages) as exit_pages, round(SUM(exit_pages)/sum(visits),4) as exit_rate from dw_wda_pages_summary_hm where 1=1');
            
           if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
                set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
           end if;
        
           if(pSiteId is not null and '' != pSiteId) then
                set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
           end if;
           
           if(pPageUrl is not null and '' != pPageUrl) then
            	  set vSelectSql = concat(vSelectSql, ' and page_url = ''', pPageUrl, ''' ');
        	 end if;
        
           SET @Sql = vSelectSql;
           prepare stmt FROM @Sql;    
           execute stmt;
        
        end if;
        
    -- 最近7天或30天    
    else
        
        set vSelectSql = concat('select count(1) as total, SUM(pv) as pv, SUM(uv) as uv, SUM(exit_pages) as exit_pages, round(SUM(exit_pages)/sum(visits),4) as exit_rate  from dw_wda_pages_summary_dm where 1=1');
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
       -- 查询环比数据
       if('' <> pIsHuanBi) then
        
           -- set vSelectSql = concat('select SUM(pv) as pv, SUM(visits) as visit, SUM(ip) as ip,  round(sum(bounces) / sum(visits), 2) AS bounce_rate  from dw_wda_websites_summary_hm where 1=1');
           set vSelectSql = concat('select count(1) as total, SUM(pv) as pv, SUM(uv) as uv, SUM(exit_pages) as exit_pages, round(SUM(exit_pages)/sum(visits),4) as exit_rate from dw_wda_pages_summary_dm where 1=1');
            
           if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
                set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
           end if;
        
           if(pSiteId is not null and '' != pSiteId) then
                set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
           end if;
           
           SET @Sql = vSelectSql;
           prepare stmt FROM @Sql;    
           execute stmt;
        
        end if;
           
    end if;
    
    
    set pRetCode = 0;
    
    
END//
