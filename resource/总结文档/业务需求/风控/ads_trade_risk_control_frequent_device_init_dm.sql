# --------------------------------------------------------------------------------------------------------
#  @FileName: ads_trade_risk_control_frequent_device_init_dm.sql
#  @CopyRight: copyright(c)huawei technologies co.,ltd.1998-2026.all rights reserved.
#  @Purpose: 支付账号的常用设备统计
#  @Describe: 支付账号的常用设备统计
#  @Input:dwd_evt_up_oper_log_dm,dwd_sal_order_pay_ds
#  @Output: ads_trade_risk_control_frequent_device_init_dm
#  @Author: dingbaitong/dwx404269
#  @Version: DataOne 2.0.2
#  @Create: 2017-2-22
#  @Modify:
# ---------------------------------------------------------------------------------------------------------

beeline -e "
set mapreduce.job.queuename=CDM_DW;
use bicoredata;

# @TableName ads_trade_risk_control_frequent_device_init_dm
# @TableDesc 支付账号的常用设备初始化表
CREATE TABLE IF NOT EXISTS biads.ads_trade_risk_control_frequent_device_init_dm
(    
    up_id                 varchar(128)         COMMENT'华为账号',
    imei                  varchar(128)         COMMENT'伴随账号出现的设备号',
    num                   INT                  COMMENT'此账号在之前60天伴随出现的设备数',
    update_time           varchar(30)          COMMENT'数据最近一次的更新时间,精确到天'
)
COMMENT '支付账号的常用设备初始化表'
PARTITIONED BY(pt_d STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE
LOCATION '/AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_device_init_dm';


INSERT OVERWRITE TABLE biads.ads_trade_risk_control_frequent_device_init_dm
PARTITION (pt_d='$date')
select
  t1.up_id as up_id,
  t2.imei as imei,
  t2.num as num,
  t2.update_time as update_time
from
  (
    select 
        pay_up_id as up_id
    from 
        bicoredata.dwd_sal_order_pay_ds
    where  pay_status_cd='0' and pt_d = '$date' and !isEmpty(pay_up_id)
    group by pay_up_id  
  )t1
inner join 
  ( 
    select 
      up_id,
      imei,
      count(distinct pt_d) as num,    
      max(oper_time) as update_time
    from 
      bicoredata.dwd_evt_up_oper_log_dm 
    where pt_d <'$date' and  pt_d>=DateUtil(date_sub(DateUtil('$date','yyyyMMdd','yyyy-MM-dd'), 60),'yyyy-MM-dd','yyyyMMdd') and !isEmpty(imei)
    group by up_id,imei
   )t2 
on t1.up_id=t2.up_id;


"

