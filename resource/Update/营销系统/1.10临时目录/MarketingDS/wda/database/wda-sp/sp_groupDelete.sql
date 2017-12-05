delimiter //
DROP PROCEDURE IF EXISTS sp_groupDelete;
CREATE PROCEDURE sp_groupDelete (
    out pRetCode            int,    
    in pId                  int
)
proc: BEGIN

    if(not exists(select * from t_wda_group where id=pId)) then
  set pRetCode = 2001;
  leave proc;
    end if;

    delete from t_wda_group where id=pId;
    
    if(row_count() <= 0) then
        set pRetCode = 4; -- 数据库错误
        leave proc;
    end if;

    -- 删除用户数据
    delete from t_wda_group_user where groupId=pId;
       
    -- if(row_count() <= 0) then
    --    set pRetCode = 4; -- 数据库错误
    --    leave proc;
    -- end if;
    
    set pRetCode = 0;
end//

