# ----------------------------------------------------------------------------
#  file name:   dw_dev_app_active_users_portrayal_frshow_dm.sql
#  copyright(c)huawei technologies co.,ltd.1998-2011.all rights reserved.
#  purpose:
#  describe: 应用用户活跃日活-用户画像表
#  字段描述：
#    app_id                应用ID   
#    app_name              应用名
#    active_all            应用用户日活数
#    age                   年龄段,格式为age_0_16/.../age_50+/age_x
#    gender                性别,格式为g_m/g_f/g_x
#
#  input:  dim_dev_app_submit_list_dm,dim_persona_label_0level_allsummary_ds
#  output: dw_dev_app_active_users_portrayal_frshow_dm
#  author:  panjiankang/p84035806
#  creation date:  2014-04-21
# ----------------------------------------------------------------------------

hive -e"
CREATE EXTERNAL TABLE IF NOT EXISTS dw_dev_app_active_users_portrayal_frshow_dm
(   
    app_id                STRING,
    app_name              STRING,  
    age                   STRING,
    gender                STRING,
    active_all            INT
)
PARTITIONED BY(pt_d STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/DEV/dw_dev_app_active_users_portrayal_frshow_dm';

INSERT OVERWRITE TABLE dw_dev_app_active_users_portrayal_frshow_dm
PARTITION (pt_d='$date')
SELECT
    t1.app_id           AS app_id,
    MAX(t2.app_name)    AS app_name,
    COUNT(t2.imei)      AS active_all,
    t3.age              AS age,
    t3.gender           AS gender
FROM
( 
    SELECT 
        app_id,
        COUNT(imei) AS activate_all
    FROM dim_dev_app_submit_list_dm
    WHERE pt_d = '$date'
    GROUP BY app_id
    HAVING COUNT(imei) > 10000
)t1
LEFT OUTER JOIN
(
    SELECT
        imei,
        app_id,
        app_name
    FROM dim_dev_app_submit_list_dm
    WHERE pt_d = '$date'
        AND to_date(last_use_date) = '$date_ep'
)t2
ON t1.app_id = t2.app_id
LEFT OUTER JOIN
(
    SELECT 
        device_id,
        MAX(gender) AS gender,
        MAX(age)    AS age
    FROM dim_persona_label_0level_allsummary_ds
    WHERE gender IS NOt NULL 
        AND gender <> ''
        AND age IS NOT NULL 
        AND age <> ''
    GROUP BY device_id
)t3
ON t2.imei = t3.device_id
GROUP BY t1.app_id,
         t3.age,
         t3.gender
;
"