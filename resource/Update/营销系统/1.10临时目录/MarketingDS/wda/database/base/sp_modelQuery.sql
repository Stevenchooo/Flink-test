delimiter //
DROP PROCEDURE IF EXISTS sp_modelQuery;
CREATE PROCEDURE sp_modelQuery (
    out pRetCode            int,
    in pOperator            varchar(50), -- 操作人
    in pPid                 int,
    in pMeta                char(15),
    in pVisible             boolean,
    in pFrom                int,
    in pPerPage             int,
    in pSegments            varchar(1024)
)
proc: BEGIN
	declare vSql            varchar(1024) default 'select ';
	
    set pRetCode = 0;
    
    if(pSegments is not null and pSegments <> '') THEN
        set vSql = concat(vSql, pSegments);
    else
        set vSql = concat(vSql, 'id, name, meta');
    end if;
    
    set vSql = concat(vSql, ' from t_ms_model where pid=', pPid);
    
    if(pMeta is not null) then
        set vSql = concat(vSql, ' and meta=\'', pMeta, '\'');
    end if;
    
    if(pVisible is not null) then
        set vSql = concat(vSql, ' and visible=', pVisible);
    end if;

    SET vSql = concat(vSql, ' order by id ');
    if(pFrom is not null) then
        set vSql = concat(vSql, ' limit ', pFrom);
	    if(pPerPage is not null) then
	        set vSql = concat(vSql, ',', pPerPage);
	    end if;
    end if;

    SET @Sql = vSql;
    
    PREPARE stmt FROM @Sql;    
    EXECUTE stmt;
    
    -- 总数
    select count(*) as total
      from t_ms_model  
     where pid = pPid
       and meta = ifnull(pMeta, meta)
       and visible = ifnull(pVisible, visible);
end//