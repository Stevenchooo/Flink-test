delimiter //
DROP PROCEDURE IF EXISTS sp_visitor_area_distribution_map;
CREATE PROCEDURE sp_visitor_area_distribution_map
(
    OUT pRetCode    INT,
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pCurKpi     VARCHAR(20),
    IN  pFromDate   VARCHAR(20),
    IN  pToDate     VARCHAR(20)
    
)
COMMENT ''
proc: BEGIN

    declare vSelectSql varchar(1000);
    
    -- 今日或昨日
    if(pFromDate = pToDate) then
    
        set vSelectSql = concat('select t1.province_name as name, ''', pCurKpi, ''' as kpiName, SUM(t1.', pCurKpi, ') as event, t2.code as id from dw_wda_visitors_area_distribution_hm t1, t_wda_province_code t2 where t1.province_name = t2.province_name ');
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and t1.pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and t1.pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and t1.site_id = ''', pSiteId, ''' ');
        end if;
        
        set vSelectSql = concat(vSelectSql, ' group by t1.province_name order by t1.pv desc');
                
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
       
       set vSelectSql = concat('select SUM(t1.', pCurKpi, ') as totalKpi from dw_wda_visitors_area_distribution_hm t1,t_wda_province_code t2 where t1.province_name = t2.province_name ');
       
       if(pFromDate is not null and pToDate is not null) then
          set vSelectSql = concat(vSelectSql, ' and t1.pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and t1.pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
       end if;
       
       if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and t1.site_id = ''', pSiteId, ''' ');
       end if;
       
       SET @Sql = vSelectSql;
       prepare stmt FROM @Sql;    
       execute stmt;
        
    -- 最近7天或30天    
    else
        
        set vSelectSql = concat('select t1.province_name as name, ''', pCurKpi, ''' as kpiName, SUM(t1.', pCurKpi, ') as event, t2.code as id from dw_wda_visitors_area_distribution_dm t1, t_wda_province_code t2 where t1.province_name = t2.province_name ');
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and t1.pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and t1.pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and t1.site_id = ''', pSiteId, ''' ');
        end if;
        
        set vSelectSql = concat(vSelectSql, ' group by t1.province_name order by t1.pv desc');
                
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
       
       set vSelectSql = concat('select SUM(', pCurKpi, ') as totalKpi from dw_wda_visitors_area_distribution_dm t1, t_wda_province_code t2 where t1.province_name = t2.province_name ');
       
       if(pFromDate is not null and pToDate is not null) then
          set vSelectSql = concat(vSelectSql, ' and t1.pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and t1.pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
       end if;
       
       if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and t1.site_id = ''', pSiteId, ''' ');
       end if;
       
       SET @Sql = vSelectSql;
       prepare stmt FROM @Sql;    
       execute stmt;
           
    end if;
    
    
    set pRetCode = 0;
    
END//

