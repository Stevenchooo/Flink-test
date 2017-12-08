# --------------------------------------------------------------------------------------------------------
#  @FileName: ads_trade_risk_control_frequent_adress_result_dm.sql
#  @CopyRight: copyright(c)huawei technologies co.,ltd.1998-2026.all rights reserved.
#  @Purpose: 支付账号的常用地信息
#  @Describe: 支付账号的常用地信息
#  @Input:ads_trade_risk_control_frequent_adress_init_dm,dwd_evt_up_oper_log_dm,dwd_cust_mapping
#  @Output: ads_trade_risk_control_frequent_adress_result_dm
#  @Author: dingbaitong/dwx404269
#  @Version: DataOne 2.0.2
#  @Create: 2017-2-22
#  @Modify:
# ---------------------------------------------------------------------------------------------------------

beeline -e "
set mapreduce.job.queuename=CDM_DW;
use bicoredata;

# @TableName ads_trade_risk_control_frequent_adress_result_dm
# @TableDesc 支付账号的常用地结果表
CREATE TABLE IF NOT EXISTS biads.ads_trade_risk_control_frequent_adress_result_dm
(    
    up_id                 varchar(128)         COMMENT'华为账号',
    country               varchar(50)         COMMENT'账号出现的国家',
    province              varchar(50)         COMMENT'账号出现的省份',
    city                  varchar(50)         COMMENT'账号出现的城市',
    num                   INT                 COMMENT'此账号在往前60天在省市出现的频次，最大60，最小为10',
    update_time           varchar(30)         COMMENT'数据最近一次的更新时间,精确到天'
)
COMMENT '支付账号常用地结果表'
PARTITIONED BY (pt_d VARCHAR(8) COMMENT '天分区')
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS TEXTFILE
LOCATION '/AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_adress_result_dm';

###昨天的统计历史数据
DROP TABLE IF EXISTS biads.tmp_ys_ads_trade_risk_control_frequent_adress_result_dm;
CREATE TABLE IF NOT EXISTS biads.tmp_ys_ads_trade_risk_control_frequent_adress_result_dm
AS
select
  up_id,
  country,
  province,
  city,
  num,  
  update_time
from
  biads.ads_trade_risk_control_frequent_adress_init_dm
where pt_d = '$last_date';


###60天前的出现的记录
DROP TABLE IF EXISTS biads.tmp_60_ads_trade_risk_control_frequent_adress_result_dm;
CREATE TABLE IF NOT EXISTS biads.tmp_60_ads_trade_risk_control_frequent_adress_result_dm
AS
select
  t1.up_id as up_id,
  get_json_object(t1.adress,'$.country') as country,
  get_json_object(t1.adress,'$.province') as province,
  get_json_object(t1.adress,'$.city') as city,
  t1.update_time as update_time
from
    (select 
        up_id,
        CASE WHEN size(split(user_ip_addr,':'))=1 THEN IP2AreaInfo (UpDecryption(user_ip_addr)) 
        WHEN size(split(user_ip_addr,':'))in(2,3) THEN IP2AreaInfo (AesCBCUpDecry(CONCAT(split(user_ip_addr,':')[0],':',split(user_ip_addr,':')[1]),'3DBE3962BA89D10CF652276ABE6D1433'))
        end as adress,  
        max(oper_time) as update_time
    from 
        bicoredata.dwd_evt_up_oper_log_dm
    where pt_d = DateUtil(date_sub(DateUtil('$date','yyyyMMdd','yyyy-MM-dd'), 60),'yyyy-MM-dd','yyyyMMdd') and !isEmpty(user_ip_addr)  
    group by 
        CASE WHEN size(split(user_ip_addr,':'))=1 THEN IP2AreaInfo (UpDecryption(user_ip_addr)) 
        WHEN size(split(user_ip_addr,':'))in(2,3) THEN IP2AreaInfo (AesCBCUpDecry(CONCAT(split(user_ip_addr,':')[0],':',split(user_ip_addr,':')[1]),'3DBE3962BA89D10CF652276ABE6D1433'))
        end,up_id
   )t1;
   
###今天出现的记录   
DROP TABLE IF EXISTS biads.tmp_td_ads_trade_risk_control_frequent_adress_result_dm;
CREATE TABLE IF NOT EXISTS biads.tmp_td_ads_trade_risk_control_frequent_adress_result_dm
AS
select
  t2.up_id as up_id,
  get_json_object(t2.adress,'$.country') as country,
  get_json_object(t2.adress,'$.province') as province,
  get_json_object(t2.adress,'$.city') as city,
  t2.update_time as update_time
from

    (select 
        up_id,
        CASE WHEN size(split(user_ip_addr,':'))=1 THEN IP2AreaInfo (UpDecryption(user_ip_addr)) 
        WHEN size(split(user_ip_addr,':'))in(2,3) THEN IP2AreaInfo (AesCBCUpDecry(CONCAT(split(user_ip_addr,':')[0],':',split(user_ip_addr,':')[1]),'3DBE3962BA89D10CF652276ABE6D1433'))
        end as adress,  
        max(oper_time) as update_time
    from 
        bicoredata.dwd_evt_up_oper_log_dm
    where pt_d = '$date' and !isEmpty(user_ip_addr)  
    group by 
        CASE WHEN size(split(user_ip_addr,':'))=1 THEN IP2AreaInfo (UpDecryption(user_ip_addr)) 
        WHEN size(split(user_ip_addr,':'))in(2,3) THEN IP2AreaInfo (AesCBCUpDecry(CONCAT(split(user_ip_addr,':')[0],':',split(user_ip_addr,':')[1]),'3DBE3962BA89D10CF652276ABE6D1433'))
        end,up_id
   )t2;
   
###昨天的统计历史数据 - 60天前的出现的记录 + 今天出现的记录（出现则次数记为1，反之为0）
DROP TABLE IF EXISTS biads.tmp_ads_trade_risk_control_frequent_adress_result_dm;
CREATE TABLE IF NOT EXISTS biads.tmp_ads_trade_risk_control_frequent_adress_result_dm
AS
select
  t1.up_id as up_id,
  t1.country as country,
  t1.province as province,
  t1.city as city,
  case when !isEmpty(t2.up_id) and !isEmpty(t3.up_id) then t1.num
       when isEmpty(t2.up_id) and isEmpty(t3.up_id) then t1.num
       when !isEmpty(t2.up_id) and isEmpty(t3.up_id) then t1.num-1
       when isEmpty(t2.up_id) and !isEmpty(t3.up_id) then t1.num+1
       end as num,
  case when !isEmpty(t3.update_time) then t3.update_time else t1.update_time end as update_time

from 
  (
    select
      up_id,
      country,
      province,
      city,
      num,
      update_time
    from 
      biads.tmp_ys_ads_trade_risk_control_frequent_adress_result_dm
  )t1
left outer join
  (
   select
    up_id,  
    city
   from 
     biads.tmp_60_ads_trade_risk_control_frequent_adress_result_dm  
  )t2
on t1.up_id = t2.up_id and t1.city = t2.city
left outer join
  (
   select
     up_id, 
     city,
     update_time
   from 
     biads.tmp_td_ads_trade_risk_control_frequent_adress_result_dm 
  )t3
on t1.up_id = t3.up_id and t1.city = t3.city;

###今天新增出现而之前没有出现过的记录
DROP TABLE IF EXISTS biads.tmp_td_new_ads_trade_risk_control_frequent_adress_result_dm;
CREATE TABLE IF NOT EXISTS biads.tmp_td_new_ads_trade_risk_control_frequent_adress_result_dm
AS
select
   t1.up_id as up_id,
   t1.country as country,
   t1.province as province,
   t1.city as city,
   case when !isEmpty(t2.up_id) then 1 else 0 end as num,
   t1.update_time as update_time
from 
  (
    select
     up_id, 
     country,
     province,
     city,
     update_time
    from 
      biads.tmp_td_ads_trade_risk_control_frequent_adress_result_dm  
  )t1
left outer join
  (
    select
      up_id, 
      city
    from 
      biads.tmp_ys_ads_trade_risk_control_frequent_adress_result_dm  
  )t2
on t1.up_id = t2.up_id and t1.city = t2.city
where isEmpty(t2.up_id);

###昨天的统计记录加上今天新增出现而之前没有出现过的记录插入到初始化表中(第二天查的时候能查到)
INSERT OVERWRITE TABLE biads.ads_trade_risk_control_frequent_adress_init_dm
PARTITION (pt_d='$date')
select
    up_id,
    country,
    province,
    city,
    num,
    update_time
from 
    biads.tmp_td_new_ads_trade_risk_control_frequent_adress_result_dm
union all 
select
    up_id,
    country,
    province,
    city,
    num,
    update_time
from 
    biads.tmp_ads_trade_risk_control_frequent_adress_result_dm;

###(过滤出num大于10的输出)
INSERT OVERWRITE TABLE biads.ads_trade_risk_control_frequent_adress_result_dm
PARTITION (pt_d='$date')
select 
  t2.cust_id_map as up_id,
  t1.country as country,
  t1.province as province,
  t1.city as city,
  t1.num as num,
  t1.update_time as update_time
from
   (
      select 
        up_id,
        country,
        province,
        city,
        num,
        DateUtil(update_time,'yyyy-MM-dd HH:mm:ss','yyyy-MM-dd') as update_time
      from 
        biads.ads_trade_risk_control_frequent_adress_init_dm
      where pt_d='$date' and num>=10
   
   )t1
left outer join
   (
      select
         cust_id,
         cust_id_map
      from bicoredata.dwd_cust_mapping
      where pt_type='upid'  and  !isEmpty(cust_id)
   )t2
on t1.up_id = t2.cust_id;


DROP TABLE IF EXISTS biads.tmp_ys_ads_trade_risk_control_frequent_adress_result_dm;
DROP TABLE IF EXISTS biads.tmp_60_ads_trade_risk_control_frequent_adress_result_dm;
DROP TABLE IF EXISTS biads.tmp_td_ads_trade_risk_control_frequent_adress_result_dm;
DROP TABLE IF EXISTS biads.tmp_ads_trade_risk_control_frequent_adress_result_dm;

# @DESC保留近7天数据
ALTER TABLE ads_trade_risk_control_frequent_adress_result_dm DROP IF EXISTS PARTITION (pt_d ='${date,-7,yyyyMMdd}');
" $END
hadoop fs -getmerge /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_adress_result_dm/pt_d=$date /data1/birisk/ads_trade_risk_control_frequent_adress_result_dm/UserAddress_$date.data

scp /data1/birisk/ads_trade_risk_control_frequent_adress_result_dm/UserAddress_$date.data hibi@10.228.146.242:/home/hibi/

hadoop fs -test -e /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_adress_result_dm/pt_d='${date,-15,yyyyMMdd}'
if [ $? -eq 0 ];then
hadoop fs -rmr /AppData/BIProd/ADS/RISK/ads_trade_risk_control_frequent_adress_result_dm/pt_d='${date,-15,yyyyMMdd}'
else
echo "The partition file does not exist"
fi


rm -f /data1/birisk/ads_trade_risk_control_frequent_adress_result_dm/UserAddress_'${date,-15,yyyyMMdd}'
