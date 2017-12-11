# ----------------------------------------------------------------------------
#  File Name:   dim_core_psi_one_imei_detail_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: 帐号设备信息
#  Describe: 帐号全量设备信息（合法IMEI设备）
#  Input：  ODS_PSI_IMEI_LIST_DM_CRYPT,ODS_PSI_5_IMEI_DM,dim_important_device_baseinfo_new_ds

#  Output： dim_core_psi_one_imei_detail_dm
#  Author:  heyuanhong/h00201904
#  Creation Date:  2016-06-06
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
use biwarehouse;

CREATE EXTERNAL TABLE IF NOT EXISTS dim_core_psi_one_imei_detail_dm
(
    imei              STRING COMMENT '设备IMEI/MEID',
    did               STRING COMMENT '根据PSI数据生成的设备唯一标识',
    bom               STRING COMMENT 'BOM编码,统一设备多个BOM编码取最新的BOM编码',
    inside_name       STRING COMMENT '设备内部名称',
    product           STRING COMMENT '产品名称',
    device_name       STRING COMMENT '设备外部名称，传播名称',
    team_flag         STRING COMMENT '产品系列',
    device_color      STRING COMMENT '设备颜色',
    ram_size          STRING COMMENT 'RAM大小',
    rom_size          STRING COMMENT 'ROM大小',
    channel           STRING COMMENT '渠道：样机&备机，海外版本等',
    packing_list_no   STRING COMMENT '发货箱单号',
    customer_name     STRING COMMENT '发货客户',    
    arrival_country   STRING COMMENT '发货国家',
    arrival_city      STRING COMMENT '发货城市',
    packing_date      STRING COMMENT '发货日期',
    order_no          STRING COMMENT '订单号',
    sync_date         STRING COMMENT 'PSI首次同步时间'
)
PARTITIONED BY (pt_d STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/hadoop-NJ/data/common/DIM/dim_core_psi_one_imei_detail_dm'
TBLPROPERTIES('orc.compress'='ZLIB');


CREATE TABLE IF NOT EXISTS tmp_core_psi_device_info_1 
(
    did             STRING,
    imei            STRING,
    inside_name     STRING,
    bom             STRING,
    packing_list_no STRING,
    sync_date       STRING
)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
TBLPROPERTIES('orc.compress'='ZLIB');


#每日同步的IMEI行转列，一行多个IMEI转多行的IMEI，并且生成唯一的设备ID
INSERT OVERWRITE TABLE tmp_core_psi_device_info_1
SELECT did,
    RevertDeviceId(imei) AS imei,
    inside_name,
    bom,
    packing_list_no,
    sync_date
FROM
(
    SELECT
        CONCAT('1',did) AS did,
        item            AS imei,
        inside_name     AS inside_name,
        bom             AS bom,
        packing_list_no AS packing_list_no,
        sync_date       AS sync_date
    FROM
    (
        SELECT reflect('java.util.UUID','randomUUID')                           AS did,
            array(IF(isempty(imei1), null,imei1),IF(isempty(imei2),null,imei2)) AS ary,
            inside_name                                                         AS inside_name,
            inside_code                                                         AS bom,
            packing_list_no                                                     AS packing_list_no,
            '$date_ep'                                                          AS sync_date
        FROM 
        (
            SELECT IF(LENGTH(imei1) >= 32, aesDecrypt(imei1,'tcsm'), imei1) AS imei1,
                IF(LENGTH(imei2) >= 32, aesDecrypt(imei2,'tcsm'), imei2)    AS imei2,
                inside_name,
                inside_code,
                packing_list_no
            FROM ODS_PSI_IMEI_LIST_DM_CRYPT
            WHERE pt_d = '$date'
        ) t11
        UNION ALL
        SELECT reflect('java.util.UUID','randomUUID')                           AS did,
            array(IF(isempty(aesDecrypt(imei1,'tcsm')), null, aesDecrypt(imei1,'tcsm')),
                IF(isempty(aesDecrypt(imei2,'tcsm')),null, aesDecrypt(imei2,'tcsm')),
                IF(isempty(aesDecrypt(imei3,'tcsm')),null, aesDecrypt(imei3,'tcsm')),
                IF(isempty(aesDecrypt(imei4,'tcsm')),null, aesDecrypt(imei4,'tcsm')),
                IF(isempty(aesDecrypt(imei5,'tcsm')),null, aesDecrypt(imei5,'tcsm'))
            )                                                                   AS ary,
            inside_name                                                         AS inside_name,
            bom                                                                 AS bom,
            packing_list_no                                                     AS packing_list_no,
            '$date_ep'                                                          AS sync_date
        FROM ODS_PSI_5_IMEI_DM
        WHERE pt_d = '$date'
    ) t1 LATERAL VIEW explode(ary) arrayTable AS item
) t
where isempty(imei) = FALSE;

CREATE TABLE IF NOT EXISTS tmp_core_psi_device_info_2
(
    did             STRING,
    imei            STRING,
    inside_name     STRING,
    bom             STRING,
    packing_list_no STRING,
    sync_date       STRING
)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
TBLPROPERTIES('orc.compress'='ZLIB');


#PSI历史同步的IMEI合并，
INSERT OVERWRITE TABLE tmp_core_psi_device_info_2
SELECT did,
    imei,
    inside_name,
    bom,
    packing_list_no,
    sync_date
FROM
(
    SELECT did,
        imei,
        inside_name,
        bom,
        packing_list_no,
        sync_date
    FROM tmp_core_psi_device_info_1
    UNION ALL
    SELECT CONCAT('0',did) AS did,
        RevertDeviceId(imei) AS imei,
        inside_name,
        bom,
        packing_list_no,
        sync_date
    FROM dim_core_psi_one_imei_detail_dm
    WHERE pt_d = '$last_date'
) t;


CREATE TABLE IF NOT EXISTS tmp_core_psi_device_info_3
(
    min_did         STRING,
    imei            STRING,
    inside_name     STRING,
    bom             STRING
)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
TBLPROPERTIES('orc.compress'='ZLIB');

#按IMEI分组，取IMEI对应最小did
INSERT OVERWRITE TABLE tmp_core_psi_device_info_3
SELECT 
    MIN(did)                                                 AS min_did,
    imei                                                     AS imei,
    MAX(inside_name)                                         AS inside_name,
    SPLIT(MAX(CONCAT(did,'\001',bom)),'\001')[1]             AS bom
FROM tmp_core_psi_device_info_2
GROUP BY imei;

#1、关联表2和表3，以表的的did分组，归一did对应的所有imei号的最小did值，以此值为设备ID（did）
#2、分解imei后去重，跟发货信息匹配获取发货信息
INSERT OVERWRITE TABLE dim_core_psi_one_imei_detail_dm
PARTITION(pt_d='$date')
SELECT
    t1.imei,
    SUBSTR(t1.did, 2)                   AS did,
    t1.bom,
    t1.inside_name,
    t2.productname                      AS product,
    IF(TerminalFormateUDF(t2.device_name) = '未知', NULL, TerminalFormateUDF(t2.device_name)) AS device_name,
    CASE t2.series
        WHEN '荣耀'  THEN 'HONOR'
        WHEN '畅玩'  THEN 'HONOR'
        WHEN 'G系列' THEN 'GY'
        WHEN '其他'  THEN 'OTHER'
        WHEN '平板'  THEN 'PAD'
        WHEN '畅享'  THEN 'GY'
    ELSE series END                     AS team_flag,
    t2.color                            AS device_color,
    IF(REGEXP(SPLIT(t2.config, '\\\\+')[0], '^[0-9GMBgbm]+$'), SPLIT(t2.config, '\\\\+')[0], NULL) AS ram_size,
    IF(REGEXP(SPLIT(t2.config, '\\\\+')[1], '^[0-9GMBgbm]+$'), SPLIT(t2.config, '\\\\+')[1], NULL) AS rom_size,
    t2.channel                          AS channel,
    t3.packing_list_no                  AS packing_list_no,
    t3.customer_name                    AS customer_name,  
    t3.arrival_country                  AS arrival_country,
    t3.arrival_city                     AS arrival_city,
    t3.packing_date                     AS packing_date,
    t3.order_no                         AS order_no,
    t1.sync_date                        AS sync_date
FROM
(
    SELECT imei,
        MIN(did)             AS did,
        MAX(inside_name)     AS inside_name,
        MAX(bom)             AS bom,
        MAX(packing_list_no) AS packing_list_no,
        MIN(sync_date)       AS sync_date
    FROM
    (
        SELECT
            min_did         AS did,
            item            AS imei,
            inside_name     AS inside_name,
            bom             AS bom,
            packing_list_no AS packing_list_no,
            sync_date       AS sync_date
        FROM
        (
            SELECT 
                did,
                collect_set(imei)                                        AS imei_set,
                MAX(inside_name)                                         AS inside_name,
                SPLIT(MAX(CONCAT(did,'\001',bom)),'\001')[1]             AS bom,
                SPLIT(MAX(CONCAT(did,'\001',packing_list_no)),'\001')[1] AS packing_list_no,
                MIN(min_did)                                             AS min_did,
                MIN(sync_date)                                           AS sync_date
            FROM
            (
                SELECT 
                    t1111.did,
                    t1111.imei,
                    t1111.inside_name,
                    t1111.bom,
                    t1111.packing_list_no,
                    t1111.sync_date,
                    t1112.min_did
                FROM tmp_core_psi_device_info_2 t1111
                LEFT OUTER JOIN tmp_core_psi_device_info_3 t1112
                on t1111.imei = t1112.imei
            ) t111
            GROUP BY did
        ) t11
        LATERAL VIEW explode(imei_set) arrayTable AS item
    ) t
    GROUP BY imei
) t1
LEFT OUTER JOIN 
(
    SELECT bom,
        MAX(productname) AS productname,
        MAX(device_name) AS device_name,
        MAX(series)      AS series,
        MAX(color)       AS color,
        MAX(config)      AS config,
        MAX(channel)     AS channel
    FROM dim_important_device_baseinfo_new_ds
    GROUP BY bom
) t2
ON t1.bom = t2.bom
LEFT OUTER JOIN 
(
    SELECT UPPER(packing_list_no) AS packing_list_no,
        MAX(packing_list_no)      AS packing_list_no,
        MAX(customer_name)        AS customer_name,  
        MAX(arrival_country)      AS arrival_country,
        MAX(arrival_city)         AS arrival_city,
        MAX(packing_date)         AS packing_date,
        MAX(order_no)             AS order_no
    FROm dim_psi_packing_info_2_dm 
    WHERE pt_d = '$date'
    GROUP BY UPPER(packing_list_no)
) t3
ON UPPER(t1.packing_list_no) = UPPER(t3.packing_list_no);
"
