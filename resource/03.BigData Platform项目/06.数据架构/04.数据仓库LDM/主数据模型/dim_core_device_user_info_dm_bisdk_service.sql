# ----------------------------------------------------------------------------
#  File Name:   dim_core_device_user_info_dm_bisdk_service.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: BISDK各业务上报打点数据
#  Describe: BISDK各业务上报打点数据（合法IMEI设备）插入各业务分区的bisdk渠道分区
#  Input：  t_appa_visit_dm,t_appa_event_dm
#  Output： dim_core_device_user_info_dm
#  Author:  heyuanhong/h00201904
#  Creation Date:  2016-06-06
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
use biwarehouse;
set hive.exec.dynamic.partition=true;  
set hive.exec.dynamic.partition.mode=nonstrict;

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
PARTITION(pt_d, pt_service, pt_source)
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
    NULL                                                                                  AS app_ver,
    t1.create_time                                                                        AS create_time,
    t1.last_oper_time                                                                     AS last_oper_time,
    IF(!IsEmpty(t4.imei), 1, 0)                                                           AS huawei_flag,
    '$date'                                                                               AS pt_d,
    pt_service                                                                            AS pt_service,
    'bisdk'                                                                               AS pt_source
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
        MAX(last_oper_time)                                                         AS last_oper_time,
        pt_service                                                                  AS pt_service
    FROM
    (
        SELECT 
            t111.imei                                                                               AS imei             ,
            NULL                                                                                    AS did              ,
            NULL                                                                                    AS mac              ,
            NULL                                                                                    AS sn               ,
            IF(REGEXP(t111.device_name, '^[0-9a-zA-Z \\\\-\\\\._\\\\+]+$'), t111.device_name, NULL) AS device_name      ,
            NULL                                                                                    AS device_color     ,
            NULL                                                                                    AS ram_size         ,
            NULL                                                                                    AS rom_size         ,
            NULL                                                                                    AS first_eui_ver    ,
            NULL                                                                                    AS last_eui_ver     ,
            t111.rom_ver                                                                            AS first_rom_ver    ,
            t111.rom_ver                                                                            AS last_rom_ver     ,
            t111.android_ver                                                                        AS android_ver      ,
            NULL                                                                                    AS os_language      ,
            t111.plmn                                                                               AS first_plmn       ,
            t111.plmn                                                                               AS last_plmn        ,
            t111.ip                                                                                 AS first_ip         ,
            t111.ip                                                                                 AS last_ip          ,
            CONCAT(t111.province, '#')                                                              AS first_area       ,
            CONCAT(t111.province, '#')                                                              AS last_area        ,
            t111.app_ver                                                                            AS app_ver          ,
            t111.create_time                                                                        AS create_time      ,
            t111.create_time                                                                        AS last_oper_time   ,
            NULL                                                                                    AS huawei_flag      ,
            t112.service_id                                                                         AS pt_service
        FROM
        (
            SELECT app_package_name,
                RevertDeviceId(device_id)                                                                   AS imei,
                IF(IsEmptyUDF(TerminalFormateUDF(terminal_type)), NULL, TerminalFormateUDF(terminal_type))  AS device_name,
                app_ver                                                                                     AS app_ver,
                IF(UPPER(trim(rom_ver)) = 'ROM', NULL, UPPER(trim(rom_ver)))                                AS rom_ver,
                CASE
                WHEN instr(trim(net_provider_name),'460')=1 and length(trim(net_provider_name))>=5 then substr(trim(net_provider_name),1,5)
                WHEN instr(trim(net_provider_name),'中国移动')=1 then '46000' 
                WHEN instr(trim(net_provider_name),'中國移動')=1 then '46000'
                WHEN instr(trim(net_provider_name),'中国联通')=1 then '46001' 
                WHEN instr(trim(net_provider_name),'中國聯通')=1 then '46001'
                WHEN instr(trim(net_provider_name),'中國電信')=1 then '46003' 
                WHEN instr(trim(net_provider_name),'中国电信')=1 then '46003'
                ELSE NULL END                                                                                         AS plmn, 
                REGEXP_EXTRACT(terminal_os,'(\\\\d+\\\\.\\\\d+)',1)                                                   AS android_ver,
                IF(REGEXP(ip_address, '^\\\\d{1,3}\\\\.\\\\d{1,3}\\\\.\\\\d{1,3}\\\\.\\\\d{1,3}$'), ip_address, NULL) AS ip,
                regexp_replace(GetChinaProvinceName(ip_address),'市|省|回族|壮族|维吾尔|自治区|特别行政区','')        AS province,
                dateUtil(CAST(server_time AS BIGINT), 1, 'yyyy-MM-dd HH:mm:ss')                                       AS create_time
            FROM t_appa_visit_dm
            WHERE pt_d='$date' 
                AND IsDeviceIdLegal(device_id) 
                AND LENGTH (device_id) < 17
            UNION ALL
            SELECT 'com.huawei.android.pushagent' AS app_package_name,
                RevertDeviceId(device_id)                                                               AS imei,
                NULL                                                                                    AS device_name,
                app_ver                                                                                 AS app_ver,
                NULL                                                                                    AS rom_ver,
                NULL                                                                                    AS plmn,
                NULL                                                                                    AS android_ver,
                IF(REGEXP(ip_address, '^\\\\d{1,3}\\\\.\\\\d{1,3}\\\\.\\\\d{1,3}\\\\.\\\\d{1,3}$'), ip_address, NULL) AS ip,
                regexp_replace(GetChinaProvinceName(ip_address),'市|省|回族|壮族|维吾尔|自治区|特别行政区','')     AS province,
                dateUtil(CAST(server_time AS BIGINT), 1, 'yyyy-MM-dd HH:mm:ss')                         AS create_time
            FROM t_appa_event_dm
            WHERE pt_d = '$date' AND event_key = 'PUSH_PS'
                AND IsDeviceIdLegal(device_id) AND LENGTH(device_id) < 17
        ) t111
        JOIN dim_core_pkg2service2up_ds t112
        ON t111.app_package_name = t112.pkg_name
        WHERE to_date(t111.create_time) IS NOT NULL
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
            huawei_flag,
            pt_service
        FROM dim_core_device_user_info_dm
        WHERE pt_d = '$last_date' AND pt_source = 'bisdk'
    ) t11
    GROUP BY pt_service,imei
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
