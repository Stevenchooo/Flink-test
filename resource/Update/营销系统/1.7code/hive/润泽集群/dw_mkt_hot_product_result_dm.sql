# ----------------------------------------------------------------------------
#  File Name:dw_mkt_hot_product_result_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2012.All rights reserved.
#  Purpose: 
#  Describe:营销系统热度
#  Input：  
#  Output： 
#  Author: w84057406
#  Creation Date:  2015-06-07
#  Last Modified:  
#  Last Modified:  cwx376287 2016-07-15  将原来取值top100改为top50
#                  l00166278 20160716  1、考虑到一个cid会对应多个活动的情况，使用dim_mkt_base_info_ds时增加时间判断，只取在有效期内的活动id与cid关系，避免将当前活动效果映射到以前活动上
#                                     
# ----------------------------------------------------------------------------  
hive -e "
set mapreduce.job.queuename=QueueA;
set hive.exec.compress.output=true;
set mapreduce.output.fileoutputformat.compress.codec=com.hadoop.compression.lzo.LzopCodec;
use biwarehouse;
CREATE EXTERNAL TABLE IF NOT EXISTS dw_mkt_hot_product_result_dm(
     activtiy_id STRING,
     cid STRING,
     url STRING,
     entercount STRING,
     landing_uv STRING,
     landing_pv STRING,
     ipcount STRING,
     downuv STRING,
     visit_time String
     
)
PARTITIONED BY(pt_d STRING COMMENT 'DATE_PARTITION (YYYYMMDD)')
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n'
STORED AS INPUTFORMAT 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'
          OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION '/AppData/EUIProd/TCSM/data/DW/TCSM/dw_mkt_hot_product_result_dm';

INSERT OVERWRITE TABLE dw_mkt_hot_product_result_dm
PARTITION (pt_d = '$date')

select 
t.activtiy_id,
t.cid,
t.url,
t.entercount,
t.landing_uv,
t.landing_pv,
t.ipcount,
t.downuv,
t.visit_time
from
(
    select 
    fk.activtiy_id,
    fk.cid,
    fk.url,
    fk.entercount,
    fk.landing_uv,
    fk.landing_pv,
    fk.ipcount,
    fk.downuv,
    fk.visit_time,
    RowNumber_UDF(fk.activtiy_id,fk.cid) as row_number
    from 
    (
        select 
        f.activtiy_id as activtiy_id,
        f.cid as cid,
        f.url as url,
        sum(f.entercount) as entercount,
        sum(f.landing_uv) as landing_uv,
        sum(f.landing_pv) as landing_pv,
        sum(f.ipcount)    as ipcount,
        sum(k.downuv)     as downuv,
        sum(unix_timestamp(k.end_time)-unix_timestamp(k.start_time)) as visit_time
        from 
        (
          SELECT 
          t.activtiy_id as activtiy_id,
          t.cid as cid,
          t1.url,
          count(distinct concat(t1.cookieid,t1.urlref)) as  entercount,
          count(distinct t1.cookieid) as landing_uv,
          count(t1.cookieid) as landing_pv,
          count(distinct t1.ip) as ipcount
          from 
          (
             select 
                 t11.activtiy_id as activtiy_id,
                 t11.cid as cid
             from 
                 dim_mkt_active_starttime_endtime_dm t11
             where 
                 t11.pt_d='$date' and t11.start_time <= '$date' and t11.end_time >= '$date'
           )  t
          join 
           (select  
            url,
            urlref,
            get_json_object(cvar1,'$.1[1]') as cid,
            client_ip as ip,
            s_id as cookieid,
            server_time
           from ods_vmall_hi_data_dm
                where action_type=1 and pt_d='$date' and idsite in ('www.vmall.com','mm.vmall.com','sale.vmall.com','m.vmall.com','mt.vmall.com','msale.vmall.com','mw.vmall.com','ma.vmall.com','asale.vmall.com')
                    and url like '%product%'    
            ) t1
           on t.cid=t1.cid
           group by 
                t.activtiy_id,t.cid,t1.url
        ) f
        left outer join 
           (select 
           if(action_type>1,count(distinct s_id),0) as downuv,
            get_json_object(cvar1,'$.1[1]') as cid,
            max(server_time) as end_time,
            min(server_time) as start_time
           from ods_vmall_hi_data_dm
           where pt_d='$date' and idsite in ('www.vmall.com','mm.vmall.com','sale.vmall.com','m.vmall.com','mt.vmall.com','msale.vmall.com','mw.vmall.com','ma.vmall.com','asale.vmall.com')
                  and url like '%product%'
           group by action_type,get_json_object(cvar1,'$.1[1]')
           ) k
        on f.cid =k.cid
        group by f.activtiy_id,f.cid,f.url
    )fk 
    distribute by activtiy_id,cid sort by activtiy_id,cid,landing_uv desc 
) t
where t.row_number<=50;
"
