# --------------------------------------------------------------------------------------------------------
#  @FileName: ads_trade_risk_control_frequent_login_info_dm.sql
#  @CopyRight: copyright(c)huawei technologies co.,ltd.1998-2026.all rights reserved.
#  @Purpose: 支付账号登录信息统计
#  @Describe: 支付账号登录信息统计
#  @Input:dwd_evt_up_oper_log_dm,dwd_sal_order_pay_ds,dwd_cust_mapping
#  @Output: ads_trade_risk_control_frequent_login_info_dm
#  @Author: dingbaitong/dwx404269
#  @Version: DataOne 2.0.2
#  @Create: 2017-2-22
#  @Modify:
# ---------------------------------------------------------------------------------------------------------

beeline -e "
set mapreduce.job.queuename=CDM_DW;
use bicoredata;

# @TableName ads_trade_risk_control_frequent_login_info_dm
# @TableDesc 支付账号的登录信息
CREATE TABLE IF NOT EXISTS biads.ads_trade_risk_control_frequent_login_info_dm
(    
    up_id                  varchar(128)         COMMENT'华为账号',
    user_account           varchar(128)         COMMENT'登录名',          
    login_time             varchar(30)          COMMENT'登录时间',         
    login_client_ip        varchar(256)         COMMENT'登录ip',           
    login_device_id        varchar(128)         COMMENT'登录设备',         
    login_channel_id       varchar(128)         COMMENT'登录来源'
)
COMMENT '支付账号登录信息结果表'
PARTITIONED BY (pt_d VARCHAR(8) COMMENT '天分区')
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE
LOCATION '/AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_login_info_dm';


INSERT OVERWRITE TABLE biads.ads_trade_risk_control_frequent_login_info_dm
PARTITION (pt_d='$date')
select 
    t3.cust_id_map as up_id,
    t2.user_acct_id as user_account,
    t2.oper_time as login_time,
    t2.user_ip_addr as login_client_ip,
    t2.imei as login_device_id,
    t2.channel_id as login_channel_id
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
        oper_time,
        user_ip_addr,
        imei,
        channel_id,
        user_acct_id
    from 
        bicoredata.dwd_evt_up_oper_log_dm
    where up_oper_type_cd = '2' and pt_d='$date'  
  )t2
on t1.up_id=t2.up_id
left outer join
   (
      select
         cust_id,
         cust_id_map
      from bicoredata.dwd_cust_mapping
      where pt_type='upid'  and  !isEmpty(cust_id)
   )t3
on t1.up_id = t3.cust_id;

# @DESC保留近7天数据
ALTER TABLE ads_trade_risk_control_frequent_login_info_dm DROP IF EXISTS PARTITION (pt_d ='${date,-7,yyyyMMdd}');
"$END
hadoop fs -getmerge /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_login_info_dm/pt_d=$date /data1/birisk/ads_trade_risk_control_frequent_login_info_dm/UserLoginInfo_$date.data

scp /data1/birisk/ads_trade_risk_control_frequent_login_info_dm/UserLoginInfo_$date.data hibi@10.228.146.242:/home/hibi/

hadoop fs -test -e /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_login_info_dm/pt_d='${date,-7,yyyyMMdd}'
if [ $? -eq 0 ];then
hadoop fs -rmr /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_login_info_dm/pt_d='${date,-7,yyyyMMdd}'
else
echo "The partition file does not exist"
fi

rm -f /data1/birisk/ads_trade_risk_control_frequent_login_info_dm/UserLoginInfo_'${date,-7,yyyyMMdd}'

