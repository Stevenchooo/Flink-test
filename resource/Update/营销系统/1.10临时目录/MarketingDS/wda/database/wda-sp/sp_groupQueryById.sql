delimiter //

DROP PROCEDURE IF EXISTS sp_groupQueryById;

CREATE PROCEDURE sp_groupQueryById (
    out pRetCode            int,
  in pId          int
)

proc: BEGIN

    declare vSelectSql varchar(1000);
  
  set vSelectSql = concat('select id, name, creator, createtime from t_wda_group where 1=1 ');
  set vSelectSql = concat(vSelectSql, ' and id = ', pId);
  
  SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
  
  set vSelectSql = concat('select userId as id, userName as name from t_wda_group_user where 1=1 ');
  set vSelectSql = concat(vSelectSql, ' and groupId = ', pId);
  
  SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
  
    set pRetCode = 0;
end//
