CREATE EXTERNAL TABLE ODS_HIMOVIE_HVS_REL_CHANNEL_ATTRIBUTE_DM(
channelID string comment '频道编号',
SUBNETID string comment '子网ID',
RATINGID string comment '父母字ID',
STATUS int comment '频道状态',
isDelete int comment '已经删除')
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
   'hdfs://hacluster/AppData/BIProd/ODS/HIMOVIE/ODS_HIMOVIE_HVS_REL_CHANNEL_ATTRIBUTE_DM'
数据文件推送路径：/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_CHANNEL_ATTRIBUTE_DM
XML配置文件:
<FileToHDFS action="ODS_HIMOVIE_HVS_REL_CHANNEL_ATTRIBUTE_DM">
<!-- 文件配置信息 -->
<Fileconf>ODS_Hota</Fileconf>
<!-- 输入文件配置-->
    <MultiInputFileConf>
        <!-- 输入文件列表-->
        <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_HIMOVIE_HVS_REL_CHANNEL_ATTRIBUTE_DM*.txt</InputFileList>
        <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
        <InputFileMinCount>1</InputFileMinCount>
        <!-- 等待输入时间（分钟） -->
        <WaitInputMinutes>120</WaitInputMinutes>
        <!-- 文件字段的分割符，默认使用\0x01 -->
        <Separator>\|</Separator>
    </MultiInputFileConf>
<!-- 表名 -->
<Tablename>ODS_HIMOVIE_HVS_REL_CHANNEL_ATTRIBUTE_DM</Tablename>
<!-- 表存储位置 -->
<Tablelocation>HIMOVIE/ODS_HIMOVIE_HVS_REL_CHANNEL_ATTRIBUTE_DM</Tablelocation>
<!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
<Partition>pt_d</Partition>
<CompressType>orc_zlib</CompressType>
</FileToHDFS>