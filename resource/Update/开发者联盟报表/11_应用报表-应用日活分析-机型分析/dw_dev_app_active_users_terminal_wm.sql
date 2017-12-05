# ----------------------------------------------------------------------------
#  file name:   dw_dev_app_active_users_terminal_wm.sql
#  copyright(c)huawei technologies co.,ltd.1998-2011.all rights reserved.
#  purpose:
#  describe: 应用用户活跃日活-机型分布表
#  字段描述：
#    app_id                应用ID   
#    app_name              应用名
#    active_all            应用用户日活数
#    terminal_num          机型数量
#    terminal_type         机器型号
#    period                机器使用时间
#
#  input:  dim_dev_app_submit_list_dm,DIM_HOTA_USA_EUR_CHINA_HK_DS
#  output: dw_dev_app_active_users_terminal_wm
#  author:  panjiankang/p84035806
#  creation date:  2014-04-17
# ----------------------------------------------------------------------------

hive -e"
CREATE EXTERNAL TABLE IF NOT EXISTS dw_dev_app_active_users_terminal_wm
(   
    app_id                STRING,
    app_name              STRING,  
    terminal_type         STRING,
    period                STRING,
    active_all            INT,
    terminal_num          INT
)
PARTITIONED BY(pt_w STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/DEV/dw_dev_app_active_users_terminal_wm';

INSERT OVERWRITE TABLE dw_dev_app_active_users_terminal_wm
PARTITION (pt_w='$week')
SELECT 
    t1.app_id        AS app_id, 
    t1.app_name      AS app_name,
    t1.terminal_type AS terminal_type,
    t1.period        AS period,
    t1.active_all    AS active_all,
    t2.terminal_num  AS terminal_num
FROM
(
    SELECT
        t11.app_id           AS app_id,
        MAX(t11.app_name)    AS app_name,
        COUNT(t11.imei)      AS active_all,
        t12.terminal_type    AS terminal_type,
        CASE 
            WHEN datediff('$sunday', to_date(create_time)) <= 30 THEN '30'
            WHEN 30 <  datediff('$sunday', to_date(create_time)) <= 90  THEN '90'
            WHEN 90 <  datediff('$sunday', to_date(create_time)) <= 180 THEN '180'
            WHEN 180 < datediff('$sunday', to_date(create_time)) <= 365 THEN '365'
            WHEN 365 < datediff('$sunday', to_date(create_time)) <= 730 THEN '730'
            ELSE '9999'
        END                  AS period
    FROM
    ( 
        SELECT
            t112.imei     AS imei,
            t111.app_id   AS app_id,
            t112.app_name AS app_name
        FROM 
        (
            SELECT 
                app_id,
                COUNT(imei) AS activate_all
            FROM dim_dev_app_submit_list_dm
            WHERE pt_d = '$sunday_ep'
            GROUP BY app_id
            HAVING COUNT(imei) > 10000
        )t111
        LEFT OUTER JOIN
        (
            SELECT
                imei,
                app_id,
                app_name
            FROM dim_dev_app_submit_list_dm
            WHERE pt_d = '$sunday_ep'
                AND datediff(to_date(last_use_date),'$monday')>=0 
                AND datediff(to_date(last_use_date),'$sunday')<=0
        )t112
        ON t111.app_id = t112.app_id
    )t11
    LEFT OUTER JOIN
    (
        SELECT 
            decrypt_imei,
            regexp_replace(TRIM(regexp_replace(upper(device_name),'HUAWEI','')),' ','')  AS terminal_type,
            create_time
        FROM DIM_HOTA_USA_EUR_CHINA_HK_DS 
        WHERE device_name IS NOT NULL 
            AND device_name <> ''
            AND firmware_version IS NOT NULL 
            AND firmware_version <> ''
    )t12
    ON t11.imei = t12.decrypt_imei
    GROUP BY t11.app_id,
             t12.terminal_type,
             CASE 
                WHEN datediff('$sunday', to_date(create_time)) <= 30 THEN '30'
                WHEN 30 <  datediff('$sunday', to_date(create_time)) <= 90  THEN '90'
                WHEN 90 <  datediff('$sunday', to_date(create_time)) <= 180 THEN '180'
                WHEN 180 < datediff('$sunday', to_date(create_time)) <= 365 THEN '365'
                WHEN 365 < datediff('$sunday', to_date(create_time)) <= 730 THEN '730'
                ELSE '9999'
             END       
)t1
LEFT OUTER JOIN
(
    SELECT
        COUNT(t21.imei)      AS terminal_num,
        t22.terminal_type    AS terminal_type,
        CASE 
            WHEN datediff('$sunday', to_date(create_time)) <= 30 THEN '30'
            WHEN 30 <  datediff('$sunday', to_date(create_time)) <= 90  THEN '90'
            WHEN 90 <  datediff('$sunday', to_date(create_time)) <= 180 THEN '180'
            WHEN 180 < datediff('$sunday', to_date(create_time)) <= 365 THEN '365'
            WHEN 365 < datediff('$sunday', to_date(create_time)) <= 730 THEN '730'
            ELSE '9999'
        END                 AS period
    FROM
    ( 
        SELECT DISTINCT imei
        FROM dim_dev_app_submit_list_dm
        WHERE pt_d = '$sunday_ep'
    )t21
    LEFT OUTER JOIN
    (
        SELECT 
            decrypt_imei,
            regexp_replace(TRIM(regexp_replace(upper(device_name),'HUAWEI','')),' ','')  AS terminal_type,
            create_time
        FROM DIM_HOTA_USA_EUR_CHINA_HK_DS 
        WHERE device_name IS NOT NULL 
            AND device_name <> ''
            AND firmware_version IS NOT NULL 
            AND firmware_version <> ''
    )t22
    ON t21.imei = t22.decrypt_imei
    GROUP BY t22.terminal_type,
             CASE 
                WHEN datediff('$sunday', to_date(create_time)) <= 30 THEN '30'
                WHEN 30 <  datediff('$sunday', to_date(create_time)) <= 90  THEN '90'
                WHEN 90 <  datediff('$sunday', to_date(create_time)) <= 180 THEN '180'
                WHEN 180 < datediff('$sunday', to_date(create_time)) <= 365 THEN '365'
                WHEN 365 < datediff('$sunday', to_date(create_time)) <= 730 THEN '730'
                ELSE '9999'
             END       
)t2
ON (t1.terminal_type = t2.terminal_type AND t1.period = t2.period);
"