delimiter //
DROP PROCEDURE IF EXISTS sp_pages_updown_summary;
CREATE PROCEDURE sp_pages_updown_summary
(
	OUT pRetCode    INT,
	IN  pSiteId     VARCHAR(2000),
	IN  pPeriod     VARCHAR(128),
	IN  pTab     INT,
	IN  pPageUrl     VARCHAR(128),
	IN  pFromDate   VARCHAR(20),
	IN pToDate      VARCHAR(20)
)
COMMENT ''
proc: BEGIN
	declare vSelectSql varchar(1000);
	if(pFromDate = pToDate) then
	-- 上游页面
	if(pTab = 1) then
	set vSelectSql = concat('select updown_page_url,updown_page_uv as uv,updown_page_pv as pv from dw_wda_pages_updown_summary_dm where 1=1');
	   if(pFromDate is not null and pToDate is not null) then
	    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
	    end if;
	    if(pSiteId is not null and '' != pSiteId) then
           set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        if(pPageUrl is not null and '' != pPageUrl) then
           set vSelectSql = concat(vSelectSql,' and page_url = ''', pPageUrl, ''' ');
           end if;
      set vSelectSql = concat(vSelectSql,'and length(TRIM(updown_page_url)) >= 1 and updown_flag = 1 order by uv desc limit 10');
	 SET @Sql = vSelectSql;
              prepare stmt FROM @Sql;    
              execute stmt;
      -- 下游页面
     elseif(pTab = 2) then
	 set vSelectSql = concat('select updown_page_url,updown_page_uv as uv,updown_page_pv as pv from dw_wda_pages_updown_summary_dm where 1=1');
	  
	 if(pFromDate is not null and pToDate is not null) then
	    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
	    end if;
	 if(pSiteId is not null and '' != pSiteId) then
           set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
      if(pPageUrl is not null and '' != pPageUrl) then
           set vSelectSql = concat(vSelectSql,' and page_url = ''', pPageUrl, ''' ');
         end if;
         set vSelectSql = concat(vSelectSql,'and length(TRIM(updown_page_url)) >= 1 and updown_flag = 0 order by uv desc limit 10');
         SET @Sql = vSelectSql;
              prepare stmt FROM @Sql;    
              execute stmt;
     -- 页面点击详情
	elseif(pTab = 3) then
	set vSelectSql = concat('select action_name as updown_page_url, action_uv as uv, action_pv as pv from dw_wda_pages_button_click_summary_dm where 1=1');
	if(pFromDate is not null and pToDate is not null) then
	set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
	    end if;
	    if(pSiteId is not null and '' != pSiteId) then
           set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        if(pPageUrl is not null and '' != pPageUrl) then
           set vSelectSql = concat(vSelectSql,' and page_url = ''', pPageUrl, ''' ');
         end if;
        set vSelectSql = concat(vSelectSql,'and length(TRIM(action_name)) >= 1 order by uv desc limit 10');
        SET @Sql = vSelectSql;
              prepare stmt FROM @Sql;    
              execute stmt;
	end if;
	elseif (pFromDate <> pToDate) then
	-- 上游页面
	if(pTab = 1) then
	set vSelectSql = concat('select updown_page_url,SUM(updown_page_uv) as uv,SUM(updown_page_pv) as pv from dw_wda_pages_updown_summary_dm where 1=1');
	   if(pFromDate is not null and pToDate is not null) then
	    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
	    end if;
	    if(pSiteId is not null and '' != pSiteId) then
           set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        if(pPageUrl is not null and '' != pPageUrl) then
           set vSelectSql = concat(vSelectSql,' and page_url = ''', pPageUrl, ''' ');
           end if;
      set vSelectSql = concat(vSelectSql,'and length(TRIM(updown_page_url)) >= 1 and updown_flag = 1 group by updown_page_url order by uv desc limit 10');
	 SET @Sql = vSelectSql;
              prepare stmt FROM @Sql;    
              execute stmt;
      -- 下游页面
     elseif(pTab = 2) then
	 set vSelectSql = concat('select updown_page_url,SUM(updown_page_uv) as uv,SUM(updown_page_pv) as pv from dw_wda_pages_updown_summary_dm where 1=1');
	  
	 if(pFromDate is not null and pToDate is not null) then
	    set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
	    end if;
	 if(pSiteId is not null and '' != pSiteId) then
           set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
      if(pPageUrl is not null and '' != pPageUrl) then
           set vSelectSql = concat(vSelectSql,' and page_url = ''', pPageUrl, ''' ');
         end if;
         set vSelectSql = concat(vSelectSql,'and length(TRIM(updown_page_url)) >= 1 and updown_flag = 0 group by updown_page_url order by uv desc limit 10');
         SET @Sql = vSelectSql;
              prepare stmt FROM @Sql;    
              execute stmt;
     -- 页面点击详情
	elseif(pTab = 3) then
	set vSelectSql = concat('select action_name as updown_page_url, SUM(action_uv) as uv, SUM(action_pv) as pv from dw_wda_pages_button_click_summary_dm where 1=1');
	if(pFromDate is not null and pToDate is not null) then
	set vSelectSql = concat(vSelectSql, ' and pt_d >= ', DATE_FORMAT(pFromDate, '%Y%m%d'), ' and pt_d <= ', DATE_FORMAT(pToDate, '%Y%m%d'));
	    end if;
	    if(pSiteId is not null and '' != pSiteId) then
           set vSelectSql = concat(vSelectSql, ' and site_id = ''', pSiteId, ''' ');
        end if;
        if(pPageUrl is not null and '' != pPageUrl) then
           set vSelectSql = concat(vSelectSql,' and page_url = ''', pPageUrl, ''' ');
         end if;
        set vSelectSql = concat(vSelectSql,'and length(TRIM(action_name)) >= 1 group by updown_page_url order by uv desc limit 10');
        SET @Sql = vSelectSql;
              prepare stmt FROM @Sql;    
              execute stmt;
	end if;
	end if;
	set pRetCode = 0;
END