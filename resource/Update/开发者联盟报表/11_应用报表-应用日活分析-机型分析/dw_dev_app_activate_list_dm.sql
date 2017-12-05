# ----------------------------------------------------------------------------
#  file name:   dw_dev_app_activate_list_dm.sql
#  copyright(c)huawei technologies co.,ltd.1998-2011.all rights reserved.
#  purpose:
#  describe: 应用激活表
#  字段描述：
#         user_id                 开发者ID                         
#         app_id                  应用ID
#         app_name                应用名称
#         activate_all            激活总量
#         activate_new            新增激活量
#         user_new                当天日活量
#
#  input:  dim_dev_app_submit_list_dm
#  output: dw_dev_app_activate_list_dm
#  author:  panjiankang/p84035806
#  creation date:  2015-03-24
#  Last Modified:  2015-04-17
#  Last Modified By: panjiankang p84035806
# ----------------------------------------------------------------------------

hive -e"
CREATE EXTERNAL TABLE IF NOT EXISTS dw_dev_app_activate_list_dm
(   
    user_id               STRING,
    app_id                STRING,
    app_name              STRING,
    activate_all          INT,
    activate_new          INT,
    user_new              INT
)
PARTITIONED BY(pt_d STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/DEV/dw_dev_app_activate_list_dm';

INSERT OVERWRITE TABLE dw_dev_app_activate_list_dm
PARTITION (pt_d = '$date')

SELECT 
    MAX(user_id)  AS user_id,
    app_id,
    MAX(app_name) AS app_name,
    COUNT(app_id) AS activate_all,
    COUNT(IF(activate_date = '$date_ep', imei, NULL)) AS activate_new,
    COUNT(IF(last_use_date = '$date_ep', imei, NULL)) AS user_new
FROM dim_dev_app_submit_list_dm
WHERE pt_d = '$date'
GROUP BY app_id;
"