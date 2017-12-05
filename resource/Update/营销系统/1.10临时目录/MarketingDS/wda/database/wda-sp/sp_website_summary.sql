delimiter //
DROP PROCEDURE IF EXISTS sp_website_summary;
CREATE PROCEDURE sp_website_summary
(
    OUT pRetCode    INT, 
    IN  pSiteId     VARCHAR(2000), 
    IN  pCurDate    VARCHAR(20)
)
COMMENT ''
proc: BEGIN

    declare vSelectSql1 varchar(1000);
    declare vSelectSql2 varchar(1000);
    declare vSelectSql3 varchar(1000);
    declare vSelectSql4 varchar(1000);
    declare vSelectSql5 varchar(1000);
    declare vSelectSql6 varchar(1000);
    
    -- 今天已经最大小时记录数
    declare cnt INT DEFAULT 0;
    
    declare curDate2 VARCHAR(20);
    
  set curDate2 = DATE_FORMAT(pCurDate, '%Y%m%d');
    
    -- 今日此时 统计项,  把每个小时的记录相加
    
    set vSelectSql1 = concat('
        SELECT
          1 as ord,
          max(pt_h) as curHour,
          '''' as row_header,
          sum(pv) AS pv,
          sum(uv) AS uv,
          sum(nuv) AS nuv,
          round(sum(nuv) / sum(uv), 4) AS nuv_rate,
          sum(ip) AS ip,
          sum(visits) AS visits,
          sum(total_visit_time) AS total_visit_time,
          round(sum(total_visit_time) / sum(visits), 2) AS avg_visit_time,
          sum(total_visit_pages) AS total_visit_pages,
          round(sum(total_visit_pages) / sum(visits), 2) AS avg_visit_pages,
          sum(bounces) AS bounces,
          round(sum(bounces) / sum(visits), 4) AS bounce_rate
        FROM
          dw_wda_websites_summary_hm
        WHERE
          1 = 1');
          
    
  set vSelectSql1 = concat(vSelectSql1, ' and site_id = ''', pSiteId, ''' ');
    set vSelectSql1 = concat(vSelectSql1, ' and pt_d = ', DATE_FORMAT(pCurDate, '%Y%m%d'));
    
    SET @Sql = vSelectSql1;
    prepare stmt FROM @Sql;    
    execute stmt;
    
    
    -- 查询网站 昨日 的统计指标，查询天表
    set vSelectSql2 = concat('
        SELECT
          2 as ord,
          0 as curHour,
          '''' as row_header,
          sum(pv) AS pv,
          sum(uv) AS uv,
          sum(nuv) AS nuv,
          round(sum(nuv) / sum(uv), 4) AS nuv_rate,
          sum(ip) AS ip,
          sum(visits) AS visits,
          sum(total_visit_time) AS total_visit_time,
          round(sum(total_visit_time) / sum(visits), 2) AS avg_visit_time,
          sum(total_visit_pages) AS total_visit_pages,
          round(sum(total_visit_pages) / sum(visits), 2) AS avg_visit_pages,
          sum(bounces) AS bounces,
          round(sum(bounces) / sum(visits), 4) AS bounce_rate
        FROM
          dw_wda_websites_summary_dm
        WHERE
          1 = 1');
    
    
    set vSelectSql2 = concat(vSelectSql2, ' and site_id = ''', pSiteId, ''' ');
    set vSelectSql2 = concat(vSelectSql2, ' and pt_d = ', DATE_FORMAT(date_sub(pCurDate, interval 1 day), '%Y%m%d'));
    
    SET @Sql = vSelectSql2;
    prepare stmt FROM @Sql;    
    execute stmt;
    
    -- 预计今日, TODO
    set vSelectSql3 = concat('
        SELECT
          3 as ord,
          0 as curHour,
          '''' as row_header,
          0 AS pv,
          0 AS uv,
          0 AS nuv,
          0 AS nuv_rate,
          0 AS ip,
          0 AS visits,
          0 AS total_visit_time,
          0 AS avg_visit_time,
          0 AS total_visit_pages,
          0 AS avg_visit_pages,
          0 AS bounces,
          0 AS bounce_rate');
        
    -- 查询网站 昨日截止到目前的统计指标，从小时表查询。 目前时间按今天小时表中有数据的小时
    
    select max(pt_h) into cnt from dw_wda_websites_summary_hm where site_id=ifnull(pSiteId, site_id) and pt_d = curDate2;
    
    -- 没有找到记录时,into没执行，此时设置cnt为0
    set cnt = ifnull(cnt, 0);
    
    set vSelectSql4 = concat('
        SELECT
          4 as ord,
          0 as curHour,
          '''' as row_header,
          sum(pv) AS pv,
          sum(uv) AS uv,
          sum(nuv) AS nuv,
          round(sum(nuv) / sum(uv), 4) AS nuv_rate,
          sum(ip) AS ip,
          sum(visits) AS visits,
          sum(total_visit_time) AS total_visit_time,
          round(sum(total_visit_time) / sum(visits), 2) AS avg_visit_time,
          sum(total_visit_pages) AS total_visit_pages,
          round(sum(total_visit_pages) / sum(visits), 2) AS avg_visit_pages,
          sum(bounces) AS bounces,
          round(sum(bounces) / sum(visits), 4) AS bounce_rate
        FROM
          dw_wda_websites_summary_hm
        WHERE
          1 = 1');

    set vSelectSql4 = concat(vSelectSql4, ' and site_id = ''', pSiteId, ''' ');
    set vSelectSql4 = concat(vSelectSql4, ' and pt_d = ', DATE_FORMAT(date_sub(pCurDate, interval 1 day), '%Y%m%d'), ' and pt_h <= ', cnt);
    
    SET @Sql = vSelectSql4;
    prepare stmt FROM @Sql;    
    execute stmt;
    
    -- 计算网站每日各个统计项的平均值，查询天表
     set vSelectSql5 = concat('
        select 
          5 as ord,
          0 as curHour,
          '''' as row_header,
          cast(avg(pv) as signed) as pv, 
          cast(avg(uv) as signed) as uv, 
          cast(avg(nuv) as signed) as nuv, 
          avg(nuv_rate) as nuv_rate, 
          cast(avg(ip) as signed) as ip,
          cast(avg(visits) as signed) as visits,
          cast(avg(total_visit_time) as signed) as total_visit_time,
		  cast(avg(avg_visit_time) as signed) as avg_visit_time,
          cast(avg(total_visit_pages) as signed) as total_visit_pages,
          avg(avg_visit_pages) as avg_visit_pages,
          cast(avg(bounces) as signed) as bounces,
          avg(bounce_rate) as bounce_rate 
        from 
          dw_wda_websites_summary_dm where 1=1');
        
     set vSelectSql5 = concat(vSelectSql5, ' and site_id = ''', pSiteId, ''' ');
     
     SET @Sql = vSelectSql5;
    prepare stmt FROM @Sql;    
    execute stmt;
    
     set vSelectSql6 = concat('
        select 
          6 as ord,
          0 as curHour,
          '''' as row_header,
          max(pv) as pv, 
          max(uv) as uv, 
          max(nuv) as nuv, 
          max(nuv_rate) as nuv_rate, 
          max(ip) as ip,
          max(visits) as visits,
          max(total_visit_time) as total_visit_time,
          max(avg_visit_time) as avg_visit_time,
          max(total_visit_pages) as total_visit_pages,
          max(avg_visit_pages) as avg_visit_pages,
          max(bounces) as bounces,
          max(bounce_rate) as bounce_rate 
        from dw_wda_websites_summary_dm where 1=1');
     
     set vSelectSql6 = concat(vSelectSql6, ' and site_id = ''', pSiteId, ''' ');
     
     SET @Sql = vSelectSql6;
     prepare stmt FROM @Sql;    
     execute stmt;
      
     set pRetCode = 0;
END//