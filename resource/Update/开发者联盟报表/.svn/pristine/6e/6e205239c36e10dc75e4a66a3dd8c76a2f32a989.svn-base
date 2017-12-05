# ----------------------------------------------------------------------------
#  file name:   dw_dev_app_retention_terminal_dm.sql
#  copyright(c)huawei technologies co.,ltd.1998-2011.all rights reserved.
#  purpose:
#  describe: 应用用户留存人数-机型分布表
#  字段描述：
#    user_id               开发者ID  
#    app_id                应用ID   
#    app_name              应用名
#    terminal_type         机器型号
#    pt_retention          留存类型(1,3,7,30为多少天留存用户)
#    retention_users       留存人数
#    retention_rate        留存率
#
#  input:  dim_dev_app_submit_list_dm,DIM_HOTA_USA_EUR_CHINA_HK_DS
#  output: dw_dev_app_retention_terminal_dm
#  author:  panjiankang/p84035806
#  creation date:  2014-04-22
# ----------------------------------------------------------------------------

hive -e"
CREATE EXTERNAL TABLE IF NOT EXISTS dw_dev_app_retention_terminal_dm
(   
    user_id               STRING,
    app_id                STRING,
    app_name              STRING,  
    terminal_type         STRING,
    retention_users        INT,
    retention_rate         FLOAT
)
PARTITIONED BY(pt_d STRING,pt_retention STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/DEV/dw_dev_app_retention_terminal_dm';

set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;

INSERT OVERWRITE TABLE dw_dev_app_retention_terminal_dm
PARTITION (pt_d,pt_retention)
SELECT
    MAX(t1.user_id)                                                                                               AS user_id,
    t1.app_id                                                                                                     AS app_id,
    MAX(t1.app_name)                                                                                              AS app_name,
    t2.terminal_type                                                                                              AS terminal_type,
    COUNT(IF(to_date(t1.last_use_date) = '$date_ep', t1.imei, NULL))                                              AS retention_users,
    IF(COUNT( t1.imei ) > 0 ,COUNT(IF(to_date(t1.last_use_date) = '$date_ep', t1.imei, NULL))/COUNT( t1.imei ),0) AS retention_rate,
    regexp_replace(to_date(t1.activate_date),'-','')                                                              AS pt_d,
    DATEDIFF('$date_ep', to_date(t1.activate_date))                                                               AS pt_retention
FROM
( 
    SELECT
        user_id,
        imei,
        app_id,
        app_name,
        activate_date,
        last_use_date
    FROM dim_dev_app_submit_list_dm
    WHERE pt_d = '$date'
        AND to_date(activate_date) IN (DATE_SUB('$date_ep',1),DATE_SUB('$date_ep',3),DATE_SUB('$date_ep',7),DATE_SUB('$date_ep',30))
)t1
LEFT OUTER JOIN
(
    SELECT 
        decrypt_imei,
        regexp_replace(TRIM(regexp_replace(upper(device_name),'HUAWEI','')),' ','')  AS terminal_type
    FROM DIM_HOTA_USA_EUR_CHINA_HK_DS 
    WHERE device_name IS NOT NULL 
        AND device_name <> ''
)t2
ON t1.imei = t2.decrypt_imei
GROUP BY t1.app_id,
         to_date(t1.activate_date),
         t2.terminal_type
;
"