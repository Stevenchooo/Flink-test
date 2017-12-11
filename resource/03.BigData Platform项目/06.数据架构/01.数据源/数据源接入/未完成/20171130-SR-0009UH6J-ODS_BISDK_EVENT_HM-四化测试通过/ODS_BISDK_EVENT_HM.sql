CREATE EXTERNAL TABLE ODS_BISDK_EVENT_HM(
`data` string comment 'HiAnalytics SDK上报的加密消息',
ip string comment 'SDK上报消息时端侧自带的加密后ip',
`time` string comment 'SDK上报消息时采集服务器自动生成的时间戳')
PARTITIONED BY ( pt_d string,pt_h string)
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
   'hdfs://hacluster/AppData/BIProd/ODS/BISDK/ODS_BISDK_EVENT_HM'
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HIANALYSIS_SDK_DATA_HM
XML配置文件:
<FileToHDFS action="ODS_BISDK_EVENT_HM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HIANALYSIS_SDK_DATA_HM/*ODS_BISDK_EVENT_HM*.log</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\001</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_BISDK_EVENT_HM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>BISDK/ODS_BISDK_EVENT_HM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d,pt_h</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>