CREATE EXTERNAL TABLE ODS_HWREAD_FACTORY_CLICK_DM(
user_id string comment '用户I号',
channel_id string comment '渠道id',
inner_version string comment '内部版本号',
app_platform string comment '平台',
model string comment '手机型号',
ip string comment '用户IP地址',
imei string comment '设备IMEI',
`datetime` string comment '用户操作时间',
json string comment 'json字符串')
PARTITIONED BY ( pt_d string )
ROW FORMAT SERDE 
'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
WITH SERDEPROPERTIES ( 
   'field.delim'='\t', 
   'line.delim'='\n',
   'serialization.format'='\t')
STORED AS INPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat' 
LOCATION 
   'hdfs://hacluster/AppData/BIProd/ODS/HWREAD/ODS_HWREAD_FACTORY_CLICK_DM'
数据文件推送路径：/MFS/DataIn/hadoop-NJ/odsdata/ODS_HWREAD_FACTORY_CLICK_DM
XML配置文件:
<FileToHDFS action="ODS_HWREAD_FACTORY_CLICK_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/hadoop-NJ/odsdata/ODS_HWREAD_FACTORY_CLICK_DM/*_ODS_HWREAD_FACTORY_CLICK_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\t</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HWREAD_FACTORY_CLICK_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HWREAD/ODS_HWREAD_FACTORY_CLICK_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>