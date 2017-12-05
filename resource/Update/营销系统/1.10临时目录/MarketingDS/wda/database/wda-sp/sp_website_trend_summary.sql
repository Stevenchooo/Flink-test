delimiter //
DROP PROCEDURE IF EXISTS sp_website_trend_summary;
CREATE PROCEDURE sp_website_trend_summary
(
    OUT pRetCode    INT,
    IN  pIsHuanBi   VARCHAR(128),
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pFromDate   VARCHAR(20),
    IN  pToDate     VARCHAR(20),
    IN  pHuanBiFromDate   VARCHAR(20),
    IN  pHuanBiToDate     VARCHAR(20)
)
COMMENT ''
proc: BEGIN

    declare vSelectSql varchar(1000);
    declare vSelectCurDaySql varchar(1000);
    
   
    if(pFromDate = pToDate and pFromDate = curdate()) then
    
        set vSelectSql = concat('select SUM(pv) as pv, SUM(visits) as visit, SUM(ip) as ip,  round(sum(bounces) / sum(visits), 4) AS bounce_rate  from dw_wda_websites_summary_hm where 1=1');
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
       
       if('' != pIsHuanBi) then
        
           set vSelectSql = concat('select SUM(pv) as pv, SUM(visits) as visit, SUM(ip) as ip,  round(sum(bounces) / sum(visits), 4) AS bounce_rate  from dw_wda_websites_summary_hm where 1=1');
            
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
        
    -- 
    else
        
        set vSelectSql = concat('select SUM(pv) as pv, SUM(visits) as visit, SUM(ip) as ip, SUM(bounces) as bounces, round(sum(bounces) / sum(visits), 4) AS bounce_rate  from dw_wda_websites_summary_dm where 1=1');
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
        -- 
        if('' != pIsHuanBi) then
        
        		set vSelectSql = concat('select SUM(pv) as pv, SUM(visits) as visit, SUM(ip) as ip, SUM(bounces) as bounces, round(sum(bounces) / sum(visits), 4) AS bounce_rate  from dw_wda_websites_summary_dm where 1=1');
        
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
