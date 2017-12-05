delimiter //
DROP PROCEDURE IF EXISTS sp_roleRemove;
CREATE PROCEDURE sp_roleRemove (
    out pRetCode            int,
    
    in pRole                varchar(50)
)
proc: BEGIN
	if(not exists(select * from t_ms_role where role=pRole)) then
	    select 0 into pRetCode; -- 不存在，也认为删除成功
	    leave proc;
	end if;
	
    if(exists(select * from t_ms_model where val=pRole and meta='admin')) then
        set pRetCode = 3503; -- 有使用此角色的管理员
        leave proc;
    end if;
	
	-- 先删除响应的权限，及使用了这个角色的管理员
	delete from t_ms_model where val=pRole and meta='admin';
    delete from t_ms_right where role=pRole;
    delete from t_ms_role where role=pRole;

    if(row_count() <= 0) then
        set pRetCode = 4; -- 数据库错误
    end if;
    
    set pRetCode = 0;
end//