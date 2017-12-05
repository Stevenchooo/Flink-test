DROP PROCEDURE IF EXISTS sp_reportData;
CREATE PROCEDURE sp_reportData (
    out pRetCode            int,
    in pOperator            varchar(50), -- 操作人
    in pServerId            int,
    in pReportItemId        int,
    in pStartTime           timestamp,
    in pEndTime             timestamp
)
proc: BEGIN
    if(not exists(select * from t_ms_model where id=pReportItemId and pid=pServerId)) then
        set pRetCode = 4100;
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;
    
    select id,name from t_ms_model where id=pReportItemId;
    
    select reportTime as 'time', val 
      from t_ms_report
     where reportItemId=pReportItemId 
       and reportTime>pStartTime
       and reportTime<=pEndTime;
end;