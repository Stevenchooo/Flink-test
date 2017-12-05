set global log_bin_trust_function_creators=1;

DROP FUNCTION IF EXISTS fun_cutOldPassword;
CREATE FUNCTION fun_cutOldPassword (
    pOldPwds               varchar(255),
    pMaxOld                int           -- 最多能保留的密码个数
) RETURNS varchar(255)
BEGIN
    declare vPos           int default 1;
    declare vNum           int default 0;
    
    loop_label: LOOP
        set vPos = LOCATE(',', pOldPwds, vPos);
        if(vPos <= 0) then
            return pOldPwds;
        end if;
        set vNum = vNum + 1;
        
        if(vNum >= pMaxOld) then
            return LEFT(pOldPwds, vPos);
        end if;
        set vPos = vPos + 1;
    END LOOP loop_label;
end;