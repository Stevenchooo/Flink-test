# ----------------------------------------------------------------------------
#  File Name:   dw_core_trade_app_fact_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: 支付流水事实表
#  Describe: 数据源增量推送，先取当天的最新数据入表，再取最新数据中不包含的数据入维表；再通过商户ID及应用ID获取流水的业务编码
#  Input：  dim_core_trade_app_detail_info_dm, dim_core_device_user_info_dm
#  Output： dw_core_trade_app_fact_dm
#  Author:  heyuanhong/h00201904
#  Creation Date:  2016-06-06
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
set hive.exec.compress.output=false;
use biwarehouse;

CREATE EXTERNAL TABLE IF NOT EXISTS dw_core_trade_app_fact_dm 
(
    project_no              STRING COMMENT '项目编码',
    app_id                  STRING COMMENT '应用包名', 
    application_id          STRING COMMENT '应用ID', 
    user_id                 BIGINT COMMENT '华为账号',  
    imei                    STRING COMMENT '设备ID',  
    pay_type                STRING COMMENT '支付方式', 
    province                STRING COMMENT '省份',  
    city                    STRING COMMENT '城市',  
    device_name             STRING COMMENT '机型',  
    consume                 DOUBLE COMMENT '消费', 
    consume_times           INT    COMMENT '消费次数', 
    consume_refund          DOUBLE COMMENT '消费退款',
    consume_refund_times    INT    COMMENT '消费退款次数',
    topup                   DOUBLE COMMENT '充值',
    topup_times             INT    COMMENT '充值次数', 
    topup_refund            DOUBLE COMMENT '充值退款', 
    topup_refund_times      INT    COMMENT '充值退款次数', 
    gift_up                 DOUBLE COMMENT '赠送调增', 
    gift_up_times           INT    COMMENT '赠送调增次数', 
    gift_down               DOUBLE COMMENT '赠送调减', 
    gift_down_times         INT    COMMENT '赠送调减次数'
)
PARTITIONED BY (pt_d STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/hadoop-NJ/data/common/DW/dw_core_trade_app_fact_dm'
TBLPROPERTIES('orc.compress'='ZLIB');

INSERT OVERWRITE TABLE dw_core_trade_app_fact_dm
PARTITION(pt_d='$date')
SELECT 
    t1.project_no,
    t1.app_id,
    t1.application_id,
    t1.user_id,
    t1.imei,
    t1.pay_type,
    t2.province,
    t2.city,
    t2.device_name,
    SUM(  IF(pay_type IN ('花币' ,'华为钱包') AND type = 'REFUND' AND (service_catalog <> 'H0' or service_catalog is null), 0.0, pay_money))       AS consume,
    COUNT(IF(pay_type IN ('花币' ,'华为钱包') AND type = 'REFUND' AND (service_catalog <> 'H0' or service_catalog is null), NULL, 1))              AS consume_times,
    SUM(  IF(pay_type IN ('花币' ,'华为钱包') AND type = 'REFUND' AND (service_catalog <> 'H0' or service_catalog is null), pay_money, 0.0))       AS consume_refund,
    COUNT(IF(pay_type IN ('花币' ,'华为钱包') AND type = 'REFUND' AND (service_catalog <> 'H0' or service_catalog is null), 1, NULL))              AS consume_refund_times,
    SUM(  IF(service_catalog = 'H0' AND type = 'REFUND' AND (pay_type NOT IN('花币', '华为钱包', '预付款') OR pay_type is null), 0.0, pay_money))  AS topup,
    COUNT(IF(service_catalog = 'H0' AND type = 'REFUND' AND (pay_type NOT IN('花币', '华为钱包', '预付款') OR pay_type is null), NULL, 1))         AS topup_times,
    SUM(  IF(service_catalog = 'H0' AND type = 'REFUND' AND (pay_type NOT IN('花币', '华为钱包', '预付款') OR pay_type is null), pay_money, 0.0))  AS topup_refund,
    COUNT(IF(service_catalog = 'H0' AND type = 'REFUND' AND (pay_type NOT IN('花币', '华为钱包', '预付款') OR pay_type is null), 1, NULL))         AS topup_refund_times,
    SUM(  IF(service_catalog = 'H0' AND pay_type IN ('花币' ,'华为钱包') AND pay_money >= 0, pay_money, 0.0))                                      AS gift_up,
    COUNT(IF(service_catalog = 'H0' AND pay_type IN ('花币' ,'华为钱包') AND pay_money >= 0, 1, NULL))                                             AS gift_up_times,
    SUM(  IF(service_catalog = 'H0' AND pay_type IN ('花币' ,'华为钱包') AND pay_money <  0, pay_money, 0.0))                                      AS gift_down,
    COUNT(IF(service_catalog = 'H0' AND pay_type IN ('花币' ,'华为钱包') AND pay_money <  0, 1, NULL))                                             AS gift_down_times
FROM 
(
    SELECT 
        *
    FROM 
        dim_core_trade_app_detail_info_dm
    WHERE 
        pt_d = '$date' AND IsDeviceIdLegal(imei) AND trade_state IN ('0','3') AND to_date(trade_time) = '$date_ep'
) t1
LEFT OUTER JOIN
(
    SELECT
        *,
        last_province as province,
        last_city as city
    FROM
        dim_core_device_user_info_dm
    WHERE pt_d = '$date' AND pt_service='summary' AND pt_source = 'summary'
) t2
ON t1.imei = t2.imei
GROUP BY project_no, app_id, application_id, user_id, t1.imei, pay_type, province, city, device_name;
"