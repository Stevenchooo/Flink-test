delimiter //
DROP PROCEDURE IF EXISTS sp_adminRight;
CREATE PROCEDURE sp_adminRight (
    out pRetCode            int,
    in  pAccount            varchar(50),
    in  pModelId            int,
    in  pDataType           char(50)  -- 数据类型
)
proc:BEGIN
    declare vPid        int;
    declare vRole       char(20);
    declare vOprRight   char(255) default null;
    
	set pRetCode = 4100;
    if(not exists(select * from t_ms_user where account=pAccount and status='actived')) then
        leave proc;
    end if;
    set vPid = pModelId;
	
    
    -- 确定范围是否有权限，上层有权限，则下层也有权限
    loop_label: LOOP
        -- 角色存放在t_ms_model.val中
        select m.val into vRole from t_ms_model m, t_ms_user u
         where m.pid = vPid
           and m.name = pAccount
           and m.meta = 'admin'
           and u.account = pAccount
           and u.status = 'actived';
           
        if(found_rows() > 0) then
            -- 确定对此类数据是否有相应操作权限
            select oprRight into vOprRight from t_ms_right where role = vRole and meta = pDataType;
            if(vOprRight is null) then
                select oprRight into vOprRight from t_ms_right where role = vRole and meta = '*';
            end if;
            
            if(vOprRight is not null) then
                set pRetCode = 0; -- 有数据的情况才有权限
                select vOprRight as userRight;
            end if;
            
            leave loop_label;
        end if;
        
        -- 往上一层查找
        select pid into vPid from t_ms_model where id=vPid;
        
        if(found_rows() <= 0) then
            leave loop_label;
        end if;
    END LOOP loop_label;
END//
