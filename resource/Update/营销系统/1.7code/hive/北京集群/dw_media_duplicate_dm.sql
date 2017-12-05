# --------------    --------------------------------------------------------------
#  File Name:dw_media_duplicate_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2012.All rights reserved.
#  Purpose: 媒体重合度信息
#  Describe: 
#  Input：  ODS_VMALL_DJ_HM
#  Output： dw_media_duplicate_dm
#  Author:  s00359263
#  Creation Date:  2015-12-31
# -----------------------------------------------------------------------------
hive -e "


set hive.exec.parallel=true;
SET mapreduce.job.priority=VERY_HIGH;
set hive.exec.compress.output=false;
set mapreduce.output.fileoutputformat.compression.codec=com.hadoop.compression.lzo.LzopCodec;
CREATE EXTERNAL TABLE IF NOT EXISTS dw_media_duplicate_dm
(
  activtiy_id             int,
  media_id                int,
  secondary_media_id      int,
  target_type             int,
  duplicate_users         BIGINT,
  area_type               int,
  user_type               int

)
PARTITIONED BY (pt_d STRING)
ROW FORMAT delimited
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/vmall/dw_media_duplicate_dm';


insert overwrite table dw_media_duplicate_dm
partition (pt_d='$date')    
select
    fk.activtiy_id,
    fk.main_media_id,
    fk.secondary_media_id,
    fk.target_type,
    fk.duplicate_users,
    fk.area_type,
    fk.user_type
from
(
    -- 媒体总重合度
    select 
        t1.activtiy_id   as activtiy_id,
        t1.media_id      as main_media_id,
        -999             as secondary_media_id,
        1                as target_type,
        count(distinct if(t2.cookieid is not null,t1.cookieid,null)) as duplicate_users,
        0                as area_type,
        0                as user_type
    from
    (
        select t1.activtiy_id,t1.media_id,t2.cookieid
        from
        (
            select * from dim_mkt_base_info_ds 
        )t1
        left outer join
        (
            select * from ods_vmall_dj_dm where pt_d='$date'
        )t2
        on t1.sid=t2.sid
        group by t1.activtiy_id,t1.media_id,t2.cookieid
    )t1
    left outer join
    (
        select 
            t1.activtiy_id as activtiy_id,
            t1.media_id    as media_id,
            t2.cookieid    as cookieid
        from
        (
            select * from dim_mkt_base_info_ds 
        )t1
        left outer join
        (
            select * from ods_vmall_dj_dm where pt_d='$date'
        )t2
        on t1.sid=t2.sid
        group by t1.activtiy_id,t1.media_id,t2.cookieid
    )t2
    on t1.activtiy_id = t2.activtiy_id and  t1.cookieid = t2.cookieid 
    where t1.media_id != t2.media_id
    group by t1.activtiy_id,t1.media_id
    
    union all
    
    -- 媒体两两重合度
    select
        t1.activtiy_id   as activtiy_id,
        t1.media_id      as main_media_id,
        t2.media_id      as secondary_media_id,
        1                as target_type,
        count(distinct if(t2.cookieid is not null,t1.cookieid,null)) as duplicate_users,
        0                as area_type,
        0                as user_type    
    from
    (
        select t1.activtiy_id as activtiy_id,t1.media_id as media_id,t2.cookieid as cookieid
        from
        (
            select * from dim_mkt_base_info_ds
        )t1
        left outer join
        (
            select * from ods_vmall_dj_dm where pt_d='$date'
        )t2
        on t1.sid=t2.sid
        group by t1.activtiy_id,t1.media_id,t2.cookieid
    )t1
    left outer join
    (
        select 
            t1.activtiy_id as activtiy_id,
            t1.media_id    as media_id,
            t2.cookieid    as cookieid
        from
        (
            select * from dim_mkt_base_info_ds 
        )t1
        left outer join
        (
            select * from ods_vmall_dj_dm where pt_d='$date'
        )t2
        on t1.sid=t2.sid
        group by t1.activtiy_id,t1.media_id,t2.cookieid
    )t2
    on t1.activtiy_id = t2.activtiy_id  and t1.cookieid = t2.cookieid
    group by t1.activtiy_id,t1.media_id,t2.media_id
    
    union all
    
    -- 媒体总重合度
    
    select 
        t1.activtiy_id   as activtiy_id,
        t1.media_id      as main_media_id,
        -999      as secondary_media_id,
        1                as target_type,
        count(distinct if(t2.cookieid is not null,t1.cookieid,null)) as duplicate_users,
        0                as area_type,
        1                as user_type 
    from
    (
        select t1.activtiy_id,t1.media_id,t2.cookieid
        from
        (
            select * from dim_mkt_base_info_ds 
        )t1
        left outer join
        (
            select * from ods_vmall_dj_dm where pt_d='$date'
        )t2
        on t1.sid=t2.sid
        group by t1.activtiy_id,t1.media_id,t2.cookieid
    )t1
    left outer join
    (
        select 
            t1.activtiy_id as activtiy_id,
            t1.media_id    as media_id,
            t2.cookieid    as cookieid
        from
        (
            select * from dim_mkt_base_info_ds 
        )t1
        left outer join
        (
            select * from ods_vmall_dj_dm where pt_d='$date'
        )t2
        on t1.sid=t2.sid
        group by t1.activtiy_id,t1.media_id,t2.cookieid
    )t2
    on t1.activtiy_id = t2.activtiy_id and  t1.cookieid = t2.cookieid 
    left outer join
    (
        select * from ods_vmall_dj_dm where pt_d='$last_date'
    ) t3
    on t1.cookieid = t3.cookieid 
    where t1.media_id != t2.media_id and t3.cookieid is not null
    group by t1.activtiy_id,t1.media_id
    
    union all
    
    -- 媒体两两重合度
    select
        t1.activtiy_id   as activtiy_id,
        t1.media_id      as main_media_id,
        t2.media_id      as secondary_media_id,
        1                as target_type,
        count(distinct if(t2.cookieid is not null,t1.cookieid,null)) as duplicate_users,
        0                as area_type,
        1                as user_type 
    from
    (
        select t1.activtiy_id as activtiy_id,t1.media_id as media_id,t2.cookieid as cookieid
        from
        (
            select * from dim_mkt_base_info_ds
        )t1
        left outer join
        (
            select * from ods_vmall_dj_dm where pt_d='$date'
        )t2
        on t1.sid=t2.sid
        group by t1.activtiy_id,t1.media_id,t2.cookieid
    )t1
    left outer join
    (
        select 
            t1.activtiy_id as activtiy_id,
            t1.media_id    as media_id,
            t2.cookieid    as cookieid
        from
        (
            select * from dim_mkt_base_info_ds 
        )t1
        left outer join
        (
            select * from ods_vmall_dj_dm where pt_d='$date'
        )t2
        on t1.sid=t2.sid
        group by t1.activtiy_id,t1.media_id,t2.cookieid
    )t2
    on t1.activtiy_id = t2.activtiy_id and t1.cookieid = t2.cookieid
    left outer join
    (
        select * from ods_vmall_dj_dm where pt_d='$last_date'
    ) t3
    on t1.cookieid = t3.cookieid 
    where t3.cookieid is not null
    group by t1.activtiy_id,t1.media_id,t2.media_id
) fk
where fk.secondary_media_id is not null;
"
