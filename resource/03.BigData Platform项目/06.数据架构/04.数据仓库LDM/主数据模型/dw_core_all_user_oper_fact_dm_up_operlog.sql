#----------------------------------------------------------------------------
#  file Name: 核心模型 v0.1 dw_core_all_user_oper_fact_dm_up_operlog.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2014.All rights reserved.
#  Purpose:  用户行为记录表,汇总up操作日志的操作记录
#  Describe: 
#  Input: ods_up_oper_log_dm,dim_hispace_app_info_ds,dim_core_channel2service_ds,dim_core_common_d_ds
#  Output: dw_core_all_user_oper_fact_dm
#  Author:  liangxiao 00350030 
#  Dependency: ods_up_oper_log_dm,0,0;dim_hispace_app_info_ds,0,0
#  Creation Date:  2016-07-04
#  Last Modified:  
#----------------------------------------------------------------------------
hive -e "
set mapreduce.job.queuename=QueueA;
use biwarehouse;

CREATE EXTERNAL TABLE IF NOT EXISTS dw_core_all_user_oper_fact_dm
(
    up_id              BIGINT    COMMENT 'up帐号id',
    imei               STRING    COMMENT '设备标识',
    account_type       INT       COMMENT '帐号类型',
    user_account       STRING    COMMENT '用户账户',
    device_name        STRING    COMMENT '设备型号',
    oper_time          STRING    COMMENT '操作时间',
    oper_result        STRING    COMMENT '操作结果',
    oper_object        STRING    COMMENT '操作业务对象',
    oper_channel_id    STRING    COMMENT '操作渠道',
    extend             STRING    COMMENT '其他信息',
    oper_ip            STRING    COMMENT 'ip地址'
)
COMMENT '用户行为记录表，包括UP和device'
PARTITIONED BY (pt_d STRING COMMENT '天分区',pt_service STRING COMMENT '业务id',pt_oper STRING COMMENT '操作类型')
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/AppData/BIProd/Persona/DW/dw_core_all_user_oper_fact_dm_new'
TBLPROPERTIES('orc.compress'='ZLIB');



#解密解得真纠结,两套加密方法共存
set hive.exec.max.created.files=655350;
drop table if exists temp_dw_core_all_user_oper_fact_dm_operlog;
create table temp_dw_core_all_user_oper_fact_dm_operlog as
WITH T1 AS(
SELECT 
            a.user_id                                                                   AS    up_id,
            CASE WHEN !IsEmptyUDF(a.device_id) 
                 THEN CASE WHEN size(split(a.device_id,':'))=1 THEN a.device_id
                           WHEN size(split(a.device_id,':'))=2 THEN UpDecryptionUDF(split(a.device_id,':')[0])
                           WHEN size(split(a.device_id,':'))=3 THEN AesCBCUpDecry(CONCAT(split(a.device_id,':')[0],':',split(a.device_id,':')[1]),'3DBE3962BA89D10CF652276ABE6D1433')
                      ELSE NULL END
                WHEN IsEmptyUDF(a.device_id) AND !IsEmptyUDF(get_json_object(b.deviceInfo,'$.deviceID'))
                THEN CASE WHEN size(split(get_json_object(b.deviceInfo,'$.deviceID'),':'))=1 THEN get_json_object(b.deviceInfo,'$.deviceID') 
                          WHEN size(split(get_json_object(b.deviceInfo,'$.deviceID'),':'))=2 THEN UpDecryptionUDF(split(get_json_object(b.deviceInfo,'$.deviceID'),':')[0])
                          WHEN size(split(get_json_object(b.deviceInfo,'$.deviceID'),':'))=3 THEN AesCBCUpDecry(CONCAT(split(get_json_object(b.deviceInfo,'$.deviceID'),':')[0],':',split(get_json_object(b.deviceInfo,'$.deviceID'),':')[1]),'3DBE3962BA89D10CF652276ABE6D1433')
                     ELSE NULL END
                   ELSE NULL END                                                        AS device_id,
            if(get_json_object(b.deviceInfo, '$.deviceType') in('0','2'),get_json_object(b.deviceInfo, '$.terminalType'),NULL
               )                                                                        AS    device_name,
            cast(a.oper_time as timestamp)                                              AS    oper_time,
            if(!IsEmptyUDF(a.account_type),a.account_type,b.accountType)                AS    account_type,
            CASE WHEN !IsEmptyUDF(a.user_account) 
                 THEN CASE WHEN size(split(a.user_account,':'))=1 THEN a.user_account
                           WHEN size(split(a.user_account,':'))=2 THEN UpDecryptionUDF(split(a.user_account,':')[0])
                           WHEN size(split(a.user_account,':'))=3 THEN AesCBCUpDecry(CONCAT(split(a.user_account,':')[0],':',split(a.user_account,':')[1]),'3DBE3962BA89D10CF652276ABE6D1433')
                      ELSE NULL END
                WHEN IsEmptyUDF(a.user_account) AND !IsEmptyUDF(b.userAccount)
                THEN CASE WHEN size(split(b.userAccount,':'))=1 THEN b.userAccount 
                          WHEN size(split(b.userAccount,':'))=2 THEN UpDecryptionUDF(split(b.userAccount,':')[0])
                          WHEN size(split(b.userAccount,':'))=3 THEN AesCBCUpDecry(CONCAT(split(b.userAccount,':')[0],':',split(b.userAccount,':')[1]),'3DBE3962BA89D10CF652276ABE6D1433')
                     ELSE NULL END
                   ELSE NULL END                                                                      AS user_account,
             CASE WHEN !IsEmptyUDF(a.req_client_ip) 
                 THEN CASE WHEN size(split(a.req_client_ip,':'))=1 THEN UpDecryptionUDF(a.req_client_ip)
                           WHEN size(split(a.req_client_ip,':'))=2 THEN AesCBCUpDecry(CONCAT(split(a.req_client_ip,':')[0],':',split(a.req_client_ip,':')[1]),'3DBE3962BA89D10CF652276ABE6D1433')
                      ELSE NULL END
                WHEN IsEmptyUDF(a.req_client_ip) AND !IsEmptyUDF(b.clientIP)
                THEN CASE WHEN size(split(b.clientIP,':'))=1 THEN UpDecryptionUDF(b.clientIP)
                          WHEN size(split(b.clientIP,':'))=2 THEN AesCBCUpDecry(CONCAT(split(b.clientIP,':')[0],':',split(b.clientIP,':')[1]),'3DBE3962BA89D10CF652276ABE6D1433')
                     ELSE NULL END
                   ELSE NULL END                                                                      AS act_ip,
            if(a.success_flag=true,'1','0')                                                           AS    oper_result,
            IF(server_name = 'SSO', trim(other_params), '') app_pkg,
            CASE WHEN other_params rlike 'client_id' THEN split(other_params,'\"')[3]  
            WHEN server_name = 'B2XB' AND interface_name = 'OpenUP.User.getInfo' AND other_params RLIKE '^[0-9]*$' THEN other_params
            ELSE '' END                                                                               AS app_id,
            if(!IsEmpty(b.registerChannel) and b.registerChannel!=-1, b.registerChannel,
               if(!IsEmpty(a.login_channel_id) and a.login_channel_id!=-1,a.login_channel_id,NULL))   AS registerChannel,
            if(!IsEmpty(a.login_channel_id) and a.login_channel_id!=-1, a.login_channel_id, 
                if(!IsEmpty(b.loginChannel) and b.loginChannel!=-1,b.loginChannel,NULL))              AS loginChannel,
            CAST(COALESCE(floor(a.login_channel_id/1000000),floor(b.loginChannel/1000000),
            floor(b.registerChannel/1000000),floor(a.user_id)/10000000000000000) AS INT)              AS service_id,
            a.oper_type
        from
            ods_up_oper_log_dm a 
        LATERAL VIEW json_tuple(a.other_params, 'accountType','deviceInfo','clientIP','userAccount','registerChannel','loginChannel')b AS accountType,deviceInfo,clientIP,userAccount,registerChannel,loginChannel
        WHERE
            pt_d='$date' and !isempty(user_id) and user_id>0
)
SELECT 
      up_id                                                        AS up_id,
      if(IsDeviceIdLegal(device_id) and length(device_id)<17,
      device_id,null)                                              AS device_id,
      device_name                                                  AS device_name,
      oper_time                                                    AS oper_time,
      account_type                                                 AS account_type,
      user_account                                                 AS user_account,
      act_ip                                                       AS act_ip,      
      oper_result                                                  AS oper_result, 
      app_pkg                                                      AS app_pkg,      
      app_id                                                       AS app_id,      
      registerchannel                                              AS registerchannel,
      loginchannel                                                 AS loginchannel,   
      CAST(COALESCE(floor(loginchannel/1000000),floor(registerChannel/1000000),
           floor(up_id)/10000000000000000) AS INT)                 AS service_id,     
      oper_type                                                    AS oper_type    
FROM T1;


#UP-操作记录
#华为阅读与荣耀阅读需要通过appid来判断业务归属
set hive.exec.dynamic.partition=true;  
set hive.exec.dynamic.partition.mode=nonstrict;

INSERT OVERWRITE TABLE dw_core_all_user_oper_fact_dm
PARTITION (pt_d = '$date',pt_service ,pt_oper)
SELECT
    t.up_id                                          AS    up_id,
    RevertDeviceId(DeviceIdFormat(device_id))        AS    imei,
    t.account_type                                   AS    account_type,
    t.user_account                                   AS    user_account,
    IF(!REGEXP(TerminalFormateUDF(device_name), '^[0-9a-zA-Z \\\\-\\\\._\\\\+]+$'), NULL, 
       TerminalFormateUDF(t.device_name))            AS    device_name,
    t.oper_time                                      AS    oper_time,
    t.oper_result                                    AS    oper_result,
    COALESCE(t.app_pkg,t2.app_pkg,t3.pkg_name)       AS    oper_object,
    t.registerChannel                                AS    oper_channel_id,
    NULL                                             AS    extend,
    act_ip                                           AS    oper_ip,
    case when isEmpty(t.service_id) and t.loginChannel<1000000 then 'dbank' 
         when COALESCE(t.app_pkg,t2.app_pkg,t3.pkg_name)='com.huawei.hwireader' then 'hwread'
         when COALESCE(t.app_pkg,t2.app_pkg,t3.pkg_name)='com.huawei.hnreader' then 'hnread'
         when !isempty(t3.service_id) then t3.service_id
         when !isempty(t4.common_value) then t4.common_value
    else 'other'         end                         AS    service_id,
    'register'                                       AS    oper_type
from 
    (select * from temp_dw_core_all_user_oper_fact_dm_operlog  where oper_type=1) t
left outer join
    (select if(substr(dev_app_id,1,1)='S',substr(dev_app_id,2),dev_app_id) dev_app_id,max(package) app_pkg 
     from dim_hispace_app_info_ds group by if(substr(dev_app_id,1,1)='S',substr(dev_app_id,2),dev_app_id) 
    ) t2 on concat('C',t.app_id)=t2.dev_app_id
left outer join dim_core_channel2service_ds t3 on t.registerChannel =t3.channel_id
left outer join 
(SELECT common_cd service_id,common_value FROM dim_core_common_d_ds WHERE COMMON_ID=7) t4 on t.service_id=t4.service_id;


INSERT OVERWRITE TABLE dw_core_all_user_oper_fact_dm
PARTITION (pt_d = '$date',pt_service ,pt_oper)
SELECT
    t.up_id                                          AS    up_id,
    RevertDeviceId(DeviceIdFormat(device_id))        AS    imei,
    t.account_type                                   AS    account_type,
    t.user_account                                   AS    user_account,
    IF(!REGEXP(TerminalFormateUDF(device_name), '^[0-9a-zA-Z \\\\-\\\\._\\\\+]+$'), NULL, 
       TerminalFormateUDF(t.device_name))            AS    device_name,
    t.oper_time                                      AS    oper_time,
    t.oper_result                                    AS    oper_result,
    COALESCE(t.app_pkg,t2.app_pkg,t3.pkg_name)       AS    oper_object,
    t.loginChannel                                   AS    oper_channel_id,
    NULL                                             AS    extend,
    act_ip                                           AS    oper_ip,
    case when isEmpty(t.service_id) and t.loginChannel<1000000 then 'dbank' 
         when COALESCE(t.app_pkg,t2.app_pkg,t3.pkg_name)='com.huawei.hwireader' then 'hwread'
         when COALESCE(t.app_pkg,t2.app_pkg,t3.pkg_name)='com.huawei.hnreader' then 'hnread'
         when !isempty(t3.service_id) then t3.service_id
         when !isempty(t4.common_value) then t4.common_value
         when !isempty(t5.service_id)   then t5.service_id
    else 'other'         end                        AS service_id,
    'use'                                           AS oper_type
from 
    (select * from temp_dw_core_all_user_oper_fact_dm_operlog  where oper_type!=1) t
left outer join
    (select if(substr(dev_app_id,1,1)='S',substr(dev_app_id,2),dev_app_id) dev_app_id,max(package) app_pkg 
     from dim_hispace_app_info_ds group by if(substr(dev_app_id,1,1)='S',substr(dev_app_id,2),dev_app_id) 
    ) t2 on concat('C',t.app_id)=t2.dev_app_id
left outer join dim_core_channel2service_ds t3 on t.loginChannel =t3.channel_id
left outer join 
(SELECT common_cd service_id,common_value FROM dim_core_common_d_ds WHERE COMMON_ID=7) t4 on t.service_id=t4.service_id
left outer join
(SELECT pkg_name,max(service_id) service_id FROM dim_core_channel2service_ds GROUP BY pkg_name) t5 on t2.app_pkg=t5.pkg_name;

#删除临时表
drop table temp_dw_core_all_user_oper_fact_dm_operlog;
"