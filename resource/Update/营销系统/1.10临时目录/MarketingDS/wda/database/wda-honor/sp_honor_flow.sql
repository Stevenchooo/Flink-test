delimiter //
DROP PROCEDURE IF EXISTS sp_honor_flow;
CREATE PROCEDURE sp_honor_flow
(
  OUT pRetCode    INT
)
COMMENT ''
proc: BEGIN
	-- 流量经营
   declare vSelectSql varchar(1000);
   declare vSelectSql2 varchar(1000);
     
   	-- 请求三种趋势，点击UV、曝光UV、CTR（点击UV/曝光UV）
    set vSelectSql = concat('select pt_d, sum(exposure_uv) as exposure_uv, SUM(click_uv) as click_uv, FORMAT(sum(click_uv)/sum(exposure_uv),2) as ctr from dw_honorhome_mkt_data_dm where 1=1');
    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(MAKEDATE(YEAR(NOW()), 1), '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(curdate(), '%Y%m%d'));  
              
    set vSelectSql = concat(vSelectSql, ' group by pt_d asc');
    
    SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
    
    -- ROI，ROI=销售金额/点击UV
    set vSelectSql = concat('select pt_d, FORMAT(sum(sale_amount)/sum(click_uv),2) as roi from dw_honorhome_mkt_data_dm where 1=1');
    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(DATE_SUB(CURRENT_DATE(),INTERVAL 3 MONTH ), '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(curdate(), '%Y%m%d'));  
              
    set vSelectSql = concat(vSelectSql, ' group by pt_d asc');
    
    SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
             
    -- 输出日期
    set vSelectSql = concat('select DATE_FORMAT(DATE_SUB(CURRENT_DATE(),INTERVAL 3 MONTH ), ''%Y年%m月%d日'') as am, DATE_FORMAT(curdate(), ''%Y年%m月%d日'') as bm');
    
    SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
    set pRetCode = 0;
  
END//