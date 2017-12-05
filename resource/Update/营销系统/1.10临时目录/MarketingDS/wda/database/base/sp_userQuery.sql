delimiter //
DROP PROCEDURE IF EXISTS sp_userQuery;
CREATE PROCEDURE sp_userQuery (
    out pRetCode            int,
    in pAccount             varchar(50),
    in pFrom                int,
    in pPerPage             int
)
proc: BEGIN
    declare vAccount        varchar(100);
    
    set pRetCode = 0;
    if(pAccount is not null) then
        set vAccount = concat('%', pAccount, '%');
    else
        set vAccount = '%%';
    end if;
    
    select account,createTime,phoneNum,email,creator,description as 'desc'
      from t_ms_user
     where status <> 'del'
       and account like vAccount
     order by createTime desc
     limit pFrom,pPerPage;
    
    -- 总数
    select count(*) as total
      from t_ms_user
     where status <> 'del'
       and account like vAccount;
end//