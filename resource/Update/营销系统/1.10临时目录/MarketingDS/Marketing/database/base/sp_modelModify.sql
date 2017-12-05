DROP PROCEDURE IF EXISTS sp_modelModify;
CREATE PROCEDURE sp_modelModify (
    out pRetCode            int,
    in pModelId             int,
    in pPid                 int,
    in pName                varchar(255),
    in pVal                 varchar(255)
)
proc: BEGIN
    declare vPid           int default -1;
    declare vMeta          char(15);
    declare vIsUnique      boolean default false;
    
    select pid, meta into vPid, vMeta from t_ms_model where id = pModelId;

    if(vPid < 0) then -- not exists
        set pRetCode = 2001;
        leave proc;
    end if;
     
    if(pPid is not null && pPid <> vPid) then -- 改变父项
        if(not exists(select * from t_ms_model where id=pPid)) THEN
           set pRetCode = 2001;
           leave proc;
        end if;
        
        select isUnique into vIsUnique
          from t_ms_meta
         where name = vMeta;
         
        -- 必须唯一的情况，目标项下唯一
        if(vIsUnique && pName is not null) then
            if(exists(select * from t_ms_model where pid=pPid and name=pName and id<>pModelId)) then
                set pRetCode = 2000;
                leave proc;
            end if;
        end if;
    else
        select isUnique into vIsUnique
          from t_ms_meta
         where name = vMeta;
         
        -- 必须唯一的情况
        if(vIsUnique && pName is not null) then
            if(exists(select * from t_ms_model where pid=vPid and name=pName and id<>pModelId)) then
                set pRetCode = 2000;
                leave proc;
            end if;
        end if;
    end if;

    
    update t_ms_model set
        pid = ifnull(pPid, pid),
        name = ifnull(pName, name),
        val = ifnull(pVal, val),
        modifyTime = now()
    where id = pModelId;
    
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;
end;