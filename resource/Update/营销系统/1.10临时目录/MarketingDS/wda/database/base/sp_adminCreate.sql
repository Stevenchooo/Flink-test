delimiter //
DROP PROCEDURE IF EXISTS sp_adminCreate;
CREATE PROCEDURE sp_adminCreate (
    out pRetCode            int,
    in pCreator             varchar(50),
    in pPid                 int,
    in pAccount             varchar(50),
    in pRole                varchar(100)
)
proc: BEGIN
    declare vModelId        int;
    
    if(not exists(select * from t_ms_user where account=pAccount)) then
        set pRetCode = 2001;
        leave proc;
    end if;
    
    if(not fun_isValidSubMeta(pPid, 'admin')) then
        set pRetCode = 7002;
        leave proc;
    end if;
    
    if(exists(select * from t_ms_model where pid=pPid and name=pAccount and meta='admin')) then
        set pRetCode = 2000;
        leave proc;
    end if;
    
    -- 描述中存放权限，不是很合理，不过使用方便
    insert into t_ms_model(creator, pid, name, meta, val, visible)
    values(pCreator, pPid, pAccount, 'admin', pRole, false);
    
    if(row_count() <= 0) then
    	set pRetCode = 4; -- 数据库错误
        leave proc;
    end if;
    
    select LAST_INSERT_ID() into vModelId; 
    
    set pRetCode = 0; 
    select vModelId as id;
end//