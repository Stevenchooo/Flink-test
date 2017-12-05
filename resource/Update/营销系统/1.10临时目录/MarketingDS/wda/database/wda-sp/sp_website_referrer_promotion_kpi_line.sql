delimiter //
DROP PROCEDURE IF EXISTS sp_website_referrer_promotion_kpi_line;
CREATE PROCEDURE sp_website_referrer_promotion_kpi_line
(
    OUT pRetCode    INT,    
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pCurKpi     VARCHAR(20),
    IN  pReferrerType     INT,
    IN  pFromDate   VARCHAR(20),
    IN  pToDate     VARCHAR(20)
)
COMMENT ''
proc: BEGIN

     declare vSelectSql varchar(1000);
     declare vSelectSql1 varchar(1000);
          
     if(pFromDate = pToDate) then
        
        set vSelectSql = concat('select pt_h as x, SUM(', pCurKpi, ') as y from dw_wda_websites_summary_by_referrer_hm where 1=1 and referrer_type = ', pReferrerType);
              
          if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
              end if;
              
              if(pSiteId is not null and '' != pSiteId) then
                  set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
              end if;
              
              set vSelectSql = concat(vSelectSql, ' group by pt_h order by x');
              SET @Sql = vSelectSql;
              prepare stmt FROM @Sql;    
              execute stmt;
         
              if(pReferrerType <> 3) then
           set vSelectSql = concat('select t2.referrer_name, t2.y, t2.x from (
	select referrer_name, sum(',pCurKpi,') as pv from dw_wda_websites_summary_by_referrer_hm
	where site_id=''',pSiteId,''' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'),' and referrer_type = ', pReferrerType,'
	group by referrer_name
	order by pv desc 
    limit 10
)t1
left outer join(select referrer_name,',pCurKpi,' as y,pt_d,pt_h as x from dw_wda_websites_summary_by_referrer_hm where site_id=''',pSiteId,''' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'),' and referrer_type = ', pReferrerType,')t2
on t1.referrer_name=t2.referrer_name'
);
              
              set vSelectSql = concat(vSelectSql, ' order by referrer_name,x');
              SET @Sql = vSelectSql;
              prepare stmt FROM @Sql;    
              execute stmt;
     end if;
              
     elseif(pFromDate <> pToDate) then
      
      set vSelectSql = concat('select DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x, SUM(', pCurKpi, ') as y from dw_wda_websites_summary_by_referrer_dm where 1=1 and referrer_type = ', pReferrerType);
              
          if(pFromDate is not null and pToDate is not null) then
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
              end if;
              
              if(pSiteId is not null and '' != pSiteId) then
                  set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
              end if;
              
              set vSelectSql = concat(vSelectSql, ' group by pt_d order by x');
              SET @Sql = vSelectSql;
              prepare stmt FROM @Sql;    
              execute stmt;
      
               if(pReferrerType <> 3) then
     set vSelectSql = concat('select t2.* from (
	select referrer_name, sum(',pCurKpi,') as pv from dw_wda_websites_summary_by_referrer_dm
	where site_id=''',pSiteId,''' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'),' and referrer_type = ', pReferrerType,'
	group by referrer_name
	order by pv desc 
    limit 5
)t1
left outer join(select referrer_name,',pCurKpi,' as y,DATE_FORMAT(pt_d, ''%Y/%m/%d'') as x from dw_wda_websites_summary_by_referrer_dm where site_id=''',pSiteId,''' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'),' and referrer_type = ', pReferrerType,')t2
on t1.referrer_name=t2.referrer_name'
);
              
             set vSelectSql = concat(vSelectSql, ' order by referrer_name,x');
              SET @Sql = vSelectSql;
              prepare stmt FROM @Sql;    
              execute stmt;
      end if;
     elseif(pPeriod = 99) then
        set vSelectSql = '';
     end if;
     
     set pRetCode = 0;
END//
