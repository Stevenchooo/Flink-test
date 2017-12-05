delimiter //
DROP PROCEDURE IF EXISTS sp_websiteCreate;
CREATE PROCEDURE sp_websiteCreate (
    out pRetCode            int,
    in pCreator             varchar(50), -- 创建人
    in pSiteId              varchar(2000),
    in pSiteName            varchar(200),
    in pSiteUrl             varchar(200),
    in pGroupId             int
)
proc: BEGIN

    if(exists(select * from t_wda_site_basic_info where site_name=pSiteName or site_id=pSiteId)) then
  select 2000 into pRetCode;
  leave proc;
    end if;
    
    insert into t_wda_site_basic_info (
  creator,
  site_id,
  site_name,
  site_url,
  group_id
    )
    values (
  pCreator,
  pSiteId,
  pSiteName,
  pSiteUrl,
  pGroupId
    );
    
    if(row_count() <= 0) then
        select 4 into pRetCode; -- 数据库错误
        leave proc;
    end if;
    
    set pRetCode = 0;

end//
