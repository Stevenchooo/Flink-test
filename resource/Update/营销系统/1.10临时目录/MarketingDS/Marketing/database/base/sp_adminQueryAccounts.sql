
DROP PROCEDURE IF EXISTS sp_adminQueryAccounts;
CREATE PROCEDURE sp_adminQueryAccounts (
    out pRetCode            int,
    in pPid                 int
)
proc: BEGIN
    set pRetCode = 0;
    
    select 
        u.account   as    id,
        concat(u.name,' (',u.account,')')   as    name
    from
        t_ms_user u
    where
        u.account not in (select name from t_ms_model m where m.pid = pPid and m.meta='admin')
    order by id;
    
   
end;