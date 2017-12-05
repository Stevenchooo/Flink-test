delimiter //
DROP PROCEDURE IF EXISTS sp_honor_report_all;
CREATE PROCEDURE sp_honor_report_all
(
  OUT pRetCode    INT
)
COMMENT ''
proc: BEGIN
	declare vSelectSql varchar(1000);
	declare vSelectSql2 varchar(1000);
	declare vSelectSql3 varchar(1000);
	-- 报告类型
	set vSelectSql = concat('select id, report_typename as reportTypeName from dw_wda_honor_reportType where 1=1');
	SET @Sql = vSelectSql;
    prepare stmt FROM @Sql;    
    execute stmt;
    -- 报告热门关键词
    set vSelectSql2 = concat('select id, hot_keyword as hotKeyword from reportkeyword order by count desc, update_time desc limit 20');
    SET @Sql = vSelectSql2;
    prepare stmt FROM @Sql;    
    execute stmt;
    -- 所有报告信息
    set vSelectSql3 = concat('select id,report_back_img as reportBackImg, report_creator as reportCreator, report_down_user as reportDownUser,report_filename as reportFileName,report_filepath as reportFilePath,report_filetype as reportFileType,report_keyword as reportKeyword,report_manager_user as reportManagerUser,report_owner as reportOwner, report_time as reportTime,report_title as reportTitle, report_type_id as reportTypeId from reportshare where 1=1');
    set vSelectSql3 = concat(vSelectSql3,' order by report_time desc');
    SET @Sql = vSelectSql3;
    prepare stmt FROM @Sql;    
    execute stmt;
    
    
    set pRetCode = 0;
END// 