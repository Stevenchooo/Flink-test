#----------------------------------------------------------------------------
#  file Name: 核心模型 v0.1 dw_core_all_user_oper_fact_dm_up_vmall.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2014.All rights reserved.
#  Purpose:  用户行为记录表,汇总up操作日志的操作记录
#  Describe: 
#  Input: dim_vmall2_order_sku_ds
#  Output: dw_core_all_user_oper_fact_dm
#  Author:  liangxiao 00350030 
#  Dependency: dim_vmall2_order_sku_ds,0,0;
#  Creation Date:  2016-07-04
#  Last Modified:  
#----------------------------------------------------------------------------
hive -e "
CREATE EXTERNAL TABLE IF NOT EXISTS dw_core_all_user_oper_fact_dm
(
    up_id              BIGINT    COMMENT 'up帐号id',
    imei               STRING    COMMENT '设备标识',
    account_type       INT       COMMENT '帐号类型',
    user_account       STRING    COMMENT '用户账户',
    device_name        STRING    COMMENT '设备型号',
    oper_time          STRING    COMMENT '操作时间',
    oper_province      STRING    COMMENT '省份',
    oper_city          STRING    COMMENT '地市',
    oper_result        STRING    COMMENT '操作结果',
    oper_object        STRING    COMMENT '操作业务对象',
    oper_channel_id    STRING    COMMENT '操作渠道',
    extend             STRING    COMMENT '其他信息',
    create_timestamp   STRING    COMMENT '标识ETL时间'
)
COMMENT '用户行为记录表，包括UP和device'
PARTITIONED BY (pt_d STRING COMMENT '天分区',pt_service STRING COMMENT '业务id',pt_oper STRING COMMENT '操作类型')
STORED AS INPUTFORMAT 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'
          OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION '/AppData/BIProd/Persona/DW/dw_core_all_user_oper_fact_dm';


set mapreduce.job.queuename=QueueA;
set hive.exec.compress.output=true;
set mapreduce.output.fileoutputformat.compress.codec=com.hadoop.compression.lzo.LzopCodec;
use biwarehouse;

#vmall订单记录
set hive.exec.dynamic.partition=true;  
set hive.exec.dynamic.partition.mode=nonstrict;

INSERT OVERWRITE TABLE dw_core_all_user_oper_fact_dm
PARTITION (pt_d = '$date',pt_service ,pt_oper)
SELECT
    t1.user_id                                       AS    up_id,
    null                                             AS    imei,
    null                                             AS    account_type,
    null                                             AS    user_account,
    null                                             AS    device_name,
    t3.order_date                                    AS    oper_time,
    GetAreaIDUDF(t3.cust_ip,1)                       AS    oper_province,
    GeoIpCityApiUDF(t3.cust_ip)[2]                   AS    oper_city,
    t3.status                                        AS    oper_result,
    t1.sku_name                                      AS    oper_object,
    t1.channel                                       AS    oper_channel_id,
    concat('{\"plat\":\"',COALESCE(t1.plat,''),
    '\",\"pay_name\":\"',COALESCE(if(t4.pay_method_name=t4.pay_channel_name,t4.pay_method_name,concat(t4.pay_channel_name,' ',t4.pay_method_name)),''),
    '\",\"order_type\":\"',COALESCE(cast(t3.type as string),''),
    '\",\"order_code\":\"',COALESCE(t1.order_code,''),
	'\",\"payment_status\":\"',COALESCE(t3.payment_status,''),
    '\",\"payment_amount\":\"',COALESCE(concat(t3.payment_amount),''),'\"}') AS    extend,
    from_unixtime(unix_timestamp(),'yyyy-MM-dd HH:mm:ss') AS create_timestamp,
    'vmall'                                         AS service_id,
    'order'                                       AS oper_type
FROM
(
    SELECT 
        t1.user_id,t1.order_code,
        max(t1.plat)    AS    plat,
        max(t1.channel)    AS    channel,
        FI1_groupconcat(t2.prd_sku_name,'\043')    AS    sku_name
    FROM
        (select * from dim_vmall2_order_sku_ds 
        WHERE (to_date(order_date)='$date_ep' or to_date(payment_date)='$date_ep' or to_date(update_date)='$date_ep') and cast(user_id as bigint)>0 
        )t1
    LEFT OUTER JOIN 
        ODS_VMALL_TBL_PRD_SKU_DS t2
    on(t1.sku_code=t2.code)
    GROUP BY t1.user_id,t1.order_code
)t1
left outer join
    dim_vmall2_tbl_order_ds t3
on(t1.order_code=t3.order_code and cast(t1.user_id as string)=cast(t3.user_id as string))
left outer join
    dim_vmall_paytype_desc_ds t4
on (t3.payment_type=t4.pay_type and t3.payment_method=t4.pay_method)
where !IsEmptyUDF(t1.sku_name);
"