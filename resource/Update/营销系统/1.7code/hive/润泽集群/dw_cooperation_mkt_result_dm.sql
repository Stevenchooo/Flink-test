# ----------------------------------------------------------------------------
#  File Name:dw_cooperation_mkt_result_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2012.All rights reserved.
#  Purpose: 
#  Describe:营销系统第三方数据源人群
#  Input：  
#  Output： 
#  Author: w84057406
#  Creation Date:  2015-05-5
#  Last Modified:  
#  Last Modified:  
# ----------------------------------------------------------------------------   
hive -e "
set mapreduce.job.queuename=QueueA;
set hive.exec.compress.output=true;
set mapreduce.output.fileoutputformat.compress.codec=com.hadoop.compression.lzo.LzopCodec;
use biwarehouse;
CREATE EXTERNAL TABLE IF NOT EXISTS dw_cooperation_mkt_result_dm(
    activtiy_id   INT,
    activity_name STRING,
    material_type String,
    media_name    STRING,
    landing_plate STRING,
    media_type    STRING,
    dim_value     STRING,
    cookies       STRING,
    pt_dim        STRING
)
PARTITIONED BY(pt_d STRING COMMENT 'DATE_PARTITION (YYYYMMDD)')
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n'
STORED AS INPUTFORMAT 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'
          OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION '/AppData/EUIProd/TCSM/data/DW/TCSM/dw_cooperation_mkt_result_dm';

drop table if EXISTS tmp_cooperation_mkt_tag;
create table tmp_cooperation_mkt_tag as
select 
    t3.activtiy_id as activtiy_id,
    t3.activity_name as activity_name,
    t3.material_type as material_type,
    t3.media_name as media_name,
    t3.landing_plate as landing_plate,
    t3.media_type as media_type,
    t1.cookie as cookie,
    t1.tagid  as tagid 
from 
    (select * from dim_cooperation_cookie_ds) t
join 
    (select a.cookie as cookie,b.tagid as tagid from ODS_COOPERATION_DWR_AD_TAG_DM a lateral view explode(split(tagid,',')) b as  tagid where pt_d='$date') t1
on t.cookieid=t1.cookie 
join 
    (select * from ODS_VMALL_DJ_DM where pt_d='$date') t2
on t.cookie=t2.cookieid
join 
    dim_mkt_base_info_ds t3
on t2.sid=t3.sid;


INSERT OVERWRITE TABLE dw_cooperation_mkt_result_dm
PARTITION (pt_d = '$date')

select 
f.activtiy_id as activtiy_id,
f.activity_name as activity_name,
f.material_type as material_type,
f.media_name as media_name,
f.landing_plate as landing_plate,
f.media_type as media_type,
f.dim_value as dim_value,
count(distinct f.cookies) as cookies,
f.pt_dim as pt_dim
from 
(
    select 
    tt.activtiy_id as activtiy_id,
    tt.activity_name as activity_name,
    tt.material_type as material_type,
    tt.media_name as media_name,
    tt.landing_plate as landing_plate,
    tt.media_type as media_type,
    t2.child_name as dim_value,
    tt.cookie as cookies,
    'gender' as pt_dim
    from
        tmp_cooperation_mkt_tag tt
    join 
        (select * from dim_COOPERATION_persontype_ds where type='性别') t2
    on tt.tagid=t2.id

union all

    select 
    tt.activtiy_id as activtiy_id,
    tt.activity_name as activity_name,
    tt.material_type as material_type,
    tt.media_name as media_name,
    tt.landing_plate as landing_plate,
    tt.media_type as media_type,
    t2.child_name as dim_value,
    tt.cookie as cookies,
    'age' as pt_dim
    from
        tmp_cooperation_mkt_tag tt
    join 
        (select * from dim_COOPERATION_persontype_ds where type='年龄') t2
    on tt.tagid=t2.id
    
union all

   
    select 
    tt.activtiy_id as activtiy_id,
    tt.activity_name as activity_name,
    tt.material_type as material_type,
    tt.media_name as media_name,
    tt.landing_plate as landing_plate,
    tt.media_type as media_type,
    t2.type as dim_value,
    tt.cookie as cookies,
    'media' as pt_dim
    from
        tmp_cooperation_mkt_tag tt
    join 
        (select * from dim_COOPERATION_persontype_ds where category='媒体轨迹') t2
    on tt.tagid=t2.id  
    
    
union all

    select 
    tt.activtiy_id as activtiy_id,
    tt.activity_name as activity_name,
    tt.material_type as material_type,
    tt.media_name as media_name,
    tt.landing_plate as landing_plate,
    tt.media_type as media_type,
    t2.type as dim_value,
    tt.cookie as cookies,
    'industry' as pt_dim
    from
        tmp_cooperation_mkt_tag tt
    join 
        (select * from dim_COOPERATION_persontype_ds where category='行业倾向') t2
    on tt.tagid=t2.id 
) f
group by f.activtiy_id ,
f.activity_name,
f.material_type ,
f.media_name ,
f.landing_plate ,
f.media_type,
f.dim_value,
f.pt_dim
"
