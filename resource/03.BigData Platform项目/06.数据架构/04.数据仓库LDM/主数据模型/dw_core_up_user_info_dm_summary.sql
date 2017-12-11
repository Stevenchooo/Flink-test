# ----------------------------------------------------------------------------
#  File Name:   dw_core_up_user_info_dm_summary.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: 业务帐号信息
#  Describe: 
#       用户使用业务状况
#       分为三种情况：1、本业务注册 2、其他业务注册
#       依赖dim_user_info_ds,ods_up_oper_log_dm,
#       1、每天将dim_user_info_ds的ServiceFlag字段分析得到新增的开通业务的用户
#       2、每天更新数据来源于ods_up_oper_log_dm，找到每个用户的操作日志，
#          如果user_id标识的业务类型为a，登录的请求客户端类型对应的业务是b，就认为该用户为b业务的其他业务注册用户，注意新增和更新处理
#  Input：  dim_user_info_ds,ods_up_oper_log_dm
#  Output： dim_core_up_user_info_dm
#  Author:  liangxiao/l00350030
#  Creation Date:  2016-06-06
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
use biwarehouse;

set hive.exec.dynamic.partition=true;  
set hive.exec.dynamic.partition.mode=nonstrict;

CREATE EXTERNAL TABLE IF NOT EXISTS dw_core_up_user_info_dm
(
    up_id              BIGINT    COMMENT 'upid',
    register_status    INT       COMMENT '是否本业务注册,1是0否',
    user_valid_status  INT       COMMENT '帐号有效状态，1正常2暂停',
    account_phone      STRING    COMMENT '手机帐号',
    account_email      STRING    COMMENT '邮箱帐号',
    safe_phone         STRING    COMMENT '安全手机',
    safe_email         STRING    COMMENT '安全邮箱',
    account_3rd        STRING    COMMENT '第三方帐号，只保存类型,#连接',
    create_time        STRING    COMMENT '首次使用时间',
    latest_oper_time   STRING    COMMENT '最近一次使用时间',
    biz_channel_id     STRING    COMMENT '渠道id',
    extend             STRING    COMMENT '扩展字段',
    rights_id          INT       COMMENT '会员权益ID',
    account_common     INT       COMMENT '普通帐号',
    register_chnl_id   INT       COMMENT '注册渠道id'
)
PARTITIONED BY (pt_d STRING COMMENT '天分区',pt_service STRING COMMENT '业务id')
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/AppData/BIProd/Persona/DW/dw_core_up_user_info_dm'
TBLPROPERTIES('orc.compress'='ZLIB');


--写汇总数据，生成summary分区
INSERT OVERWRITE TABLE dw_core_up_user_info_dm
PARTITION(pt_d,pt_service)
SELECT 
    t1.up_id,         
    NULL                                         AS     register_status,
    MIN(user_valid_status)                       AS     user_valid_status,
    MAX(t1.account_phone)                        AS     account_phone,
    MAX(t1.account_email)                        AS     account_email,  
    MAX(t1.safe_phone)                           AS     safe_phone,  
    MAX(t1.safe_email)                           AS     safe_email,
    MAX(t1.account_3rd)                          AS     account_3rd,    
    MIN(t1.create_time)                          AS     create_time,     
    MAX(t1.latest_oper_time)                     AS     update_time,
    groupConcatDistinct(t1.biz_channel_id,'\043',true)    AS     biz_channel_id,
    groupConcatDistinct(t1.pt_service,'\043',true)        AS     extend,
    MAX(rights_id)                               AS     rights_id,       
	MAX(account_common)                          AS     account_common,  
	MAX(register_chnl_id)                        AS     register_chnl_id,
    '$date'                                      AS     pt_d,
    'summary'                                    AS     pt_service
FROM dw_core_up_user_info_dm t1 WHERE pt_d='$date' and pt_service!='summary'
group by t1.up_id;

"
