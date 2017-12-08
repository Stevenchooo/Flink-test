# ----------------------------------------------------------------------------
#  file name:  ads_trade_risk_control_frequent_vip_info_dm.sql
#  copyright(c)huawei technologies co.,ltd.1998-2026.all rights reserved.
#  purpose:  用户VIP信息统计
#  describe: 用户VIP信息统计    从20170101开始执行
#  字段描述： 
#  input:   dwd_sal_order_pay_ds,dwd_cust_mapping,ads_trade_risk_control_frequent_regist_info_dm
#  output:  ads_trade_risk_control_frequent_vip_info_dm
#  author:   dingbaitong/dwx404269
#  Creation Date:  2017-02-23
#  Last Modified:  
#  Change History:
#  备注： 此表的用途是查询用户VIP信息。
# ----------------------------------------------------------------------------

beeline -e "
set mapreduce.job.queuename=CDM_DW;
use bicoredata;

# @TableName ads_trade_risk_control_frequent_vip_info_dm
# @TableDesc 用户VIP信息
CREATE TABLE IF NOT EXISTS biads.ads_trade_risk_control_frequent_vip_info_dm
(    
    up_id                varchar(128)        COMMENT'华为账号',
    update_time          varchar(30)         COMMENT'更新时间(最后一次交易的时间)',
    num                  int                 COMMENT'此账号在往前一周内支付的次数',
    total_pay            decimal(26,2)       COMMENT'此账号在往前一周内支付的总金额'
   
)
COMMENT '用户VIP信息表'
PARTITIONED BY (pt_d VARCHAR(8) COMMENT '天分区')
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE
LOCATION '/AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_vip_info_dm';

###up_id解密
DROP TABLE IF EXISTS biads.tmp_ads_trade_risk_control_frequent_vip_info_dm;
CREATE TABLE IF NOT EXISTS biads.tmp_ads_trade_risk_control_frequent_vip_info_dm
AS
select
   t2.cust_id_map as up_id,
   t1.txn_finish_time,
   t1.pay_id,
   t1.pay_amt
from 
   (
     select 
       pay_up_id,
       txn_finish_time,
       pay_id,
       pay_amt
     from 
       bicoredata.dwd_sal_order_pay_ds
     where  pay_status_cd='0' and pt_d = '$date' and  DateUtil(txn_finish_time,'yyyy-MM-dd HH:mm:ss','yyyyMMdd')<'$date' and DateUtil(txn_finish_time,'yyyy-MM-dd HH:mm:ss','yyyyMMdd')>=DateUtil(date_sub(DateUtil('$date','yyyyMMdd','yyyy-MM-dd'),7),'yyyy-MM-dd','yyyyMMdd') and !isEmpty(pay_up_id)
   
   )t1
left outer join
   (
    select
         cust_id,
         cust_id_map
      from bicoredata.dwd_cust_mapping
      where pt_type='upid'  and  !isEmpty(cust_id)    
   )t2
on t1.pay_up_id = t2.cust_id;   
    



INSERT OVERWRITE TABLE biads.ads_trade_risk_control_frequent_vip_info_dm
PARTITION (pt_d='$date')
select 
    t1.up_id as up_id,
    max(t1.txn_finish_time) as update_time,
    count(t1.pay_id) as num,
    sum(t1.pay_amt) as total_pay
from 
    (
    select 
       up_id,
       txn_finish_time,
       pay_id,
       pay_amt
    from 
       biads.tmp_ads_trade_risk_control_frequent_vip_info_dm    
)t1
inner join     
    (select
      up_id
    from 
      biads.ads_trade_risk_control_frequent_regist_info_dm
    where  pt_d='$date' and DateUtil(date_add(DateUtil(register_time,'yyyyMMdd','yyyy-MM-dd'),10),'yyyy-MM-dd','yyyyMMdd')<'$date'
    group by up_id
    
    )t2
on t1.up_id=t2.up_id
group by t1.up_id
having count(t1.pay_id)>=10 and sum(t1.pay_amt)>=70000;

DROP TABLE IF EXISTS biads.tmp_ads_trade_risk_control_frequent_vip_info_dm;
# @DESC保留近7天数据
ALTER TABLE ads_trade_risk_control_frequent_vip_info_dm DROP IF EXISTS PARTITION (pt_d ='${date,-7,yyyyMMdd}');
"$END
hadoop fs -getmerge /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_vip_info_dm/pt_d=$date /data1/birisk/ads_trade_risk_control_frequent_vip_info_dm/UserVip_$date.data

scp /data1/birisk/ads_trade_risk_control_frequent_vip_info_dm/UserVip_$date.data hibi@10.228.146.242:/home/hibi/

hadoop fs -test -e /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_vip_info_dm/pt_d='${date,-7,yyyyMMdd}'
if [ $? -eq 0 ];then
hadoop fs -rmr /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_vip_info_dm/pt_d='${date,-7,yyyyMMdd}'
else
echo "The partition file does not exist"
fi


hadoop fs -rmr /data1/birisk/ads_trade_risk_control_frequent_vip_info_dm/UserVip_'${date,-7,yyyyMMdd}'
