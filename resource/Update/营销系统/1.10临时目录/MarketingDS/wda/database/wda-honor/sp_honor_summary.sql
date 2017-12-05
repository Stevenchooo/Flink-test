delimiter //
DROP PROCEDURE IF EXISTS sp_honor_summary;
CREATE PROCEDURE sp_honor_summary
(
  OUT pRetCode    INT
)
COMMENT ''
proc: BEGIN

  declare vSelectSql varchar(1000);
    
    set vSelectSql = concat('select DATE_FORMAT(t1.pt_d, ''%Y年%m月%d日'') as pt_d,t2.exposure_uv, t2.phone_exposure_uv, t2.click_uv, t2.phone_click_uv, t2.landing_uv, t2.phone_landing_uv,t3.year_exposure_uv, t3.year_click_uv, t3.year_landing_uv, t4.posts, t4.phone_posts, t4.year_posts from 
							(select 
								max(pt_d) as pt_d 
							from 
								dw_honorhome_overview_honor_mkt_dm)
 							t1
							left outer join 
							(
							select 
								*
							from
								dw_honorhome_overview_honor_mkt_dm 
							) t2 
							on t1.pt_d = t2.pt_d

							left outer join 
							(
							select 
								*
							from
								dw_honorhome_overview_honor_mkt_year_dm 
							) t3 
							on t1.pt_d = t3.pt_d

							left outer join 
							(
							select 
								*
							from
								dw_honorhome_overview_fans_post_data_dm 
							) t4 
							on t1.pt_d = t4.pt_d;');
    
  	SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
  
    set pRetCode = 0;
    
END//