# --------------    --------------------------------------------------------------
#  File Name:dw_honor_marketing_result_hm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2012.All rights reserved.
#  Purpose: Glory official website marketing results statistics
#  Describe: 
#  Input：  ODS_VMALL_DJ_HM,ODS_VMALL_BG_HM,ODS_VMALL_HI_DATA_IM,ods_cooperation_dwr_ad_exposure_dm,ods_cooperation_dwr_ad_click_dm
#  Output： dw_marketing_result_hm
#  Author:  s00191231
#  Creation Date:  2015-03-30
#  Last Modified:  2016-12-20 cwx376287 Added third party push over click, exposure data; clicks less than 50 calculated landing increased Sid Association
# -----------------------------------------------------------------------------
hive -e "
set mapred.job.priority=HIGH;
set hive.exec.parallel=true;
set hive.exec.compress.output=false;
set mapred.output.compression.codec=com.hadoop.compression.lzo.LzopCodec;
use biwarehouse;
CREATE EXTERNAL TABLE IF NOT EXISTS dw_honor_marketing_result_hm
(
  pt_h      string,
  sid       string,
  cid         int,
  bg_pv       int,
  bg_uv       int,
  dj_pv       int,
  dj_uv       int,
  landing_pv  int,
  landing_uv  int,
  land_rate  string,
  bespeak_nums  int,
  transform_rate     string,
  buy_nums          int,
  buy_trans_rate   string,
  avg_access         int,
  isdsp              int
)
PARTITIONED BY (pt_d STRING)
ROW FORMAT delimited
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS RCFILE
LOCATION '$hadoopuser/data/DW/vmall/dw_honor_marketing_result_hm';

drop table if exists tmp_bg_dj_ds;
create table tmp_bg_dj_ds as
select
    'NA' as pt_h,    
    sid,
    cast(cid as int) as cid,
    count(*) as bg_pv,
    count(distinct cookieid) as bg_uv,
    cast (0 as bigint) as dj_pv,
    cast (0 as bigint) as dj_uv,
    cast (0 as bigint) as landing_pv ,
    cast (0 as bigint) as landing_uv,
    cast (0 as double) as avg_access 
from 
    ODS_VMALL_BG_DM 
where 
    pt_d='$date'
group by 
    sid,cast(cid as int)   

union all
select
    'NA' as pt_h,    
    sid,
    cast(cid as int) as cid,
    count(*) as bg_pv,
    count(distinct cookie) as bg_uv,
    cast (0 as bigint) as dj_pv,
    cast (0 as bigint) as dj_uv,
    cast (0 as bigint) as landing_pv ,
    cast (0 as bigint) as landing_uv,
    cast (0 as double) as avg_access 
from 
    (
    select 
        b.sid as sid,
        b.cid as cid,
        cookie 
    from ods_cooperation_dwr_ad_exposure_dm
    LATERAL VIEW parse_url_tuple(regexp_replace(UrlDecoderUDF(landing_url),'#','?'), 'QUERY:sid', 'QUERY:cid') b as sid,cid
    where pt_d='$date'
    ) t 
 where !IsEmptyUDF(sid) and !IsEmptyUDF(cid)
group by 
    sid,cast(cid as int)
union all

select
    'NA' as pt_h,      
    sid,
    cast(cid as int) as cid,
    cast (0 as bigint) as bg_pv,
    cast (0 as bigint) as bg_uv,
    count(*) as dj_pv,
    count(distinct cookieid) as dj_uv,
    cast (0 as bigint) as landing_pv ,
    cast (0 as bigint) as landing_uv,
    cast (0 as double) as avg_access       
from 
    ODS_VMALL_DJ_DM 
where 
    pt_d='$date'
 group by 
    sid,cast(cid as int)

union all
select
    'NA' as pt_h,      
    sid,
    cast(cid as int) as cid,
    cast (0 as bigint) as bg_pv,
    cast (0 as bigint) as bg_uv,
    count(*) as dj_pv,
    count(distinct cookie) as dj_uv,
    cast (0 as bigint) as landing_pv ,
    cast (0 as bigint) as landing_uv,
    cast (0 as double) as avg_access       
from 
    (
    select 
        b.sid as sid,
        b.cid as cid,
        cookie 
    from ods_cooperation_dwr_ad_click_dm
    LATERAL VIEW parse_url_tuple(regexp_replace(UrlDecoderUDF(landing_url),'#','?'), 'QUERY:sid', 'QUERY:cid') b as sid,cid
    where pt_d='$date'
    ) t
 where !IsEmptyUDF(sid) and !IsEmptyUDF(cid)
 group by 
    sid,cast(cid as int);

insert overwrite table dw_honor_marketing_result_hm
partition (pt_d='$date')
select 
    t1.pt_h,
    t1.sid,
    t1.cid,
    t1.bg_pv,
    t1.bg_uv,
    t1.dj_pv,
    t1.dj_uv,
    t1.landing_pv,
    t1.landing_uv,
    if(dj_uv=0,0,substr(cast(t1.landing_uv/dj_uv as string),1,20)) as land_rate,
    t2.prom_user as bespeak_nums,
    if(t1.landing_uv=0,0,substr(cast(t2.prom_user/t1.landing_uv as string),1,20)) as transform_rate,
    t3.orders as buy_nums,
    if(prom_user=0,0,substr(cast(t3.orders/t2.prom_user as string),1,20)) as buy_trans_rate,
    avg_access,
    if(t4.sid is not null,0,0) as isdsp
from
(
    select 
        pt_h,
        sid,
        max(cid) as cid,
        sum(bg_pv) as bg_pv,
        sum(bg_uv) as bg_uv ,
        sum(dj_pv) as dj_pv,
        sum(dj_uv) as dj_uv,
        sum(landing_pv) as landing_pv,
        sum(landing_uv) as landing_uv,
        max(avg_access) as avg_access
    from  
    (
       select
           pt_h,
           sid,
           cid,
           bg_pv,
           bg_uv,
           dj_pv,
           dj_uv,
           landing_pv,
           landing_uv,
           avg_access
       from tmp_bg_dj_ds
       union all
       select
           'NA' as pt_h,      
           t3.sid,
           cast(t3.cid  as int) as cid,
           cast (0 as bigint) as bg_pv,
           cast (0 as bigint) as bg_uv,
           cast (0 as bigint) as dj_pv,
           cast (0 as bigint) as dj_uv,
           count(*) as landing_pv,
           count(distinct t1.s_id) as landing_uv,
           count(*)/count(distinct t1.s_id) as avg_access
       from
       (
        select
            parse_url(regexp_replace(UrlDecoderUDF(url),'#','?'), 'QUERY','sid') as sid,
            s_id
        from ods_vmall_hi_data_dm
        WHERE pt_d='$date' 
        and idsite in ('www.honor.cn','www.vmall.com','mm.vmall.com','sale.vmall.com','m.vmall.com','mt.vmall.com','msale.vmall.com','mw.vmall.com','ma.vmall.com','asale.vmall.com','consumer.huawei.com','cn.club.vmall.com')
        and trim(action_type) = '1' and id is not null and idvc is not null
        ) t1
        left outer join
        (select sid,cid from ODS_VMALL_DJ_DM where pt_d='$date' group by sid,cid) t3
        on t1.sid=t3.sid
       where t3.sid is not null
       group by 
           t3.sid,cast(t3.cid  as int)
    ) t1
    group by pt_h,sid
) t1
left outer join
(
    select
        cid,
        count(distinct customer_id) as prom_user
    from ODS_VMALL2_TBL_CUSTOMER_REL_NUM_DM
    where pt_d='$date'
    group by cid
) t2
on t1.cid=t2.cid
left outer join
(
    select
        cid,
        count(distinct user_id) orders
    from dim_vmall2_order_sku_ds
    where to_date(order_date)>='$date_ep'
    group by cid
) t3
on t1.cid = cast (t3.cid as string)
left outer join
(
    select
        sid
    from ods_vmall_dj_dm
    where pt_d='$date' and length(ext)>10
    group by sid
) t4
on t1.sid = cast (t4.sid as string)
left semi join 
    dim_mkt_base_info_ds t5
on t1.sid  = t5.sid;
"
