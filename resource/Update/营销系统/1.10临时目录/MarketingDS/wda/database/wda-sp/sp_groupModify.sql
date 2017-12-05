delimiter //
DROP PROCEDURE IF EXISTS sp_groupModify;
CREATE PROCEDURE sp_groupModify (
    out pRetCode            int,
    in pCreator             varchar(50),
    in pName                varchar(50),
  in pId            int,
    in pUserId              varchar(400),   
    in pUserName          varchar(400)
 
)
proc: BEGIN
    
  declare cnt INT DEFAULT 0;
    declare i INT DEFAULT 0;
  
  if(not exists(select * from t_wda_group where id=pId)) then
        select 2001 into pRetCode;
        leave proc;
    end if;
  
  if(exists(select * from t_wda_group where name=pName and id != pId)) then
        select 2000 into pRetCode;
        leave proc;
    end if;
    
    update t_wda_group set
        creator = ifnull(pCreator, creator),
        name = ifnull(pName, name)
    where id = pId; 

    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
  -- 先删除group包含的user
  delete from t_wda_group_user where groupId = pId;
    
  if(pUserId is not null and '' != pUserId) then
  
    set cnt = 1 + (length(pUserId) - length(replace(pUserId, ',', ''))); 
    
    while i<cnt
    do
       set i=i+1;
       insert into t_wda_group_user (groupId, userId, userName) values(pId, reverse(substring_index(reverse(substring_index(pUserId, ',' ,i)), ',',1)), reverse(substring_index(reverse(substring_index(pUserName, ',' ,i)), ',',1)) );
    end while;
    
    if(row_count() <= 0) then
      select 4 into pRetCode; -- 数据库错误
      leave proc;
    end if;
  
  end if;
  
    SELECT 0 INTO pRetCode;
end//
