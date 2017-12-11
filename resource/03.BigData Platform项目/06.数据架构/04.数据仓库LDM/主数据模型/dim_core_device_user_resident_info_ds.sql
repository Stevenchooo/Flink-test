# ----------------------------------------------------------------------------
#  File Name:   dim_core_device_user_resident_info_ds.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: 设备最近30天常驻地域
#  Describe: 设备最近30天常驻地域
#  Input：  dim_core_device_user_info_dm
#  Output： dim_core_device_user_resident_info_ds
#  Author:  heyuanhong/h00201904
#  Creation Date:  2016-06-06
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
use biwarehouse;

CREATE EXTERNAL TABLE IF NOT EXISTS dim_core_device_user_resident_info_ds
(
    imei              STRING COMMENT '设备IMEI/MEID',
    did               STRING COMMENT '根据PSI数据生成的设备唯一标识',
    resident_province STRING COMMENT '最近30天常驻省份',
    resident_city     STRING COMMENT '最近30天常驻城市'
)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/hadoop-NJ/data/common/DIM/dim_core_device_user_resident_info_ds'
TBLPROPERTIES('orc.compress'='ZLIB');


INSERT OVERWRITE TABLE dim_core_device_user_resident_info_ds
SELECT
    imei,
    did,
    SPLIT(area,'#')[0]  AS resident_province,
    SPLIT(area,'#')[1]  AS resident_city
FROM
(
    SELECT
        imei,
        did,
        area,
        row_number() OVER (PARTITION BY imei ORDER BY cnt DESC) row_number
    FROM
    (
        SELECT
            imei                                                AS imei,
            MAX(did)                                            AS did,
            CONCAT(last_province, '#', COALESCE(last_city, '')) AS area,
            COUNT(*)                                            AS cnt
        FROM dim_core_device_user_info_dm
        WHERE pt_d >= regexp_replace(date_sub('$date_ep', 30),'-','') AND pt_d <= '$date' 
            AND pt_service = 'summary' AND pt_source = 'summary'
            AND IsEmpty(CONCAT(last_province, '#', COALESCE(last_city, ''))) = FALSE
        GROUP BY imei,CONCAT(last_province, '#', COALESCE(last_city, ''))
    ) t1
) t
WHERE row_number = 1;
"
