DROP PROCEDURE IF EXISTS sp_adInfoSaveMaterialFile;

CREATE PROCEDURE sp_adInfoSaveMaterialFile (
    out pRetCode            int,
    
    in pOperator           varchar(50),
    in pAid                int,
    in materialPath        varchar(255),
    in materialName        varchar(255)
   
)
proc: BEGIN
    declare vType           int;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    if(not exists(select * from t_mkt_ad_info where aid = pAid and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
    end if;
    
    
	SELECT 0 INTO pRetCode;
	
    if(exists(select  * from t_mkt_material_info where aid = pAid)) then
    
       update
           t_mkt_material_info
       set
           type = 0,
           show_name = materialName,
           path = materialPath,
           operator = pOperator,
           update_time = now(),
           state = 1
       where
           aid = pAid;
    else
        insert into t_mkt_material_info ( 
               aid,
               type,
               show_name,
               path,
               operator,
               create_time,
               update_time,
               state
           )
           values(
               pAid,
               0,
               materialName,
               materialPath,
               pOperator,
               now(),
               now(),
               1
           );
    end if; 
    
     if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
   
end;