delimiter //
DROP PROCEDURE IF EXISTS sp_modelGetPathById;
CREATE PROCEDURE sp_modelGetPathById (
    out pRetCode            int,
    in pMid                 int
)
proc: BEGIN
    declare vPath          varchar(255) default '';
    declare vName          varchar(255);
    declare vId            int;
    
    set vId = pMid;
    
    loop_label: LOOP
        select pid, name into vId, vName
          from t_ms_model
         where id = vId;
        
        if(found_rows() <> 1) then
            select 2001 into pRetCode;
            leave proc;
        end if;
        set vPath = concat(vName, '/', vPath);

        if(vId = 1) then -- 到顶了
                        set vPath = concat('/', vPath);
            leave loop_label;
        end if;
    END LOOP loop_label;

    SELECT 0 INTO pRetCode;
    select vPath as path; 
end//