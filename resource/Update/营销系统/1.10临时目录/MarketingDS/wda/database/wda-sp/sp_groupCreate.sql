delimiter //
DROP PROCEDURE IF EXISTS sp_groupCreate;
CREATE PROCEDURE sp_groupCreate (
    out pRetCode            int,
    in pCreator             varchar(50), -- 创建人
    in pName                varchar(50),
    in pUserId              varchar(200),
    in pUserName            varchar(200)
)
proc: BEGIN

    declare curId INT DEFAULT 0;
    declare cnt INT DEFAULT 0;
    declare i INT DEFAULT 0;

    if(exists(select * from t_wda_group where name=pName)) then
  select 2000 into pRetCode;
  leave proc;
    end if;
    
    insert into t_wda_group (
  creator,
        name
    )
    values (
  pCreator,
        pName
    );
    
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;

    if(pUserId is not null and '' != pUserId) then

      select id into curId from t_wda_group where name = pName;
      
      set cnt = 1 + (length(pUserId) - length(replace(pUserId, ',', ''))); 
      
      while i<cnt
      do
         set i=i+1;
         insert into t_wda_group_user (groupId,userId, userName) values(curId, reverse(substring_index(reverse(substring_index(pUserId, ',' ,i)), ',',1)), reverse(substring_index(reverse(substring_index(pUserName, ',' ,i)), ',',1)) );
      end while;
    end if;
    
    set pRetCode = 0;

end//
