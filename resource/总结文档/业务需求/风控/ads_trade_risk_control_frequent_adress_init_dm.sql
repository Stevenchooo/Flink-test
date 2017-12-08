# --------------------------------------------------------------------------------------------------------
#  @FileName: ads_trade_risk_control_frequent_adress_init_dm.sql
#  @CopyRight: copyright(c)huawei technologies co.,ltd.1998-2026.all rights reserved.
#  @Purpose: 支付账号的往前60天的常用地信息
#  @Describe: 支付账号往前60天的常用地信息
#  @Input:dwd_evt_up_oper_log_dm,dwd_sal_order_pay_ds
#  @Output: ads_trade_risk_control_frequent_adress_init_dm
#  @Author: dingbaitong/dwx404269
#  @Version: DataOne 2.0.2
#  @Create: 2017-2-22
#  @Modify:
# ---------------------------------------------------------------------------------------------------------

beeline -e "
set mapreduce.job.queuename=CDM_DW;
use bicoredata;

# @TableName ads_trade_risk_control_frequent_adress_init_dm
# @TableDesc 支付账号的常用地初始化表
CREATE TABLE IF NOT EXISTS biads.ads_trade_risk_control_frequent_adress_init_dm
(    
    up_id                 varchar(128)        COMMENT'华为账号',
    country               varchar(50)         COMMENT'账号出现的国家',
    province              varchar(50)         COMMENT'账号出现的省份',
    city                  varchar(50)         COMMENT'账号出现的城市',
    num                   INT                 COMMENT'此账号在往前60天在省市出现的频次，最大60，最小为10',
    update_time           varchar(30)         COMMENT'数据最近一次的更新时间,精确到天'
)
COMMENT '支付账号常用地初始化表'
PARTITIONED BY (pt_d VARCHAR(8) COMMENT '天分区')
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE
LOCATION '/AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_adress_init_dm';


INSERT OVERWRITE TABLE biads.ads_trade_risk_control_frequent_adress_init_dm
PARTITION (pt_d='$date')
select
  t1.up_id as up_id,
  get_json_object(t2.adress,'$.country') as country,
  get_json_object(t2.adress,'$.province') as province,
  get_json_object(t2.adress,'$.city') as city,
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
        CASE WHEN size(split(user_ip_addr,':'))=1 THEN IP2AreaInfo (UpDecryption(user_ip_addr)) 
        WHEN size(split(user_ip_addr,':'))in(2,3) THEN IP2AreaInfo (AesCBCUpDecry(CONCAT(split(user_ip_addr,':')[0],':',split(user_ip_addr,':')[1]),'3DBE3962BA89D10CF652276ABE6D1433'))
        end as adress,
        count(distinct pt_d) as num,    
        max(oper_time) as update_time
    from 
        bicoredata.dwd_evt_up_oper_log_dm
    where pt_d <'$date' and pt_d>= DateUtil(date_sub(DateUtil('$date','yyyyMMdd','yyyy-MM-dd'), 60),'yyyy-MM-dd','yyyyMMdd') and !isEmpty(user_ip_addr)  
    group by 
        CASE WHEN size(split(user_ip_addr,':'))=1 THEN IP2AreaInfo (UpDecryption(user_ip_addr)) 
        WHEN size(split(user_ip_addr,':'))in(2,3) THEN IP2AreaInfo (AesCBCUpDecry(CONCAT(split(user_ip_addr,':')[0],':',split(user_ip_addr,':')[1]),'3DBE3962BA89D10CF652276ABE6D1433'))
        end,up_id
   )t2 
on t1.up_id=t2.up_id;

"

