# ----------------------------------------------------------------------------
#  File Name:   dw_core_up_user_info_dm_init.sql
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


--创建临时表，取帐号信息
drop table temp_user_account_info;
create table temp_user_account_info as
select user_id,
       max(account_email) account_email,
       max(account_phone) account_phone,
       max(safe_phone)    safe_phone,
       max(safe_email)    safe_email,
       max(account_common) account_common
from(
    select 
        user_id ,
        case when account_type=1 then user_account else null end as account_email,
        case when account_type=2 then user_account else null end as account_phone,
        case when account_type=6 then user_account else null end as safe_phone,
        case when account_type=5 then user_account else null end as safe_email,
        case when account_type=0 then user_account else null end as account_common
    from dim_user_acct_info_ds) t
group by user_id;

--创建临时表，取第三方帐号信息
drop table temp_user_account_info_3rd;
create table temp_user_account_info_3rd as
select user_id,
       FI1_groupconcat(account_type,'\043') account_3rd
from(
    select user_id,account_type
    from dim_user_acct_info_ds where !(account_type in(0,1,2,5,6)) 
    ) t
group by user_id;

--取会员权益
drop table temp_user_member_right;
create table temp_user_member_right as
select 
     userid user_id,
     max(rightsid) rights_id
from ODS_UP_T_UP_MEMBERRIGHT_DM 
where pt_d='$date' 
group by userid;


--写各个业务的数据
INSERT OVERWRITE TABLE dw_core_up_user_info_dm
PARTITION(pt_d,pt_service)
SELECT 
    t1.user_id,         
    t1.register_status,
    t4.user_valid_status,
    t2.account_phone,
    t2.account_email,  
    t2.safe_phone,  
    t2.safe_email, 
    t3.account_3rd,   
    t1.create_time,     
    t1.update_time,
    t1.biz_channel_id,
    null AS extend,
    t5.rights_id,
    t2.account_common,
    IF(IsEmpty(t4.register_channel_id) or t4.register_channel_id=-1, 
    concat(floor(t1.user_id/10000000000000000),'000000'),t4.register_channel_id) register_chnl_id,
    t1.pt_d,
    t1.pt_service
FROM(
SELECT
    user_id,
    biz_channel_id,
    MAX(register_status)               AS register_status,
    MIN(create_time)                   AS create_time,
    MAX(update_time)                   AS update_time,
    '$date'                            AS pt_d,
    service_id                         AS pt_service
FROM
    (
        SELECT t1.user_id,
               t1.biz_channel_id,
               t1.register_status,
               t1.create_time,
               t1.update_time,
               COALESCE(t2.service_id,t3.common_value,'other') service_id
        FROM(
        SELECT
            user_id,
            IF(IsEmpty(register_channel_id) or register_channel_id=-1, 
             concat(floor(user_id/10000000000000000),'000000'),register_channel_id)           AS biz_channel_id,
            1                                                                                 AS register_status,
            cast(register_time as timestamp)                                                  AS create_time,
            cast(register_time as timestamp)                                                  AS update_time,
            IF(IsEmpty(register_channel_id) or register_channel_id=-1, 
               floor(user_id/10000000000000000), floor(register_channel_id/1000000))          AS service_id
        FROM
            dim_user_info_ds 
        WHERE to_date(register_time)<='$date_ep' and pt='real'
        ) t1
        LEFT OUTER JOIN dim_core_channel2service_ds t2 on t1.biz_channel_id=t2.channel_id
        LEFT OUTER JOIN 
        (SELECT common_cd service_id,common_value FROM dim_core_common_d_ds WHERE COMMON_ID=7) t3 on t1.service_id=t3.service_id
        UNION ALL
        SELECT
            up_id                                                                     AS user_id,
            oper_channel_id                                                           AS biz_channel_id,
            IF(floor(up_id/10000000000000000)=floor(oper_channel_id/1000000),1,0)     AS register_status,
            oper_time                                                                 AS create_time,
            oper_time                                                                 AS update_time,
            pt_service                                                                AS service_id
        FROM dw_core_all_user_oper_fact_dm
        WHERE pt_d='$date' and pt_oper in('register','use')
        UNION ALL
        SELECT 
             up_id             AS  user_id,       
             biz_channel_id,                 
             register_status,
             create_time,
             latest_oper_time as update_time,
             pt_service as service_id
        FROM dw_core_up_user_info_dm 
        WHERE pt_d='$last_date' and pt_service!='summary'
    )t
    GROUP BY user_id,service_id,biz_channel_id
    ) t1
    LEFT OUTER JOIN temp_user_account_info t2  on t1.user_id=t2.user_id
    LEFT OUTER JOIN temp_user_account_info_3rd t3 on t1.user_id=t3.user_id
    LEFT OUTER JOIN dim_user_info_ds t4 on t1.user_id=t4.user_id
    LEFT OUTER JOIN temp_user_member_right t5 on t1.user_id=t5.user_id
    ;

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

--删除临时表
drop table temp_user_account_info;
drop table temp_user_account_info_3rd;
drop table temp_user_member_right;
"
