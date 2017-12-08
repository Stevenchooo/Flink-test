# --------------------------------------------------------------------------------------------------------
#  @FileName: ads_trade_risk_control_frequent_regist_info_dm.sql
#  @CopyRight: copyright(c)huawei technologies co.,ltd.1998-2026.all rights reserved.
#  @Purpose: 支付账号注册信息
#  @Describe: 支付账号注册信息
#  @Input:dwd_pty_up_ds_his,dwd_sal_order_pay_ds,dwd_cust_mapping
#  @Output: ads_trade_risk_control_frequent_regist_info_dm
#  @Author: dingbaitong/dwx404269
#  @Version: DataOne 2.0.2
#  @Create: 2017-2-22
#  @Modify:
# ---------------------------------------------------------------------------------------------------------


beeline -e "
set mapreduce.job.queuename=CDM_DW;
use bicoredata;

# @TableName ads_trade_risk_control_frequent_regist_info_dm
# @TableDesc 支付账号注册信息
CREATE TABLE IF NOT EXISTS biads.ads_trade_risk_control_frequent_regist_info_dm
(    
    up_id                  varchar(128)         COMMENT'华为账号',
    register_time          varchar(30)          COMMENT'注册时间',
    register_channel_id    varchar(128)         COMMENT'注册平台(渠道号)',
    register_ip_addr       varchar(128)         COMMENT'注册IP',
    register_mobile_phone  varchar(128)         COMMENT'注册时绑定的手机号',
    register_mail          varchar(128)         COMMENT'注册时绑定的邮箱'
)
COMMENT '支付账号注册信息表'
PARTITIONED BY (pt_d VARCHAR(8) COMMENT '天分区')
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE
LOCATION '/AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_regist_info_dm';



INSERT OVERWRITE TABLE biads.ads_trade_risk_control_frequent_regist_info_dm
PARTITION (pt_d='$date')
select 
    t3.cust_id_map as up_id,
    DateUtil(t2.register_time,'yyyy-MM-dd HH:mm:ss','yyyy-MM-dd') as register_time,	
    t2.register_channel_id as register_channel_id,
    t2.register_ip_addr as register_ip_addr,
    t2.mobile_login_acct as register_mobile_phone,
    t2.email_login_acct as  register_mail  
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
        register_time,
        register_ip_addr,
        register_channel_id,
        mobile_login_acct,
        email_login_acct
    from 
        bicoredata.dwd_pty_up_ds_his
    where start_date<='$date' AND end_date>'$date'
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
ALTER TABLE ads_trade_risk_control_frequent_regist_info_dm DROP IF EXISTS PARTITION (pt_d ='${date,-7,yyyyMMdd}');
"$END
hadoop fs -getmerge /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_regist_info_dm/pt_d=$date /data1/birisk/ads_trade_risk_control_frequent_regist_info_dm/RegistData_$date.data

scp /data1/birisk/ads_trade_risk_control_frequent_regist_info_dm/RegistData_$date.data hibi@10.228.146.242:/home/hibi/

hadoop fs -test -e /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_regist_info_dm/pt_d='${date,-7,yyyyMMdd}'
if [ $? -eq 0 ];then
hadoop fs -rmr /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_regist_info_dm/pt_d='${date,-7,yyyyMMdd}'
else
echo "The partition file does not exist"
fi

rm -f /data1/birisk/ads_trade_risk_control_frequent_regist_info_dm/RegistData_'${date,-7,yyyyMMdd}'
