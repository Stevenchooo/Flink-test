# ----------------------------------------------------------------------------
#  file name:   dw_dev_app_retention_terminal_top100_dm.sql
#  copyright(c)huawei technologies co.,ltd.1998-2011.all rights reserved.
#  purpose:
#  describe: 应用用户留存人数-机型分布表(取激活量大于10000的应用，FineReport展示使用)
#  字段描述： 
#    app_id                应用ID   
#    app_name              应用名
#    terminal_type         机器型号
#    pt_retention          留存类型(1,3,7,30为多少天留存用户)
#    retention_rate        留存率
#    activate_user         当天激活用户
#    retention_users       对应留存用户
#
#  input:  dim_dev_app_submit_list_dm,DIM_HOTA_USA_EUR_CHINA_HK_DS,dw_dev_app_retention_rate_top100_dm
#  output: dw_dev_app_retention_terminal_top100_dm
#  author:  panjiankang/p84035806
#  creation date:  2014-04-22
# ----------------------------------------------------------------------------

hive -e"
CREATE TABLE IF NOT EXISTS tmp_dev_app_retention_terminal_top100
(   
    app_id                STRING,
    app_name              STRING,  
    terminal_type         STRING,
    activate_user         INT,
    retention_users       INT,
    retention_rate        FLOAT,
    pt_d                  STRING,
    pt_retention          STRING
);

INSERT OVERWRITE TABLE tmp_dev_app_retention_terminal_top100
SELECT
    t1.app_id                                                                                                     AS app_id,
    MAX(t2.app_name)                                                                                              AS app_name,
    t3.terminal_type                                                                                              AS terminal_type,
    COUNT( t2.imei )                                                                                              AS activate_user,
    COUNT(IF(to_date(t2.last_use_date) = '$date_ep', t2.imei, NULL))                                              AS retention_users,
    IF(COUNT( t2.imei ) > 0 ,COUNT(IF(to_date(t2.last_use_date) = '$date_ep', t2.imei, NULL))/COUNT( t2.imei ),0) AS retention_rate,
    regexp_replace(to_date(t2.activate_date),'-','')                                                              AS pt_d,
    DATEDIFF('$date_ep', to_date(t2.activate_date))                                                               AS pt_retention
FROM
(
    SELECT 
        DISTINCT app_id
    FROM dw_dev_app_retention_rate_top100_dm
    WHERE pt_d = '$date'
)t1
LEFT OUTER JOIN
( 
    SELECT
        imei,
        app_id,
        app_name,
        activate_date,
        last_use_date
    FROM dim_dev_app_submit_list_dm
    WHERE pt_d = '$date'
        AND to_date(activate_date) IN (DATE_SUB('$date_ep',1),DATE_SUB('$date_ep',3),DATE_SUB('$date_ep',7),DATE_SUB('$date_ep',30))
)t2
ON t1.app_id = t2.app_id
LEFT OUTER JOIN
(
    SELECT 
        decrypt_imei,
        regexp_replace(TRIM(regexp_replace(upper(device_name),'HUAWEI','')),' ','')  AS terminal_type
    FROM DIM_HOTA_USA_EUR_CHINA_HK_DS 
    WHERE device_name IS NOT NULL 
        AND device_name <> ''
)t3
ON t2.imei = t3.decrypt_imei
GROUP BY t1.app_id,
         to_date(t2.activate_date),
         t3.terminal_type;

CREATE EXTERNAL TABLE IF NOT EXISTS dw_dev_app_retention_terminal_top100_dm
(   
    app_id                STRING,
    app_name              STRING,  
    terminal_type         STRING,
    retention_rate        FLOAT,
    activate_user         INT,
    retention_users       INT
)
PARTITIONED BY(pt_d STRING,pt_retention STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/DEV/dw_dev_app_retention_terminal_top100_dm';

set hive.exec.dynamic.partition=true;
set hive.exec.dynamic.partition.mode=nonstrict;

INSERT OVERWRITE TABLE dw_dev_app_retention_terminal_top100_dm
PARTITION (pt_d,pt_retention)
SELECT 
    t2.app_id          AS app_id,
    t2.app_name        AS app_name,
    t1.terminal_type   AS terminal_type,
    t2.retention_rate  AS retention_rate,
    t2.activate_user   AS activate_user,
    t2.retention_users AS retention_users,
    t1.pt_d            AS pt_d,
    t1.pt_retention    AS pt_retention
FROM
(
    SELECT 
        terminal_type,
        retention_rate, 
        pt_d,
        pt_retention
    FROM 
    (   
        SELECT   
            key_words,
            terminal_type,
            retention_rate, 
            pt_d,
            pt_retention,
            rowudf(key_words) as rowudf
        FROM
        (
            SELECT 
                key_words,
                terminal_type,
                retention_rate, 
                pt_d,
                pt_retention
            FROM 
            ( 
                SELECT
                    CONCAT(pt_d, ',', pt_retention) AS key_words,
                    pt_d,
                    terminal_type,
                    pt_retention,
                    IF(SUM(activate_user) > 0,SUM(retention_users)/SUM(activate_user),0) AS retention_rate
                FROM tmp_dev_app_retention_terminal_top100
                GROUP BY pt_d,
                         terminal_type,
                         pt_retention
            )t
            DISTRIBUTE BY key_words
            SORT BY key_words, retention_rate DESC
        )t
    )t
    WHERE rowudf <= 100
)t1
LEFT OUTER JOIN 
(
    SELECT 
        app_id,
        app_name,
        terminal_type,
        retention_rate,
        activate_user,
        retention_users,
        pt_d,
        pt_retention
    FROM tmp_dev_app_retention_terminal_top100
)t2
ON (t1.terminal_type = t2.terminal_type AND t1.pt_d = t2.pt_d AND t1.pt_retention = t2.pt_retention);
DROP TABLE IF EXISTS tmp_dev_app_retention_terminal_top100;
"