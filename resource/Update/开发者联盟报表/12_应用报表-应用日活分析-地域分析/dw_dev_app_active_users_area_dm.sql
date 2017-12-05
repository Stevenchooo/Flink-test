# -----------------------------------------------------------------------------------------------------
#  File Name: dw_dev_app_active_users_area_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2012.All rights reserved.
#  Purpose: 应用日活总量按地域分析
#  Describe: 开发者社区的统计应用日活总量按地域分析的更新数据
#  字段描述：
#           user_id                         开发者id  
#           app_id                          应用编号 
#           app_name                        应用名称
#           area_id                         地区编号
#           area_name                       地区名称
#           period                          机器使用时间 
#           active_num                      应用日活数量
#           device_num                      机器数量
#  Input： dim_dev_app_submit_list_dm,dw_emui_rom_info_dm,dim_location_cn_ds,DIM_HOTA_USA_EUR_CHINA_HK_DS
#  Output： dw_dev_app_active_users_area_dm
#  Author:  panjiankang/p84035806
#  Creation Date:  2015-04-20
# --------------------------------------------------------------------------------------------------------
hive -e"
ADD jar $HIBI_PATH/udflib/huawei_udf.jar;
CREATE TEMPORARY FUNCTION areaID_UDF  as 'com.huawei.udf.GetAreaIDUDF';
CREATE EXTERNAL TABLE IF NOT EXISTS dw_dev_app_active_users_area_dm
(  
    user_id                         STRING,
    app_id                          STRING,
    app_name                        STRING,
    area_id                         BIGINT,
    area_name                       STRING,
    period                          STRING,
    active_num                      INT,
    device_num                      INT
)
PARTITIONED BY(pt_d STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/DEV/dw_dev_app_active_users_area_dm';

INSERT OVERWRITE TABLE dw_dev_app_active_users_area_dm
PARTITION (pt_d='$date')
SELECT 
    t1.user_id    AS user_id,
    t1.app_id     AS app_id,
    t1.app_name   AS app_name,
    t1.area_id    AS area_id,
    t1.area_name  AS area_name,
    t1.period     AS period,
    t1.active_num AS active_num,
    t2.device_num AS device_num
FROM
(
    SELECT
        MAX(t11.user_id)   AS user_id,
        t11.app_id         AS app_id,
        MAX(t11.app_name)  AS app_name,
        t12.area_id        AS area_id,
        MAX(t13.area_name) AS area_name,
        t14.period         AS period,
        COUNT(t11.imei)    AS active_num
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
    )t11
    LEFT OUTER JOIN
    (
        SELECT 
            imei,
            areaID_UDF(ip,1) AS area_id
        FROM dw_emui_rom_info_dm
        WHERE pt_d = '$date'
            AND ip IS NOT NULL 
            AND ip <> ''
    )t12
    ON UPPER(t11.imei) = UPPER(t12.imei)
    LEFT OUTER JOIN
    (
        SELECT 
            area_id,
            area_name
        FROM dim_location_cn_ds
        WHERE area_name IS NOT NULL 
            AND area_name <> ''
    )t13
    ON t12.area_id = t13.area_id
    LEFT OUTER JOIN
    (
        SELECT 
            decrypt_imei AS imei,
            CASE
                WHEN datediff('$date_ep', to_date(create_time)) <= 30 THEN '30'
                WHEN 30 <  datediff('$date_ep', to_date(create_time)) <= 90  THEN '90'
                WHEN 90 <  datediff('$date_ep', to_date(create_time)) <= 180 THEN '180'
                WHEN 180 < datediff('$date_ep', to_date(create_time)) <= 365 THEN '365'
                WHEN 365 < datediff('$date_ep', to_date(create_time)) <= 730 THEN '730'
                ELSE '9999'
            END          AS period
        FROM DIM_HOTA_USA_EUR_CHINA_HK_DS 
    )t14
    ON UPPER(t14.imei) = UPPER(t11.imei)
    WHERE t11.app_id <> '' AND t11.app_id IS NOT NULL
    GROUP BY t11.app_id,t12.area_id,t14.period
)t1
LEFT OUTER JOIN
(
    SELECT
        t22.area_id        AS area_id,
        t24.period         AS period,
        COUNT(t21.imei)    AS device_num
    FROM 
    (
        SELECT DISTINCT imei AS imei
        FROM dim_dev_app_submit_list_dm
        WHERE pt_d = '$date'
    )t21
    LEFT OUTER JOIN
    (
        SELECT 
            imei,
            areaID_UDF(ip,1) AS area_id
        FROM dw_emui_rom_info_dm
        WHERE pt_d = '$date'
            AND ip IS NOT NULL 
            AND ip <> ''
    )t22
    ON UPPER(t21.imei) = UPPER(t22.imei)
    LEFT OUTER JOIN
    (
        SELECT 
            area_id,
            area_name
        FROM dim_location_cn_ds
        WHERE area_name IS NOT NULL 
            AND area_name <> ''
    )t23
    ON t22.area_id = t23.area_id
    LEFT OUTER JOIN
    (
        SELECT 
            decrypt_imei AS imei,
            CASE
                WHEN datediff('$date_ep', to_date(create_time)) <= 30 THEN '30'
                WHEN 30 <  datediff('$date_ep', to_date(create_time)) <= 90  THEN '90'
                WHEN 90 <  datediff('$date_ep', to_date(create_time)) <= 180 THEN '180'
                WHEN 180 < datediff('$date_ep', to_date(create_time)) <= 365 THEN '365'
                WHEN 365 < datediff('$date_ep', to_date(create_time)) <= 730 THEN '730'
                ELSE '9999'
            END          AS period
        FROM DIM_HOTA_USA_EUR_CHINA_HK_DS 
    )t24
    ON UPPER(t24.imei) = UPPER(t21.imei)
    GROUP BY t22.area_id,t24.period
)t2
ON (t1.area_id = t2.area_id AND t1.period = t2.period);
" 