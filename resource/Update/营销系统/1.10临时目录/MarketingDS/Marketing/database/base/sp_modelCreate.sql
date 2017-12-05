DROP PROCEDURE IF EXISTS sp_modelCreate;
CREATE PROCEDURE sp_modelCreate (
    out pRetCode            int,
    in pCreator             varchar(50),
    in pPid                 int,
    in pName                varchar(255),
    in pMeta                char(15),
    in pVal                 varchar(255)
)
proc: BEGIN
    declare vModelId        int default -1;
    declare vUnique         boolean default false;
    declare vVisible        boolean default true;
    
    if(not fun_isValidSubMeta(pPid, pMeta)) then
        set pRetCode = 7002;
        leave proc;
    end if;
    
    select isUnique, isVisible
      into vUnique, vVisible
      from t_ms_meta
     where name = pMeta;
     
    if(vUnique) then
        if(exists(select * from t_ms_model where pid=pPid and name=pName)) then
            set pRetCode = 2000;
            leave proc;
        end if;
    end if;
    
    insert into t_ms_model(creator, pid, name, meta, val, visible)
    values(pCreator, pPid, pName, pMeta, pVal, vVisible);
    
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    select LAST_INSERT_ID() into vModelId;
    
    SELECT 0 INTO pRetCode;
    select vModelId as id; 
end;