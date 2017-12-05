delimiter //
DROP PROCEDURE IF EXISTS sp_websiteModify;
CREATE PROCEDURE sp_websiteModify (
    out pRetCode            int,
    in pCreator             varchar(50),
    in pId        int,
    in pSiteId              varchar(2000),
    in pSiteName            varchar(50),
    in pSiteUrl             varchar(200),   
    in pGroupId             int
 
)
proc: BEGIN
    
    if(not exists(select * from t_wda_site_basic_info where id=pId)) then
        select 2001 into pRetCode;
        leave proc;
    end if;
  
    if(exists(select * from t_wda_site_basic_info where (site_name=pSiteName  or  site_id = pSiteId ) and id != pId)) then
        select 2000 into pRetCode;
        leave proc;
    end if;
    
    update t_wda_site_basic_info set
        creator = ifnull(pCreator, creator),
  site_id = ifnull(pSiteId, site_id),
  site_name = ifnull(pSiteName, site_name),
  site_url = ifnull(pSiteUrl, site_url),
  group_id = pGroupId
    where id = pId; 

    if(row_count() <= 0) then
        select 4 into pRetCode; -- ��ݿ����
        leave proc;
    end if;
    
    SELECT 0 INTO pRetCode;
end//
