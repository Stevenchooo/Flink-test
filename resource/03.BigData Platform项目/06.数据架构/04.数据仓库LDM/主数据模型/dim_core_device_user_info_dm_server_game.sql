# ----------------------------------------------------------------------------
#  File Name:   dim_core_device_user_info_dm_hispace.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: 游戏中心用户设备信息
#  Describe: 游戏中心用户全量设备信息（合法IMEI设备）
#  Input：  dw_hispace_game_oper_log_dm
#  Output： dim_core_device_user_info_dm
#  Author:  heyuanhong/h00201904
#  Creation Date:  2016-06-06
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
use biwarehouse;

CREATE EXTERNAL TABLE IF NOT EXISTS dim_core_device_user_info_dm
(
    imei              STRING COMMENT '设备IMEI/MEID',
    did               STRING COMMENT '根据PSI数据生成的设备唯一标识',
    mac               STRING COMMENT '小写的MAC:f4:9f:f3:94:25:ea',
    sn                STRING COMMENT '设备SN码',
    device_name       STRING COMMENT '设备外部名称，传播名称',
    device_color      STRING COMMENT '设备颜色',
    ram_size          STRING COMMENT 'RAM大小',
    rom_size          STRING COMMENT 'ROM大小',
    first_eui_ver     STRING COMMENT '首次激活EMUI版本:3.0',
    last_eui_ver      STRING COMMENT '最后使用EMUI版本:3.0',
    first_rom_ver     STRING COMMENT '首次激活设备ROM版本',
    last_rom_ver      STRING COMMENT '最后使用设备ROM版本',
    android_ver       STRING COMMENT '安卓版本:5.1',
    os_language       STRING COMMENT '操作系统语言',
    first_plmn        STRING COMMENT '首次激活PLMN:46002',
    last_plmn         STRING COMMENT '最后使用PLMN:46002',
    first_ip          STRING COMMENT '首次激活IP',
    last_ip           STRING COMMENT '最后使用IP',
    first_province    STRING COMMENT '首次激活时所在省份',
    first_city        STRING COMMENT '首次激活时所在城市',
    resident_province STRING COMMENT '最近30常驻省份',
    resident_city     STRING COMMENT '最近30常驻城市',
    last_province     STRING COMMENT '最后使用设备时所在省份',
    last_city         STRING COMMENT '最后使用设备时所在城市',
    app_ver           STRING COMMENT '应用版本号',
    create_time       STRING COMMENT '首次激活时间',
    last_oper_time    STRING COMMENT '最后使用时间',
    huawei_flag       INT    COMMENT '是否华为机型，0：非华为，1：IMEI匹配华为'
)
PARTITIONED BY (pt_d STRING,pt_service STRING, pt_source STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/hadoop-NJ/data/common/DIM/dim_core_device_user_info_dm'
TBLPROPERTIES('orc.compress'='ZLIB');


INSERT OVERWRITE TABLE dim_core_device_user_info_dm
PARTITION(pt_d='$date', pt_service='game', pt_source='server')
SELECT
    t1.imei,
    COALESCE(t4.did, t1.imei)                                                             AS did,
    COALESCE(t2.mac, t1.mac)                                                              AS mac,
    COALESCE(t2.sn, t1.sn)                                                                AS sn,
    COALESCE(t4.device_name, t2.device_name, t1.device_name)                              AS device_name,
    COALESCE(t4.device_color, t2.device_color, t1.device_color)                           AS device_color,
    COALESCE(t4.ram_size, t1.ram_size, t2.ram_size)                                       AS ram_size,
    COALESCE(t4.rom_size, t1.rom_size, t2.rom_size)                                       AS rom_size,
    t1.first_eui_ver                                                                      AS first_eui_ver,
    COALESCE(t1.last_eui_ver, t2.last_eui_ver)                                            AS last_eui_ver,
    t1.first_rom_ver                                                                      AS first_rom_ver,
    COALESCE(t1.last_rom_ver, t2.last_rom_ver)                                            AS last_rom_ver,
    COALESCE(IF(IsEmpty(t1.android_ver), NULL, t1.android_ver), t2.android_ver)           AS android_ver,
    COALESCE(t1.os_language, t2.os_language)                                              AS os_language,
    t1.first_plmn                                                                         AS first_plmn,
    COALESCE(t1.last_plmn, t2.last_plmn)                                                  AS last_plmn,
    t1.first_ip                                                                           AS first_ip,
    COALESCE(t1.last_ip, t2.last_ip)                                                      AS last_ip,
    IF(TO_DATE(t1.create_time) = '$date_ep', t3.gps[1], SPLIT(t1.first_area, '#')[0])     AS first_province,
    IF(TO_DATE(t1.create_time) = '$date_ep', t3.gps[2], SPLIT(t1.first_area, '#')[1])     AS first_city,
    COALESCE(t5.resident_province, t3.gps[1], SPLIT(t1.first_area, '#')[0])               AS resident_province,
    COALESCE(t5.resident_city,t3.gps[2], SPLIT(t1.first_area, '#')[1])                    AS resident_city,
    COALESCE(t3.gps[1], SPLIT(t1.last_area,'#')[0], SPLIT(t2.last_area,'#')[0])           AS last_province,
    COALESCE(t3.gps[2], SPLIT(t1.last_area,'#')[1], SPLIT(t2.last_area,'#')[1])           AS last_city,
    t1.app_ver                                                                            AS app_ver,
    t1.create_time                                                                        AS create_time,
    t1.last_oper_time                                                                     AS last_oper_time,
    IF(!IsEmpty(t4.imei), 1, 0)                                                           AS huawei_flag
FROM
(
    SELECT
        imei,
        MAX(did)                                                                    AS did,
        MAX(mac)                                                                    AS mac,
        MAX(sn)                                                                     AS sn,
        SPLIT(MIN(CONCAT(create_time, '\001', device_name)), '\001')[1]             AS device_name,
        SPLIT(MIN(CONCAT(create_time, '\001', device_color)), '\001')[1]            AS device_color,
        MAX(ram_size)                                                               AS ram_size,
        MAX(rom_size)                                                               AS rom_size,
        SPLIT(MIN(CONCAT(create_time, '\001', first_eui_ver)), '\001')[1]           AS first_eui_ver,
        SPLIT(MAX(CONCAT(last_oper_time, '\001', last_eui_ver)), '\001')[1]         AS last_eui_ver,
        SPLIT(MIN(CONCAT(create_time, '\001', first_rom_ver)), '\001')[1]           AS first_rom_ver,
        SPLIT(MAX(CONCAT(last_oper_time, '\001', last_rom_ver)), '\001')[1]         AS last_rom_ver,
        SPLIT(MAX(CONCAT(last_oper_time, '\001', android_ver)), '\001')[1]          AS android_ver,
        SPLIT(MAX(CONCAT(last_oper_time, '\001', os_language)), '\001')[1]          AS os_language,
        SPLIT(MIN(CONCAT(create_time, '\001', first_plmn)), '\001')[1]              AS first_plmn,
        SPLIT(MAX(CONCAT(last_oper_time, '\001', last_plmn)), '\001')[1]            AS last_plmn,
        SPLIT(MIN(CONCAT(create_time, '\001', first_ip)), '\001')[1]                AS first_ip,
        SPLIT(MAX(CONCAT(last_oper_time, '\001', last_ip)), '\001')[1]              AS last_ip,
        SPLIT(MIN(CONCAT(create_time, '\001', first_area)), '\001')[1]              AS first_area,
        SPLIT(MAX(CONCAT(last_oper_time, '\001', last_area)), '\001')[1]            AS last_area,
        SPLIT(MAX(CONCAT(last_oper_time, '\001', app_ver)), '\001')[1]              AS app_ver,
        MIN(create_time)                                                            AS create_time,
        MAX(last_oper_time)                                                         AS last_oper_time
    FROM
    (
        SELECT 
            imei                                                                          AS imei             ,
            NULL                                                                          AS did              ,
            NULL                                                                          AS mac              ,
            NULL                                                                          AS sn               ,
            NULL                                                                          AS device_name      ,
            NULL                                                                          AS device_color     ,
            NULL                                                                          AS ram_size         ,
            NULL                                                                          AS rom_size         ,
            NULL                                                                          AS first_eui_ver    ,
            NULL                                                                          AS last_eui_ver     ,
            NULL                                                                          AS first_rom_ver    ,
            NULL                                                                          AS last_rom_ver     ,
            REGEXP_EXTRACT(t13.describes,'(\\\\d+\\\\.\\\\d+)',1)                         AS android_ver      ,
            NULL                                                                          AS os_language      ,
            IF(t12.localvalue IN ('00','01','02','03'),CONCAT('460',t12.localvalue),NULL) AS first_plmn       ,
            IF(t12.localvalue IN ('00','01','02','03'),CONCAT('460',t12.localvalue),NULL) AS last_plmn        ,
            NULL                                                                          AS first_ip         ,
            NULL                                                                          AS last_ip          ,
            NULL                                                                          AS first_area       ,
            NULL                                                                          AS last_area        ,
            t14.service_client_version                                                    AS app_ver          ,
            create_time                                                                   AS create_time      ,
            create_time                                                                   AS last_oper_time   ,
            NULL                                                                          AS huawei_flag
        FROM
        (
            SELECT RevertDeviceId(SPLIT(logon_id,'@')[1])         AS imei,
                MAX(oper_time)                                    AS create_time,
                MAX(deviceId_UDF(logon_id,'@','sim_mobile_oper')) AS mnc_id,
                MAX(substr(logon_id,1,1))                         AS andriod_id,
                MAX(deviceId_UDF(logon_id,'@',2))                 AS service_client_code
            FROM ODS_HISPACE_OPER_LOG_DM
            WHERE pt_d = '$date' 
                AND (biz_channel_id = 4010002 or lower(expand) like '%servicetype:1%' or lower(expand) like '%gameflag:{1}%' or lower(expand) like '%servicetype:5%')
                AND IsDeviceIdLegal(SPLIT(logon_id,'@')[1]) AND LENGTH(SPLIT(logon_id,'@')[1]) < 17
                AND to_date(oper_time) IS NOT NULL
            GROUP BY RevertDeviceId(SPLIT(logon_id,'@')[1])
        ) t11
        LEFT OUTER JOIN 
        (
            SELECT * 
            FROM ods_hispace_terminal_code_dm
            WHERE pt_d = '$date' AND startindex = '11'
        )t12
        ON t11.mnc_id = t12.signvaluie
        LEFT OUTER JOIN 
        (
            SELECT * 
            FROM ods_hispace_terminal_code_dm
            WHERE pt_d = '$date' AND startindex = '0'
        )t13
        ON t11.andriod_id = t13.signvaluie
        LEFT OUTER JOIN dim_service_client_ds t14
        ON t11.service_client_code=t14.service_client_code

        UNION ALL
        
        SELECT 
            imei             ,
            did              ,
            mac              ,
            sn               ,
            device_name      ,
            device_color     ,
            ram_size         ,
            rom_size         ,
            first_eui_ver    ,
            last_eui_ver     ,
            first_rom_ver    ,
            last_rom_ver     ,
            android_ver      ,
            os_language      ,
            first_plmn       ,
            last_plmn        ,
            first_ip         ,
            last_ip          ,
            CONCAT(first_province, '#', COALESCE(first_city, '')) AS first_area,
            CONCAT(last_province, '#', COALESCE(last_city, ''))   AS last_area,
            app_ver                                               AS app_ver,
            COALESCE(create_time, '$last_date_ep')                AS create_time,
            COALESCE(last_oper_time, '$last_date_ep')             AS last_oper_time,
            huawei_flag
        FROM dim_core_device_user_info_dm
        WHERE pt_d = '$last_date' AND pt_service = 'game' AND pt_source = 'server'
    ) t11
    GROUP BY imei
) t1
LEFT OUTER JOIN
(
    SELECT 
        imei             ,
        did              ,
        mac              ,
        sn               ,
        device_name      ,
        device_color     ,
        ram_size         ,
        rom_size         ,
        last_eui_ver     ,
        last_rom_ver     ,
        android_ver      ,
        os_language      ,
        last_plmn        ,
        last_ip          ,
        CONCAT(last_province, '#', COALESCE(last_city, '')) AS last_area,
        create_time      ,
        last_oper_time   ,
        huawei_flag
    FROM dim_core_device_user_info_dm
    WHERE pt_d = '$last_date' AND pt_service = 'summary' AND pt_source = 'summary'
) t2
ON t1.imei = t2.imei
LEFT OUTER JOIN
(
    SELECT imei,
        SPLIT(MAX(CONCAT(upload_time, '\001', gps_province, '\001', gps_city)), '\001') AS gps
    FROM dw_core_sdk_weather_gps_hm
    WHERE pt_d = '$date'
    GROUP BY imei
) t3
ON t1.imei = t3.imei
LEFT OUTER JOIN 
(
    SELECT *
    FROM dim_core_psi_one_imei_detail_dm
    WHERE pt_d = '$date' AND IsDeviceIdLegal(imei) AND LENGTH(imei) < 17
) t4
ON t1.imei = RevertDeviceId(t4.imei)
LEFT OUTER JOIN dim_core_device_user_resident_info_ds t5
ON t1.imei = t5.imei;
"
