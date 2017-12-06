#----------------------------------------------------------------------------
#  file Name: v0.3 dw_hiad_user_defined_label_ds.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2013.All rights reserved.
#  Purpose:  广告自定义标签活动标签信息
#  Describe: 
#  Input:  
#  Output: dw_hiad_user_defined_label_ds
#  Author:  wangweiguo 00190105
#  Creation Date  2013-08-29
#  Last Modified:  2013-08-29
#----------------------------------------------------------------------------
hive -e"
CREATE EXTERNAL TABLE IF NOT EXISTS dw_hiad_user_defined_label_ds
(
    device_id        STRING,
    labels           STRING,
    service_id       STRING
)
PARTITIONED BY (activity_id STRING)
ROW FORMAT DELIMITED FIELDS 
TERMINATED BY '\001' LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/label/dw_hiad_user_defined_label_ds';

set hive.exec.dynamic.partition=true;  
set hive.exec.dynamic.partition.mode=nonstrict;

INSERT OVERWRITE TABLE dw_hiad_user_defined_label_ds
PARTITION(activity_id)
SELECT
    t3.device_id            as      device_id,
    t3.labels               as      labels,
    t3.service_id           as      service_id,
    t3.activity_id          as      activity_id
FROM
(
    SELECT
        t2.device_id                            as      device_id,
        if(t1.event = 1 and t2.succ_flag >0,t1.labels,if(t1.event = 2 and t2.clicks >0,t1.labels,null))   as      labels,
        'ad'                                    as      service_id,
        t1.activity_id                          as      activity_id
    FROM   
    (
        SELECT
            activity_id,
            event,
            labels
        FROM
            dw_hiad_activity_defined_ds
    )t1
    left outer join
    (
        SELECT
            *
        FROM
            dw_hiad_hicloud_push_stat_dm 
        where
            succ_flag > 0
        or
            clicks > 0
        
    )t2
    on t1.activity_id = t2.task_id
    where
        t2.task_id is not null
)t3
where
      t3.labels is not null;
"