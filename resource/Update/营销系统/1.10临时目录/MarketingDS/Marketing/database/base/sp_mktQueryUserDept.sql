
DROP PROCEDURE IF EXISTS `sp_mktQueryUserDept`;
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_mktQueryUserDept`(
     out pRetCode               int,
     in pOperator               varchar(50)

)
proc: BEGIN

  declare tmpCount          int default -1;

  
  -- set tmpDateEnd = date_format(pPt_d,'%Y%m%d');
  set pRetCode = 0;
  select count(*) into tmpCount from t_ms_user where account = pOperator;
  if ( tmpCount < 0 ) then
    set pRetCode = 125126;
    leave proc;
  end if;
  
  select
      deptType
  from t_ms_user where account = pOperator;

  end//
DELIMITER ;