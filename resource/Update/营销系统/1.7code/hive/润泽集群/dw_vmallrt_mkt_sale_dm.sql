# ----------------------------------------------------------------------------
#  File Name: dw_vmallrt_mkt_sale_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2012.All rights reserved.
#  Purpose: 营销vmall数据和BI数据
#  Describe: 
#  Input:  dim_vmallrt_mkt_order2cid_ds,dim_vmallrt_mkt_order2cid_ds,
#          dw_vmall2_reserved_user_stat_dm 
#          
#
#
#  Output: dw_vmallrt_mkt_sale_dm
#  Author: s00359263
#  Creation Date:  2016-03-11
#  Modified history: 
# ----------------------------------------------------------------------------

hive -e "
use biwarehouse;
CREATE TABLE IF NOT EXISTS dw_vmallrt_mkt_sale_dm 
(
    cid                       int,
    plat                      int,
    order_count               int,
    order_pay_count           int,
    order_pay_cancel_cnt      int,
    order_return_cnt          int,
    order_pay_price_count     double,
    order_pay_cancel_amout    double,
    return_amount             double,
    reserver_uv               int,
    order_count_bi            int,
    order_pay_count_bi        int,
    order_pay_cancel_cnt_bi   int,
    order_return_cnt_bi       int,
    order_pay_price_count_bi  double,
    order_pay_cancel_amout_bi double,
    return_amount_bi          double
)
PARTITIONED BY (pt_d STRING)
ROW format delimited
FIELDS terminated by '\001'
LINES terminated by '\n'
LOCATION '$hadoopuser/data/DIM/vmall/dw_vmallrt_mkt_sale_dm';


INSERT OVERWRITE TABLE dw_vmallrt_mkt_sale_dm
PARTITION (pt_d = '$date')
select 
    COALESCE(a.cid,b.cid,c.cid)                 as cid,
    cast(0 as int)                             as plat,
    a.order_count                              as order_count,
    a.order_pay_count                          as order_pay_count,
    a.order_pay_cancel_cnt                     as order_pay_cancel_cnt,
    a.order_return_cnt                         as order_return_cnt,
    round(a.order_pay_price_count,2)           as order_pay_price_count,
    round(a.order_pay_cancel_amout,2)          as order_pay_cancel_amout,
    round(a.return_amount,2)                   as return_amount,
    c.reserver_uv                              as reserver_uv,
    b.order_count_bi                           as order_count_bi,
    b.order_pay_count_bi                       as order_pay_count_bi,
    b.order_pay_cancel_cnt_bi                  as order_pay_cancel_cnt_bi,
    b.return_order_cnt_bi                      as order_return_cnt_bi,
    round(b.order_pay_price_count_bi,2)        as order_pay_price_count_bi,
    round(b.order_pay_cancel_amout_bi,2)       as order_pay_cancel_amout_bi,
    round(b.return_amount_bi,2)                as return_amount_bi
from
(
    select 
        cast(cid as int)             as cid,
        sum(order_count)             as order_count,
        sum(order_pay_count)         as order_pay_count,
        sum(order_pay_cancel_cnt)    as order_pay_cancel_cnt,
        sum(order_return_cnt)        as order_return_cnt,
        sum(order_pay_price_count)   as order_pay_price_count,
        sum(order_pay_cancel_amout)  as order_pay_cancel_amout,
        sum(return_amount)           as return_amount
    from dw_vmall2_channal_popularize_dm 
    where pt_d='$date' and cid is not null
    group by cid
) a
FULL OUTER JOIN  
(
    select            
        cast(fk.cid as int)                                                                                                              as cid,
        count( distinct if(to_date(fk.order_date)='$date_ep',fk.order_code,null))                                                        as order_count_bi,
        count(distinct if(fk.payment_status='1' and to_date(fk.payment_date)='$date_ep',fk.order_code,null))                             as order_pay_count_bi,
        count(distinct (if(fk.payment_status='1' and fk.status = '8' and to_date(fk.cancel_date)='$date_ep',fk.order_code,null)))        as order_pay_cancel_cnt_bi,
        COUNT(DISTINCT IF(fk.return_amount IS NULL,NULL, fk.order_code))                                                                 as return_order_cnt_bi,
        sum(if(fk.payment_status='1'  and to_date(fk.payment_date)='$date_ep',cast(fk.payment_amount as double),0))                      as order_pay_price_count_bi,
        sum(if(fk.payment_status='1' and fk.status = '8' and to_date(fk.cancel_date)='$date_ep',cast(fk.payment_amount as double),null)) as order_pay_cancel_amout_bi,
        SUM(IF(fk.return_amount IS NULL, 0, cast(fk.return_amount as double)))                                                           as return_amount_bi
    from
    (
        select 
            f.cid               as cid,
            f.order_code        as order_code, 
            f.order_date        as order_date,
            f.payment_date      as payment_date,
            f.cancel_date       as cancel_date,
            f.payment_status    as payment_status,
            f.status            as status,
            f.payment_amount    as payment_amount,
            k.return_amount     as return_amount
        from
        (
            select
                cid,
                order_code, 
                order_date,
                payment_date,
                cancel_date,
                payment_status,
                status,
                payment_amount
            from dim_vmallrt_mkt_order2cid_ds 
            where 
                cid is not null and (
                TO_DATE(payment_date)='$date_ep' OR 
                TO_DATE(cancel_date)='$date_ep'  OR 
                TO_DATE(order_date)='$date_ep')
        ) f
        LEFT OUTER JOIN
        (
            SELECT 
                a.order_code         as order_code,
                sum(a.return_amount) as return_amount
            FROM dim_vmall2_return_exchenge_ds a
            JOIN dim_vmallrt_mkt_order2cid_ds b
            ON a.order_code = b.order_code
            WHERE TO_DATE(a.ofs_storage_time)='$date_ep'
            group by a.order_code
        ) k
        on f.order_code = k.order_code 
    )  fk
    group by fk.cid
)  b
on a.cid = b.cid
FULL OUTER JOIN 
(
    select 
        sum(reserved_users) as reserver_uv, 
        cast(reserved_cid as int)        as cid 
    from dw_vmall2_reserved_user_stat_dm
    where pt_d='$date'
    group by reserved_cid
) c
on a.cid = c.cid
;

"
