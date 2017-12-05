DROP PROCEDURE IF EXISTS sp_adminQuery;
CREATE PROCEDURE sp_adminQuery (
    out pRetCode            int,
    in pPid                 int,
    in pFrom                int,
    in pPerPage             int
)
proc: BEGIN
    set pRetCode = 0;
    
    select m.id as id,
           u.name as account,
           m.name as name,
           m.createTime as createTime,
           m.creator as creator,
           r.name as role
      from t_ms_model m, t_ms_role r,t_ms_user u
     where m.pid=pPid
       and m.meta='admin'
       and r.role=m.val
       and m.name=u.account
        order by id
      limit pFrom, pPerPage;
    
    -- 总数
    select count(*) as total
      from t_ms_model  
     where pid = pPid
       and meta = 'admin';
end;