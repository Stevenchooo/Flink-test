CREATE EXTERNAL TABLE ODS_VMALL_PROMOTION_RULE_DM(
rule_code string comment '促销规则编码',
name string comment '促销规则名称',
state int comment '状态',
description string comment '促销规则描述',
promotion_code string comment '促销活动编码',
type int comment '订单类型',
order_price_min string comment '订单最小金额',
order_price_max string comment '订单最大金额',
apply_sku_code string comment '参与商品SkuCode',
not_apply_sku_code string comment '不参与商品SkuCode',
user_grade_min string comment '会员最低级别编码',
is_real_name_auth int comment '是否实名认证',
is_use_coupon int comment '是否允许使用优惠券',
create_date string comment '促销规则创建日期',
content_type string comment '优惠类型',
content_value string comment '优惠内容值',
send_score int comment '订单是否赠送积分',
promotion_tag string comment '促销标签')
PARTITIONED BY ( pt_d string )
ROW FORMAT SERDE 
'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
WITH SERDEPROPERTIES ( 
   'field.delim'='\u0001', 
   'line.delim'='\n',
   'serialization.format'='\u0001')
STORED AS INPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat' 
LOCATION 
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_PROMOTION_RULE_DM'
数据文件推送路径：/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PROMOTION_RULE_DM
XML配置文件:
<FileToHDFS action="ODS_VMALL_PROMOTION_RULE_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PROMOTION_RULE_DM/*_ODS_VMALL_PROMOTION_RULE_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_VMALL_PROMOTION_RULE_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VMALL/ODS_VMALL_PROMOTION_RULE_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>