DROP PROCEDURE IF EXISTS sp_mktDicMediaModify;
CREATE PROCEDURE sp_mktDicMediaModify (
    out pRetCode            int,
    
    in  pOperator           varchar(50),
    in  dicId               int,
    in  pName               varchar(255)
)
proc: BEGIN
	 declare vType           int;
    
     set vType = null;
    
     if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
     end if;
     
     if(not exists(select * from t_mkt_dic_info where id = dicId and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
    end if;
    
    set pRetCode = 0;
	
    if(exists(select * from t_mkt_dic_info where id = dicId)) then
	    update t_mkt_dic_info
	    set name = ifnull(pName, name), 
	        update_time = ifnull(now(),update_time)
	    where id = dicId;
	else
	    insert into t_mkt_dic_info (
			name,
			pid,
			type,
			create_time,
			update_time,
			operator
	        )
	    values (
	        pName,
	        '',
	        'media',
			now(),
			now(),
			pOperator
	        );
	end if;
    
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
end;

