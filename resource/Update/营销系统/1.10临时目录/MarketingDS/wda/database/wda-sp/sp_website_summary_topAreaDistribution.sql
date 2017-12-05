delimiter //
DROP PROCEDURE IF EXISTS sp_website_summary_topAreaDistribution;
CREATE PROCEDURE sp_website_summary_topAreaDistribution
(
    OUT pRetCode    INT,
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pFromDate   VARCHAR(20),
    IN  pToDate     VARCHAR(20)
    
)
COMMENT ''
proc: BEGIN

    declare vSelectSql varchar(1000);
    
    -- 选择今天昨天或者一天
    if(pFromDate = pToDate) then
    
        set vSelectSql = 'select t1.province_name as name, SUM(t1.pv) as event, t2.code as id from dw_wda_visitors_area_distribution_hm t1, t_wda_province_code t2 where t1.province_name = t2.province_name ';
        set vSelectSql = concat(vSelectSql, ' and t1.pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and t1.pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        
    -- 选择多个日期天数  
    else
        
        set vSelectSql = 'select t1.province_name as name, SUM(t1.pv) as event, t2.code as id from dw_wda_visitors_area_distribution_dm t1, t_wda_province_code t2 where t1.province_name = t2.province_name ';
        set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
           
    end if;
    
    
    set vSelectSql = concat(vSelectSql, ' and t1.site_id = ''', pSiteId, ''' ');
    
    set vSelectSql = concat(vSelectSql, ' group by t1.province_name order by event desc');
                
    SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
    
    set pRetCode = 0;
    
END//