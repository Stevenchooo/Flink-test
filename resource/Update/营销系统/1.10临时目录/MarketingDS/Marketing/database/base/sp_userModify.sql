DROP PROCEDURE IF EXISTS sp_userModify;
CREATE PROCEDURE sp_userModify (
    out pRetCode            int,
    in pAccount             varchar(50),
    in pPhoneNum            varchar(255),
    in pEmail               varchar(255),
    in pName                varchar(20),
    in pDepartment          varchar(50),
    in pDescription         varchar(50),
    in pShowFlag            int,
    in pDepartmentType      int
)
proc: BEGIN
    if(not exists(select * from t_ms_user where account=pAccount)) then
        select 2001 into pRetCode;
        leave proc;
    end if;
    
    update t_ms_user set
        phoneNum = ifnull(pPhoneNum, phoneNum), 
        email = ifnull(pEmail, email),
        name = ifnull(pName,name),
        department = ifnull(pDepartment,department),
        description = ifnull(pDescription,description),
        showFlag = ifnull(pShowFlag,showFlag),
        deptType =  ifnull(pDepartmentType,deptType)
     where account = pAccount; 

    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;
end;