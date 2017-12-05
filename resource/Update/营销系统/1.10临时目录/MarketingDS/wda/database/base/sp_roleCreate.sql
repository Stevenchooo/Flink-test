delimiter //
DROP PROCEDURE IF EXISTS sp_roleCreate;
CREATE PROCEDURE sp_roleCreate (
    out pRetCode            int,
    
    in pRole                varchar(50),
    in pName                varchar(50),
    in pDesc                varchar(255)
)
proc: BEGIN
	if(exists(select * from t_ms_role where role=pRole)) then
	    select 2000 into pRetCode;
	    leave proc;
	end if;
	
    if(exists(select * from t_ms_role where name=pName)) then
        select 2000 into pRetCode;
        leave proc;
    end if;
	
    set pRetCode = 0;

    insert into t_ms_role (
        role,
        name, 
        description
    ) values (
        pRole,
        pName,
        pDesc
    );
    
    if(row_count() <= 0) then
        set pRetCode = 4; -- 数据库错误
    end if;
end//