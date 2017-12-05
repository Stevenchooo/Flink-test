DROP PROCEDURE IF EXISTS sp_checkRight;
CREATE PROCEDURE sp_checkRight (
    out pRetCode           int,
    in pAccount            varchar(50),
    in pModelId            int,
    in pMeta               varchar(50), -- 数据类型
    in pNeedRight          varchar(50)  -- 需要的权限
) 
proc:BEGIN
	declare vPid        int;
	declare vRole       char(20);
	declare vRight      char(255) default null;
	
	set pRetCode = 4100;
    if(not exists(select * from t_ms_user where account=pAccount and status='actived')) then
        leave proc;
    end if;
	
	set vPid = pModelId;
	
	-- 查看范围中是否有权限，上层的管理员可以管理下层的数据
    loop_label: LOOP
        select val into vRole
          from t_ms_model m, t_ms_user u
         where m.pid = vPid
           and m.name = pAccount
           and m.meta = 'admin'
           and u.account = pAccount
           and u.status = 'actived';
           
        if(found_rows() > 0) then
            -- 根据角色判断是否有权限
            select oprRight into vRight from t_ms_right where role=vRole and meta=pMeta;
            if(vRight is not null and (instr(vRight, pNeedRight) or vRight = '*')) then
                set pRetCode = 0;
                leave loop_label;
            end if;
            
            select oprRight into vRight from t_ms_right where role=vRole and meta='*';
            if(vRight is not null and (instr(vRight, pNeedRight) or vRight = '*')) then
                set pRetCode = 0;
                leave loop_label;
            end if;
	    end if;
	    
        select pid into vPid from t_ms_model where id=vPid;
        if(found_rows() <= 0) then
            leave loop_label;
        end if;
	END LOOP loop_label;
END;
