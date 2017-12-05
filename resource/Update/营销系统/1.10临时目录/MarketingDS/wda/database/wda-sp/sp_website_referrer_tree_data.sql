delimiter //
DROP PROCEDURE IF EXISTS sp_website_referrer_tree_data;
CREATE PROCEDURE sp_website_referrer_tree_data
(
    OUT pRetCode    INT,    
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pIsHuanBi   VARCHAR(128),
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
  
        set vSelectSql = concat('select referrer_type,  case referrer_type when 1 then ''广告导流'' when 2 then ''链接导流'' when 3 then ''直接访问'' end as referrer_name,  SUM(pv) as pv, SUM(visits) as visits, round(sum(total_visit_time) / sum(visits),2) as avg_visit_time, round(sum(total_visit_pages) / sum(visits),2) as avg_visit_pages from dw_wda_websites_summary_by_referrer_hm where 1=1');
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        set vSelectSql = concat(vSelectSql, ' group by referrer_type');
        
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
        -- 查询广告导流top10
        set vSelectSql = 'select referrer_type, referrer_name, sum(pv) as pv, sum(visits) as visits, round(sum(total_visit_time) / sum(visits),2) as avg_visit_time, round(sum(total_visit_pages) / sum(visits),2) as avg_visit_pages from dw_wda_websites_referrer_topn_hm where 1=1 ';
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        set vSelectSql = concat(vSelectSql, ' and referrer_type = 1 and referrer_name is not null group by referrer_type, referrer_name order by pv desc limit 10');
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
        -- 查询外链top10
        set vSelectSql = 'select referrer_type, referrer_name, sum(pv) as pv, sum(visits) as visits, round(sum(total_visit_time) / sum(visits),2) as avg_visit_time, round(sum(total_visit_pages) / sum(visits),2) as avg_visit_pages from dw_wda_websites_referrer_topn_hm where 1=1 ';
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        set vSelectSql = concat(vSelectSql, ' and referrer_type = 2 and referrer_name is not null group by referrer_type, referrer_name order by pv desc limit 10');
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
    
    -- 最近7天或30天    
    else
        
    set vSelectSql = concat('select referrer_type,  case referrer_type when 1 then ''广告导流'' when 2 then ''链接导流'' when 3 then ''直接访问'' end as referrer_name,  SUM(pv) as pv, SUM(visits) as visits, round(sum(total_visit_time) / sum(visits),2) as avg_visit_time, round(sum(total_visit_pages) / sum(visits),2) as avg_visit_pages from dw_wda_websites_summary_by_referrer_dm where 1=1');
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        set vSelectSql = concat(vSelectSql, ' group by referrer_type');
        
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
        -- 查询广告导流top10
        set vSelectSql = 'select referrer_type, referrer_name, sum(pv) as pv, sum(visits) as visits, round(sum(total_visit_time) / sum(visits),2) as avg_visit_time, round(sum(total_visit_pages) / sum(visits),2) as avg_visit_pages from dw_wda_websites_referrer_topn_dm where 1=1 ';
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        set vSelectSql = concat(vSelectSql, ' and referrer_type = 1 and referrer_name is not null group by referrer_type, referrer_name order by pv desc limit 10');
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
        -- 查询外链top10
        set vSelectSql = 'select referrer_type, referrer_name, sum(pv) as pv, sum(visits) as visits, round(sum(total_visit_time) / sum(visits),2) as avg_visit_time, round(sum(total_visit_pages) / sum(visits),2) as avg_visit_pages from dw_wda_websites_referrer_topn_dm where 1=1 ';
        
        if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
        end if;
        
        if(pSiteId is not null and '' != pSiteId) then
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        
        set vSelectSql = concat(vSelectSql, ' and referrer_type = 2 and referrer_name is not null group by referrer_type, referrer_name order by pv desc limit 10');
        SET @Sql = vSelectSql;
        prepare stmt FROM @Sql;    
        execute stmt;
        
    end if;
    
    
    set pRetCode = 0;
    
    
END//
