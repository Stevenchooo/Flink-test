# ----------------------------------------------------------------------------
#  File Name:   dim_core_trade_app_detail_info_dm.sql
#  Copyright(C)Huawei Technologies Co.,Ltd.1998-2016.All rights reserved.
#  Purpose: 支付结算平台的全量流水明细
#  Describe: 数据源增量推送，先取当天的最新数据入表，再取最新数据中不包含的数据入维表；再通过商户ID及应用ID获取流水的业务编码
#  Input：  ODS_TRADE_TRANSACTION_INFO_DM,ODS_TRADE_SETTLE_APP_DM,ODS_TRADE_SETTLE_MERCHANT_DM
#  Output： dim_core_trade_app_detail_info_dm
#  Author:  heyuanhong/h00201904
#  Creation Date:  2016-06-06
# ----------------------------------------------------------------------------

hive -e "
set mapreduce.job.queuename=QueueA;
set hive.exec.compress.output=false;
use biwarehouse;

CREATE EXTERNAL TABLE IF NOT EXISTS dim_core_trade_app_detail_info_dm 
(
    author_id               STRING COMMENT '商户ID',  
    dev_user_id             BIGINT COMMENT '开发者用户ID',
    trade_no                STRING COMMENT '订单号',
    app_id                  STRING COMMENT '应用包名', 
    application_id          STRING COMMENT '应用ID', 
    product_name            STRING COMMENT '商品名', 
    pay_money               DOUBLE COMMENT '支付金额', 
    received_money          STRING COMMENT '预付款或花币订单处理完毕时的余额(元)', 
    huawei_received_money   STRING COMMENT '华为实收金额',
    yeealipay_handlingfee   STRING COMMENT '手续费',
    developer_handlingfee   STRING COMMENT '开发者承担的手续费',
    huawei_handlingfee      STRING COMMENT '华为承担的手续费', 
    user_handlingfee        STRING COMMENT '消费者承担的手续费', 
    refund_money            STRING COMMENT '退款金额', 
    order_time              STRING COMMENT '下单时间', 
    trade_time              STRING COMMENT '交易完成时间', 
    order_no                STRING COMMENT '华为订单号', 
    trade_state             INT    COMMENT '0: 已付, 1: 已退, 2: 付款失败, 3: 退款失败, 4: 未支付, 5: 退款中',
    pay_type                STRING COMMENT '支付方式', 
    remarks                 STRING COMMENT '备注', 
    channel                 STRING COMMENT '支付渠道', 
    refund_time             STRING COMMENT '退款时间', 
    bank_id                 STRING COMMENT '详细支付类型为YeePay20时表示易宝实名系统',  
    sdk_channel             STRING COMMENT 'sdk渠道：0 自有应用, 1 智汇云渠道, 2 预装渠道, 3 游戏吧, 9x 无法结算的交易',  
    border_id               STRING COMMENT '易宝银行订单号',  
    request_id              STRING COMMENT '商户请求号',  
    imei                    STRING COMMENT '设备ID',  
    phone_no                STRING COMMENT '电话号码',  
    sdk_version             STRING COMMENT 'sdk版本信息',  
    device_type             STRING COMMENT '设备类型',  
    proportion              STRING COMMENT '分成比例信息',  
    user_id                 BIGINT COMMENT '统一帐号ID',  
    card_info               STRING COMMENT '支付卡信息',
    ullage                  STRING COMMENT '总耗损',
    hwtoll                  STRING COMMENT '华为承担的损耗部分',
    devtoll                 STRING COMMENT '开发者承担的损耗部分',  
    service_catalog         STRING COMMENT '服务目录',
    currency                STRING COMMENT '订单货币',
    ori_order_no            STRING COMMENT '原始华为订单号, 在退款情况下有效',
    type                    STRING COMMENT '业务类型, PURCHASE: 支付, REFUND: 退款',
    project_no              STRING COMMENT '项目编码'
)
PARTITIONED BY (pt_d STRING)
ROW FORMAT DELIMITED 
FIELDS TERMINATED BY '\001'
LINES TERMINATED BY '\n'
STORED AS ORC
LOCATION '/hadoop-NJ/data/common/DIM/dim_core_trade_app_detail_info_dm'
TBLPROPERTIES('orc.compress'='ZLIB');

INSERT OVERWRITE TABLE dim_core_trade_app_detail_info_dm
PARTITION(pt_d='$date')
SELECT
    t.author_id                        ,
    t.dev_user_id                      ,
    t.trade_no                         ,
    t.app_id                           ,
    t.application_id                   ,
    t.product_name                     ,
    t.pay_money                        ,
    t.received_money                   ,
    t.huawei_received_money            ,
    t.yeealipay_handlingfee            ,
    t.developer_handlingfee            ,
    t.huawei_handlingfee               ,
    t.user_handlingfee                 ,
    t.refund_money                     ,
    t.order_time                       ,
    t.trade_time                       ,
    t.order_no                         ,
    t.trade_state                      ,
    t.pay_type                         ,
    t.remarks                          ,
    t.channel                          ,
    t.refund_time                      ,
    t.bank_id                          ,
    t.sdk_channel                      ,
    t.border_id                        ,
    t.request_id                       ,
    revertDeviceId(t.imei) AS imei     ,
    t.phone_no                         ,
    t.sdk_version                      ,
    t.device_type                      ,
    t.proportion                       ,
    t.user_id                          ,
    t.card_info                        ,
    t.ullage                           ,
    t.hwtoll                           ,
    t.devtoll                          ,
    t.service_catalog                  ,
    t.currency                         ,
    t.ori_order_no                     ,
    t.type                             ,
    t.project_no
FROM
(
    SELECT
        author_id                , 
        t1.user_id AS dev_user_id, 
        trade_no                 , 
        app_id                   , 
        t1.application_id        , 
        product_name             , 
        pay_money                , 
        received_money           , 
        huawei_received_money    ,
        yeealipay_handlingfee    ,
        developer_handlingfee    ,
        huawei_handlingfee       , 
        user_handlingfee         , 
        refund_money             , 
        order_time               , 
        trade_time               , 
        order_no                 , 
        trade_state              , 
        pay_type                 , 
        remarks                  , 
        channel                  , 
        refund_time              , 
        bank_id                  ,  
        sdk_channel              ,  
        border_id                ,  
        request_id               ,  
        device_id  AS  imei      ,  
        phone_no                 ,  
        sdk_version              ,  
        device_type              ,  
        proportion               ,  
        up_account AS  user_id   ,  
        card_info                ,
        ullage                   ,
        hwtoll                   ,
        devtoll                  ,  
        service_catalog          ,
        currency                 ,
        ori_order_no             ,
        type                     ,
        COALESCE(IF(IsEmpty(t2.project_no), NULL, t2.project_no), t3.project_no) AS project_no
    FROM
    (
        SELECT *
        FROM ODS_TRADE_TRANSACTION_INFO_DM
        WHERE pt_d='$date'
    ) t1
    LEFT OUTER JOIN
    (
        SELECT
            user_id,
            application_id,
            project_no
        from ODS_TRADE_SETTLE_APP_DM
        where pt_d ='$date'
        group by user_id,application_id,project_no
    ) t2
    ON trim(t1.user_id) = trim(t2.user_id) AND trim(t1.application_id) = trim(t2.application_id)
    LEFT OUTER JOIN
    (   
        select
            user_id,
            project_no
        from ODS_TRADE_SETTLE_MERCHANT_DM
        where pt_d ='$date'
        group by user_id,project_no
    ) t3   
    ON trim(t1.user_id) = trim(t3.user_id)

    UNION ALL
    
    SELECT
        t1.author_id               , 
        t1.dev_user_id             , 
        t1.trade_no                , 
        t1.app_id                  , 
        t1.application_id          , 
        t1.product_name            , 
        t1.pay_money               , 
        t1.received_money          , 
        t1.huawei_received_money   ,
        t1.yeealipay_handlingfee   ,
        t1.developer_handlingfee   ,
        t1.huawei_handlingfee      , 
        t1.user_handlingfee        , 
        t1.refund_money            , 
        t1.order_time              , 
        t1.trade_time              , 
        t1.order_no                , 
        t1.trade_state             , 
        t1.pay_type                , 
        t1.remarks                 , 
        t1.channel                 , 
        t1.refund_time             , 
        t1.bank_id                 ,  
        t1.sdk_channel             ,  
        t1.border_id               ,  
        t1.request_id              ,  
        t1.imei                    ,  
        t1.phone_no                ,  
        t1.sdk_version             ,  
        t1.device_type             ,  
        t1.proportion              ,  
        t1.user_id                 ,  
        t1.card_info               ,
        t1.ullage                  ,
        t1.hwtoll                  ,
        t1.devtoll                 ,  
        t1.service_catalog         ,
        t1.currency                ,
        t1.ori_order_no            ,
        t1.type                    ,
        t1.project_no
    FROM
    (
        SELECT *
        FROM dim_core_trade_app_detail_info_dm
        WHERE pt_d ='$last_date'
        
    ) t1
    LEFT OUTER JOIN
    (
        SELECT * 
        FROM ODS_TRADE_TRANSACTION_INFO_DM 
        WHERE pt_d='$date'
    ) t2
    ON t1.trade_no=t2.trade_no AND t1.order_no=t2.order_no
    WHERE ISNULL(t2.trade_no) AND ISNULL(t2.order_no)
)t;
"