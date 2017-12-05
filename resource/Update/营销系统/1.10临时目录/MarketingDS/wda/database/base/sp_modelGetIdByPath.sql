delimiter //
DROP PROCEDURE IF EXISTS sp_modelGetIdByPath;
CREATE PROCEDURE sp_modelGetIdByPath (
    out pRetCode            int,
    in pPid                 int,
    in pPath                varchar(255)
)
proc: BEGIN
    declare vStart          int;
    declare vEnd            int;
    declare vLen            int;
    declare vStr            varchar(1);
    declare vName           varchar(100);
    declare vId             int;
    
    -- 第一个是/，则表示从根开始，跟的id是1
    if(SUBSTRING(pPath, 1, 1) = '/') then
        select 2, 2, length(pPath), 1 into vStart, vEnd, vLen, vId;
    else
        select 1, 1, length(pPath), pPid into vStart, vEnd, vLen, vId;
    end if;

    -- 最后一个不是/，则添加
    if(SUBSTRING(pPath, vLen, 1) <> '/') then
        set pPath = concat(pPath, '/');
        set vLen = vLen + 1;
    end if;

    loop_label: LOOP
        set vEnd = LOCATE('/', pPath, vStart);
        if(vEnd > vStart) then
            set vName = SUBSTRING(pPath, vStart, vEnd - vStart);
            select id into vId from t_ms_model where pid = vId and name = vName;
            if(found_rows() <> 1) then
                set pRetCode = 2001;
                leave proc;
            end if;
            set vStart = vEnd + 1;
        end if;
        
        if(vStart > vLen) then
            leave loop_label;
        end if;
    END LOOP loop_label;

    set pRetCode = 0;
    select vId as id; 
end//