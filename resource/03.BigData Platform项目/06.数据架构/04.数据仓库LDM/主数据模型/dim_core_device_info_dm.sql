# ----------------------------------------------------------------------------
#  File Name:   dim_core_device_info_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: 帐号设备信息
#  Describe: 帐号全量设备信息（合法IMEI设备）
#  Input：  dim_core_psi_one_imei_detail_dm,dim_core_device_user_info_dm

#  Output： dim_core_device_info_dm
#  Author:  heyuanhong/h00201904
#  Creation Date:  2016-06-06
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
use biwarehouse;

CREATE EXTERNAL TABLE IF NOT EXISTS dim_core_device_info_dm
(
    device_name            STRING COMMENT '外部型号',
    product                STRING COMMENT '产品名称',
    screen_resolution      STRING COMMENT '分辨率',
    double_sim_flag        INT    COMMENT '是否双卡；0：否，1：是',
    publish_date           STRING COMMENT '发布日期',
    price_range            STRING COMMENT '价格区间',
    team_flag              STRING COMMENT '产品系统：DP、GY、HONOR、PAD、ODM、OTHER',
    devices                INT    COMMENT '跟PSI关联上的机型的IMEI数'
)
PARTITIONED BY (pt_d STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/hadoop-NJ/DIM/dim_core_device_info_dm';

INSERT OVERWRITE TABLE dim_core_device_info_dm
PARTITION(pt_d='$date')
SELECT COALESCE(t1.device_name,t2.device_name) AS device_name,
    MAX(t2.product)                            AS product,
    NULL                                       AS screen_resolution,
    NULL                                       AS double_sim_flag,
    NULL                                       AS publish_date,
    NULL                                       AS price_range,
    MAX(COALESCE(t2.team_flag, t3.team_flag))  AS team_flag,
    COUNT(t2.imei)                             AS devices
FROM
(
    SELECT imei,
        device_name
    FROM dim_core_device_user_info_dm
    WHERE pt_d = '$last_date' AND pt_service = 'summary' AND pt_source = 'summary'
) t1
LEFT OUTER JOIN
(
    SELECT *
    FROM dim_core_psi_one_imei_detail_dm
    WHERE pt_d = '$date'
) t2
ON t1.imei = t2.imei
LEFT OUTER JOIN 
(
    SELECT IF(IsEmptyUDF(TerminalFormateUDF(device_name)), NULL, TerminalFormateUDF(device_name)) AS device_name,
        MAX(IF(team_flag = 'HORNOR', 'HONOR', team_flag))                                         AS team_flag
    FROM dim_kpi_hardware_software_info_ds
    GROUP BY IF(IsEmptyUDF(TerminalFormateUDF(device_name)), NULL, TerminalFormateUDF(device_name))
) t3
ON TerminalFormateUDF(t2.device_name) = t3.device_name
WHERE IsEmpty(COALESCE(t1.device_name,t2.device_name)) = FALSE
GROUP BY COALESCE(t1.device_name,t2.device_name);
"
