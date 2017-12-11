CREATE EXTERNAL TABLE ODS_VMALL_PMS_BUNDLE_DM(
id string comment '捆绑套餐ID',
name string comment '套餐名',
order_num string comment '排序',
is_use_coupon int comment '是否使用优惠券',
is_join_promotion int comment '是否参加促销',
create_by string comment '创建人',
create_time string comment '创建时间',
update_by string comment '修改人',
update_time string comment '修改时间',
sync_status int comment '同步状态',
carrier_id string comment '运营商ID',
channel_id string comment '渠道ID',
status int comment '状态',
original_price string comment '原价格',
weight string comment '套餐捆绑重量',
discount_total_amount string comment '折扣总金额',
online_status int comment '在线状态',
sale_by_enterprisechar int comment '是否通过内购渠道销售',
code string comment '捆绑套餐code')
COMMENT '套餐表'
PARTITIONED BY ( pt_d string )
ROW FORMAT SERDE 
'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
WITH SERDEPROPERTIES ( 
   'field.delim'='\0001', 
   'line.delim'='\n',
   'serialization.format'='\0001')
STORED AS INPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat' 
LOCATION 
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_PMS_BUNDLE_DM'
数据文件推送路径：/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PMS_BUNDLE_DM
XML配置文件:
<FileToHDFS action="ODS_VMALL_PMS_BUNDLE_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PMS_BUNDLE_DM/*_ODS_VMALL_PMS_BUNDLE_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_VMALL_PMS_BUNDLE_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VMALL/ODS_VMALL_PMS_BUNDLE_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>