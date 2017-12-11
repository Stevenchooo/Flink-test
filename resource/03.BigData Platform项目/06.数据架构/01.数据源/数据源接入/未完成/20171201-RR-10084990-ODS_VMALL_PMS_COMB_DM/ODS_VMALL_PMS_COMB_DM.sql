CREATE EXTERNAL TABLE ODS_VMALL_PMS_COMB_DM(
id string comment '搭配销售ID',
carrier_id string comment '运营商ID',
channel_id string comment '渠道ID',
name string comment '名称',
status string comment '状态',
effective_date string comment '开始时间',
terminate_date string comment '结束时间',
create_date string comment '创建日期',
create_by string comment '创建人',
update_date string comment '更新日期',
update_by string comment '更新人',
sync_status string comment '同步状态')
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
   'hdfs://hacluster/AppData/BIProd/ODS/VMALL/ODS_VMALL_PMS_COMB_DM'
数据文件推送路径：/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PMS_COMB_DM
XML配置文件:
<FileToHDFS action="ODS_VMALL_PMS_COMB_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/VMallProd/odsdata/ODS_VMALL_PMS_COMB_DM/*_ODS_VMALL_PMS_COMB_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_VMALL_PMS_COMB_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VMALL/ODS_VMALL_PMS_COMB_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>