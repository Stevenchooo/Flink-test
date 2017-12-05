delimiter //
DROP PROCEDURE IF EXISTS sp_visitor_area_userinsight_industry;
CREATE PROCEDURE sp_visitor_area_userinsight_industry
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
	-- 同一天
	if(pFromDate = pToDate) then
	set vSelectSql = concat('select dim_value as x,cookies as y from dw_cooperation_wda_result_dm where 1=1');
	  if(pFromDate is not null and pToDate is not null) then
        set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
      end if;
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      set vSelectSql = concat(vSelectSql,'and pt_dim =''industry'' group by x order by y desc');
      SET @Sql = vSelectSql;
      prepare stmt FROM @Sql;    
      execute stmt;
      if('' <> pIsHuanBi) then
      set vSelectSql = concat('select dim_value as x,cookies as y from dw_cooperation_wda_result_dm where 1=1');
        if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
          set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
        end if;    
        if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        set vSelectSql = concat(vSelectSql,'and pt_dim =''industry'' group by x order by y desc');
       SET @Sql = vSelectSql;
          prepare stmt FROM @Sql;    
          execute stmt;
       end if;
    -- 多天
    elseif(pFromDate <> pToDate) then
    set vSelectSql = concat('select dim_value as x,SUM(cookies) as y from dw_cooperation_wda_result_dm where 1=1');
    if(pFromDate is not null and pToDate is not null) then
        set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));  
      end if;
      if(pSiteId is not null and '' != pSiteId) then
        set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
      end if;
      set vSelectSql = concat(vSelectSql,'and pt_dim =''industry'' group by x order by y desc');
      SET @Sql = vSelectSql;
      prepare stmt FROM @Sql;    
      execute stmt;
      if('' <> pIsHuanBi) then
      set vSelectSql = concat('select dim_value as x,SUM(cookies) as y from dw_cooperation_wda_result_dm where 1=1');
        if(pHuanBiFromDate is not null and pHuanBiToDate is not null) then
          set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pHuanBiFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pHuanBiToDate, '%Y%m%d'));
        end if;    
        if(pSiteId is not null and '' != pSiteId) then
          set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        set vSelectSql = concat(vSelectSql,'and pt_dim =''industry'' group by x order by y desc');
       SET @Sql = vSelectSql;
          prepare stmt FROM @Sql;    
          execute stmt;
     end if; 
   end if;
   set pRetCode = 0;
END 