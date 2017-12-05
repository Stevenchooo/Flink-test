DROP PROCEDURE IF EXISTS sp_userCreate;
CREATE PROCEDURE sp_userCreate (
    out pRetCode            int,
    
    in pCreator             varchar(50), -- 创建人
    in pAccount             varchar(50),
    in pPhoneNum            varchar(255),
    in pEmail               varchar(255),
    in pName                varchar(20),
    in pDepartment          varchar(50),
    in pDescription         varchar(50),
    in pShowFlag            int,
    in pDeptType            int

)
proc: BEGIN
	if(exists(select * from t_ms_user where account=pAccount)) then
	    select 2000 into pRetCode;
	    leave proc;
	end if;
	
    SELECT 0 INTO pRetCode;

    -- 因为创建用户的用户，一定比比被创建用户权限高，能够看到它的所有数据
    -- 所以密码可以不必立刻过期
    -- 但是，当低级用户创建，高级用户赋权时，会出现安全问题，低级用户知道高级用户的密码
    -- 因为此时被他创建的用户可能具有比他还要高级的权限
    -- 所以密码必须立即过期
    
    insert into t_ms_user (
        account,
        name, 
        phoneNum,
        creator,
        email,
        department,
        showFlag,
        description,
        deptType)
    values (
        pAccount,
        pName,
        pPhoneNum,
        pCreator,
        pEmail,
        pDepartment,
        pShowFlag,
        pDescription,
        pDeptType);
    
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
end;