CREATE EXTERNAL TABLE ODS_VMALL_CHEAP_BUY_DM(
id string comment '优惠购活动主键ID',
cheap_buy_code string comment '活动编码',
name string comment '活动名称',
status int comment '活动状态',
sale_portal string comment '可使用销售前端',
member_grade string comment '允许参加会员等级',
is_use_coupon int comment '是否允许使用优惠券',
link string comment '推广链接',
promo_word string comment '促销语',
description string comment '活动描述',
start_time string comment '活动起始时间',
end_time string comment '活动结束时间',
create_by string comment '活动创建人',
create_date string comment '活动创建时间',
update_by string comment '活动更新人',
update_date string comment '活动更新时间',
audit_by string comment '活动审核人',
audit_date string comment '活动审核时间',
audit_opinion string comment '活动审核意见')
PARTITIONED BY ( 未知 )
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_CHEAP_BUY_DM'
数据文件推送路径：/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_CHEAP_BUY_DM
XML配置文件:
<FileToHDFS action="ODS_VMALL_CHEAP_BUY_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_CHEAP_BUY_DM/*_ODS_VMALL_CHEAP_BUY_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_VMALL_CHEAP_BUY_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VMALL/ODS_VMALL_CHEAP_BUY_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>