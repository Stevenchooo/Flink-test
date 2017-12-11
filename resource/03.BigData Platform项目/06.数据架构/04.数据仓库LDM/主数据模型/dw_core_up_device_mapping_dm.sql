# ----------------------------------------------------------------------------
#  File Name: 核心模型 v0.1  dw_core_up_device_mapping_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: 帐号与设备绑定关系
#  Describe: 
#       UP中帐号在设备上登录后，帐号与该设备绑定
#  Input：  ods_up_user_device_info_dm,dw_core_all_user_oper_fact_dm,dim_core_psi_one_imei_detail_dm
#  Output： dw_core_up_device_mapping_dm
#  Author:  liangxiao l00350030
#  Creation Date:  2016-07-02
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
use biwarehouse;

set hive.exec.dynamic.partition=true;  
set hive.exec.dynamic.partition.mode=nonstrict;

CREATE EXTERNAL TABLE IF NOT EXISTS dw_core_up_device_mapping_dm
(
    up_id               BIGINT COMMENT 'UP帐号id',
    imei                STRING COMMENT '设备id',
    did                STRING COMMENT  '设备唯一标识',
    first_map_time      STRING COMMENT '首次绑定时间',
    update_map_time     STRING COMMENT '最后一次更新时间',
    active_days_month   INT    COMMENT '最近30天活跃天数'
)
COMMENT '帐号与设备的绑定关系表,一个设备有多个imei的情况下，通过uuid拉通'
PARTITIONED BY(pt_d STRING COMMENT '天分区')
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/AppData/BIProd/Persona/DW/dw_core_up_device_mapping_dm'
TBLPROPERTIES('orc.compress'='ZLIB');


#创建临时表，取up与imei绑定时间
drop table if exists tmp_core_up_device_mapping;
create table tmp_core_up_device_mapping as 
select
    up_id,
    imei,
    min(first_map_time) as first_map_time,
    max(update_map_time) as update_map_time,
    max(active_days_month) as active_days_month
from
    (
        select 
            user_id  as up_id,
            RevertDeviceId(DeviceIdFormat(device_id)) AS imei,
            if(!isempty(terminal_settime),terminal_settime,from_unixtime(unix_timestamp(pt_d,'yyyyMMdd'),'yyyy-MM-dd HH:mm:ss'))   AS  first_map_time,
            terminal_settime                                                                   AS  update_map_time,
            0                                                                                  AS  active_days_month        
        from 
            ods_up_user_device_info_dm 
        WHERE pt_d='$date' and IsDeviceIdLegal(device_id) and length(device_id)<17 and user_id >0 and unix_timestamp(terminal_settime)>0
        union all
        select 
             up_id,
             imei                                     AS     imei,
             min(oper_time)                           AS     first_map_time,
             max(oper_time)                           AS     update_map_time,
             0                                        AS     active_days_month
        from dw_core_all_user_oper_fact_dm t where pt_d='$date' and IsDeviceIdLegal(imei) and length(imei)<17 and pt_oper in('register','use')
        group by up_id,imei
        union all
        select up_id,imei,first_map_time,update_map_time,active_days_month from dw_core_up_device_mapping_dm where pt_d='$last_date'   
    )t
group by up_id,imei;

#创建临时表，取imei对应UUID
drop table if exists tmp_core_up_device_uuid;
create table tmp_core_up_device_uuid as
select 
      RevertDeviceId(DeviceIdFormat(imei)) as imei,
      max(did) as uuid 
  from dim_core_psi_one_imei_detail_dm 
  where pt_d='$date' and !IsEmptyUDF(did)and !IsEmptyUDF(RevertDeviceId(DeviceIdFormat(imei)))
  group by RevertDeviceId(DeviceIdFormat(imei));
  
#取up与uuid绑定关系
drop table if exists tmp_core_up_uuid_maptime;
create table tmp_core_up_uuid_maptime as
select up_id,           
        if(!isEmpty(t2.uuid),t2.uuid,t1.imei) uuid,      
        min(first_map_time) first_map_time,
        max(update_map_time) update_map_time,
        max(active_days_month) as active_days_month
from tmp_core_up_device_mapping t1
left outer join tmp_core_up_device_uuid t2 on t1.imei=t2.imei
group by t1.up_id,if(!isEmpty(t2.uuid),t2.uuid,t1.imei);
  
#31天前是否活跃
#关联UUID是设备UUID有变化的情况下，取现在的uuid
drop table if exists tmp_core_up_imei_active_31;
create table tmp_core_up_imei_active_31 as
select 
    t1.up_id,if(!isEmpty(t2.uuid),t2.uuid,t1.imei) uuid,
    max(if(to_date(update_map_time)=date_sub('$date_ep',30),1,0)) active_f
from(
select up_id,imei,update_map_time
from dw_core_up_device_mapping_dm t where pt_d=dateFormat(date_sub('$date_ep',30))
) t1
left outer join tmp_core_up_device_uuid t2 on t1.imei=t2.imei
group by t1.up_id,if(!isEmpty(t2.uuid),t2.uuid,t1.imei);

#当天是否活跃
drop table if exists tmp_core_up_imei_active_current;
create table tmp_core_up_imei_active_current as
select up_id,uuid,max(if(to_date(update_map_time)='$date_ep',1,0)) active_f
from tmp_core_up_uuid_maptime t
group by up_id,uuid;


INSERT  OVERWRITE TABLE dw_core_up_device_mapping_dm
partition(pt_d='$date')
select
    t1.up_id,t1.imei,
    if(!isEmpty(t2.uuid),t2.uuid,t1.imei)                                      uuid,
    if(!isempty(t3.first_map_time),t3.first_map_time,t1.first_map_time)        first_map_time,
    if(!isempty(t3.update_map_time),t3.update_map_time,t1.update_map_time)     update_map_time,
    t3.active_days_month-(if(t4.active_f=1,1,0))+(if(t5.active_f=1,1,0))        active_days_month
from
    (
        select 
            *
        from
            tmp_core_up_device_mapping
    )t1
left outer join tmp_core_up_device_uuid t2 on t1.imei=t2.imei
left outer join 
      tmp_core_up_uuid_maptime t3 on if(!isEmpty(t2.uuid),t2.uuid,t1.imei)=t3.uuid  and t1.up_id=t3.up_id
left outer join
      tmp_core_up_imei_active_31 t4 on if(!isEmpty(t2.uuid),t2.uuid,t1.imei)=t4.uuid  and t1.up_id=t4.up_id
left outer join
      tmp_core_up_imei_active_current t5 on if(!isEmpty(t2.uuid),t2.uuid,t1.imei)=t5.uuid  and t1.up_id=t5.up_id
;

drop table tmp_core_up_device_mapping;
drop table tmp_core_up_device_uuid;
drop table tmp_core_up_uuid_maptime;
drop table tmp_core_up_imei_active_31;
drop table tmp_core_up_imei_active_current;
"
