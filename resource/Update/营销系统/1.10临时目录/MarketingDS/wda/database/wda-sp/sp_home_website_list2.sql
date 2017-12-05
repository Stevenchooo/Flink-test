delimiter //
DROP PROCEDURE IF EXISTS sp_home_website_list2;
CREATE PROCEDURE sp_home_website_list2
(
    OUT pRetCode    INT,    
  IN  pAccount  VARCHAR(20)
)
COMMENT ''
proc: BEGIN

  declare vSelectSql varchar(1000);
    
  set vSelectSql = concat('select site_id, site_name from t_wda_site_basic_info where 1=1 ');
  
  if(pAccount <> 'admin123') then
    set vSelectSql = concat(vSelectSql,' and group_id in( select groupId from t_wda_group_user where 1=1 ');
    set vSelectSql = concat(vSelectSql, ' and userId = ''', pAccount, ''' ) ');      
  end if;
   
    SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
     
    set pRetCode = 0;

End//