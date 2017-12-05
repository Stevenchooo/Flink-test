# ----------------------------------------------------------------------------
#  File Name: dim_vmallrt_mkt_order2cid_ds.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2012.All rights reserved.
#  Purpose: 营销vmall数据和BI数据，逆向推算
#  Describe:  
#  Input:  dim_vmall2_tbl_order_ds,ods_vmall_hi_data_dm,ods_vmall_dj_dm
#
#
#  Output: dim_vmallrt_mkt_order2cid_ds
#  Author: s00359263
#  Creation Date:  2016-03-11
#  Modified history: 
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
set hive.exec.compress.output=true;
set hive.exec.parallel=true;
set mapreduce.output.fileoutputformat.compress.codec=com.hadoop.compression.lzo.LzopCodec;
use biwarehouse;


# 建立临时表

CREATE TABLE IF NOT EXISTS tmp_vmallrt_mkt_s00359263_order2cid 
(
    order_id            bigint,
    s_id                string,
    order_code          string,
    type                string,      
    ceate_date          string,
    order_date          string,
    status              string,      
    payment_amount      string,
    payment_date        string,
    payment_status      string,
    repair_amount       string,
    user_id             string,      
    cust_ip             string,      
    petal_pay           string,
    petal_return_status string,
    cancel_date         string,
    payment_type        string,
    payment_method      string,
    channel             string,      
    plat                string,      
    clicktime           string,
    sid                 string,      
    cid                 string
)
;

     
INSERT OVERWRITE TABLE tmp_vmallrt_mkt_s00359263_order2cid 
SELECT
    f.order_id                                                   AS  order_id               ,
    SUBSTR(Max(CONCAT(k.clicktime, f.s_id)), 20)                 AS  s_id                   ,
    SUBSTR(Max(CONCAT(k.clicktime, f.order_code)), 20)           AS  order_code             ,
    SUBSTR(Max(CONCAT(k.clicktime, f.type)), 20)                 AS  type                   ,
    SUBSTR(Max(CONCAT(k.clicktime, f.ceate_date)), 20)           AS  ceate_date             ,
    SUBSTR(Max(CONCAT(k.clicktime, f.order_date)), 20)           AS  order_date             ,
    SUBSTR(Max(CONCAT(k.clicktime, f.status)), 20)               AS  status                 ,
    SUBSTR(Max(CONCAT(k.clicktime, f.payment_amount)), 20)       AS  payment_amount         ,
    SUBSTR(Max(CONCAT(k.clicktime, f.payment_date)), 20)         AS  payment_date           ,
    SUBSTR(Max(CONCAT(k.clicktime, f.payment_status)), 20)       AS  payment_status         ,
    SUBSTR(Max(CONCAT(k.clicktime, f.repair_amount)), 20)        AS  repair_amount          ,
    SUBSTR(Max(CONCAT(k.clicktime, f.user_id)), 20)              AS  user_id                ,
    SUBSTR(Max(CONCAT(k.clicktime, f.cust_ip)), 20)              AS  cust_ip                ,
    SUBSTR(Max(CONCAT(k.clicktime, f.petal_pay)), 20)            AS  petal_pay              ,
    SUBSTR(Max(CONCAT(k.clicktime, f.petal_return_status)), 20)  AS  petal_return_status    ,
    SUBSTR(Max(CONCAT(k.clicktime, f.cancel_date)), 20)          AS  cancel_date            ,
    SUBSTR(Max(CONCAT(k.clicktime, f.payment_type)), 20)         AS  payment_type           ,
    SUBSTR(Max(CONCAT(k.clicktime, f.payment_method)), 20)       AS  payment_method         ,
    SUBSTR(Max(CONCAT(k.clicktime, f.channel)), 20)              AS  channel                ,
    SUBSTR(Max(CONCAT(k.clicktime, f.plat)), 20)                 AS  plat                   ,
    MAX(k.clicktime)                                             AS  clicktime              ,
    SUBSTR(Max(CONCAT(k.clicktime, k.sid)), 20)                  AS  sid                    ,
    SUBSTR(Max(CONCAT(k.clicktime, k.cid)), 20)                  AS  cid
    
FROM
(
    SELECT
        t2.order_id                                                      AS  order_id               ,
        SUBSTR(Max(CONCAT(t0.server_time, t0.s_id)), 20)                 AS  s_id                   ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.order_code)), 20)           AS  order_code             ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.type)), 20)                 AS  type                   ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.ceate_date)), 20)           AS  ceate_date             ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.order_date)), 20)           AS  order_date             ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.status)), 20)               AS  status                 ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.payment_amount)), 20)       AS  payment_amount         ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.payment_date)), 20)         AS  payment_date           ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.payment_status)), 20)       AS  payment_status         ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.repair_amount)), 20)        AS  repair_amount          ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.user_id)), 20)              AS  user_id                ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.cust_ip)), 20)              AS  cust_ip                ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.petal_pay)), 20)            AS  petal_pay              ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.petal_return_status)), 20)  AS  petal_return_status    ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.cancel_date)), 20)          AS  cancel_date            ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.payment_type)), 20)         AS  payment_type           ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.payment_method)), 20)       AS  payment_method         ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.channel)), 20)              AS  channel                ,
        SUBSTR(Max(CONCAT(t0.server_time, t2.plat)), 20)                 AS  plat                   ,
        MAX(t0.server_time)                                              AS  server_time
    FROM
    (
      SELECT
          order_id               ,
          order_code             ,
          type                   ,
          ceate_date             ,
          order_date             ,
          status                 ,
          payment_amount         ,
          payment_date           ,
          payment_status         ,
          repair_amount          ,
          user_id                ,
          cust_ip                ,
          petal_pay              ,
          petal_return_status    ,
          cancel_date            ,
          payment_type           ,
          payment_method         ,
          channel                ,
          plat
      FROM dim_vmall2_tbl_order_ds
      WHERE type != 3 AND (TO_DATE(payment_date)='$date_ep' OR TO_DATE(cancel_date)='$date_ep' OR TO_DATE(order_date)='$date_ep') AND channel='B2C'
    )t2
    LEFT OUTER JOIN
    (
      SELECT
              s_id                                               as s_id,
              pt_d                                               as pt_d,
              regexp_extract(cvar2, '(?<=\"uid\",\")\\\\d+(?=\")' ,0)  as uid,
              regexp_extract(cvar1, '(?<=\"cid\",\")\\\\d+(?=\")' ,0)  as cid,
              server_time
      FROM ods_vmall_hi_data_dm
      WHERE pt_d='$date'
          AND regexp_extract(cvar2, '(?<=\"uid\",\")\\\\d+(?=\")' ,0) <>''
          AND regexp_extract(cvar2, '(?<=\"uid\",\")\\\\d+(?=\")' ,0) is not null
          AND regexp_extract(cvar1, '(?<=\"cid\",\")\\\\d+(?=\")' ,0) <> ''
          AND regexp_extract(cvar1, '(?<=\"cid\",\")\\\\d+(?=\")' ,0) is not null
          AND lower(idsite) in ('www.vmall.com','mm.vmall.com','sale.vmall.com','m.vmall.com','mt.vmall.com','msale.vmall.com','mw.vmall.com','ma.vmall.com','asale.vmall.com')
    )t0
    on t0.uid = t2.user_id
    WHERE t0.uid is not null AND t2.order_date>t0.server_time
    GROUP BY t2.order_id
) f
JOIN 
(
    SELECT
    *
    FROM 
    ods_vmall_dj_dm
    WHERE
    pt_d='$date' AND cid IS NOT NULL
    
) k
ON f.s_id = k.cookieid
WHERE k.clicktime<f.server_time
GROUP BY f.order_id
;

CREATE TABLE IF NOT EXISTS dim_vmallrt_mkt_order2cid_ds 
(
    order_id            bigint,
    s_id                string,
    order_code          string,
    type                string,      
    ceate_date          string,
    order_date          string,
    status              string,      
    payment_amount      string,
    payment_date        string,
    payment_status      string,
    repair_amount       string,
    user_id             string,      
    cust_ip             string,      
    petal_pay           string,
    petal_return_status string,
    cancel_date         string,
    payment_type        string,
    payment_method      string,
    channel             string,      
    plat                string,      
    clicktime           string,
    sid                 string,      
    cid                 string
)
ROW format delimited
FIELDS terminated by '\001'
LINES terminated by '\n'
LOCATION '$hadoopuser/data/DIM/vmall/dim_vmallrt_mkt_order2cid_ds';

    
INSERT OVERWRITE TABLE dim_vmallrt_mkt_order2cid_ds
select 
    t.order_id            ,
    t.s_id                ,
    t.order_code          ,
    t.type                ,      
    t.ceate_date          ,
    t.order_date          ,
    t.status              ,      
    t.payment_amount      ,
    t.payment_date        ,
    t.payment_status      ,
    t.repair_amount       ,
    t.user_id             ,      
    t.cust_ip             ,      
    t.petal_pay           ,
    t.petal_return_status ,
    t.cancel_date         ,
    t.payment_type        ,
    t.payment_method      ,
    t.channel             ,      
    t.plat                ,      
    t.clicktime           ,
    t.sid                 ,      
    t.cid                 
from                                 
(
    select 
        t1.* 
    from  
        dim_vmallrt_mkt_order2cid_ds t1
    left outer join tmp_vmallrt_mkt_s00359263_order2cid t2
    on t1.order_id = t2.order_id
    where t2.order_id is null  
    
    union all
    
    select 
        t2.order_id            ,
        t2.s_id                ,
        t2.order_code          ,
        t2.type                ,
        t2.ceate_date          ,
        COALESCE(t2.order_date,t1.order_date)  as order_date,
        t2.status              ,
        t2.payment_amount      ,
        COALESCE(t2.payment_date,t1.payment_date)  as payment_date,
        t2.payment_status      ,
        t2.repair_amount       ,
        t2.user_id             ,
        t2.cust_ip             ,
        t2.petal_pay           ,
        t2.petal_return_status ,
        COALESCE(t2.cancel_date,t1.cancel_date)  as cancel_date,
        t2.payment_type        ,
        t2.payment_method      ,
        t2.channel             ,
        t2.plat                ,
        t2.clicktime           ,
        t2.sid                 ,
        COALESCE(t1.cid,t2.cid)  as cid
    from  dim_vmallrt_mkt_order2cid_ds t1
    left outer join tmp_vmallrt_mkt_s00359263_order2cid t2
    on t1.order_id = t2.order_id
    where t2.order_id is not null 
    
    union all
    
    select 
        t1.* 
    from  
        tmp_vmallrt_mkt_s00359263_order2cid t1
    left outer join  dim_vmallrt_mkt_order2cid_ds t2
    on t1.order_id = t2.order_id
    where t2.order_id is null 
) t;
"

