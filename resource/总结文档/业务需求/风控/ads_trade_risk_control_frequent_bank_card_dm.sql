# --------------------------------------------------------------------------------------------------------
#  @FileName: ads_trade_risk_control_frequent_bank_card_dm.sql
#  @CopyRight: copyright(c)huawei technologies co.,ltd.1998-2026.all rights reserved.
#  @Purpose: 用户绑定银行卡信息
#  @Describe: 用户绑定银行卡信息
#  @Input:dwd_pty_up_bank_card_rela_ds_his,dwd_sal_order_pay_ds,dwd_cust_mapping
#  @Output: ads_trade_risk_control_frequent_bank_card_dm
#  @Author: dingbaitong/dwx404269
#  @Version: DataOne 2.0.2
#  @Create: 2017-2-22
#  @Modify:
# ---------------------------------------------------------------------------------------------------------

beeline -e "
set mapreduce.job.queuename=CDM_DW;
use bicoredata;

# @TableName ads_trade_risk_control_frequent_bank_card_dm
# @TableDesc 用户绑定银行卡信息
CREATE TABLE IF NOT EXISTS biads.ads_trade_risk_control_frequent_bank_card_dm
(    
    up_id                  varchar(128)         COMMENT'华为账号',
    user_card_num          varchar(128)         COMMENT'卡号(后四位)   7867',
    bank_card_type         varchar(128)         COMMENT'卡类型：credut，debit',
    iss_bank               varchar(16)          COMMENT'银行代码',
    iss_bank_name          varchar(256)         COMMENT'银行名如：农业银行 ',
    mobile_num             varchar(128)         COMMENT'手机号码********878',      
    update_time            varchar(30)          COMMENT'最后修改时间   20160101 01:01:22'
)
COMMENT '用户绑定银行卡信息'
PARTITIONED BY (pt_d VARCHAR(8) COMMENT '天分区')
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE
LOCATION '/AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_bank_card_dm';


INSERT OVERWRITE TABLE biads.ads_trade_risk_control_frequent_bank_card_dm
PARTITION (pt_d='$date')
select 
    t3.cust_id_map as up_id,
    t2.user_card_num as user_card_num,
    t2.bank_card_type as bank_card_type,
    t2.iss_bank as iss_bank,
    t2.iss_bank_name as iss_bank_name,
    t2.mobile_num as mobile_num,
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
        mobile_num,
        iss_bank,
        iss_bank_name,
        bank_card_type,
        user_card_num,
        case when end_date!='99991231' then DateUtil(end_date,'yyyyMMdd','yyyy-MM-dd') end as update_time
    from 
        bicoredata.dwd_pty_up_bank_card_rela_ds_his
    where  start_date<='$date' AND end_date>'$date'

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
ALTER TABLE ads_trade_risk_control_frequent_bank_card_dm DROP IF EXISTS PARTITION (pt_d ='${date,-7,yyyyMMdd}');
"$END
hadoop fs -getmerge /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_bank_card_dm/pt_d=$date /data1/birisk/ads_trade_risk_control_frequent_bank_card_dm/UserCard_$date.data

scp /data1/birisk/ads_trade_risk_control_frequent_bank_card_dm/UserCard_$date.data hibi@10.228.146.242:/home/hibi/

hadoop fs -test -e /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_bank_card_dm/pt_d='${date,-7,yyyyMMdd}'
if [ $? -eq 0 ];then
hadoop fs -rmr /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_bank_card_dm/pt_d='${date,-7,yyyyMMdd}'
else
echo "The partition file does not exist"
fi

rm -f /data1/birisk/ads_trade_risk_control_frequent_bank_card_dm/UserCard_'${date,-7,yyyyMMdd}'

