CREATE EXTERNAL TABLE ODS_HISPACE_FEEDBACK_LOG_DM(
cus_id string comment 'deviceid',
app_id string comment '应用id',
device_type string comment '设备型号',
app_version string comment '应用版本',
crt_date string comment '发布时间',
complaints string comment '反馈内容')
COMMENT '用户反馈表'
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
   'hdfs://hacluster/AppData/BIProd/ODS/HISPACE/ODS_HISPACE_FEEDBACK_LOG_DM'
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HISPACE_FEEDBACK_LOG_DM
XML配置文件:
<FileToHDFS action="ODS_HISPACE_FEEDBACK_LOG_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HISPACE_FEEDBACK_LOG_DM/*_ODS_HISPACE_FEEDBACK_LOG_DM_*.lzo</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HISPACE_FEEDBACK_LOG_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HISPACE/ODS_HISPACE_FEEDBACK_LOG_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>