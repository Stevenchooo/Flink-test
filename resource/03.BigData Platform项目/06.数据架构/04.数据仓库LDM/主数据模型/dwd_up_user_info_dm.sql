# ----------------------------------------------------------------------------
#  File Name:   dwd_up_user_info_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: 帐号用户信息表
#  Describe:
#  Input：  ODS_UP_USER_INFO_DM,dim_core_common_d_ds
#  Output： dwd_up_user_info_dm
#  Author:  liangxiao/l00350030
#  Creation Date:  2016-06-06
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
use biwarehouse;

set hive.exec.dynamic.partition=true;  
set hive.exec.dynamic.partition.mode=nonstrict;

CREATE EXTERNAL TABLE IF NOT EXISTS dwd_up_user_info_dm
(
    up_id                 bigint         COMMENT     '用户ID（内部）',
    nick_name             string         COMMENT     '昵称',
    language_code         string         COMMENT     '语言代码',
    first_name            string         COMMENT     'FirstName',
    last_name             string         COMMENT     'LastName',
    gender                int            COMMENT     '性别0男1女2保密-1未知',
    birthday              date           COMMENT     '用户生日',
    address               string         COMMENT     '用户家庭地址',
    occupation            string         COMMENT     '用户职业',
    national_code         string         COMMENT     '用户国籍代码',
    province              string         COMMENT     '省份',
    city                  string         COMMENT     '城市',
    password_prompt       string         COMMENT     '密码提示信息',
    head_picture_url      string         COMMENT     '头像',
    service_flag          string         COMMENT     '业务开通标志',
    user_state            int            COMMENT     '用户激活状态0:未激活1:已激活',
    user_valid_status     int            COMMENT     '用户有效状态注：仅供查询1正常2Dbank暂停3销户4全业务暂停',
    inviter_user_id       bigint         COMMENT     '邀请者UserID',
    inviter               string         COMMENT     '邀请者',
    update_time           timestamp      COMMENT     '数据更新时间',
    register_time         timestamp      COMMENT     '注册时间',
    register_client_type  int            COMMENT     '注册客户端类型',
    register_client_ip    string         COMMENT     '注册客户端IP',
    register_from         bigint         COMMENT     '注册来源',
    register_channel_id   int            COMMENT     '注册渠道;格式：[3位业务ID]+[6位渠道编码]'
)
PARTITIONED BY (pt_d STRING COMMENT '天分区')
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/AppData/BIProd/Persona/DW/dwd_up_user_info_dm'
TBLPROPERTIES('orc.compress'='ZLIB');


INSERT OVERWRITE TABLE dwd_up_user_info_dm
PARTITION(pt_d='$date')
SELECT 
    up_id,
	max(nick_name),
	max(language_code),
	max(first_name),
	max(last_name),
	max(gender),
	max(birthday),
	max(address),
	max(occupation),
	max(national_code),
	max(province),
	max(city),
	max(password_prompt),
	max(head_picture_url),
	max(service_flag),
	max(user_state),
	max(user_valid_status),
	max(inviter_user_id),
	max(inviter),
	max(update_time),
	max(register_time),
	max(register_client_type),
	max(register_client_ip),
	max(register_from),
	max(register_channel_id)
FROM(
SELECT
    user_id                                                                        AS    up_id,                       
    if(isEmpty(nick_name),null,trim(nick_name))                                    AS    nick_name,          
    if(isEmpty(language_code),null,trim(language_code))                            AS    language_code,       
    if(isEmpty(first_name),null,trim(first_name))                                  AS    first_name,          
    if(isEmpty(last_name),null,trim(last_name))                                    AS    last_name,           
    case when isEmpty(gender) then -1
         when !gender in(0,1,2) then -1
    ELSE gender end                                                                AS    gender,              
    dateformat(birthday)                                                           AS    birthday,            
    if(isEmpty(address),null,trim(address))                                        AS    address,             
    if(isEmpty(occupation),null,trim(occupation))                                  AS    occupation,          
    if(isEmpty(national_code),null,trim(national_code))                            AS    national_code,       
    if(isEmpty(province),null,trim(province))                                      AS    province,            
    if(isEmpty(city),null,trim(city))                                              AS    city,                
    if(isEmpty(password_prompt),null,trim(password_prompt))                        AS    password_prompt,     
    if(isEmpty(head_picture_url),null,trim(head_picture_url))                      AS    head_picture_url,    
    if(isEmpty(service_flag),null,trim(service_flag))                              AS    service_flag,        
    coalesce(user_state,-1)                                                        AS    user_state,          
    coalesce(user_valid_status,-1)                                                 AS    user_valid_status,   
    inviter_user_id                                                                AS    inviter_user_id,     
    if(isEmpty(inviter),null,trim(inviter))                                        AS    inviter,             
    cast(update_time as timestamp)                                                 AS    update_time,         
    cast(register_time as timestamp)                                               AS    register_time,       
    coalesce(t1.register_client_type,-1)                                           AS    register_client_type,
    if(isEmpty(register_client_ip),null,trim(register_client_ip))                  AS    register_client_ip, 
    coalesce(register_from,-1)                                                     AS    register_from,       
    if(isEmpty(t1.register_channel_id) or t1.register_channel_id=-1,
    concat(floor(t1.user_id/10000000000000000),'000000'),t1.register_channel_id)   AS    register_channel_id
FROM
    ods_up_user_info_dm t1 where pt_d='$date' 
UNION ALL
SELECT 
     up_id,        
	 nick_name,           
	 language_code,       
	 first_name,          
	 last_name,           
	 gender,              
	 birthday,            
	 address,             
	 occupation,          
	 national_code,       
	 province,            
	 city,               
	 password_prompt,     
	 head_picture_url,    
	 service_flag,       
	 user_state,         
	 user_valid_status,  
	 inviter_user_id,    
	 inviter,             
	 update_time,        
	 register_time,      
	 register_client_type,
	 register_client_ip,
	 register_from,  
	 register_channel_id
FROM
    (select * from dwd_up_user_info_dm where pt_d='$last_date') a
where not exists 
(select 1 from
    (select user_id from ods_up_user_info_dm where pt_d='$date') b 
where a.up_id=b.user_id )
)t
GROUP BY up_id;
"