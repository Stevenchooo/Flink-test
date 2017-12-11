CREATE EXTERNAL TABLE ODS_HWMOVIE_CONTENT_OPERATE_LOG_DM(
`timeStamp` string comment '时间',
userId string comment '用户Id',
clientIP string comment '客户端IP',
deviceType string comment '设备类型',
deviceId string comment '设备标示Id',
appId string comment '应用Id',
packageName string comment '应用包名',
appVersion string comment '应用版本号',
clientOS string comment '客户端操作系统',
action string comment '用户行为',
result string comment '响应码',
responseTime int comment '响应耗时',
appendInfo string comment '扩展字段',
beId string comment '默认为华为视频中国区运营渠道标识',
timeStampUTC string comment 'yyyyMMDDHHMMSS')
PARTITIONED BY ( pt_d string )
ROW FORMAT SERDE 
'org.apache.hadoop.hive.ql.io.orc.OrcSerde' 
WITH SERDEPROPERTIES ( 
   'field.delim'='|', 
   'line.delim'='\n',
   'serialization.format'='|')
STORED AS INPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcInputFormat' 
OUTPUTFORMAT 
   'org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat' 
LOCATION 
   'hdfs://hacluster/AppData/BIProd/ODS/VIDEO/ODS_HWMOVIE_CONTENT_OPERATE_LOG_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_CONTENT_OPERATE_LOG_DM
XML配置文件:
<FileToHDFS action="ODS_HWMOVIE_CONTENT_OPERATE_LOG_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HWMOVIE_CONTENT_OPERATE_LOG_DM/*_ODS_HWMOVIE_CONTENT_OPERATE_LOG_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HWMOVIE_CONTENT_OPERATE_LOG_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>VIDEO/ODS_HWMOVIE_CONTENT_OPERATE_LOG_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>