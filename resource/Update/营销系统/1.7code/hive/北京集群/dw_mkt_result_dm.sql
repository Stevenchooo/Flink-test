# --------------    --------------------------------------------------------------
#  File Name:dw_mkt_result_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2012.All rights reserved.
#  Purpose: 总体概况的数据
#  Describe: 
#  Input：  ODS_VMALL_DJ_DM,ODS_VMALL_BG_DM,ods_vmall_hi_data_dm
#  Output： dw_mkt_result_dm
#  Author:  s00359263
#  Creation Date:  2015-12-29
# -----------------------------------------------------------------------------
hive -e "
add jar /MFS/Share/BICommon/task/HiBI/UDF/lib/commons-io-2.0.1.jar; 
add jar /MFS/Share/BICommon/task/HiBI/UDF/lib/geoip-api-1.2.11.jar;
add jar /MFS/Share/BICommon/task/HiBI/UDF/lib/huawei_udf.jar;
add file /MFS/Share/BICommon/task/HiBI/UDF/lib/GeoLiteCity.dat;
add file /MFS/Share/BICommon/task/HiBI/UDF/lib/dim_location_cn_ds_new.dat;
add file /MFS/Share/BICommon/task/HiBI/UDF/lib/global_ip2area.dat;
add file /MFS/Share/BICommon/task/HiBI/UDF/lib/ipData2use.csv;


create temporary function GetAreaInfoUDF  AS 'com.huawei.platform.bi.udf.common.GetAreaInfoUDF';
CREATE TEMPORARY FUNCTION GetChinaProvinceNameUDF AS 'com.huawei.platform.bi.udf.common.GetChinaProvinceNameUDF';
create temporary function isEmptyUDF as 'com.huawei.platform.bi.udf.common.IsEmptyUDF';

set hive.exec.parallel=true;
SET mapreduce.job.priority=VERY_HIGH;
set hive.exec.compress.output=false;
set mapreduce.output.fileoutputformat.compression.codec=com.hadoop.compression.lzo.LzopCodec;
CREATE EXTERNAL TABLE IF NOT EXISTS dw_mkt_result_dm
(
  sid          string,
  province     string,
  city         string,
  hour         string,
  bg_pv       int,
  bg_uv       int,
  dj_pv       int,
  dj_uv       int,
  landing_pv  int,
  landing_uv  int,
  prom_users  int,
  pay_users   int,
  area_type   int,
  user_type   int
)
PARTITIONED BY (pt_d STRING)
ROW FORMAT delimited
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/vmall/dw_mkt_result_dm';

CREATE TABLE IF NOT EXISTS tmp_mktsystem_app_data_dm
(   
    ip               STRING,
    sid              STRING,
    cid              STRING,
    clicktime        STRING,
    land_time        STRING,
    cookieid         STRING,
    isStable         INT
);

INSERT OVERWRITE TABLE tmp_mktsystem_app_data_dm
select 
    f.ip,
    f.sid,
    f.cid,
    f.clicktime,
    f.land_time,
    f.cookieid,    
    if(k.cookieid is null,0,1) as isStable
from
( 
   select
       f.ip,
       f.sid,
       cast(f.cid as int) as cid,
       f.clicktime,
       cookieid,
       'NA' as land_time
   from 
       ODS_VMALL_DJ_DM f   
   left semi join 
       dim_mkt_base_info_ds t5
   on f.sid  = t5.sid
   where 
       f.pt_d = '$date'
       
   union all
      
   select
       t1.ip          as ip,
       t1.sid         as sid,
       cast(t1.cid as int) as cid,
       'NA'           as clicktime,
       t1.cookieid    as cookieid,
       t1.server_time as land_time
   from
   (
       select
           f.sid           as sid,
           k.cid           as cid,
           k.ip            as ip,
           k.server_time   as server_time,
           k.cookieid      as cookieid
       from
       (
            select
                sid,
                cid
            from dim_mkt_base_info_ds
       ) f
       join
       (
           select 
                get_json_object(cvar1,'$.1[1]') as cid,
                client_ip    as ip,
                s_id         as cookieid,
                server_time
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
        t2.ip          as ip,
        t1.sid         as sid,
        cast(t1.cid as int) as cid,
        'NA'           as clicktime,
        t2.cookieid    as cookieid,
        t2.server_time as land_time
    from
    (
        select sid,cid ,cookieid,ip
        from ODS_VMALL_DJ_DM t1
        left semi join 
        dim_mkt_base_info_ds t2
        on t1.sid  = t2.sid
        where pt_d='$date'
        group by ip,sid,cid,cookieid
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
     select cookieid from ODS_VMALL_DJ_DM where pt_d='$last_date'
     group by cookieid
) k
on  f.cookieid = k.cookieid;

insert overwrite table dw_mkt_result_dm
partition (pt_d='$date')
select
fk.sid,
     fk.province,   
     fk.city,
     fk.hour,        
     fk.bg_pv,      
     fk.bg_uv,      
     fk.dj_pv,      
     fk.dj_uv,      
     fk.landing_pv, 
     fk.landing_uv, 
     0,
     0,                   
     fk.area_type,  
     fk.user_type
from
(
    
    -- 省份、所有网民、天维度
    select
        f.province         as province,  
        f.sid              as sid,      
        sum(f.bg_pv)       as bg_pv,
        sum(f.bg_uv)       as bg_uv,    
        sum(f.dj_pv)       as dj_pv,     
        sum(f.dj_uv)       as dj_uv,     
        sum(f.landing_pv)  as landing_pv,
        sum(f.landing_uv)  as landing_uv,
        'NA'               as hour,
        'NA'               as city,
        '0'                as area_type,
        '0'                as user_type
    from     
    (    
        select 
            if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)) as province,
            t1.sid,
            count(*) as bg_pv,
            count(distinct t1.ip,t1.cookieid) as bg_uv,
            cast (0 as bigint) as dj_pv,
            cast (0 as bigint) as dj_uv,
            cast (0 as bigint) as landing_pv ,
            cast (0 as bigint) as landing_uv
        from ODS_VMALL_BG_DM t1
        left outer join 
        dim_mkt_base_info_ds t2
        on t1.sid  = t2.sid
        where t1.pt_d='$date' and t2.sid is not null
        group by if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)),t1.sid
        
        union all
        
        select
            k.province   as province,  
            k.sid        as sid,      
            cast (0 as bigint)      as bg_pv,
            cast (0 as bigint)      as bg_uv,    
            k.dj_pv      as dj_pv,     
            k.dj_uv      as dj_uv,     
            k.landing_pv as landing_pv,
            k.landing_uv as landing_uv
        from 
        (
            select 
                if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)) as province,
                t1.sid              as  sid,
                max(t1.cid)         as  cid,
                sum(t1.dj_pv)       as  dj_pv,
                sum(t1.dj_uv)       as  dj_uv,    
                sum(t1.landing_pv)  as  landing_pv,
                sum(t1.landing_uv)  as  landing_uv
                    
            from 
            ( 
                select ip,
                       sid,
                       cast(cid as int) as cid,
                       count(*) as dj_pv,
                       count(distinct ip,cookieid) as dj_uv,
                       cast (0 as bigint) as landing_pv ,
                       cast (0 as bigint) as landing_uv 
                from tmp_mktsystem_app_data_dm where  land_time='NA' 
                group by ip,sid,cid
                
                union all
                
                select ip,
                       sid,
                       cast(cid as int) as cid,
                       cast (0 as bigint) as dj_pv,
                       cast (0 as bigint) as dj_uv,
                       count (cookieid) as landing_pv ,
                       count (distinct ip,cookieid) as landing_uv 
                from tmp_mktsystem_app_data_dm where  clicktime='NA'
                group by ip,sid,cid
            ) t1
            group by if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)),t1.sid
        ) k
    )  f
    group by f.province,f.sid  
     
    union all
    
    -- 地市、所有网民、天维度
    
    select
        'NA'               as province,  
        f.sid              as sid,      
        sum(f.bg_pv)       as bg_pv,
        sum(f.bg_uv)       as bg_uv,    
        sum(f.dj_pv)       as dj_pv,     
        sum(f.dj_uv)       as dj_uv,     
        sum(f.landing_pv)  as landing_pv,
        sum(f.landing_uv)  as landing_uv,
        'NA'               as hour,
        f.city             as city,
        '0'                as area_type,
        '0'                as user_type
    from     
    (    
        select 
            if(k.city is null,'未知地市',k.city) as city,
            f.sid    as sid,
            count(*) as bg_pv,
            count(distinct f.cookieid) as bg_uv,
            cast (0 as bigint) as dj_pv,
            cast (0 as bigint) as dj_uv,
            cast (0 as bigint) as landing_pv ,
            cast (0 as bigint) as landing_uv
        from
        (
            select 
               CAST(SUBSTR(GetAreaInfoUDF(t1.ip,1),1,9) AS STRING) AS area_id,
               t1.sid,
               t1.cookieid
               
            from ODS_VMALL_BG_DM t1
            left outer join 
               dim_mkt_base_info_ds t2
            on t1.sid  = t2.sid
            where t1.pt_d='$date' and t2.sid is not null
        ) f
        left outer join
        (
            SELECT CAST(SUBSTR(area_id, 1, 9) AS STRING) AS area_id,city
            FROM dim_location_cn_ds_new WHERE area_level = 3
        ) k
        on f.area_id = k.area_id
        group by if(k.city is null,'未知地市',k.city),f.sid
    
        union all
        
        select
            k.city,  
            k.sid ,      
            k.bg_pv,
            k.bg_uv,    
            k.dj_pv,     
            k.dj_uv,     
            k.landing_pv,
            k.landing_uv
        from 
        (
            select 
                t2.city    as city,
                t2.sid         as sid,
                cast (0 as bigint) as bg_pv,
                cast (0 as bigint) as bg_uv,              
                t2.dj_pv       as dj_pv,
                t2.dj_uv       as dj_uv,
                t2.landing_pv  as landing_pv,
                t2.landing_uv  as landing_uv 
            from 
            (
                select 
                    if(k.city is null,'未知地市',k.city) as city,
                    t1.sid              as  sid,
                    max(t1.cid)         as  cid,
                    sum(t1.dj_pv)       as  dj_pv,
                    sum(t1.dj_uv)       as  dj_uv,    
                    sum(t1.landing_pv)  as  landing_pv,
                    sum(t1.landing_uv)  as  landing_uv
                    
                from 
                ( 
                    select CAST(SUBSTR(GetAreaInfoUDF(ip,1),1,9) AS STRING) AS area_id,
                           sid,
                           cast(cid as int) as cid,
                           count(*) as dj_pv,
                           count(distinct ip,cookieid) as dj_uv,
                           cast (0 as bigint) as landing_pv ,
                           cast (0 as bigint) as landing_uv 
                    from tmp_mktsystem_app_data_dm where  land_time='NA' 
                    group by ip,sid,cid
                    
                    union all
                    
                    select CAST(SUBSTR(GetAreaInfoUDF(ip,1),1,9) AS STRING) AS area_id,
                           sid,
                           cast(cid as int) as cid,
                           cast (0 as bigint) as dj_pv,
                           cast (0 as bigint) as dj_uv,
                           count (cookieid) as landing_pv ,
                           count (distinct ip,cookieid) as landing_uv 
                    from tmp_mktsystem_app_data_dm where  clicktime='NA'
                    group by ip,sid,cid
                ) t1
                left outer join
                (
                    SELECT CAST(SUBSTR(area_id, 1, 9) AS STRING) AS area_id,city
                    FROM dim_location_cn_ds_new WHERE area_level = 3
                ) k
                on t1.area_id = k.area_id
                group by if(k.city is null,'未知地市',k.city),t1.sid
            ) t2
        ) k
    )  f
    group by f.city,f.sid
    
    union all
    
    -- 省份、所有网民、小时维度
    select
        f.province         as province,  
        f.sid              as sid,      
        sum(f.bg_pv)       as bg_pv,
        sum(f.bg_uv)       as bg_uv,    
        sum(f.dj_pv)       as dj_pv,     
        sum(f.dj_uv)       as dj_uv,     
        sum(f.landing_pv)  as landing_pv,
        sum(f.landing_uv)  as landing_uv,
        cast(f.hour as STRING )            as hour,
        'NA'               as city,
        '0'                as area_type,
        '0'                as user_type
    from     
    (    
        select 
            if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)) as province,
            t1.sid,
            count(*) as bg_pv,
            count(distinct t1.ip,t1.cookieid) as bg_uv,
            cast (0 as bigint) as dj_pv,
            cast (0 as bigint) as dj_uv,
            cast (0 as bigint) as landing_pv ,
            cast (0 as bigint) as landing_uv,
            if(hour(exposuretime)<10,concat('0',hour(exposuretime)),hour(exposuretime)) as hour
        from ODS_VMALL_BG_DM t1
        left outer join 
        dim_mkt_base_info_ds t2
        on t1.sid  = t2.sid
        where t1.pt_d='$date' and t2.sid is not null
        group by if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)),hour(exposuretime),t1.sid
        
        union all
        
        select
            k.province   as province,  
            k.sid        as sid,      
            cast (0 as bigint)      as bg_pv,
            cast (0 as bigint)      as bg_uv,    
            k.dj_pv      as dj_pv,     
            k.dj_uv      as dj_uv,     
            k.landing_pv as landing_pv,
            k.landing_uv as landing_uv,
            k.hour       as hour
        from 
        (
            select 
                if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)) as province,
                t1.sid              as  sid,
                max(t1.cid)         as  cid,
                sum(t1.dj_pv)       as  dj_pv,
                sum(t1.dj_uv)       as  dj_uv,    
                sum(t1.landing_pv)  as  landing_pv,
                sum(t1.landing_uv)  as  landing_uv,
                t1.hour             as  hour
                    
            from 
            ( 
                select ip,
                       sid,
                       cast(cid as int) as cid,
                       count(*) as dj_pv,
                       count(distinct ip,cookieid) as dj_uv,
                       cast (0 as bigint) as landing_pv ,
                       cast (0 as bigint) as landing_uv ,
                       if(hour(clicktime)<10,concat('0',hour(clicktime)),hour(clicktime)) as hour
                from tmp_mktsystem_app_data_dm where  land_time='NA' 
                group by ip,hour(clicktime),sid,cid
                
                union all
                
                select ip,
                       sid,
                       cast(cid as int) as cid,
                       cast (0 as bigint) as dj_pv,
                       cast (0 as bigint) as dj_uv,
                       count (cookieid) as landing_pv ,
                       count (distinct ip,cookieid) as landing_uv,
                       if(hour(land_time)<10,concat('0',hour(land_time)),hour(land_time)) as hour
                from tmp_mktsystem_app_data_dm where  clicktime='NA'
                group by ip,hour(land_time),sid,cid
            ) t1
            group by if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)),t1.hour,t1.sid
        ) k
    )  f
    group by f.province,f.hour,f.sid 
    
    union all
    
    -- 省份、稳定人群、天维度
       
    select 
        t2.province        as province,
        t2.sid             as sid,
        cast (0 as bigint) as bg_pv,
        cast (0 as bigint) as bg_uv,              
        t2.dj_pv           as dj_pv,
        t2.dj_uv           as dj_uv,
        t2.landing_pv      as landing_pv,
        t2.landing_uv      as landing_uv,
        'NA'               as hour,
        'NA'               as city,
        '0'                as area_type,
        '1'                as user_type 
    from 
    (
        select 
            if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)) as province,
            t1.sid              as  sid,
            max(t1.cid)         as  cid,
            sum(t1.dj_pv)       as  dj_pv,
            sum(t1.dj_uv)       as  dj_uv,    
            sum(t1.landing_pv)  as  landing_pv,
            sum(t1.landing_uv)  as  landing_uv
                    
        from 
        ( 
            select ip,
                sid,
                cast(cid as int) as cid,
                count(*) as dj_pv,
                count(distinct ip,cookieid) as dj_uv,
                cast (0 as bigint) as landing_pv ,
                cast (0 as bigint) as landing_uv 
            from tmp_mktsystem_app_data_dm where  land_time='NA' and  isStable = 1
            group by ip,sid,cid
                    
            union all
                    
            select ip,
                sid,
                cast(cid as int) as cid,
                cast (0 as bigint) as dj_pv,
                cast (0 as bigint) as dj_uv,
                count (cookieid) as landing_pv ,
                count (distinct ip,cookieid) as landing_uv 
            from tmp_mktsystem_app_data_dm where  clicktime='NA' and  isStable = 1
            group by ip,sid,cid
        ) t1
        group by if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)),t1.sid
    ) t2
        
    -- 省份、稳定人群、小时维度
    union all
     
    select 
        t2.province        as province,
        t2.sid             as sid,
        cast (0 as bigint) as bg_pv,
        cast (0 as bigint) as bg_uv,              
        t2.dj_pv           as dj_pv,
        t2.dj_uv           as dj_uv,
        t2.landing_pv      as landing_pv,
        t2.landing_uv      as landing_uv,
        cast(t2.hour as STRING )            as hour,
        'NA'               as city,
        '0'                as area_type,
        '1'                as user_type 
    from 
    (
        select 
            if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)) as province,
            t1.sid              as  sid,
            max(t1.cid)         as  cid,
            sum(t1.dj_pv)       as  dj_pv,
            sum(t1.dj_uv)       as  dj_uv,    
            sum(t1.landing_pv)  as  landing_pv,
            sum(t1.landing_uv)  as  landing_uv,
            t1.hour             as  hour
                    
        from 
        ( 
            select ip,
                sid,
                cast(cid as int) as cid,
                count(*) as dj_pv,
                count(distinct ip,cookieid) as dj_uv,
                cast (0 as bigint) as landing_pv ,
                cast (0 as bigint) as landing_uv,
                if(hour(clicktime)<10,concat('0',hour(clicktime)),hour(clicktime)) as hour 
            from tmp_mktsystem_app_data_dm where  land_time='NA' and  isStable = 1
            group by ip,hour(clicktime),sid,cid
                    
            union all
                    
            select ip,
                sid,
                cast(cid as int) as cid,
                cast (0 as bigint) as dj_pv,
                cast (0 as bigint) as dj_uv,
                count (cookieid) as landing_pv ,
                count (distinct ip,cookieid) as landing_uv,
                if(hour(land_time)<10,concat('0',hour(land_time)),hour(land_time)) as hour 
            from tmp_mktsystem_app_data_dm where  clicktime='NA' and  isStable = 1
            group by ip,hour(land_time),sid,cid
        ) t1
        group by if(isEmptyUDF(GetChinaProvinceNameUDF(t1.ip)),'未知省份',GetChinaProvinceNameUDF(t1.ip)),t1.hour,t1.sid
    ) t2
) fk;

drop table tmp_mktsystem_app_data_dm;
"
