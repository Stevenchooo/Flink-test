DROP PROCEDURE IF EXISTS sp_mktDicCommonModify;
CREATE PROCEDURE sp_mktDicCommonModify (
    out pRetCode            int,
    
    in  pOperator           varchar(50),
    in  dicId               int,
    in  parentId            int,
    in  pName               varchar(255)
)
proc: BEGIN
	 declare vType           int;
	 declare vKind           varchar(50);
    
	 if(dicId = 0) then
	    select null into dicId; 
	 end if;
	 
     if(parentId = 0) then
        select null into parentId; 
     end if;
     
     
	 select dicId;
	 select parentId;
	 select pName;
	 
     set vType = null;
    
     if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
     end if;
     
     
	if(pName is null or trim(pName) = '') then
	    select 6000 into pRetCode;
	    leave proc;
	end if;
	
	
	--  字典id不为空，进行更新
	if(dicId is not null) then
	
		 --  检查有没有更新记录
	    if(not exists(select * from t_mkt_dic_info where id=dicId)) then
	        select 2001 into pRetCode;
	        leave proc;
	    end if;
    
	   --   判断权限
	   if(not exists(select * from t_mkt_dic_info where id=dicId and dept_type = ifnull(vType,dept_type))) then
		    select 4100 into pRetCode;
	        leave proc;
	   end if;
	   
	    --  获取字典属性
       select dept_type into vType from t_mkt_dic_info where id=dicId;
       
	   --   查看有没有同名字典信息
	   select type into vKind from t_mkt_dic_info where id=dicId;
    
	    if(exists(select * from t_mkt_dic_info where name=pName and id != dicId and type = vKind and dept_type = vType)) then
	        select 8901 into pRetCode;
	        leave proc;
	    end if;
    
	    --   更新数据
	    update t_mkt_dic_info
        set name = ifnull(pName, name), 
            pid = ifnull(parentId, pid),
            update_time = ifnull(now(),update_time)
        where id = dicId;
         
        set pRetCode = 0;
	    leave proc;
	end if;
	
	
	
	--  字典id为空，进行新增
	if(dicId is null and parentId is not null) then
	    --  检查有没有更新记录
        if(not exists(select * from t_mkt_dic_info where id=parentId)) then
            select 2001 into pRetCode;
            leave proc;
        end if;
    
       --   判断权限
       if(not exists(select * from t_mkt_dic_info where id=parentId and dept_type = ifnull(vType,dept_type))) then
            select 4100 into pRetCode;
            leave proc;
       end if;
       
        --  获取字典属性
       select dept_type into vType from t_mkt_dic_info where id=parentId;
       
        if(exists(select * from t_mkt_dic_info where name=pName and type = 'web' and dept_type = vType)) then
            select 8901 into pRetCode;
            leave proc;
        end if;
    
        --   新增数据
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
            pName,
            parentId,
            'web',
            now(),
            now(),
            pOperator,
            vType
            );
         
        set pRetCode = 0;
        leave proc;
	end if;
	
end;

