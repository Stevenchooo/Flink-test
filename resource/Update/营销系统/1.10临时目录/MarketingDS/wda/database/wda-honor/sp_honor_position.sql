delimiter //
DROP PROCEDURE IF EXISTS sp_honor_position;
CREATE PROCEDURE sp_honor_position
(
  OUT pRetCode    INT,
    IN  pSiteIdPC     VARCHAR(2000),
    IN  pSiteIdWAP     VARCHAR(128)
)
COMMENT ''
proc: BEGIN

   declare vSelectSql varchar(1000);
   declare vSelectSql2 varchar(1000);
   declare vSelectSql3 varchar(1000);
   declare vSelectSql4 varchar(1000);
     
   	-- pSiteIdPC 请求柱状图的类型（referrer_type：1、2、3对应广告导流、链接导流、直接访问）和对应的值
    set vSelectSql = concat('select referrer_type as x, SUM(uv) as y from dw_wda_websites_summary_by_referrer_dm where 1=1');
    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(DATE_SUB(CURRENT_DATE(),INTERVAL 3 MONTH ), '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(curdate(), '%Y%m%d'));  
              
    if(pSiteIdPC is not null and '' != pSiteIdPC) then
      set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteIdPC, ''' ');
    end if;
              
    set vSelectSql = concat(vSelectSql, ' group by x desc');
    
    SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;
    execute stmt;
             
   -- pSiteIdPC 请求折线图，需要日期和对应的总量
    set vSelectSql2 = concat('select  DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, SUM(uv) as y from dw_wda_websites_summary_by_referrer_dm where 1=1');
    set vSelectSql2 = concat(vSelectSql2, ' and pt_d >= ', DATE_FORMAT(DATE_SUB(CURRENT_DATE(),INTERVAL 3 MONTH ), '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(curdate(), '%Y%m%d'));  
            
    if(pSiteIdPC is not null and '' != pSiteIdPC) then
      set vSelectSql2 = concat(vSelectSql2, ' and site_id = ''', pSiteIdPC, ''' ');
    end if;
              
    set vSelectSql2 = concat(vSelectSql2, ' group by x');
    
    SET @Sql = vSelectSql2;
    prepare stmt FROM @Sql;    
    execute stmt;
    
    set pRetCode = 0;
  
END//