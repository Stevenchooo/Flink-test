#依赖：dw_hiad_hicloud_push_result_compatible_dm
#刷新t_push_mkt_report
#删除老的
#echo "delete from t_push_mkt_report where pt_d='$date';"> $HIBI_PATH/config/gen_mkt_report_touser/t_push_mkt_report_touser_$date_55.txt
#if [ ! -f "$HIBI_PATH/config/gen_mkt_report_touser/t_push_mkt_report_touser_$date_55.txt" ]; then
#    echo "delete old t_push_mkt_report failed......"
#    exit 1
#else
#    echo "delete old t_push_mkt_report successed......"
#fi 
#生成新的
hive -e "
set mapreduce.job.queuename=QueueB;
set hive.exec.compress.output=false;
use biwarehouse;
drop table if exists tmp_export_gen_label_value_t_push_mkt_report_touser;
create table if not exists tmp_export_gen_label_value_t_push_mkt_report_touser 
row format delimited
lines terminated by '\n'
STORED AS TEXTFILE
LOCATION '$hadoopuser/tmp/tmp_export_gen_label_value_t_push_mkt_report_touser'
as

select
    t2.jobid,
    t2.task_name,
    t1.market_channel,
    t1.send,
    t1.success,
    t1.clicks,
    t1.views,
    t1.downloads,
    t1.hispace_downloads,
    t1.pt_d
from
    (
        select * from dw_hiad_hicloud_push_result_compatible_dm where pt_d='$date' and pt_dim='NA'
    )t1
join
    (
        select 
            jobid,if(!IsEmptyUDF(task_id),task_id,jobid) as task_id,name as task_name 
        from ods_persona_mkt_taskinfo_push_dm
        where pt_d='$date' and appid='6'
        group by jobid,if(!IsEmptyUDF(task_id),task_id,jobid),name
    )t2
on(t1.task_id=t2.task_id)
;"$END
hadoop fs -cat $hadoopuser/tmp/tmp_export_gen_label_value_t_push_mkt_report_touser/*>> $HIBI_PATH/config/gen_mkt_report_touser/t_push_mkt_report_touser_$date_55.txt
hive -e "
set mapreduce.job.queuename=QueueB;
set hive.exec.compress.output=false;
use biwarehouse;
drop table if exists tmp_export_gen_label_value_t_push_mkt_report_touser;
"$END

if [ ! -f "$HIBI_PATH/config/gen_mkt_report_touser/t_push_mkt_report_touser_$date_55.txt" ]; then
    echo "$HIBI_PATH/config/gen_mkt_report_touser/t_push_mkt_report_touser_$date_55.txt is not exit"
    exit 1
else
    echo "$HIBI_PATH/config/gen_mkt_report_touser/t_push_mkt_report_touser_$date_55.txt is exit"
fi 
scp $HIBI_PATH/config/gen_mkt_report_touser/t_push_mkt_report_touser_$date_55.txt oplatform@10.61.36.3:/opt/huawei/BI/pushLabel/
if [ $? -eq 0 ]; then
    echo "scp t_push_mkt_report_touser_$date_55.txt successfully......"
else
    echo "scp t_push_mkt_report_touser_$date_55.txt failed......"
    exit 1
fi