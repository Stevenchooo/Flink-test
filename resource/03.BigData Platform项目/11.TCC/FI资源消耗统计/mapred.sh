#!/bin/bash
cd `dirname $0`
source /MFS/Share/BICommon/task/HiBI/FIClient/bigdata_env
kinit -kt /opt/huawei/TCCProd/task/FIKey/user.keytab tcc
prc_time=$1
prc_date=${prc_time%-*}

mysql -h 10.71.24.196 -u tcc2rs -pg50CKgs8TwTqu9GmcPnX -Dtcc2rs -e "
select f.*,date_format(start_time,'%Y%m%d') as run_date,unix_timestamp(f.end_time) - unix_timestamp(f.start_time) as execute_seconds from
(select c.task_id,
   c.cycle_id as period_id,
   min(e.Running_Start_Time) as start_time,
   c.Running_End_Time as end_time,
   group_concat(e.Running_Job_ID) as job_Ids,
   c.Return_Times-1 as redo_times
from tcc_task a,
 tcc_task_running_state c,
 tcc_batch_running_state d,
 tcc_step_running_state e
where a.task_id = c.task_id and
      c.task_id = d.task_id and
  c.cycle_id =  d.cycle_id and
  d.task_id = e.task_id and
  d.cycle_id =  e.cycle_id and
  d.batch_id = e.batch_id and
  c.Running_Start_Time>=date_format(DATE_ADD('$prc_date',interval -1 day),'%Y-%m-%d 08:00:00') and
  c.Running_Start_Time<date_format('$prc_date','%Y-%m-%d 08:00:00')
group by c.task_id,c.cycle_id
order by start_time) f" > taskinfo

python mapred.py taskinfo
mysql -h 10.71.24.196 -u tcc2rs -pg50CKgs8TwTqu9GmcPnX -Dtcc2rs < mapred.sql
mysql -h 10.71.24.196 -u tcc2rs -pg50CKgs8TwTqu9GmcPnX -Dtcc2rs -e "
truncate table res_statics;
insert into res_statics 
select task_id, 1 as res_type, sum(task_duration)/count(task_duration) as value from tcc_mapred_info group by task_id
union all
select task_id, 2 as res_type, sum(maps_total)/count(maps_total) as value from tcc_mapred_info group by task_id
union all
select task_id, 3 as res_type, sum(reduces_total)/count(reduces_total) as value from tcc_mapred_info group by task_id
union all
select task_id, 4 as res_type, sum(redo_times)/count(redo_times) as value from tcc_mapred_info group by task_id
union all
select task_id, 5 as res_type, avg(UNIX_TIMESTAMP(task_start_time)-UNIX_TIMESTAMP(date(task_start_time))) as value from tcc_mapred_info group by task_id
union all
select task_id, 6 as res_type, avg(UNIX_TIMESTAMP(task_end_time)-UNIX_TIMESTAMP(date(task_end_time))) as value from tcc_mapred_info group by task_id;
"

rm taskinfo
rm mapred.sql