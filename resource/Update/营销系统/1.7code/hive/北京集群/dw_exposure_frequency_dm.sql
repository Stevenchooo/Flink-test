# --------------    --------------------------------------------------------------
#  File Name:dw_exposure_frequency_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2012.All rights reserved.
#  Purpose:   频次数据
#  Describe: 
#  Input：  ODS_VMALL_DJ_DM,ODS_VMALL_BG_DM
#  Output： dw_exposure_frequency_dm
#  Author:  s00359263
#  Creation Date:  2015-12-30
# -----------------------------------------------------------------------------
hive -e "
add jar /MFS/Share/BICommon/task/HiBI/UDF/lib/commons-io-2.0.1.jar; 
add jar /MFS/Share/BICommon/task/HiBI/UDF/lib/geoip-api-1.2.11.jar;
add jar /MFS/Share/BICommon/task/HiBI/UDF/lib/huawei_udf.jar;
add file /MFS/Share/BICommon/task/HiBI/UDF/lib/GeoLiteCity.dat;
add file /MFS/Share/BICommon/task/HiBI/UDF/lib/dim_location_cn_ds_new.dat;
add file /MFS/Share/BICommon/task/HiBI/UDF/lib/global_ip2area.dat;
add file /MFS/Share/BICommon/task/HiBI/UDF/lib/ipData2use.csv;
CREATE TEMPORARY FUNCTION GetChinaProvinceNameUDF AS 'com.huawei.platform.bi.udf.common.GetChinaProvinceNameUDF';
create temporary function isEmptyUDF as 'com.huawei.platform.bi.udf.common.IsEmptyUDF';

set hive.exec.parallel=true;
SET mapreduce.job.priority=VERY_HIGH;
set hive.exec.compress.output=false;
set mapreduce.output.fileoutputformat.compression.codec=com.hadoop.compression.lzo.LzopCodec;
CREATE EXTERNAL TABLE IF NOT EXISTS dw_exposure_frequency_dm
(
  activtiy_id             int,
  media_id                int,
  province                string,
  frequency               int,
  exp_frequency_users     BIGINT,
  click_frequency_users   BIGINT,
  landing_frequency_users BIGINT,
  area_type               int,
  user_type               int

)
PARTITIONED BY (pt_d STRING)
ROW FORMAT delimited
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/vmall/dw_exposure_frequency_dm';

CREATE TABLE IF NOT EXISTS tmp_exposure_frequency_dm
(   
    activtiy_id      INT,
    media_id         INT,
    province         STRING,
    sid              STRING,
    cookieid         STRING,
    type             STRING,
    isStable         INT
);

INSERT OVERWRITE TABLE tmp_exposure_frequency_dm
select 
    f.activtiy_id,
    f.media_id,
    f.province,
    f.sid,
    f.cookieid,
    f.type,    
    if(k.cookieid is null,0,1) as isStable
from
( 
   select
       t5.activtiy_id as activtiy_id,
       t5.media_id    as media_id,
       if(isEmptyUDF(GetChinaProvinceNameUDF(f.ip)),'未知省份',GetChinaProvinceNameUDF(f.ip)) as province,
       f.sid          as sid,
       f.cookieid     as cookieid,
       'DJ' as type
   from 
       ODS_VMALL_DJ_DM f   
   left outer join 
       dim_mkt_base_info_ds t5
   on f.sid  = t5.sid
   where 
       f.pt_d = '$date' and t5.sid is not null
       
   union all
  
   select
       t5.activtiy_id as activtiy_id,
       t5.media_id    as media_id,
       if(isEmptyUDF(GetChinaProvinceNameUDF(f.ip)),'未知省份',GetChinaProvinceNameUDF(f.ip)) as province,
       f.sid          as sid,
       f.cookieid     as cookieid,
       'BG' as type
   from 
       ODS_VMALL_BG_DM f   
   left outer join 
       dim_mkt_base_info_ds t5
   on f.sid  = t5.sid
   where 
       f.pt_d = '$date' and t5.sid is not null
       
   union all
              
   select
       t1.activtiy_id as activtiy_id,
       t1.media_id    as media_id,
       if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)) as province,
       t1.sid         as sid,
       t1.cookieid    as cookieid,
       'DD'           as type
   from
   (
       select
           f.sid as sid,
           f.cid as cid,
           f.activtiy_id as activtiy_id,
           f.media_id    as media_id,
           k.ip          as ip,
           k.cookieid    as cookieid
       from
       (
            select
                sid,
                cid,
                activtiy_id,
                media_id
            from dim_mkt_base_info_ds where cid is not null
       ) f
       join
       (
           select 
                get_json_object(cvar1,'$.1[1]') as cid,
                client_ip    as ip,
                s_id         as cookieid
           from ods_vmall_hi_data_dm
           WHERE 
                pt_d='$date' and 
                idsite in ('www.vmall.com','mm.vmall.com','sale.vmall.com','m.vmall.com','mt.vmall.com','msale.vmall.com','mw.vmall.com','ma.vmall.com','asale.vmall.com')  
                and trim(action_type) = '1' and id is not null and idvc is not null
       ) k
       on f.cid = k.cid
       
    ) t1  
    left semi join
    (
       select 
           cid, 
           count(distinct cookieid) 
       from  ODS_VMALL_DJ_DM 
       where pt_d='$date' and length(cid)<=9999999 group by sid,cid having count(distinct cookieid)<=50 
    )t3
    on t1.cid=t3.cid   
   
    union all
    
    select
       t1.activtiy_id as activtiy_id,
       t1.media_id    as media_id,
       if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)) as province,
       t1.sid         as sid,
       t2.cookieid    as cookieid,
       'DD'           as type
    from
    (
        select 
            t1.sid               as sid,
            t1.cid               as cid,
            t1.cookieid          as cookieid,
            t1.ip                as ip,
            max(t2.activtiy_id)  as activtiy_id,
            max(t2.media_id)     as media_id
        from ODS_VMALL_DJ_DM t1
        left outer join 
        dim_mkt_base_info_ds t2
        on t1.sid  = t2.sid
        where pt_d='$date' and t2.sid is not null
        group by t1.ip,t1.sid,t1.cid,t1.cookieid
    ) t1
    left outer join
    (
        select 
            s_id      as cookieid,
            client_ip as ip,
            server_time  
        from ods_vmall_hi_data_dm 
        where 
            pt_d='$date' and idsite in ('www.honor.cn','www.vmall.com','mm.vmall.com','sale.vmall.com','m.vmall.com','mt.vmall.com','msale.vmall.com','mw.vmall.com','ma.vmall.com','asale.vmall.com','consumer.huawei.com','cn.club.vmall.com') 
    ) t2
    on t1.ip = t2.ip and t1.cookieid = t2.cookieid
    left semi join
    (
        select sid, count(distinct cookieid)from  ODS_VMALL_DJ_DM where pt_d='$date'  group by sid having count(distinct cookieid)>50
    ) t3
    on t1.sid=t3.sid
    
) f
left outer join
(
     select t1.cookieid as cookieid from ODS_VMALL_DJ_DM t1
     left semi join 
     dim_mkt_base_info_ds t2
     on t1.sid  = t2.sid
     where t1.pt_d='$last_date'
     group by t1.cookieid
) k
on  f.cookieid = k.cookieid
;


insert overwrite table dw_exposure_frequency_dm
partition (pt_d='$date')
select
    fk.activtiy_id,
    fk.media_id,
    fk.province,
    fk.frequency,
    fk.exp_frequency_users,
    fk.click_frequency_users,
    fk.landing_frequency_users,
    fk.area_type,
    fk.user_type    
from
(
    select
        k.activtiy_id     as activtiy_id,
        k.media_id        as media_id,
        k.province        as province,
        k.frequency       as frequency,
        sum(k.exp_frequency_users)     as exp_frequency_users,
        sum(k.click_frequency_users)   as click_frequency_users,
        sum(k.landing_frequency_users) as landing_frequency_users,
        0 as area_type,
        0 as user_type
    from
    (
        select
            f.province        as province,
            f.activtiy_id     as activtiy_id,
            f.media_id        as media_id,
            f.frequency       as frequency,
            count(f.cookieid) as exp_frequency_users,
            cast (0 as bigint) as click_frequency_users,
            cast (0 as bigint) as landing_frequency_users
        from
        (
            select
                activtiy_id,
                media_id,
                province,
                cookieid,
                count(1) as frequency
            from tmp_exposure_frequency_dm where type='BG'
            group by province,activtiy_id,media_id,cookieid
        ) f
        group by f.province,f.activtiy_id,f.media_id,f.frequency
        
        union all
        
        select
            f.province        as province,
            f.activtiy_id     as activtiy_id,
            f.media_id        as media_id,
            f.frequency       as frequency,
            cast (0 as bigint) as exp_frequency_users,
            count(f.cookieid)  as click_frequency_users,
            cast (0 as bigint) as landing_frequency_users
        from
        (
            select
                activtiy_id,
                media_id,
                province,
                cookieid,
                count(1) as frequency
            from tmp_exposure_frequency_dm where type='DJ'
            group by province,activtiy_id,media_id,cookieid
        ) f
        group by f.province,f.activtiy_id,f.media_id,f.frequency
        
        
        union all
        
        select
            f.province        as province,
            f.activtiy_id     as activtiy_id,
            f.media_id        as media_id,
            f.frequency       as frequency,
            cast (0 as bigint) as exp_frequency_users,
            cast (0 as bigint) as click_frequency_users,
            count(f.cookieid)  as landing_frequency_users
        from
        (
            select
                activtiy_id,
                media_id,
                province,
                cookieid,
                count(1) as frequency
            from tmp_exposure_frequency_dm where type='DD'
            group by province,activtiy_id,media_id,cookieid
        ) f
        group by f.province,f.activtiy_id,f.media_id,f.frequency
    ) k
    group by k.province,k.activtiy_id,k.media_id,k.frequency
    
    union all
    
    select
        k.activtiy_id     as activtiy_id,
        k.media_id        as media_id,
        k.province        as province,
        k.frequency       as frequency,
        sum(k.exp_frequency_users)     as exp_frequency_users,
        sum(k.click_frequency_users)   as click_frequency_users,
        sum(k.landing_frequency_users) as landing_frequency_users,
        0 as area_type,
        1 as user_type
    from
    (        
        select
            f.province        as province,
            f.activtiy_id     as activtiy_id,
            f.media_id        as media_id,
            f.frequency       as frequency,
            cast (0 as bigint) as exp_frequency_users,
            count(f.cookieid)  as click_frequency_users,
            cast (0 as bigint) as landing_frequency_users
        from
        (
            select
                activtiy_id,
                media_id,
                province,
                cookieid,
                count(1) as frequency
            from tmp_exposure_frequency_dm where type='DJ' and isStable = 1
            group by province,activtiy_id,media_id,cookieid
        ) f
        group by f.province,f.activtiy_id,f.media_id,f.frequency
        
        
        union all
        
        select
            f.province        as province,
            f.activtiy_id     as activtiy_id,
            f.media_id        as media_id,
            f.frequency       as frequency,
            cast (0 as bigint) as exp_frequency_users,
            cast (0 as bigint) as click_frequency_users,
            count(f.cookieid)  as landing_frequency_users
        from
        (
            select
                activtiy_id,
                media_id,
                province,
                cookieid,
                count(1) as frequency
            from tmp_exposure_frequency_dm where type='DD'  and isStable = 1
            group by province,activtiy_id,media_id,cookieid
        ) f
        group by f.province,f.activtiy_id,f.media_id,f.frequency
    ) k    
    group by k.province,k.activtiy_id,k.media_id,k.frequency
    
) fk;

drop table tmp_exposure_frequency_dm;
"
