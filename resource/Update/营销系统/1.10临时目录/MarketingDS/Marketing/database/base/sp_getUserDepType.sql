DELIMITER // 
DROP PROCEDURE IF EXISTS sp_getUserDepType;
CREATE PROCEDURE sp_getUserDepType (
    out pRetCode           int,
    in pAccount            varchar(50)
) 
proc:BEGIN

  
  set pRetCode = 4100;
    if(not exists(select * from t_ms_user where account=pAccount and status='actived')) then
        leave proc;
  end if;
  
  if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pAccount)) then
        select deptType into pRetCode from t_ms_user where account = pAccount;
  else
        set pRetCode = -1;
  end if; 
END
//
DELIMITER ;