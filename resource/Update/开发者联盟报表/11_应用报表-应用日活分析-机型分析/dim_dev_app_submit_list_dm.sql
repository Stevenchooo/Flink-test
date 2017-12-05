# ----------------------------------------------------------------------------
#  file name:   dim_dev_app_submit_list_dm.sql
#  copyright(c)huawei technologies co.,ltd.1998-2011.all rights reserved.
#  purpose:
#  describe: 应用激活表
#  字段描述：
#         user_id                 开发者ID                         
#         imei                    设备号
#         app_id                  应用ID
#         app_name                应用名称
#         activate_date           激活时间
#         last_use_date           最后使用时间
#
#  input:  ods_dev_app_up_dm ,dim_hispace_app_info_ds ,dw_hispace_usage_fact_dm ,dw_emui_system_app_info_dm
#  output: dim_dev_app_submit_list_dm
#  author:  panjiankang/p84035806
#  creation date:  2015-03-24
#  Last Modified:  2015-03-30
#  Last Modified By: panjiankang p84035806
# ----------------------------------------------------------------------------

hive -e"
set hive.exec.compress.output=true;
set mapred.output.compression.codec=com.hadoop.compression.lzo.LzopCodec;
set io.compression.codecs = org.apache.hadoop.io.compress.GzipCodec,org.apache.hadoop.io.compress.DefaultCodec,com.hadoop.compression.lzo.LzoCodec,com.hadoop.compression.lzo.LzopCodec,org.apache.hadoop.io.compress.BZip2Codec;

CREATE TABLE IF NOT EXISTS tmp1_emui_hibi_app_usage_dm
(
    uuid                  STRING,
    imei                  STRING,
    record_time           STRING,
    collect_date          STRING,
    package_name          STRING,
    cnt                   INT,
    times                 INT
)
PARTITIONED BY(pt_d STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE
LOCATION '$hadoopuser/data/DW/DEV/tmp1_emui_hibi_app_usage_dm';

CREATE TABLE IF NOT EXISTS tmp2_emui_hibi_app_usage_dm
(
    uuid                  STRING,
    imei                  STRING,
    record_time           STRING,
    content               STRING
)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS INPUTFORMAT 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'
          OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION '$hadoopuser/data/DW/DEV/tmp2_emui_hibi_app_usage_dm';

INSERT OVERWRITE TABLE tmp2_emui_hibi_app_usage_dm
SELECT uuid,
    imei,
    record_time,
    content
FROM dw_emui_system_app_info_dm
WHERE pt_d = '$date' AND info_id = '131077'
;
"&
hadoop jar $HIBI_PATH/mrlib/EUI_ParseAppUsage.jar $hadoopuser/data/DW/DEV/tmp2_emui_hibi_app_usage_dm $hadoopuser/data/DW/DEV/tmp1_emui_hibi_app_usage_dm/pt_d=$date &

hive -e "

add JAR /MFS/Share/BICommon/task/HiBI/UDF/lib/huawei_udf.jar;
create temporary function getHiCloudAppId  AS 'com.huawei.platform.bi.udf.service.hispace.GetHiCloudAppId';


ALTER TABLE tmp1_emui_hibi_app_usage_dm ADD IF NOT EXISTS PARTITION (pt_d='$date');
DROP TABLE tmp2_emui_hibi_app_usage_dm;

CREATE EXTERNAL TABLE IF NOT EXISTS dim_dev_app_submit_list_dm
(   
    imei                  STRING,
    app_id                STRING,
    app_name              STRING,
    activate_date         STRING,
    last_use_date         STRING,
    user_id               STRING
)
PARTITIONED BY(pt_d STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/DEV/dim_dev_app_submit_list_dm';

INSERT OVERWRITE TABLE dim_dev_app_submit_list_dm
PARTITION (pt_d = '$date')
SELECT 
    
    COALESCE(t1.imei,t2.imei)                    AS     imei,
    COALESCE(t1.app_id,t2.app_id)                AS     app_id,
    COALESCE(t1.app_name,t2.app_name)            AS     app_name,
    COALESCE(t2.activate_date,t1.activate_date)  AS     activate_date,
    COALESCE(t1.activate_date,t2.activate_date)  AS     last_use_date,
    COALESCE(t1.user_id,t2.user_id)              AS     user_id
FROM
( 
    SELECT 
        t11.user_id       AS user_id,
        t12.imei          AS imei,
        t11.app_id        AS app_id,
        MAX(t11.app_name) AS app_name,
        '$date_ep'        AS activate_date
    FROM
    (
        SELECT
            MAX(t111.user_id)     AS user_id,
            MAX(t111.org_app_id)  AS app_id,                                                                          
            MAX(t111.app_name)    AS app_name,
            t112.package          AS package
        FROM 
        (   
            SELECT 
                uid                       AS    user_id,
                MAX(app_id)               AS    org_app_id,
                getHiCloudAppId(app_id)   AS    app_id,
                MAX(ch_name)              AS    app_name
            FROM ods_dev_app_up_dm
            WHERE pt_d = '$date' 
              AND rootid = 0 
              AND substr(last_uptime,1,10)<='$date_ep'
              AND state<>9997 
            GROUP BY getHiCloudAppId(app_id),
                     uid
        )t111
        LEFT OUTER JOIN
        (   
            SELECT 
                package,
                IF(substr(dev_app_id,1,1)='S',substr(dev_app_id,2),dev_app_id)      AS  dev_app_id
            FROM dim_hispace_app_info_ds
            GROUP BY  package,
                      IF(substr(dev_app_id,1,1)='S',substr(dev_app_id,2),dev_app_id)
        )t112
        ON t111.app_id = t112.dev_app_id
        GROUP BY t112.package
    )t11
    LEFT OUTER JOIN
    (
        SELECT          
            imei,          
            package_name
        FROM tmp1_emui_hibi_app_usage_dm
        WHERE pt_d = '$date'
    )t12
    ON t11.package = t12.package_name 
    WHERE t12.imei IS NOT NULL    
    GROUP BY t12.imei,t11.app_id,t11.user_id
)t1
FULL OUTER JOIN
(
    SELECT
        user_id,
        imei,
        app_id,
        app_name,
        activate_date
    FROM dim_dev_app_submit_list_dm
    WHERE pt_d = '$last_date'
)t2
ON (t1.imei = t2.imei AND t1.app_id = t2.app_id);
"
    
