#----------------------------------------------------------------------------
#  file Name: v0.1 dim_mkt_active_starttime_endtime_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose:  Calculate the start time of the marketing campaign
#  Describe: Start time and end time statistics for each marketing campaign
#  Input:  ODS_VMALL_BG_DM,ODS_VMALL_DJ_DM,dim_mkt_base_info_ds
#  Output: dim_mkt_active_starttime_endtime_dm
#  Author: cwx376287
#  Dependency: 
#  Creation Date:  2016-11-07
#  Last Modified:  
#----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueB;
set hive.exec.compress.output=true;
set mapreduce.output.fileoutputformat.compress.codec=com.hadoop.compression.lzo.LzopCodec;
use biwarehouse;

CREATE EXTERNAL TABLE IF NOT EXISTS dim_mkt_active_starttime_endtime_dm
(
     activtiy_id  string,
     sid          string,           
     cid          string,            
     start_time   string,            
     end_time     string
)
PARTITIONED BY (pt_d STRING)
STORED AS INPUTFORMAT 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'
          OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat'
LOCATION '$hadoopuser/DIM/dim_mkt_active_starttime_endtime_dm';

INSERT OVERWRITE TABLE dim_mkt_active_starttime_endtime_dm
PARTITION (pt_d='$date')
    SELECT
        t1.activtiy_id,
        t1.sid,
        t1.cid,
        t2.start_time,
        t2.end_time
    from 
        dim_mkt_base_info_ds t1
    left outer join
    (       
       select 
      t.sid   AS     sid,
      t.cid   AS     cid,
      min(t.start_time) AS   start_time,
      max(t.end_time) AS   end_time
      from(
                SELECT
                    sid,
                    cid,
                    pt_d as start_time,
                    pt_d as end_time
                FROM
                    ODS_VMALL_BG_DM
                    where pt_d='$date'
                union all
                SELECT
                    sid,
                    cid,
                    pt_d as start_time,
                    pt_d as end_time
                FROM
                    ODS_VMALL_DJ_DM
                    where pt_d='$date'
             union all
             select
                 sid,
                 cid,
                 start_time,
                 end_time
             from 
                 dim_mkt_active_starttime_endtime_dm
                 where pt_d='$last_date'
       )t
          group by t.sid,t.cid       
    )t2
    on t1.sid=t2.sid and t1.cid=t2.cid;"