set global log_bin_trust_function_creators=1;

DROP FUNCTION IF EXISTS fun_getPathById;
CREATE FUNCTION fun_getPathById (
    pMid               int
) RETURNS varchar(255)
BEGIN
    declare vPath          varchar(255) default '';
    declare vName          varchar(255);
    declare vId            int;
    
    set vId = pMid;

    if(vId = 1) THEN
        return '/';
    end if;
    
    loop_label: LOOP
        select pid, name into vId, vName
          from t_ms_model
         where id = vId;
        
        if(found_rows() <> 1) then
            return vPath;
        end if;
        
        if(vId <= 0) then -- 到顶了
            return concat(vName, vPath);
        ELSE
            set vPath = concat(vName, '/', vPath);
        end if;
    END LOOP loop_label;
end;