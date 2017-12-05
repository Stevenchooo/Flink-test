# ----------------------------------------------------------------------------
#  File Name:dw_mkt_hot_url_product_result_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2012.All rights reserved.
#  Purpose: Calculate the average length of each URL product visit, URL jump out rate
#  Describe:Heat of marketing system
#  Input：  dim_mkt_active_starttime_endtime_dm,ods_vmall_hi_data_dm
#  Output： dw_mkt_hot_url_product_result_dm
#  Author: cwx376287
#  Creation Date:  2016-12-20
#  Last Modified:  
# ----------------------------------------------------------------------------  
hive -e "
set mapreduce.job.queuename=QueueA;
set hive.exec.compress.output=true;
set mapreduce.output.fileoutputformat.compress.codec=com.hadoop.compression.lzo.LzopCodec;
use biwarehouse;
CREATE EXTERNAL TABLE IF NOT EXISTS dw_mkt_hot_url_product_result_dm(
     activtiy_id    STRING,
     sid            STRING,
     cid            STRING,
     url            STRING,
     landing_uv     STRING,
     landing_pv     STRING,
     bounces        STRING,
     visit_time     STRING
)
PARTITIONED BY(pt_d STRING COMMENT 'DATE_PARTITION (YYYYMMDD)')
ROW FORMAT DELIMITED
FIELDS TERMINATED BY '\t'
LINES TERMINATED BY '\n'
STORED AS INPUTFORMAT 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'
          OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION '$hadoopuser/data/DW/dw_mkt_hot_url_product_result_dm';

INSERT OVERWRITE TABLE dw_mkt_hot_url_product_result_dm
PARTITION (pt_d = '$date')
select 
    t.activtiy_id,
    t.sid,
    t.cid,
    t.url,
    t.landing_uv,
    t.landing_pv,
    t.bounces,
    t.visit_time
from 
(
    select 
        fk.activtiy_id,
        fk.sid,
        fk.cid,
        fk.url,
        fk.landing_uv,
        fk.landing_pv,
        fk.bounces,
        fk.visit_time,
        RowNumber_UDF(fk.activtiy_id,fk.sid,fk.cid) as row_number
    from 
    (
        select
            f.activtiy_id,
            f.sid,
            f.cid,
            f.url,
            f.landing_uv,
            f.landing_pv,
            if(!IsEmptyUDF(t.bounces),t.bounces,'0') as bounces,
            f.url_times as visit_time
        from
        (
            select
                t.activtiy_id,
                t.cid,
                t.sid,
                f.url,
                f.url_times,
                f.landing_uv,
                f.landing_pv
            from
            (
                 select 
                     activtiy_id,
                     sid,
                     cid
                 from 
                     dim_mkt_active_starttime_endtime_dm
                 where 
                     pt_d='$date' and start_time <= '$date' and end_time >= '$date'
            ) t
            join
            (
                select
                    sid,
                    sum(url_times) as url_times,
                    count(distinct id) as landing_uv,
                    count(id) as landing_pv,
                    url
                from
                (
                    select 
                       parse_url(regexp_replace(UrlDecoderUDF(url),'#','?'), 'QUERY','sid') as sid,
                       idsite,
                       id,
                       idvc,
                       if((LEAD(server_time,1) OVER(PARTITION BY idsite,id,idvc ORDER BY server_time)) is null,
                       3,unix_timestamp(LEAD(server_time,1) OVER(PARTITION BY idsite,id,idvc ORDER BY server_time))-unix_timestamp(server_time)) as url_times,
                       url
                    from ods_vmall_hi_data_dm
                    where pt_d='$date' and idsite in ('www.vmall.com','mm.vmall.com','sale.vmall.com','m.vmall.com','mt.vmall.com','msale.vmall.com','mw.vmall.com','ma.vmall.com','asale.vmall.com')
                        and action_type='1' and id is not null and idvc is not null and server_time is not null and url is not null
                ) t
                where !IsEmptyUDF(sid) and url like '%product%'
                group by sid,url
            ) f
        on t.sid=f.sid
        ) f
        left outer join  
        (  
            select
                sid,
                count(bounces) as bounces,
                url
            from
            (
                select 
                   max(parse_url(regexp_replace(UrlDecoderUDF(url),'#','?'), 'QUERY','sid')) as sid,
                   idsite,
                   id,
                   idvc,
                   count(url) as bounces,
                   max(url) as url
                from ods_vmall_hi_data_dm
                where pt_d='$date' and idsite in ('www.vmall.com','mm.vmall.com','sale.vmall.com','m.vmall.com','mt.vmall.com','msale.vmall.com','mw.vmall.com','ma.vmall.com','asale.vmall.com')
                   and action_type='1' and id is not null and idvc is not null and url is not null
                group by idsite,id,idvc
            ) t
            where bounces=1 and !IsEmptyUDF(sid) and url like '%product%'
            group by sid,url
        ) t
        on f.sid=t.sid and f.url=t.url
    )fk
    distribute by activtiy_id,sid,cid sort by activtiy_id,sid,cid,landing_uv desc 
) t 
where t.row_number<=50;
"