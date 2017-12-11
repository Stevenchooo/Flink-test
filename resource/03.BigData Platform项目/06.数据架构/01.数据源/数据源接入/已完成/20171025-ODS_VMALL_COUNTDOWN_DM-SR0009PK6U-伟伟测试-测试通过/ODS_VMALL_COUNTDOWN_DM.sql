CREATE EXTERNAL TABLE ODS_VMALL_COUNTDOWN_DM(
id string comment '限时抢购活动ID',
name string comment '活动名称',
description string comment '活动描述',
effective_date string comment '开始时间',
terminate_date string comment '结束时间',
status int comment '状态 0:已创建；1:待审核 2:审核通过 3:审核未通过 4:已下线 5：已作废 默认0',
create_by string comment '创建人',
create_date string comment '创建时间',
basic_number string comment '抢购基数',
is_promotion int comment '是否参数促销活动 0：不参加 1：参加',
is_coupon int comment '是否使用优惠券 0：不使用 1：使用',
grades string comment '允许参加会员等级',
now_number string comment '已抢购数量',
promotion_word string comment '抢购促销语',
promocount_code string comment '限时抢购活动编码',
promotion_tag string comment '促销标签',
auditor string comment '审核人',
audit_date string comment '审核时间',
audit_opinion string comment '审核意见',
update_by string comment '最后更新人',
update_date string comment '最后更新时间')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_COUNTDOWN_DM'
数据文件推送路径：/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_COUNTDOWN_DM
XML配置文件:
<FileToHDFS action="ODS_VMALL_COUNTDOWN_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/VMALLProd/odsdata/ODS_VMALL_COUNTDOWN_DM/*_ODS_VMALL_COUNTDOWN_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_VMALL_COUNTDOWN_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VMALL/ODS_VMALL_COUNTDOWN_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>