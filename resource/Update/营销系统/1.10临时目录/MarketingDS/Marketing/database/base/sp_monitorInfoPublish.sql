DROP PROCEDURE IF EXISTS sp_monitorInfoPublish;
DELIMITER //
CREATE PROCEDURE sp_monitorInfoPublish (
    out pRetCode            int,
    
    in  pAid                int,
    in  pOperator           varchar(255)
)
proc: BEGIN
        
    declare vType           int;

   
    
    
    declare default_Click_URL        varchar(255);
    declare default_Exposure_URL     varchar(255);
    declare bicode                   varchar(255);
    declare land_cid                 varchar(255);
    declare url                      varchar(255);
    declare turl                      varchar(255);
    
    declare click_URL                varchar(1024);
    declare exposure_URL             varchar(1024);
    
    declare formId                   varchar(50);
    declare len                      int;
    declare cidLen                   int;
    
    declare len1                     int;
    
    set vType = null;
    
    if(not exists(select * from t_ms_model where meta = 'admin' and val = 'root' and name = pOperator)) then
        select deptType into vType from t_ms_user where account = pOperator;
    end if;
    
    if(not exists(select * from t_mkt_ad_info where aid = pAid and dept_type = ifnull(vType,dept_type))) then
        select 4100 into pRetCode;
        leave proc;
    end if;
    SELECT 1 INTO pRetCode;
    
    select concat(pAid,UNIX_TIMESTAMP()) into bicode;
    select dic_value into  default_Click_URL   from t_mkt_common_dic_info where type = 'click_url' limit 1;
    select dic_value into  default_Exposure_URL from t_mkt_common_dic_info where type = 'exposure_url' limit 1;
    select cid into land_cid from t_mkt_land_info where aid = pAid;
    select REPLACE(land_link,'&','%26') into  turl from t_mkt_land_info where aid = pAid;
    select REPLACE(turl,'#','%23') into  url from t_mkt_land_info where aid = pAid;
    
    
    select LOCATE('cid=',url) into cidLen; 
    if(cidLen = 0) then
        select LOCATE('?',url) into len; 
        select LOCATE('#',url) into len1; 
        update t_mkt_land_info set sid = bicode where aid = pAid;
        
        if(len = 0 and len1 = 0) then
            set url = concat(url,'?sid=',bicode);
        else
            set url = concat(url,'%26sid=',bicode);
        end if;
    end if;
     
    set click_URL = concat(default_Click_URL,'?sid=', bicode,'&cid=',land_cid ,'&url=', url);
    set exposure_URL = concat(default_Exposure_URL,'?sid=', bicode,'&cid=',land_cid);
    
    
    delete from t_mkt_monitor_info where aid = pAid;
    
     --  获取属性
   select  dept_type into vType from t_mkt_ad_info where aid = pAid;
   
    insert into t_mkt_monitor_info (
        aid,
        bi_code,
        exposure_url,
        click_url,
        create_time,
        update_time,
        operator,
        dept_type
        )
    values (
        pAid,
        bicode,
        exposure_URL,
        click_URL,
        now(),
        now(),
        pOperator,
        vType);
    
        update
            t_mkt_ad_info
	    set
	        state = 3
	    where
	        aid = pAid;
	        
	   SELECT 0 INTO pRetCode;
        
end
//  
DELIMITER ;