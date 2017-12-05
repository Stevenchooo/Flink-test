set global log_bin_trust_function_creators=1;

DROP FUNCTION IF EXISTS fun_isValidSubMeta;
CREATE FUNCTION fun_isValidSubMeta(
    pPid               int,
    pSubType           char(15)
) RETURNS boolean
BEGIN
    declare vSubTypeS  varchar(20);
    
    set vSubTypeS = concat('|', pSubType, '|');
    if(exists(select * from t_ms_model d, t_ms_meta m
               where d.id = pPid
                 and d.meta = m.name
                 and instr(m.subMetas, vSubTypeS) > 0)) then
        return true;
    else
        return false;
    end if;
END;
