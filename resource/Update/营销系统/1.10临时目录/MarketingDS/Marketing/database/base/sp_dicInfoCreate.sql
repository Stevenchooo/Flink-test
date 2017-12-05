DROP PROCEDURE IF EXISTS sp_dicInfoCreate;
CREATE PROCEDURE sp_dicInfoCreate (
    out pRetCode            int,
    
	in  pOperator              varchar(50),
	in  mktDicInfoName         varchar(255) ,
	in  mktDicInfoType         varchar(255) ,
	in  mktDicPid              int
   
)
proc: BEGIN
	
	declare vType           int;

    set vType = null;
    
    select deptType into vType from t_ms_user where account = pOperator;
    
    
    
    if(exists(
            select 
                * 
            from 
                t_mkt_dic_info 
            where 
                name=mktDicInfoName 
            and type = mktDicInfoType 
            and if(mktDicPid is null, 1=1, pid = mktDicPid)
            and dept_type = ifnull(vType,dept_type)
               )
        ) then
        select 2000 into pRetCode;
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;

    -- 因为创建用户的用户，一定比比被创建用户权限高，能够看到它的所有数据
    -- 所以密码可以不必立刻过期
    -- 但是，当低级用户创建，高级用户赋权时，会出现安全问题，低级用户知道高级用户的密码
    -- 因为此时被他创建的用户可能具有比他还要高级的权限
    -- 所以密码必须立即过期

    
    
    insert into t_mkt_dic_info (
		name,
		pid,
		type,
		create_time,
		update_time,
		operator,
		dept_type
        )
    values (
        mktDicInfoName,
        mktDicPid,
        mktDicInfoType,
		now(),
		now(),
		pOperator,
		vType
        );
    
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
end;