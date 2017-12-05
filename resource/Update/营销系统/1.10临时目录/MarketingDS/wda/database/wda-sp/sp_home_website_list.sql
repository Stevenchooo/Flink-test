delimiter //
DROP PROCEDURE IF EXISTS sp_home_website_list;
CREATE PROCEDURE sp_home_website_list
(
  OUT pRetCode INT,    
  IN  pAccount VARCHAR(20),
  IN  pLastDate VARCHAR(20)
)
COMMENT ''
proc: BEGIN
    declare vSelectSql varchar(1000);
    declare vSelectSql1 varchar(1000);
    declare vSelectSql2 varchar(1000);
    declare vSelectSql3 varchar(1000);
    declare lastDate VARCHAR(20);
    
    set lastDate = DATE_FORMAT(pLastDate, '%Y%m%d');
    set vSelectSql1 = concat('select * from t_wda_site_basic_info where 1=1 ');
    if(pAccount <> 'admin123') then
        set vSelectSql1 = concat(vSelectSql1,' and group_id in( select groupId from t_wda_group_user where 1=1 ');
  set vSelectSql1 = concat(vSelectSql1, ' and userId = ''', pAccount, ''' ) ');      
    end if;
    set vSelectSql2 = concat('select pv, site_id, pt_d from dw_wda_websites_summary_dm where 1=1');
    if(lastDate is not null) then
      set vSelectSql2 = concat(vSelectSql2, ' and pt_d = ', lastDate);  
    end if;
    set vSelectSql3 = concat('select sum(pv) as pv, site_id, max(pt_d) as pt_d from dw_wda_websites_summary_dm group by site_id');
    set vSelectSql = concat('select t1.site_id, t1.site_name, t1.site_url, t1.site_desc, t1.site_type, t1.access_net_type, t1.createtime, t3.pt_d as updatetime, t2.pv as last_pv, t3.pv as total_pv from  (', vSelectSql1, ') t1');
    set vSelectSql = concat(vSelectSql, ' left outer join (', vSelectSql2, ') t2 on (t1.site_id = t2.site_id) left outer join (', vSelectSql3, ') t3 on (t1.site_id = t3.site_id)');
    set vSelectSql = concat(vSelectSql, ' order by t1.site_name');
	set vSelectSql = concat('select t4.site_id, t4.site_name, t4.site_url, t4.site_desc, t4.site_type, t4.access_net_type, t4.createtime, t4.updatetime, t4.last_pv, t4.total_pv, max(t5.pt_h) as pt_h from (', vSelectSql, ') t4 left outer join dw_wda_websites_summary_hm t5 on (t4.site_id = t5.site_id and t4.updatetime = t5.pt_d) group by t4.site_id');
    SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;
    execute stmt;
    set pRetCode = 0;
End//