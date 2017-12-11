CREATE EXTERNAL TABLE ODS_DEV_CLOUD_FOLDER_DELETE_FOLDER_DM(
`time` string comment '时间',
deviceId string comment '用户终端设备号',
mcc string comment '移动国家码',
phoneType string comment '机型',
folderType int comment '文件夹类型',
`timestamp` string comment '打点时间戳',
interfaceVersion string comment '接口版本号',
clientVersion string comment '客户端版本号')
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
   'hdfs://hacluster/AppData/BIProd/ODS/DEV/ODS_DEV_CLOUD_FOLDER_DELETE_FOLDER_DM'
数据文件推送路径：/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_CLOUD_FOLDER_DELETE_FOLDER_DM
XML配置文件:
<FileToHDFS action="ODS_DEV_CLOUD_FOLDER_DELETE_FOLDER_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/OpenAlliance/odsdata/ODS_DEV_CLOUD_FOLDER_DELETE_FOLDER_DM/*_ODS_DEV_CLOUD_FOLDER_DELETE_FOLDER_DM_*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_DEV_CLOUD_FOLDER_DELETE_FOLDER_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>DEV/ODS_DEV_CLOUD_FOLDER_DELETE_FOLDER_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>