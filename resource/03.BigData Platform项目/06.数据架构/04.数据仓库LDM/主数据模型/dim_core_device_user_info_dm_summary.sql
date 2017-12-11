# ----------------------------------------------------------------------------
#  File Name:   dim_core_device_user_info_dm_summary.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: 用户设备信息
#  Describe: 用户全量设备信息（合法IMEI设备：包括HOTA、大数据采集、BISDK、智汇云、游戏、UP、PUSH、主题BISDK）
#  Input：  dim_core_device_user_info_dm，dim_kpi_s_h_info_ds，dw_psi_one_imei_detail_dm
#  Output： dim_core_device_user_info_dm
#  Author:  heyuanhong/h00201904
#  Creation Date:  2016-06-06
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
use biwarehouse;
set hive.auto.convert.join=false;
set hive.auto.convert.join.noconditionaltask=false;

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
PARTITIONED BY (pt_d STRING,pt_service STRING,pt_source STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/hadoop-NJ/data/common/DIM/dim_core_device_user_info_dm'
TBLPROPERTIES('orc.compress'='ZLIB');

INSERT OVERWRITE TABLE dim_core_device_user_info_dm
PARTITION(pt_d='$date', pt_service='summary', pt_source='summary')
SELECT
    p1.imei,
    p1.did,
    p1.mac,
    p1.sn,
    p1.device_name,
    p1.device_color,
    p1.ram_size,
    p1.rom_size,
    COALESCE(p1.first_eui_ver, p2.emui) AS first_eui_ver,
    COALESCE(p1.last_eui_ver, p2.emui)  AS last_eui_ver,
    p1.first_rom_ver,
    p1.last_rom_ver,
    p1.android_ver,
    p1.os_language,
    p1.first_plmn,
    p1.last_plmn,
    p1.first_ip,
    p1.last_ip,
    SPLIT(p1.first_area, '#')[0]       AS first_province,
    SPLIT(p1.first_area, '#')[1]       AS first_city,
    p1.resident_province,
    p1.resident_city,
    SPLIT(p1.last_area, '#')[0]        AS last_province, 
    SPLIT(p1.last_area, '#')[1]        AS last_city,
    NULL AS app_ver,
    p1.create_time,
    p1.last_oper_time,
    p1.huawei_flag
FROM
(
    SELECT 
        trim(upper(software_version))                               AS rom_ver,    
        TerminalFormateUDF(hardware_version)                        AS device_name,
        max(REGEXP_EXTRACT(emui_version,'(\\\\d+\\\\.\\\\d+)',1))   AS emui
    FROM dim_kpi_s_h_info_ds 
    WHERE !IsEmptyUDF(REGEXP_EXTRACT(emui_version,'(\\\\d+\\\\.\\\\d+)',1)) 
        AND REGEXP(hardware_version, '^[0-9a-zA-Z \\\\-\\\\._\\\\+]+$')
        AND !IsEmptyUDF(trim(upper(software_version)))
    GROUP BY trim(upper(software_version)), TerminalFormateUDF(hardware_version)
)p2
RIGHT OUTER JOIN
(
    SELECT
        t1.imei,
        t2.did                                                                                                              AS did,
        COALESCE(t3.mac, t1.mac)                                                                                            AS mac,
        COALESCE(t3.sn, t1.sn)                                                                                              AS sn,
        COALESCE(t3.device_name,
                t1.device_name['hota'],
                t1.device_name['bdreporter'],
                t1.device_name['bisdk'],
                t1.device_name['push'],
                t1.device_name['hispace'],
                t1.device_name['up'],
                t1.device_name['hitop'],
                t1.device_name['game'])                                                                                     AS device_name,
        COALESCE(t3.device_color, t1.device_color)                                                                          AS device_color,
        COALESCE(t3.ram_size, t1.ram_size)                                                                                  AS ram_size,
        COALESCE(t3.rom_size, t1.rom_size)                                                                                  AS rom_size,
        COALESCE(t3.first_eui_ver, t1.first_eui_ver)                                                                        AS first_eui_ver,
        COALESCE(t1.last_eui_ver, t3.last_eui_ver)                                                                          AS last_eui_ver,
        COALESCE(t3.first_rom_ver, t1.first_rom_ver)                                                                        AS first_rom_ver,
        COALESCE(
                t1.last_rom_ver['hota'],
                t1.last_rom_ver['bdreporter'],
                t1.last_rom_ver['bisdk'],
                t1.last_rom_ver['phoneservice'],
                t3.last_rom_ver)                                                                                            AS last_rom_ver,
        COALESCE(t1.android_ver, t3.android_ver)                                                                            AS android_ver,
        COALESCE(t1.os_language, t3.os_language)                                                                            AS os_language,
        COALESCE(t3.first_plmn,
                t1.first_plmn['hota'],
                t1.first_plmn['bdreporter'],
                t1.first_plmn['push'],
                t1.first_plmn['bisdk'],
                t1.first_plmn['hitop'],
                t1.first_plmn['up'],
                t1.first_plmn['hispace'],
                t1.first_plmn['game'])                                                                                      AS first_plmn,
        COALESCE(
                t1.last_plmn['hota'],
                t1.last_plmn['bdreporter'],
                t1.last_plmn['push'],
                t1.last_plmn['bisdk'],
                t1.last_plmn['hitop'],
                t1.last_plmn['up'],
                t1.last_plmn['hispace'],
                t1.last_plmn['game'],
                t3.last_plmn)                                                                                               AS last_plmn,
        COALESCE(t3.first_ip, t1.first_ip['hota'], t1.first_ip['bdreporter'], t1.first_ip['bisdk'])                         AS first_ip,
        COALESCE(t1.last_ip['hota'], t1.last_ip['bdreporter'], t1.last_ip['bisdk'], t3.last_ip)                             AS last_ip,
        COALESCE(IF(t3.imei IS NULL, CONCAT(t5.gps[1], '#', t5.gps[2]), NULL),
            t3.first_area, 
            t1.first_area['hota'], 
            t1.first_area['bdreporter'], 
            t1.first_area['bisdk'])                                                                                         AS first_area,
        COALESCE(t4.resident_province, 
            t5.gps[1], 
            split(t1.last_area['bdreporter'],'#')[0], 
            split(t1.last_area['bisdk'],'#')[0], 
            split(t1.last_area['hota'],'#')[0])                                                                             AS resident_province,
        COALESCE(t4.resident_city, t5.gps[2])                                                                                 AS resident_city,
        COALESCE(CONCAT(t5.gps[1], '#', t5.gps[2]),
            t1.last_area['hota'], 
            t1.last_area['bdreporter'], 
            t1.last_area['bisdk'], 
            t3.last_area)                                                                                                   AS last_area,
        COALESCE(t3.create_time, t1.create_time)                                                                            AS create_time,
        COALESCE(t1.last_oper_time, t3.last_oper_time)                                                                      AS last_oper_time,
        IF(IsEmpty(t2.imei), 0, 1)                                                                                          AS huawei_flag
    FROM
    (
        SELECT
            imei,
            MAX(mac)                                                                                                        AS mac,
            MAX(sn)                                                                                                         AS sn,
            IF(IsEmptyUDF(FI1_groupconcat(device_name,'&')), NULL, str_to_map(FI1_groupconcat(device_name,'&'),'&','='))    AS device_name,
            MAX(device_color)                                                                                               AS device_color,
            MAX(ram_size)                                                                                                   AS ram_size,
            MAX(rom_size)                                                                                                   AS rom_size,
            SPLIT(MIN(CONCAT(create_time, '\001', first_eui_ver)), '\001')[1]                                               AS first_eui_ver,
            MAX(last_eui_ver)                                                                                               AS last_eui_ver,
            SPLIT(MIN(CONCAT(create_time, '\001', first_rom_ver)), '\001')[1]                                               AS first_rom_ver,
            IF(IsEmptyUDF(FI1_groupconcat(last_rom_ver,'&')), NULL, str_to_map(FI1_groupconcat(last_rom_ver,'&'),'&','='))  AS last_rom_ver,
            MAX(android_ver)                                                                                                AS android_ver,
            MAX(os_language)                                                                                                AS os_language,
            IF(IsEmptyUDF(FI1_groupconcat(first_plmn,'&')), NULL, str_to_map(FI1_groupconcat(first_plmn,'&'),'&','='))      AS first_plmn,
            IF(IsEmptyUDF(FI1_groupconcat(last_plmn,'&')), NULL, str_to_map(FI1_groupconcat(last_plmn,'&'),'&','='))        AS last_plmn,
            IF(IsEmptyUDF(FI1_groupconcat(first_ip,'&')), NULL, str_to_map(FI1_groupconcat(first_ip,'&'),'&','='))          AS first_ip,
            IF(IsEmptyUDF(FI1_groupconcat(last_ip,'&')), NULL, str_to_map(FI1_groupconcat(last_ip,'&'),'&','='))            AS last_ip,
            IF(IsEmptyUDF(FI1_groupconcat(first_area,'&')), NULL, str_to_map(FI1_groupconcat(first_area,'&'),'&','='))      AS first_area,
            IF(IsEmptyUDF(FI1_groupconcat(last_area,'&')), NULL, str_to_map(FI1_groupconcat(last_area,'&'),'&','='))        AS last_area,
            MIN(create_time)                                                                                                AS create_time,
            MAX(last_oper_time)                                                                                             AS last_oper_time
        FROM
        (
            SELECT
                imei,
                IF(IsEmptyUDF(mac), NULL, mac)                                                                                                AS mac,
                IF(IsEmptyUDF(sn), NULL, sn)                                                                                                  AS sn,
                CONCAT(pt_service,'=', IF(IsEmptyUDF(TerminalFormateUDF(device_name)), NULL, TerminalFormateUDF(device_name)))                AS device_name,
                IF(IsEmptyUDF(device_color), NULL, device_color)                                                                              AS device_color,
                IF(IsEmptyUDF(ram_size), NULL, ram_size)                                                                                      AS ram_size,
                IF(IsEmptyUDF(rom_size), NULL, rom_size)                                                                                      AS rom_size,
                IF(IsEmptyUDF(first_eui_ver), NULL, first_eui_ver)                                                                            AS first_eui_ver,
                IF(IsEmptyUDF(last_eui_ver), NULL, last_eui_ver)                                                                              AS last_eui_ver,
                IF(!IsEmptyUDF(first_rom_ver), first_rom_ver, NULL)                                                                           AS first_rom_ver,
                CONCAT(pt_service,'=',IF(!IsEmptyUDF(last_rom_ver), last_rom_ver, NULL))                                                      AS last_rom_ver,
                IF(IsEmptyUDF(android_ver), NULL, android_ver)                                                                                AS android_ver,
                IF(IsEmptyUDF(os_language), NULL, os_language)                                                                                AS os_language,
                CONCAT(pt_service,'=',IF(IsEmptyUDF(first_plmn), NULL, first_plmn))                                                           AS first_plmn,
                CONCAT(pt_service,'=',IF(IsEmptyUDF(last_plmn), NULL, last_plmn))                                                             AS last_plmn,
                CONCAT(pt_service,'=',IF(IsEmptyUDF(first_ip), NULL, first_ip))                                                               AS first_ip,
                CONCAT(pt_service,'=',IF(IsEmptyUDF(last_ip), NULL, last_ip))                                                                 AS last_ip,
                CONCAT(pt_service,'=',IF(IsEmptyUDF(CONCAT(first_province, '#', first_city)), NULL, CONCAT(first_province, '#', first_city))) AS first_area,
                CONCAT(pt_service,'=',IF(IsEmptyUDF(CONCAT(last_province, '#', last_city)), NULL, CONCAT(last_province, '#', last_city)))     AS last_area,
                create_time                                                                                                                   AS create_time,
                last_oper_time                                                                                                                AS last_oper_time
            FROM dim_core_device_user_info_dm
            WHERE pt_d = '$date' AND pt_service <> 'summary'
        ) t11
        GROUP BY imei
    )t1
    LEFT OUTER JOIN
    (
        SELECT *
        FROM dim_core_psi_one_imei_detail_dm 
        WHERE pt_d='$date' AND IsDeviceIdLegal(imei) AND LENGTH(imei) < 17
    )t2
    ON t1.imei=RevertDeviceId(t2.imei)
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
            create_time      ,
            last_oper_time   ,
            huawei_flag
        FROM dim_core_device_user_info_dm
        WHERE pt_d = '$last_date' AND pt_service = 'summary' AND pt_source = 'summary'
    ) t3
    ON t1.imei=t3.imei
    LEFT OUTER JOIN dim_core_device_user_resident_info_ds t4
    ON t1.imei = t4.imei
    LEFT OUTER JOIN
    (
        SELECT imei,
            SPLIT(MAX(CONCAT(upload_time, '\001', gps_province, '\001', gps_city)), '\001') AS gps
        FROM dw_core_sdk_weather_gps_hm
        WHERE pt_d = '$date'
        GROUP BY imei
    ) t5
    ON t1.imei = t5.imei

)p1
ON p1.last_rom_ver=p2.rom_ver and IF(IsEmpty(p1.device_name), CONCAT('hive', rand()), p1.device_name)=p2.device_name;
"
