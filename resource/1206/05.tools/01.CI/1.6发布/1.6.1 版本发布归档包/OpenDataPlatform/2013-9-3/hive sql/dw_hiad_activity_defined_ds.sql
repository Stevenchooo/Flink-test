#----------------------------------------------------------------------------
#  file Name: v0.3 dw_hiad_activity_defined_ds.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2013.All rights reserved.
#  Purpose:  广告自定义标签活动标签信息
#  Describe: 
#  Input:  
#  Output: dw_hiad_activity_defined_ds
#  Author:  wangweiguo 00190105
#  Creation Date  2013-08-29
#  Last Modified:  2013-08-29
#----------------------------------------------------------------------------
hive -e"
CREATE EXTERNAL TABLE IF NOT EXISTS dw_hiad_activity_defined_ds
(
    event        STRING,  
    labels       STRING,
    service_id   STRING,
    pt_d         STRING
)
PARTITIONED BY (activity_id STRING)
ROW FORMAT DELIMITED FIELDS 
TERMINATED BY '\001' LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/label/dw_hiad_activity_defined_ds';


set hive.exec.dynamic.partition=true;  
set hive.exec.dynamic.partition.mode=nonstrict;

INSERT OVERWRITE TABLE dw_hiad_activity_defined_ds
PARTITION(activity_id)
SELECT
    event           as      event,
    labels          as      labels,
    'ad'            as      service_id,
    pt_d            as      pt_d,
    activity_id     as      activity_id
FROM
    ODS_AD_ACTIVITY_LABEL_DM
WHERE
    pt_d = '$date'
"