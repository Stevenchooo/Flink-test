DELIMITER // 
DROP PROCEDURE IF EXISTS sp_dicInfoRemoveMediaWeb;
CREATE PROCEDURE sp_dicInfoRemoveMediaWeb (
    out pRetCode            int,
    
    in  pOperator           varchar(50),
    in  mediaId             int,
    in  webId               int
)
proc: BEGIN  
	
	
	declare vType           int;
	
	if(webId = 0) then
		set webId = null;
	end if;

    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    -- 判断用户有没有权限
    if(not exists(select * from t_mkt_dic_info where (id = mediaId or id = webId) and dept_type = ifnull(vType,dept_type))) then
        set  pRetCode = 4100;
        leave proc;
    end if;
    
    
	-- 判断广告位表里是否还有依赖的项 or ---> and 
	if(exists(select * from t_mkt_ad_info where media_type = mediaId and  web_name = ifnull(webId,web_name))) then
	    set  pRetCode = 9003;
	    leave proc;
    end if;
	
    -- 
    -- 判断是否存在该媒体
    if(not exists(select * from t_mkt_dic_info where id = mediaId)) then
        set pRetCode = 2001;
        leave proc;
    end if;
    
    if(not exists(select * from t_mkt_dic_info where id = webId and pid = mediaId)) then
        delete from t_mkt_dic_info where id = mediaId;
        set pRetCode = 0;
        leave proc;
    end if;
    
    -- 删除该网站记录
    delete from t_mkt_dic_info where id = webId and pid = mediaId;
    
    -- 判断该媒体是否还有其它网站，如果没有则删除该媒体
    if(not exists(select * from t_mkt_dic_info where id != webId and pid = mediaId)) then
        delete from t_mkt_dic_info where id = mediaId; 
    end if;
    
    set pRetCode = 0;
end


//  
DELIMITER ;