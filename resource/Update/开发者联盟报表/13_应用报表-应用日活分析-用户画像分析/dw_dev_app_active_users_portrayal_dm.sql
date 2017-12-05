# ----------------------------------------------------------------------------
#  file name:   dw_dev_app_active_users_portrayal_dm.sql
#  copyright(c)huawei technologies co.,ltd.1998-2011.all rights reserved.
#  purpose:
#  describe: 应用用户活跃日活-用户画像表
#  字段描述：
#    user_id               开发者ID  
#    app_id                应用ID   
#    app_name              应用名
#    active_all            应用用户日活数
#    age                   年龄段,格式为age_0_16/.../age_50+/age_x
#    gender                性别,格式为g_m/g_f/g_x
#
#  input:  dim_dev_app_submit_list_dm,dim_persona_label_0level_allsummary_ds
#  output: dw_dev_app_active_users_portrayal_dm
#  author:  panjiankang/p84035806
#  creation date:  2014-04-21
# ----------------------------------------------------------------------------

hive -e"
CREATE EXTERNAL TABLE IF NOT EXISTS dw_dev_app_active_users_portrayal_dm
(   
    user_id               STRING,
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
LOCATION '$hadoopuser/data/DW/DEV/dw_dev_app_active_users_portrayal_dm';

INSERT OVERWRITE TABLE dw_dev_app_active_users_portrayal_dm
PARTITION (pt_d='$date')
SELECT
    MAX(t1.user_id)     AS user_id,
    t1.app_id           AS app_id,
    MAX(t1.app_name)    AS app_name,
    COUNT(t1.imei)      AS active_all,
    t2.age              AS age,
    t2.gender           AS gender
FROM
( 
    SELECT
        user_id,
        imei,
        app_id,
        app_name
    FROM dim_dev_app_submit_list_dm
    WHERE pt_d = '$date'
        AND to_date(last_use_date) = '$date_ep'
)t1
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
)t2
ON t11.imei = t12.device_id
GROUP BY t1.app_id,
         t2.age,
         t2.gender
;
"