delimiter //
DROP PROCEDURE IF EXISTS sp_website_trend_list;
CREATE PROCEDURE sp_website_trend_list
(
    OUT pRetCode    INT, 
    IN  pIsHuanBi   VARCHAR(128), 
    IN  pSiteId     VARCHAR(2000),
    IN  pPeriod     VARCHAR(128),
    IN  pTimeDim    INT,
    IN  pFromDate   VARCHAR(20),
    IN  pToDate     VARCHAR(20),
    IN  pHuanBiFromDate   VARCHAR(20),
    IN  pHuanBiToDate     VARCHAR(20)
)
COMMENT ''
proc: BEGIN

     declare vSelectSql varchar(1000);
     declare vSelectSql2 varchar(1000);
     declare vSelectSql3 varchar(1000);
     
     
     if(pTimeDim = 1) then
       
        
        if(pFromDate = pToDate) then
            
            
            set vSelectSql = concat('select pt_d, pt_h, pv, visits, ip, bounce_rate from dw_wda_websites_summary_hm where 1=1');
            
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
            
            
            if('' <> pIsHuanBi) then
            
                set vSelectSql2 = concat('select pt_d, pt_h, pv, visits, ip, bounce_rate from dw_wda_websites_summary_hm where 1=1');
                set vSelectSql2 = concat(vSelectSql2, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
                set vSelectSql2 = concat(vSelectSql2, ' and site_id = ''', pSiteId, ''' ');
            
                
                set vSelectSql = concat('select t1.pt_d, t1.pt_h, t1.pv, t1.visits, t1.ip, t1.bounce_rate, t2.pt_d as pt_d2, t2.pv as pv2, t2.visits as visits2, t2.ip as ip2, t2.bounce_rate as bounce_rate2 from (', vSelectSql, ') t1 left outer join (', vSelectSql2, ') t2 on (t1.pt_h = t2.pt_h) ');
            
            end if;
          
        
        elseif(pFromDate <> pToDate) then
            
            
            set vSelectSql = concat('select pt_h, min(pt_d) as start_pt_d, max(pt_d) as end_pt_d, sum(pv) as pv, sum(visits) as visits, sum(ip) as ip, round(sum(bounces)/sum(visits),4) as bounce_rate from dw_wda_websites_summary_hm where 1=1');
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
            set vSelectSql = concat(vSelectSql, ' group by pt_h');
            
            
            if('' <> pIsHuanBi) then
                set vSelectSql2 = concat('select pt_h, min(pt_d) as start_pt_d, max(pt_d) as end_pt_d, sum(pv) as pv, sum(visits) as visits, sum(ip) as ip, round(sum(bounces)/sum(visits),4) as bounce_rate from dw_wda_websites_summary_hm where 1=1');
                set vSelectSql2 = concat(vSelectSql2, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
                set vSelectSql2 = concat(vSelectSql2, ' and site_id = ''', pSiteId, ''' ');
                set vSelectSql2 = concat(vSelectSql2, ' group by pt_h');
                
                
                set vSelectSql = concat('select t1.start_pt_d, t1.end_pt_d, t1.pt_h, t1.pv, t1.visits, t1.ip, t1.bounce_rate, t2.start_pt_d as start_pt_d2, t2.end_pt_d as end_pt_d2, t2.pv as pv2, t2.visits as visits2, t2.ip as ip2, t2.bounce_rate as bounce_rate2 from (', vSelectSql, ') t1 left outer join (', vSelectSql2, ') t2 on (t1.pt_h = t2.pt_h) ');     
                
            end if;
            
      
      
         
         elseif(pPeriod = 99) then
            set vSelectSql = '';
         end if;
       
         
         set vSelectSql = concat(vSelectSql, ' order by pt_h desc');
         set @Sql = vSelectSql;
         prepare stmt FROM @Sql;    
         execute stmt;
         
         
     
     elseif(pTimeDim = 2) then
        
       
       if(pFromDate = pToDate) then
       
            set vSelectSql = concat('select pt_d, sum(pv) as pv, sum(visits) as visits, sum(ip) as ip, sum(bounces)/sum(visits) as bounce_rate from dw_wda_websites_summary_hm where 1=1');
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
            
            
            if('' <> pIsHuanBi) then
                
                set vSelectSql2 = concat('select pt_d, sum(pv) as pv, sum(visits) as visits, sum(ip) as ip, sum(bounces)/sum(visits) as bounce_rate from dw_wda_websites_summary_hm where 1=1');
                set vSelectSql2 = concat(vSelectSql2, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
                set vSelectSql2 = concat(vSelectSql2, ' and site_id = ''', pSiteId, ''' ');   
                
                
                set vSelectSql = concat('select t1.pt_d, t1.pv, t1.visits, t1.ip, t1.bounce_rate, t2.pt_d as pt_d2, t2.pv as pv2, t2.visits as visits2, t2.ip as ip2, t2.bounce_rate as bounce_rate2 from (', vSelectSql, ') t1 left outer join (', vSelectSql2, ') t2 on (1=1) ');
                
            end if;
            
            
            SET @Sql = vSelectSql;
            prepare stmt FROM @Sql;
            execute stmt;
            
       
       elseif(pFromDate <> pToDate) then
            
            
            set vSelectSql = concat('select  pt_d as start_pt_d, pt_d, pt_d as end_pt_d, pv, visits, ip, bounce_rate from dw_wda_websites_summary_dm where 1=1');
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
            set vSelectSql = concat(vSelectSql, ' group by pt_d');
      
            
            if('' <> pIsHuanBi) then
                
                set vSelectSql2 = concat('select pt_d as start_pt_d, pt_d, pt_d as end_pt_d, pv, visits, ip, bounce_rate from dw_wda_websites_summary_dm where 1=1');
                set vSelectSql2 = concat(vSelectSql2, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
                set vSelectSql2 = concat(vSelectSql2, ' and site_id = ''', pSiteId, ''' ');   
                set vSelectSql2 = concat(vSelectSql2, ' group by pt_d');
              
								-- 根据选择天数来判断环比天数间隔
                  set vSelectSql = concat('select t1.start_pt_d, t1.pt_d, t1.end_pt_d, t1.pv, t1.visits, t1.ip, t1.bounce_rate, t2.start_pt_d, t2.pt_d as pt_d2, t2.end_pt_d, t2.pv as pv2, t2.visits as visits2, t2.ip as ip2, t2.bounce_rate as bounce_rate2 from (', vSelectSql, ') t1 left outer join (', vSelectSql2, ') t2 on (datediff(t1.pt_d, t2.pt_d) = ', datediff(pFromDate, pHuanBiFromDate) ,') ');
               
            end if;
            
            set vSelectSql = concat(vSelectSql, ' order by pt_d desc');
            
            
            SET @Sql = vSelectSql;
            prepare stmt FROM @Sql;
            execute stmt;
            
            
       
       elseif(pPeriod = 99) then
          set vSelectSql = '';
       end if;
       elseif(pTimeDim = 3 or pTimeDim = 4) then
       if(pFromDate <> pToDate) then
         set vSelectSql = concat('select  pt_d as start_pt_d, pt_d, pt_d as end_pt_d, pv, visits, ip, bounce_rate from dw_wda_websites_summary_dm where 1=1');
            set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
            set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
            set vSelectSql = concat(vSelectSql, ' group by pt_d');
         if('' <> pIsHuanBi) then
                
                set vSelectSql2 = concat('select pt_d as start_pt_d, pt_d, pt_d as end_pt_d, pv, visits, ip, bounce_rate from dw_wda_websites_summary_dm where 1=1');
                set vSelectSql2 = concat(vSelectSql2, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
                set vSelectSql2 = concat(vSelectSql2, ' and site_id = ''', pSiteId, ''' ');   
                set vSelectSql2 = concat(vSelectSql2, ' group by pt_d');
              
								-- 根据选择天数来判断环比天数间隔
                  set vSelectSql = concat('select t1.start_pt_d, t1.pt_d, t1.end_pt_d, t1.pv, t1.visits, t1.ip, t1.bounce_rate, t2.start_pt_d, t2.pt_d as pt_d2, t2.end_pt_d, t2.pv as pv2, t2.visits as visits2, t2.ip as ip2, t2.bounce_rate as bounce_rate2 from (', vSelectSql, ') t1 left outer join (', vSelectSql2, ') t2 on (datediff(t1.pt_d, t2.pt_d) = ',datediff(pFromDate, pHuanBiFromDate),') ');
               
          end if;
          set vSelectSql = concat(vSelectSql, ' order by pt_d desc');  
         SET @Sql = vSelectSql;
            prepare stmt FROM @Sql;
            execute stmt;
        end if;
     
     end if;
     
     

     set pRetCode = 0;
END//
