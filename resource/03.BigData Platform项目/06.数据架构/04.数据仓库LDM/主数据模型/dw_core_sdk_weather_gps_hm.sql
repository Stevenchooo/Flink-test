# ----------------------------------------------------------------------------
#  File Name:   dw_core_sdk_weather_gps_hm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: 天气采集经纬度信息小时表
#  Describe: 天气采集经纬度信息小时表
#  Input：  t_appa_event_hm,t_appa_visit_hm
#  Output： dw_core_sdk_weather_gps_hm
#  Author:  heyuanhong/h00201904
#  Creation Date:  2016-06-06
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
use biwarehouse;

CREATE EXTERNAL TABLE IF NOT EXISTS dw_core_sdk_weather_gps_hm
(
    imei         STRING COMMENT 'IMEI',
    device_name  STRING COMMENT '机型',
    longitude    STRING COMMENT '经度',
    latitude     STRING COMMENT '维度',
    upload_time  STRING COMMENT '上报时间', 
    ip           STRING COMMENT '客户端IP',
    gps_province STRING COMMENT '省份',
    gps_city     STRING COMMENT '城市'
)
COMMENT '天气采集经纬度信息小时表'
PARTITIONED BY(pt_d STRING COMMENT 'DATE_PARTITION (YYYYMMDD)', pt_h STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
STORED AS ORC
LOCATION '/hadoop-NJ/data/common/DIM/dw_core_sdk_weather_gps_hm'
TBLPROPERTIES('orc.compress'='ZLIB');


INSERT OVERWRITE TABLE dw_core_sdk_weather_gps_hm
PARTITION (pt_d = '$date', pt_h = '$hour')
SELECT t1.imei,
    t1.device_name, 
    t1.longitude,
    t1.latitude,
    t1.upload_time,
    t1.ip_address,
    regexp_replace(t2.province,'市|省|回族|壮族|维吾尔|自治区|特别行政区','') AS gps_province,
    t2.city                                                        AS gps_city
FROM
(
    SELECT 
        t11.device_id            AS imei,
        t12.terminal_type        AS device_name, 
        longitude                AS longitude,
        latitude                 AS latitude,
        t11.server_time          AS upload_time,
        t11.ip_address           AS ip_address
    FROM
    ( 
        SELECT 
            RevertDeviceId(device_id) AS device_id,
            visit_id,
            MAX(event_local_time) AS event_local_time, 
            MAX(IF(event_key = 'longitude',event_value,NULL)) AS longitude,
            MAX(IF(event_key = 'latitude' ,event_value,NULL)) AS latitude,
            MAX(DateUtil(CAST(server_time AS BIGINT), 1, 'yyyy-MM-dd HH:mm:ss')) AS server_time,
            MAX(ip_address) AS ip_address
        FROM t_appa_event_hm
        WHERE pt_d = '$date' AND pt_h = '$hour' 
            AND app_package_name = 'com.huawei.android.totemweather' 
            AND event_key IN ('longitude','latitude')
            AND IsDeviceIdLegal(device_id) AND LENGTH(device_id) < 17
        GROUP BY RevertDeviceId(device_id),visit_id

        UNION ALL

        SELECT 
            RevertDeviceId(device_id) AS device_id,
            visit_id,
            event_local_time, 
            split(split(event_value,'\\\\^')[2],':')[1] AS longitude,
            split(split(event_value,'\\\\^')[1],':')[1]  AS latitude  ,
            DateUtil(CAST(server_time AS BIGINT), 1, 'yyyy-MM-dd HH:mm:ss') AS server_time,
            ip_address
        FROM t_appa_event_hm 
        WHERE pt_d='$date' AND pt_h = '$hour' 
        AND app_package_name = 'com.huawei.android.totemweather' AND event_key='cityInfo'
        AND IsDeviceIdLegal(device_id) AND LENGTH(device_id) < 17
    )t11
    LEFT OUTER JOIN 
    (
        SELECT 
            RevertDeviceId(device_id) AS device_id,
            MAX(terminal_type) AS terminal_type
        FROM t_appa_visit_hm 
        WHERE pt_d = '$date' AND pt_h = '$hour' 
            AND app_package_name= 'com.huawei.android.totemweather'
            AND IsDeviceIdLegal(device_id) AND LENGTH(device_id) < 17
        GROUP BY RevertDeviceId(device_id)
    )t12
    ON t11.device_id=t12.device_id
)t1
LEFT OUTER JOIN dim_gps_database_ds t2
ON ROUND(t1.longitude,2) = t2.lng AND ROUND(t1.latitude,2)=t2.lat;
"
