DROP PROCEDURE IF EXISTS sp_userDetail;
CREATE PROCEDURE sp_userDetail (
    out pRetCode            int,
    in pAccount             varchar(255)
)
proc: BEGIN
    SELECT 0 INTO pRetCode;
    
    select account,createTime,phoneNum,email from t_ms_user where account=pAccount;
    
    select fun_getPathById(pid) as path, r.name as role 
     from t_ms_model m, t_ms_role r
    where m.name = pAccount
      and m.meta = 'admin'
      and r.role = m.val;
end;