delimiter //
DROP PROCEDURE IF EXISTS sp_roleRightQuery;
CREATE PROCEDURE sp_roleRightQuery (
    out pRetCode            int,
    in  pRole               varchar(50),
    in  pMeta               char(50)  -- 数据类型
)
proc:BEGIN
    declare vOprRight   char(255) default null;
    
    set pRetCode = 0; -- 有数据的情况才有权限
    -- 确定对此类数据是否有相应操作权限
    select oprRight into vOprRight from t_ms_right where role = pRole and meta = pMeta;
    if(vOprRight is null) then
        select oprRight from t_ms_right where role = pRole and meta = '*';
    else
        select vOprRight as oprRight;
    end if;
END//
